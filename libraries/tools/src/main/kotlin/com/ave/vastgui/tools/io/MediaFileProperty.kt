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

package com.ave.vastgui.tools.io

import android.os.Environment
import com.ave.vastgui.core.onFailure
import com.ave.vastgui.tools.utils.AppUtils
import com.ave.vastgui.tools.utils.DateUtils
import java.io.File

/** Using to provide information about media file. */
sealed interface MediaFileProperty {

    /**
     * Get application-specific storage folder.
     *
     * @param type The type of storage directory to return. Should be one
     * of [Environment.DIRECTORY_MUSIC], [Environment.DIRECTORY_PODCASTS],
     * [Environment.DIRECTORY_RINGTONES], [Environment.DIRECTORY_ALARMS],
     * [Environment.DIRECTORY_NOTIFICATIONS], [Environment.DIRECTORY_PICTURES],
     * [Environment.DIRECTORY_MOVIES], [Environment.DIRECTORY_DOWNLOADS],
     * [Environment.DIRECTORY_DCIM], or [Environment.DIRECTORY_DOCUMENTS].
     * @since 1.5.2
     */
    fun getExternalFilesDir(type: String, subDir: String? = null): File {
        val dictionary = appExternalFilesDir(type)
        if (dictionary.exists()) return dictionary
        dictionary.mkDirs().onFailure { ex -> throw ex }
        return dictionary
    }

    /**
     * Get shared storage folder.
     *
     * @param type The type of storage directory to return. Should be one
     * of [Environment.DIRECTORY_MUSIC], [Environment.DIRECTORY_PODCASTS],
     * [Environment.DIRECTORY_RINGTONES], [Environment.DIRECTORY_ALARMS],
     * [Environment.DIRECTORY_NOTIFICATIONS], [Environment.DIRECTORY_PICTURES],
     * [Environment.DIRECTORY_MOVIES], [Environment.DIRECTORY_DOWNLOADS],
     * [Environment.DIRECTORY_DCIM], or [Environment.DIRECTORY_DOCUMENTS].
     * @since 1.5.2
     */
    fun getSharedFilesDir(type: String): File {
        val dictionary = Environment.getExternalStoragePublicDirectory(type)
        if (dictionary.exists()) return dictionary
        dictionary.mkDirs().onFailure { ex -> throw ex }
        return dictionary
    }

    /**
     * Get default file name.
     *
     * @param extension The media file extension.
     * @return For example, 20230313_234940_455_com_ave_vastgui_app.jpg.
     */
    fun getDefaultFileName(extension: String): String {
        val timeStamp: String = DateUtils.getCurrentTime(DateUtils.FORMAT_YYYY_MM_DD_HH_MM_SS)
        return try {
            "${timeStamp}_${AppUtils.getPackageName().replace(".", "_")}$extension"
        } catch (_: Exception) {
            "${timeStamp}_media_file_mgr$extension"
        }
    }

}