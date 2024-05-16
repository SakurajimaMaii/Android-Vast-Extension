/*
 * Copyright 2024 VastGui guihy2019@gmail.com
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

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2024/1/6 1:24

data class AirNow(
    val fxLink: String,
    val code: String,
    val refer: Refer,
    val now: Now,
    val station: List<Now>,
    val updateTime: String
) {
    sealed class Now(
        val no2: String,
        val o3: String,
        val level: String,
        val pm2P5: String,
        val pubTime: String,
        val so2: String,
        val aqi: String,
        val pm10: String,
        val category: String,
        val co: String,
        val primary: String,
        val name: String? = null,
        val id: String? = null
    )

    data class Refer(
        val license: List<String>,
        val sources: List<String>
    )
}