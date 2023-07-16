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

package com.ave.vastgui.tools.activity.app

import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import com.ave.vastgui.core.extension.NotNUllVar
import com.ave.vastgui.tools.activity.VastVbActivity
import com.ave.vastgui.tools.activity.app.VastCropActivity.Companion.ACTION
import com.ave.vastgui.tools.activity.app.VastCropActivity.Companion.AUTHORITY
import com.ave.vastgui.tools.activity.app.VastCropActivity.Companion.FRAME_TYPE
import com.ave.vastgui.tools.activity.app.VastCropActivity.Companion.OUTPUT_X
import com.ave.vastgui.tools.activity.app.VastCropActivity.Companion.OUTPUT_Y
import com.ave.vastgui.tools.activity.app.VastCropActivity.Companion.PREVIEW_HEIGHT
import com.ave.vastgui.tools.activity.app.VastCropActivity.Companion.PREVIEW_WIDTH
import com.ave.vastgui.tools.activity.app.VastCropActivity.Companion.RETURN_DATA
import com.ave.vastgui.tools.databinding.ActivityCropBinding
import com.ave.vastgui.tools.manager.filemgr.FileMgr
import com.ave.vastgui.tools.manager.mediafilemgr.ImageMgr
import com.ave.vastgui.tools.utils.DensityUtils.DP
import com.ave.vastgui.tools.utils.permission.requestPermission
import com.ave.vastgui.tools.view.cropview.CropFrameType
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/4/18
// Documentation: https://ave.entropy2020.cn/documents/VastTools/core-topics/ui/cropview/CropView/

/**
 * Vast crop activity
 *
 * @property ACTION The action of [VastCropActivity].
 * @property PREVIEW_WIDTH The width of the crop preview frame.
 * @property PREVIEW_HEIGHT The height of the crop preview frame.
 * @property OUTPUT_X The width of output image in pixels.
 * @property OUTPUT_Y The height of output image in pixels.
 * @property AUTHORITY When the [Build.VERSION.SDK_INT] is smaller than
 *     [Build.VERSION_CODES.R], You should set it for FileProvider.
 * @property RETURN_DATA True if return the crop image by a bitmap object,
 *     false otherwise.
 * @property FRAME_TYPE See [CropFrameType].
 * @property originalUri Set by [Intent.setData].
 * @property originalImageFile The temporary file of the original image, it
 *     is saved in the app internal cache file directory and it will be
 *     deleted when the VastCropActivity is destroy.
 * @since 0.5.0
 */
open class VastCropActivity : VastVbActivity<ActivityCropBinding>() {

    companion object {
        const val ACTION = "com.ave.vastgui.tools.action.CROP"
        const val PREVIEW_WIDTH = "preview_width"
        const val PREVIEW_HEIGHT = "preview_height"
        const val OUTPUT_X = "outputX"
        const val OUTPUT_Y = "outputY"
        const val AUTHORITY = "authority"
        const val RETURN_DATA = "return-data"
        const val FRAME_TYPE = "frameType"
        const val FRAME_TYPE_CIRCLE = "circle"
        const val FRAME_TYPE_SQUARE = "square"
        const val FRAME_TYPE_GRID9 = "grid9"
        const val FRAME_TYPE_RECTANGLE = "rectangle"
        const val RESULT_OK = 1
        const val RESULT_FAILED = 0
        const val RESULT_CANCELED = -1
        private const val DEFAULT_AUTHORITY = ""
        private const val DEFAULT_OUTPUT_X = -1F
        private const val DEFAULT_OUTPUT_Y = -1F
    }

    private val DEFAULT_PREVIEW_WIDTH = 100F.DP
    private val DEFAULT_PREVIEW_HEIGHT = 100F.DP
    private var originalUri: Uri? = null
    private var originalImageFile: File by NotNUllVar(true)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set size.
        val previewWidth = intent.getFloatExtra(PREVIEW_WIDTH, DEFAULT_PREVIEW_WIDTH)
        val previewHeight = intent.getFloatExtra(PREVIEW_HEIGHT, DEFAULT_PREVIEW_HEIGHT)
        getBinding().cropViewLayout.apply {
            setCropFrameSize(previewWidth, previewHeight)
        }

        // Get crop type.
        intent.getStringExtra(FRAME_TYPE)?.let {
            when (it) {
                FRAME_TYPE_CIRCLE -> getBinding().cropViewLayout.setCropFrameType(CropFrameType.CIRCLE)
                FRAME_TYPE_SQUARE -> getBinding().cropViewLayout.setCropFrameType(CropFrameType.SQUARE)
                FRAME_TYPE_GRID9 -> getBinding().cropViewLayout.setCropFrameType(CropFrameType.GRID9)
                FRAME_TYPE_RECTANGLE -> getBinding().cropViewLayout.setCropFrameType(CropFrameType.RECTANGLE)
                else -> throw IllegalArgumentException("Please set correct type.")
            }
        } ?: throw RuntimeException("Please set type.")

        // Get data
        originalUri = intent.data
        if (null != originalUri) {
            val inputStream = this@VastCropActivity.contentResolver.openInputStream(originalUri!!)
            if (null != inputStream) {
                val name = "temp_cache_${ImageMgr.getDefaultFileName(".jpg")}"
                originalImageFile = File(FileMgr.appInternalCacheDir(), name)
                FileMgr.saveFile(originalImageFile)
                val outputStream = FileOutputStream(originalImageFile)
                val bufferSize = 1024 * 8
                val buffer = ByteArray(bufferSize)
                val bufferedInputStream = BufferedInputStream(inputStream, bufferSize)
                var readLength: Int
                while (bufferedInputStream.read(buffer, 0, bufferSize)
                        .also { readLength = it } != -1
                ) {
                    outputStream.write(buffer, 0, readLength)
                }
                getBinding().cropViewLayout.setImageSrc(originalImageFile)
            } else {
                Log.d(getDefaultTag(), "Did not get the input stream.")
            }
        } else {
            Log.d(getDefaultTag(), "No image obtained.")
        }

        getBinding().activityCropBottomBar.cropSure.setOnClickListener {
            val outputX = intent.getFloatExtra(OUTPUT_X, DEFAULT_OUTPUT_X)
            val outputY = intent.getFloatExtra(OUTPUT_Y, DEFAULT_OUTPUT_Y)
            if (outputX != DEFAULT_OUTPUT_X && outputY != DEFAULT_OUTPUT_Y) {
                val authority = intent.getStringExtra(AUTHORITY) ?: DEFAULT_AUTHORITY
                getBinding().cropViewLayout.getCroppedImageUnderApi28(
                    outputX.toInt(),
                    outputY.toInt()
                )
                val bitmap: Bitmap? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    getBinding().cropViewLayout.getCroppedImageAboveApi28(
                        outputX.toInt(),
                        outputY.toInt()
                    )
                } else {
                    getBinding().cropViewLayout.getCroppedImageUnderApi28(
                        outputX.toInt(),
                        outputY.toInt()
                    )
                }
                requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE) {
                    granted = {
                        returnBitmapData(bitmap, authority)
                    }
                }
            } else {
                setResult(RESULT_FAILED, intent)
                finish()
            }
        }

        getBinding().activityCropBottomBar.cropExit.setOnClickListener {
            setResult(RESULT_CANCELED, intent)
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        setResult(RESULT_CANCELED, intent)
        FileMgr.deleteFile(originalImageFile)
    }

    private fun returnBitmapData(bitmap: Bitmap?, authority: String) {
        bitmap?.let { bmp ->
            val name = "crop_${ImageMgr.getDefaultFileName(".jpg")}"
            val path =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).path
            val bmpFile = File(path, name)
            // The Uri must be obtained before accessing, otherwise the file reading will fail.
            // The reason is as follows. Take file 1.jpg as an example. If you save file 1.jpg
            // first, read the bitmap data into it, and then call contentResolver.insert(),
            // the insert() method will insert a new record in order to avoid duplicate names
            // with the file name is 1(1).jpg, and the Uri corresponding to 1(1).jpg is returned.
            // So you cannot find the corresponding data through the returned Uri.

            // Get the uri
            val uri =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    ImageMgr.getFileUriAboveApi30(bmpFile)
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    ImageMgr.getFileUriAboveApi24(bmpFile, authority)
                } else {
                    ImageMgr.getFileUriOnApi23(bmpFile)
                }
            // Save bitmap
            uri?.apply {
                contentResolver.openOutputStream(this).use { outputStream ->
                    try {
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                        outputStream?.flush()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    } finally {
                        try {
                            outputStream?.close()
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }
                }
                intent.data = uri
                val returnData = intent.getBooleanExtra(RETURN_DATA, false)
                if (returnData) {
                    intent.putExtra(RETURN_DATA, bmp)
                }
                setResult(RESULT_OK, intent)
            } ?: setResult(RESULT_FAILED, intent)
            FileMgr.deleteFile(originalImageFile)
            finish()
        } ?: let {
            if (null != originalUri) {
                FileMgr.deleteFile(originalImageFile)
                setResult(RESULT_FAILED, intent)
                finish()
            }
        }
    }

}