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
import com.ave.vastgui.core.extension.NotNUllVar
import com.ave.vastgui.tools.log.LogUtil

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/8/28
// Documentation: https://ave.entropy2020.cn/documents/VastTools/log/description/

/**
 * Log info.
 *
 * @property mCurrentThread The current thread.
 * @property mLevel The level of the log.
 * @property mTag Used to identify the source of a log message. It usually
 *     identifies the class or activity where the log call occurs.
 * @property mContent The message you would like logged.
 * @property mTimeMillis The current time in milliseconds, only initialized
 *     when the object is created.
 * @property mThreadName The name of the current thread.
 * @property mMethodStackTrace The stack trace of the current method.
 * @since 0.5.2
 */
data class LogInfo internal constructor(
    private val mCurrentThread: Thread,
    val mLevel: LogLevel,
    val mTag: String,
    val mContent: String,
    val throwable: Throwable? = null
) {
    internal val mTraceLength
        get() = mMethodStackTrace.toString().length

    internal val mContentLength
        get() = mContent.length

    internal var mTimeMillis: Long by NotNUllVar(once = true)

    internal val mPrintLength
        get() = mTraceLength.coerceAtLeast(mContentLength)

    internal val mPrintBytesLength
        get() = if (mTraceLength >= mContentLength) mMethodStackTrace.toString()
            .toByteArray().size else mContent.toByteArray().size

    internal val mThreadName: String
        get() = mCurrentThread.name

    internal val mMethodStackTrace: StackTraceElement?
        get() {
            val stackTrace = mCurrentThread.stackTrace
            val methodStackTraceIndex = stackTrace.indexOfLast {
                it.className == LogUtil::class.java.name
            } + 1
            return stackTrace[methodStackTraceIndex]
        }

    internal val mLevelPriority: Int
        get() = mLevel.priority

    /** @since 0.5.3 */
    override fun toString(): String {
        return """
            (level:$mLevel 
            tag:$mTag 
            threadName:$mThreadName
            methodStackTrace:$mMethodStackTrace
            content:$mContent
            Exception:${Log.getStackTraceString(throwable)})
            """.trimIndent()
    }

    init {
        mTimeMillis = System.currentTimeMillis()
    }
}
