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

package com.ave.vastgui.app.activity.view

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import com.ave.vastgui.app.databinding.ActivityCropImageBinding
import com.ave.vastgui.tools.activity.VastVbActivity
import com.ave.vastgui.tools.activity.app.VastCropActivity
import com.ave.vastgui.tools.activity.result.contract.CropPhotoContract
import com.ave.vastgui.tools.activity.result.contract.PickPhotoContract
import com.ave.vastgui.tools.activity.result.contract.TakePhotoContract
import com.ave.vastgui.tools.helper.ContextHelper
import com.ave.vastgui.tools.manager.mediafilemgr.ImageMgr
import com.ave.vastgui.tools.utils.DensityUtils.DP
import com.ave.vastgui.tools.utils.cropimage.CropIntent
import java.io.File

class CropImageActivity : VastVbActivity<ActivityCropImageBinding>() {

    private var output: Uri? = null

    @RequiresApi(Build.VERSION_CODES.R)
    private val pickImage =
        registerForActivityResult(PickPhotoContract()) { uri ->
            uri?.let { cropImageWithActivity(it) }
        }

    private val cropActivityLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == VastCropActivity.RESULT_OK) {
                val uri = it.data?.data
                getBinding().image.setImageURI(uri)
            }
        }

    private val cropIntentLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { res ->
            val uri = res.data?.data
            uri?.let {
                getBinding().image.setImageURI(it)
            }
        }

    private val cropContractLauncher =
        registerForActivityResult(CropPhotoContract()) { uri ->
            uri?.let {
                getBinding().image.setImageURI(it)
            }
        }

    private val takePhoto =
        registerForActivityResult(TakePhotoContract("com.ave.vastgui.app")) {
            getBinding().image.setImageURI(it)
        }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getBinding().openGallery.setOnClickListener {
            pickImage.launch(null)
        }

        getBinding().openCamera.setOnClickListener {
            takePhoto.launch(null)
        }
    }

    /** Use [CropIntent]. */
    @RequiresApi(Build.VERSION_CODES.R)
    private fun cropImageWithCropIntent(uri: Uri) {
        val dir = ImageMgr.getExternalFilesDir(null)
        val name = ImageMgr.getDefaultFileName(".jpg")
        output = ImageMgr.getFileUriAboveApi30(File(dir, name))
        val cropIntent = CropIntent()
            .setData(uri)
            .setCorp(true)
            .setAspect(1, 1)
            .setOutput(200, 200)
            .setReturnData(false)
            .setNoFaceDetection(true)
            .setOutputUri(output)
            .getIntent()
        cropIntentLauncher.launch(cropIntent)
    }

    /** Use [CropIntent] with [CropPhotoContract]. */
    private fun cropImageWithCropContract(uri: Uri) {
        val cropIntent = CropIntent()
            .setData(uri)
            .setCorp(true)
            .setAspect(1, 1)
            .setOutput(200, 200)
            .setReturnData(false)
            .setOutputName(null)
            .setNoFaceDetection(true)
        cropContractLauncher.launch(cropIntent)
    }

    /** Use [CropIntent] with [VastCropActivity]. */
    private fun cropImageWithActivity(uri: Uri) {
        val intent = Intent(this, VastCropActivity::class.java).apply {
            data = uri
            putExtra(VastCropActivity.AUTHORITY, "com.ave.vastgui.app")
            putExtra(VastCropActivity.FRAME_TYPE, VastCropActivity.FRAME_TYPE_RECTANGLE)
            putExtra(VastCropActivity.PREVIEW_WIDTH, 200f.DP)
            putExtra(VastCropActivity.PREVIEW_HEIGHT, 300f.DP)
            putExtra(VastCropActivity.OUTPUT_X, 400f)
            putExtra(VastCropActivity.OUTPUT_Y, 400f)
        }
        cropActivityLauncher.launch(intent)
    }

    private fun removePermission() {
        /** Revoke uri permission that is granted in [CropIntent.setOutputUri] */
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R && Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            output?.let {
                ContextHelper.getAppContext()
                    .revokeUriPermission(it, Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
        }
    }
}