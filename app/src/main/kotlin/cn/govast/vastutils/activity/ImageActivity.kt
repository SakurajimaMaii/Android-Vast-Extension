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

package cn.govast.vastutils.activity

import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContracts
import cn.govast.vasttools.activity.VastVbActivity
import cn.govast.vasttools.manager.mediafilemgr.ImageMgr
import cn.govast.vasttools.utils.LogUtils
import cn.govast.vastutils.databinding.ActivityImageBinding
import java.io.File

class ImageActivity : VastVbActivity<ActivityImageBinding>() {

    private val getImage = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ activityResult->
        activityResult.data?.data?.let {
            cropImage(it)
        }
    }

    private val cropPicture =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val bitmap: Uri = it?.data?.data
                    ?: throw  RuntimeException("bitmap is null")
                getBinding().image.setImageURI(bitmap)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getBinding().openGallery.setOnClickListener {
            val chooseAvatarIntent = Intent(Intent.ACTION_PICK, null)
            //使用INTERNAL_CONTENT_URI只能显示存储在内部的照片
            chooseAvatarIntent.setDataAndType(
                MediaStore.Images.Media.INTERNAL_CONTENT_URI, "image/*"
            )
            getImage.launch(chooseAvatarIntent)
        }

        getBinding().openCamera.setOnClickListener {
            delete()
            search()
        }
    }

    private fun cropImage(uri: Uri){
        val intent = Intent("com.android.camera.action.CROP").apply {
            addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION)
            setDataAndType(uri, "image/*")
            // 设置裁剪
            putExtra("crop", "true")
            // aspectX aspectY 是宽高的比例
            putExtra("aspectX", 1)
            putExtra("aspectY", 1)
            // outputX outputY 是裁剪图片宽高
            putExtra("outputX", 300)
            putExtra("outputY", 300)
            val output = File("avatar.jpg").let {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
                    ImageMgr.getFileUriAboveApi30(it)
                } else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                    ImageMgr.getFileUriAboveApi24(it,null)
                } else {
                    ImageMgr.getFileUriOnApi23(it)
                }
            }
            putExtra(MediaStore.EXTRA_OUTPUT, output)
            putExtra("return-data", false) //是否将数据保留在Bitmap中返回
            putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString()) //输出格式，一般设为Bitmap格式及图片类型
            putExtra("noFaceDetection", true) //是否去除面部检测
        }
        cropPicture.launch(intent)
    }

    private fun search(){
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val cursor: Cursor? =
            contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, proj, null, null, null)
        if (cursor != null) {
            if (cursor.moveToNext()) {
                val columnIndex: Int = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                val path: String = cursor.getString(columnIndex)
                LogUtils.d(getDefaultTag(),path)
            }
            cursor.close()
        }
    }

    private fun delete(){
        contentResolver.delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,MediaStore.Images.Media.DISPLAY_NAME + "=?",
            arrayOf("avatar.jpg")
        )
    }
}