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

package com.ave.vastgui.tools.log

import android.util.Log
import com.ave.vastgui.core.extension.NotNUllVar
import com.ave.vastgui.tools.log.base.LogDivider
import com.ave.vastgui.tools.log.base.LogInfo
import com.ave.vastgui.tools.log.base.LogPrinter
import com.ave.vastgui.tools.log.json.Converter
import com.ave.vastgui.tools.utils.AppUtils.getAppDebug
import java.util.Arrays

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/3/10 15:27
// Description: A log utils.
// Documentation: https://ave.entropy2020.cn/documents/VastTools/LogUtils/

/**
 * LogUtils
 *
 * For more settings, please refer to the documentation.
 */
class LogUtil internal constructor() {

    companion object {
        /**
         * Default maximum length of chars printed of a single log.
         *
         * Notes:Considering fault tolerance, 1000 is set here instead of 1024.
         */
        internal const val defaultCharLength = 1000

        /** Default max print repeat times. */
        internal const val defaultMaxPrintTimes = 5
    }

    /** `true` if app in debug,`false` otherwise. */
    private var isDeBug: Boolean = true

    /**
     * Used to identify the source of a log message. It usually identifies the
     * class or activity where the log call occurs.
     *
     * @since 0.5.2
     */
    internal var tag by NotNUllVar<String>()

    /** Maximum length of chars printed of a single log. */
    internal var singleLogCharLength: Int = defaultCharLength

    /** Max print repeat times. */
    internal var maxPrintTimes: Int = defaultMaxPrintTimes

    /**
     * `true` if you want to print log,`false` if you don't want to print the
     * log.
     */
    internal var logEnabled = true

    internal var converter: Converter? = null

    /** Sync is debug. */
    @Synchronized
    internal fun init() {
        isDeBug = getAppDebug()
    }

    /**
     * Send info message.
     *
     * @param content Message content.
     * @since 0.5.2
     */
    fun i(content: String, tr: Throwable? = null) {
        if (logEnabled && isDeBug) {
            logPrint(Log.INFO, tag, content, tr)
        }
    }

    /**
     * Send verbose message.
     *
     * @param content Message content.
     * @since 0.5.2
     */
    fun v(content: String, tr: Throwable? = null) {
        if (logEnabled && isDeBug) {
            logPrint(Log.VERBOSE, tag, content, tr)
        }
    }

    /**
     * Send warning message.
     *
     * @param content Message content.
     * @since 0.5.2
     */
    fun w(content: String, tr: Throwable? = null) {
        if (logEnabled && isDeBug) {
            logPrint(Log.WARN, tag, content, tr)
        }
    }

    /**
     * Send debug message.
     *
     * @param content Message content.
     * @since 0.5.2
     */
    fun d(content: String, tr: Throwable? = null) {
        if (logEnabled && isDeBug) {
            logPrint(Log.DEBUG, tag, content, tr)
        }
    }

    /**
     * Send error message.
     *
     * @param content Message content.
     * @since 0.5.2
     */
    fun e(content: String, tr: Throwable? = null) {
        if (logEnabled && isDeBug) {
            logPrint(Log.ERROR, tag, content, tr)
        }
    }

    /**
     * Print object [target] to json.
     *
     * @param priority The log priority.
     * @since 0.5.2
     */
    fun json(priority: Int, target: Any) {
        if (logEnabled && isDeBug) {
            jsonPrint(priority, target)
        }
    }

    /**
     * Log print.
     *
     * @param priority log level.
     * @param tag log keyboard.
     * @param content log message.
     * @since 0.5.2
     */
    private fun logPrint(
        priority: Int,
        tag: String,
        content: String,
        tr: Throwable? = null
    ) {
        val stackTrace = Thread.currentThread().stackTrace

        val methodStackTraceIndex = stackTrace.indexOfLast {
            it.className == this.javaClass.name
        } + 1
        val methodStackTraceElement = stackTrace[methodStackTraceIndex]

        val threadName = Thread.currentThread().name
        val logInfo = LogInfo(threadName, methodStackTraceElement, content, tr)

        print(priority, tag, logInfo)
    }

    /**
     * Print the log(In order to solve the length limit)
     *
     * @param priority The priority/type of this log message
     * @param tag Used to identify the source of a log message.It usually
     *     identifies the class or activity where the log call occurs.
     * @param logInfo The message you would like logged.
     * @since 0.5.2
     */
    private fun print(priority: Int, tag: String, logInfo: LogInfo) {
        val logPrinter = LogPrinter(priority, tag, logInfo)

        /**
         * 1. The console can print 4062 bytes at most, and there are slight
         *    discrepancies in different situations (note: here are bytes, not
         *    characters!!!)
         * 2. The default character set encoding of strings is utf-8, which is a
         *    variable-length encoding. One character is represented by 1 to 4
         *    bytes.
         */
        val length = logInfo.dividerLength

        /**
         * Here the character length is less than [singleLogCharLength], then it
         * will be printed directly, avoiding the subsequent process.
         */
        if (length < singleLogCharLength) {
            logPrinter.printLog {
                Log.println(priority, tag, LogDivider.getInfo(it))
            }
            return
        }

        // Convert the content to ByteArray.
        var bytes = logInfo.contentBytes

        if (singleLogCharLength * 4 >= bytes.size) {
            logPrinter.printLog {
                Log.println(priority, tag, LogDivider.getInfo(it))
            }
            return
        }

        // Segment printing count
        var count = 1

        var printTheRest = true

        logPrinter.printLog(singleLogCharLength * 4) {
            // In the range of the array, print in cycles
            while (singleLogCharLength * 4 < bytes.size) {
                val subStr = cutStr(bytes)

                Log.println(priority, tag, LogDivider.getInfo(String.format("%s", subStr)))

                // Truncate the unprinted bytes
                bytes = bytes.copyOfRange(subStr!!.toByteArray().size, bytes.size)

                if (count == maxPrintTimes) {
                    printTheRest = false
                    break
                }

                count++
            }

            // Print the unprinted bytes
            if (printTheRest) {
                Log.println(priority, tag, LogDivider.getInfo(String.format("%s", String(bytes))))
            }
        }
    }


    /**
     * Truncate the byte array as a string according to [singleLogCharLength].
     *
     * @param bytes byte array.
     * @return The string obtained by [singleLogCharLength].
     */
    private fun cutStr(bytes: ByteArray?): String? {
        // Return when the bytes is null.
        if (bytes == null) {
            return null
        }

        // Return when the bytes length is less than the subLength.
        if (singleLogCharLength * 4 >= bytes.size) {
            return String(bytes)
        }

        // Copy the fixed-length byte array and convert it to a string
        val subStr = String(Arrays.copyOf(bytes, singleLogCharLength * 4))

        // Avoid the end character is split, here minus 1 to keep the string intact
        return subStr.substring(0, subStr.length - 1)
    }

    /**
     * Json print
     *
     * @since 0.5.2
     */
    private fun jsonPrint(priority: Int, target: Any) {
        if (converter == null)
            throw RuntimeException("Please set converter in LogFormat.")

        val stackTrace = Thread.currentThread().stackTrace
        val methodStackTraceIndex = stackTrace.indexOfLast {
            it.className == this.javaClass.name
        } + 1
        val methodStackTraceElement = stackTrace[methodStackTraceIndex]
        val threadName = Thread.currentThread().name

        val jsonString = converter!!.toJson(target)
        val logInfo = LogInfo(threadName, methodStackTraceElement, jsonString)

        val logPrinter = LogPrinter(priority, tag, logInfo)
        logPrinter.printLog {
            for (line in it.split("\n")) {
                Log.println(priority, tag, LogDivider.getInfo(line))
            }
        }
    }

    init {
        init()
    }
}