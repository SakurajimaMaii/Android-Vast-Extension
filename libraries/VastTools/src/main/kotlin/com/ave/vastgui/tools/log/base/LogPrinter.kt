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

package com.ave.vastgui.tools.log.base

import android.util.Log

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/8/28
// Documentation: https://ave.entropy2020.cn/documents/VastTools/log/Description/

/**
 * Log printer
 *
 * @since 0.5.2
 */
class LogPrinter internal constructor(
    val priority: Int,
    val tag: String,
    val logInfo: LogInfo
) {

    inline fun printLog(len: Int = logInfo.printLength, contentPrinter: (String) -> Unit) {
        println(LogDivider.getTop(len))
        println(LogDivider.getInfo("Thread: ${logInfo.threadName}"))
        println(LogDivider.getDivider(len))
        println(LogDivider.getInfo("${logInfo.traceElement}"))
        println(LogDivider.getDivider(len))
        contentPrinter(logInfo.content)
        logInfo.tr?.apply {
            println(LogDivider.getDivider(len))
            println(LogDivider.getInfo("$this"))
            for (item in this.stackTrace) {
                println(LogDivider.getInfo("  at $item"))
            }
        }
        println(LogDivider.getBottom(len))
    }

    fun println(content: String) = Log.println(priority, tag, content)
}