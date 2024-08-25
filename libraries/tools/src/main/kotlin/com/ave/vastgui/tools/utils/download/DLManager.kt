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

import okhttp3.OkHttpClient

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/4/14 18:11
// Documentation: https://ave.entropy2020.cn/documents/tools/core-topics/connectivity/download/download/

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
object DLManager {

    private lateinit var okHttpClient: OkHttpClient
    fun createTaskConfig() = DLTaskConfig()

    /** @since 1.5.0 */
    fun configOkhttp(config: OkHttpClient.Builder.() -> Unit) {
        okHttpClient = OkHttpClient.Builder().also(config).build()
    }

    /** @since 1.5.0 */
    fun createTask(config: DLTaskConfig.() -> Unit): DLTask {
        return DLTask(DLTaskConfig().also(config))
    }

}