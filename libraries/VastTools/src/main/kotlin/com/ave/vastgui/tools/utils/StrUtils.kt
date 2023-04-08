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

package com.ave.vastgui.tools.utils

import com.ave.vastgui.tools.R

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
abstract class StrUnit(val unit: String)

class Angle : StrUnit(ResUtils.getString(R.string.unit_angle))
class Celsius : StrUnit(ResUtils.getString(R.string.unit_celsius))
class Kmh : StrUnit(ResUtils.getString(R.string.unit_kmh))
class Kilometer : StrUnit(ResUtils.getString(R.string.unit_km))
class Meter : StrUnit(ResUtils.getString(R.string.unit_m))
class Ms : StrUnit(ResUtils.getString(R.string.unit_ms))
class Percent : StrUnit(ResUtils.getString(R.string.unit_percent))

object StrUtils {

    /**
     * Concatenate the strings in the parameters in turn.
     *
     * @param str string to be concatenated.
     * @return concatenated string.
     */
    @JvmStatic
    fun strConcat(vararg str: Any): String {
        val sb = StringBuilder()
        for (s in str) {
            sb.append(s)
        }
        return sb.toString()
    }

    /**
     * Get the string in [strUnit].
     *
     * @param value the value or string res id.
     * @return the temperature string in Celsius unit,like 39℃.
     */
    @JvmStatic
    @Throws(RuntimeException::class)
    fun getUnitStr(value: Any, strUnit: StrUnit): String {
        return getUnitFormatString(value, strUnit.unit)
    }

    /**
     * Get format string with unit.
     *
     * @throws RuntimeException
     */
    @Throws(RuntimeException::class)
    private fun getUnitFormatString(value: Any, unit: String): String {
        return when (value) {
            is Int -> String.format(unit, ResUtils.getString(value))
            is String -> String.format(unit, value)
            else -> {
                throw RuntimeException("The type of value should be ${Int.Companion::class.java.simpleName} or ${String.Companion::class.java.simpleName}")
            }
        }
    }

}