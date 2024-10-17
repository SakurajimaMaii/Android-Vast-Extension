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
import com.log.vastgui.core.annotation.LogExperimental
import com.log.vastgui.core.base.LogLevel
import com.log.vastgui.core.base.LogTag

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

    @OptIn(LogExperimental::class)
    private val mTag: LogTag =
        LogTag(mLogcat?.tag ?: ActivityLifecycleLogger::class.java.simpleName)

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        printLog(mLogLevel, "onActivityCreated:${activity::class.java.simpleName}")
    }

    override fun onActivityStarted(activity: Activity) {
        printLog(mLogLevel, "onActivityStarted:${activity::class.java.simpleName}")
    }

    override fun onActivityResumed(activity: Activity) {
        printLog(mLogLevel, "onActivityResumed:${activity::class.java.simpleName}")
    }

    override fun onActivityPaused(activity: Activity) {
        printLog(mLogLevel, "onActivityPaused:${activity::class.java.simpleName}")
    }

    override fun onActivityStopped(activity: Activity) {
        printLog(mLogLevel, "onActivityStopped:${activity::class.java.simpleName}")
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        printLog(mLogLevel, "onActivitySaveInstanceState:${activity::class.java.simpleName}")
    }

    override fun onActivityDestroyed(activity: Activity) {
        printLog(mLogLevel, "onActivityDestroyed:${activity::class.java.simpleName}")
    }

    @OptIn(LogExperimental::class)
    private fun printLog(level: LogLevel, content: String) = when (level) {
        LogLevel.VERBOSE -> mLogcat?.v(mTag, content)
        LogLevel.DEBUG -> mLogcat?.d(mTag, content)
        LogLevel.INFO -> mLogcat?.i(mTag, content)
        LogLevel.WARN -> mLogcat?.w(mTag, content)
        LogLevel.ERROR -> mLogcat?.e(mTag, content)
        LogLevel.ASSERT -> mLogcat?.a(mTag, content)
    }

    init {
        mLogcat = logCat
        mLogLevel = logLevel
    }
}