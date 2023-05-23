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

import android.graphics.Bitmap
import android.graphics.BlurMaskFilter
import android.graphics.BlurMaskFilter.Blur
import android.graphics.drawable.Drawable
import android.net.Uri
import android.text.Layout.Alignment
import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.text.style.AbsoluteSizeSpan
import android.text.style.AlignmentSpan
import android.text.style.BackgroundColorSpan
import android.text.style.BulletSpan
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.ImageSpan
import android.text.style.LeadingMarginSpan
import android.text.style.MaskFilterSpan
import android.text.style.QuoteSpan
import android.text.style.RelativeSizeSpan
import android.text.style.ScaleXSpan
import android.text.style.StrikethroughSpan
import android.text.style.StyleSpan
import android.text.style.SubscriptSpan
import android.text.style.SuperscriptSpan
import android.text.style.TypefaceSpan
import android.text.style.URLSpan
import android.text.style.UnderlineSpan
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.FloatRange
import com.ave.vastgui.core.extension.NotNUllVar
import com.ave.vastgui.tools.helper.ContextHelper
import com.ave.vastgui.tools.utils.ResUtils

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/9/21 16:45
// Documentation: https://ave.entropy2020.cn/documents/VastTools/core-topics/app-resources/string/SpanUtils/

object SpanStrUtils {

    /**
     * Get builder.
     *
     * When you using this method and append a [URLSpan] in the string, you
     * should call [TextView.setMovementMethod] as last to make sure the
     * [URLSpan] can be clicked.
     */
    @JvmStatic
    fun getBuilder(text: CharSequence?): Builder {
        return Builder(null, text)
    }

    /**
     * Get builder.
     *
     * When you using this method, please call
     * [SpanStrUtils.Builder.attachToTextView] at last.
     *
     * @param view The TextView that the string will be attached to.
     * @since 0.2.0
     */
    @JvmStatic
    fun getBuilder(view: TextView, text: CharSequence?): Builder {
        return Builder(view, null, text)
    }

    class Builder {

        internal constructor(builder: SpannableStringBuilder?, text: CharSequence?) {
            mBuilder = builder ?: SpannableStringBuilder()
            start = mBuilder.length
            mBuilder.append(text)
            end = mBuilder.length
        }

        /**
         * Creates a [SpanStrUtils.Builder] instance.
         *
         * @param view The TextView that the string will be attached to.
         * @since 0.2.0
         */
        internal constructor(
            view: TextView,
            builder: SpannableStringBuilder?,
            text: CharSequence?
        ) {
            mBuilder = builder ?: SpannableStringBuilder()
            start = mBuilder.length
            mBuilder.append(text)
            end = mBuilder.length
            mTextView = view
        }

        private val flag = SpanMode.SEE.value
        private var start by NotNUllVar<Int>()
        private var end by NotNUllVar<Int>()
        private var mBuilder by NotNUllVar<SpannableStringBuilder>()

        /**
         * The TextView that the string will be attached to.
         *
         * @since 0.2.0
         */
        private var mTextView: TextView? = null

        /** Set text size(in pixels). */
        fun setTextSize(textSize: Int) = apply {
            mBuilder.setSpan(AbsoluteSizeSpan(textSize), start, end, flag)
        }

        /** Set foreground color. */
        fun setForegroundColor(@ColorRes foregroundColor: Int) = apply {
            mBuilder.setSpan(
                ForegroundColorSpan(ResUtils.getColor(foregroundColor)),
                start,
                end,
                flag
            )
        }

        /** Set background color. */
        fun setBackgroundColor(@ColorRes backgroundColor: Int) = apply {
            mBuilder.setSpan(
                BackgroundColorSpan(ResUtils.getColor(backgroundColor)),
                start,
                end,
                flag
            )
        }

        /** Set the color of the quote line. */
        fun setQuoteColor(@ColorRes quoteColor: Int) = apply {
            mBuilder.setSpan(QuoteSpan(ResUtils.getColor(quoteColor)), start, end, 0)
        }

        /**
         * Set indent.
         *
         * @param first First line indent.
         * @param rest Others lines indent.
         */
        fun setLinesIndent(first: Int, rest: Int) = apply {
            mBuilder.setSpan(LeadingMarginSpan.Standard(first, rest), start, end, flag)
        }

        /**
         * Set the span which styles paragraphs as bullet points
         *
         * @param bulletGapWidth the distance, in pixels, between the bullet point
         *     and the paragraph.
         * @param bulletColor the bullet point color, as a color integer
         */
        fun setBullet(bulletGapWidth: Int, bulletColor: Int) = apply {
            mBuilder.setSpan(BulletSpan(bulletGapWidth, bulletColor), start, end, 0)
        }

        /**
         * Scale the size of the text.
         *
         * @param proportion the proportion with which the text is scaled.
         */
        fun setProportion(proportion: Float) = apply {
            mBuilder.setSpan(RelativeSizeSpan(proportion), start, end, flag)
        }

        /**
         * Values > 1.0 will stretch the text wider. Values < 1.0 will stretch the
         * text narrower.
         *
         * @param xProportion the horizontal scale factor.
         */
        fun setXProportion(xProportion: Float) = apply {
            mBuilder.setSpan(ScaleXSpan(xProportion), start, end, flag)
        }

        /** Making a span that strikes through the text it's attached to. */
        fun setStrikethrough() = apply {
            mBuilder.setSpan(StrikethroughSpan(), start, end, flag)
        }

        /** Making a span that underlines the text it's attached to. */
        fun setUnderline() = apply {
            mBuilder.setSpan(UnderlineSpan(), start, end, flag)
        }

        /**
         * Set scriptMode.
         *
         * @see ScriptMode
         */
        fun setScriptMode(scriptMode: ScriptMode) = apply {
            when (scriptMode) {
                ScriptMode.SUPERSCRIPT -> {
                    mBuilder.setSpan(SuperscriptSpan(), start, end, flag)
                }

                ScriptMode.SUBSCRIPT -> {
                    mBuilder.setSpan(SubscriptSpan(), start, end, flag)
                }

                ScriptMode.NONE -> {
                    return this@Builder
                }
            }
        }

        /**
         * Set font style
         *
         * @param style
         */
        fun setFontStyle(style: StyleMode) = apply {
            mBuilder.setSpan(StyleSpan(style.value), start, end, flag)
        }

        /**
         * Set font family.
         *
         * @param fontFamily monospace, serif, sans-serif
         */
        fun setFontFamily(fontFamily: String?) = apply {
            mBuilder.setSpan(TypefaceSpan(fontFamily), start, end, flag)
        }

        /**
         * Set Alignment.
         *
         * @see Alignment.ALIGN_NORMAL
         * @see Alignment.ALIGN_OPPOSITE
         * @see Alignment.ALIGN_CENTER
         */
        fun setAlign(align: Alignment) = apply {
            mBuilder.setSpan(AlignmentSpan.Standard(align), start, end, flag)
        }

        /** Set image. */
        fun setImage(image: Bitmap) = apply {
            mBuilder.setSpan(ImageSpan(ContextHelper.getAppContext(), image), start, end, flag)
        }

        /** Set image. */
        fun setImage(image: Drawable) = apply {
            mBuilder.setSpan(ImageSpan(image), start, end, flag)
        }

        /** Set image. */
        fun setImage(image: Uri) = apply {
            mBuilder.setSpan(ImageSpan(ContextHelper.getAppContext(), image), start, end, flag)
        }

        /** Set image. */
        fun setImage(image: Int) = apply {
            mBuilder.setSpan(ImageSpan(ContextHelper.getAppContext(), image), start, end, flag)
        }


        /**
         * Register a click listener. Need to call [TextView.setMovementMethod] if
         * [mTextView] is null.
         */
        fun setClickSpan(clickSpan: ClickableSpan?) = apply {
            mBuilder.setSpan(clickSpan, start, end, flag)
            mTextView?.movementMethod = LinkMovementMethod.getInstance()
        }

        /**
         * Making the span from a url string.Need to call
         * [TextView.setMovementMethod] if [mTextView] is null.
         */
        fun setUrl(url: String?) = apply {
            mBuilder.setSpan(URLSpan(url), start, end, flag)
            mTextView?.movementMethod = LinkMovementMethod.getInstance()
        }

        /**
         * Setting blur.
         *
         * @param radius Blur radius (must be greater than 0).
         * @param blur [Blur.NORMAL] [Blur.SOLID] [Blur.OUTER] [Blur.INNER].
         */
        fun setBlur(@FloatRange(from = 0.0) radius: Float, blur: Blur?) = apply {
            mBuilder.setSpan(MaskFilterSpan(BlurMaskFilter(radius, blur)), start, end, flag)
        }

        /** Append style string. */
        fun append(text: CharSequence?): Builder {
            return mTextView?.let {
                Builder(it, mBuilder, text)
            } ?: Builder(mBuilder, text)
        }

        /** Create a style string. */
        fun build(): SpannableStringBuilder {
            return mBuilder
        }

        /**
         * Attach to [mTextView].
         *
         * @since 0.2.0
         */
        @Throws(NullPointerException::class)
        fun attachToTextView() {
            mTextView?.let {
                it.text = mBuilder
            } ?: throw NullPointerException("Please init mTextView.")
        }

    }

}