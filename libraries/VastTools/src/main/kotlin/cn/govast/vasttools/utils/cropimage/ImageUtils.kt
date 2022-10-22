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

package cn.govast.vasttools.utils.cropimage

import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import cn.govast.vasttools.helper.ContextHelper
import cn.govast.vasttools.utils.AppUtils
import cn.govast.vasttools.utils.DateUtils
import cn.govast.vasttools.utils.FileUtils
import java.io.File

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/10/21
// Description: 
// Documentation:
// Reference:

object ImageUtils {

    /**
     * Get the uri by [fileName] as the value of the [MediaStore.EXTRA_OUTPUT]
     * for the intent that start the Camera or Crop.
     *
     * The root dir path refer to [getImageRootDirPath].
     *
     * If the SDK version of your app is above [Build.VERSION_CODES.R],
     * [getImageUri] will insert a new row into a table at the
     * given URL [MediaStore.Images.Media.EXTERNAL_CONTENT_URI].
     * So when you want to delete the image file, please also
     * run [android.content.ContentResolver.delete] to make sure
     * that the file-related information is completely deleted.
     *
     * If the SDK version of your app is above [Build.VERSION_CODES.N], please
     * register a provider in AndroidManifest.xml.
     *
     * @param authority By default, the value is the app package name.
     * @throws IllegalArgumentException
     * @see
     */
    @JvmOverloads
    fun getImageUri(
        fileName: String,
        authority: String = AppUtils.getPackageName() ?: ""
    ): Uri? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            getImageUriAboveApi30(fileName)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            getImageUriAboveApi24(fileName, authority)
        } else {
            getImageUriOnApi23(fileName)
        }
    }

    /**
     * Get the crop image file from [uri].
     *
     * @return crop image file, null otherwise.
     */
    fun getImageFile(uri: Uri): File? {
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val cursor: Cursor? =
            ContextHelper.getAppContext().contentResolver.query(uri, proj, null, null, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                val columnIndex: Int = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                val path: String = cursor.getString(columnIndex)
                cursor.close()
                return File(path)
            }
        }
        return null
    }

    private fun createImageFile(fileName: String): File {
        return File(getImageRootDirPath() + File.separator + fileName)
    }

    /** Get the crop image file uri above [Build.VERSION_CODES.R]. */
    @RequiresApi(Build.VERSION_CODES.R)
    private fun getImageUriAboveApi30(fileName: String): Uri? {
        val file = createImageFile(fileName)
        val values = ContentValues().apply {
            put(MediaStore.Images.Media.DATA, file.absolutePath)
            put(MediaStore.Images.Media.DISPLAY_NAME, file.name)
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        }
        return ContextHelper.getAppContext().contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            values
        )
    }

    /** Get the crop image file uri above [Build.VERSION_CODES.N]. */
    @RequiresApi(Build.VERSION_CODES.N)
    private fun getImageUriAboveApi24(fileName: String, authority: String): Uri {
        val file = createImageFile(fileName)
        return FileProvider.getUriForFile(ContextHelper.getAppContext(), authority, file)
    }

    /** Get the crop image file uri. */
    private fun getImageUriOnApi23(fileName: String): Uri {
        return Uri.parse("file:///${getImageRootDirPath()}/${fileName}")
    }

    private fun getImageRootDirPath() =
        FileUtils.appExternalFilesDir(Environment.DIRECTORY_PICTURES)?.path

    /**
     * Get default file name.
     *
     * @return For example, IMG_20221022_170455_com_govast_vasttools.jpg
     */
    fun getDefaultFileName(): String {
        val timeStamp: String = DateUtils.getCurrentTime("yyyyMMdd_HHmmss")
        return "IMG_${timeStamp}_${AppUtils.getPackageName()?.replace(".", "_")}.jpg"
    }
}