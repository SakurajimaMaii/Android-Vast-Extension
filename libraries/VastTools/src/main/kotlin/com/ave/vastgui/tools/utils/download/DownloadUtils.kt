/*
 * Copyright 2022 VastGui guihy2019@gmail.com
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

import com.ave.vastgui.core.extension.NotNUllVar
import com.ave.vastgui.tools.coroutines.await
import com.ave.vastgui.tools.manager.filemgr.FileMgr
import com.ave.vastgui.tools.utils.LogUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/4/14 18:11
// Description: DownloadUtils is based on OKHttp3 and ProgressManager to help you download files.
// Documentation: https://ave.entropy2020.cn/documents/VastTools/core-topics/DownloadUtils/

/**
 * Download utils.
 *
 * [DownloadUtils] can help you to download from network server,
 *
 * ```kotlin
 * // Download url
 * private val downloadUrl = ....
 * // Download save path
 * private val saveDir = ....
 * // Download
 * val downloadUtils = DownloadUtils
 *      .createConfig()
 *      .setDownloadUrl(downloadUrl)
 *      .setSaveDir(saveDir)
 *      .setDownloading {
 *          .. //Do something
 *      }
 *      .setDownloadFailed {
 *          .. //Do something
 *      }
 *      .setDownloadSuccess {
 *          .. //Do something
 *      }
 *      .build()
 *
 * // Start download
 * lifecycleScope.launch {
 *     downloadUtils.download()
 * }
 * ```
 *
 * @since 0.2.0
 */
class DownloadUtils private constructor(config: DownloadConfig) {

    private val okHttpClient: OkHttpClient = OkHttpClient()
    private var downloadUrl: String by NotNUllVar()
    private var saveDir: String by NotNUllVar()
    private var saveName: String? = null
    private var onDownloadSuccessListener: ((File) -> Unit)? = null
    private var onDownloadingListener: ((progress: DownloadResult.Progress?) -> Unit)? = null
    private var onDownloadFailedListener: ((e: Throwable?) -> Unit)? = null

    init {
        downloadUrl = config.downloadUrl
        saveDir = config.saveDir
        saveName = config.saveName
        onDownloadingListener = config.onDownloadingListener
        onDownloadSuccessListener = config.onDownloadSuccessListener
        onDownloadFailedListener = config.onDownloadFailedListener
    }

    companion object {
        @JvmStatic
        fun createConfig() = DownloadConfig()
    }

    /**
     * The config of the download.
     *
     * @property downloadUrl Download url.
     * @property saveDir SDCard directory to store downloaded files.
     * @property saveName Save name of downloaded files.If null,the default is
     *     to truncate from the end of the download link. For example,the link
     *     is
     *     [https://github.com/SakurajimaMaii/BluetoothDemo/blob/master/app-debug.apk],
     *     saveName will take app-debug.apk as the value.
     */
    class DownloadConfig {

        var downloadUrl: String by NotNUllVar()
            private set
        var saveDir: String by NotNUllVar()
            private set
        var saveName: String? = null
            private set
        var onDownloadSuccessListener: ((File) -> Unit)? = null
            private set
        var onDownloadingListener: ((progress: DownloadResult.Progress?) -> Unit)? = null
            private set
        var onDownloadFailedListener: ((e: Throwable?) -> Unit)? = null
            private set

        fun setDownloadUrl(url: String) = apply {
            downloadUrl = url
        }

        fun setSaveDir(saveDir: String) = apply {
            this.saveDir = saveDir
        }

        fun setSaveName(saveName: String) = apply {
            this.saveName = saveName
        }

        fun setDownloadSuccess(l: (File) -> Unit) = apply {
            onDownloadSuccessListener = l
        }

        fun setDownloading(l: (progress: DownloadResult.Progress?) -> Unit) = apply {
            onDownloadingListener = l
        }

        fun setDownloadFailed(l: (e: Throwable?) -> Unit) = apply {
            onDownloadFailedListener = l
        }

        fun build() = DownloadUtils(this)

    }

    suspend fun download() {
        flow {
            try {
                val request: Request = Request.Builder().url(downloadUrl).build()
                val body = okHttpClient.newCall(request).await()
                val contentLength = body.contentLength()
                LogUtils.d("ArcProgressViewActivity", contentLength.toString())
                val inputStream = body.byteStream()
                val file = if (null != saveName) {
                    File(saveDir, saveName!!)
                } else {
                    File(saveDir, getNameFromUrl(downloadUrl))
                }
                val result = FileMgr.saveFile(file)
                if (result == FileMgr.FileResult.FLAG_FAILED)
                    throw RuntimeException("The download file save failed.")
                val outputStream = FileOutputStream(file)
                var currentLength = 0
                val bufferSize = 1024 * 8
                val buffer = ByteArray(bufferSize)
                val bufferedInputStream = BufferedInputStream(inputStream, bufferSize)
                var readLength: Int
                while (bufferedInputStream.read(buffer, 0, bufferSize)
                        .also { readLength = it } != -1
                ) {
                    outputStream.write(buffer, 0, readLength)
                    currentLength += readLength
                    emit(
                        DownloadResult.progress(
                            currentLength.toLong(),
                            contentLength,
                            currentLength.toFloat() / contentLength.toFloat()
                        )
                    )
                }
                bufferedInputStream.close()
                outputStream.close()
                inputStream.close()
                emit(DownloadResult.success(file))
            } catch (e: Exception) {
                emit(DownloadResult.failure(e))
            }
        }.flowOn(Dispatchers.IO).collect {
            it.fold(onFailure = { e ->
                onDownloadFailedListener?.invoke(e)
            }, onSuccess = { file ->
                onDownloadSuccessListener?.invoke(file)
            }, onLoading = { progress ->
                onDownloadingListener?.invoke(
                    DownloadResult.Progress(
                        progress.currentLength,
                        progress.length,
                        progress.process
                    )
                )
            })
        }
    }

    /**
     * Get file save name for url.
     *
     * @param url For
     *     example:[https://github.com/SakurajimaMaii/BluetoothDemo/blob/master/app-debug.apk]
     * @return For example:app-debug.apk
     */
    private fun getNameFromUrl(url: String): String {
        return url.substring(url.lastIndexOf("/") + 1)
    }

}