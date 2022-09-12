/*
 * Copyright 2022 VastGui
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gcode.vasttools.utils

import androidx.annotation.StringRes
import com.gcode.vasttools.R
import com.gcode.vasttools.helper.ContextHelper


// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/6/20
// Description: 
// Documentation:

object UnitStrUtils {

    /**
     * Get the temperature string in Celsius unit.
     *
     * @param value the value or string res id of temperature.
     * @return the temperature string in Celsius unit,like 39℃.
     * @since 0.0.9
     */
    @JvmStatic
    @Throws(RuntimeException::class)
    fun getCelsius(value: Any): String {
        return getUnitFormatString(value, R.string.unit_celsius)
    }

    /**
     * Get the speed string in m/s.
     *
     * @param value the value of temperature.
     * @return the speed string in m/s,like 39m/s.
     * @since 0.0.9
     */
    @JvmStatic
    @Throws(RuntimeException::class)
    fun getSpeed(value: Any): String {
        return getUnitFormatString(value, R.string.unit_speed)
    }

    /**
     * Get the angle value string.
     *
     * @param value the value of angle.
     * @return the angle value string,like 39°.
     * @since 0.0.9
     */
    @JvmStatic
    @Throws(RuntimeException::class)
    fun getAngle(value: Any): String {
        return getUnitFormatString(value, R.string.unit_angle)
    }

    /**
     * Get the speed value string.
     *
     * @param value the value of angle.
     * @return the speed value string,like 39km/h.
     * @since 0.0.9
     */
    @JvmStatic
    @Throws(RuntimeException::class)
    fun getKm(value: Any): String {
        return getUnitFormatString(value, R.string.unit_km)
    }

    /**
     * Get the percent value string.
     *
     * @param value the value of angle.
     * @return the percent value string,like 39%.
     * @since 0.0.9
     */
    @JvmStatic
    @Throws(RuntimeException::class)
    fun getPercent(value: Any): String {
        return getUnitFormatString(value, R.string.unit_percent)
    }

    /**
     * Get format string with unit.
     *
     * @throws RuntimeException
     * @since 0.0.9
     */
    @Throws(RuntimeException::class)
    private fun getUnitFormatString(value: Any, @StringRes unit: Int): String {
        return when (value) {
            is Int -> String.format(
                ResUtils.getString(unit),
                ResUtils.getString(value)
            )
            is String -> String.format(
                ResUtils.getString(unit),
                value
            )
            else -> {
                throw RuntimeException("The type of value should be ${Int.Companion::class.java.simpleName} or ${String.Companion::class.java.simpleName}")
            }
        }
    }

}