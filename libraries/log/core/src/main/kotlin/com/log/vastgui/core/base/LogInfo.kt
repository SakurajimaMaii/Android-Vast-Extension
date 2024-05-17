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

package com.log.vastgui.core.base

import com.log.vastgui.core.LogUtil
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
 * @property mCurrentThread The current thread.
 * @property mLevel The level of the log.
 * @property mTag Used to identify the source of a log message. It usually
 *     identifies the class or activity where the log call occurs.
 * @property mContent The message you would like logged.
 * @property mType Please refer to [TEXT_TYPE] or [JSON_TYPE].
 * @property mTime The current time in milliseconds, only initialized when
 *     the object is created.
 * @property mThreadName The name of the current thread.
 * @property mMethodStackTrace The stack trace of the current method.
 * @since 0.5.2
 */
data class LogInfo @LogApi constructor(
    private val mCurrentThread: Thread,
    val mLevel: LogLevel,
    val mTag: String,
    val mTime: Long,
    val mContent: String,
    val mType: Int,
    val mThrowable: Throwable? = null
) {
    val mMethodStackTrace: StackTraceElement? = mCurrentThread.stackTrace.let {
        val stackTrace = mCurrentThread.stackTrace
        val methodStackTraceIndex = stackTrace.indexOfLast {
            it.className == LogUtil::class.java.name
        } + 1
        stackTrace[methodStackTraceIndex]
    }

    val mTraceLength = mMethodStackTrace.toString().length

    val mContentLength = mContent.length

    val mPrintLength = mTraceLength.coerceAtLeast(mContentLength)

    val mPrintBytesLength = if (mTraceLength >= mContentLength) mMethodStackTrace.toString()
        .toByteArray().size else mContent.toByteArray().size

    val mThreadName: String = mCurrentThread.name

    val mLevelPriority: Int = mLevel.priority

    /** @since 0.5.3 */
    override fun toString(): String {
        return """
            (Level:$mLevel 
            Tag:$mTag
            Time:$mTime
            ThreadName:$mThreadName
            MethodStackTrace:$mMethodStackTrace
            Content:$mContent
            Exception:${mThrowable?.stackTraceToString()})
            """.trimIndent()
    }

}