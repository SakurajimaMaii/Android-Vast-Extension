/*
 * Copyright 2021-2024 VastGui
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

@file:JvmName("GradientDrawableKt")

package com.ave.vastgui.tools.graphics

import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.GradientDrawable.Orientation
import android.os.Build
import androidx.annotation.ColorInt
import androidx.annotation.FloatRange
import androidx.annotation.RequiresApi

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/3/10 15:27
// Description: Help you to quickly build GradientDrawable.
// Documentation: https://ave.entropy2020.cn/documents/tools/core-topics/graphics/gradient-drawable/gradient-drawable/

/**
 * Left top corner radius(in pixels).
 *
 * @since 0.5.3
 */
@get:RequiresApi(Build.VERSION_CODES.N)
val GradientDrawable.leftTopRadius: Float
    get() = cornerRadii?.get(0) ?: 0f

/**
 * Left bottom corner radius(in pixels).
 *
 * @since 0.5.3
 */
@get:RequiresApi(Build.VERSION_CODES.N)
val GradientDrawable.leftBottomRadius: Float
    get() = cornerRadii?.get(6) ?: 0f

/**
 * Right top corner radius(in pixels).
 *
 * @since 0.5.3
 */
@get:RequiresApi(Build.VERSION_CODES.N)
val GradientDrawable.rightTopRadius: Float
    get() = cornerRadii?.get(2) ?: 0f

/**
 * Right bottom corner radius(in pixels).
 *
 * @since 0.5.3
 */
@get:RequiresApi(Build.VERSION_CODES.N)
val GradientDrawable.rightBottomRadius: Float
    get() = cornerRadii?.get(4) ?: 0f

/**
 * Set single color to background of GradientDrawable.
 *
 * @param color The color-int value.
 * @since 0.5.3
 */
fun GradientDrawable.setSingle(@ColorInt color: Int) {
    orientation = angle2Orientation(0)
    colors = intArrayOf(color, color)
}

/**
 * Set gradient color and gradient angle.
 *
 * @param angle Must be an integer multiple of 45.
 * @param startColor Start gradient color.
 * @param endColor End gradient color.
 * @since 0.5.3
 */
fun GradientDrawable.setGradient(
    angle: Int,
    @ColorInt startColor: Int,
    @ColorInt endColor: Int
) = apply {
    orientation = angle2Orientation(angle)
    colors = intArrayOf(startColor, endColor)
}

/**
 * Set gradient color and gradient orientation.
 *
 * @param orientation Gradient Orientation.
 * @param startColor Start gradient color.
 * @param endColor End gradient color.
 * @since 0.5.3
 */
fun GradientDrawable.setGradient(
    orientation: Orientation,
    @ColorInt startColor: Int,
    @ColorInt endColor: Int
) {
    this.orientation = orientation
    colors = intArrayOf(startColor, endColor)
}

/**
 * Set gradient color and gradient angle.
 *
 * @param angle Must be an integer multiple of 45.
 * @param startColor Start gradient color.
 * @param centerColor Center gradient color.
 * @param endColor End gradient color.
 * @since 0.5.3
 */
fun GradientDrawable.setGradient(
    angle: Int,
    @ColorInt startColor: Int,
    @ColorInt centerColor: Int,
    @ColorInt endColor: Int
) {
    this.orientation = angle2Orientation(angle)
    colors = intArrayOf(startColor, centerColor, endColor)
}

/**
 * Set gradient color and gradient orientation.
 *
 * @param orientation Gradient Orientation.
 * @param startColor Start gradient color.
 * @param centerColor Center gradient color.
 * @param endColor End gradient color.
 * @since 0.5.3
 */
fun GradientDrawable.setGradient(
    orientation: Orientation,
    @ColorInt startColor: Int,
    @ColorInt centerColor: Int,
    @ColorInt endColor: Int
) {
    this.orientation = orientation
    colors = intArrayOf(startColor, centerColor, endColor)
}

/**
 * Set specifies radii for each of the 4 corners.
 *
 * @param topLeft radius of top left corner.
 * @param topRight radius of top right corner.
 * @param bottomLeft radius of bottom left corner.
 * @param bottomRight radius of bottom right corner.
 * @since 0.5.3
 */
fun GradientDrawable.setCornerRadii(
    @FloatRange(from = 0.0) topLeft: Float,
    @FloatRange(from = 0.0) topRight: Float,
    @FloatRange(from = 0.0) bottomLeft: Float,
    @FloatRange(from = 0.0) bottomRight: Float
) {
    cornerRadii = floatArrayOf(
        topLeft, topLeft,
        topRight, topRight,
        bottomRight, bottomRight,
        bottomLeft, bottomLeft
    )
}

/**
 * Convert [angle] to [GradientDrawable.Orientation].
 *
 * @param angle Must be an integer multiple of 45. [Orientation.LEFT_RIGHT]
 *     otherwise.
 * @since 0.5.3
 */
fun GradientDrawable.angle2Orientation(angle: Int): Orientation {
    if (angle % 45 != 0)
        return Orientation.LEFT_RIGHT
    return when (angle) {
        0 -> Orientation.LEFT_RIGHT
        45 -> Orientation.BL_TR
        90 -> Orientation.BOTTOM_TOP
        135 -> Orientation.BR_TL
        180 -> Orientation.RIGHT_LEFT
        225 -> Orientation.TR_BL
        270 -> Orientation.TOP_BOTTOM
        315 -> Orientation.TL_BR
        else -> Orientation.LEFT_RIGHT
    }
}