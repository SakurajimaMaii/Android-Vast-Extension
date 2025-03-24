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

import com.ave.vastgui.tools.utils.download.exception.DownloadException
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Request
import okhttp3.Response
import java.io.BufferedInputStream
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.io.RandomAccessFile
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
data class DownloadBean(
    override val url: String,
    override val file: File,
    override val startPos: Long = 0L,
    override val endPos: Long = startPos,
    override val observer: DownloadObserver? = null
) : DownloadEventObserver(),
    Downloadable,
    Callback {

    private var requestCall: Call by Delegates.notNull()

    private var state: State by Delegates.notNull()
    val isSuccess: Boolean
        get() = state == State.Success

    private var _completeSize: Long = 0L
    override val completeSize: Long
        get() = _completeSize

    override fun onFailure(call: Call, e: IOException) {
        observer?.onFailure(DownloadException(e.message, e))
    }

    override fun onResponse(call: Call, response: Response) {
        var bodyStream: InputStream? = null
        var fileStream: RandomAccessFile? = null
        try {
            if (200 == response.code || 206 == response.code) {
                val body = response.body ?: return
                bodyStream = body.byteStream()
                fileStream = RandomAccessFile(file, "rwd")
                fileStream.seek(startPos + completeSize)
                val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
                var bytes = bodyStream.read(buffer)
                while (bytes >= 0 && state != State.Pause) {
                    if (state == State.Cancel || state == State.Interrupt) {
                        if (!requestCall.isCanceled())
                            requestCall.cancel()
                        if (state == State.Cancel)
                            observer?.onCancel()
                        return
                    }
                    fileStream.write(buffer, 0, bytes)
                    _completeSize += bytes
                    observer?.onDownload()
                    bytes = bodyStream.read(buffer)
                }
                state = State.Success
                observer?.onSuccess()
            } else {
                val exception = RuntimeException("The http status code is ${response.code}")
                observer?.onFailure(DownloadException(exception.message, exception))
            }
        } catch (e: Throwable) {
            observer?.onFailure(DownloadException(e.message, e))
        } finally {
            fileStream?.close()
            bodyStream?.close()
        }
    }

    override fun onStart() {
        val builder = Request.Builder()
        builder.url(url)
        if (endPos > startPos + completeSize) {
            builder.addHeader("RANGE", "bytes=${startPos + completeSize}-$endPos")
        }
        val request = builder.build()
        requestCall = DownloadManager.client.newCall(request)
        requestCall.enqueue(this)
    }

    override fun onPause() {
        state = State.Pause
    }

    override fun onResume() {
        state = State.Init
        onStart()
    }

    override fun onCancel() {
        state = State.Cancel
    }

    override fun onInterrupt() {
        state = State.Interrupt
    }

    init {
        state = State.Init
    }

    private enum class State {
        Init, Pause, Cancel, Interrupt, Success
    }

    companion object {
        private const val DEFAULT_BUFFER_SIZE = 16 * 1024
    }

}