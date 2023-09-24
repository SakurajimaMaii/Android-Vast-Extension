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

import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import androidx.annotation.RequiresApi
import com.ave.vastgui.core.extension.cast
import com.ave.vastgui.tools.content.ContextHelper

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/3/10 15:27
// Description: Help you to get app information.
// Documentation: https://ave.entropy2020.cn/documents/VastTools/core-topics/information-get/app-utils/

object AppUtils {

    /**
     * Get app name.
     *
     * @param callback The default value.
     * @return App name,like VastUtilsSampleDemo or [callback] if [callback] is
     *     not null,throw exception otherwise.
     * @throws Resources.NotFoundException
     * @throws PackageManager.NameNotFoundException
     * @since 0.5.1
     */
    @JvmStatic
    @Throws(Resources.NotFoundException::class, PackageManager.NameNotFoundException::class)
    fun getAppName(callback: String? = null): String = try {
        val labelRes = getPackageInfo().applicationInfo.labelRes
        ContextHelper.getAppContext().resources.getString(labelRes)
    } catch (exception: Exception) {
        callback ?: throw exception
    }


    /**
     * Get version name.
     *
     * @param callback The default value.
     * @return The version name of the current application,like 1.0 or callback
     *     if callback is not null,throw exception otherwise.
     * @throws PackageManager.NameNotFoundException
     * @since 0.5.1
     */
    @JvmStatic
    @Throws(PackageManager.NameNotFoundException::class)
    fun getVersionName(callback: String? = null): String = try {
        getPackageInfo().versionName
    } catch (exception: Exception) {
        callback ?: throw exception
    }


    /**
     * Get version code.
     *
     * @param callback The default value.
     * @return The version code of the current application,like 1 or callback
     *     if callback is not null,throw exception otherwise.
     * @throws PackageManager.NameNotFoundException
     * @since 0.5.1
     */
    @JvmStatic
    @Throws(PackageManager.NameNotFoundException::class)
    fun getVersionCode(callback: Int? = null) =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getVersionCodeApi28Above(callback)
        } else {
            getVersionCodeApi28Down(callback)
        }


    /**
     * Get VersionCode (in Api 28 Above)
     *
     * @return The version code of the current application.
     * @since 0.5.1
     */
    @RequiresApi(Build.VERSION_CODES.P)
    @Throws(PackageManager.NameNotFoundException::class)
    private fun getVersionCodeApi28Above(callback: Int? = null) = try {
        getPackageInfo().longVersionCode.toInt()
    } catch (exception: Exception) {
        callback ?: throw exception
    }

    /**
     * Get VersionCode (in Api 28 Down)
     *
     * @return The version code of the current application.
     * @since 0.5.1
     */
    @Throws(PackageManager.NameNotFoundException::class)
    private fun getVersionCodeApi28Down(callback: Int? = null): Int = try {
        @Suppress("DEPRECATION") getPackageInfo().versionCode
    } catch (exception: Exception) {
        callback ?: throw exception
    }


    /**
     * Get package name.
     *
     * @param callback The default value.
     * @return The package name of the application,like com.gcode.vastutils or
     *     callback if callback is not null,throw exception otherwise.
     * @throws PackageManager.NameNotFoundException
     * @since 0.5.1
     */
    @JvmStatic
    @Throws(PackageManager.NameNotFoundException::class)
    fun getPackageName(callback: String? = null): String = try {
        getPackageInfo().packageName
    } catch (exception: Exception) {
        callback ?: throw exception
    }


    /**
     * Get app icon bitmap.
     *
     * @return The icon of the application or [callback] if [callback] is not
     *     null,throw exception otherwise.
     * @throws PackageManager.NameNotFoundException
     * @since 0.5.1
     */
    @JvmStatic
    @Throws(PackageManager.NameNotFoundException::class)
    fun getAppBitmap(callback: Bitmap? = null): Bitmap = try {
        val packageManager = ContextHelper.getAppContext().applicationContext.packageManager
        val applicationInfo = getApplicationInfo()
        val drawable = packageManager.getApplicationIcon(applicationInfo)
        // Fix AdaptiveIconDrawable cannot be cast to BitmapDrawable.
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val bitmap =
                Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            BitmapDrawable(ContextHelper.getAppContext().resources, bitmap).bitmap
        } else {
            cast<BitmapDrawable>(drawable).bitmap
        }
    } catch (exception: Exception) {
        callback ?: throw exception
    }


    /**
     * Get app debug mode.
     *
     * @return True if the app is debuggable.false otherwise.
     */
    @JvmStatic
    fun getAppDebug() = try {
        val info = ContextHelper.getAppContext().applicationInfo
        info.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0
    } catch (exception: Exception) {
        false
    }


    /**
     * Get memory information.
     *
     * @return The maxMemory, freeMemory, totalMemory of the application in
     *     turn.
     */
    @JvmStatic
    fun getMemoryInfo(): Triple<Double, Double, Double> {
        return Triple(
            Runtime.getRuntime().maxMemory() * 1.0 / (1024 * 1024),
            Runtime.getRuntime().freeMemory() * 1.0 / (1024 * 1024),
            Runtime.getRuntime().totalMemory() * 1.0 / (1024 * 1024)
        )
    }

    @Throws(PackageManager.NameNotFoundException::class)
    private fun getPackageInfo(): PackageInfo {
        val packageManager: PackageManager = ContextHelper.getAppContext().packageManager
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            packageManager.getPackageInfo(
                ContextHelper.getAppContext().packageName, PackageManager.PackageInfoFlags.of(0)
            )
        } else {
            @Suppress("DEPRECATION") packageManager.getPackageInfo(
                ContextHelper.getAppContext().packageName, 0
            )
        }
    }

    @Throws(PackageManager.NameNotFoundException::class)
    private fun getApplicationInfo(): ApplicationInfo {
        val packageManager: PackageManager = ContextHelper.getAppContext().packageManager
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            packageManager.getApplicationInfo(
                ContextHelper.getAppContext().packageName, PackageManager.ApplicationInfoFlags.of(0)
            )
        } else {
            @Suppress("DEPRECATION") packageManager.getApplicationInfo(
                ContextHelper.getAppContext().packageName, 0
            )
        }
    }
}