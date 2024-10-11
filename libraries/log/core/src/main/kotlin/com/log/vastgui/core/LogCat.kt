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
import com.log.vastgui.core.annotation.LogExperimental
import com.log.vastgui.core.base.JSON_TYPE
import com.log.vastgui.core.base.LogInfo
import com.log.vastgui.core.base.LogInfoFactory
import com.log.vastgui.core.base.LogLevel
import com.log.vastgui.core.base.TEXT_TYPE
import com.log.vastgui.core.base.Tag
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
 * @since 1.3.4
 */
class LogCat internal constructor(@LogApi val tag: String) {

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
        replaceWith = ReplaceWith("i(content, throwable)")
    )
    fun i(content: String, throwable: Throwable? = null) {
        if (logEnabled) {
            logPrint(LogLevel.INFO, tag, content, throwable)
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
        replaceWith = ReplaceWith("i(tag, content, throwable)")
    )
    fun i(tag: String, content: String, throwable: Throwable? = null) {
        if (logEnabled) {
            logPrint(LogLevel.INFO, tag, content, throwable)
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
        replaceWith = ReplaceWith("v(content, throwable)")
    )
    fun v(content: String, throwable: Throwable? = null) {
        if (logEnabled) {
            logPrint(LogLevel.VERBOSE, tag, content, throwable)
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
        replaceWith = ReplaceWith("v(tag, content, throwable)")
    )
    fun v(tag: String, content: String, throwable: Throwable? = null) {
        if (logEnabled) {
            logPrint(LogLevel.VERBOSE, tag, content, throwable)
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
        replaceWith = ReplaceWith("w(content, throwable)")
    )
    fun w(content: String, throwable: Throwable? = null) {
        if (logEnabled) {
            logPrint(LogLevel.WARN, tag, content, throwable)
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
        replaceWith = ReplaceWith("w(tag, content, throwable)")
    )
    fun w(tag: String, content: String, throwable: Throwable? = null) {
        if (logEnabled) {
            logPrint(LogLevel.WARN, tag, content, throwable)
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
        replaceWith = ReplaceWith("d(content, throwable)")
    )
    fun d(content: String, throwable: Throwable? = null) {
        if (logEnabled) {
            logPrint(LogLevel.DEBUG, tag, content, throwable)
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
        replaceWith = ReplaceWith("d(tag, content, throwable)")
    )
    fun d(tag: String, content: String, throwable: Throwable? = null) {
        if (logEnabled) {
            logPrint(LogLevel.DEBUG, tag, content, throwable)
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
        replaceWith = ReplaceWith("e(content, throwable)")
    )
    fun e(content: String, throwable: Throwable? = null) {
        if (logEnabled) {
            logPrint(LogLevel.ERROR, tag, content, throwable)
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
        replaceWith = ReplaceWith("e(tag, content, throwable)")
    )
    fun e(tag: String, content: String, throwable: Throwable? = null) {
        if (logEnabled) {
            logPrint(LogLevel.ERROR, tag, content, throwable)
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
        replaceWith = ReplaceWith("a(content, throwable)")
    )
    fun a(content: String, throwable: Throwable? = null) {
        if (logEnabled) {
            logPrint(LogLevel.ASSERT, tag, content, throwable)
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
        replaceWith = ReplaceWith("a(tag, content, throwable)")
    )
    fun a(tag: String, content: String, throwable: Throwable? = null) {
        if (logEnabled) {
            logPrint(LogLevel.ASSERT, tag, content, throwable)
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
        replaceWith = ReplaceWith("log(level, tag, content, throwable)")
    )
    @Suppress("DEPRECATION_ERROR")
    fun logPrint(level: LogLevel, tag: String, content: String, throwable: Throwable? = null) {
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
            throwable
        )
        mLogPrinter.printLog(logInfo)
        mLogStorage?.storeLog(logInfo)
    }

    private fun log(level: LogLevel, tag: String, content: Any, throwable: Throwable? = null) {
        val name = Thread.currentThread().name
        val stackTrace = Throwable().stackTrace[2]
        logPipeline.execute(this, LogInfoFactory(level, tag, content, name, stackTrace, throwable))
    }

    /**
     * Send a [LogLevel.INFO] log message.
     *
     * @since 1.3.8
     */
    @OptIn(LogExperimental::class)
    fun i(tag: Tag, content: Any?) {
        log(LogLevel.INFO, tag(), convertIfNull(content), null)
    }

    /**
     * Send a [LogLevel.INFO] log message.
     *
     * @since 1.3.8
     */
    @OptIn(LogExperimental::class)
    fun i(tag: Tag, lazyMsg: () -> Any) {
        log(LogLevel.INFO, tag(), convertIfNull(LazyMessageWrapper(lazyMsg)), null)
    }

    /**
     * Send a [LogLevel.INFO] log message.
     *
     * @since 1.3.8
     */
    @OptIn(LogExperimental::class)
    fun i(tag: Tag, content: Any?, throwable: Throwable) {
        log(LogLevel.INFO, tag(), convertIfNull(content), throwable)
    }

    /**
     * Send a [LogLevel.INFO] log message.
     *
     * @since 1.3.8
     */
    @OptIn(LogExperimental::class)
    fun i(tag: Tag, throwable: Throwable, lazyMsg: () -> Any) {
        log(LogLevel.INFO, tag(), convertIfNull(LazyMessageWrapper(lazyMsg)), throwable)
    }

    /**
     * Send a [LogLevel.INFO] log message.
     *
     * ```kotlin
     * logcat.i(NullPointerException("this object is null."))
     * ```
     *
     * @since 1.3.8
     */
    @OptIn(LogExperimental::class)
    fun i(tag: Tag, throwable: Throwable) {
        log(LogLevel.INFO, tag(), throwable.message ?: "Please refer to exception.", throwable)
    }

    /**
     * Send a [LogLevel.INFO] log message.
     *
     * @since 1.3.8
     */
    fun i(content: Any?) {
        log(LogLevel.INFO, tag, convertIfNull(content), null)
    }

    /**
     * Send a [LogLevel.INFO] log message.
     *
     * @since 1.3.8
     */
    fun i(lazyMsg: () -> Any) {
        log(LogLevel.INFO, tag, convertIfNull(LazyMessageWrapper(lazyMsg)), null)
    }

    /**
     * Send a [LogLevel.INFO] log message.
     *
     * @since 1.3.8
     */
    fun i(content: Any?, throwable: Throwable) {
        log(LogLevel.INFO, tag, convertIfNull(content), throwable)
    }

    /**
     * Send a [LogLevel.INFO] log message.
     *
     * @since 1.3.8
     */
    fun i(throwable: Throwable?, lazyMsg: () -> Any) {
        log(LogLevel.INFO, tag, convertIfNull(LazyMessageWrapper(lazyMsg)), throwable)
    }

    /**
     * Send a [LogLevel.INFO] log message.
     *
     * ```kotlin
     * logcat.i(NullPointerException("this object is null."))
     * ```
     *
     * @since 1.3.8
     */
    fun i(throwable: Throwable) {
        log(LogLevel.INFO, tag, throwable.message ?: "Please refer to exception.", throwable)
    }

    /**
     * Send a [LogLevel.VERBOSE] log message.
     *
     * @since 1.3.8
     */
    @OptIn(LogExperimental::class)
    fun v(tag: Tag, content: Any?) {
        log(LogLevel.VERBOSE, tag(), convertIfNull(content), null)
    }

    /**
     * Send a [LogLevel.VERBOSE] log message.
     *
     * @since 1.3.8
     */
    @OptIn(LogExperimental::class)
    fun v(tag: Tag, lazyMsg: () -> Any) {
        log(LogLevel.VERBOSE, tag(), convertIfNull(LazyMessageWrapper(lazyMsg)), null)
    }

    /**
     * Send a [LogLevel.VERBOSE] log message.
     *
     * @since 1.3.8
     */
    @OptIn(LogExperimental::class)
    fun v(tag: Tag, content: Any?, throwable: Throwable) {
        log(LogLevel.VERBOSE, tag(), convertIfNull(content), throwable)
    }

    /**
     * Send a [LogLevel.VERBOSE] log message.
     *
     * @since 1.3.8
     */
    @OptIn(LogExperimental::class)
    fun v(tag: Tag, throwable: Throwable, lazyMsg: () -> Any) {
        log(LogLevel.VERBOSE, tag(), convertIfNull(LazyMessageWrapper(lazyMsg)), throwable)
    }

    /**
     * Send a [LogLevel.VERBOSE] log message.
     *
     * ```kotlin
     * logcat.v(NullPointerException("this object is null."))
     * ```
     *
     * @since 1.3.8
     */
    @OptIn(LogExperimental::class)
    fun v(tag: Tag, throwable: Throwable) {
        log(LogLevel.VERBOSE, tag(), throwable.message ?: "Please refer to exception.", throwable)
    }


    /**
     * Send a [LogLevel.VERBOSE] log message.
     *
     * @since 1.3.8
     */
    fun v(content: Any?) {
        log(LogLevel.VERBOSE, tag, convertIfNull(content), null)
    }

    /**
     * Send a [LogLevel.VERBOSE] log message.
     *
     * @since 1.3.8
     */
    fun v(lazyMsg: () -> Any) {
        log(LogLevel.VERBOSE, tag, convertIfNull(LazyMessageWrapper(lazyMsg)), null)
    }

    /**
     * Send a [LogLevel.VERBOSE] log message.
     *
     * @since 1.3.8
     */
    fun v(content: Any?, throwable: Throwable) {
        log(LogLevel.VERBOSE, tag, convertIfNull(content), throwable)
    }

    /**
     * Send a [LogLevel.VERBOSE] log message.
     *
     * @since 1.3.8
     */
    fun v(throwable: Throwable, lazyMsg: () -> Any) {
        log(LogLevel.VERBOSE, tag, convertIfNull(LazyMessageWrapper(lazyMsg)), throwable)
    }

    /**
     * Send a [LogLevel.VERBOSE] log message.
     *
     * ```kotlin
     * logcat.v(NullPointerException("this object is null."))
     * ```
     *
     * @since 1.3.8
     */
    fun v(throwable: Throwable) {
        log(LogLevel.VERBOSE, tag, throwable.message ?: "Please refer to exception.", throwable)
    }

    /**
     * Send a [LogLevel.WARN] log message.
     *
     * @since 1.3.8
     */
    @OptIn(LogExperimental::class)
    fun w(tag: Tag, content: Any?) {
        log(LogLevel.WARN, tag(), convertIfNull(content), null)
    }

    /**
     * Send a [LogLevel.WARN] log message.
     *
     * @since 1.3.8
     */
    @OptIn(LogExperimental::class)
    fun w(tag: Tag, lazyMsg: () -> Any) {
        log(LogLevel.WARN, tag(), convertIfNull(LazyMessageWrapper(lazyMsg)), null)
    }

    /**
     * Send a [LogLevel.WARN] log message.
     *
     * @since 1.3.8
     */
    @OptIn(LogExperimental::class)
    fun w(tag: Tag, content: Any?, throwable: Throwable) {
        log(LogLevel.WARN, tag(), convertIfNull(content), throwable)
    }

    /**
     * Send a [LogLevel.WARN] log message.
     *
     * @since 1.3.8
     */
    @OptIn(LogExperimental::class)
    fun w(tag: Tag, throwable: Throwable, lazyMsg: () -> Any) {
        log(LogLevel.WARN, tag(), convertIfNull(LazyMessageWrapper(lazyMsg)), throwable)
    }

    /**
     * Send a [LogLevel.WARN] log message.
     *
     * ```kotlin
     * logcat.w(NullPointerException("this object is null."))
     * ```
     *
     * @since 1.3.8
     */
    @OptIn(LogExperimental::class)
    fun w(tag: Tag, throwable: Throwable) {
        log(LogLevel.WARN, tag(), throwable.message ?: "Please refer to exception.", throwable)
    }

    /**
     * Send a [LogLevel.WARN] log message.
     *
     * @since 1.3.8
     */
    fun w(content: Any?) {
        log(LogLevel.WARN, tag, convertIfNull(content), null)
    }

    /**
     * Send a [LogLevel.WARN] log message.
     *
     * @since 1.3.8
     */
    fun w(lazyMsg: () -> Any) {
        log(LogLevel.WARN, tag, convertIfNull(LazyMessageWrapper(lazyMsg)), null)
    }

    /**
     * Send a [LogLevel.WARN] log message.
     *
     * @since 1.3.8
     */
    fun w(content: Any?, throwable: Throwable) {
        log(LogLevel.WARN, tag, convertIfNull(content), throwable)
    }

    /**
     * Send a [LogLevel.WARN] log message.
     *
     * @since 1.3.8
     */
    fun w(throwable: Throwable, lazyMsg: () -> Any) {
        log(LogLevel.WARN, tag, convertIfNull(LazyMessageWrapper(lazyMsg)), throwable)
    }

    /**
     * Send a [LogLevel.WARN] log message.
     *
     * ```kotlin
     * logcat.w(NullPointerException("this object is null."))
     * ```
     *
     * @since 1.3.8
     */
    fun w(throwable: Throwable) {
        log(LogLevel.WARN, tag, throwable.message ?: "Please refer to exception.", throwable)
    }

    /**
     * Send a [LogLevel.DEBUG] log message.
     *
     * @since 1.3.8
     */
    @OptIn(LogExperimental::class)
    fun d(tag: Tag, content: Any?) {
        log(LogLevel.DEBUG, tag(), convertIfNull(content), null)
    }

    /**
     * Send a [LogLevel.DEBUG] log message.
     *
     * @since 1.3.8
     */
    @OptIn(LogExperimental::class)
    fun d(tag: Tag, lazyMsg: () -> Any) {
        log(LogLevel.DEBUG, tag(), convertIfNull(LazyMessageWrapper(lazyMsg)), null)
    }

    /**
     * Send a [LogLevel.DEBUG] log message.
     *
     * @since 1.3.8
     */
    @OptIn(LogExperimental::class)
    fun d(tag: Tag, content: Any?, throwable: Throwable) {
        log(LogLevel.DEBUG, tag(), convertIfNull(content), throwable)
    }

    /**
     * Send a [LogLevel.DEBUG] log message.
     *
     * @since 1.3.8
     */
    @OptIn(LogExperimental::class)
    fun d(tag: Tag, throwable: Throwable, lazyMsg: () -> Any) {
        log(LogLevel.DEBUG, tag(), convertIfNull(LazyMessageWrapper(lazyMsg)), throwable)
    }

    /**
     * Send a [LogLevel.DEBUG] log message.
     *
     * ```kotlin
     * logcat.d(NullPointerException("this object is null."))
     * ```
     *
     * @since 1.3.8
     */
    @OptIn(LogExperimental::class)
    fun d(tag: Tag, throwable: Throwable) {
        log(LogLevel.DEBUG, tag(), throwable.message ?: "Please refer to exception.", throwable)
    }


    /**
     * Send a [LogLevel.DEBUG] log message.
     *
     * @since 1.3.8
     */
    fun d(content: Any?) {
        log(LogLevel.DEBUG, tag, convertIfNull(content), null)
    }

    /**
     * Send a [LogLevel.DEBUG] log message.
     *
     * @since 1.3.8
     */
    fun d(lazyMsg: () -> Any) {
        log(LogLevel.DEBUG, tag, convertIfNull(LazyMessageWrapper(lazyMsg)), null)
    }

    /**
     * Send a [LogLevel.DEBUG] log message.
     *
     * @since 1.3.8
     */
    fun d(content: Any?, throwable: Throwable) {
        log(LogLevel.DEBUG, tag, convertIfNull(content), throwable)
    }

    /**
     * Send a [LogLevel.DEBUG] log message.
     *
     * @since 1.3.8
     */
    fun d(throwable: Throwable, lazyMsg: () -> Any) {
        log(LogLevel.DEBUG, tag, convertIfNull(LazyMessageWrapper(lazyMsg)), throwable)
    }

    /**
     * Send a [LogLevel.DEBUG] log message.
     *
     * ```kotlin
     * logcat.d(NullPointerException("this object is null."))
     * ```
     *
     * @since 1.3.8
     */
    fun d(throwable: Throwable) {
        log(LogLevel.DEBUG, tag, throwable.message ?: "Please refer to exception.", throwable)
    }

    /**
     * Send a [LogLevel.ERROR] log message.
     *
     * @since 1.3.8
     */
    @OptIn(LogExperimental::class)
    fun e(tag: Tag, content: Any?) {
        log(LogLevel.ERROR, tag(), convertIfNull(content), null)
    }

    /**
     * Send a [LogLevel.ERROR] log message.
     *
     * @since 1.3.8
     */
    @OptIn(LogExperimental::class)
    fun e(tag: Tag, lazyMsg: () -> Any) {
        log(LogLevel.ERROR, tag(), convertIfNull(LazyMessageWrapper(lazyMsg)), null)
    }

    /**
     * Send a [LogLevel.ERROR] log message.
     *
     * @since 1.3.8
     */
    @OptIn(LogExperimental::class)
    fun e(tag: Tag, content: Any?, throwable: Throwable) {
        log(LogLevel.ERROR, tag(), convertIfNull(content), throwable)
    }

    /**
     * Send a [LogLevel.ERROR] log message.
     *
     * @since 1.3.8
     */
    @OptIn(LogExperimental::class)
    fun e(tag: Tag, throwable: Throwable?, lazyMsg: () -> Any) {
        log(LogLevel.ERROR, tag(), convertIfNull(LazyMessageWrapper(lazyMsg)), throwable)
    }

    /**
     * Send a [LogLevel.ERROR] log message.
     *
     * ```kotlin
     * logcat.e(NullPointerException("this object is null."))
     * ```
     *
     * @since 1.3.8
     */
    @OptIn(LogExperimental::class)
    fun e(tag: Tag, tr: Throwable) {
        log(LogLevel.ERROR, tag(), tr.message ?: "Please refer to exception.", tr)
    }


    /**
     * Send a [LogLevel.ERROR] log message.
     *
     * @since 1.3.8
     */
    fun e(content: Any?) {
        log(LogLevel.ERROR, tag, convertIfNull(content), null)
    }

    /**
     * Send a [LogLevel.ERROR] log message.
     *
     * @since 1.3.8
     */
    fun e(lazyMsg: () -> Any) {
        log(LogLevel.ERROR, tag, convertIfNull(LazyMessageWrapper(lazyMsg)), null)
    }

    /**
     * Send a [LogLevel.ERROR] log message.
     *
     * @since 1.3.8
     */
    fun e(content: Any?, throwable: Throwable) {
        log(LogLevel.ERROR, tag, convertIfNull(content), throwable)
    }

    /**
     * Send a [LogLevel.ERROR] log message.
     *
     * @since 1.3.8
     */
    fun e(throwable: Throwable?, lazyMsg: () -> Any) {
        log(LogLevel.ERROR, tag, convertIfNull(LazyMessageWrapper(lazyMsg)), throwable)
    }

    /**
     * Send a [LogLevel.ERROR] log message.
     *
     * ```kotlin
     * logcat.e(NullPointerException("this object is null."))
     * ```
     *
     * @since 1.3.8
     */
    fun e(throwable: Throwable) {
        log(LogLevel.ERROR, tag, throwable.message ?: "Please refer to exception.", throwable)
    }

    /**
     * Send a [LogLevel.ASSERT] log message.
     *
     * @since 1.3.8
     */
    @OptIn(LogExperimental::class)
    fun a(tag: Tag, content: Any?) {
        log(LogLevel.ASSERT, tag(), convertIfNull(content), null)
    }

    /**
     * Send a [LogLevel.ASSERT] log message.
     *
     * @since 1.3.8
     */
    @OptIn(LogExperimental::class)
    fun a(tag: Tag, lazyMsg: () -> Any) {
        log(LogLevel.ASSERT, tag(), convertIfNull(LazyMessageWrapper(lazyMsg)), null)
    }

    /**
     * Send a [LogLevel.ASSERT] log message.
     *
     * @since 1.3.8
     */
    @OptIn(LogExperimental::class)
    fun a(tag: Tag, content: Any?, throwable: Throwable) {
        log(LogLevel.ASSERT, tag(), convertIfNull(content), throwable)
    }

    /**
     * Send a [LogLevel.ASSERT] log message.
     *
     * @since 1.3.8
     */
    @OptIn(LogExperimental::class)
    fun a(tag: Tag, throwable: Throwable, lazyMsg: () -> Any) {
        log(LogLevel.ASSERT, tag(), convertIfNull(LazyMessageWrapper(lazyMsg)), throwable)
    }

    /**
     * Send a [LogLevel.ASSERT] log message.
     *
     * ```kotlin
     * logcat.a(NullPointerException("this object is null."))
     * ```
     *
     * @since 1.3.8
     */
    @OptIn(LogExperimental::class)
    fun a(tag: Tag, throwable: Throwable) {
        log(LogLevel.ASSERT, tag(), throwable.message ?: "Please refer to exception.", throwable)
    }

    /**
     * Send a [LogLevel.ASSERT] log message.
     *
     * @since 1.3.8
     */
    fun a(content: Any?) {
        log(LogLevel.ASSERT, tag, convertIfNull(content), null)
    }

    /**
     * Send a [LogLevel.ASSERT] log message.
     *
     * @since 1.3.8
     */
    fun a(lazyMsg: () -> Any) {
        log(LogLevel.ASSERT, tag, convertIfNull(LazyMessageWrapper(lazyMsg)), null)
    }

    /**
     * Send a [LogLevel.ASSERT] log message.
     *
     * @since 1.3.8
     */
    fun a(content: Any?, throwable: Throwable) {
        log(LogLevel.ASSERT, tag, convertIfNull(content), throwable)
    }

    /**
     * Send a [LogLevel.ASSERT] log message.
     *
     * @since 1.3.8
     */
    fun a(throwable: Throwable, lazyMsg: () -> Any) {
        log(LogLevel.ASSERT, tag, convertIfNull(LazyMessageWrapper(lazyMsg)), throwable)
    }

    /** @since 1.3.5 */
    private fun convertIfNull(content: Any?): Any {
        if (null != content) return content
        return "null"
    }

}