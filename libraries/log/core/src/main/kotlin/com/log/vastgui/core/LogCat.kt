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

package com.log.vastgui.core

import com.log.vastgui.core.annotation.LogApi
import com.log.vastgui.core.base.JSON_TYPE
import com.log.vastgui.core.base.LogInfo
import com.log.vastgui.core.base.LogInfoFactory
import com.log.vastgui.core.base.LogLevel
import com.log.vastgui.core.base.TEXT_TYPE
import com.log.vastgui.core.base.getStackOffset
import com.log.vastgui.core.internel.LazyMessageWrapper
import com.log.vastgui.core.json.Converter
import com.log.vastgui.core.plugin.LogPrinter
import com.log.vastgui.core.plugin.LogStorage
import com.log.vastgui.core.plugin.LogSwitch
import kotlin.properties.Delegates

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/3/10 15:27
// Documentation: https://ave.entropy2020.cn/documents/log/log-core/setting-up/

/**
 * [LogCat].
 *
 * ```kotlin
 * val logFactory: LogFactory = getLogFactory {
 *     install(LogSwitch) {
 *         open = true
 *     }
 *     install(LogPrinter)
 * }
 *
 * val logcat = logFactory("global")
 * ```
 *
 * @param tag The default tag of [LogCat].
 * @param caller The caller of [LogCat].
 * @since 1.3.4
 */
class LogCat internal constructor(@LogApi val tag: String, @LogApi val caller: String) {

    /**
     * Log pipeline.
     *
     * @since 1.3.4
     */
    val logPipeline: LogPipeline = LogPipeline()

    /**
     * `true` if you want to print log,`false` if you don't want to print the
     * log.
     *
     * @see LogSwitch
     */
    internal var logEnabled = true

    /**
     * Log printer.
     *
     * @since 0.5.3
     */
    @Deprecated(message = "Use pipeline instead.", level = DeprecationLevel.ERROR)
    internal var mLogPrinter: LogPrinter by Delegates.notNull()

    /**
     * Log storage.
     *
     * @since 0.5.3
     */
    @Deprecated(message = "Use pipeline instead.", level = DeprecationLevel.ERROR)
    internal var mLogStorage: LogStorage? = null

    /**
     * Log json converter.
     *
     * @since 0.5.3
     */
    @Deprecated(message = "Use pipeline instead.", level = DeprecationLevel.ERROR)
    internal var mLogConverter: Converter? = null

    /**
     * Send a [LogLevel.INFO] log message.
     *
     * @param content Message content.
     * @since 0.5.2
     */
    @JvmOverloads
    @Deprecated(
        message = "Use pipeline instead.",
        level = DeprecationLevel.HIDDEN,
        replaceWith = ReplaceWith("i(content, tr)")
    )
    fun i(content: String, tr: Throwable? = null) {
        if (logEnabled) {
            logPrint(LogLevel.INFO, tag, content, tr)
        }
    }

    /**
     * Send a [LogLevel.INFO] log message.
     *
     * @param content Message content.
     * @since 0.5.3
     */
    @JvmOverloads
    @Deprecated(
        message = "Use pipeline instead.",
        level = DeprecationLevel.HIDDEN,
        replaceWith = ReplaceWith("i(tag, content, tr)")
    )
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
    @Deprecated(
        message = "Use pipeline instead.",
        level = DeprecationLevel.HIDDEN,
        replaceWith = ReplaceWith("v(content, tr)")
    )
    fun v(content: String, tr: Throwable? = null) {
        if (logEnabled) {
            logPrint(LogLevel.VERBOSE, tag, content, tr)
        }
    }

    /**
     * Send a [LogLevel.VERBOSE] log message.
     *
     * @param content Message content.
     * @since 0.5.3
     */
    @JvmOverloads
    @Deprecated(
        message = "Use pipeline instead.",
        level = DeprecationLevel.HIDDEN,
        replaceWith = ReplaceWith("v(tag, content, tr)")
    )
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
    @Deprecated(
        message = "Use pipeline instead.",
        level = DeprecationLevel.HIDDEN,
        replaceWith = ReplaceWith("w(content, tr)")
    )
    fun w(content: String, tr: Throwable? = null) {
        if (logEnabled) {
            logPrint(LogLevel.WARN, tag, content, tr)
        }
    }

    /**
     * Send a [LogLevel.WARN] message.
     *
     * @param content Message content.
     * @since 0.5.3
     */
    @JvmOverloads
    @Deprecated(
        message = "Use pipeline instead.",
        level = DeprecationLevel.HIDDEN,
        replaceWith = ReplaceWith("w(tag, content, tr)")
    )
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
    @Deprecated(
        message = "Use pipeline instead.",
        level = DeprecationLevel.HIDDEN,
        replaceWith = ReplaceWith("d(content, tr)")
    )
    fun d(content: String, tr: Throwable? = null) {
        if (logEnabled) {
            logPrint(LogLevel.DEBUG, tag, content, tr)
        }
    }

    /**
     * Send a [LogLevel.DEBUG] log message.
     *
     * @param content Message content.
     * @since 0.5.3
     */
    @JvmOverloads
    @Deprecated(
        message = "Use pipeline instead.",
        level = DeprecationLevel.HIDDEN,
        replaceWith = ReplaceWith("d(tag, content, tr)")
    )
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
    @Deprecated(
        message = "Use pipeline instead.",
        level = DeprecationLevel.HIDDEN,
        replaceWith = ReplaceWith("e(content, tr)")
    )
    fun e(content: String, tr: Throwable? = null) {
        if (logEnabled) {
            logPrint(LogLevel.ERROR, tag, content, tr)
        }
    }

    /**
     * Send a [LogLevel.ERROR] log message.
     *
     * @param content Message content.
     * @since 0.5.3
     */
    @JvmOverloads
    @Deprecated(
        message = "Use pipeline instead.",
        level = DeprecationLevel.HIDDEN,
        replaceWith = ReplaceWith("e(tag, content, tr)")
    )
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
    @Deprecated(
        message = "Use pipeline instead.",
        level = DeprecationLevel.HIDDEN,
        replaceWith = ReplaceWith("a(content, tr)")
    )
    fun a(content: String, tr: Throwable? = null) {
        if (logEnabled) {
            logPrint(LogLevel.ASSERT, tag, content, tr)
        }
    }

    /**
     * Send a [LogLevel.ASSERT] log message.
     *
     * @param content Message content.
     * @since 1.3.1
     */
    @Deprecated(
        message = "Use pipeline instead.",
        level = DeprecationLevel.HIDDEN,
        replaceWith = ReplaceWith("a(tag, content, tr)")
    )
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
    @Deprecated(message = "Use pipeline instead.", level = DeprecationLevel.ERROR)
    @Suppress("DEPRECATION_ERROR")
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
    @Deprecated(message = "Use pipeline instead.", level = DeprecationLevel.ERROR)
    @Suppress("DEPRECATION_ERROR")
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
    @Deprecated(
        message = "Use pipeline instead.",
        level = DeprecationLevel.ERROR,
        replaceWith = ReplaceWith("i(target)")
    )
    @Suppress("DEPRECATION_ERROR")
    fun json(level: LogLevel, target: Any) {
        if (null == mLogConverter)
            throw RuntimeException("Please init mLogConverter by LogJson.")
        if (!logEnabled) return
        val jsonString = mLogConverter!!.toJson(target)
        val thread = Thread.currentThread()
        val index = getStackOffset<LogCat>(thread.stackTrace)
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
     * Print object [target] to json.
     *
     * @param level The log level.
     * @throws RuntimeException
     * @since 0.5.3
     */
    @Deprecated(
        message = "Use pipeline instead.",
        level = DeprecationLevel.ERROR,
        replaceWith = ReplaceWith("i(tag, target)")
    )
    @Suppress("DEPRECATION_ERROR")
    fun json(tag: String, level: LogLevel, target: Any) {
        if (null == mLogConverter)
            throw RuntimeException("Please init mLogConverter by LogJson.")
        if (!logEnabled) return
        val jsonString = mLogConverter!!.toJson(target)
        val thread = Thread.currentThread()
        val index = getStackOffset<LogCat>(thread.stackTrace)
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
    @Deprecated(
        message = "Use pipeline instead.",
        level = DeprecationLevel.WARNING,
        replaceWith = ReplaceWith("log(level, tag, content, tr)")
    )
    @Suppress("DEPRECATION_ERROR")
    fun logPrint(level: LogLevel, tag: String, content: String, tr: Throwable? = null) {
        val thread = Thread.currentThread()
        val index = getStackOffset<LogCat>(thread.stackTrace)
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

    private fun log(level: LogLevel, tag: String, content: Any, tr: Throwable? = null) {
        Throwable().stackTrace.forEach { println(it) }
        println("===============================================")
        logPipeline.execute(this, LogInfoFactory(level, tag, content, caller, tr))
    }

    /**
     * Send a [LogLevel.INFO] log message.
     *
     * @since 1.3.4
     */
    fun i(tag: String, content: Any?) {
        log(LogLevel.INFO, tag, convertIfNull(content), null)
    }

    /**
     * Send a [LogLevel.INFO] log message.
     *
     * @since 1.3.4
     */
    fun i(tag: String, lazyMsg: () -> Any) {
        log(LogLevel.INFO, tag, convertIfNull(LazyMessageWrapper(lazyMsg)), null)
    }

    /**
     * Send a [LogLevel.INFO] log message.
     *
     * @since 1.3.4
     */
    fun i(tag: String, content: Any?, tr: Throwable) {
        log(LogLevel.INFO, tag, convertIfNull(content), tr)
    }

    /**
     * Send a [LogLevel.INFO] log message.
     *
     * @since 1.3.4
     */
    fun i(tag: String, tr: Throwable, lazyMsg: () -> Any) {
        log(LogLevel.INFO, tag, convertIfNull(LazyMessageWrapper(lazyMsg)), tr)
    }

    /**
     * Send a [LogLevel.INFO] log message.
     *
     * @since 1.3.4
     */
    fun i(content: Any?) {
        log(LogLevel.INFO, tag, convertIfNull(content), null)
    }

    /**
     * Send a [LogLevel.INFO] log message.
     *
     * @since 1.3.4
     */
    fun i(lazyMsg: () -> Any) {
        log(LogLevel.INFO, tag, convertIfNull(LazyMessageWrapper(lazyMsg)), null)
    }

    /**
     * Send a [LogLevel.INFO] log message.
     *
     * @since 1.3.4
     */
    fun i(content: Any?, tr: Throwable) {
        log(LogLevel.INFO, tag, convertIfNull(content), tr)
    }

    /**
     * Send a [LogLevel.INFO] log message.
     *
     * @since 1.3.4
     */
    fun i(tr: Throwable?, lazyMsg: () -> Any) {
        log(LogLevel.INFO, tag, convertIfNull(LazyMessageWrapper(lazyMsg)), tr)
    }

    /**
     * Send a [LogLevel.VERBOSE] log message.
     *
     * @since 1.3.4
     */
    fun v(tag: String, content: Any?) {
        log(LogLevel.VERBOSE, tag, convertIfNull(content), null)
    }

    /**
     * Send a [LogLevel.VERBOSE] log message.
     *
     * @since 1.3.4
     */
    fun v(tag: String, lazyMsg: () -> Any) {
        log(LogLevel.VERBOSE, tag, convertIfNull(LazyMessageWrapper(lazyMsg)), null)
    }

    /**
     * Send a [LogLevel.VERBOSE] log message.
     *
     * @since 1.3.4
     */
    fun v(tag: String, content: Any?, tr: Throwable) {
        log(LogLevel.VERBOSE, tag, convertIfNull(content), tr)
    }

    /**
     * Send a [LogLevel.VERBOSE] log message.
     *
     * @since 1.3.4
     */
    fun v(tag: String, tr: Throwable, lazyMsg: () -> Any) {
        log(LogLevel.VERBOSE, tag, convertIfNull(LazyMessageWrapper(lazyMsg)), tr)
    }

    /**
     * Send a [LogLevel.VERBOSE] log message.
     *
     * @since 1.3.4
     */
    fun v(content: Any?) {
        log(LogLevel.VERBOSE, tag, convertIfNull(content), null)
    }

    /**
     * Send a [LogLevel.VERBOSE] log message.
     *
     * @since 1.3.4
     */
    fun v(lazyMsg: () -> Any) {
        log(LogLevel.VERBOSE, tag, convertIfNull(LazyMessageWrapper(lazyMsg)), null)
    }

    /**
     * Send a [LogLevel.VERBOSE] log message.
     *
     * @since 1.3.4
     */
    fun v(content: Any?, tr: Throwable) {
        log(LogLevel.VERBOSE, tag, convertIfNull(content), tr)
    }

    /**
     * Send a [LogLevel.VERBOSE] log message.
     *
     * @since 1.3.4
     */
    fun v(tr: Throwable, lazyMsg: () -> Any) {
        log(LogLevel.VERBOSE, tag, convertIfNull(LazyMessageWrapper(lazyMsg)), tr)
    }

    /**
     * Send a [LogLevel.WARN] log message.
     *
     * @since 1.3.4
     */
    fun w(tag: String, content: Any?) {
        log(LogLevel.WARN, tag, convertIfNull(content), null)
    }

    /**
     * Send a [LogLevel.WARN] log message.
     *
     * @since 1.3.4
     */
    fun w(tag: String, lazyMsg: () -> Any) {
        log(LogLevel.WARN, tag, convertIfNull(LazyMessageWrapper(lazyMsg)), null)
    }

    /**
     * Send a [LogLevel.WARN] log message.
     *
     * @since 1.3.4
     */
    fun w(tag: String, content: Any?, tr: Throwable) {
        log(LogLevel.WARN, tag, convertIfNull(content), tr)
    }

    /**
     * Send a [LogLevel.WARN] log message.
     *
     * @since 1.3.4
     */
    fun w(tag: String, tr: Throwable, lazyMsg: () -> Any) {
        log(LogLevel.WARN, tag, convertIfNull(LazyMessageWrapper(lazyMsg)), tr)
    }

    /**
     * Send a [LogLevel.WARN] log message.
     *
     * @since 1.3.4
     */
    fun w(content: Any?) {
        log(LogLevel.WARN, tag, convertIfNull(content), null)
    }

    /**
     * Send a [LogLevel.WARN] log message.
     *
     * @since 1.3.4
     */
    fun w(lazyMsg: () -> Any) {
        log(LogLevel.WARN, tag, convertIfNull(LazyMessageWrapper(lazyMsg)), null)
    }

    /**
     * Send a [LogLevel.WARN] log message.
     *
     * @since 1.3.4
     */
    fun w(content: Any?, tr: Throwable) {
        log(LogLevel.WARN, tag, convertIfNull(content), tr)
    }

    /**
     * Send a [LogLevel.WARN] log message.
     *
     * @since 1.3.4
     */
    fun w(tr: Throwable, lazyMsg: () -> Any) {
        log(LogLevel.WARN, tag, convertIfNull(LazyMessageWrapper(lazyMsg)), tr)
    }

    /**
     * Send a [LogLevel.DEBUG] log message.
     *
     * @since 1.3.4
     */
    fun d(tag: String, content: Any?) {
        log(LogLevel.DEBUG, tag, convertIfNull(content), null)
    }

    /**
     * Send a [LogLevel.DEBUG] log message.
     *
     * @since 1.3.4
     */
    fun d(tag: String, lazyMsg: () -> Any) {
        log(LogLevel.DEBUG, tag, convertIfNull(LazyMessageWrapper(lazyMsg)), null)
    }

    /**
     * Send a [LogLevel.DEBUG] log message.
     *
     * @since 1.3.4
     */
    fun d(tag: String, content: Any?, tr: Throwable) {
        log(LogLevel.DEBUG, tag, convertIfNull(content), tr)
    }

    /**
     * Send a [LogLevel.DEBUG] log message.
     *
     * @since 1.3.4
     */
    fun d(tag: String, tr: Throwable, lazyMsg: () -> Any) {
        log(LogLevel.DEBUG, tag, convertIfNull(LazyMessageWrapper(lazyMsg)), tr)
    }

    /**
     * Send a [LogLevel.DEBUG] log message.
     *
     * @since 1.3.4
     */
    fun d(content: Any?) {
        log(LogLevel.DEBUG, tag, convertIfNull(content), null)
    }

    /**
     * Send a [LogLevel.DEBUG] log message.
     *
     * @since 1.3.4
     */
    fun d(lazyMsg: () -> Any) {
        log(LogLevel.DEBUG, tag, convertIfNull(LazyMessageWrapper(lazyMsg)), null)
    }

    /**
     * Send a [LogLevel.DEBUG] log message.
     *
     * @since 1.3.4
     */
    fun d(content: Any?, tr: Throwable) {
        log(LogLevel.DEBUG, tag, convertIfNull(content), tr)
    }

    /**
     * Send a [LogLevel.DEBUG] log message.
     *
     * @since 1.3.4
     */
    fun d(tr: Throwable, lazyMsg: () -> Any) {
        log(LogLevel.DEBUG, tag, convertIfNull(LazyMessageWrapper(lazyMsg)), tr)
    }

    /**
     * Send a [LogLevel.ERROR] log message.
     *
     * @since 1.3.4
     */
    fun e(tag: Tag, content: Any?) {
        log(LogLevel.ERROR, tag(), convertIfNull(content), null)
    }

    /**
     * Send a [LogLevel.ERROR] log message.
     *
     * @since 1.3.4
     */
    fun e(tag: String, lazyMsg: () -> Any) {
        log(LogLevel.ERROR, tag, convertIfNull(LazyMessageWrapper(lazyMsg)), null)
    }

    /**
     * Send a [LogLevel.ERROR] log message.
     *
     * @since 1.3.4
     */
    fun e(tag: String, content: Any?, tr: Throwable?) {
        log(LogLevel.ERROR, tag, convertIfNull(content), tr)
    }

    /**
     * Send a [LogLevel.ERROR] log message.
     *
     * @since 1.3.4
     */
    fun e(tag: String, tr: Throwable?, lazyMsg: () -> Any) {
        log(LogLevel.ERROR, tag, convertIfNull(LazyMessageWrapper(lazyMsg)), tr)
    }

    /**
     * Send a [LogLevel.ERROR] log message.
     *
     * @since 1.3.4
     */
    fun e(content: Any?) {
        log(LogLevel.ERROR, tag, convertIfNull(content), null)
    }

    /**
     * Send a [LogLevel.ERROR] log message.
     *
     * @since 1.3.4
     */
    fun e(lazyMsg: () -> Any) {
        log(LogLevel.ERROR, tag, convertIfNull(LazyMessageWrapper(lazyMsg)), null)
    }

    /**
     * Send a [LogLevel.ERROR] log message.
     *
     * @since 1.3.4
     */
    fun e(content: Any?, tr: Throwable) {
        log(LogLevel.ERROR, tag, convertIfNull(content), tr)
    }

    /**
     * Send a [LogLevel.ERROR] log message.
     *
     * @since 1.3.4
     */
    fun e(tr: Throwable?, lazyMsg: () -> Any) {
        log(LogLevel.ERROR, tag, convertIfNull(LazyMessageWrapper(lazyMsg)), tr)
    }

    /**
     * Send a [LogLevel.ERROR] log message.
     *
     * ```kotlin
     * mLogger.e(NullPointerException("this object is null."))
     * ```
     *
     * @since 1.3.4
     */
    fun e(tr: Throwable) {
        log(LogLevel.ERROR, tag, tr.message ?: "Please refer to exception.", tr)
    }

    /**
     * Send a [LogLevel.ERROR] log message.
     *
     * ```kotlin
     * mLogger.e(NullPointerException("this object is null."))
     * ```
     *
     * @since 1.3.4
     */
    fun e(tr: Throwable, tag: String) {
        log(LogLevel.ERROR, tag, tr.message ?: "Please refer to exception.", tr)
    }

    /**
     * Send a [LogLevel.ASSERT] log message.
     *
     * @since 1.3.4
     */
    fun a(tag: String, content: Any?) {
        log(LogLevel.ASSERT, tag, convertIfNull(content), null)
    }

    /**
     * Send a [LogLevel.ASSERT] log message.
     *
     * @since 1.3.4
     */
    fun a(tag: String, lazyMsg: () -> Any) {
        log(LogLevel.ASSERT, tag, convertIfNull(LazyMessageWrapper(lazyMsg)), null)
    }

    /**
     * Send a [LogLevel.ASSERT] log message.
     *
     * @since 1.3.4
     */
    fun a(tag: String, content: Any?, tr: Throwable) {
        log(LogLevel.ASSERT, tag, convertIfNull(content), tr)
    }

    /**
     * Send a [LogLevel.ASSERT] log message.
     *
     * @since 1.3.4
     */
    fun a(tag: String, tr: Throwable, lazyMsg: () -> Any) {
        log(LogLevel.ASSERT, tag, convertIfNull(LazyMessageWrapper(lazyMsg)), tr)
    }

    /**
     * Send a [LogLevel.ASSERT] log message.
     *
     * @since 1.3.4
     */
    fun a(content: Any?) {
        log(LogLevel.ASSERT, tag, convertIfNull(content), null)
    }

    /**
     * Send a [LogLevel.ASSERT] log message.
     *
     * @since 1.3.4
     */
    fun a(lazyMsg: () -> Any) {
        log(LogLevel.ASSERT, tag, convertIfNull(LazyMessageWrapper(lazyMsg)), null)
    }

    /**
     * Send a [LogLevel.ASSERT] log message.
     *
     * @since 1.3.4
     */
    fun a(content: Any?, tr: Throwable) {
        log(LogLevel.ASSERT, tag, convertIfNull(content), tr)
    }

    /**
     * Send a [LogLevel.ASSERT] log message.
     *
     * @since 1.3.4
     */
    fun a(tr: Throwable, lazyMsg: () -> Any) {
        log(LogLevel.ASSERT, tag, convertIfNull(LazyMessageWrapper(lazyMsg)), tr)
    }

    /** @since 1.3.5 */
    private fun convertIfNull(content: Any?): Any {
        if (null != content) return content
        Result
        return "null"
    }

}

@JvmInline
value class Tag(val tag: String) {
    operator fun invoke(): String = tag

}