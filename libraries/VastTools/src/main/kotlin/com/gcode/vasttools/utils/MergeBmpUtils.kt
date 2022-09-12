/*
 * Copyright 2022 VastGui
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gcode.vasttools.utils

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2021/11/8 15:27
// Description: Provides some methods for merging Bitmaps.
// Documentation: [MergeBmpUtils](https://sakurajimamaii.github.io/VastDocs/document/en/MergeBmpUtils.html)

object MergeBmpUtils {
    /**
     * Merge the two bitmaps into one bitmap, based on the length and
     * width of the [bottomBitmap].
     *
     * @param bottomBitmap Bitmap at the bottom.
     * @param topBitmap Bitmap at the top.
     * @return `null` if one of the bitmaps is recycled.
     * @since 0.0.4
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
     * @param isBaseMax Whether to take the bitmap with large width as
     *     the standard, `true` means that the small image
     *     is stretched proportionally, `false` means that
     *     the larger image is compressed proportionally.
     * @return `null` if one of the bitmaps is recycled.
     * @since 0.0.4
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
     * @param isBaseMax Whether to take the bitmap with a large height
     *     as the standard, `true` means that the small image
     *     is stretched proportionally, `false` means that
     *     the larger image is compressed proportionally
     * @return `null` if one of the bitmaps is recycled.
     * @since 0.0.4
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
}