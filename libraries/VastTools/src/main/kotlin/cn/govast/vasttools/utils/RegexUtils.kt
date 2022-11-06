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

import androidx.annotation.IntRange

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/3/10 15:27
// Description: Provides regex checks for some common strings.
// Documentation: [RegexUtils](https://sakurajimamaii.github.io/VastDocs/document/en/RegexUtils.html)

object RegexUtils {

    enum class PasswordMode{
        /** Password contains at least numbers and letters. */
        EASY,
        /** Password contains at least numbers and letters, and can have characters. */
        NORMAL,
        /** Password contains two or more types:numbers, letters, and characters. */
        HARD
    }

    /**
     * Check if string is Email address.
     *
     * @param string string to matches.
     * @return true if the String is a email address, false otherwise.
     */
    @JvmStatic
    fun isEmail(string: String): Boolean {
        return Regex("[A-Za-z\\d-_\u4e00-\u9fa5]+@[a-zA-Z\\d_-]+(\\.[a-zA-Z\\d_-]+)+").matches(
            string
        )
    }

    /**
     * Check if string meets password requirements.
     *
     * @param string string to matches.
     * @param case [PasswordMode]
     * @param minLength Minimum password length.
     * @param maxLength Maximum password length.
     * @return true if the String is a password, false otherwise.
     * @throws IllegalArgumentException
     */
    @JvmStatic
    @JvmOverloads
    fun isPwd(
        string: String,
        case: PasswordMode = PasswordMode.EASY,
        @IntRange(from = 0) minLength: Int = 6,
        @IntRange(from = 0) maxLength: Int = 20
    ): Boolean {
        if (minLength > maxLength) {
            throw IllegalArgumentException("minLength should be less than maxLength")
        }
        if ((string.length < minLength) or (string.length > maxLength)) {
            throw IllegalArgumentException("Password length does not meet requirements")
        }
        return when (case) {
            PasswordMode.EASY -> Regex("(?!\\d+\$)(?![a-zA-Z]+\$)[\\dA-Za-z]{$minLength,$maxLength}").matches(
                string
            )
            PasswordMode.NORMAL -> Regex("(?=.*([a-zA-Z].*))(?=.*\\d.*)[a-zA-Z\\d-*/+.~!@#\$%^&()]{$minLength,$maxLength}").matches(
                string
            )
            PasswordMode.HARD -> Regex("(?!\\d+\$)(?![a-z]+\$)(?![A-Z]+\$)(?!([^(\\da-zA-Z)])+\$).{$minLength,$maxLength}").matches(
                string
            )
        }
    }

    /**
     * Check if string is QQ number.
     *
     * @param string string to matches.
     * @return true if the String is a qq number, false otherwise.
     */
    @JvmStatic
    fun isQQ(string: String): Boolean {
        return Regex("\\d{4,}").matches(string)
    }

    /**
     * String verified according to the Chinese phone number.
     *
     * @param string string to matches.
     * @return true if the String is a phone number, false otherwise.
     */
    @JvmStatic
    fun isPhoneNumber(string: String): Boolean {
        return Regex("1(3\\d|4[5-9]|5[0-35-9]|6[567]|7[0-8]|8\\d|9[0-35-9])\\d{8}").matches(string)
    }

    /**
     * String to match.By default, it is verified according to the
     * Chinese phone number.
     *
     * @param string string to matches.
     * @param otherCountryPattern Other National Phone Number Formats.
     * @return true if the String is a phone number, false otherwise.
     */
    @JvmStatic
    @Deprecated(
        "The function is deprecated!",
        ReplaceWith("isPhoneNumber(string)", "new.isPhoneNumber"),
        DeprecationLevel.WARNING
    )
    fun isPhoneNumber(string: String, otherCountryPattern: String?): Boolean {
        return if (null != otherCountryPattern) {
            Regex(otherCountryPattern).matches(string)
        } else {
            Regex("1(3\\d|4[5-9]|5[0-35-9]|6[567]|7[0-8]|8\\d|9[0-35-9])\\d{8}").matches(string)
        }
    }

    /**
     * Check if string is a number
     *
     * @param string string to matches.
     * @return true if the String is a number, false otherwise.
     */
    @JvmStatic
    fun isNumeric(string: String): Boolean {
        return Regex("\\d*").matches(string)
    }
}