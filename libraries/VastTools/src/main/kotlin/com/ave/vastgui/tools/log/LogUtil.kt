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

import com.ave.vastgui.core.extension.NotNUllVar
import com.ave.vastgui.tools.log.base.LogInfo
import com.ave.vastgui.tools.log.base.LogInfo.Companion.JSON_TYPE
import com.ave.vastgui.tools.log.base.LogInfo.Companion.TEXT_TYPE
import com.ave.vastgui.tools.log.base.LogLevel
import com.ave.vastgui.tools.log.json.Converter
import com.ave.vastgui.tools.log.plugin.LogPrinter
import com.ave.vastgui.tools.log.plugin.LogStorage
import com.ave.vastgui.tools.utils.AppUtils.getAppDebug

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/3/10 15:27
// Documentation: https://ave.entropy2020.cn/documents/VastTools/log/description/

/**
 * LogUtils.
 *
 * @see LogPrinter
 */
class LogUtil internal constructor() {

    /** `true` if app in debug,`false` otherwise. */
    private var isDeBug: Boolean = true

    /**
     * Used to identify the source of a log message. It usually identifies the
     * class or activity where the log call occurs.
     *
     * @since 0.5.3
     */
    internal var mDefaultTag by NotNUllVar<String>()

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
    internal lateinit var mLogPrinter: LogPrinter

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

    /** Sync is debug. */
    @Synchronized
    internal fun init() {
        isDeBug = getAppDebug()
    }

    /**
     * Send a [LogLevel.INFO] log message.
     *
     * @param content Message content.
     * @since 0.5.2
     */
    @JvmOverloads
    fun i(content: String, tr: Throwable? = null) {
        if (logEnabled && isDeBug) {
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
        if (logEnabled && isDeBug) {
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
        if (logEnabled && isDeBug) {
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
        if (logEnabled && isDeBug) {
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
        if (logEnabled && isDeBug) {
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
        if (logEnabled && isDeBug) {
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
        if (logEnabled && isDeBug) {
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
        if (logEnabled && isDeBug) {
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
        if (logEnabled && isDeBug) {
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
        if (logEnabled && isDeBug) {
            logPrint(LogLevel.ERROR, tag, content, tr)
        }
    }


    /**
     * Print object [target] to json.
     *
     * @param level The log level.
     * @since 0.5.2
     */
    fun json(level: LogLevel, target: Any) {
        if (logEnabled && isDeBug) {
            val jsonString = mLogConverter!!.toJson(target)
            val logInfo =
                LogInfo(Thread.currentThread(), level, mDefaultTag, jsonString, JSON_TYPE, null)
            mLogPrinter.printLog(logInfo)
            mLogStorage?.storageJson(logInfo)
        }
    }

    /**
     * Print object [target] to json.
     *
     * @param level The log level.
     * @since 0.5.3
     */
    fun json(tag: String, level: LogLevel, target: Any) {
        if (logEnabled && isDeBug) {
            val jsonString = mLogConverter!!.toJson(target)
            val logInfo = LogInfo(Thread.currentThread(), level, tag, jsonString, JSON_TYPE, null)
            mLogPrinter.printLog(logInfo)
            mLogStorage?.storageJson(logInfo)
        }
    }

    /**
     * Log print.
     *
     * @param level log level.
     * @param tag log keyboard.
     * @param content log message.
     * @since 0.5.3
     */
    private fun logPrint(
        level: LogLevel,
        tag: String,
        content: String,
        tr: Throwable? = null
    ) {
        val logInfo = LogInfo(Thread.currentThread(), level, tag, content, TEXT_TYPE, tr)
        mLogPrinter.printLog(logInfo)
        mLogStorage?.storage(logInfo)
    }

    /**
     * Enable storage
     *
     * @since 0.5.3
     */
    private fun LogInfo.enableStorage() = this.mLevel >= mLevel

    init {
        init()
    }

    companion object {
        /**
         * @since 1.3.1
         */
        internal const val TAG = "VastTools"
    }
}