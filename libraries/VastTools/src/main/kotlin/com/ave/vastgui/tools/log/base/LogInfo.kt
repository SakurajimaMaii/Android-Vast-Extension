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

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/8/28
// Description: 
// Documentation:
// Reference:

/**
 * Log info
 *
 * @since 0.5.2
 */
data class LogInfo internal constructor(
    val threadName: String,
    val traceElement: StackTraceElement,
    val content: String,
    val tr: Throwable? = null
) {
    private val traceLength
        get() = traceElement.toString().length

    private val contentLength
        get() = content.length

    val dividerLength
        get() = traceLength.coerceAtLeast(contentLength)

    val contentBytes
        get() = content.toByteArray()
}