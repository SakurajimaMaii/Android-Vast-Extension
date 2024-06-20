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

package com.log.vastgui.desktop

import com.log.vastgui.core.base.LogInfo
import com.log.vastgui.core.base.LogLevel
import com.log.vastgui.core.base.Logger
import com.log.vastgui.desktop.base.Blue
import com.log.vastgui.desktop.base.Cyan
import com.log.vastgui.desktop.base.Gray
import com.log.vastgui.desktop.base.Green
import com.log.vastgui.desktop.base.Purple
import com.log.vastgui.desktop.base.Red
import com.log.vastgui.desktop.base.Reset
import com.log.vastgui.desktop.base.White
import com.log.vastgui.desktop.base.Yellow
import java.text.SimpleDateFormat
import java.util.Locale

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2024/5/14 23:04
// Documentation: https://ave.entropy2020.cn/documents/log/log-desktop/logger/

/** @since 1.3.1 */
private val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)

/**
 * Desktop logger.
 *
 * ```kotlin
 * val mLogFactory: LogFactory = getLogFactory {
 *     ...
 *     install(LogPrinter) {
 *         logger = Logger.desktop()
 *     }
 * }
 * ```
 *
 * @since 1.3.1
 */
fun Logger.Companion.desktop(): DesktopLogger = DesktopLogger()

/**
 * Desktop logger Implementation.You can view the following image to
 * preview the printing effect.
 *
 * @since 1.3.1
 * @see <img src="https://github.com/SakurajimaMaii/Android-Vast-Extension/blob/develop/libraries/log/desktop/image/log.png?raw=true"/>
 */
class DesktopLogger internal constructor() : Logger {

    override fun log(info: LogInfo) {
        val time = sdf.format(info.mTime)
        val logStrInfo =
            "$Cyan$time$Reset ${info.headColor()}[${info.mLevel}|${info.mTag}|${info.mThreadName}]$Reset $Blue(${info.mStackTrace?.fileName}:${info.mStackTrace?.lineNumber})$Reset ${info.mContent}"
        println(logStrInfo)
    }

    /**
     * Get the head color by [LogInfo.mLevel].
     *
     * @since 1.3.1
     */
    private fun LogInfo.headColor() = when (mLevel) {
        LogLevel.VERBOSE -> Gray
        LogLevel.DEBUG -> Blue
        LogLevel.INFO -> Green
        LogLevel.WARN -> Yellow
        LogLevel.ERROR -> Red
        LogLevel.ASSERT -> Purple
        else -> White
    }

}