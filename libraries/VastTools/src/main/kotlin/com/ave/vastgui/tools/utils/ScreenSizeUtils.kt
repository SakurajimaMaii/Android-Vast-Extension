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

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.graphics.Point
import android.os.Build
import android.util.DisplayMetrics
import android.view.Surface
import android.view.WindowManager
import android.view.WindowMetrics
import androidx.annotation.RequiresApi
import com.ave.vastgui.tools.helper.ContextHelper

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/3/10 15:27
// Description: Help you to get the width and height of screen.
// Documentation: https://ave.entropy2020.cn/documents/VastTools/core-topics/information-get/ScreenSizeUtils/

object ScreenSizeUtils {
    /**
     * The screen size.
     *
     * @property width width of the screen.
     * @property height height of the screen.
     */
    private data class ScreenSize(val width: Int, val height: Int)

    /**
     * ScreenSize of your device.
     *
     */
    private var mScreenSize = ScreenSize(0, 0)

    /**
     * Get the [ScreenSize] of your device in api 31.
     *
     * @return [ScreenSize] of your device.
     */
    @RequiresApi(Build.VERSION_CODES.S)
    private fun getScreenSizeApi31(context: Context): ScreenSize {
        val vm: WindowMetrics =
            (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).currentWindowMetrics
        return ScreenSize(vm.bounds.width(), vm.bounds.height())
    }

    /**
     * Get the [ScreenSize] of your device in api 30.
     *
     * @return [ScreenSize] of your device.
     */
    @RequiresApi(Build.VERSION_CODES.R)
    @Suppress("DEPRECATION")
    private fun getScreenSizeApi30(context: Context): ScreenSize {
        val orientation = context.resources?.configuration?.orientation
        val point = Point()
        context.display?.getRealSize(point)
        return if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            ScreenSize(point.x, point.y)
        } else {
            ScreenSize(point.y, point.x)
        }
    }

    /**
     * Get the [ScreenSize] of your device below api 30.
     *
     * @return [ScreenSize] of your device.
     */
    @Suppress("DEPRECATION")
    private fun getScreenSizeApi30Below(context: Context): ScreenSize {
        val orientation = context.resources?.configuration?.orientation
        val point = Point()
        val vm: WindowManager =
            context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        vm.defaultDisplay.getRealSize(point)
        return if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            ScreenSize(point.x, point.y)
        } else {
            ScreenSize(point.y, point.x)
        }
    }

    /**
     * Get the [ScreenSize] of your device.
     *
     * @return [ScreenSize] of your device.
     */
    private fun getMobileScreenSize(context: Context): ScreenSize {
        return when {
            (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) -> getScreenSizeApi31(context)
            (Build.VERSION.SDK_INT == Build.VERSION_CODES.R) -> getScreenSizeApi30(context)
            else -> getScreenSizeApi30Below(context)
        }
    }

    /**
     * Get mobile screen height.
     *
     * @return The height of the screen(in pixels).
     */
    @JvmStatic
    fun getMobileScreenHeight() = getMobileScreenSize(ContextHelper.getAppContext()).height

    /**
     * Get mobile screen width.
     *
     * @return The width of the screen(in pixels).
     */
    @JvmStatic
    fun getMobileScreenWidth() = getMobileScreenSize(ContextHelper.getAppContext()).width

    /**
     * Get screen orientation.
     *
     * @see Configuration.ORIENTATION_LANDSCAPE
     * @see Configuration.ORIENTATION_PORTRAIT
     */
    @JvmStatic
    fun getScreenOrientation() = ContextHelper.getAppContext().resources?.configuration?.orientation

    /**
     * Get screen orientation in degrees. By default, it returns 0.
     */
    @JvmStatic
    fun getScreenOrientationInDegree(activity: Activity): Int {
        val rotation = getDisplay(activity)?.rotation
        var degrees = 0
        when (rotation) {
            Surface.ROTATION_0 -> degrees = 0
            Surface.ROTATION_90 -> degrees = 90
            Surface.ROTATION_180 -> degrees = 180
            Surface.ROTATION_270 -> degrees = 270
        }
        return degrees
    }

    /**
     * Get the StatusBar height.
     *
     * @param activity the activity.
     * @return the height of the status height, 0 otherwise.
     */
    @SuppressLint("InternalInsetResource", "DiscouragedApi")
    @JvmStatic
    fun getStatusBarHeight(activity: Activity): Int {
        var result = 0
        val resourceId = activity.resources.getIdentifier(
            "status_bar_height",
            "dimen",
            "android"
        )
        if (resourceId > 0) {
            result = activity.resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

    /**
     * Get the logical density of the display.
     *
     * @see [DisplayMetrics.density]
     */
    @JvmStatic
    fun getDensity() = ContextHelper.getAppContext().resources.displayMetrics.density

    /**
     * Get [activity] display.
     */
    @Suppress("DEPRECATION")
    private fun getDisplay(activity: Activity) = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
        activity.display
    } else activity.windowManager.defaultDisplay
}