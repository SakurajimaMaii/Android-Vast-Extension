/*
 * Copyright 2021-2025 VastGui
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

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore.Images.Media
import android.webkit.MimeTypeMap
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ave.vastgui.app.databinding.ActivityCropImageBinding
import com.ave.vastgui.app.log.logFactory
import com.ave.vastgui.tools.activity.VastVbActivity
import com.ave.vastgui.tools.activity.app.VastCropActivity
import com.ave.vastgui.tools.activity.result.contract.CropPhotoContract
import com.ave.vastgui.tools.activity.result.contract.PickPhotoContract
import com.ave.vastgui.tools.activity.result.contract.TakePhotoContract
import com.ave.vastgui.tools.io.asImageFile
import com.ave.vastgui.tools.io.getImageFile
import com.ave.vastgui.tools.utils.AppUtils
import com.ave.vastgui.tools.utils.DensityUtils.DP
import com.ave.vastgui.tools.utils.cropimage.CropIntent
import com.ave.vastgui.tools.utils.permission.Permission
import com.ave.vastgui.tools.utils.permission.requestPermission
import java.io.File

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Documentation: https://sakurajimamaii.github.io/AVE-DOC/documents/tools/core-topics/ui/cropview/crop-view/
// Documentation: https://sakurajimamaii.github.io/AVE-DOC/documents/tools/core-topics/intent/crop-intent/

class CropImageActivity : VastVbActivity<ActivityCropImageBinding>() {

    private val logger = logFactory("CropImageActivity")

    private var output: Uri? = null

    // 从相册选择图片之后调用自定义裁剪
    private val pickWithCropActivity =
        registerForActivityResult(PickPhotoContract()) { uri ->
            uri?.let { cropImageWithActivity(it) }
        }
    private val openWithCropActivity =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            logger.d { "调用自定义裁剪返回代码 ${result.resultCode}" }
            if (result.resultCode == VastCropActivity.RESULT_OK) {
                getBinding().image.setImageURI(result.data?.data)
            }
        }

    // 从相册选择图片之后通过 CropPhotoContract 调用系统裁剪
    private val pickWithCropPhotoContract =
        registerForActivityResult(PickPhotoContract()) { uri ->
            uri?.let { cropImageWithCropContract(it) }
        }
    private val openWithCropPhotoContract =
        registerForActivityResult(CropPhotoContract(AppUtils.getPackageName())) { uri ->
            getBinding().image.setImageURI(uri)
        }

    // 从相册选择图片之后通过 CropIntent 调用系统裁剪
    private val pickWithCropIntent =
        registerForActivityResult(PickPhotoContract()) { uri ->
            uri?.let { cropImageWithCropIntent(it) }
        }
    private val openWithCropIntent =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                getBinding().image.setImageURI(result.data?.data)
                removePermission()
            }
        }

    private val takePhoto =
        registerForActivityResult(TakePhotoContract("com.ave.vastgui.app")) {
            getBinding().image.setImageURI(it)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // NOTE https://medium.com/androiddevelopers/insets-handling-tips-for-android-15s-edge-to-edge-enforcement-872774e8839b
        ViewCompat.setOnApplyWindowInsetsListener(getBinding().root) { v, insets ->
            val padding = insets.getInsets(WindowInsetsCompat.Type.systemBars()
                    or WindowInsetsCompat.Type.displayCutout())
            v.setPadding(padding.left, padding.top, padding.right, padding.bottom)
            insets
        }


        requestPermission(Permission.READ_MEDIA_IMAGES) {
            granted = {
                getSnackbar().setText("$it 权限已授予").show()
            }
            denied = {
                getSnackbar().setText("$it 权限已被拒绝").show()
            }
            noMoreAsk = {
                getSnackbar().setText("$it 权限已被拒绝并不再询问").show()
            }
        }

        getBinding().openWithCropActivity.setOnClickListener {
            pickWithCropActivity.launch(null)
        }

        getBinding().openWithCropPhotocontract.setOnClickListener {
            pickWithCropPhotoContract.launch(null)
        }

        getBinding().openWithCropIntent.setOnClickListener {
            pickWithCropIntent.launch(null)
        }

        getBinding().openCamera.setOnClickListener {
            takePhoto.launch(null)
        }
    }

    /** 使用 [VastCropActivity] 来调用裁剪。 */
    private fun cropImageWithActivity(uri: Uri) {
        val intent = Intent(this, VastCropActivity::class.java).apply {
            data = uri
            putExtra(VastCropActivity.AUTHORITY, "com.ave.vastgui.app")
            putExtra(VastCropActivity.FRAME_TYPE, VastCropActivity.FRAME_TYPE_RECTANGLE)
            putExtra(VastCropActivity.PREVIEW_WIDTH, 300f.DP)
            putExtra(VastCropActivity.PREVIEW_HEIGHT, 300f.DP)
            putExtra(VastCropActivity.OUTPUT_X, 400f)
            putExtra(VastCropActivity.OUTPUT_Y, 400f)
        }
        openWithCropActivity.launch(intent)
    }

    /** 使用 [CropIntent] 和 [CropPhotoContract] 来调用系统裁剪。 */
    private fun cropImageWithCropContract(uri: Uri) {
        val cropIntent = CropIntent()
            .setData(uri)
            .setCorp(true)
            .setAspect(1, 1)
            .setOutput(200, 200)
            .setReturnData(false)
            .setOutputName(null)
            .setNoFaceDetection(true)
        openWithCropPhotoContract.launch(cropIntent)
    }

    /** 使用 [CropIntent] 来调用系统裁剪。 */
    private fun cropImageWithCropIntent(uri: Uri) {
        val dictionary = getImageFile().sharedPictures()
        val name = getImageFile().getDefaultFileName(".${getExtension(uri)}")
        val destination = File(dictionary, name)
        output = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            destination.asImageFile()
                .uri { put(Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES) }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            destination.asImageFile().uri("com.ave.vastgui.app")
        } else {
            destination.asImageFile().uri()
        }
        val cropIntent = CropIntent()
            .setData(uri)
            .setCorp(true)
            .setAspect(1, 1)
            .setOutput(200, 200)
            .setReturnData(false)
            .setNoFaceDetection(true)
            .setOutputUri(output)
            .getIntent()
        openWithCropIntent.launch(cropIntent)
    }

    private fun removePermission() {
        /** Revoke uri permission that is granted in [CropIntent.setOutputUri] */
        if (Build.VERSION.SDK_INT in (Build.VERSION_CODES.N until Build.VERSION_CODES.R) && null != output) {
            revokeUriPermission(
                output, Intent.FLAG_GRANT_WRITE_URI_PERMISSION or
                        Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
        }
    }

    private fun getExtension(uri: Uri): String =
        MimeTypeMap.getSingleton().getExtensionFromMimeType(contentResolver.getType(uri)) ?: "jpg"
}