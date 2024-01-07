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

package com.ave.vastgui.tools.text.appendablestylestring

import android.graphics.BlurMaskFilter
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.text.style.AbsoluteSizeSpan
import android.text.style.AlignmentSpan
import android.text.style.BackgroundColorSpan
import android.text.style.ForegroundColorSpan
import android.text.style.ImageSpan
import android.text.style.MaskFilterSpan
import android.text.style.RelativeSizeSpan
import android.text.style.ScaleXSpan
import android.text.style.StrikethroughSpan
import android.text.style.StyleSpan
import android.text.style.SubscriptSpan
import android.text.style.SuperscriptSpan
import android.text.style.URLSpan
import android.text.style.UnderlineSpan
import android.widget.TextView

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/5/21
// Documentation: https://ave.entropy2020.cn/documents/VastTools/core-topics/text/appendable-style-string/appendable-style-string/

/**
 * Create a style string scope.
 *
 * @since 0.5.1
 */
inline fun appendableStyleScope(
    textView: TextView,
    scope: SpannableStringBuilder.() -> Unit
) {
    val builder = SpannableStringBuilder()
    builder.scope()
    textView.movementMethod = LinkMovementMethod.getInstance()
    textView.text = SpannableString(builder)
}

/**
 * Create a style string scope.
 *
 * @since 0.5.1
 */
inline fun appendableStyleScope(
    scope: SpannableStringBuilder.() -> Unit
) = run {
    val builder = SpannableStringBuilder()
    builder.scope()
    SpannableString(builder)
}

/**
 * Add style string.
 *
 * @since 0.5.1
 */
inline fun SpannableStringBuilder.withStyle(
    style: AppendableStyle = AppendableStyle(),
    scope: SpannableStringBuilder.() -> Unit
) = run {
    val start = length
    val flag = SpanMode.SEE.value
    scope()
    this.apply {
        with(style) {
            setSpan(RelativeSizeSpan(proportion), start, length, flag)
            setSpan(ScaleXSpan(xProportion), start, length, flag)
            foreColor?.let {
                setSpan(ForegroundColorSpan(it), start, length, flag)
            }
            setSpan(BackgroundColorSpan(backColor), start, length, flag)
            setSpan(StyleSpan(fontStyle.value), start, length, flag)
            setSpan(fontFamily, start, length, flag)
            when (strikeMode) {
                StrikeMode.STRIKETHROUGH ->
                    setSpan(StrikethroughSpan(), start, length, flag)

                StrikeMode.UNDERLINE ->
                    setSpan(UnderlineSpan(), start, length, flag)

                StrikeMode.NONE -> {}
            }
            when (scriptMode) {
                ScriptMode.SUPERSCRIPT ->
                    setSpan(SuperscriptSpan(), start, length, flag)

                ScriptMode.SUBSCRIPT ->
                    setSpan(SubscriptSpan(), start, length, flag)

                ScriptMode.NONE -> {}
            }
            fontAlign?.let {
                setSpan(AlignmentSpan.Standard(it), start, length, flag)
            }
            linesIndent?.let {
                setSpan(it, start, length, flag)
            }
            quoteSpan?.let {
                setSpan(it, start, length, 0)
            }
            bulletSpan?.let {
                setSpan(it, start, length, 0)
            }
            fontSize?.let { size ->
                setSpan(AbsoluteSizeSpan(size), start, length, flag)
            }
            clickSpan?.let {
                setSpan(it, start, length, flag)
            }
            url?.let {
                setSpan(URLSpan(it), start, length, flag)
            }
            blur?.let {
                setSpan(MaskFilterSpan(BlurMaskFilter(blurRadius, blur)), start, length, flag)
            }
        }
    }
    this
}


/**
 * Add image into string.
 *
 * @since 0.5.1
 */
fun SpannableStringBuilder.withImage(image: ImageSpan) = run {
    val start = length
    append(" ")
    setSpan(image, start, length, SpanMode.SEE.value)
    this
}