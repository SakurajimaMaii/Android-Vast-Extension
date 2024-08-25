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

package com.ave.vastgui.tools.view.textview.mailboxassociateview

import android.content.Context
import android.util.AttributeSet
import android.widget.ArrayAdapter
import androidx.appcompat.widget.AppCompatMultiAutoCompleteTextView
import com.ave.vastgui.tools.R

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/4/2
// Documentation: https://ave.entropy2020.cn/documents/tools/core-topics/ui/textview/mail-box-associate-view/

/**
 * Default mail box postfix string array.
 *
 * @since 0.5.3
 */
private fun MailBoxAssociateView.defaultMailBoxPostfix(): Array<String> =
    resources.getStringArray(R.array.default_mail_box_postfix)

/**
 * [MailBoxAssociateView] default adapter
 *
 * @since 0.5.3
 */
fun MailBoxAssociateView.defaultMailBoxAssociateViewAdapter() =
    ArrayAdapter(this.context, android.R.layout.simple_list_item_1, defaultMailBoxPostfix())

/**
 * MailBoxAssociateView.
 *
 * @see MailBoxAssociateTokenizer
 * @since 0.2.0
 */
class MailBoxAssociateView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = androidx.appcompat.R.attr.autoCompleteTextViewStyle,
) : AppCompatMultiAutoCompleteTextView(context, attrs, defStyleAttr) {

    override fun enoughToFilter(): Boolean {
        return text.toString().contains("@") && text.toString().indexOf("@") > 0
    }

}