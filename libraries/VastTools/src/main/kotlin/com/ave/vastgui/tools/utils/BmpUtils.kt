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

package com.ave.vastgui.tools.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Rect
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.VectorDrawable
import android.util.Base64
import androidx.annotation.DrawableRes
import androidx.annotation.IntRange
import androidx.core.content.ContextCompat
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import com.ave.vastgui.tools.helper.ContextHelper
import com.ave.vastgui.tools.manager.filemgr.FileMgr
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import kotlin.math.roundToInt

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2021/11/8 15:27
// Description: Provides some methods for merging Bitmaps.
// Documentation: https://ave.entropy2020.cn/documents/VastTools/core-topics/images/BmpUtils/

object BmpUtils {
    /**
     * Merge the two bitmaps into one bitmap, based on the length and width of
     * the [bottomBitmap].
     *
     * @param bottomBitmap Bitmap at the bottom.
     * @param topBitmap Bitmap at the top.
     * @return `null` if one of the bitmaps is recycled.
     */
    @JvmStatic
    fun mergeBmp(bottomBitmap: Bitmap, topBitmap: Bitmap): Bitmap? {
        if (bottomBitmap.isRecycled || topBitmap.isRecycled
        ) {
            return null
        }
        val bitmap = bottomBitmap.copy(Bitmap.Config.ARGB_8888, true)
        val canvas = Canvas(bitmap)
        val baseRect = Rect(0, 0, bottomBitmap.width, bottomBitmap.height)
        canvas.drawBitmap(topBitmap, baseRect, baseRect, null)
        return bitmap
    }

    /**
     * Merge two bitmaps into one bitmap, splicing left and right.
     *
     * @param leftBitmap Bitmap shown on the left.
     * @param rightBitmap Bitmap shown on the right.
     * @param isBaseMax Whether to take the bitmap with large width as the
     *     standard, `true` means that the small image is stretched
     *     proportionally, `false` means that the larger image is compressed
     *     proportionally.
     * @return `null` if one of the bitmaps is recycled.
     */
    @JvmStatic
    fun mergeBmpLR(leftBitmap: Bitmap, rightBitmap: Bitmap, isBaseMax: Boolean): Bitmap? {
        if (leftBitmap.isRecycled || rightBitmap.isRecycled) {
            return null
        }
        val height: Int = if (isBaseMax) {
            leftBitmap.height.coerceAtLeast(rightBitmap.height)
        } else {
            leftBitmap.height.coerceAtMost(rightBitmap.height)
        } // The height of the merged bitmap.

        // Bitmap after merged.
        var tempBitmapL: Bitmap = leftBitmap
        var tempBitmapR: Bitmap = rightBitmap
        if (leftBitmap.height != height) {
            tempBitmapL = Bitmap.createScaledBitmap(
                leftBitmap,
                (leftBitmap.width * 1f / leftBitmap.height * height).toInt(), height, false
            )
        } else if (rightBitmap.height != height) {
            tempBitmapR = Bitmap.createScaledBitmap(
                rightBitmap,
                (rightBitmap.width * 1f / rightBitmap.height * height).toInt(), height, false
            )
        }

        // The width of the merged bitmap.
        val width = tempBitmapL.width + tempBitmapR.width

        // Define the output bitmap.
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        // Parameters that need to be drawn for the two bitmaps after scaling.
        val leftRect = Rect(0, 0, tempBitmapL.width, tempBitmapL.height)
        val rightRect = Rect(0, 0, tempBitmapR.width, tempBitmapR.height)

        // The position where the picture on the right needs to be drawn is offset to
        // the right by the width of the picture on the left, and the height is the same
        val rightRectT = Rect(tempBitmapL.width, 0, width, height)
        canvas.drawBitmap(tempBitmapL, leftRect, leftRect, null)
        canvas.drawBitmap(tempBitmapR, rightRect, rightRectT, null)
        return bitmap
    }


    /**
     * Merge two bitmaps into one bitmap, splicing up and down.
     *
     * @param topBitmap Bitmap shown on the top.
     * @param bottomBitmap Bitmap shown on the bottom.
     * @param isBaseMax Whether to take the bitmap with a large height as the
     *     standard, `true` means that the small image is stretched
     *     proportionally, `false` means that the larger image is compressed
     *     proportionally
     * @return `null` if one of the bitmaps is recycled.
     */
    @JvmStatic
    fun mergeBmpTB(topBitmap: Bitmap, bottomBitmap: Bitmap, isBaseMax: Boolean): Bitmap? {
        if (topBitmap.isRecycled || bottomBitmap.isRecycled
        ) {
            return null
        }
        val width: Int = if (isBaseMax) {
            if (topBitmap.width > bottomBitmap.width) topBitmap.width else bottomBitmap.width
        } else {
            if (topBitmap.width < bottomBitmap.width) topBitmap.width else bottomBitmap.width
        }
        var tempBitmapT: Bitmap = topBitmap
        var tempBitmapB: Bitmap = bottomBitmap
        if (topBitmap.width != width) {
            tempBitmapT = Bitmap.createScaledBitmap(
                topBitmap, width,
                (topBitmap.height * 1f / topBitmap.width * width).toInt(), false
            )
        } else if (bottomBitmap.width != width) {
            tempBitmapB = Bitmap.createScaledBitmap(
                bottomBitmap, width,
                (bottomBitmap.height * 1f / bottomBitmap.width * width).toInt(), false
            )
        }
        val height = tempBitmapT.height + tempBitmapB.height
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val topRect = Rect(0, 0, tempBitmapT.width, tempBitmapT.height)
        val bottomRect = Rect(0, 0, tempBitmapB.width, tempBitmapB.height)
        val bottomRectT = Rect(0, tempBitmapT.height, width, height)
        canvas.drawBitmap(tempBitmapT, topRect, topRect, null)
        canvas.drawBitmap(tempBitmapB, bottomRect, bottomRectT, null)
        return bitmap
    }

    /**
     * Store the Bitmap object under the local cache folder.
     *
     * @param bitmap The bitmap need to store.
     * @param file The file to store the bitmap.
     * @param format The format of the compressed image.
     * @param quality Hint to the compressor, 0-100. The value is interpreted
     *     differently depending on the [Bitmap.CompressFormat].
     * @return The file path after storage, or null if the storage fails.
     */
    @JvmStatic
    @JvmOverloads
    fun saveBitmapAsFile(
        bitmap: Bitmap,
        file: File,
        format: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG,
        @IntRange(from = 0, to = 100) quality: Int = 100
    ): File? {
        FileMgr.saveFile(file).let { result ->
            if(result.isFailure) return null
        }
        var fos: FileOutputStream? = null
        try {
            fos = FileOutputStream(file)
            bitmap.compress(format, quality, fos)
            fos.flush()
            return file
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                fos?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return null
    }

    /** Get bitmap from base64. */
    @JvmStatic
    fun getBitmapFromBase64(base64: String): Bitmap {
        val decode: ByteArray = Base64.decode(base64.split(",")[1], Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decode, 0, decode.size)
    }

    /**
     * Scale bitmap
     *
     * @since 0.5.0
     */
    @JvmStatic
    fun scaleBitmap(bitmap: Bitmap, reqWidth: Int, reqHeight: Int): Bitmap? {
        val width = bitmap.width
        val height = bitmap.height
        val matrix = Matrix()
        val scaleWidth = reqWidth.toFloat() / width
        val scaleHeight = reqHeight.toFloat() / height
        matrix.postScale(scaleWidth, scaleHeight)
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, false)
    }

    /**
     * Convert drawable to bitmap.
     *
     * @since 0.2.0
     */
    @JvmOverloads
    @JvmStatic
    fun getBitmapFromDrawable(
        @DrawableRes id: Int,
        context: Context = ContextHelper.getAppContext()
    ): Bitmap {
        val drawable = ContextCompat.getDrawable(context, id)
        return getBitmapFromDrawable(drawable)
    }

    /** Convert drawable to bitmap. */
    @JvmStatic
    fun getBitmapFromDrawable(drawable: Drawable?): Bitmap {
        return when (drawable) {
            is BitmapDrawable -> {
                drawable.bitmap
            }

            is VectorDrawable, is VectorDrawableCompat -> {
                val w: Int = drawable.intrinsicWidth
                val h: Int = drawable.intrinsicHeight
                val bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
                val canvas = Canvas(bitmap)
                drawable.setBounds(0, 0, w, h)
                drawable.draw(canvas)
                bitmap
            }

            else -> {
                throw IllegalArgumentException("unsupported drawable type")
            }
        }
    }

    /**
     * Get bitmap width and height.
     *
     * @param path complete path name for the file to be decoded.
     * @since 0.5.0
     */
    @JvmStatic
    fun getBitmapWidthHeight(path: String): IntArray {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(path, options)
        return intArrayOf(options.outWidth, options.outHeight)
    }

    /**
     * Get bitmap with require size.
     *
     * @since 0.5.0
     */
    @JvmStatic
    internal fun getBitmapWithRequireSize(path: String, reqWidth: Int, reqHeight: Int): Bitmap? {
        // First decode with inJustDecodeBounds=true to check dimensions
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        options.inPreferredConfig = Bitmap.Config.RGB_565
        // bitmap is null
        BitmapFactory.decodeFile(path, options)

        // Calculate inSampleSize
        val inSampleSize: Int = calculateRequireSize(options, reqWidth, reqHeight)
        options.inSampleSize = inSampleSize
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false
        return BitmapFactory.decodeFile(path, options)
    }

    /**
     * Calculate require size
     *
     * @since 0.5.0
     */
    private fun calculateRequireSize(
        options: BitmapFactory.Options,
        reqWidth: Int, reqHeight: Int
    ): Int {
        // Raw height and width of image
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1
        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and
            // width
            val heightRatio = (height.toFloat() / reqHeight.toFloat()).roundToInt()
            val widthRatio = (width.toFloat() / reqWidth.toFloat()).roundToInt()

            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            val ratio = if (heightRatio < widthRatio) heightRatio else widthRatio
            inSampleSize =
                if (ratio < 3) ratio else if (ratio < 6.5) 4 else if (ratio < 8) 8 else ratio
        }
        return inSampleSize
    }

}