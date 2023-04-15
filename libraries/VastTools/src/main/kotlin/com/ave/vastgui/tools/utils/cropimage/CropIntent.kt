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

package com.ave.vastgui.tools.utils.cropimage

import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import com.ave.vastgui.tools.helper.ContextHelper


// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/3/23
// Documentation: https://ave.entropy2020.cn/documents/VastTools/core-topics/intent/CropIntent/

class CropIntent : CropProperty() {

    private val intent = Intent("com.android.camera.action.CROP").apply {
        addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }

    override fun setData(uri: Uri) = apply {
        intent.setDataAndType(uri, "image/*")
    }

    override fun setCorp(value: Boolean) = apply {
        intent.putExtra("crop", "true")
    }

    override fun setAspect(aspectX: Int, aspectY: Int) = apply {
        intent.putExtra("aspectX", aspectX)
        intent.putExtra("aspectY", aspectY)
    }

    override fun setOutput(outputX: Int, outputY: Int) = apply {
        intent.putExtra("outputX", outputX)
        intent.putExtra("outputY", outputY)
    }

    @Suppress("DEPRECATION")
    override fun setOutputUri(uri: Uri?) = apply {
        // In order to solve the problem of file file saving failure.
        // https://stackoverflow.com/questions/18249007/how-to-use-support-fileprovider-for-sharing-content-to-other-apps
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R &&
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
        ) {
            val resInfoList: List<ResolveInfo> =
                ContextHelper.getApp().packageManager.queryIntentActivities(
                    intent,
                    PackageManager.MATCH_DEFAULT_ONLY
                )
            for (resolveInfo in resInfoList) {
                val packageName = resolveInfo.activityInfo.packageName
                ContextHelper.getAppContext().grantUriPermission(
                    packageName,
                    uri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            }
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
    }

    override fun setOutputFormat(format: String) = apply {
        intent.putExtra("outputFormat", format)
    }

    override fun setReturnData(value: Boolean) = apply {
        intent.putExtra("return-data", value)
    }

    override fun setNoFaceDetection(value: Boolean) = apply {
        intent.putExtra("noFaceDetection", value)
    }

    /** Return crop intent. */
    fun getIntent(): Intent {
        setOutputFormat(Bitmap.CompressFormat.JPEG.toString())
        return intent
    }

}