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

import android.content.res.Resources
import android.util.TypedValue

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/3/10 15:27
// Description: DensityUtils provide you with some methods to convert different dimensions.
// Documentation: [DensityUtils](https://sakurajimamaii.github.io/VastDocs/document/en/DensityUtils.html)

object DensityUtils {
    /**
     * Converting px to dp.
     *
     * @param pxValue a px value.
     * @return a dp value.
     * @since 0.0.1
     */
    @JvmStatic
    fun px2dp(pxValue: Float): Float {
        val scale: Float = Resources.getSystem().displayMetrics.density
        return pxValue / scale
    }

    /**
     * Converting dp to px.
     *
     * @param dpValue a dp value.
     * @return a px value.
     * @since 0.0.1
     */
    @JvmStatic
    fun dp2px(dpValue: Float): Float {
        val scale = Resources.getSystem().displayMetrics.density
        return dpValue * scale
    }

    /**
     * Converting px to sp.
     *
     * @param pxValue a px value.
     * @return a sp value.
     * @since 0.0.1
     */
    @JvmStatic
    fun px2sp(pxValue: Float): Float {
        val fontScale = Resources.getSystem().displayMetrics.scaledDensity
        return pxValue / fontScale
    }

    /**
     * Converting sp to px.
     *
     * @param spValue a sp value.
     * @return a px value.
     * @since 0.0.1
     */
    @JvmStatic
    fun sp2px(spValue: Float): Float {
        val fontScale = Resources.getSystem().displayMetrics.scaledDensity
        return spValue * fontScale
    }

    /**
     * Converting dp to sp.
     *
     * @param dpValue a dp value.
     * @return a sp value.
     * @since 0.0.1
     */
    @JvmStatic
    fun dp2sp(dpValue: Float): Float {
        val scale = Resources.getSystem().displayMetrics.density
        return px2sp(dpValue * scale)
    }

    /**
     * Converting sp to dp.
     *
     * @param spValue a sp value.
     * @return a dp value.
     * @since 0.0.5
     */
    @JvmStatic
    fun sp2dp(spValue: Float): Float {
        val fontScale = Resources.getSystem().displayMetrics.scaledDensity
        return px2dp(spValue * fontScale)
    }

    /**
     * @return The complex floating point value multiplied by the
     *     appropriate metrics(in density-independent pixels).
     * @since 0.0.1
     */
    @JvmStatic
    val Float.DP
        get() = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            this,
            Resources.getSystem().displayMetrics
        )

    /**
     * @return The complex floating point value multiplied by the
     *     appropriate metrics(in scale-independent pixels).
     * @since 0.0.1
     */
    @JvmStatic
    val Float.SP
        get() = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            this,
            Resources.getSystem().displayMetrics
        )

    /**
     * @return The complex floating point value multiplied by the
     *     appropriate metrics(in pixels).
     * @since 0.0.6
     */
    @JvmStatic
    val Float.PX
        get() = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_PX,
            this,
            Resources.getSystem().displayMetrics
        )

    /**
     * @return The complex floating point value multiplied by the
     *     appropriate metrics(in points).
     * @since 0.0.6
     */
    @JvmStatic
    val Float.PT
        get() = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_PT,
            this,
            Resources.getSystem().displayMetrics
        )

    /**
     * @return The complex floating point value multiplied by the
     *     appropriate metrics(in inches).
     * @since 0.0.6
     */
    @JvmStatic
    val Float.INCHES
        get() = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_IN,
            this,
            Resources.getSystem().displayMetrics
        )

    /**
     * @return The complex floating point value multiplied by the
     *     appropriate metrics(in millimeters).
     * @since 0.0.6
     */
    @JvmStatic
    val Float.MM
        get() = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_MM,
            this,
            Resources.getSystem().displayMetrics
        )
}