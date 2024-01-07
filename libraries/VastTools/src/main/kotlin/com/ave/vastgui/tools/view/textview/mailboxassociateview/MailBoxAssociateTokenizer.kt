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

package com.ave.vastgui.tools.view.textview.mailboxassociateview

import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.widget.MultiAutoCompleteTextView

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/4/2
// Documentation: https://ave.entropy2020.cn/documents/VastTools/core-topics/ui/textview/mail-box-associate-view/

/**
 * MailBoxAssociateTokenizer
 *
 * Specifies where to start associative characters.
 *
 * @since 0.2.0
 */
class MailBoxAssociateTokenizer : MultiAutoCompleteTextView.Tokenizer {

    /**
     * Used to find the position of the delimiter preceding the current cursor
     * position and return.
     *
     * @param text The text edited by the user.
     * @param cursor Current cursor position.
     * @since 0.2.0
     */
    override fun findTokenStart(text: CharSequence, cursor: Int): Int {
        return text.indexOf("@")
    }

    /**
     * Used to find the position of the separator after the current cursor
     * position and return, [cursor] otherwise.
     *
     * @param text The text edited by the user.
     * @param cursor Current cursor position.
     * @since 0.2.0
     */
    override fun findTokenEnd(text: CharSequence, cursor: Int): Int {
        val index = text.indexOf('@', cursor)
        return if (index != -1) index else text.length
    }

    /** It is used to return the text content with the token. */
    override fun terminateToken(text: CharSequence): CharSequence {
        var i = text.length
        // Remove trailing spaces from the original matched data.
        while (i > 0 && text[i - 1] == ' ') {
            i--
        }
        // Determine whether the original matching data contains '@' after
        // removing the trailing space, and return immediately if there is.
        return if (text is Spanned) {
            SpannableString(text).apply {
                TextUtils.copySpansFrom(text, 0, text.length, Any::class.java, this, 0)
            }
        } else text
    }
}