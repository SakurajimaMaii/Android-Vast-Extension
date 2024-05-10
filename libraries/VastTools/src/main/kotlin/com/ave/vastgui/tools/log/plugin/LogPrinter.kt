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

import android.content.ComponentCallbacks2
import android.util.Log
import com.ave.vastgui.core.extension.NotNUllVar
import com.ave.vastgui.core.extension.NotNullOrDefault
import com.ave.vastgui.tools.content.ContextHelper
import com.ave.vastgui.tools.log.LogUtil
import com.ave.vastgui.tools.log.base.LogDivider
import com.ave.vastgui.tools.log.base.LogInfo
import com.ave.vastgui.tools.log.base.LogInfo.Companion.JSON_TYPE
import com.ave.vastgui.tools.log.base.LogInfo.Companion.TEXT_TYPE
import com.ave.vastgui.tools.log.base.LogLevel
import com.ave.vastgui.tools.log.base.LogPlugin
import com.ave.vastgui.tools.log.base.cutStr
import com.ave.vastgui.tools.log.base.needCut
import com.ave.vastgui.tools.log.plugin.LogPrinter.Configuration
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/8/28
// Documentation: https://ave.entropy2020.cn/documents/VastTools/log/description/

/**
 * LogPrinter.
 *
 * @property mConfiguration The configuration of [LogPrinter].
 * @property mMaxSingleLogLength Refer to [Configuration.maxSingleLogLength].
 * @property maxPrintTimes Refer to [Configuration.maxSingleLogLength]
 * @since 0.5.3
 */
class LogPrinter private constructor(private val mConfiguration: Configuration) {

    /**
     * [LogPrinter] configuration.
     *
     * @property maxSingleLogLength The max length of single line of log. every
     *     char in line is calculated as 4 bytes.
     * @property maxPrintTimes The total repeat number of prints. For example,
     *     the log content is divided into ten lines depending on
     *     maxSingleLogLength. If you set maxPrintTimes to 5, only the first
     *     five lines will be printed.
     * @since 0.5.3
     */
    class Configuration internal constructor() {
        var maxSingleLogLength by NotNullOrDefault(defaultMaxSingleLogLength)

        var maxPrintTimes by NotNullOrDefault(defaultMaxPrintTimes)
    }

    private val mMaxSingleLogLength
        get() = mConfiguration.maxSingleLogLength
    private val maxPrintTimes
        get() = mConfiguration.maxPrintTimes

    private var mLogInfo by NotNUllVar<LogInfo>()

    /**
     * @since 1.3.1
     */
    private val mHandler = CoroutineExceptionHandler { _, exception ->
        val logInfo = LogInfo(
            Thread.currentThread(),
            LogLevel.ERROR,
            LogUtil.TAG,
            exception.stackTraceToString(),
            TEXT_TYPE,
            null
        )
        printLog(logInfo)
    }

    /**
     * @since 1.3.1
     */
    private val mLogScope: CoroutineScope =
        CoroutineScope(SupervisorJob() + Dispatchers.IO + CoroutineName("LogScope") + mHandler)

    /**
     * A channel of [LogInfo].
     *
     * @since 1.3.1
     */
    private val mLogChannel: Channel<LogInfo> = Channel()

    /**
     * Print log.
     *
     * @since 1.3.1
     */
    internal fun printLog(logInfo: LogInfo) {
        mLogScope.launch { mLogChannel.send(logInfo) }
    }

    /**
     * Print [logInfo].
     *
     * @since 0.5.3
     */
    private fun printTextLog(logInfo: LogInfo) {
        mLogInfo = logInfo
        // The length of the log content is less than mMaxSingleLogLength
        if (!mLogInfo.needCut(mMaxSingleLogLength)) {
            printLog { content ->
                printLog(LogDivider.getInfo(content))
            }
        }
        // The length of the log content is greater than mMaxSingleLogLength
        else {
            // Segment printing count
            var count = 0
            var printTheRest = true
            printLog(mMaxSingleLogLength * 4) { content ->
                // FIX: DEAL LINESEPARATOR THAT EXIST WITHIN THE LOG CONTENT
                val patterns = content.split(System.lineSeparator())
                patterns.forEachIndexed { index, pattern ->
                    var bytes = pattern.toByteArray()
                    if (mMaxSingleLogLength * 4 < bytes.size) {
                        do {
                            val subStr = bytes.cutStr(mMaxSingleLogLength)
                            printLog(LogDivider.getInfo(String.format("%s", subStr)))
                            bytes = bytes.copyOfRange(subStr.toByteArray().size, bytes.size)
                            count++
                            if (count == maxPrintTimes) {
                                printTheRest = false
                                break
                            }
                        } while (mMaxSingleLogLength * 4 < bytes.size)
                    } else {
                        count++
                    }

                    if (printTheRest && count <= maxPrintTimes) {
                        printLog(LogDivider.getInfo(String.format("%s", String(bytes))))
                    }
                }
            }
        }
    }

    /**
     * Print json.
     *
     * @since 0.5.3
     */
    @Throws(RuntimeException::class)
    private fun printJsonLog(logInfo: LogInfo) {
        mLogInfo = logInfo
        printLog {
            for (line in it.split("\n")) {
                printLog(LogDivider.getInfo(line))
            }
        }
    }

    /**
     * Print log.
     *
     * @since 0.5.3
     */
    private fun printLog(len: Int = mLogInfo.mPrintLength, customScope: (String) -> Unit) {
        val mContent = mLogInfo.mContent
        printLog(LogDivider.getTop(len))
        printLog(LogDivider.getInfo("Thread: ${mLogInfo.mThreadName}"))
        printLog(LogDivider.getDivider(len))
        printLog(LogDivider.getInfo("${mLogInfo.mMethodStackTrace}"))
        printLog(LogDivider.getDivider(len))
        customScope(mContent)
        mLogInfo.throwable?.apply {
            printLog(LogDivider.getDivider(len))
            printLog(LogDivider.getInfo("$this"))
            for (item in this.stackTrace) {
                printLog(LogDivider.getInfo("  at $item"))
            }
        }
        printLog(LogDivider.getBottom(len))
    }

    /** @since 0.5.3 */
    private fun printLog(content: String) {
        Log.println(mLogInfo.mLevelPriority, mLogInfo.mTag, content)
    }

    init {
        ContextHelper.getApp().registerComponentCallbacks(object : ComponentCallbacks2 {
            override fun onConfigurationChanged(newConfig: android.content.res.Configuration) {

            }

            override fun onLowMemory() {
                mLogScope.cancel("Cancel mLogScope onLowMemory.")
            }

            override fun onTrimMemory(level: Int) {

            }
        })
        mLogScope.launch {
            while (isActive) {
                val info = mLogChannel.receive()
                if (TEXT_TYPE == info.mType) {
                    printTextLog(info)
                } else if (JSON_TYPE == info.mType) {
                    printJsonLog(info)
                }
            }
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

        /**
         * Default maximum length of chars printed of a single log.
         *
         * Notes:Considering fault tolerance, 1000 is set here instead of 1024.
         */
        private const val defaultMaxSingleLogLength = 1000

        /** Default max print repeat times. */
        private const val defaultMaxPrintTimes = Int.MAX_VALUE
    }
}