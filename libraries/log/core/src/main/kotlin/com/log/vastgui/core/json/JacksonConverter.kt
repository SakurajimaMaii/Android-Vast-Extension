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

package com.log.vastgui.core.json

import com.ave.vastgui.core.extension.SingletonHolder
import com.fasterxml.jackson.databind.ObjectMapper

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/8/29
// Documentation: https://ave.entropy2020.cn/documents/log/log-core/description/

/**
 * Jackson converter
 *
 * @since 0.5.2
 */
class JacksonConverter private constructor(override val isPretty: Boolean) : Converter {

    private val mapper = ObjectMapper()

    override fun toJson(data: Any): String = if (isPretty) {
        mapper.writerWithDefaultPrettyPrinter().writeValueAsString(data)
    } else {
        mapper.writeValueAsString(data)
    }

    override fun parseString(jsonString: String): String = runCatching {
        if (isPretty) {
            mapper.readTree(jsonString).toPrettyString()
        } else {
            mapper.readTree(jsonString).toString()
        }
    }.getOrDefault(jsonString)

    companion object : SingletonHolder<JacksonConverter, Boolean>(::JacksonConverter)

}