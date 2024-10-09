/*
 * Copyright 2021-2024 VastGui
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

package com.log.vastgui.slf4j

import com.log.vastgui.core.LogCat
import com.log.vastgui.core.annotation.LogExperimental
import com.log.vastgui.core.base.LogLevel
import com.log.vastgui.core.plugin.LogPrinter
import com.log.vastgui.core.plugin.LogStorage
import org.slf4j.Marker
import org.slf4j.event.Level
import org.slf4j.helpers.LegacyAbstractLogger
import org.slf4j.helpers.MessageFormatter

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2024/10/7
// Documentation: https://ave.entropy2020.cn/documents/log/log-slf4j/usage/

/**
 * [Slf4jLogger].
 *
 * @since 1.3.7
 */
@LogExperimental
internal class Slf4jLogger(private val logcat: LogCat) : LegacyAbstractLogger() {

    /**
     * For [LogCat], its log level control is controlled by [LogPrinter] and
     * [LogStorage], so it is returned by default here `true`.
     *
     * @see LogPrinter.levelMap
     * @see LogStorage.levelMap
     * @since 1.3.7
     */
    override fun isTraceEnabled() = true

    /**
     * For [LogCat], its log level control is controlled by [LogPrinter] and
     * [LogStorage], so it is returned by default here `true`.
     *
     * @see LogPrinter.levelMap
     * @see LogStorage.levelMap
     * @since 1.3.7
     */
    override fun isDebugEnabled() = true

    /**
     * For [LogCat], its log level control is controlled by [LogPrinter] and
     * [LogStorage], so it is returned by default here `true`.
     *
     * @see LogPrinter.levelMap
     * @see LogStorage.levelMap
     * @since 1.3.7
     */
    override fun isInfoEnabled() = true

    /**
     * For [LogCat], its log level control is controlled by [LogPrinter] and
     * [LogStorage], so it is returned by default here `true`.
     *
     * @see LogPrinter.levelMap
     * @see LogStorage.levelMap
     * @since 1.3.7
     */
    override fun isWarnEnabled() = true

    /**
     * For [LogCat], its log level control is controlled by [LogPrinter] and
     * [LogStorage], so it is returned by default here `true`.
     *
     * @see LogPrinter.levelMap
     * @see LogStorage.levelMap
     * @since 1.3.7
     */
    override fun isErrorEnabled() = true

    /** @since 1.3.7 */
    override fun getFullyQualifiedCallerName() = logcat.tag

    /**
     * Log information is processed through [LogCat.log].
     *
     * @since 1.3.7
     */
    override fun handleNormalizedLoggingCall(
        level: Level?,
        marker: Marker?,
        messagePattern: String?,
        arguments: Array<out Any>?,
        throwable: Throwable?
    ) {
        logcat.log(
            convertLevel(level),
            convertMarker(marker),
            MessageFormatter.basicArrayFormat(messagePattern, arguments),
            throwable
        )
    }

    /**
     * Convert [Level] to [LogLevel].
     *
     * @since 1.3.7
     */
    private fun convertLevel(level: Level?): LogLevel {
        return when (level) {
            Level.ERROR -> LogLevel.ERROR
            Level.WARN -> LogLevel.WARN
            Level.INFO -> LogLevel.INFO
            Level.DEBUG -> LogLevel.DEBUG
            Level.TRACE -> LogLevel.VERBOSE
            null -> LogLevel.INFO
        }
    }

    /**
     * Convert [Marker].
     *
     * @since 1.3.7
     */
    private fun convertMarker(marker: Marker?): String {
        if (marker == null) return logcat.tag
        return marker.name
    }
}