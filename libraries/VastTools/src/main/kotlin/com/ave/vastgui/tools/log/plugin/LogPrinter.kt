/*
 * Copyright 2024 VastGui guihy2019@gmail.com
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

package com.ave.vastgui.tools.log.plugin

import com.ave.vastgui.tools.log.LogUtil
import com.ave.vastgui.tools.log.base.LogInfo
import com.ave.vastgui.tools.log.base.LogLevel
import com.ave.vastgui.tools.log.base.LogPlugin
import com.ave.vastgui.tools.log.base.Logger
import com.ave.vastgui.tools.log.base.android

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/8/28
// Documentation: https://ave.entropy2020.cn/documents/VastTools/log/description/

/**
 * LogPrinter.
 *
 * @property mConfiguration The configuration of [LogPrinter].
 * @since 0.5.3
 */
class LogPrinter private constructor(private val mConfiguration: Configuration) {

    /**
     * [LogPrinter] configuration.
     *
     * @property level Minimum priority of the log.
     * @property logger Log printing implementation.
     * @since 1.3.1
     */
    class Configuration internal constructor() {
        var level: LogLevel = LogLevel.VERBOSE

        var logger: Logger = Logger.android()
    }

    /** @since 1.3.1 */
    private val mLevel: LogLevel
        get() = mConfiguration.level

    /** @since 1.3.1 */
    private val mLogger: Logger
        get() = mConfiguration.logger

    /**
     * Print log.
     *
     * @since 1.3.1
     */
    internal fun printLog(logInfo: LogInfo) {
        if (logInfo.mLevel >= mLevel) {
            mLogger.log(logInfo)
        }
    }

    companion object : LogPlugin<Configuration, LogPrinter> {
        override val key: String
            get() = LogPrinter::class.java.simpleName

        override fun configuration(config: Configuration.() -> Unit): LogPrinter {
            val configuration = Configuration().apply(config)
            return LogPrinter(configuration)
        }

        override fun install(plugin: LogPrinter, scope: LogUtil) {
            scope.mLogPrinter = plugin
        }
    }
}