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

package cn.govast.vasttools.utils.spannablestring

import android.graphics.Bitmap
import android.graphics.BlurMaskFilter
import android.graphics.BlurMaskFilter.Blur
import android.graphics.drawable.Drawable
import android.net.Uri
import android.text.Layout.Alignment
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.*
import androidx.annotation.ColorRes
import androidx.annotation.FloatRange
import cn.govast.vasttools.extension.NotNUllVar
import cn.govast.vasttools.helper.ContextHelper
import cn.govast.vasttools.utils.ResUtils

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/9/21 16:45
// Description: 
// Documentation:

object SpanStrUtils {

    /**
     * Get builder.
     *
     * @since 0.0.9
     */
    @JvmStatic
    fun getBuilder(text: CharSequence?): Builder {
        return Builder(null, text)
    }

    class Builder internal constructor(builder: SpannableStringBuilder?, text: CharSequence?) {
        private var flag by NotNUllVar<Int>()
        private var start by NotNUllVar<Int>()
        private var end by NotNUllVar<Int>()
        private var mBuilder by NotNUllVar<SpannableStringBuilder>()

        init {
            mBuilder = builder ?: SpannableStringBuilder()
            start = mBuilder.length
            mBuilder.append(text)
            end = mBuilder.length
        }

        /**
         * Set span type.
         *
         * @see Spanned.SPAN_INCLUSIVE_EXCLUSIVE
         * @see Spanned.SPAN_INCLUSIVE_INCLUSIVE
         * @see Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
         * @see Spanned.SPAN_EXCLUSIVE_INCLUSIVE
         * @since 0.0.9
         */
        fun setFlag(flag: SpanMode) = apply {
            this.flag = flag.value
        }

        /**
         * Set text size(in pixels).
         *
         * @since 0.0.9
         */
        fun setTextSize(textSize: Int) = apply {
            mBuilder.setSpan(AbsoluteSizeSpan(textSize), start, end, flag)
        }

        /**
         * Set foreground color.
         *
         * @since 0.0.9
         */
        fun setForegroundColor(@ColorRes foregroundColor: Int) = apply {
            mBuilder.setSpan(
                ForegroundColorSpan(ResUtils.getColor(foregroundColor)),
                start,
                end,
                flag
            )
        }

        /**
         * Set background color.
         *
         * @since 0.0.9
         */
        fun setBackgroundColor(@ColorRes backgroundColor: Int) = apply {
            mBuilder.setSpan(
                BackgroundColorSpan(ResUtils.getColor(backgroundColor)),
                start,
                end,
                flag
            )
        }

        /**
         * Set the color of the quote line.
         *
         * @since 0.0.9
         */
        fun setQuoteColor(@ColorRes quoteColor: Int) = apply {
            mBuilder.setSpan(QuoteSpan(ResUtils.getColor(quoteColor)), start, end, 0)
        }

        /**
         * Set indent.
         *
         * @param first First line indent.
         * @param rest Others lines indent.
         * @since 0.0.9
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
         * @since 0.0.9
         */
        fun setBullet(bulletGapWidth: Int, bulletColor: Int) = apply {
            mBuilder.setSpan(BulletSpan(bulletGapWidth, bulletColor), start, end, 0)
        }

        /**
         * Scale the size of the text.
         *
         * @param proportion the proportion with which the text is scaled.
         * @since 0.0.9
         */
        fun setProportion(proportion: Float) = apply {
            mBuilder.setSpan(RelativeSizeSpan(proportion), start, end, flag)
        }

        /**
         * Values > 1.0 will stretch the text wider. Values < 1.0 will stretch the
         * text narrower.
         *
         * @param xProportion the horizontal scale factor.
         * @since 0.0.9
         */
        fun setXProportion(xProportion: Float) = apply {
            mBuilder.setSpan(ScaleXSpan(xProportion), start, end, flag)
        }

        /**
         * Making a span that strikes through the text it's attached to.
         *
         * @since 0.0.9
         */
        fun setStrikethrough() = apply {
            mBuilder.setSpan(StrikethroughSpan(), start, end, flag)
        }

        /**
         * Making a span that underlines the text it's attached to.
         *
         * @since 0.0.9
         */
        fun setUnderline() = apply {
            mBuilder.setSpan(UnderlineSpan(), start, end, flag)
        }


        /**
         * Set scriptMode.
         *
         * @see ScriptMode
         * @since 0.0.9
         */
        fun setScriptMode(scriptMode: ScriptMode) = apply {
            when (scriptMode) {
                ScriptMode.Superscript -> {
                    mBuilder.setSpan(SuperscriptSpan(), start, end, flag)
                }
                ScriptMode.Subscript -> {
                    mBuilder.setSpan(SubscriptSpan(), start, end, flag)
                }
                else -> {}
            }
        }

        /**
         * Set font style
         *
         * @param style
         * @since 0.0.9
         */
        fun setFontStyle(style: StyleMode) = apply {
            mBuilder.setSpan(StyleSpan(style.value), start, end, flag)
        }

        /**
         * Set font family.
         *
         * @param fontFamily monospace, serif, sans-serif
         * @since 0.0.9
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
         * @since 0.0.9
         */
        fun setAlign(align: Alignment) = apply {
            mBuilder.setSpan(AlignmentSpan.Standard(align), start, end, flag)
        }

        /**
         * Set image.
         *
         * @since 0.0.9
         */
        fun setImage(image: Bitmap) = apply {
            mBuilder.setSpan(ImageSpan(ContextHelper.getAppContext(), image), start, end, flag)
        }

        /**
         * Set image.
         *
         * @since 0.0.9
         */
        fun setImage(image: Drawable) = apply {
            mBuilder.setSpan(ImageSpan(image), start, end, flag)
        }

        /**
         * Set image.
         *
         * @since 0.0.9
         */
        fun setImage(image: Uri) = apply {
            mBuilder.setSpan(ImageSpan(ContextHelper.getAppContext(), image), start, end, flag)
        }

        /**
         * Set image.
         *
         * @since 0.0.9
         */
        fun setImage(image: Int) = apply {
            mBuilder.setSpan(ImageSpan(ContextHelper.getAppContext(), image), start, end, flag)
        }


        /**
         * Register a click listener. Need to add
         * view.setMovementMethod(LinkMovementMethod.getInstance())
         *
         * @since 0.0.9
         */
        fun setClickSpan(clickSpan: ClickableSpan?) = apply {
            mBuilder.setSpan(clickSpan, start, end, flag)
        }

        /**
         * Making the span from a url string. Need to add
         * view.setMovementMethod(LinkMovementMethod.getInstance())
         *
         * @since 0.0.9
         */
        fun setUrl(url: String?) = apply {
            mBuilder.setSpan(URLSpan(url), start, end, flag)
        }

        /**
         * Setting blur.
         *
         * @param radius Blur radius (must be greater than 0).
         * @param blur [Blur.NORMAL] [Blur.SOLID] [Blur.OUTER] [Blur.INNER].
         * @since 0.0.9
         */
        fun setBlur(@FloatRange(from = 0.0) radius: Float, blur: Blur?) = apply {
            mBuilder.setSpan(MaskFilterSpan(BlurMaskFilter(radius, blur)), start, end, flag)
        }

        /**
         * Append style string.
         *
         * @since 0.0.9
         */
        fun append(text: CharSequence?): Builder {
            return Builder(mBuilder, text)
        }

        /**
         * Create a style string.
         *
         * @since 0.0.9
         */
        fun build(): SpannableStringBuilder {
            return mBuilder
        }

    }

}