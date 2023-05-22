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

package com.ave.vastgui.tools.utils.spannablestring

import android.graphics.BlurMaskFilter
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
import com.ave.vastgui.tools.bean.Component2
import com.ave.vastgui.tools.utils.LogUtils
import com.ave.vastgui.tools.utils.ResUtils

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/5/21
// Description: 
// Documentation:
// Reference:

/**
 * Create a style string.
 *
 * @since 0.5.1
 */
object AppendableStyleString {

    fun getStyleScope(textView: TextView, styleScope: StyleScope.() -> Unit) {
        StyleScope(textView).also(styleScope).finish()
    }

    class StyleScope internal constructor(private val mTextView: TextView) {
        private val mBuilder: SpannableStringBuilder = SpannableStringBuilder()
        private val flag = SpanMode.SEE.value

        /**
         * Add style string.
         *
         * @since 0.5.1
         */
        fun withStyle(
            style: AppendableStyle = AppendableStyle(),
            strScope: SpannableStringBuilder.() -> Unit
        ) {
            val (start, end) = foldScope(strScope)
            with(style) {
                mBuilder.apply {
                    setSpan(RelativeSizeSpan(proportion), start, end, flag)
                    setSpan(ScaleXSpan(xProportion), start, end, flag)
                    setSpan(ForegroundColorSpan(ResUtils.getColor(foreColor)), start, end, flag)
                    setSpan(BackgroundColorSpan(ResUtils.getColor(backColor)), start, end, flag)
                    setSpan(StyleSpan(fontStyle.value), start, end, flag)
                    setSpan(fontFamily, start, end, flag)
                    when (strikeMode) {
                        StrikeMode.STRIKETHROUGH ->
                            setSpan(StrikethroughSpan(), start, end, flag)

                        StrikeMode.UNDERLINE ->
                            setSpan(UnderlineSpan(), start, end, flag)

                        StrikeMode.NONE -> {}
                    }
                    when (scriptMode) {
                        ScriptMode.Superscript ->
                            setSpan(SuperscriptSpan(), start, end, flag)

                        ScriptMode.Subscript ->
                            setSpan(SubscriptSpan(), start, end, flag)

                        ScriptMode.None -> {}
                    }
                    fontAlign?.let {
                        setSpan(AlignmentSpan.Standard(it), start, end, flag)
                    }
                    linesIndent?.let {
                        LogUtils.i("GGGGG", "$start $end")
                        setSpan(it, start, end, flag)
                        return
                    }
                    quoteSpan?.let {
                        setSpan(it, start, end, 0)
                        return
                    }
                    bulletSpan?.let {
                        setSpan(it, start, end, 0)
                        return
                    }
                    fontSize?.let { size ->
                        setSpan(AbsoluteSizeSpan(size), start, end, flag)
                        return
                    }
                    clickSpan?.let {
                        setSpan(it, start, end, flag)
                        mTextView.movementMethod = LinkMovementMethod.getInstance()
                        return
                    }
                    url?.let {
                        LogUtils.i("GGGG", "Hello")
                        setSpan(URLSpan(it), start, end, flag)
                        mTextView.movementMethod = LinkMovementMethod.getInstance()
                        return
                    }
                    blur?.let {
                        setSpan(MaskFilterSpan(BlurMaskFilter(blurRadius, blur)), start, end, flag)
                        return
                    }
                }
            }
        }

        /**
         * Add image into string.
         *
         * @since 0.5.1
         */
        fun withImage(image: ImageSpan) {
            val (start, end) = foldScope {
                append(" ")
            }
            mBuilder.setSpan(image, start, end, flag)
        }

        /**
         * Attach to [mTextView].
         */
        internal fun finish() {
            mTextView.text = mBuilder
        }

        /**
         * Get the start and the end index of the string in [strScope].
         *
         * @since 0.5.1
         */
        private fun foldScope(strScope: SpannableStringBuilder.() -> Unit): Component2<Int, Int> {
            val start = mBuilder.length
            mBuilder.also(strScope)
            val end = mBuilder.length
            return Component2(start, end)
        }
    }

}