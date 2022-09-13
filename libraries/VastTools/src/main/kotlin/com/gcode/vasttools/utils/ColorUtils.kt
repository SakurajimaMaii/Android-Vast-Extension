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

package com.gcode.vasttools.utils

import android.graphics.Color
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import com.gcode.vasttools.helper.ContextHelper

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/3/10 15:27
// Description: ColorUtils Provides methods for converting between different formats of Color.
// Documentation: [ColorUtils](https://sakurajimamaii.github.io/VastDocs/document/en/ColorUtils.html)

object ColorUtils {

    /**
     * Color hex regex.Supported formats are `#RRGGBB` and `#AARRGGBB`.
     *
     * @since 0.0.9
     */
    const val COLOR_HEX_REGEX = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{8})$"

    /**
     * Color parse failed.
     *
     * @since 0.0.9
     */
    const val COLOR_PARSE_ERROR = 0

    /**
     * Wrong result when converting color to RGB.
     *
     * @since 0.0.9
     */
    @JvmField val COLOR_RGB_ARRAY_ERROR = intArrayOf(-1, -1, -1)

    /**
     * Parse the color string, and return the corresponding color-int.
     *
     * @param colorHex Color string like #12c2e9.
     * @return the corresponding color-int of the color string, [COLOR_PARSE_ERROR] otherwise.
     * @since 0.0.5
     */
    @JvmStatic
    fun colorHex2Int(colorHex: String): Int {
        return if (isColorHex(colorHex)) {
            Color.parseColor(colorHex)
        } else COLOR_PARSE_ERROR
    }

    /**
     * Converting color hexadecimal string to an array of RGB.
     *
     * @param colorHex Color hexadecimal string,for example:#12c2e9.
     * @return the corresponding color rgb array like {18,194,233},[COLOR_RGB_ARRAY_ERROR] otherwise.
     * @since 0.0.5
     */
    @JvmStatic
    fun colorHex2RGB(colorHex: String): IntArray {
        val colorInt = colorHex2Int(colorHex)
        return if (COLOR_PARSE_ERROR == colorInt) {
            COLOR_RGB_ARRAY_ERROR
        } else colorInt2RGB(colorInt)
    }

    /**
     * Converting color-int to hexadecimal string.
     *
     * @param colorInt color-int.
     * @return color hexadecimal string like #3FE2C5.
     *
     * @since 0.0.5
     */
    @JvmStatic
    fun colorInt2Hex(@ColorInt colorInt: Int): String {
        val rgb = colorInt2RGB(colorInt)
        return colorRGB2Hex(rgb)
    }

    /**
     * Converting color-int to an array of RGB.
     *
     * @param colorInt color-int.
     * @return an array of RGB,like {63,226,197}.
     *
     * @since 0.0.5
     */
    @JvmStatic
    fun colorInt2RGB(@ColorInt colorInt: Int): IntArray {
        val result = intArrayOf(0, 0, 0)
        result[0] = Color.red(colorInt)
        result[1] = Color.green(colorInt)
        result[2] = Color.blue(colorInt)
        return result
    }

    /**
     * Converting an array of RGB to color hexadecimal string.
     *
     * @param rgb an array of RGB like {63,226,197}.
     * @return Color hexadecimal string like #3FE2C5.
     *
     * @since 0.0.5
     */
    @JvmStatic
    fun colorRGB2Hex(rgb: IntArray): String {
        var hexCode = "#"
        for (i in rgb.indices) {
            var rgbItem = rgb[i]
            if (rgbItem < 0) {
                rgbItem = 0
            } else if (rgbItem > 255) {
                rgbItem = 255
            }
            val code =
                arrayOf(
                    "0",
                    "1",
                    "2",
                    "3",
                    "4",
                    "5",
                    "6",
                    "7",
                    "8",
                    "9",
                    "A",
                    "B",
                    "C",
                    "D",
                    "E",
                    "F"
                )
            val lCode = rgbItem / 16
            val rCode = rgbItem % 16
            hexCode += code[lCode] + code[rCode]
        }
        return hexCode
    }

    /**
     * Converting an array of RGB to color-int.
     *
     * @param rgb an array of RGB like {63,226,197}.
     * @return color-int.
     *
     * @since 0.0.5
     */
    @JvmStatic
    fun colorRGB2Int(rgb: IntArray): Int {
        return Color.rgb(rgb[0], rgb[1], rgb[2])
    }

    /**
     * Return true if the color hex string is right,false otherwise.
     *
     * @param colorHex color hex string.
     * @return true if the color hex string is right,false otherwise.
     *
     * @since 0.0.9
     */
    @JvmStatic
    fun isColorHex(colorHex: String): Boolean {
        return Regex(COLOR_HEX_REGEX).matches(colorHex)
    }

}