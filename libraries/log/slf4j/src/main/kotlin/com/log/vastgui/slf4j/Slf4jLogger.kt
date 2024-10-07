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
import org.slf4j.Marker
import org.slf4j.event.Level
import org.slf4j.helpers.LegacyAbstractLogger
import org.slf4j.helpers.MessageFormatter

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2024/10/7
// Documentation:
// Reference:

@LogExperimental
internal class Slf4jLogger (private val logcat: LogCat) : LegacyAbstractLogger() {
    override fun isTraceEnabled() = true

    override fun isDebugEnabled() = true

    override fun isInfoEnabled() = true

    override fun isWarnEnabled() = true

    override fun isErrorEnabled() = true

    override fun getFullyQualifiedCallerName() = logcat.tag

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

    private fun convertMarker(marker: Marker?): String {
        if (marker == null) return logcat.tag
        return marker.name
    }
}
