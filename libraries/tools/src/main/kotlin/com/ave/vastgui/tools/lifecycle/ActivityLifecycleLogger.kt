/*
 * Copyright 2021-2024 VastGui
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ave.vastgui.tools.lifecycle

import android.app.Activity
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Bundle
import com.ave.vastgui.core.extension.NotNUllVar
import com.log.vastgui.core.LogCat
import com.log.vastgui.core.base.LogLevel

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2024/7/17
// Documentation: https://ave.entropy2020.cn/documents/tools/app-entry-points/activities/lifecycle/

/** @since 1.5.0 */
class ActivityLifecycleLogger(logCat: LogCat? = null, logLevel: LogLevel = LogLevel.DEBUG) :
    ActivityLifecycleCallbacks {

    var mLogcat: LogCat? = null
        set(value) {
            if (null != field) return
            field = value
        }

    var mLogLevel: LogLevel by NotNUllVar()
    private val mTag: String
        get() = mLogcat?.tag ?: ActivityLifecycleLogger::class.java.simpleName

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        mLogcat?.log(mLogLevel, mTag, "onActivityCreated:${activity::class.java.simpleName}")
    }

    override fun onActivityStarted(activity: Activity) {
        mLogcat?.log(mLogLevel, mTag, "onActivityStarted:${activity::class.java.simpleName}")
    }

    override fun onActivityResumed(activity: Activity) {
        mLogcat?.log(mLogLevel, mTag, "onActivityResumed:${activity::class.java.simpleName}")
    }

    override fun onActivityPaused(activity: Activity) {
        mLogcat?.log(mLogLevel, mTag, "onActivityPaused:${activity::class.java.simpleName}")
    }

    override fun onActivityStopped(activity: Activity) {
        mLogcat?.log(mLogLevel, mTag, "onActivityStopped:${activity::class.java.simpleName}")
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        mLogcat?.log(
            mLogLevel,
            mTag,
            "onActivitySaveInstanceState:${activity::class.java.simpleName}"
        )
    }

    override fun onActivityDestroyed(activity: Activity) {
        mLogcat?.log(mLogLevel, mTag, "onActivityDestroyed:${activity::class.java.simpleName}")
    }

    init {
        mLogcat = logCat
        mLogLevel = logLevel
    }
}