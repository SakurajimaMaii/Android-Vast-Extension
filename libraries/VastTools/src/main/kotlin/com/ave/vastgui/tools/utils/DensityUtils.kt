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

import android.content.res.Resources
import android.os.Build
import android.util.DisplayMetrics
import android.util.TypedValue

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/3/10 15:27
// Documentation: https://ave.entropy2020.cn/documents/VastTools/core-topics/app-resources/density-utils/

object DensityUtils {
    /**
     * Converting px to dp.
     *
     * @param pxValue a px value.
     * @return a dp value.
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
     */
    @JvmStatic
    fun dp2px(dpValue: Float): Float {
        val scale = Resources.getSystem().displayMetrics.density
        return dpValue * scale
    }

    /**
     * Converting px to sp.
     *
     * The [DisplayMetrics.scaledDensity] is deprecate from
     * [Build.VERSION_CODES.UPSIDE_DOWN_CAKE], click
     * [scaledDensity](https://developer.android.com/reference/android/util/DisplayMetrics#scaledDensity)
     * to get more information.
     *
     * @param pxValue a px value.
     * @return a sp value.
     */
    @JvmStatic
    fun px2sp(pxValue: Float): Float =
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            @Suppress("DEPRECATION")
            val fontScale = Resources.getSystem().displayMetrics.scaledDensity
            pxValue / fontScale
        } else TypedValue.deriveDimension(
            TypedValue.COMPLEX_UNIT_SP,
            pxValue,
            Resources.getSystem().displayMetrics
        )


    /**
     * Converting sp to px.
     *
     * The [DisplayMetrics.scaledDensity] is deprecate from
     * [Build.VERSION_CODES.UPSIDE_DOWN_CAKE], click
     * [scaledDensity](https://developer.android.com/reference/android/util/DisplayMetrics#scaledDensity)
     * to get more information.
     *
     * @param spValue a sp value.
     * @return a px value.
     */
    @JvmStatic
    fun sp2px(spValue: Float): Float =
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            @Suppress("DEPRECATION")
            val fontScale = Resources.getSystem().displayMetrics.scaledDensity
            spValue * fontScale
        } else {
            spValue.SP
        }

    /**
     * Converting dp to sp.
     *
     * @param dpValue a dp value.
     * @return a sp value.
     */
    @JvmStatic
    fun dp2sp(dpValue: Float): Float {
        val scale = Resources.getSystem().displayMetrics.density
        return px2sp(dpValue * scale)
    }

    /**
     * Converting sp to dp.
     *
     * The [DisplayMetrics.scaledDensity] is deprecate from
     * [Build.VERSION_CODES.UPSIDE_DOWN_CAKE], click
     * [scaledDensity](https://developer.android.com/reference/android/util/DisplayMetrics#scaledDensity)
     * to get more information.
     *
     * @param spValue a sp value.
     * @return a dp value.
     */
    @JvmStatic
    fun sp2dp(spValue: Float): Float =
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            @Suppress("DEPRECATION")
            val fontScale = Resources.getSystem().displayMetrics.scaledDensity
            px2dp(spValue * fontScale)
        } else TypedValue.deriveDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            spValue.SP,
            Resources.getSystem().displayMetrics
        )

    /**
     * @return The complex floating point value multiplied by the appropriate
     *     metrics(in density-independent pixels).
     */
    @JvmStatic
    val Float.DP
        get() = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            this,
            Resources.getSystem().displayMetrics
        )

    /**
     * @return The complex floating point value multiplied by the appropriate
     *     metrics(in scale-independent pixels).
     */
    @JvmStatic
    val Float.SP
        get() = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            this,
            Resources.getSystem().displayMetrics
        )

    /**
     * @return The complex floating point value multiplied by the appropriate
     *     metrics(in pixels).
     */
    @JvmStatic
    val Float.PX
        get() = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_PX,
            this,
            Resources.getSystem().displayMetrics
        )

    /**
     * @return The complex floating point value multiplied by the appropriate
     *     metrics(in points).
     */
    @JvmStatic
    val Float.PT
        get() = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_PT,
            this,
            Resources.getSystem().displayMetrics
        )

    /**
     * @return The complex floating point value multiplied by the appropriate
     *     metrics(in inches).
     */
    @JvmStatic
    val Float.INCHES
        get() = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_IN,
            this,
            Resources.getSystem().displayMetrics
        )

    /**
     * @return The complex floating point value multiplied by the appropriate
     *     metrics(in millimeters).
     */
    @JvmStatic
    val Float.MM
        get() = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_MM,
            this,
            Resources.getSystem().displayMetrics
        )

}