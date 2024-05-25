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

package com.log.vastgui.okhttp.base

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2024/5/24 0:23
// Documentation: https://ave.entropy2020.cn/documents/log/log-okhttp/usage/
// Reference: https://github.com/ktorio/ktor/blob/0567a43c04fd68151e290a466e0d46933d5b83f6/ktor-client/ktor-client-plugins/ktor-client-logging/common/src/io/ktor/client/plugins/logging/LogLevel.kt

/**
 * [ContentLevel] allows you to set the printed content of the log.
 *
 * @since 1.3.3
 */
enum class ContentLevel(
    val info: Boolean,
    val headers: Boolean,
    val body: Boolean
) {
    ALL(true, true, true),
    HEADERS(true, true, false),
    BODY(true, false, true),
    INFO(true, false, false),
    NONE(false, false, false)
}