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

package com.ave.vastgui.tools.log.json

import com.google.gson.GsonBuilder

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/8/29
// Description: 
// Documentation:
// Reference:

/**
 * Gson converter
 *
 * @since 0.5.2
 */
class GsonConverter(override val isPretty: Boolean) : Converter {

    private val gson = GsonBuilder().apply {
        if (isPretty) {
            setPrettyPrinting()
        }
    }.create()

    override fun toJson(data: Any): String = gson.toJson(data)

}