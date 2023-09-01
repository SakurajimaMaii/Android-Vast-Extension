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

package com.ave.vastgui.tools.utils.image

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Rect
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Base64
import androidx.annotation.DrawableRes
import androidx.annotation.IntRange
import androidx.core.content.ContextCompat
import com.ave.vastgui.tools.helper.ContextHelper
import com.ave.vastgui.tools.manager.filemgr.FileMgr
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2021/11/8 15:27
// Documentation: https://ave.entropy2020.cn/documents/VastTools/core-topics/images/BmpUtils/

object BmpUtils {
    /**
     * Merge the two bitmaps into one bitmap, based on the length and width of
     * the [bottomBitmap].
     *
     * @param topBitmap Bitmap at the top.
     * @param bottomBitmap Bitmap at the bottom.
     * @param position The position of the [topBitmap] in the return bitmap.
     * @return `null` if one of the bitmaps is recycled.
     * @throws IllegalArgumentException
     * @since 0.5.2
     */
    @JvmStatic
    @JvmOverloads
    @Throws(IllegalArgumentException::class)
    fun mergeBitmap(
        topBitmap: Bitmap,
        bottomBitmap: Bitmap,
        position: MergePosition = MergePosition.LT
    ): Bitmap {
        if (bottomBitmap.isRecycled || topBitmap.isRecycled)
            throw IllegalArgumentException("One of the topBitmap or bottomBitmap is recycled.")
        if (topBitmap.width > bottomBitmap.width || topBitmap.height > bottomBitmap.height)
            throw IllegalArgumentException("The size of the topBitmap is bigger than bottomBitmap.")
        val bitmap = bottomBitmap.copy(Bitmap.Config.ARGB_8888, true)
        val canvas = Canvas(bitmap)
        when (position) {
            MergePosition.LT ->
                canvas.drawBitmap(topBitmap, 0f, 0f, null)

            MergePosition.LB ->
                canvas.drawBitmap(
                    topBitmap,
                    0f,
                    (bottomBitmap.height - topBitmap.height).toFloat(),
                    null
                )

            MergePosition.RT ->
                canvas.drawBitmap(
                    topBitmap,
                    (bottomBitmap.width - topBitmap.width).toFloat(),
                    0f, null
                )

            MergePosition.RB ->
                canvas.drawBitmap(
                    topBitmap,
                    (bottomBitmap.width - topBitmap.width).toFloat(),
                    (bottomBitmap.height - topBitmap.height).toFloat(),
                    null
                )

            MergePosition.CENTER ->
                canvas.drawBitmap(
                    topBitmap,
                    (bottomBitmap.width - topBitmap.width) / 2f,
                    (bottomBitmap.height - topBitmap.height) / 2f,
                    null
                )
        }
        return bitmap
    }

    /**
     * Merge two bitmaps into one bitmap, splicing left and right.
     *
     * @param leftBitmap Bitmap shown on the left.
     * @param rightBitmap Bitmap shown on the right.
     * @return `null` if one of the bitmaps is recycled.
     * @throws IllegalArgumentException
     * @since 0.5.2
     */
    @JvmStatic
    @JvmOverloads
    @Throws(IllegalArgumentException::class)
    fun mergeBitmapLR(
        leftBitmap: Bitmap,
        rightBitmap: Bitmap,
        scale: MergeScale = MergeScale.SMALL_ENLARGE
    ): Bitmap {
        if (leftBitmap.isRecycled || rightBitmap.isRecycled) {
            throw IllegalArgumentException("One of the topBitmap or bottomBitmap is recycled.")
        }

        // The height of the merged bitmap.
        val height: Int = when (scale) {
            MergeScale.SMALL_ENLARGE ->
                leftBitmap.height.coerceAtLeast(rightBitmap.height)

            MergeScale.BIG_REDUCE ->
                leftBitmap.height.coerceAtMost(rightBitmap.height)
        }

        // Bitmap after merged.
        val tempBitmapL: Bitmap = if (leftBitmap.height != height) {
            Bitmap.createScaledBitmap(
                leftBitmap,
                (height * 1f / leftBitmap.height * leftBitmap.width).toInt(),
                height,
                false
            )
        } else leftBitmap
        val tempBitmapR: Bitmap = if (rightBitmap.height != height) {
            Bitmap.createScaledBitmap(
                rightBitmap,
                (height * 1f / rightBitmap.height * rightBitmap.width).toInt(), height, false
            )
        } else rightBitmap

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
     * @return `null` if one of the bitmaps is recycled.
     * @throws IllegalArgumentException
     * @since 0.5.2
     */
    @JvmStatic
    @JvmOverloads
    @Throws(IllegalArgumentException::class)
    fun mergeBitmapTB(
        topBitmap: Bitmap,
        bottomBitmap: Bitmap,
        scale: MergeScale = MergeScale.SMALL_ENLARGE
    ): Bitmap? {
        if (topBitmap.isRecycled || bottomBitmap.isRecycled) {
            throw IllegalArgumentException("One of the topBitmap or bottomBitmap is recycled.")
        }

        val width: Int = when (scale) {
            MergeScale.SMALL_ENLARGE ->
                topBitmap.width.coerceAtLeast(bottomBitmap.width)

            MergeScale.BIG_REDUCE ->
                topBitmap.width.coerceAtMost(bottomBitmap.width)
        }

        val tempBitmapT: Bitmap = if (topBitmap.width != width) {
            Bitmap.createScaledBitmap(
                topBitmap, width,
                (topBitmap.height * 1f / topBitmap.width * width).toInt(), false
            )
        } else topBitmap
        val tempBitmapB: Bitmap = if (bottomBitmap.width != width) {
            Bitmap.createScaledBitmap(
                bottomBitmap, width,
                (bottomBitmap.height * 1f / bottomBitmap.width * width).toInt(), false
            )
        } else bottomBitmap

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
            if (result.isFailure) return null
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
    @Throws(IllegalArgumentException::class)
    fun scaleBitmap(
        bitmap: Bitmap,
        @IntRange(from = 0) reqWidth: Int,
        @IntRange(from = 0) reqHeight: Int
    ): Bitmap {
        if (bitmap.isRecycled) throw IllegalArgumentException("Parameter bitmap is recycled.")
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
     * @throws RuntimeException
     * @since 0.2.0
     */
    @JvmStatic
    @JvmOverloads
    @Throws(RuntimeException::class)
    fun getBitmapFromDrawable(
        @DrawableRes id: Int,
        context: Context = ContextHelper.getAppContext()
    ): Bitmap {
        val drawable = ContextCompat.getDrawable(context, id)
            ?: throw RuntimeException("Can't get the drawable by $id.")
        return getBitmapFromDrawable(drawable)
    }

    /** Convert drawable to bitmap. */
    @JvmStatic
    fun getBitmapFromDrawable(drawable: Drawable): Bitmap {
        return when (drawable) {
            is BitmapDrawable -> {
                drawable.bitmap
            }

            else -> {
                val w: Int = drawable.intrinsicWidth
                val h: Int = drawable.intrinsicHeight
                val bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
                val canvas = Canvas(bitmap)
                drawable.setBounds(0, 0, w, h)
                drawable.draw(canvas)
                bitmap
            }
        }
    }

    /**
     * Get bitmap width and height.
     *
     * @param decoder Decode a bitmap with option.
     * @since 0.5.2
     */
    @JvmStatic
    inline fun getBitmapWidthHeight(decoder: (BitmapFactory.Options) -> Unit): Pair<Int,Int> {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        decoder(options)
        return Pair(options.outWidth, options.outHeight)
    }

}