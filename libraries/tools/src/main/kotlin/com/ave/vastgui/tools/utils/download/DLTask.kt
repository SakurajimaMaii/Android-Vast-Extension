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

import com.ave.vastgui.core.extension.cast
import com.ave.vastgui.tools.manager.filemgr.FileMgr
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.apache.commons.codec.digest.DigestUtils
import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream
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

    private val okHttpClient = OkHttpClient()
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
        event = DLEvent.FAILED(e)
        listener.onFailure(cast(event))
    }

    override fun onResponse(call: Call, response: Response) {
        val body = response.body ?: run {
            event =
                DLEvent.FAILED(RuntimeException("Can't get the response body, cancel download."))
            listener.onFailure(cast(event))
            return
        }
        val inputStream = body.byteStream()
        if (event is DLEvent.INIT || event is DLEvent.CANCEL) {
            contentLength = body.contentLength()
            FileMgr.saveFile(downloadFile).exceptionOrNull()?.let {
                event = DLEvent.FAILED(it)
                listener.onFailure(cast(event))
                return
            }
        }
        val outputStream = RandomAccessFile(downloadFile, "rwd")
        outputStream.seek(completeSize)
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
                event = DLEvent.DOWNLOADING(completeSize, contentLength)
                listener.onDownloading(cast(event))
            }
            md5.takeIf { it != null }?.let {
                if (it == DigestUtils.md5Hex(FileInputStream(downloadFile.path))) {
                    event = DLEvent.SUCCESS(downloadFile)
                    listener.onSuccess(cast(event))
                } else {
                    throw RuntimeException("File MD5($md5) verification failed, the error file has been deleted.")
                }
            } ?: run {
                event = DLEvent.SUCCESS(downloadFile)
                listener.onSuccess(cast(event))
            }
        } catch (exception: Throwable) {
            event = DLEvent.FAILED(exception)
            listener.onFailure(cast(event))
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

}