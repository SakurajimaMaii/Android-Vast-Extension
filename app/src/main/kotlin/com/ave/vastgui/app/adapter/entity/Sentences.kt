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

package com.ave.vastgui.app.adapter.entity

import com.ave.vastgui.tools.network.response.ResponseApi

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2024/1/2

/** 通过 Api 请求到的随机一言名句。 */
data class Sentences(
    val code: Int,
    val message: String,
    val result: Result
) : ResponseApi {
    data class Result(
        val from: String,
        val name: String
    )

    override fun isSuccessful(): Boolean = code == 200

    override fun message(): String = message

    override fun toString(): String = "${result.from}:${result.name}"
}