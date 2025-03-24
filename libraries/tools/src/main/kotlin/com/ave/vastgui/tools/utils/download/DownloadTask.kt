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

import com.ave.vastgui.tools.utils.download.core.DownloadResult
import com.ave.vastgui.tools.utils.download.exception.DownloadException
import com.ave.vastgui.tools.utils.download.interfaces.DownloadListener
import com.ave.vastgui.tools.utils.getFileMD5
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import kotlin.properties.Delegates

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/7/24
// Documentation: https://sakurajimamaii.github.io/AVE-DOC/documents/tools/core-topics/connectivity/download/download/
// Reference: https://github.com/Heart-Beats/Downloader/blob/master/downloader/src/main/java/com/hl/downloader/SubDownloadTask.kt

/**
 * Download task.
 *
 * @since 1.5.2
 */
class DownloadTask internal constructor(
    override val url: String,
    override val maxCoreCount: Int,
    override val file: File,
    override val md5: String?,
    override val listener: DownloadListener?
) : DownloadOwner,
    DownloadEventOwner,
    DownloadObserver {

    private var length: Long by Delegates.notNull()
    private val dispatcher: DownloadDispatcher = DownloadDispatcher()

    override fun onDownload() {
        val progress = dispatcher.beans.fold(0f) { sum, bean ->
            sum + if (bean.startPos == bean.endPos) Float.NaN else bean.completeSize.toFloat()
        }
        val download = if (progress.isNaN()) {
            DownloadResult.Download()
        } else {
            DownloadResult.Download(progress, length.toFloat())
        }
        listener?.onDownloading(download)
    }

    override fun onSuccess() {
        if (!dispatcher.beans.all { it.isSuccess }) return
        if (null == md5) {
            listener?.onSuccess(DownloadResult.Success(file))
            return
        }
        if (md5 == getFileMD5(file)) {
            listener?.onSuccess(DownloadResult.Success(file))
        } else {
            val e = DownloadException(null, RuntimeException("File MD5($md5) verification failed."))
            listener?.onFailure(DownloadResult.Failure(e))
        }
    }

    override fun onFailure(e: DownloadException) {
        listener?.onFailure(DownloadResult.Failure(e))
    }

    override fun onCancel() {
        listener?.onCancel()
    }

    /**
     * Start download
     *
     * @since 0.5.2
     */
    override fun start() {
        // Create file.
        if (file.exists()) {
            throw IllegalArgumentException("${file.absoluteFile} is already exists.")
        } else if (!file.createNewFile()) {
            throw RuntimeException("${file.absoluteFile} create failed.")
        }

        MainScope().launch {
            // Try to get the file length for segmentation.
            length = tryToGetDownloadSize()
            if (UNKNOWN_LENGTH == length) {
                val bean = DownloadBean(
                    url = url,
                    file = file,
                    observer = this@DownloadTask
                )
                dispatcher.addBean(bean)
            } else {
                val remainder = length % maxCoreCount
                val subLength = (length - remainder) / maxCoreCount
                for (index in 0 until maxCoreCount) {
                    val bean = DownloadBean(
                        url = url,
                        file = file,
                        startPos = index * subLength,
                        endPos = (index + 1) * subLength
                                + if (index == maxCoreCount - 1) remainder else 0 - 1,
                        observer = this@DownloadTask
                    )
                    dispatcher.addBean(bean)
                }
            }

            dispatcher.start()
        }
    }

    override fun pause() {
        dispatcher.pause()
    }

    override fun resume() {
        dispatcher.resume()
    }

    override fun cancel() {
        dispatcher.cancel()
    }

    private suspend fun tryToGetDownloadSize(): Long = withContext(Dispatchers.IO) {
        var length = UNKNOWN_LENGTH
        val request = Request.Builder().url(url).build()
        runCatching {
            val response = DownloadManager.client.newCall(request).execute()
            length = response.header("content-length", "-1")!!.toLong()
        }
        return@withContext length
    }

    companion object {
        const val UNKNOWN_LENGTH = -1L
    }

}