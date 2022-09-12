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

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import androidx.annotation.RequiresApi
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
    @Synchronized
    fun getAppName() = AppUtils.getAppName(ContextHelper.getAppContext())

    /**
     * Returns app name,like VastUtilsSampleDemo.
     *
     * @param context Context
     * @return app name,like VastUtilsSampleDemo.
     * @since 0.0.1
     */
    @JvmStatic
    @Synchronized
    @Deprecated(
        "The function is deprecated!It will remove in 0.1.0",
        ReplaceWith("getAppName()", "new.getAppName"),
        DeprecationLevel.WARNING
    )
    fun getAppName(context: Context): String? {
        try {
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
     * @return the version name of the current application,like 1.0.
     * @since 0.0.9
     */
    @JvmStatic
    @Synchronized
    fun getVersionName() = getVersionName(ContextHelper.getAppContext())

    /**
     * Returns the version name of the current application,like 1.0.
     *
     * @param context Context
     * @return the version name of the current application,like 1.0.
     * @since 0.0.1
     */
    @JvmStatic
    @Synchronized
    @Deprecated(
        "The function is deprecated!It will remove in 0.1.0",
        ReplaceWith("getVersionName()", "new.getVersionName"),
        DeprecationLevel.WARNING
    )
    fun getVersionName(context: Context): String? {
        try {
            val packageManager = context.packageManager
            val packageInfo = packageManager.getPackageInfo(
                context.packageName, 0
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
     * @return the version code of the current application,like 1.
     * @since 0.0.9
     */
    @JvmStatic
    @Synchronized
    fun getVersionCode() = getVersionCode(ContextHelper.getAppContext())

    /**
     * Returns the version code of the current application,like 1.
     *
     * @param context Context
     * @return the version code of the current application,like 1.
     * @since 0.0.1
     */
    @JvmStatic
    @Synchronized
    @Deprecated(
        "The function is deprecated!It will remove in 0.1.0",
        ReplaceWith("getVersionCode()", "new.getVersionCode"),
        DeprecationLevel.WARNING
    )
    fun getVersionCode(context: Context) =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getVersionCodeApi28Above(context)
        } else {
            getVersionCodeApi28Down(context)
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
     * @return the package name of the application,like
     *     com.gcode.vastutils.
     * @since 0.0.9
     */
    @JvmStatic
    @Synchronized
    fun getPackageName() = getPackageName(ContextHelper.getAppContext())

    /**
     * Returns the package name of the application,like
     * com.gcode.vastutils.
     *
     * @param context Context
     * @return the package name of the application,like
     *     com.gcode.vastutils.
     * @since 0.0.1
     */
    @JvmStatic
    @Synchronized
    @Deprecated(
        "The function is deprecated!It will remove in 0.1.0",
        ReplaceWith("getPackageName()", "new.getPackageName"),
        DeprecationLevel.WARNING
    )
    fun getPackageName(context: Context): String? {
        try {
            val packageManager = context.packageManager
            val packageInfo = packageManager.getPackageInfo(
                context.packageName, 0
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
     * @return the icon of the application.
     * @since 0.0.9
     */
    @JvmStatic
    @Synchronized
    fun getAppBitmap() = getAppBitmap(ContextHelper.getAppContext())

    /**
     * Returns the icon of the application.
     *
     * @param context Context
     * @return the icon of the application.
     * @since 0.0.1
     */
    @JvmStatic
    @Synchronized
    @Deprecated(
        "The function is deprecated!It will remove in 0.1.0",
        ReplaceWith("getAppBitmap()", "new.getAppBitmap"),
        DeprecationLevel.WARNING
    )
    fun getAppBitmap(context: Context): Bitmap? {
        var packageManager: PackageManager? = null
        var applicationInfo: ApplicationInfo?
        try {
            packageManager = context.applicationContext
                .packageManager
            applicationInfo = packageManager.getApplicationInfo(
                context.packageName, 0
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
     * @return true if the app is debuggable.false otherwise.
     * @since 0.0.9
     */
    @JvmStatic
    @Synchronized
    fun getAppDebug() = getAppDebug(ContextHelper.getAppContext())


    /**
     * Returns true if the app is debuggable.false otherwise.
     *
     * @param context Context
     * @return true if the app is debuggable.false otherwise.
     * @since 0.0.1
     */
    @JvmStatic
    @Synchronized
    @Deprecated(
        "The function is deprecated!It will remove in 0.1.0",
        ReplaceWith("getAppDebug()", "new.getAppDebug"),
        DeprecationLevel.WARNING
    )
    fun getAppDebug(context: Context): Boolean {
        return try {
            val info = context.applicationInfo
            info.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0
        } catch (e: java.lang.Exception) {
            false
        }
    }
}