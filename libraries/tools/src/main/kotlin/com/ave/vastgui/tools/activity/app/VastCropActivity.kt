/*
 * Copyright 2024 VastGui guihy2019@gmail.com
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

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore.Images.Media
import android.webkit.MimeTypeMap
import androidx.core.content.ContextCompat
import com.ave.vastgui.tools.R
import com.ave.vastgui.tools.activity.VastVbActivity
import com.ave.vastgui.tools.activity.app.VastCropActivity.Companion.ACTION
import com.ave.vastgui.tools.activity.app.VastCropActivity.Companion.AUTHORITY
import com.ave.vastgui.tools.activity.app.VastCropActivity.Companion.FRAME_TYPE
import com.ave.vastgui.tools.activity.app.VastCropActivity.Companion.OUTPUT_X
import com.ave.vastgui.tools.activity.app.VastCropActivity.Companion.OUTPUT_Y
import com.ave.vastgui.tools.activity.app.VastCropActivity.Companion.PREVIEW_HEIGHT
import com.ave.vastgui.tools.activity.app.VastCropActivity.Companion.PREVIEW_WIDTH
import com.ave.vastgui.tools.activity.app.VastCropActivity.Companion.RESULT_DESTINATION_IMAGE_ERROR
import com.ave.vastgui.tools.activity.app.VastCropActivity.Companion.RESULT_FRAME_TYPE_ERROR
import com.ave.vastgui.tools.activity.app.VastCropActivity.Companion.RESULT_GET_CROP_IMAGE_ERROR
import com.ave.vastgui.tools.activity.app.VastCropActivity.Companion.RESULT_NULL_DATA_ERROR
import com.ave.vastgui.tools.activity.app.VastCropActivity.Companion.RESULT_PARAMETER_ERROR
import com.ave.vastgui.tools.activity.app.VastCropActivity.Companion.RESULT_PERMISSION_ERROR
import com.ave.vastgui.tools.activity.app.VastCropActivity.Companion.RESULT_SOURCE_IMAGE_ERROR
import com.ave.vastgui.tools.activity.app.VastCropActivity.Companion.RETURN_DATA
import com.ave.vastgui.tools.databinding.ActivityCropBinding
import com.ave.vastgui.tools.manager.filemgr.FileMgr
import com.ave.vastgui.tools.manager.mediafilemgr.ImageMgr
import com.ave.vastgui.tools.utils.DateUtils
import com.ave.vastgui.tools.utils.permission.Permission
import com.ave.vastgui.tools.view.cropview.CropFrameType
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/4/18
// Documentation: https://ave.entropy2020.cn/documents/VastTools/core-topics/ui/cropview/crop-view/

/**
 * [VastCropActivity].
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
 * @property originalImage The temporary file of the original image, it is
 *     saved in the app internal cache file directory and it will be
 *     deleted when the [VastCropActivity] is destroy.
 * @property RESULT_NULL_DATA_ERROR Returne when the [Intent.getData] is
 *     empty.
 * @property RESULT_FRAME_TYPE_ERROR Return when the value of [FRAME_TYPE]
 *     is error.
 * @property RESULT_SOURCE_IMAGE_ERROR Return when getting the file through
 *     [Intent.getData].
 * @property RESULT_GET_CROP_IMAGE_ERROR Return when getting the crop
 *     image.
 * @property RESULT_PERMISSION_ERROR Return when there is no
 *     [Permission.READ_MEDIA_IMAGES] permission.
 * @property RESULT_PARAMETER_ERROR Return when the parameter is error.
 * @property RESULT_DESTINATION_IMAGE_ERROR Return when get the destination
 *     image.
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
        const val RESULT_NULL_DATA_ERROR = 0x01
        const val RESULT_FRAME_TYPE_ERROR = 0x02
        const val RESULT_SOURCE_IMAGE_ERROR = 0x03
        const val RESULT_GET_CROP_IMAGE_ERROR = 0x04
        const val RESULT_PERMISSION_ERROR = 0x05
        const val RESULT_PARAMETER_ERROR = 0x06
        const val RESULT_DESTINATION_IMAGE_ERROR = 0x07
        const val RESULT_OK = 0x08
        const val RESULT_CANCELED = 0x09
        private const val DEFAULT_AUTHORITY = ""
        private const val DEFAULT_OUTPUT_X = -1F
        private const val DEFAULT_OUTPUT_Y = -1F
        private const val DEFAULT_IMAGE_EXTENSION = "jpg"
    }

    private val defaultPreviewWidth
        get() = resources.getDimension(R.dimen.default_crop_frame_width)
    private val defaultPreviewHeight
        get() = resources.getDimension(R.dimen.default_crop_frame_width)
    private var originalImage: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (null == intent.data) {
            finish(RESULT_NULL_DATA_ERROR)
        }

        // Set size.
        val previewWidth = intent.getFloatExtra(PREVIEW_WIDTH, defaultPreviewWidth)
        val previewHeight = intent.getFloatExtra(PREVIEW_HEIGHT, defaultPreviewHeight)
        getBinding().cropViewLayout.apply {
            setCropFrameSize(previewWidth, previewHeight)
        }

        // Get crop type.
        val frameType = intent.getStringExtra(FRAME_TYPE)
        if (null == frameType) finish(RESULT_FRAME_TYPE_ERROR)
        when (frameType) {
            FRAME_TYPE_CIRCLE -> getBinding().cropViewLayout.mCropFrameType = CropFrameType.CIRCLE
            FRAME_TYPE_SQUARE -> getBinding().cropViewLayout.mCropFrameType = CropFrameType.SQUARE
            FRAME_TYPE_GRID9 -> getBinding().cropViewLayout.mCropFrameType = CropFrameType.GRID9
            FRAME_TYPE_RECTANGLE -> getBinding().cropViewLayout.mCropFrameType =
                CropFrameType.RECTANGLE
        }

        // Get data
        val uri = intent.data!!
        val extension = MimeTypeMap
            .getSingleton()
            .getExtensionFromMimeType(contentResolver.getType(uri)) ?: DEFAULT_IMAGE_EXTENSION
        // Source image file to be cropped
        val source = File(
            cacheDir,
            "crop_cache_${ImageMgr.getDefaultFileName(".$extension")}"
        )
        // Destination image file
        val destination = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).path,
            "crop_${ImageMgr.getDefaultFileName(".$extension")}"
        )

        if (FileMgr.saveFile(source).isFailure) {
            finish(RESULT_SOURCE_IMAGE_ERROR)
        } else {
            originalImage = source
        }

        contentResolver.openInputStream(uri)?.let { inputStream ->
            val outputStream = FileOutputStream(originalImage)
            val bufferSize = 1024 * 8
            val buffer = ByteArray(bufferSize)
            val bufferedInputStream = BufferedInputStream(inputStream, bufferSize)
            var readLength: Int
            while (bufferedInputStream.read(buffer, 0, bufferSize)
                    .also { readLength = it } != -1
            ) {
                outputStream.write(buffer, 0, readLength)
            }
            getBinding().cropViewLayout.setImageSrc(originalImage!!)
        } ?: finish(RESULT_SOURCE_IMAGE_ERROR)

        getBinding().activityCropBottomBar.cropSure.setOnClickListener {
            val outputX = intent.getFloatExtra(OUTPUT_X, DEFAULT_OUTPUT_X)
            val outputY = intent.getFloatExtra(OUTPUT_Y, DEFAULT_OUTPUT_Y)
            if (outputX == DEFAULT_OUTPUT_X || outputY == DEFAULT_OUTPUT_Y) {
                finish(RESULT_PARAMETER_ERROR)
            }
            val authority = intent.getStringExtra(AUTHORITY) ?: DEFAULT_AUTHORITY
            val bitmap: Bitmap? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                getBinding()
                    .cropViewLayout
                    .getCroppedImageAboveApi28(outputX.toInt(), outputY.toInt())
            } else {
                getBinding()
                    .cropViewLayout
                    .getCroppedImageUnderApi28(outputX.toInt(), outputY.toInt())
            }

            if (null == bitmap) {
                finish(RESULT_GET_CROP_IMAGE_ERROR)
            }

            if (ContextCompat.checkSelfPermission(this, Permission.READ_MEDIA_IMAGES) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                returnBitmapData(bitmap!!, destination, authority)
            } else {
                finish(RESULT_PERMISSION_ERROR)
            }
        }

        getBinding().activityCropBottomBar.cropExit.setOnClickListener {
            finish(RESULT_CANCELED)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        finish(RESULT_CANCELED)
    }

    /**
     * Return bitmap data.
     *
     * @since 0.5.6
     */
    private fun returnBitmapData(bitmap: Bitmap, destination: File, authority: String) {
        // The Uri must be obtained before accessing, otherwise the file reading will fail.
        // The reason is as follows. Take file 1.jpg as an example. If you save file 1.jpg
        // first, read the bitmap data into it, and then call contentResolver.insert(),
        // the insert() method will insert a new record in order to avoid duplicate names
        // with the file name is 1(1).jpg, and the Uri corresponding to 1(1).jpg is returned.
        // So you cannot find the corresponding data through the returned Uri.

        // Get the uri
        val uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            ImageMgr.getFileUriAboveApi30 {
                put(Media.DATA, destination.absolutePath)
                put(Media.DISPLAY_NAME, destination.name)
                put(Media.MIME_TYPE, FileMgr.getMimeType(destination, "image/jpeg"))
                put(Media.DATE_ADDED, DateUtils.getCurrentTime(DateUtils.FORMAT_YYYY_MM_DD_HH_MM_SS))
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            ImageMgr.getFileUriAboveApi24(destination, authority)
        } else {
            ImageMgr.getFileUriOnApi23(destination)
        }

        if (null == uri) {
            finish(RESULT_DESTINATION_IMAGE_ERROR)
        }

        @Suppress("DEPRECATION") val format = when (FileMgr.getExtension(destination)) {
            "jpg" -> Bitmap.CompressFormat.JPEG to 100
            "png" -> Bitmap.CompressFormat.PNG to 0
            "webp" -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                Bitmap.CompressFormat.WEBP_LOSSLESS to 0
            } else Bitmap.CompressFormat.WEBP to 100

            else -> Bitmap.CompressFormat.JPEG to 100
        }

        // Save bitmap
        contentResolver.openOutputStream(uri!!)?.let { outputStream ->
            try {
                bitmap.compress(format.first, format.second, outputStream)
                outputStream.flush()
                outputStream.close()
            } catch (exception: IOException) {
                finish(RESULT_DESTINATION_IMAGE_ERROR)
            }
        } ?: finish(RESULT_DESTINATION_IMAGE_ERROR)

        intent.data = uri
        if (intent.getBooleanExtra(RETURN_DATA, false)) {
            intent.putExtra(RETURN_DATA, bitmap)
        }
        finish(RESULT_OK)
    }

    /**
     * Called when an error occurs.
     *
     * @since 0.5.6
     */
    private fun finish(resultCode: Int) {
        originalImage?.apply { FileMgr.deleteFile(this) }
        setResult(resultCode, intent)
        finish()
    }

}