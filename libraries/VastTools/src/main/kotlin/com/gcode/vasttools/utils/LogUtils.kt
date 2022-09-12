/*
 * Copyright 2022 VastGui
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gcode.vasttools.utils

import android.content.Context
import android.util.Log
import androidx.annotation.IntRange
import com.gcode.vasttools.utils.AppUtils.getAppDebug
import java.util.*

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/3/10 15:27
// Description: A log utils.
// Documentation: [LogUtils](https://sakurajimamaii.github.io/VastDocs/document/en/IDCardUtils.html)

/**
 * LogUtils
 *
 * Printing log:
 * ```kotlin
 * LogUtils.i(this.javaClass.simpleName,"Hello,This is a info")
 * ```
 * For more settings, please refer to the documentation.
 *
 * @since 0.0.1
 */
object LogUtils {

    /**
     * Log content.
     *
     * @since 0.0.1
     */
    private var logContent: LogContent? = null

    /**
     * Default maximum length of chars printed of a single log.
     *
     * Notes:Considering fault tolerance, 1000 is set here instead of
     * 1024.
     *
     * @since 0.0.5
     */
    private const val defaultCharLength = 1000

    /**
     * Default max print repeat times.
     *
     * @since 0.0.3
     */
    private const val defaultMaxPrintTimes = 5

    /**
     * Maximum length of chars printed of a single log.
     *
     * @since 0.0.5
     */
    var singleLogCharLength = defaultCharLength
        private set

    /**
     * Max print repeat times.
     *
     * @since 0.0.3
     */
    var maxPrintTimes: Int = defaultMaxPrintTimes
        private set

    /**
     * `true` if you want to print log,`false` if you don't want to
     * print the log.
     *
     * @since 0.0.1
     */
    var logEnabled = true

    /**
     * `true` if app in debug,`false` otherwise.
     *
     * @since 0.0.6
     */
    private var isDeBug: Boolean = true

    /**
     * Sync is debug.
     *
     * @since 0.0.6
     */
    @Synchronized
    internal fun init() {
        isDeBug = getAppDebug()
    }

    /**
     * Set [singleLogCharLength].
     *
     * @since 0.0.5
     */
    fun setSingleLogCharLength(@IntRange(from = 0, to = 1000) charLength: Int) {
        singleLogCharLength = charLength
    }

    /**
     * Set [maxPrintTimes].
     *
     * @since 0.0.5
     */
    fun setMaxPrintTimes(@IntRange(from = 0) maxPrint: Int) {
        maxPrintTimes = maxPrint
    }

    /**
     * Send info message.
     *
     * @param key Message keyboard.
     * @param content Message content.
     *
     * @since 0.0.1
     */
    fun i(key: String?, content: String?) {
        if (logEnabled && isDeBug) {
            logPrint(Log.INFO, key, content)
        }
    }

    /**
     * Send verbose message.
     *
     * @param key Message keyboard.
     * @param content Message content.
     *
     * @since 0.0.1
     */
    fun v(key: String?, content: String?) {
        if (logEnabled && isDeBug) {
            logPrint(Log.VERBOSE, key, content)
        }
    }

    /**
     * Send warning message.
     *
     * @param key Message keyboard.
     * @param content Message content.
     *
     * @since 0.0.1
     */
    fun w(key: String?, content: String?) {
        if (logEnabled && isDeBug) {
            logPrint(Log.WARN, key, content)
        }
    }

    /**
     * Send debug message.
     *
     * @param key Message keyboard.
     * @param content Message content.
     *
     * @since 0.0.1
     */
    fun d(key: String?, content: String?) {
        if (logEnabled && isDeBug) {
            logPrint(Log.DEBUG, key, content)
        }
    }

    /**
     * Send error message.
     *
     * @param key Message keyboard.
     * @param content Message content.
     *
     * @since 0.0.1
     */
    fun e(key: String?, content: String?) {
        if (logEnabled && isDeBug) {
            logPrint(Log.ERROR, key, content)
        }
    }

    /**
     * Define your log format by setting [LogUtils.logContent].
     *
     * @param logContent [LogUtils].
     *
     * @since 0.0.1
     */
    fun setLogContentFormat(logContent: LogContent) {
        LogUtils.logContent = logContent
    }

    /**
     * Log print.
     *
     * @param priority log level.
     * @param key log keyboard.
     * @param content log message.
     *
     * @since 0.0.1
     */
    private fun logPrint(
        priority: Int,
        key: String?,
        content: String?,
    ) {
        // Get the function call stack structure of the current thread.
        val stackTrace = Thread.currentThread().stackTrace

        val methodStackTraceIndex = stackTrace.indexOfLast {
            it.className == this.javaClass.name
        } + 1
        val methodStackTraceElement = stackTrace[methodStackTraceIndex]

        val methodName = methodStackTraceElement.methodName
        val tag =
            "class (${methodStackTraceElement.fileName}:${methodStackTraceElement.lineNumber}) "
        val parameter: String =
            logContent?.logContentFormat(methodName, key, content) ?: logContentFormat(
                methodName,
                key,
                content
            )

        print(priority, tag, parameter)
    }

    /**
     * Print the log(In order to solve the length limit)
     *
     * @param priority The priority/type of this log message
     * @param tag Used to identify the source of a log message.It
     *     usually identifies the class or
     *     activity where the log call occurs.
     * @param content The message you would like logged.
     *
     * @since 0.0.3
     */
    private fun print(priority: Int, tag: String, content: String) {
        /**
         * 1. The console can print 4062 bytes at most, and there are
         *    slight discrepancies in different situations
         *    (note: here are bytes, not characters!!!)
         * 2. The default character set encoding of strings is utf-8,
         *    which is a variable-length encoding. One
         *    character is represented by 1 to 4 bytes.
         */

        /**
         * Here the character length is less than [singleLogCharLength],
         * then it will be printed directly, avoiding the subsequent
         * process.
         */
        if (content.length < singleLogCharLength) {
            Log.println(priority, tag, content)
            return
        }

        // Convert the content to ByteArray.
        var bytes = content.toByteArray()

        if (singleLogCharLength * 4 >= bytes.size) {
            Log.println(priority, tag, content)
            return
        }

        // Segment printing count
        var count = 1

        var printTheRest = true

        // In the range of the array, print in cycles
        while (singleLogCharLength * 4 < bytes.size) {
            val subStr = cutStr(bytes)

            Log.println(priority, tag, String.format("%s", subStr))

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
            Log.println(priority, tag, String.format("%s", String(bytes)))
        }
    }


    /**
     * Truncate the byte array as a string according to
     * [singleLogCharLength].
     *
     * @param bytes byte array.
     * @return The string obtained by [singleLogCharLength].
     *
     * @since 0.0.3
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

    private fun logContentFormat(methodName: String?, key: String?, content: String?): String {
        return if (key == null || key.trim { it <= ' ' }.isEmpty()) {
            "method: $methodName()  content: $content"
        } else {
            if (content?.isEmpty() == true) {
                "method: $methodName() key: $key"
            } else {
                "method: $methodName() key: $key content: $content"
            }
        }
    }

    /**
     * [LogContent] used to define the log content format.
     *
     * @since 0.0.1
     */
    interface LogContent {
        fun logContentFormat(methodName: String?, key: String?, content: String?): String
    }
}