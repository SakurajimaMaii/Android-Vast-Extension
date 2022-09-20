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

package com.gcode.vasttools.utils

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import androidx.annotation.RequiresApi
import com.gcode.vasttools.bean.Component3
import com.gcode.vasttools.helper.ContextHelper

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/3/10 15:27
// Description: Help you to get app information.
// Documentation: [AppUtils](https://sakurajimamaii.github.io/VastDocs/document/en/AppUtils.html)

/** @since 0.0.1 */
object AppUtils {

    /**
     * Returns app name,like VastUtilsSampleDemo.
     *
     * @return app name,like VastUtilsSampleDemo.
     * @since 0.0.9
     */
    @JvmStatic
    fun getAppName(): String? {
        try {
            val context = ContextHelper.getAppContext()
            val packageManager: PackageManager = context.packageManager
            val packageInfo: PackageInfo = packageManager.getPackageInfo(
                context.packageName, 0
            )
            val labelRes = packageInfo.applicationInfo.labelRes
            return context.resources.getString(labelRes)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * Returns the version name of the current application,like 1.0.
     *
     * @since 0.0.9
     */
    @JvmStatic
    fun getVersionName(): String? {
        try {
            val packageManager = ContextHelper.getAppContext().packageManager
            val packageInfo = packageManager.getPackageInfo(
                ContextHelper.getAppContext().packageName, 0
            )
            return packageInfo.versionName
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * Returns the version code of the current application,like 1.
     *
     * @since 0.0.9
     */
    @JvmStatic
    fun getVersionCode() =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getVersionCodeApi28Above(ContextHelper.getAppContext())
        } else {
            getVersionCodeApi28Down(ContextHelper.getAppContext())
        }


    /**
     * Get VersionCode (in Api 28 Above)
     *
     * @return The version code of the current application.
     * @since 0.0.1
     */
    @Synchronized
    @RequiresApi(Build.VERSION_CODES.P)
    internal fun getVersionCodeApi28Above(context: Context): Int {
        try {
            val packageManager = context.packageManager
            val packageInfo = packageManager.getPackageInfo(
                context.packageName, 0
            )
            return packageInfo.longVersionCode.toInt()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return 0
    }

    /**
     * Get VersionCode (in Api 28 Down)
     *
     * @return The version code of the current application.
     * @since 0.0.1
     */
    internal fun getVersionCodeApi28Down(context: Context): Int {
        try {
            val packageManager = context.packageManager
            val packageInfo = packageManager.getPackageInfo(
                context.packageName, 0
            )
            return packageInfo.versionCode
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return 0
    }

    /**
     * Returns the package name of the application,like
     * com.gcode.vastutils.
     *
     * @since 0.0.9
     */
    @JvmStatic
    fun getPackageName(): String? {
        try {
            val packageManager = ContextHelper.getAppContext().packageManager
            val packageInfo = packageManager.getPackageInfo(
                ContextHelper.getAppContext().packageName, 0
            )
            return packageInfo.packageName
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * Returns the icon of the application.
     *
     * @since 0.0.9
     */
    @JvmStatic
    fun getAppBitmap(): Bitmap? {
        var packageManager: PackageManager? = null
        var applicationInfo: ApplicationInfo?
        try {
            packageManager = ContextHelper.getAppContext().applicationContext
                .packageManager
            applicationInfo = packageManager.getApplicationInfo(
                ContextHelper.getAppContext().packageName, 0
            )
        } catch (e: PackageManager.NameNotFoundException) {
            applicationInfo = null
        }
        val d =
            packageManager!!.getApplicationIcon(applicationInfo!!)
        val bd = d as BitmapDrawable
        return bd.bitmap
    }

    /**
     * Returns true if the app is debuggable.false otherwise.
     *
     * @since 0.0.9
     */
    @JvmStatic
    fun getAppDebug(): Boolean {
        return try {
            val info = ContextHelper.getAppContext().applicationInfo
            info.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0
        } catch (e: java.lang.Exception) {
            false
        }
    }

    /**
     * Return the maxMemory, freeMemory, totalMemory of the application in turn.
     *
     * @since 0.0.9
     */
    fun getMemoryInfo():Component3<Double,Double,Double> {
        return Component3(
            Runtime.getRuntime().maxMemory()*1.0/(1024*1024),
            Runtime.getRuntime().freeMemory()*1.0/(1024*1024),
            Runtime.getRuntime().totalMemory()*1.0/(1024*1024)
        )
    }
}