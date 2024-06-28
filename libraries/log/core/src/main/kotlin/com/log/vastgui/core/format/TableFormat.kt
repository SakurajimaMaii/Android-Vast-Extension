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

package com.log.vastgui.core.format

import com.log.vastgui.core.annotation.LogApi
import com.log.vastgui.core.base.JSON_TYPE
import com.log.vastgui.core.base.LogDivider
import com.log.vastgui.core.base.LogFormat
import com.log.vastgui.core.base.LogFormat.Companion.timeSdf
import com.log.vastgui.core.base.LogInfo
import com.log.vastgui.core.base.TEXT_TYPE
import com.log.vastgui.core.base.cutStr
import com.log.vastgui.core.base.needCut

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2024/6/20 22:41
// Documentation: https://ave.entropy2020.cn/documents/log/log-core/format/

/**
 * Default maximum length of chars printed of a single log.
 * **Notes:Considering fault tolerance, 1000 is set here instead of 1024.**
 *
 * @since 1.3.4
 */
@LogApi
const val DEFAULT_MAX_SINGLE_LOG_LENGTH = 1000

/**
 * Default max print repeat times.
 *
 * @since 1.3.4
 */
@LogApi
const val DEFAULT_MAX_PRINT_TIMES = Int.MAX_VALUE

/**
 * Table format of [LogInfo].
 *
 * @see <img
 *     src="https://github.com/SakurajimaMaii/Android-Vast-Extension/blob/develop/libraries/tools/image/log.png?raw=true">
 * @since 1.3.4
 */
class TableFormat(
    private val mMaxSingleLogLength: Int,
    private val mMaxPrintTimes: Int,
    private val mHeader: LogHeader
) : LogFormat {

    /**
     * Log header configuration.
     *
     * @property thread `true` if you want to show [LogInfo.mThreadName] in
     *     header, `false` otherwise.
     * @property tag `true` if you want to show [LogInfo.mTag] in header,
     *     `false` otherwise.
     * @property level `true` if you want to show [LogInfo.mLevel] in header,
     *     `false` otherwise.
     * @property time `true` if you want to show [LogInfo.mTime] in header,
     *     `false` otherwise.
     * @since 1.3.4
     */
    data class LogHeader(
        val thread: Boolean,
        val tag: Boolean,
        val level: Boolean,
        val time: Boolean
    ) {
        companion object {
            /** @since 1.3.4 */
            val default = LogHeader(thread = true, tag = true, level = true, time = true)
        }
    }

    /** @since 1.3.4 */
    override fun format(logInfo: LogInfo): String = when (logInfo.mType) {
        TEXT_TYPE -> textFormat(logInfo)
        JSON_TYPE -> jsonFormat(logInfo)
        else -> throw IllegalArgumentException("Currently only TEXT_TYPE or JSON_TYPE is supported.")
    }

    /**
     * Print [logInfo].
     *
     * @since 1.3.4
     */
    private fun textFormat(logInfo: LogInfo) =
        // The length of the log content is less than mMaxSingleLogLength
        if (!logInfo.needCut(mMaxSingleLogLength)) {
            logFormat(logInfo) { body, content ->
                // FIX: DEAL LINE SEPARATOR THAT EXIST WITHIN THE LOG CONTENT
                val patterns = content.split("\n", System.lineSeparator())
                patterns.forEach { pattern ->
                    body.appendLine(LogDivider.getInfo(pattern))
                }
            }
        }
        // The length of the log content is greater than mMaxSingleLogLength
        else {
            // Segment printing count
            var count = 0
            var printTheRest = true
            logFormat(logInfo, mMaxSingleLogLength * 4) { body, content ->
                // FIX: DEAL LINE SEPARATOR THAT EXIST WITHIN THE LOG CONTENT
                val patterns = content.split("\n", System.lineSeparator())
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

    /**
     * Print [logInfo].
     *
     * @since 1.3.4
     */
    private fun jsonFormat(logInfo: LogInfo) = logFormat(logInfo) { body, content ->
        for (line in content.split("\n", System.lineSeparator())) {
            body.appendLine(LogDivider.getInfo(line))
        }
    }

    /**
     * Print log.
     *
     * @since 1.3.4
     */
    private inline fun logFormat(
        logInfo: LogInfo,
        len: Int = logInfo.mPrintBytesLength,
        customScope: (StringBuilder, String) -> Unit
    ) = StringBuilder(logInfo.mContent.length * 4).apply {
        // It makes no sense to print a separator that is too long.
        val length = len.coerceAtMost(100)
        appendLine(LogDivider.getTop(length))
        val thread = if (mHeader.thread) "Thread: ${logInfo.mThreadName}" else ""
        val tag = if (mHeader.tag) "Tag: ${logInfo.mTag}" else ""
        val level = if (mHeader.level) "Level: ${logInfo.mLevel}" else ""
        val time = if (mHeader.time) "Time: ${timeSdf.format(logInfo.mTime)}" else ""
        appendLine(LogDivider.getInfo("$thread $tag $level $time"))
        appendLine(LogDivider.getDivider(length))
        appendLine(LogDivider.getInfo("${logInfo.mStackTrace}"))
        appendLine(LogDivider.getDivider(length))
        customScope(this, logInfo.mContent)
        logInfo.mThrowable?.apply {
            appendLine(LogDivider.getDivider(length))
            appendLine(LogDivider.getInfo("$this"))
            for (item in this.stackTrace) {
                appendLine(LogDivider.getInfo("  at $item"))
            }
        }
        append(LogDivider.getBottom(length))
    }.toString()
}