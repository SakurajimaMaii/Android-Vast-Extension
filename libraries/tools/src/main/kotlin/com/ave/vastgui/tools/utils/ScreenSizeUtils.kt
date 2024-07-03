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

package com.ave.vastgui.tools.utils

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
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ave.vastgui.tools.content.ContextHelper

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/3/10 15:27
// Description: Help you to get the width and height of screen.
// Documentation: https://ave.entropy2020.cn/documents/VastTools/core-topics/information-get/screen-size-utils/

object ScreenSizeUtils {

    /**
     * Get the [ScreenSize] of your device in api 31.
     *
     * @return [ScreenSize] of your device.
     */
    @RequiresApi(Build.VERSION_CODES.S)
    private fun getScreenSizeApi31(context: Context): Pair<Int, Int> {
        val vm: WindowMetrics =
            (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).currentWindowMetrics
        return Pair(vm.bounds.width(), vm.bounds.height())
    }

    /**
     * Get the [ScreenSize] of your device in api 30.
     *
     * @return [ScreenSize] of your device.
     */
    @RequiresApi(Build.VERSION_CODES.R)
    @Suppress("DEPRECATION")
    private fun getScreenSizeApi30(context: Context): Pair<Int, Int> {
        val point = Point()
        context.display?.getRealSize(point)
        return Pair(point.x, point.y)
    }

    /**
     * Get the [ScreenSize] of your device below api 30.
     *
     * @return [ScreenSize] of your device.
     */
    @Suppress("DEPRECATION")
    private fun getScreenSizeApi30Below(context: Context): Pair<Int, Int> {
        val point = Point()
        val vm: WindowManager =
            context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        vm.defaultDisplay.getRealSize(point)
        return Pair(point.x, point.y)
    }

    private fun getMobileScreenSize(context: Context): Pair<Int, Int> {
        return when {
            (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) -> getScreenSizeApi31(context)
            (Build.VERSION.SDK_INT == Build.VERSION_CODES.R) -> getScreenSizeApi30(context)
            else -> getScreenSizeApi30Below(context)
        }
    }

    /**
     * Gets the screen height in the current screen orientation.
     *
     * @return The height of the screen(in pixels).
     * @since 0.5.2
     */
    @JvmStatic
    @JvmOverloads
    fun getMobileScreenHeight(context: Context = ContextHelper.getAppContext()) =
        getMobileScreenSize(context).second

    /**
     * Gets the screen width in the current screen orientation.
     *
     * @return The width of the screen(in pixels).
     * @since 0.5.2
     */
    @JvmStatic
    @JvmOverloads
    fun getMobileScreenWidth(context: Context = ContextHelper.getAppContext()) =
        getMobileScreenSize(context).first

    /**
     * Get screen orientation.
     *
     * @see Configuration.ORIENTATION_LANDSCAPE
     * @see Configuration.ORIENTATION_PORTRAIT
     * @since 0.5.2
     */
    @JvmStatic
    @JvmOverloads
    fun getScreenOrientation(context: Context = ContextHelper.getAppContext()) =
        context.resources.configuration.orientation

    /** Get screen orientation in degrees. By default, it returns 0. */
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
     * @since 0.5.2
     */
    fun getStatusBarHeight(activity: Activity): Int {
        val windowInsetsCompat =
            ViewCompat.getRootWindowInsets(activity.findViewById(android.R.id.content)) ?: return 0
        return windowInsetsCompat.getInsets(WindowInsetsCompat.Type.statusBars()).top
    }

    /**
     * Get the logical density of the display.
     *
     * @see [DisplayMetrics.density].
     * @since 0.5.2
     */
    @JvmStatic
    @JvmOverloads
    fun getDensity(context: Context = ContextHelper.getAppContext()) =
        context.resources.displayMetrics.density

    /** Get [activity] display. */
    @Suppress("DEPRECATION")
    private fun getDisplay(activity: Activity) =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            activity.display
        } else activity.windowManager.defaultDisplay

}