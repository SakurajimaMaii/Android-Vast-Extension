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

package cn.govast.vasttools.manager.mediafilemgr

import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import cn.govast.vasttools.helper.ContextHelper
import cn.govast.vasttools.manager.mediafilemgr.ImageMgr.getFileUriAboveApi30
import java.io.File

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/11/2
// Description: 
// Documentation:
// Reference:

object MusicMgr : MediaFileMgr() {

    override fun getFileByUri(uri: Uri): File? {
        val proj = arrayOf(MediaStore.Audio.Media.DATA)
        val cursor: Cursor? =
            ContextHelper.getAppContext().contentResolver.query(uri, proj, null, null, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                val columnIndex: Int = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
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
     * [getFileUriAboveApi30] will insert a new row into a table at
     * the given URL [MediaStore.Audio.Media.EXTERNAL_CONTENT_URI].
     * So when you want to delete the music file, please also run
     * [android.content.ContentResolver.delete] to make sure that the
     * file-related information is completely deleted. For example:
     * ```kotlin
     * contentResolver.delete(
     *      MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
     *      MediaStore.Audio.Media.DISPLAY_NAME + "=?",
     *      arrayOf("xxx.mp3")
     * )
     * ```
     */
    @RequiresApi(Build.VERSION_CODES.R)
    override fun getFileUriAboveApi30(file: File): Uri? {
        val values = ContentValues().apply {
            put(MediaStore.Audio.Media.DATA, file.absolutePath)
            put(MediaStore.Audio.Media.DISPLAY_NAME, file.name)
            put(MediaStore.Audio.Media.MIME_TYPE, "image/jpeg")
        }
        return try {
            ContextHelper.getAppContext().contentResolver.insert(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                values
            )
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Get the uri by [saveOptions].
     *
     * [getFileUriAboveApi30] will insert a new row into a table at
     * the given URL [MediaStore.Audio.Media.EXTERNAL_CONTENT_URI].
     * So when you want to delete the image file, please also run
     * [android.content.ContentResolver.delete] to make sure that the
     * file-related information is completely deleted. For example:
     *
     * ```kotlin
     * contentResolver.delete(
     *      MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
     *      MediaStore.Audio.Media.DISPLAY_NAME + "=?",
     *      arrayOf("xxx.mp3")
     * )
     * ```
     *
     * @param saveOptions information about file.
     */
    @RequiresApi(Build.VERSION_CODES.R)
    override fun getFileUriAboveApi30(saveOptions: Map<String, String>.() -> Unit): Uri? {
        val values = ContentValues().apply {
            HashMap<String, String>().also(saveOptions).forEach { (key, value) ->
                put(key, value)
            }
        }
        return try {
            ContextHelper.getAppContext().contentResolver.insert(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                values
            )
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
            null
        }
    }

}