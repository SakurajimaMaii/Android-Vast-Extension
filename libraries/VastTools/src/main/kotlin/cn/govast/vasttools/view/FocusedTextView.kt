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

package cn.govast.vasttools.view

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/4/9 21:59
// Description: When you want to show the marquee effect, you can use FocusedTextView.
// Documentation: [FocusedTextView](https://sakurajimamaii.github.io/VastDocs/document/en/FocusedTextView.html)

/**
 * When you want to show the marquee effect, you can use [FocusedTextView].
 */
open class FocusedTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = android.R.attr.textViewStyle
) : AppCompatTextView(context, attrs, defStyle) {

    override fun isFocused(): Boolean {
        return true
    }

}