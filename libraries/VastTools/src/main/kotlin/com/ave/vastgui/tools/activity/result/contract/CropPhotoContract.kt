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

package com.ave.vastgui.tools.activity.result.contract

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.Environment.DIRECTORY_PICTURES
import androidx.activity.result.contract.ActivityResultContract
import androidx.core.content.FileProvider
import com.ave.vastgui.tools.helper.ContextHelper
import com.ave.vastgui.tools.manager.mediafilemgr.ImageMgr
import com.ave.vastgui.tools.utils.cropimage.CropIntent
import java.io.File

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/3/23
// Documentation: https://ave.entropy2020.cn/documents/VastTools/app-entry-points/activities/ActivityResult/

/**
 * Cropping photo.
 *
 * When [Build.VERSION.SDK_INT] is between [Build.VERSION_CODES.R] and
 * [Build.VERSION_CODES.N], You need to refer to the following example
 * configuration path for [FileProvider].
 *
 * ```kotlin
 * // The output image path.
 * val path = Environment.getExternalStoragePublicDirectory(DIRECTORY_PICTURES).path
 * ```
 * ```xml
 * <!-- File named file_paths.xml in xml folder. -->
 * <resources>
 *      <!-- add this line -->
 *      <external-path name="name_you_define" path="Pictures" />
 * </manifest>
 * ```
 *
 * @param authority The authority of a [FileProvider] defined in a
 *     <provider> element in your app's manifest. If the value the
 *     [authority] is null, the app package name will be used as the
 *     default authority for the [FileProvider] that is defined app's
 *     manifest.
 */
class CropPhotoContract @JvmOverloads constructor(private val authority: String? = null) :
    ActivityResultContract<CropIntent, Uri?>() {

    override fun createIntent(context: Context, input: CropIntent): Intent {
        val path = Environment.getExternalStoragePublicDirectory(DIRECTORY_PICTURES).path
        val name = input.mOutputName ?: ImageMgr.getDefaultFileName(".jpg")
        val uri = File(path, name).let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                ImageMgr.getFileUriAboveApi30(it)
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                ImageMgr.getFileUriAboveApi24(it, authority)
            } else {
                ImageMgr.getFileUriOnApi23(it)
            }
        }
        return input.setOutputUri(uri).getIntent()
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
        /** Revoke uri permission that is granted in [CropIntent.setOutputUri] */
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R && Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent?.data?.let {
                ContextHelper.getAppContext().revokeUriPermission(
                    it,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            }
        }
        return intent?.data
    }

}