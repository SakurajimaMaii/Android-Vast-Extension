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

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
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
// Date: 2023/7/23
// Documentation: https://ave.entropy2020.cn/documents/tools/core-topics/connectivity/download/download/

/**
 * The download bean.
 *
 * @property url The url of the download file.
 * @since 0.5.2
 */
class DLBean(
    val url: String,
    val file: File,
    val startPos: Long = 0L,
    val endPos: Long = -1L,
    var completeSize: Long = startPos,
    var event: DLEvent = DLEvent.INIT
) {

    private var requestCall: Call? = null
    private var isPause = false
    private var isCancel = false

    /** @since 1.5.0 */
    context(OkHttpClient, DLEventListener)
    suspend fun start() = withContext(Dispatchers.IO) {
        var outputStream: RandomAccessFile? = null
        var bufferedInputStream: BufferedInputStream? = null
        try {
            val response = await()
            // body is a non-null value because this response was passed to Callback.onResponse
            val body = response.body ?: return@withContext
            val contentLength = body.contentLength()
            val inputStream = body.byteStream()
            if (-1L == contentLength) {
                onFailure(DLEvent
                    .FAILED(RuntimeException("ContentLength of response is -1."))
                    .also { event = it })
                return@withContext
            }
            outputStream = RandomAccessFile(file, "rwd").apply { seek(completeSize) }
            val bufferSize = 1024 * 8
            val buffer = ByteArray(bufferSize)
            bufferedInputStream = BufferedInputStream(inputStream, bufferSize)
            var readLength: Int
            while (bufferedInputStream.read(buffer, 0, bufferSize)
                    .also { readLength = it } != -1 && !isPause
            ) {
                if (isCancel) {
                    if (requestCall?.isCanceled() != true) {
                        requestCall?.cancel()
                    }
                    event = DLEvent.CANCEL
                    onCancel()
                    return@withContext
                }
                outputStream.write(buffer, 0, readLength)
                completeSize += readLength
                onDownloading(DLEvent.DOWNLOADING(completeSize, contentLength).also { event = it })
            }
        } catch (exception: Exception) {
            onFailure(DLEvent.FAILED(exception).also { event = it })
        } finally {
            outputStream?.close()
            bufferedInputStream?.close()
        }
    }

    /** @since 1.5.0 */
    private suspend fun OkHttpClient.await(): Response = suspendCancellableCoroutine { continuation ->
        val request = Request.Builder()
            .url(url)
            .apply {
                if (endPos > completeSize) {
                    addHeader("RANGE", "bytes=$completeSize-$endPos")
                }
            }
            .build()
        isCancel = false
        newCall(request).apply { requestCall = this@apply }
            .enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    continuation.resumeWithException(e)
                }

                override fun onResponse(call: Call, response: Response) {
                    continuation.resume(response)
                }
            })
    }


    /**
     * Pause download.
     *
     * @since 0.5.2
     */
    context(DLEventListener)
    fun pause() {
        event = DLEvent.PAUSE
        isPause = true
        onPause()
    }

    /**
     * Resume download if current [event] is not [DLEvent.SUCCESS] or
     * [DLEvent.FAILED].
     *
     * @since 0.5.2
     */
    context(OkHttpClient, DLEventListener)
    suspend fun resume() {
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

}