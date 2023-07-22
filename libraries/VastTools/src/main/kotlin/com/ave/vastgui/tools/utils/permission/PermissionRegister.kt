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

package com.ave.vastgui.tools.utils.permission

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.text.TextUtils
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import com.ave.vastgui.core.extension.defaultLogTag
import com.ave.vastgui.tools.config.ToolsConfig
import com.ave.vastgui.tools.utils.DateUtils

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/4/6

/**
 * Obtain the corresponding [PermissionLauncher] from
 * [PermissionRegister.singlePermissionLauncherMap] for permission request.
 *
 * @since 0.2.0
 */
fun ComponentActivity.singlePermissionLauncher(): PermissionLauncher<String, Boolean>? {
    val activityKey = intent.getStringExtra(PermissionRegister.KEY_UNIQUE_ACTIVITY)
    return if (TextUtils.isEmpty(activityKey)) null else PermissionRegister.singlePermissionLauncherMap[activityKey]
}

/**
 * Obtain the corresponding [PermissionLauncher] from
 * [PermissionRegister.multiPermissionLauncherMap] for permission request.
 *
 * @since 0.2.0
 */
fun ComponentActivity.multiPermissionLauncher(): PermissionLauncher<Array<String>, Map<String, Boolean>>? {
    val activityKey = intent.getStringExtra(PermissionRegister.KEY_UNIQUE_ACTIVITY)
    return if (TextUtils.isEmpty(activityKey)) null else PermissionRegister.multiPermissionLauncherMap[activityKey]
}

/**
 * [PermissionRegister] is for developers to call the
 * [requestPermission]/[requestMultiplePermissions] API at any time. It
 * will save an [PermissionLauncher] object for each Activity when it
 * is created, so that permission requests can be made through this object.
 * At the same time, the object will be destroyed when the corresponding
 * Activity is destroyed. PermissionRegister will be initialized in
 * [ToolsConfig].
 *
 * @see ToolsConfig
 * @since 0.2.0
 */
class PermissionRegister : Application.ActivityLifecycleCallbacks {

    companion object {

        /**
         * A key pointing to a string consisting of the current Activity name and
         * timestamp, the key-value is stored in [Activity.getIntent].
         *
         * @since 0.2.0
         */
        const val KEY_UNIQUE_ACTIVITY = "KEY_UNIQUE_ACTIVITY"

        /**
         * Saving the [ActivityResultContracts.RequestPermission] of the activities
         * or fragments.
         *
         * @since 0.2.0
         */
        val singlePermissionLauncherMap: MutableMap<String, PermissionLauncher<String, Boolean>> =
            mutableMapOf()

        /**
         * Saving the [ActivityResultContracts.RequestMultiplePermissions] of the
         * activities or fragments.
         *
         * @since 0.2.0
         */
        val multiPermissionLauncherMap: MutableMap<String, PermissionLauncher<Array<String>, Map<String, Boolean>>> =
            mutableMapOf()
    }

    override fun onActivityCreated(activity: Activity, bundle: Bundle?) {
        if (activity is ComponentActivity) {
            val activityKey =
                "${activity.defaultLogTag()}${DateUtils.getCurrentTime(DateUtils.FORMAT_YYYY_MM_DD_HH_MM_SS)}"
            val singlePermissionLauncher =
                PermissionLauncher(activity, ActivityResultContracts.RequestPermission())
            val multiPermissionLauncher =
                PermissionLauncher(activity, ActivityResultContracts.RequestMultiplePermissions())
            activity.intent.putExtra(KEY_UNIQUE_ACTIVITY, activityKey)
            singlePermissionLauncherMap[activityKey] = singlePermissionLauncher
            multiPermissionLauncherMap[activityKey] = multiPermissionLauncher
        }
    }

    override fun onActivityStarted(activity: Activity) {

    }

    override fun onActivityResumed(activity: Activity) {

    }

    override fun onActivityPaused(activity: Activity) {

    }

    override fun onActivityStopped(activity: Activity) {

    }

    override fun onActivitySaveInstanceState(activity: Activity, bundle: Bundle) {

    }

    override fun onActivityDestroyed(activity: Activity) {
        if (activity is ComponentActivity) {
            val activityKey = activity.intent.getStringExtra(KEY_UNIQUE_ACTIVITY)
            if (!TextUtils.isEmpty(activityKey)) {
                singlePermissionLauncherMap[activityKey]?.unregister()
                singlePermissionLauncherMap.remove(activityKey)
                multiPermissionLauncherMap[activityKey]?.unregister()
                multiPermissionLauncherMap.remove(activityKey)
            }
        }
    }
}