/*
 * Copyright 2021-2025 VastGui
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ave.vastgui.tools.utils.download

import com.ave.vastgui.tools.utils.download.core.DownloadEvent
import com.ave.vastgui.tools.utils.download.core.DownloadResult
import com.ave.vastgui.tools.utils.download.interfaces.OnDownloadListener
import com.ave.vastgui.tools.utils.getFileMD5
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.internal.closeQuietly
import java.io.File
import java.io.IOException
import java.util.concurrent.CancellationException
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.resumeWithException
import kotlin.properties.Delegates

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/7/24
// Documentation: https://sakurajimamaii.github.io/AVE-DOC/documents/tools/core-topics/connectivity/download/download/
// Reference: https://github.com/Heart-Beats/Downloader/blob/master/downloader/src/main/java/com/hl/downloader/SubDownloadTask.kt

// https://github.com/square/okhttp/blob/master/okhttp-coroutines/src/main/kotlin/okhttp3/coroutines/ExecuteAsync.kt
internal suspend fun Call.executeAsync(): Response = suspendCancellableCoroutine { continuation ->
    continuation.invokeOnCancellation {
        this.cancel()
    }
    this.enqueue(
        object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                continuation.resumeWithException(e)
            }

            override fun onResponse(call: Call, response: Response) {
                continuation.resume(response) { cause, _, _ ->
                    response.closeQuietly()
                }
            }
        }
    )
}

/**
 * Download task.
 *
 * @since 1.5.2
 */
class DownloadTask internal constructor(
    val client: OkHttpClient,
    val url: String,
    val file: File,
    private val maxCoreCount: Int,
    private val md5: String?,
    val listener: OnDownloadListener?,
) : CoroutineScope {

    private val job = SupervisorJob()
    private val handler = CoroutineExceptionHandler { _, cause ->
        listener?.onFailure(DownloadResult.Failure(cause))
    }

    override val coroutineContext: CoroutineContext = job + Dispatchers.IO + handler

    var currentEvent: DownloadEvent = DownloadEvent.Init
        private set

    private val _eventFlow = MutableSharedFlow<DownloadEvent>(1)
    val eventFlow: SharedFlow<DownloadEvent>
        get() = _eventFlow.asSharedFlow()

    private var length: Long by Delegates.notNull()

    private val subTasks: MutableList<DownloadSubTask> = ArrayList()

    internal fun onDownload() {
        if (isActive) {
            val progress = subTasks.fold(0f) { sum, bean ->
                sum + if (bean.startPos == bean.endPos) Float.NaN else bean.completeSize.toFloat()
            }
            val download = if (progress.isNaN()) {
                DownloadResult.Download()
            } else {
                DownloadResult.Download(progress, length.toFloat())
            }
            listener?.onDownload(download)
        }
    }

    internal fun onSuccess() {
        if (!subTasks.all { it.isCompleted }) return
        if (null == md5) {
            listener?.onSuccess(DownloadResult.Success(file))
            return
        }
        if (md5 == getFileMD5(file)) {
            listener?.onSuccess(DownloadResult.Success(file))
        } else {
            val e = RuntimeException(null, RuntimeException("File MD5($md5) verification failed."))
            listener?.onFailure(DownloadResult.Failure(e))
        }
        job.cancel()
    }

    /**
     * Start download.
     *
     * @since 1.5.2
     */
    fun start() {
        launch {
            if (isValidEvent(DownloadEvent.Start)) {
                if (file.exists()) {
                    throw IOException("${file.absoluteFile} is already exists.")
                } else if (!file.createNewFile()) {
                    throw IOException("${file.absoluteFile} create failed.")
                }

                if (subTasks.isEmpty()) {
                    length = tryToGetDownloadSize()
                    if (UNKNOWN_LENGTH == length) {
                        val bean = DownloadSubTask(this@DownloadTask)
                        subTasks.add(bean)
                    } else {
                        val remainder = length % maxCoreCount
                        val subLength = (length - remainder) / maxCoreCount
                        for (index in 0 until maxCoreCount) {
                            val start = index * subLength
                            val end = (index + 1) * subLength + if (index == maxCoreCount - 1) remainder else 0 - 1
                            val bean = DownloadSubTask(this@DownloadTask, start, end)
                            subTasks.add(bean)
                        }
                    }
                }

                if (_eventFlow.tryEmit(DownloadEvent.Start)) {
                    currentEvent = DownloadEvent.Start
                }
            }
        }
    }

    /**
     * Pause download.
     *
     * @since 1.5.2
     */
    fun pause() {
        if (isValidEvent(DownloadEvent.Pause) && _eventFlow.tryEmit(DownloadEvent.Pause)) {
            currentEvent = DownloadEvent.Pause
        }
    }

    /**
     * Resume download.
     *
     * @since 1.5.2
     */
    fun resume() {
        if (isValidEvent(DownloadEvent.Resume) && _eventFlow.tryEmit(DownloadEvent.Resume)) {
            currentEvent = DownloadEvent.Resume
        }
    }

    /**
     * Terminate download.
     *
     * @since 1.5.2
     */
    fun terminate(exception: CancellationException? = null) {
        if (isValidEvent(DownloadEvent.Termination) && _eventFlow.tryEmit(DownloadEvent.Termination)) {
            currentEvent = DownloadEvent.Termination
            job.cancel(exception)
            listener?.onTerminate()
        }
    }

    /**
     * Terminate download.
     *
     * @since 1.5.2
     */
    fun terminate(message: String, cause: Throwable? = null) {
        if (isValidEvent(DownloadEvent.Termination) && _eventFlow.tryEmit(DownloadEvent.Termination)) {
            currentEvent = DownloadEvent.Termination
            job.cancel(message, cause)
            listener?.onTerminate()
        }
    }

    /** @since 1.5.2 */
    private suspend fun tryToGetDownloadSize(): Long = withContext(coroutineContext) {
        var length = UNKNOWN_LENGTH
        val request = Request.Builder().url(url).build()
        runCatching {
            length = client.newCall(request)
                .executeAsync()
                .header("content-length", UNKNOWN_LENGTH.toString())!!.toLong()
        }
        return@withContext length
    }

    /**
     * Compare the priority of [DownloadEvent] . If the [event] priority is
     * greater than [currentEvent] , returns true and updates [currentEvent] to
     * [event], otherwise returns false.
     */
    private fun isValidEvent(event: DownloadEvent): Boolean =
        currentEvent <= event

    companion object {
        const val UNKNOWN_LENGTH = -1L

        fun createNewTask(settings: Builder.() -> Unit): DownloadTask {
            return Builder().also(settings).build()
        }
    }

    class Builder internal constructor() {

        private var client: OkHttpClient = OkHttpClient()
        private var url: String by Delegates.notNull()
        private var file: File by Delegates.notNull()
        private var md5: String? = null
        private var listener: OnDownloadListener? = null

        /**
         * Set download client.
         *
         * @param client
         */
        fun setClient(client: OkHttpClient) {
            this.client = client
        }

        /** Set download url. */
        fun setDownloadUrl(url: String) = apply {
            this.url = url
        }

        /** Set the download file. */
        fun setFile(file: File) = apply {
            this.file = file
        }

        /** Set the event listener for the download. */
        fun setListener(listener: OnDownloadListener) = apply {
            this.listener = listener
        }

        /** Set the md5 value of the file for verification */
        fun setMD5(md5: String) = apply {
            this.md5 = md5
        }

        /** Build the download task. */
        fun build() = DownloadTask(client, url, file, 2, md5, listener)

        /**
         * Get file save name for url.
         *
         * @return app-debug.apk as the return value if the link is
         * [https://github.com/SakurajimaMaii/BluetoothDemo/blob/master/app-debug.apk](#)
         */
        private fun getNameFromUrl(url: String): String {
            return url.substring(url.lastIndexOf("/") + 1)
        }

    }

}