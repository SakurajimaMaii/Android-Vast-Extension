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

package com.ave.vastgui.app.activity

import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContracts
import com.ave.vastgui.app.databinding.ActivityImageBinding
import com.ave.vastgui.tools.activity.VastVbActivity
import com.ave.vastgui.tools.activity.app.VastCropActivity
import com.ave.vastgui.tools.activity.result.contract.CropPhotoContract
import com.ave.vastgui.tools.activity.result.contract.PickPhotoContract
import com.ave.vastgui.tools.activity.result.contract.TakePhotoContract
import com.ave.vastgui.tools.manager.mediafilemgr.ImageMgr
import com.ave.vastgui.tools.utils.LogUtils
import com.ave.vastgui.tools.utils.cropimage.CropIntent

class ImageActivity : VastVbActivity<ActivityImageBinding>() {

    private val getImage =
        registerForActivityResult(PickPhotoContract()) {
//            it?.apply { cropImage(this) }
            val intent = Intent(this, VastCropActivity::class.java).apply {
                data = it
                putExtra(VastCropActivity.AUTHORITY,"com.ave.vastgui.app")
                putExtra(VastCropActivity.FRAME_TYPE, VastCropActivity.FRAME_TYPE_GRID9)
                putExtra(VastCropActivity.OUTPUT_X, 300f)
                putExtra(VastCropActivity.OUTPUT_Y, 300f)
            }

            cropImage.launch(intent)
        }

    private val cropImage =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == VastCropActivity.RESULT_OK) {
                val uri = it.data?.data
                getBinding().image.setImageURI(uri)
            }
        }

    private val cropPicture =
        registerForActivityResult(CropPhotoContract("com.ave.vastgui.app")) { uri ->
            uri?.let { path ->
                LogUtils.i(getDefaultTag(), path.toString())
                ImageMgr.getFileByUri(path)?.let {
                    LogUtils.d(
                        this@ImageActivity.getDefaultTag(),
                        "裁剪图片文件名 ${it.name} ${it.path}"
                    )
                }
            }
            getBinding().image.setImageURI(uri)
        }

    private val takePhoto =
        registerForActivityResult(TakePhotoContract("com.ave.vastgui.app")) {
            getBinding().image.setImageURI(it)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getBinding().openGallery.setOnClickListener {
            getImage.launch(null)
        }

        getBinding().openCamera.setOnClickListener {
            takePhoto.launch(null)
        }
    }

    private fun cropImage(uri: Uri) {
        val cropIntent = CropIntent()
            .setData(uri)
            .setCorp(true)
            .setAspect(1, 1)
            .setOutput(200, 200)
            .setReturnData(false)
            .setOutputName(null)
            .setNoFaceDetection(true)
        cropPicture.launch(cropIntent)
    }

    private fun search() {
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val cursor: Cursor? =
            contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                proj,
                null,
                null,
                null
            )
        if (cursor != null) {
            if (cursor.moveToNext()) {
                val columnIndex: Int = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                val path: String = cursor.getString(columnIndex)
                LogUtils.d(getDefaultTag(), path)
            }
            cursor.close()
        }
    }

    private fun delete() {
        contentResolver.delete(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            MediaStore.Images.Media.DISPLAY_NAME + "=?",
            arrayOf("avatar.jpg")
        )
    }
}