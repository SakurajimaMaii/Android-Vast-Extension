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

import android.util.Log
import com.ave.vastgui.core.extension.nothing_to_do
import com.ave.vastgui.tools.utils.download.core.DownloadEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.InputStream
import java.io.RandomAccessFile
import kotlin.coroutines.CoroutineContext
import kotlin.properties.Delegates

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/7/23
// Documentation: https://sakurajimamaii.github.io/AVE-DOC/documents/tools/core-topics/connectivity/download/download/

/**
 * The download bean.
 *
 * @property url The url of the download file.
 * @since 0.5.2
 */
data class DownloadSubTask(
    val task: DownloadTask,
    val startPos: Long = 0L,
    val endPos: Long = startPos) : CoroutineScope {

    override val coroutineContext: CoroutineContext = task.coroutineContext

    private val client: OkHttpClient =
        task.client

    private val url: String =
        task.url

    private val file: File =
        task.file

    private val eventFlow: SharedFlow<DownloadEvent> =
        task.eventFlow

    private var job: Job by Delegates.notNull()

    private var _completeSize: Long = 0L
    val completeSize: Long
        get() = _completeSize

    val isCompleted: Boolean
        get() = job.isCompleted

    val isCancelled: Boolean
        get() = job.isCancelled

    fun onStart() {
        launch {
            job = launch {
                var bodyStream: InputStream? = null
                var fileStream: RandomAccessFile? = null
                try {
                    val request = Request.Builder().let { builder ->
                        builder.url(url)
                        if (endPos > startPos + completeSize) {
                            builder.addHeader("RANGE", "bytes=${startPos + completeSize}-$endPos")
                        }
                        builder.build()
                    }
                    val response = client.newCall(request).executeAsync()
                    if (200 == response.code || 206 == response.code) {
                        val body = response.body
                            ?: throw RuntimeException("The response body of $url is null.")
                        bodyStream = body.byteStream()
                        fileStream = RandomAccessFile(file, "rwd")
                        fileStream.seek(startPos + completeSize)
                        val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
                        var bytes = bodyStream.read(buffer)
                        while (bytes >= 0) {
                            fileStream.write(buffer, 0, bytes)
                            _completeSize += bytes
                            task.onDownload()
                            bytes = bodyStream.read(buffer)
                        }
                    } else {
                        throw RuntimeException("The http status code is ${response.code}")
                    }
                } catch (exception: Exception) {
                    throw exception
                } finally {
                    fileStream?.close()
                    bodyStream?.close()
                }
            }
            job.join()
            task.onSuccess()
        }
    }

    fun onPause() {
        job.cancel()
    }

    fun onResume() {
        onStart()
    }

    init {
        launch {
            while (isActive) {
                eventFlow.collect { event ->
                    when (event) {
                        DownloadEvent.Start -> onStart()
                        DownloadEvent.Pause -> onPause()
                        DownloadEvent.Resume -> onResume()
                        else -> nothing_to_do()
                    }
                }
            }
        }
    }

    companion object {
        private const val DEFAULT_BUFFER_SIZE = 16 * 1024
    }

}