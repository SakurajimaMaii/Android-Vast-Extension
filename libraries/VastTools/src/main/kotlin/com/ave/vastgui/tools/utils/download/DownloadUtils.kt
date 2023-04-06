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

import com.ave.vastgui.tools.coroutines.await
import com.ave.vastgui.tools.coroutines.createMainScope
import com.ave.vastgui.tools.manager.filemgr.FileMgr
import com.ave.vastgui.tools.utils.LogUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
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
 * Using [DownloadUtils] to download from network server.
 *
 * ```kotlin
 * // Download url
 * private val downloadUrl = ....
 * // Download save path
 * private val saveDir = ....
 * // Download
 * DownloadUtils
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
 *      .download()
 * ```
 */
object DownloadUtils {

    private val mScope = createMainScope("DownloadUtils")

    @JvmStatic
    fun createConfig() = DownloadConfig()

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
    class DownloadConfig : OnDownloadListener {

        internal var downloadUrl: String = ""
        internal var saveDir: String = ""
        internal var saveName: String? = null
        private var onDownloadSuccessListener: ((File) -> Unit)? = null
        private var onDownloadingListener: ((progress: DownloadResult.Progress?) -> Unit)? = null
        private var onDownloadFailedListener: ((e: Throwable?) -> Unit)? = null

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

        fun download() {
            download(this)
        }

        override fun onDownloadSuccess(file: File) {
            onDownloadSuccessListener?.invoke(file)
        }

        override fun onDownloading(progress: DownloadResult.Progress?) {
            onDownloadingListener?.invoke(progress)
        }

        override fun onDownloadFailed(e: Throwable?) {
            onDownloadFailedListener?.invoke(e)
        }

    }

    interface OnDownloadListener {
        /** Download successfully. */
        fun onDownloadSuccess(file: File)

        /**
         * Downloading.
         *
         * @param progress Download progress information.
         */
        fun onDownloading(progress: DownloadResult.Progress?)

        /**
         * Download failed.
         *
         * @param e Download exception.
         */
        fun onDownloadFailed(e: Throwable?)
    }

    private val okHttpClient: OkHttpClient = OkHttpClient()

    private fun download(
        downloadConfig: DownloadConfig
    ) {
        mScope.launch {
            flow {
                try {
                    val request: Request = Request.Builder().url(downloadConfig.downloadUrl).build()
                    val body = okHttpClient.newCall(request).await()
                    val contentLength = body.contentLength()
                    LogUtils.d("ArcProgressViewActivity", contentLength.toString())
                    val inputStream = body.byteStream()
                    val file = if (null != downloadConfig.saveName) {
                        File(downloadConfig.saveDir, downloadConfig.saveName!!)
                    } else {
                        File(downloadConfig.saveDir, getNameFromUrl(downloadConfig.downloadUrl))
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
                    downloadConfig.onDownloadFailed(e)
                }, onSuccess = { file ->
                    downloadConfig.onDownloadSuccess(file)
                }, onLoading = { progress ->
                    downloadConfig.onDownloading(
                        DownloadResult.Progress(
                            progress.currentLength,
                            progress.length,
                            progress.process
                        )
                    )
                })
            }
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