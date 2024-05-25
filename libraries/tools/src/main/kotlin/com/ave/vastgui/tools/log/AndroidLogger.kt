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

package com.ave.vastgui.tools.log

import android.util.Log
import com.ave.vastgui.tools.log.base.LogHeader
import com.ave.vastgui.tools.log.base.LogScope
import com.ave.vastgui.tools.log.base.timeSdf
import com.log.vastgui.core.LogUtil
import com.log.vastgui.core.base.JSON_TYPE
import com.log.vastgui.core.base.LogDivider
import com.log.vastgui.core.base.LogInfo
import com.log.vastgui.core.base.LogLevel
import com.log.vastgui.core.base.Logger
import com.log.vastgui.core.base.TEXT_TYPE
import com.log.vastgui.core.base.cutStr
import com.log.vastgui.core.base.needCut
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.properties.Delegates

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2024/5/13 20:48
// Documentation: https://ave.entropy2020.cn/documents/VastTools/log/logger/

/**
 * Default maximum length of chars printed of a single log.
 * **Notes:Considering fault tolerance, 1000 is set here instead of 1024.**
 *
 * @since 1.3.1
 */
private const val DEFAULT_MAX_SINGLE_LOG_LENGTH = 1000

/**
 * Default max print repeat times.
 *
 * @since 1.3.1
 */
private const val DEFAULT_MAX_PRINT_TIMES = Int.MAX_VALUE

/**
 * Android Logger.
 *
 * ```kotlin
 * val mLogFactory: LogFactory = getLogFactory {
 *     ...
 *     install(LogPrinter) {
 *         logger = Logger.android(30,5)
 *     }
 * }
 * ```
 *
 * @param maxSingleLogLength The max length of single line of log. every
 *     char in line is calculated as 4 bytes.
 * @param maxPrintTimes The total repeat number of prints. For example, the
 *     log content is divided into ten lines depending on
 *     maxSingleLogLength. If you set maxPrintTimes to 5, only the first
 *     five lines will be printed.
 * @since 1.3.1
 */
fun Logger.Companion.android(
    maxSingleLogLength: Int = DEFAULT_MAX_SINGLE_LOG_LENGTH,
    maxPrintTimes: Int = DEFAULT_MAX_PRINT_TIMES,
    header: LogHeader = LogHeader(thread = true, tag = true, level = true, time = true)
): AndroidLogger = AndroidLogger(maxSingleLogLength, maxPrintTimes, header)

/**
 * Android logger.
 *
 * @see <img
 *     src="https://github.com/SakurajimaMaii/Android-Vast-Extension/blob/develop/libraries/tools/image/log.png?raw=true">
 * @since 1.3.1
 */
class AndroidLogger internal constructor(
    private val mMaxSingleLogLength: Int,
    private val mMaxPrintTimes: Int,
    private val mHeader: LogHeader
) : LogScope(), Logger {
    private var mLogInfo by Delegates.notNull<LogInfo>()

    override fun log(info: LogInfo) {
        mLogScope.launch { mLogChannel.send(info) }
    }

    /**
     * Print [logInfo].
     *
     * @since 1.3.1
     */
    private fun printTextLog(logInfo: LogInfo) {
        mLogInfo = logInfo
        // The length of the log content is less than mMaxSingleLogLength
        if (!mLogInfo.needCut(mMaxSingleLogLength)) {
            printLog { body, content ->
                println(content)
                // FIX: DEAL LINE SEPARATOR THAT EXIST WITHIN THE LOG CONTENT
                val patterns = content.split(System.lineSeparator())
                patterns.forEach { pattern ->
                    println(pattern)
                    body.appendLine(LogDivider.getInfo(pattern))
                }
            }
        }
        // The length of the log content is greater than mMaxSingleLogLength
        else {
            // Segment printing count
            var count = 0
            var printTheRest = true
            printLog(mMaxSingleLogLength * 4) { body, content ->
                // FIX: DEAL LINE SEPARATOR THAT EXIST WITHIN THE LOG CONTENT
                val patterns = content.split(System.lineSeparator())
                patterns.forEach { pattern ->
                    var bytes = pattern.toByteArray()
                    if (mMaxSingleLogLength * 4 < bytes.size) {
                        do {
                            val subStr = bytes.cutStr(mMaxSingleLogLength)
                            body.appendLine(LogDivider.getInfo(String.format("%s", subStr)))
                            bytes = bytes.copyOfRange(subStr.toByteArray().size, bytes.size)
                            count++
                            if (count == mMaxPrintTimes) {
                                printTheRest = false
                                break
                            }
                        } while (mMaxSingleLogLength * 4 < bytes.size)
                    } else {
                        count++
                    }

                    if (printTheRest && count <= mMaxPrintTimes) {
                        body.appendLine(LogDivider.getInfo(String.format("%s", String(bytes))))
                    }
                }
            }
        }
    }

    /**
     * Print [logInfo].
     *
     * @since 1.3.1
     */
    @Throws(RuntimeException::class)
    private fun printJsonLog(logInfo: LogInfo) {
        mLogInfo = logInfo
        printLog { body, content ->
            for (line in content.split(System.lineSeparator())) {
                body.appendLine(LogDivider.getInfo(line))
            }
        }
    }

    /**
     * Print log.
     *
     * @since 1.3.3
     */
    private inline fun printLog(
        len: Int = mLogInfo.mPrintBytesLength,
        customScope: (StringBuilder, String) -> Unit
    ) {
        val content = mLogInfo.mContent
        val logContent = StringBuilder(content.length * 4)
        logContent.appendLine(LogDivider.getTop(len))
        val thread = if (mHeader.thread) {
            "Thread: ${mLogInfo.mThreadName}"
        } else {
            ""
        }
        val tag = if (mHeader.tag) {
            "Tag: ${mLogInfo.mTag}"
        } else {
            ""
        }
        val level = if (mHeader.level) {
            "Level: ${mLogInfo.mLevel}"
        } else {
            ""
        }
        val time = if (mHeader.time) {
            "Time: ${timeSdf.format(mLogInfo.mTime)}"
        } else {
            ""
        }
        logContent.appendLine(LogDivider.getInfo("$thread $tag $level $time"))
        logContent.appendLine(LogDivider.getDivider(len))
        logContent.appendLine(LogDivider.getInfo("${mLogInfo.mStackTrace}"))
        logContent.appendLine(LogDivider.getDivider(len))
        customScope(logContent, content)
        mLogInfo.mThrowable?.apply {
            logContent.appendLine(LogDivider.getDivider(len))
            logContent.appendLine(LogDivider.getInfo("$this"))
            for (item in this.stackTrace) {
                logContent.appendLine(LogDivider.getInfo("  at $item"))
            }
        }
        logContent.append(LogDivider.getBottom(len))
        printLog(logContent.toString())
    }

    /** @since 1.3.1 */
    private fun printLog(content: String) {
        Log.println(mLogInfo.mLevelPriority, mLogInfo.mTag, content)
    }

    override fun handleCoroutineExceptionHandler(context: CoroutineContext, exception: Throwable) {
        val thread = Thread.currentThread()
        val logInfo = LogInfo(
            thread.name,
            null,
            LogLevel.ERROR,
            LogUtil.TAG,
            System.currentTimeMillis(),
            exception.stackTraceToString(),
            TEXT_TYPE,
            null
        )
        log(logInfo)
    }

    init {
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
}