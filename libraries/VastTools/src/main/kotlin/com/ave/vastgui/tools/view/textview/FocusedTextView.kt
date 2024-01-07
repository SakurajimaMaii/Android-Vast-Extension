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

package com.ave.vastgui.tools.view.textview

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.ave.vastgui.tools.R

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/4/9 21:59
// Description: When you want to show the marquee effect, you can use FocusedTextView.
// Documentation: https://ave.entropy2020.cn/documents/VastTools/core-topics/ui/textview/marquee-text-view/

/**
 * MarqueeTextView
 *
 * @property marqueeNum Set to -1 to repeat indefinitely, the number of
 *     repetitions is an integer greater than or equal to 0 otherwise.
 * @since 0.2.0
 */
class MarqueeTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.Default_MarqueeTextView_Style,
) : AppCompatTextView(context, attrs, defStyleAttr) {

    private var marqueeNum = -1

    /**
     * Set [marqueeNum]
     *
     * @since 0.2.0
     */
    fun setMarqueeNum(marqueeNum: Int) {
        this.marqueeNum = marqueeNum
    }

    /**
     * Set related properties.
     *
     * @since 0.2.0
     */
    private fun setAttr() {
        this.ellipsize = TextUtils.TruncateAt.MARQUEE
        this.marqueeRepeatLimit = marqueeNum
        this.isSingleLine = true
        this.isFocusable = true
        this.isFocusableInTouchMode = true
        this.setHorizontallyScrolling(true);
        this.isSelected = true;
    }

    override fun isFocused(): Boolean {
        if (!isSelected) {
            return true
        }
        return super.isFocused()
    }

    init {
        setAttr()
    }

}

/** When you want to show the marquee text, you can use [FocusedTextView]. */
@Deprecated(
    message = "Please use MarqueeTextView.",
    replaceWith = ReplaceWith(
        "MarqueeTextView",
        "com.ave.vastgui.tools.view.textview.MarqueeTextView"
    ),
    level = DeprecationLevel.WARNING
)
open class FocusedTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : AppCompatTextView(context, attrs, defStyleAttr) {

    override fun isFocused(): Boolean {
        return true
    }

}