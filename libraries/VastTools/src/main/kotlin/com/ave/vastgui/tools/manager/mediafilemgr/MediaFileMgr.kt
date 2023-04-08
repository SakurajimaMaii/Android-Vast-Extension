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

package com.ave.vastgui.tools.manager.mediafilemgr

import android.database.Cursor
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import com.ave.vastgui.tools.helper.ContextHelper
import com.ave.vastgui.tools.manager.filemgr.FileMgr
import com.ave.vastgui.tools.utils.AppUtils
import com.ave.vastgui.tools.utils.DateUtils
import java.io.File

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/11/2

sealed class MediaFileMgr : MediaFileProvider {

    protected fun getFileByUri(uri: Uri, type: MediaType): File? {
        val name = when (type) {
            MediaType.Images -> MediaStore.Images.Media.DATA
            MediaType.Music -> MediaStore.Audio.Media.DATA
        }
        val proj = arrayOf(name)
        val cursor: Cursor? =
            ContextHelper.getAppContext().contentResolver.query(uri, proj, null, null, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                val columnIndex: Int = cursor.getColumnIndexOrThrow(name)
                val path: String = cursor.getString(columnIndex)
                cursor.close()
                return File(path)
            }
        }
        return null
    }

    /**
     * Get default file directory name.
     *
     * @param type [MediaType]
     */
    protected fun getDefaultRootDirPath(type: MediaType): String? {
        return when (type) {
            MediaType.Images -> {
                FileMgr.appExternalFilesDir(Environment.DIRECTORY_PICTURES)?.path
            }

            MediaType.Music -> {
                FileMgr.appExternalFilesDir(Environment.DIRECTORY_MUSIC)?.path
            }
        }
    }

    /**
     * Get default file name.
     *
     * @param extension For example, .jpg
     * @return For example, 20230313_234940_com_ave_vastgui_app.jpg.
     */
    final override fun getDefaultFileName(extension: String): String {
        val timeStamp: String = DateUtils.getCurrentTime("yyyyMMdd_HHmmss")
        return "${timeStamp}_${AppUtils.getPackageName()?.replace(".", "_")}$extension"
    }

    final override fun getFileUriAboveApi24(file: File, authority: String?): Uri {
        return authority?.let {
            FileProvider.getUriForFile(ContextHelper.getAppContext(), it, file)
        } ?: FileProvider.getUriForFile(
            ContextHelper.getAppContext(),
            AppUtils.getPackageName() ?: "",
            file
        )
    }

    final override fun getFileUriOnApi23(file: File): Uri {
        return Uri.fromFile(file)
    }

}