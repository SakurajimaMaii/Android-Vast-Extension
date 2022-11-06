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

package cn.govast.vasttools.utils

import android.graphics.Color
import androidx.annotation.ColorInt
import androidx.annotation.IntRange

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/3/10 15:27
// Description: ColorUtils Provides methods for converting between different formats of Color.
// Documentation: [ColorUtils](https://sakurajimamaii.github.io/VastDocs/document/en/ColorUtils.html)

object ColorUtils {

    /**
     * Color hex regex.Supported formats are `#RRGGBB` and `#AARRGGBB`.
     */
    @JvmField
    val COLOR_HEX_REGEX = "^#([A-Fa-f\\d]{6}|[A-Fa-f\\d]{8})$"

    /**
     * Color parse failed.
     */
    @JvmField
    val COLOR_PARSE_ERROR = 0

    /**
     * Wrong result when converting color to RGB.
     */
    @JvmField
    val COLOR_RGB_ARRAY_ERROR = intArrayOf(-1, -1, -1)

    /**
     * Map of color transparency.
     */
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
     * @param colorHex Color string like #12c2e9.
     * @return the corresponding color-int of the color string,
     *     [COLOR_PARSE_ERROR] otherwise.
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
     * @return the corresponding color rgb array like
     *     {18,194,233},[COLOR_RGB_ARRAY_ERROR] otherwise.
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
     */
    @JvmStatic
    fun colorRGB2Int(rgb: IntArray): Int {
        return Color.rgb(rgb[0], rgb[1], rgb[2])
    }

    /**
     * Get argb by [transparency] and [colorInt].
     */
    @JvmStatic
    fun getColorTransparency(@IntRange(from = 0, to = 100) transparency: Int, colorInt: Int):String {
        val colorHex = colorInt2Hex(colorInt)
        return StringBuilder(colorHex).insert(1, ColorTransparency[transparency]).toString()
    }

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