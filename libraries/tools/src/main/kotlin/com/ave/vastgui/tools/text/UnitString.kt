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

@file:JvmName("UnitStringKt")

package com.ave.vastgui.tools.text

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/8/30 18:59
// Documentation: https://ave.entropy2020.cn/documents/VastTools/core-topics/app-resources/text/unit-string/

/**
 * StrUnit
 *
 * @property unit The unit format string.
 * @since 0.5.2
 */
@JvmDefaultWithCompatibility
interface StrUnit {
    val unit: String

    /**
     * Get [value] in unit.
     *
     * @since 0.5.1
     */
    fun getValue(value: String): String = "$value$unit"
}

open class Angle : StrUnit {
    final override val unit = "°"
}

open class Celsius : StrUnit {
    final override val unit = "℃"
}

open class Kmh : StrUnit {
    final override val unit = "km/h"
}

open class Kilometer : StrUnit {
    final override val unit = "km"
}

open class Meter : StrUnit {
    final override val unit = "m"
}

open class Ms : StrUnit {
    final override val unit = "m/s"
}

open class Percent : StrUnit {
    override val unit = "%"
}

/**
 * Get the string in [unit].
 *
 * @return the temperature string in Celsius unit,like 39℃.
 * @since 0.5.1
 */
fun Int.withUnit(unit: StrUnit): String = unit.getValue(this.toString())

/**
 * Get the string in [unit].
 *
 * @return the temperature string in Celsius unit,like 39℃.
 * @since 0.5.1
 */
fun Float.withUnit(unit: StrUnit): String = unit.getValue(this.toString())

/**
 * Get the string in [unit].
 *
 * @return the temperature string in Celsius unit,like 39℃.
 * @since 0.5.1
 */
fun Double.withUnit(unit: StrUnit): String = unit.getValue(this.toString())

/**
 * Get the string in [unit].
 *
 * @return the temperature string in Celsius unit,like 39℃.
 * @since 0.5.1
 */
fun String.withUnit(unit: StrUnit): String = unit.getValue(this)