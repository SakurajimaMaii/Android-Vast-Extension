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

package com.ave.vastgui.tools.manager.mediafilemgr

import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.Environment.DIRECTORY_PICTURES
import android.provider.MediaStore.Images.Media
import androidx.annotation.RequiresApi
import com.ave.vastgui.tools.content.ContextHelper
import com.ave.vastgui.tools.manager.filemgr.FileMgr
import com.ave.vastgui.tools.utils.DateUtils
import com.ave.vastgui.tools.utils.DateUtils.FORMAT_YYYY_MM_DD_HH_MM_SS
import java.io.File

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/10/21
// Documentation: https://ave.entropy2020.cn/documents/VastTools/core-topics/app-data-and-files/file-manager/media-file-mgr/

data object ImageMgr : MediaFileMgr() {

    override fun getSharedFilesDir(): File {
        return Environment.getExternalStoragePublicDirectory(DIRECTORY_PICTURES)
    }

    @Throws(IllegalStateException::class, RuntimeException::class)
    override fun getExternalFilesDir(subDir: String?): File {
        val file = if (subDir == null) {
            FileMgr.appExternalFilesDir(DIRECTORY_PICTURES)
        } else {
            File(FileMgr.appExternalFilesDir(DIRECTORY_PICTURES), subDir)
        }
        if (file.exists()) return file
        val result = FileMgr.makeDir(file)
        if (result.isFailure) throw result.exceptionOrNull()!!
        return file
    }

    override fun getFileByUri(uri: Uri): File? {
        val proj = arrayOf(Media.RELATIVE_PATH)
        val cursor: Cursor? =
            ContextHelper.getAppContext().contentResolver.query(uri, proj, null, null, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                val columnIndex: Int =
                    cursor.getColumnIndexOrThrow(Media.RELATIVE_PATH)
                val path: String = cursor.getString(columnIndex)
                cursor.close()
                return File(path)
            }
        }
        return null
    }

    /**
     * Get the uri by [file].
     *
     * [getFileUriAboveApi30] will insert a new row into a table at the given
     * URL [Media.EXTERNAL_CONTENT_URI]. So when you want to delete the image
     * file, please also run [android.content.ContentResolver.delete] to make
     * sure that the file-related information is completely deleted. For
     * example:
     * ```kotlin
     * contentResolver.delete(
     *      MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
     *      MediaStore.Images.Media.DISPLAY_NAME + "=?",
     *      arrayOf("avatar.jpg")
     * )
     * ```
     *
     * By default, [getFileUriAboveApi30] will only insertthe following
     * columns: [Media.DATA],[Media.DISPLAY_NAME],[Media.MIME_TYPE], If you
     * want to customize, youcan refer to [getFileUriAboveApi30] by using
     * saveOptions parameter.
     *
     * Since version 0.5.0, [Media.DATE_ADDED] will also be inserted as the
     * default column.
     */
    @RequiresApi(Build.VERSION_CODES.R)
    override fun getFileUriAboveApi30(file: File): Uri? {
        val values = ContentValues().apply {
            put(Media.DATA, file.absolutePath)
            put(Media.DISPLAY_NAME, file.name)
            put(Media.MIME_TYPE, "image/jpeg")
            put(Media.DATE_ADDED, DateUtils.getCurrentTime(FORMAT_YYYY_MM_DD_HH_MM_SS))
        }
        return ContextHelper.getAppContext().contentResolver.insert(Media.EXTERNAL_CONTENT_URI, values)
    }

    /**
     * Get the uri by [saveOptions].
     *
     * [getFileUriAboveApi30] will insert a new row into a table at the given
     * URL [Media.EXTERNAL_CONTENT_URI]. So when you want to delete the image
     * file, please also run [android.content.ContentResolver.delete] to make
     * sure that the file-related information is completely deleted. For
     * example:
     * ```kotlin
     * contentResolver.delete(
     *      MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
     *      MediaStore.Images.Media.DISPLAY_NAME + "=?",
     *      arrayOf("avatar.jpg")
     * )
     * ```
     *
     * @param saveOptions information about file.
     */
    @RequiresApi(Build.VERSION_CODES.R)
    override fun getFileUriAboveApi30(saveOptions: MutableMap<String, String>.() -> Unit): Uri? {
        val values = ContentValues().apply {
            HashMap<String, String>().also(saveOptions).forEach { (key, value) ->
                put(key, value)
            }
        }
        return ContextHelper.getAppContext().contentResolver.insert(Media.EXTERNAL_CONTENT_URI, values)
    }

}