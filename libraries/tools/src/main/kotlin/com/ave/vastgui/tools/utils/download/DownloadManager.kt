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

import com.ave.vastgui.tools.utils.download.interfaces.DownloadListener
import okhttp3.OkHttpClient
import java.io.File
import kotlin.properties.Delegates

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2025/3/19
// Documentation: 
// Reference:

/**
 * By using [DownloadManager], you can create a download task.
 */
object DownloadManager {

    private var _client: OkHttpClient? = null
    val client: OkHttpClient
        get() = _client ?: createDefaultClient()

    fun setClient(client: OkHttpClient) {
        _client = client
    }

    private fun createDefaultClient() = OkHttpClient()

    fun getDownloadConfig() = DownloadConfig()

    class DownloadConfig internal constructor() {

        private var url: String by Delegates.notNull()
        private var file: File by Delegates.notNull()
        private var md5: String? = null
        private var listener: DownloadListener? = null

        /**
         * Set download url.
         */
        fun setDownloadUrl(url: String) = apply {
            this.url = url
        }

        /**
         * Set the download file.
         */
        fun setFile(file: File) = apply {
            this.file = file
        }

        /**
         * Set the event listener for the download.
         */
        fun setListener(listener: DownloadListener) = apply {
            this.listener = listener
        }

        /**
         * Set the md5 value of the file for verification
         */
        fun setMD5(md5: String) = apply {
            this.md5 = md5
        }

        /**
         * Build the download task.
         */
        fun build() = DownloadTask(url, 2, file, md5, listener)

        /**
         * Get file save name for url.
         *
         * @return app-debug.apk as the return value if the link is
         * [https://github.com/SakurajimaMaii/BluetoothDemo/blob/master/app-debug.apk](#)
         */
        private fun getNameFromUrl(url: String): String {
            return url.substring(url.lastIndexOf("/") + 1)
        }

    }

}