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

package com.log.vastgui.core

import com.log.vastgui.core.annotation.LogApi
import com.log.vastgui.core.base.JSON_TYPE
import com.log.vastgui.core.base.LogInfo
import com.log.vastgui.core.base.LogLevel
import com.log.vastgui.core.base.TEXT_TYPE
import com.log.vastgui.core.json.Converter
import com.log.vastgui.core.plugin.LogPrinter
import com.log.vastgui.core.plugin.LogStorage
import kotlin.properties.Delegates

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/3/10 15:27
// Documentation: https://ave.entropy2020.cn/documents/log/log-core/description/

/**
 * LogUtils.
 *
 * @see LogPrinter
 */
class LogUtil internal constructor() {

    /**
     * Used to identify the source of a log message. It usually identifies the
     * class or activity where the log call occurs.
     *
     * @since 0.5.3
     */
    @LogApi
    var mDefaultTag: String by Delegates.notNull()

    /**
     * `true` if you want to print log,`false` if you don't want to print the
     * log.
     */
    internal var logEnabled = true

    /**
     * Log printer.
     *
     * @since 0.5.3
     */
    internal var mLogPrinter: LogPrinter by Delegates.notNull()

    /**
     * Log storage.
     *
     * @since 0.5.3
     */
    internal var mLogStorage: LogStorage? = null

    /**
     * Log json converter.
     *
     * @since 0.5.3
     */
    internal var mLogConverter: Converter? = null

    /**
     * Send a [LogLevel.INFO] log message.
     *
     * @param content Message content.
     * @since 0.5.2
     */
    @JvmOverloads
    fun i(content: String, tr: Throwable? = null) {
        if (logEnabled) {
            logPrint(LogLevel.INFO, mDefaultTag, content, tr)
        }
    }

    /**
     * Send a [LogLevel.INFO] log message.
     *
     * @param content Message content.
     * @since 0.5.3
     */
    @JvmOverloads
    fun i(tag: String, content: String, tr: Throwable? = null) {
        if (logEnabled) {
            logPrint(LogLevel.INFO, tag, content, tr)
        }
    }

    /**
     * Send a [LogLevel.VERBOSE] log message.
     *
     * @param content Message content.
     * @since 0.5.2
     */
    @JvmOverloads
    fun v(content: String, tr: Throwable? = null) {
        if (logEnabled) {
            logPrint(LogLevel.VERBOSE, mDefaultTag, content, tr)
        }
    }

    /**
     * Send a [LogLevel.VERBOSE] log message.
     *
     * @param content Message content.
     * @since 0.5.3
     */
    @JvmOverloads
    fun v(tag: String, content: String, tr: Throwable? = null) {
        if (logEnabled) {
            logPrint(LogLevel.VERBOSE, tag, content, tr)
        }
    }

    /**
     * Send a [LogLevel.WARN] log message.
     *
     * @param content Message content.
     * @since 0.5.2
     */
    @JvmOverloads
    fun w(content: String, tr: Throwable? = null) {
        if (logEnabled) {
            logPrint(LogLevel.WARN, mDefaultTag, content, tr)
        }
    }

    /**
     * Send a [LogLevel.WARN] message.
     *
     * @param content Message content.
     * @since 0.5.3
     */
    @JvmOverloads
    fun w(tag: String, content: String, tr: Throwable? = null) {
        if (logEnabled) {
            logPrint(LogLevel.WARN, tag, content, tr)
        }
    }

    /**
     * Send a [LogLevel.DEBUG] log message.
     *
     * @param content Message content.
     * @since 0.5.2
     */
    @JvmOverloads
    fun d(content: String, tr: Throwable? = null) {
        if (logEnabled) {
            logPrint(LogLevel.DEBUG, mDefaultTag, content, tr)
        }
    }

    /**
     * Send a [LogLevel.DEBUG] log message.
     *
     * @param content Message content.
     * @since 0.5.3
     */
    @JvmOverloads
    fun d(tag: String, content: String, tr: Throwable? = null) {
        if (logEnabled) {
            logPrint(LogLevel.DEBUG, tag, content, tr)
        }
    }

    /**
     * Send a [LogLevel.ERROR] log message.
     *
     * @param content Message content.
     * @since 0.5.2
     */
    @JvmOverloads
    fun e(content: String, tr: Throwable? = null) {
        if (logEnabled) {
            logPrint(LogLevel.ERROR, mDefaultTag, content, tr)
        }
    }

    /**
     * Send a [LogLevel.ERROR] log message.
     *
     * @param content Message content.
     * @since 0.5.3
     */
    @JvmOverloads
    fun e(tag: String, content: String, tr: Throwable? = null) {
        if (logEnabled) {
            logPrint(LogLevel.ERROR, tag, content, tr)
        }
    }

    /**
     * Send a [LogLevel.ASSERT] log message.
     *
     * @param content Message content.
     * @since 1.3.1
     */
    fun a(content: String, tr: Throwable? = null) {
        if (logEnabled) {
            logPrint(LogLevel.ASSERT, mDefaultTag, content, tr)
        }
    }

    /**
     * Send a [LogLevel.ASSERT] log message.
     *
     * @param content Message content.
     * @since 1.3.1
     */
    fun a(tag: String, content: String, tr: Throwable? = null) {
        if (logEnabled) {
            logPrint(LogLevel.ASSERT, tag, content, tr)
        }
    }

    /**
     * Print [jsonString] as json string.
     *
     * @param logLevel The log level.
     * @param jsonString The log content.
     * @since 1.3.1
     */
    fun json(logLevel: LogLevel, jsonString: String) {
        if (null == mLogConverter)
            throw RuntimeException("Please init mLogConverter by LogJson.")
        runCatching {
            json(logLevel, mLogConverter!!.parseString(jsonString))
        }.onFailure {
            json(logLevel, jsonString as Any)
        }
    }

    /**
     * Print [jsonString] as json string.
     *
     * @param logLevel The log level.
     * @param jsonString The log content.
     * @since 1.3.1
     */
    fun json(tag: String, logLevel: LogLevel, jsonString: String) {
        if (null == mLogConverter)
            throw RuntimeException("Please init mLogConverter by LogJson.")
        runCatching {
            json(tag, logLevel, mLogConverter!!.parseString(jsonString))
        }.onFailure {
            json(tag, logLevel, jsonString as Any)
        }
    }

    /**
     * Print object [target] to json.
     *
     * @param level The log level.
     * @throws RuntimeException
     * @since 0.5.2
     */
    fun json(level: LogLevel, target: Any) {
        if (null == mLogConverter)
            throw RuntimeException("Please init mLogConverter by LogJson.")
        if (!logEnabled) return
        val jsonString = mLogConverter!!.toJson(target)
        val thread = Thread.currentThread()
        val index =
            thread.stackTrace.indexOfLast { it.className == LogUtil::class.java.name }
        val logInfo = LogInfo(
            thread.name,
            thread.stackTrace[index + 1],
            level,
            mDefaultTag,
            System.currentTimeMillis(),
            jsonString,
            JSON_TYPE,
            null
        )
        mLogPrinter.printLog(logInfo)
        mLogStorage?.storeLog(logInfo)
    }

    /**
     * Print object [target] to json.
     *
     * @param level The log level.
     * @throws RuntimeException
     * @since 0.5.3
     */
    fun json(tag: String, level: LogLevel, target: Any) {
        if (null == mLogConverter)
            throw RuntimeException("Please init mLogConverter by LogJson.")
        if (!logEnabled) return
        val jsonString = mLogConverter!!.toJson(target)
        val thread = Thread.currentThread()
        val index =
            thread.stackTrace.indexOfLast { it.className == LogUtil::class.java.name }
        val logInfo = LogInfo(
            thread.name,
            thread.stackTrace[index + 1],
            level,
            tag,
            System.currentTimeMillis(),
            jsonString,
            JSON_TYPE,
            null
        )
        mLogPrinter.printLog(logInfo)
        mLogStorage?.storeLog(logInfo)
    }

    /**
     * Log print.
     *
     * @param level log level.
     * @param tag log keyboard.
     * @param content log message.
     * @since 0.5.3
     */
    @LogApi
    fun logPrint(
        level: LogLevel,
        tag: String,
        content: String,
        tr: Throwable? = null
    ) {
        val thread = Thread.currentThread()
        val index =
            thread.stackTrace.indexOfLast { it.className == LogUtil::class.java.name }
        val logInfo = LogInfo(
            thread.name,
            thread.stackTrace[index + 1],
            level,
            tag,
            System.currentTimeMillis(),
            content,
            TEXT_TYPE,
            tr
        )
        mLogPrinter.printLog(logInfo)
        mLogStorage?.storeLog(logInfo)
    }

    /**
     * Log print.
     *
     * @since 1.3.3
     */
    @LogApi
    fun logPrint(logInfo: LogInfo) {
        mLogPrinter.printLog(logInfo)
        mLogStorage?.storeLog(logInfo)
    }

    companion object {
        /** @since 1.3.1 */
        const val TAG = "log-core"
    }
}