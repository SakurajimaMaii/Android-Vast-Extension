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
import java.io.File

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/4/14 18:11
// Documentation: https://ave.entropy2020.cn/documents/VastTools/core-topics/connectivity/download/download/

/**
 * By using [DLManager], you can create a download task.
 *
 * ```kotlin
 * downloadTask = DLManager
 *    .createConfig()
 *    .setDownloadUrl(downloadUrl)
 *    .setSaveDir(FileMgr.appInternalFilesDir().path)
 *    .setListener {
 *        onDownloading = {
 *            .. //Do something
 *        }
 *        onFailure = {
 *            .. //Do something
 *        }
 *        onSuccess = {
 *            .. //Do something
 *        }
 *    }
 *    .build()
 * ```
 *
 * @since 0.5.2
 */
class DLManager internal constructor(){

    companion object {
        fun createTaskConfig() = DLTaskConfig()
    }

    class DLTaskConfig internal constructor() {

        private var downloadUrl: String by NotNUllVar()
        private var saveDir: String by NotNUllVar()
        private var saveName: String? = null
        private var md5: String? = null
        private var eventListener: DLEventListener = DLEventListener()
        private val dlBean: DLBean
            get() {
                val file = saveName.takeIf { it != null }
                    ?.let { File(saveDir, it) }
                    ?: File(saveDir, getNameFromUrl(downloadUrl))
                return DLBean(downloadUrl, file, md5)
            }

        /**
         * Set download url.
         *
         * @since 0.5.2
         */
        fun setDownloadUrl(url: String) = apply {
            downloadUrl = url
        }

        /**
         * Set the save directory for the download file.
         *
         * @since 0.5.2
         */
        fun setSaveDir(saveDir: String) = apply {
            this.saveDir = saveDir
        }

        /**
         * Set the save name for the download file.
         *
         * @since 0.5.2
         */
        fun setSaveName(saveName: String) = apply {
            this.saveName = saveName
        }

        /**
         * Set the event listener for the download.
         *
         * @since 0.5.2
         */
        fun setListener(listener: DLEventListener.() -> Unit) = apply {
            eventListener = DLEventListener().also(listener)
        }

        /**
         * Set the md5 value of the file for verification
         *
         * @since 0.5.2
         */
        fun setMD5(md5: String) = apply {
            this.md5 = md5
        }

        /**
         * Build the download task.
         *
         * @since 0.5.2
         */
        fun build() = DLTask(dlBean, eventListener)

        /**
         * Get file save name for url.
         *
         * @return app-debug.apk as the return value if the link is
         *     [https://github.com/SakurajimaMaii/BluetoothDemo/blob/master/app-debug.apk](#)
         * @since 0.5.2
         */
        private fun getNameFromUrl(url: String): String {
            return url.substring(url.lastIndexOf("/") + 1)
        }

    }

}