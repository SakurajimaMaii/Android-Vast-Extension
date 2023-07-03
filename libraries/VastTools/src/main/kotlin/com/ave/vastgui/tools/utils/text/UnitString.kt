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

package com.ave.vastgui.tools.utils.text

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/8/30 18:59
// Documentation: https://ave.entropy2020.cn/documents/VastTools/core-topics/app-resources/string/StrUtils/

/**
 * StrUnit
 *
 * @property unit The unit format string.
 * @since 0.2.0
 */
abstract class StrUnit(val unit: String) {
    /**
     * Get [value] in unit.
     *
     * @since 0.5.1
     */
    open fun getValue(value: String): String = "$value$unit"
}

open class Angle : StrUnit("°")
open class Celsius : StrUnit("℃")
open class Kmh : StrUnit("km/h")
open class Kilometer : StrUnit("km")
open class Meter : StrUnit("m")
open class Ms : StrUnit("ms")
open class Percent : StrUnit("%")

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