/*
 * Copyright 2021-2025 VastGui
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
package com.ave.vastgui.core.text

/**
 * Ascii/Unicode utils.
 *
 * @since 0.1.4
 */
object AsciiUtil {

    /**
     * The ascii of halfwidth-space.
     *
     * @since 0.1.4
     */
    const val ASCII_SPACE: Char = 0x20.toChar()

    /**
     * The begin of ascii (exclude space).
     *
     * @since 0.1.4
     */
    const val ASCII_BEGIN: Char = 0x21.toChar()

    /**
     * The end of ascii.
     *
     * @since 0.1.4
     */
    const val ASCII_END: Char = 0x7E.toChar()

    /**
     * The unicode of fullwidth-space.
     *
     * @since 0.1.4
     */
    const val UNICODE_SPACE: Char = 0x3000.toChar()

    /**
     * The begin of unicode (exclude space).
     *
     * @since 0.1.4
     */
    const val UNICODE_START: Char = 0xFF01.toChar()

    /**
     * The end of unicode.
     *
     * @since 0.1.4
     */
    const val UNICODE_END: Char = 0xFF5E.toChar()

    /**
     * ascii + [ASCII_UNICODE_STEP] = unicode
     *
     * @since 0.1.4
     */
    const val ASCII_UNICODE_STEP: Char = 0xFEE0.toChar()

    /**
     * Return `true` if the [c] is fullwidth.
     *
     * @since 0.1.4
     */
    fun isFullWidth(c: Char): Boolean {
        return c == UNICODE_SPACE || c in UNICODE_START..UNICODE_END
    }

    /**
     * Return `true` if the [c] is halfwidth.
     *
     * @since 0.1.4
     */
    fun isHalfChar(c: Char): Boolean {
        return c == ASCII_SPACE || c in ASCII_BEGIN..ASCII_END
    }

    /**
     * Convert [char] from fullwidth to halfwidth.
     *
     * @since 0.1.4
     */
    fun full2half(char: Char): Char {
        return if (char == UNICODE_SPACE)
            ASCII_SPACE
        else if (char in UNICODE_START..UNICODE_END)
            (char.code - ASCII_UNICODE_STEP.code).toChar()
        else
            char
    }

    /**
     * Convert [char] from halfwidth to fullwidth.
     *
     * @since 0.1.4
     */
    fun half2full(char: Char): Char {
        return if (char == ASCII_SPACE)
            UNICODE_SPACE
        else if (char in ASCII_BEGIN..ASCII_END)
            (char.code + ASCII_UNICODE_STEP.code).toChar()
        else
            char
    }

    /**
     * Convert [src] from fullwidth to halfwidth.
     *
     * @since 0.1.4
     */
    fun full2half(src: String?): String? {
        if(src.isNullOrBlank()) return null
        return src.forEach(::full2half).toString()
    }

    /**
     * Convert [src] from halfwidth to fullwidth.
     *
     * @since 0.1.4
     */
    fun half2full(src: String?): String? {
        if(src.isNullOrBlank()) return null
        return src.forEach(::half2full).toString()
    }
}