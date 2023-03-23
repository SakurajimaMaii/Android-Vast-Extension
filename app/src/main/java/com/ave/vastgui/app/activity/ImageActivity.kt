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

import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import com.ave.vastgui.app.databinding.ActivityImageBinding
import com.ave.vastgui.tools.activity.VastVbActivity
import com.ave.vastgui.tools.activity.result.contract.CropPhotoContract
import com.ave.vastgui.tools.activity.result.contract.PickPhotoContract
import com.ave.vastgui.tools.activity.result.contract.TakePhotoContract
import com.ave.vastgui.tools.utils.LogUtils
import com.ave.vastgui.tools.utils.cropimage.CropIntent

class ImageActivity : VastVbActivity<ActivityImageBinding>() {

    private val getImage =
        registerForActivityResult(PickPhotoContract()) { it ->
            it?.apply { cropImage(this) }
        }

    private val cropPicture =
        registerForActivityResult(CropPhotoContract()) {
            val bitmap: Uri = it
                ?: throw RuntimeException("bitmap is null")
            getBinding().image.setImageURI(bitmap)
        }

    private val takePhoto =
        registerForActivityResult(TakePhotoContract()) {
            val bitmap: Uri = it
                ?: throw RuntimeException("bitmap is null")
            getBinding().image.setImageURI(bitmap)
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
            .setOutputFormat(Bitmap.CompressFormat.JPEG.toString())
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