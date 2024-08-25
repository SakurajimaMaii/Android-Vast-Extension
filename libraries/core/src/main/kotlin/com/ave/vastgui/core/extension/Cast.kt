/*
 * Copyright 2024 VastGui
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

@file:JvmName("CastKt")

package com.ave.vastgui.core.extension

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/8/28 12:06
// Documentation: https://ave.entropy2020.cn/documents/core/extension/cast/

/**
 * Class cast.
 *
 * @param obj the object will be casted.
 * @param T type casts.
 * @since 0.0.1
 */
@Suppress("UNCHECKED_CAST")
@kotlin.jvm.Throws(Exception::class)
fun <T> cast(obj: Any?): T {
    try {
        return obj as T
    } catch (ex: Exception) {
        throw ex
    }
}