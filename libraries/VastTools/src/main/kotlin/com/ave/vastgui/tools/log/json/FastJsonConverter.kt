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

import com.alibaba.fastjson2.JSON
import com.alibaba.fastjson2.JSONWriter

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/8/29
// Description: 
// Documentation:
// Reference:

/**
 * Fast json converter
 *
 * @since 0.5.2
 */
class FastJsonConverter(override val isPretty: Boolean) : Converter {

    override fun toJson(data: Any): String = if(isPretty) {
        JSON.toJSONString(data, JSONWriter.Context(JSONWriter.Feature.PrettyFormat))
    } else JSON.toJSONString(data)

}