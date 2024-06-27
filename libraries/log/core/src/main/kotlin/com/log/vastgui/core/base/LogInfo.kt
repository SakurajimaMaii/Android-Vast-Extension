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

package com.log.vastgui.core.base

import com.log.vastgui.core.annotation.LogApi

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/8/28
// Documentation: https://ave.entropy2020.cn/documents/log/log-core/description/

/**
 * The content representing [mContent] is text.
 *
 * @since 1.3.1
 */
const val TEXT_TYPE = 0x01

/**
 * The content representing [mContent] is json.
 *
 * @since 1.3.1
 */
const val JSON_TYPE = 0x02

/**
 * Log information.
 *
 * @property mThreadName The name of the current thread.
 * @property mStackTrace an array of stack trace elements representing the
 *     stack dump of LogInfo.
 * @property mLevel The level of the log.
 * @property mTag Used to identify the source of a log message. It usually
 *     identifies the class or activity where the log call occurs.
 * @property mContent The message you would like logged.
 * @property mType Please refer to [TEXT_TYPE] or [JSON_TYPE].
 * @property mTime The current time in milliseconds, only initialized when
 *     the object is created.
 * @since 0.5.2
 */
class LogInfo @LogApi constructor(
    val mThreadName: String,
    val mStackTrace: StackTraceElement?,
    val mLevel: LogLevel,
    val mTag: String,
    val mTime: Long,
    val mContent: String,
    val mType: Int,
    val mThrowable: Throwable? = null
) {
    val mTraceLength = mStackTrace.toString().length

    val mPrintLength = mTraceLength.coerceAtLeast(mContent.length)

    val mPrintBytesLength = (if (mTraceLength >= mContent.length) mStackTrace.toString()
        .toByteArray().size else mContent.toByteArray().size).coerceAtLeast(50)

    val mLevelPriority: Int = mLevel.priority

    /** @since 1.3.4 */
    val fileName = mStackTrace?.fileName ?: ""

    /** @since 1.3.4 */
    val methodName = mStackTrace?.methodName ?: ""

    /** @since 1.3.4 */
    val lineNumber = mStackTrace?.lineNumber ?: 0

    /** @since 0.5.3 */
    override fun toString(): String {
        return """
            (Level:$mLevel 
            Tag:$mTag
            Time:$mTime
            ThreadName:$mThreadName
            StackTrace:$mStackTrace
            Content:$mContent
            Exception:${mThrowable?.stackTraceToString()})
            """.trimIndent()
    }

}

/**
 * [getStackOffset] .
 *
 * @since 1.3.4
 */
internal inline fun <reified T> getStackOffset(trace: Array<StackTraceElement>): Int {
    return trace.indexOfLast { it.className == T::class.java.name }.coerceAtLeast(0)
}