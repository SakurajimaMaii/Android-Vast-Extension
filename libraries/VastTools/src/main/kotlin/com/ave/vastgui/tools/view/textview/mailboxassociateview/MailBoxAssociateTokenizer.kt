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

package com.ave.vastgui.tools.view.textview.mailboxassociateview

import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.widget.MultiAutoCompleteTextView


// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/4/2

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
        var index = text.toString().indexOf("@")
        if (index < 0) {
            index = text.length
        }
        if (index > findTokenEnd(text, cursor)) {
            index = 0
        }
        return index
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
        var i = cursor
        val len = text.length
        // 向后查找'@'字符，若找到则直接返回其所在位置
        while (i < len) {
            if (text[i] == '@') {
                return i
            } else {
                i++
            }
        }
        return len
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
        return if (i > 0 && text[i - 1] == '@') {
            text
        } else {
            // CharSequence类型的数据有可能是富文本SpannableString类型
            // 故需要进行判断
            if (text is Spanned) {
                SpannableString(text).apply {
                    TextUtils.copySpansFrom(text, 0, text.length, Any::class.java, this, 0)
                }
            } else {
                text
            }
        }
    }
}