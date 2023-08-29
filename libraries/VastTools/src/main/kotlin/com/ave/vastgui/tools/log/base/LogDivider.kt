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

package com.ave.vastgui.tools.log.base

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/8/28
// Documentation: https://ave.entropy2020.cn/documents/VastTools/log/Description/

/**
 * Log divider char.
 *
 * @since 0.5.2
 */
enum class LogDivider(val char: Char) {
    TOP_LEFT('╔'),
    BOTTOM_LEFT('╚'),
    BOLD_DIVIDER('═'),
    BOLD_START('║'),
    NORMAL_DIVIDER('─'),
    NORMAL_START('╟');

    companion object {
        /**
         * Get top bold divider with the length of [len].
         *
         * @since 0.5.2
         */
        fun getTop(len: Int) =
            "${LogDivider.TOP_LEFT.char}${String(CharArray(len) { LogDivider.BOLD_DIVIDER.char })}"

        /**
         * Get normal divider with the length of [len].
         *
         * @since 0.5.2
         */
        fun getDivider(len: Int) =
            "${LogDivider.NORMAL_START.char}${String(CharArray(len) { LogDivider.NORMAL_DIVIDER.char })}"

        /**
         * Get bottom bold divider with the length of [len].
         *
         * @since 0.5.2
         */
        fun getBottom(len: Int) =
            "${LogDivider.BOTTOM_LEFT.char}${String(CharArray(len) { LogDivider.BOLD_DIVIDER.char })}"

        /**
         * Get info with [LogDivider.BOLD_START].
         *
         * @since 0.5.2
         */
        fun getInfo(content: String) =
            "${LogDivider.BOLD_START.char} $content"
    }
}