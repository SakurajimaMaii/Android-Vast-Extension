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

package com.ave.vastgui.tools.utils

import android.os.Handler
import android.os.Looper
import com.ave.vastgui.tools.manager.filemgr.FileMgr
import me.jessyan.progressmanager.ProgressListener
import me.jessyan.progressmanager.ProgressManager
import me.jessyan.progressmanager.body.ProgressInfo
import okhttp3.*
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream


// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/4/14 18:11
// Description: DownloadUtils is based on OKHttp3 and ProgressManager to help you download files.
// Documentation: [DownloadUtils](https://sakurajimamaii.github.io/VastDocs/document/en/DownloadUtils.html)

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
        private var onDownloadSuccessListener: (() -> Unit)? = null
        private var onDownloadingListener: ((progress: ProgressInfo?) -> Unit)? = null
        private var onDownloadFailedListener: ((e: Exception?) -> Unit)? = null

        fun setDownloadUrl(url: String) = apply {
            downloadUrl = url
        }

        fun setSaveDir(saveDir: String) = apply {
            this.saveDir = saveDir
        }

        fun setSaveName(saveName: String) = apply {
            this.saveName = saveName
        }

        fun setDownloadSuccess(l: () -> Unit) = apply {
            onDownloadSuccessListener = l
        }

        fun setDownloading(l: (progress: ProgressInfo?) -> Unit) = apply {
            onDownloadingListener = l
        }

        fun setDownloadFailed(l: (e: Exception?) -> Unit) = apply {
            onDownloadFailedListener = l
        }

        fun download() {
            download(this)
        }

        override fun onDownloadSuccess() {
            onDownloadSuccessListener?.invoke()
        }

        override fun onDownloading(progress: ProgressInfo?) {
            onDownloadingListener?.invoke(progress)
        }

        override fun onDownloadFailed(e: Exception?) {
            onDownloadFailedListener?.invoke(e)
        }

    }

    interface OnDownloadListener {
        /** Download successfully. */
        fun onDownloadSuccess()

        /**
         * Downloading.
         *
         * @param progress Download progress information.
         */
        fun onDownloading(progress: ProgressInfo?)

        /**
         * Download failed.
         *
         * @param e Download exception.
         */
        fun onDownloadFailed(e: Exception?)
    }

    private val okHttpClient: OkHttpClient
    private val mHandler: Handler = Handler(Looper.getMainLooper())

    /** Download from network server. */
    private fun download(
        downloadConfig: DownloadConfig
    ) {
        val request: Request = Request.Builder().url(downloadConfig.downloadUrl).build()
        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: okio.IOException) {
                downloadConfig.onDownloadFailed(e)
            }

            override fun onResponse(call: Call, response: Response) {
                var inputStream: InputStream? = null
                val buf = ByteArray(2048)
                var len: Int
                var fos: FileOutputStream? = null

                try {
                    inputStream = response.body?.byteStream()
                    val file = if (null != downloadConfig.saveName) {
                        File(downloadConfig.saveDir, downloadConfig.saveName!!)
                    } else {
                        File(downloadConfig.saveDir, getNameFromUrl(downloadConfig.downloadUrl))
                    }
                    if (!file.parentFile?.exists()!!)
                        file.parentFile?.mkdirs()
                    fos = FileMgr.getEncryptedFile(file).openFileOutput()
                    if (inputStream != null) {
                        while (inputStream.read(buf).also { len = it } != -1) {
                            fos.write(buf, 0, len)
                        }
                    }
                    fos.flush()
                    mHandler.post {
                        downloadConfig.onDownloadSuccess()
                    }
                } catch (e: Exception) {
                    downloadConfig.onDownloadFailed(e)
                } finally {
                    try {
                        inputStream?.close()
                    } catch (e: java.io.IOException) {
                        downloadConfig.onDownloadFailed(e)
                    }
                    try {
                        fos?.close()
                    } catch (e: java.io.IOException) {
                        downloadConfig.onDownloadFailed(e)
                    }
                }
            }
        })

        ProgressManager.getInstance().addResponseListener(downloadConfig.downloadUrl, object :
            ProgressListener {
            override fun onProgress(progressInfo: ProgressInfo?) {
                downloadConfig.onDownloading(progressInfo)
            }

            override fun onError(l: Long, e: Exception?) {
                downloadConfig.onDownloadFailed(e)
            }
        })
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

    init {
        val builder = OkHttpClient.Builder()
        okHttpClient = ProgressManager.getInstance().with(builder).build()
    }
}