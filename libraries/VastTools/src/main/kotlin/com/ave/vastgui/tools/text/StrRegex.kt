/*
 * Copyright 2024 VastGui guihy2019@gmail.com
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

@file:JvmName("StrRegexKt")

package com.ave.vastgui.tools.text

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/3/10 15:27
// Description: Provides regex checks for some common strings.
// Documentation: https://ave.entropy2020.cn/documents/VastTools/core-topics/text/str-regex/

/**
 * Check if string is Email address.
 *
 * @receiver String to matches.
 * @return True if the String is a email address, false otherwise.
 */
fun String.isEmail(): Boolean {
    return Regex("[A-Za-z\\d-_\u4e00-\u9fa5]+@[a-zA-Z\\d_-]+(\\.[a-zA-Z\\d_-]+)+").matches(
        this
    )
}

/**
 * Check if string is QQ number.
 *
 * @receiver String to matches.
 * @return True if the String is a qq number, false otherwise.
 */
fun String.isQQ(): Boolean {
    return Regex("\\d{4,}").matches(this)
}

/**
 * String verified according to the Chinese phone number.
 *
 * @receiver String to matches.
 * @return True if the String is a phone number, false otherwise.
 */
fun String.isPhoneNumber(): Boolean {
    return Regex("1(3\\d|4[5-9]|5[0-35-9]|6[567]|7[0-8]|8\\d|9[0-35-9])\\d{8}").matches(this)
}

/**
 * Check if string is a number
 *
 * @receiver String to matches.
 * @return True if the String is a number, false otherwise.
 */
fun String.isNumeric(): Boolean {
    return Regex("\\d*").matches(this)
}