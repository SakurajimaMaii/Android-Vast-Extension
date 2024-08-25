/*
 * Copyright 2021-2024 VastGui
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

import com.ave.vastgui.core.text.safeToLong
import com.ave.vastgui.tools.manager.filemgr.FileMgr
import com.ave.vastgui.tools.utils.getFileMD5
import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.BufferedInputStream
import java.io.File
import java.io.IOException
import java.io.RandomAccessFile
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/7/24
// Documentation: https://ave.entropy2020.cn/documents/tools/core-topics/connectivity/download/download/
// Reference: https://github.com/Heart-Beats/Downloader/blob/master/downloader/src/main/java/com/hl/downloader/SubDownloadTask.kt

/**
 * Download task.
 *
 * @since 0.5.2
 */
class DLTask internal constructor(private val config: DLTaskConfig) :
    Callback {

    private var contentLength: Long = 0L
    private val downloadUrl: String
        get() = config.downloadUrl
    private val startPos: Long
        get() = dlBean.startPos ?: 0L
    private val endPos: Long
        get() = dlBean.endPos ?: contentLength
    private var completeSize: Long
        get() = dlBean.completeSize
        set(value) {
            dlBean.completeSize = value
        }
    private var event: DLEvent
        get() = dlBean.event
        set(value) {
            dlBean.event = value
        }
    private val md5: String?
        get() = config.md5
    private val downloadFile: File
        get() = File(config.saveDir, config.saveName)
    private val items: DLTaskItems
        get() = config.items
    private val listener: DLEventListener
        get() = config.eventListener
    private var requestCall: Call? = null
    private var isPause = false
    private var isCancel = false

    override fun onFailure(call: Call, e: IOException) {
        listener.onFailure(DLEvent.FAILED(e).apply { event = this })
    }

    override fun onResponse(call: Call, response: Response) {
        if (200 != response.code) {
            val failed = DLEvent.FAILED(RuntimeException("Http status code is ${response.code}"))
            listener.onFailure(failed.also { event = it })
            return
        }
        // body is a non-null value because this response was passed to Callback.onResponse
        val body = response.body ?: return
        val length = body.contentLength().also { contentLength = it }
        if (-1L == length) {
            val failed = DLEvent.FAILED(RuntimeException("ContentLength of response is -1."))
            listener.onFailure(failed.also { event = it })
            return
        }
        val inputStream = body.byteStream()
        if (event is DLEvent.INIT || event is DLEvent.CANCEL) {
            FileMgr.saveFile(downloadFile).exceptionOrNull()?.let { ex ->
                listener.onFailure(DLEvent.FAILED(ex).also { event = it })
                return
            }
        }
        val outputStream = RandomAccessFile(downloadFile, "rwd").apply {
            seek(completeSize)
        }
        val bufferSize = 1024 * 8
        val buffer = ByteArray(bufferSize)
        val bufferedInputStream = BufferedInputStream(inputStream, bufferSize)
        var readLength: Int
        try {
            while (bufferedInputStream.read(buffer, 0, bufferSize)
                    .also { readLength = it } != -1 && !isPause
            ) {
                if (isCancel) {
                    if (requestCall?.isCanceled() != true) {
                        requestCall?.cancel()
                    }
                    event = DLEvent.CANCEL
                    FileMgr.deleteFile(downloadFile)
                    listener.onCancel()
                    return
                }
                outputStream.write(buffer, 0, readLength)
                completeSize += readLength
                listener.onDownloading(
                    DLEvent.DOWNLOADING(completeSize, contentLength).also { event = it })
            }
            md5.takeIf { it != null }?.let { md5 ->
                if (md5 == getFileMD5(downloadFile)) {
                    listener.onSuccess(DLEvent.SUCCESS(downloadFile).also { event = it })
                } else {
                    throw RuntimeException("File MD5($md5) verification failed, the error file has been deleted.")
                }
            } ?: listener.onSuccess(DLEvent.SUCCESS(downloadFile).also { event = it })
        } catch (exception: Throwable) {
            listener.onFailure(DLEvent.FAILED(exception).also { event = it })
            FileMgr.deleteFile(downloadFile)
        } finally {
            bufferedInputStream.close()
            outputStream.close()
            inputStream.close()
        }
    }

    /**
     * Start download
     *
     * @since 0.5.2
     */
    context(OkHttpClient)
    suspend fun start() {
        val head = head()
        if (200 != head.code) {
            listener.onFailure(DLEvent.FAILED(IllegalStateException("Response code is ${head.code}")))
            return
        }
        // The http range unit is bytes
        // https://www.iana.org/assignments/http-parameters/http-parameters.xhtml#range-units
        if ("bytes" == head.headers["Accept-Ranges"]) {
            val contentLength = head.headers["Content-Length"] ?: return@pointer
            val remaining = contentLength % (items.count)
        }
    }

    /**
     * Pause download.
     *
     * @since 0.5.2
     */
    fun pause() {
        event = DLEvent.PAUSE
        isPause = true
        listener.onPause()
    }

    /**
     * Resume download if current [event] is not [DLEvent.SUCCESS] or
     * [DLEvent.FAILED].
     *
     * @since 0.5.2
     */
    fun resume() {
        if (event !is DLEvent.SUCCESS && event !is DLEvent.FAILED) {
            isPause = false
            start()
        }
    }

    /**
     * Cancel download. It will cancel the [requestCall] and delete the
     * [downloadFile].
     *
     * @since 0.5.2
     */
    fun cancel() {
        isCancel = true
    }

    /** @since 1.5.0 */
    private suspend fun OkHttpClient.head(): Response = suspendCancellableCoroutine { continuation ->
        val request = Request.Builder().url(downloadUrl).head().build()
        newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                continuation.resumeWithException(e)
            }

            override fun onResponse(call: Call, response: Response) {
                continuation.resume(response)
            }
        })
    }

}