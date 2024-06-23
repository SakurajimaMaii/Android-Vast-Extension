/*
 * Copyright 2024 VastGui
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

package com.ave.vastgui.tools.exception

import com.ave.vastgui.core.extension.SingletonHolder
import com.ave.vastgui.core.extension.nothing_to_do
import com.log.vastgui.core.LogCat
import com.log.vastgui.core.base.LogLevel

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2024/5/25 1:54
// Description: App global exception handling.
// Documentation: https://ave.entropy2020.cn/documents/VastTools/core-topics/exception/app-crash-handler/

/** @since 1.4.1 */
typealias dealAppCrash = (Thread, Throwable) -> Unit

/**
 * App global exception handling, you can configure log printing and
 * subsequent actions to be taken by setting [configuration].
 *
 * Here is a usage example:
 * ```kotlin
 * // App.kt
 * private val crashConfig = AppCrashHandler
 *      .Configuration(mLogFactory.getLog(App::class.java))
 *
 * class App : Application() {
 *
 *     override fun onCreate() {
 *         super.onCreate()
 *         ...
 *         Thread.setDefaultUncaughtExceptionHandler(AppCrashHandler.getInstance(crashConfig))
 *     }
 *
 * }
 * ```
 *
 * @since 1.4.1
 */
class AppCrashHandler private constructor(private val configuration: Configuration) :
    Thread.UncaughtExceptionHandler {

    /**
     * [AppCrashHandler] configuration.
     *
     * @since 1.4.1
     */
    class Configuration @JvmOverloads constructor(
        val logger: LogCat? = null,
        val action: dealAppCrash = { _, _ -> nothing_to_do() }
    )

    override fun uncaughtException(thread: Thread, exception: Throwable) {
        if (null != configuration.logger) {
            val logger = configuration.logger
            val content = "Please refer to exception."
            logger.log(LogLevel.ERROR, logger.mDefaultTag, content, exception)
        }
        configuration.action(thread, exception)
    }

    companion object : SingletonHolder<AppCrashHandler, Configuration>(::AppCrashHandler)
}