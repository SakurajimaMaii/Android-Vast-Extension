/*
 * Copyright 2021-2024 VastGui
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

@file:JvmName("PwdRegexKt")

package com.ave.vastgui.tools.text

import androidx.annotation.IntRange

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/6/11
// Documentation: https://ave.entropy2020.cn/documents/tools/core-topics/text/pwd-regex/

/**
 * Pwd strength
 *
 * @since 0.5.2
 */
interface PwdStrength {
    val regex: String
}

/**
 * Password contains at least one letter and one number.
 *
 * @since 0.5.2
 */
object PwdStrength1 : PwdStrength {
    override val regex: String
        get() = "(?=.*[A-Za-z])(?=.*\\d)."
}

/**
 * Password contains at least one uppercase letter, one lowercase letter
 * and one number.
 *
 * @since 0.5.2
 */
object RwdStrength2 : PwdStrength {
    override val regex: String
        get() = "(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)."
}

/**
 * Password contains at least one letter, one number and one special
 * character. Special characters include **!@#$%^& * .**
 *
 * @since 0.5.2
 */
object RwdStrength3 : PwdStrength {
    override val regex: String
        get() = "(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#\\$%\\^&\\*\\.])."
}

/**
 * Password contains at least one uppercase letter, one lowercase letter,
 * one number and one special character. Special characters include
 * **!@#$%^& * .**
 *
 * @since 0.5.2
 */
object RwdStrength4 : PwdStrength {
    override val regex: String
        get() = "(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#\\\$%\\^&\\*\\.])."
}

/**
 * Check if string meets password requirements.
 *
 * @param strength The strength of the password you want.
 * @param minLength Minimum password length.
 * @param maxLength Maximum password length.
 * @param shouldNotAppear What you think should not be in the password, for
 *     example "admin".
 * @return True if the String is a password, false otherwise.
 * @throws IllegalArgumentException
 * @since 0.5.2
 */
@JvmOverloads
fun String.isPwd(
    strength: PwdStrength = PwdStrength1,
    @IntRange(from = 0) minLength: Int = 6,
    @IntRange(from = 0) maxLength: Int = 20,
    shouldNotAppear: Array<String> = emptyArray(),
): Boolean {
    return if (minLength > maxLength) {
        throw IllegalArgumentException("minLength should be less than maxLength")
    } else if ((this.length < minLength) or (this.length > maxLength)) {
        false
    } else {
        if (Regex("${strength.regex}{$minLength,$maxLength}").matches(this)) {
            for (content in shouldNotAppear) {
                if (this.contains(content)) {
                    return false
                }
            }
            true
        } else false
    }
}