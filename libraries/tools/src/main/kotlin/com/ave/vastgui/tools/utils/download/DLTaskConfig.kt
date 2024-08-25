/*
 * Copyright 2021-2024 VastGui
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ave.vastgui.tools.utils.download

import com.ave.vastgui.core.extension.NotNUllVar
import com.ave.vastgui.core.extension.NotNullOrDefault
import java.io.File

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2024/7/24
// Description: 
// Documentation:
// Reference:

/**
 * Number of download connections supported per task。
 *
 * @since 1.5.0
 */
enum class DLTaskItems(val count: Int) {
    ONE(1), TWO(2), FOUR(4), EIGHT(8),
    SIXTEEN(16), TWENTY_FOUR(24), THIRTY_TWO(32)
}

class DLTaskConfig internal constructor() {

    var downloadUrl: String by NotNUllVar()
        private set
    var saveDir: File by NotNUllVar()
        private set
    var saveName: String by NotNUllVar()
        private set
    var md5: String? = null
        private set
    var items: DLTaskItems by NotNullOrDefault(DLTaskItems.ONE)
    var eventListener: DLEventListener = DLEventListener()
        private set

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
     * @since 1.5.0
     */
    fun setSaveDir(saveDir: File) = apply {
        this.saveDir = saveDir
    }

    /**
     * Set the save name for the download file.
     *
     * @since 0.5.2
     */
    fun setSaveName(saveName: String?) = apply {
        this.saveName = saveName ?: getNameFromUrl(downloadUrl)
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
     * Get file save name for url.
     *
     * @return app-debug.apk as the return value if the link is
     *    [https://github.com/SakurajimaMaii/BluetoothDemo/blob/master/app-debug.apk](#)
     * @since 0.5.2
     */
    private fun getNameFromUrl(url: String): String {
        return url.substring(url.lastIndexOf("/") + 1)
    }

}