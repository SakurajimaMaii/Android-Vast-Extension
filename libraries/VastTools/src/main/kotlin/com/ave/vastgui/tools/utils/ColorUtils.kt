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

import android.graphics.Color
import androidx.annotation.ColorInt
import androidx.annotation.IntRange

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/3/10 15:27
// Documentation: https://ave.entropy2020.cn/documents/VastTools/core-topics/app-resources/color-utils/

object ColorUtils {

    /** Color hex regex.Supported formats are `#RRGGBB` and `#AARRGGBB`. */
    const val COLOR_HEX_REGEX = "^#([A-Fa-f\\d]{6}|[A-Fa-f\\d]{8})$"

    /** Color parse failed. */
    const val COLOR_PARSE_ERROR = 0

    /** Wrong result when converting color to RGB. */
    val COLOR_RGB_ARRAY_ERROR = intArrayOf(-1, -1, -1, -1)

    /** Map of color transparency. */
    @JvmField
    val ColorTransparency = mapOf(
        100 to "FF",
        99 to "FC",
        98 to "FA",
        97 to "F7",
        96 to "F5",
        95 to "F2",
        94 to "F0",
        93 to "ED",
        92 to "EB",
        91 to "E8",
        90 to "E6",
        89 to "E3",
        88 to "E0",
        87 to "DE",
        86 to "DB",
        85 to "D9",
        84 to "D6",
        83 to "D4",
        82 to "D1",
        81 to "CF",
        80 to "CC",
        79 to "C9",
        78 to "C7",
        77 to "C4",
        76 to "C2",
        75 to "BF",
        74 to "BD",
        73 to "BA",
        72 to "B8",
        71 to "B5",
        70 to "B3",
        69 to "B0",
        68 to "AD",
        67 to "AB",
        66 to "A8",
        65 to "A6",
        64 to "A3",
        63 to "A1",
        62 to "9E",
        61 to "9C",
        60 to "99",
        59 to "96",
        57 to "94",
        56 to "91",
        56 to "8F",
        55 to "8C",
        54 to "8A",
        53 to "87",
        52 to "85",
        51 to "82",
        50 to "80",
        49 to "7D",
        48 to "7A",
        47 to "78",
        46 to "75",
        45 to "73",
        44 to "70",
        43 to "6E",
        42 to "6B",
        41 to "69",
        40 to "66",
        39 to "63",
        38 to "61",
        37 to "5E",
        36 to "5C",
        35 to "59",
        34 to "57",
        33 to "54",
        32 to "52",
        31 to "4F",
        30 to "4D",
        28 to "4A",
        28 to "47",
        27 to "45",
        26 to "42",
        25 to "40",
        24 to "3D",
        23 to "3B",
        22 to "38",
        21 to "36",
        20 to "33",
        19 to "30",
        18 to "2E",
        17 to "2B",
        16 to "29",
        15 to "26",
        14 to "24",
        13 to "21",
        12 to "1F",
        11 to "1C",
        10 to "1A",
        9 to "17",
        8 to "14",
        7 to "12",
        6 to "0F",
        5 to "0D",
        4 to "0A",
        3 to "08",
        2 to "05",
        1 to "03",
        0 to "00"
    )

    /**
     * Parse the color string, and return the corresponding color-int.
     *
     * @return the corresponding color-int of the color string,
     *     [COLOR_PARSE_ERROR] otherwise.
     * @see Color.parseColor
     */
    @JvmStatic
    fun colorHex2Int(colorHex: String): Int {
        return if (isColorHex(colorHex)) {
            Color.parseColor(colorHex)
        } else COLOR_PARSE_ERROR
    }

    /**
     * Converting color hexadecimal string to an array of ARGB.
     *
     * @param colorHex Color hexadecimal string,for example:#12c2e9.
     * @return The corresponding color rgb array like {255,63,226,197},
     *     [COLOR_RGB_ARRAY_ERROR] otherwise.
     * @since 0.5.3
     */
    @JvmStatic
    fun colorHex2ARGB(colorHex: String): IntArray {
        val colorInt = colorHex2Int(colorHex)
        return if (COLOR_PARSE_ERROR == colorInt) {
            COLOR_RGB_ARRAY_ERROR
        } else colorInt2ARGB(colorInt)
    }

    /**
     * Converting color-int to hexadecimal string.
     *
     * @param colorInt color-int.
     * @return color hexadecimal string like #3FE2C5.
     */
    @JvmStatic
    fun colorInt2Hex(@ColorInt colorInt: Int): String = colorRGB2Hex(colorInt2ARGB(colorInt))

    /**
     * Converting color-int to an array of ARGB.
     *
     * @param colorInt color-int.
     * @return An array of ARGB, like {255,63,226,197}.
     * @since 0.5.3
     */
    @JvmStatic
    fun colorInt2ARGB(@ColorInt colorInt: Int): IntArray = intArrayOf(
        Color.alpha(colorInt), Color.red(colorInt), Color.green(colorInt), Color.blue(colorInt)
    )

    /**
     * Converting an array of RGB to color hexadecimal string.
     *
     * @param rgb an array of RGB like {63,226,197}.
     * @return Color hexadecimal string like #3FE2C5.
     */
    @JvmStatic
    fun colorRGB2Hex(argb: IntArray): String {
        var hexCode = "#"
        for (i in argb.indices) {
            var rgbItem = argb[i]
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
     * Get the color hex in the format of #AARRGGBB by [transparency] and
     * [colorInt].
     *
     * If the given [colorInt] itself has transparency, it will be forced to
     * the transparency specified by [transparency].
     */
    @JvmStatic
    fun getColorWithTransparency(
        @IntRange(from = 0, to = 100) transparency: Int,
        colorInt: Int
    ): String {
        val color = if (Color.alpha(colorInt) != 255) {
            Color.rgb(Color.red(colorInt), Color.green(colorInt), Color.blue(colorInt))
        } else colorInt
        val colorHex = colorInt2Hex(color)
        return StringBuilder(colorHex).insert(1, ColorTransparency[transparency]).toString()
    }

    /**
     * Get the color-int by [transparency] and [colorInt].
     *
     * If the given [colorInt] itself has transparency, it will be forced to
     * the transparency specified by [transparency].
     *
     * @since 0.5.3
     */
    fun getColorIntWithTransparency(
        @IntRange(from = 0, to = 100) transparency: Int,
        colorInt: Int
    ) = colorHex2Int(getColorWithTransparency(transparency, colorInt))

    /**
     * Return true if the color hex string is right,false otherwise.
     *
     * @param colorHex color hex string.
     * @return true if the color hex string is right,false otherwise.
     */
    @JvmStatic
    fun isColorHex(colorHex: String): Boolean {
        return Regex(COLOR_HEX_REGEX).matches(colorHex)
    }

}