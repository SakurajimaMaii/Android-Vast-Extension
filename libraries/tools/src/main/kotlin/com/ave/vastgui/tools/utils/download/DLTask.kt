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

import com.ave.vastgui.tools.io.destroy
import com.ave.vastgui.tools.io.mkFile
import com.ave.vastgui.tools.utils.getFileMD5
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.BufferedInputStream
import java.io.File
import java.io.IOException
import java.io.RandomAccessFile

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
class DLTask internal constructor(private val dlBean: DLBean, val listener: DLEventListener) :
    Callback {

    private val okHttpClient = createOkHttpClient()
    private var contentLength: Long = 0L
    private val downloadUrl: String
        get() = dlBean.url
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
        get() = dlBean.md5
    private val downloadFile: File
        get() = dlBean.file
    private var requestCall: Call? = null
    private var isPause = false
    private var isCancel = false

    override fun onFailure(call: Call, e: IOException) {
        listener.onFailure(DLEvent.Failed(e).apply { event = this })
    }

    override fun onResponse(call: Call, response: Response) {
        if (200 != response.code) {
            val failed = DLEvent.Failed(RuntimeException("Http status code is ${response.code}"))
            listener.onFailure(failed.also { event = it })
            return
        }
        // body is a non-null value because this response was passed to Callback.onResponse
        val body = response.body ?: return
        contentLength = body.contentLength()
        val inputStream = body.byteStream()
        if (event is DLEvent.Init || event is DLEvent.Cancel) {
            downloadFile.mkFile().exceptionOrNull()?.let { ex ->
                listener.onFailure(DLEvent.Failed(ex).also { event = it })
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
                    event = DLEvent.Cancel
                    downloadFile.destroy()
                    listener.onCancel()
                    return
                }
                outputStream.write(buffer, 0, readLength)
                completeSize += readLength

                // FIXME Sometimes the response doesn't contain content-length,
                //  which doesn't mean that the download cannot be done.
                if (-1L == contentLength) {
                    listener.onDownloading(DLEvent.Downloading().also { event = it })
                } else {
                    val downloading = DLEvent
                        .Downloading(completeSize.toFloat(), contentLength.toFloat())
                    listener.onDownloading(downloading)
                    event = downloading
                }
            }
            md5.takeIf { it != null }?.let { md5 ->
                if (md5 == getFileMD5(downloadFile)) {
                    listener.onSuccess(DLEvent.Success(downloadFile).also { event = it })
                } else {
                    throw RuntimeException("File MD5($md5) verification failed, the error file has been deleted.")
                }
            } ?: listener.onSuccess(DLEvent.Success(downloadFile).also { event = it })
        } catch (exception: Throwable) {
            listener.onFailure(DLEvent.Failed(exception).also { event = it })
            downloadFile.destroy()
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
    fun start() {
        val request = Request.Builder()
            .url(downloadUrl)
            .apply {
                if (endPos > completeSize) {
                    addHeader("RANGE", "bytes=$completeSize-$endPos")
                }
            }
            .build()
        isCancel = false
        requestCall = okHttpClient.newCall(request)
        requestCall?.enqueue(this)
    }

    /**
     * Pause download.
     *
     * @since 0.5.2
     */
    fun pause() {
        event = DLEvent.Pause
        isPause = true
        listener.onPause()
    }

    /**
     * Resume download if current [event] is not [DLEvent.Success] or
     * [DLEvent.Failed].
     *
     * @since 0.5.2
     */
    fun resume() {
        if (event !is DLEvent.Success && event !is DLEvent.Failed) {
            isPause = false
            listener.onResume()
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

    /** @since 1.5.2 */
    private fun createOkHttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
        if (null != DLManager.logger) {
            builder.addInterceptor(DLManager.logger!!)
        }
        return builder.build()
    }

}