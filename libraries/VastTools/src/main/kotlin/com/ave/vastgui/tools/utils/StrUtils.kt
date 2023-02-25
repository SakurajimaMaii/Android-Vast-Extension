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
// Description: 
// Documentation:

object StrUtils {

    enum class Unit(val unit: String) {
        Angle(ResUtils.getString(R.string.unit_angle)),
        Celsius(ResUtils.getString(R.string.unit_celsius)),
        Kmh(ResUtils.getString(R.string.unit_kmh)),
        Kilometer(ResUtils.getString(R.string.unit_km)),
        Meter(ResUtils.getString(R.string.unit_m)),
        Ms(ResUtils.getString(R.string.unit_ms)),
        Percent(ResUtils.getString(R.string.unit_percent))
    }

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
     * Get the string in [unit].
     *
     * @param value the value or string res id.
     * @return the temperature string in Celsius unit,like 39℃.
     */
    @JvmStatic
    @Throws(RuntimeException::class)
    fun getUnitStr(value: Any, unit: Unit): String {
        return getUnitFormatString(value, unit.unit)
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