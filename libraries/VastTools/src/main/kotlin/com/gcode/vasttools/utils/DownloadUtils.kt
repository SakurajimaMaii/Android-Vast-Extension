/*
 * Copyright 2022 VastGui
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gcode.vasttools.utils

import android.os.Handler
import android.os.Looper
import android.util.Log
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
 * Firstly,you need to add dependencies.
 *
 * Add it in your root build.gradle at the end of repositories:
 * ```groovy
 * allprojects {
 *      repositories {
 *          ...
 *          maven { url 'https://jitpack.io' }
 *      }
 * }
 * ```
 *
 * Add the dependency:
 * ```groovy
 * implementation 'com.github.SakurajimaMaii:ProgressManager:0.0.1'
 * ```
 *
 * Secondly,use [DownloadUtils] to download from network server.
 *
 * ```kotlin
 * // Download url
 * private val downloadUrl = ....
 * // Download save path
 * private val saveDir = ....
 * // Download
 * DownloadUtils.download(
 *      downloadUrl,
 *      saveDir,
 *      "bluetooth.apk",
 *      object :DownloadUtils.OnDownloadListener{
 *              override fun onDownloadSuccess() {
 *                  // Something to do when download successfully.
 *              }
 *              override fun onDownloading(progress: ProgressInfo?) {
 *                  // Something to do when downloading.
 *              }
 *              override fun onDownloadFailed(e: Exception?) {
 *                  // Something to do when download failed.
 *              }
 *       }
 * )
 * ```
 *
 * @since 0.0.8
 */
object DownloadUtils {
    private val tag = this.javaClass.simpleName
    private val okHttpClient: OkHttpClient
    private val mHandler: Handler = Handler(Looper.getMainLooper())

    init {
        val builder = OkHttpClient.Builder()
        okHttpClient = ProgressManager.getInstance().with(builder).build()
    }

    interface OnDownloadListener {
        /**
         * Download successfully.
         */
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

    /**
     * Download from network server.
     *
     * @param url      Download url.
     * @param saveDir  SDCard directory to store downloaded files.
     * @param saveName Save name of downloaded files.If null,the default is to truncate from the end of the download link.
     *                 For example,the link is [https://github.com/SakurajimaMaii/BluetoothDemo/blob/master/app-debug.apk],
     *                 saveName will take app-debug.apk as the value.
     * @param listener Download listener.
     *
     * @since 0.0.8
     */
    fun download(url: String, saveDir: String, saveName: String?, listener: OnDownloadListener) {
        val request: Request = Request.Builder().url(url).build()
        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: okio.IOException) {
                listener.onDownloadFailed(e)
            }

            override fun onResponse(call: Call, response: Response) {
                var inputStream: InputStream? = null
                val buf = ByteArray(2048)
                var len: Int
                var fos: FileOutputStream? = null

                try {
                    inputStream = response.body?.byteStream()
                    Log.i(tag, response.body?.contentLength().toString())
                    val file = if (null != saveName) {
                        File(saveDir, saveName)
                    } else {
                        File(saveDir, getNameFromUrl(url))
                    }
                    if (!file.parentFile?.exists()!!)
                        file.parentFile?.mkdirs()
                    fos = FileOutputStream(file)
                    if (inputStream != null) {
                        while (inputStream.read(buf).also { len = it } != -1) {
                            fos.write(buf, 0, len)
                        }
                    }
                    fos.flush()
                    mHandler.post {
                        listener.onDownloadSuccess()
                    }
                } catch (e: Exception) {
                    listener.onDownloadFailed(e)
                } finally {
                    try {
                        inputStream?.close()
                    } catch (e: java.io.IOException) {
                        listener.onDownloadFailed(e)
                    }
                    try {
                        fos?.close()
                    } catch (e: java.io.IOException) {
                        listener.onDownloadFailed(e)
                    }
                }
            }
        })

        ProgressManager.getInstance().addResponseListener(url, object : ProgressListener {
            override fun onProgress(progressInfo: ProgressInfo?) {
                listener.onDownloading(progressInfo)
            }

            override fun onError(l: Long, e: Exception?) {
                listener.onDownloadFailed(e)
            }
        })
    }

    /**
     * Get file save name for url.
     *
     * @param url For example:[https://github.com/SakurajimaMaii/BluetoothDemo/blob/master/app-debug.apk]
     * @return For example:app-debug.apk
     */
    private fun getNameFromUrl(url: String): String {
        return url.substring(url.lastIndexOf("/") + 1)
    }
}