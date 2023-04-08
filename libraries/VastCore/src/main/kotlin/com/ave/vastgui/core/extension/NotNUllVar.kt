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

package com.ave.vastgui.core.extension

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/7/15
// Documentation: https://ave.entropy2020.cn/documents/VastCore/extension/NotNUllVar/

/**
 * Get or set a not null parameter.
 *
 * @param T the parameter type.
 * @param once if true, the parameter will only set by once, false otherwise.
 * @since 0.0.1
 */
class NotNUllVar<T> @JvmOverloads constructor(private val once:Boolean = false) : ReadWriteProperty<Any?, T> {

    private var value: T? = null

    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return value ?: throw UninitializedPropertyAccessException("${property.name} is not initialized.")
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        when{
            (null == value) ->
                throw  IllegalStateException("${property.name} can't be set to null.")
            (null == this.value || (null != this.value && !once)) -> this.value = value
            (once) -> throw IllegalStateException("${property.name} has already been initialized.")
        }
    }

}