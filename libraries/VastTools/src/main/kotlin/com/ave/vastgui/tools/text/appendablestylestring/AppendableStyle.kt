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
import android.text.Layout.Alignment
import android.text.style.BulletSpan
import android.text.style.ClickableSpan
import android.text.style.LeadingMarginSpan
import android.text.style.QuoteSpan
import android.text.style.TypefaceSpan
import androidx.annotation.ColorInt
import com.ave.vastgui.tools.R
import com.ave.vastgui.tools.utils.findByContext

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/5/22
// Documentation: https://ave.entropy2020.cn/documents/VastTools/core-topics/text/appendable-style-string/appendable-style-string/

/**
 * The append style.
 *
 * @property foreColor Color that defines the text color.
 * @property backColor Color that defines the background color.
 * @property fontStyle An enum constant describing the style for this text. Refer to [StyleMode].
 * @property fontFamily Refer to [TypefaceSpan].
 * @property fontSize Set the text size to [fontSize] in pixels.
 * @property fontAlign Refer to [Alignment.ALIGN_NORMAL], [Alignment.ALIGN_OPPOSITE], [Alignment.ALIGN_CENTER].
 * @property proportion The proportion with which the text is scaled.
 * @property xProportion Values > 1.0 will stretch the text wider. Values < 1.0 will stretch the text narrower.
 * @property strikeMode Add a strikethrough or underline for the text.
 * @property scriptMode Defines the text as superscript or subscript.
 * @property linesIndent Defines the text indentation.
 * @property quoteSpan Defines the text quote.
 * @property bulletSpan Defines the text bullet point.
 * @property clickSpan Defines the text click event.
 * @property url Defines the text as url.
 * @property blurRadius The radius to extend the blur from the original mask. Must be > 0.
 * @property blur The Blur to use.
 * @since 0.5.1
 */
class AppendableStyle(
    @ColorInt val foreColor: Int? = null,
    @ColorInt val backColor: Int = findByContext { getColor(R.color.transparent) },
    val fontStyle: StyleMode = StyleMode.NORMAL,
    val fontFamily: TypefaceSpan? = null,
    val fontSize: Int? = null,
    val fontAlign: Alignment? = null,
    val proportion: Float = 1.0f,
    val xProportion: Float = 1.0f
) {
    var strikeMode: StrikeMode = StrikeMode.NONE
    var scriptMode: ScriptMode = ScriptMode.NONE
    var linesIndent: LeadingMarginSpan.Standard? = null
    var quoteSpan: QuoteSpan? = null
    var bulletSpan: BulletSpan? = null
    var clickSpan: ClickableSpan? = null
    var url: String? = null
    var blurRadius: Float = 0.0f
    var blur: BlurMaskFilter.Blur? = null

    constructor(
        linesIndent: LeadingMarginSpan.Standard,
        @ColorInt foreColor: Int? = null,
        @ColorInt backColor: Int = findByContext { getColor(R.color.transparent) },
        fontStyle: StyleMode = StyleMode.NORMAL,
        fontFamily: TypefaceSpan? = null,
        fontSize: Int? = null,
        fontAlign: Alignment? = null,
        proportion: Float = 1.0f,
        xProportion: Float = 1.0f
    ) : this(
        foreColor = foreColor,
        backColor = backColor,
        fontStyle = fontStyle,
        fontFamily = fontFamily,
        fontSize = fontSize,
        fontAlign = fontAlign,
        proportion = proportion,
        xProportion = xProportion
    ) {
        this.linesIndent = linesIndent
    }

    constructor(
        quoteSpan: QuoteSpan,
        @ColorInt foreColor: Int? = null,
        @ColorInt backColor: Int = findByContext { getColor(R.color.transparent) },
        fontStyle: StyleMode = StyleMode.NORMAL,
        fontFamily: TypefaceSpan? = null,
        fontSize: Int? = null,
        fontAlign: Alignment? = null,
        proportion: Float = 1.0f,
        xProportion: Float = 1.0f
    ) : this(
        foreColor = foreColor,
        backColor = backColor,
        fontStyle = fontStyle,
        fontFamily = fontFamily,
        fontSize = fontSize,
        fontAlign = fontAlign,
        proportion = proportion,
        xProportion = xProportion
    ) {
        this.quoteSpan = quoteSpan
    }

    constructor(
        bulletSpan: BulletSpan,
        @ColorInt foreColor: Int? = null,
        @ColorInt backColor: Int = findByContext { getColor(R.color.transparent) },
        fontStyle: StyleMode = StyleMode.NORMAL,
        fontFamily: TypefaceSpan? = null,
        fontSize: Int? = null,
        fontAlign: Alignment? = null,
        proportion: Float = 1.0f,
        xProportion: Float = 1.0f
    ) : this(
        foreColor = foreColor,
        backColor = backColor,
        fontStyle = fontStyle,
        fontFamily = fontFamily,
        fontSize = fontSize,
        fontAlign = fontAlign,
        proportion = proportion,
        xProportion = xProportion
    ) {
        this.bulletSpan = bulletSpan
    }

    constructor(
        strikeMode: StrikeMode = StrikeMode.NONE,
        @ColorInt foreColor: Int? = null,
        @ColorInt backColor: Int = findByContext { getColor(R.color.transparent) },
        fontStyle: StyleMode = StyleMode.NORMAL,
        fontFamily: TypefaceSpan? = null,
        fontSize: Int? = null,
        fontAlign: Alignment? = null,
        proportion: Float = 1.0f,
        xProportion: Float = 1.0f
    ) : this(
        foreColor = foreColor,
        backColor = backColor,
        fontStyle = fontStyle,
        fontFamily = fontFamily,
        fontSize = fontSize,
        fontAlign = fontAlign,
        proportion = proportion,
        xProportion = xProportion
    ) {
        this.strikeMode = strikeMode
    }

    constructor(
        scriptMode: ScriptMode = ScriptMode.NONE,
        @ColorInt foreColor: Int? = null,
        @ColorInt backColor: Int = findByContext { getColor(R.color.transparent) },
        fontStyle: StyleMode = StyleMode.NORMAL,
        fontFamily: TypefaceSpan? = null,
        fontSize: Int? = null,
        fontAlign: Alignment? = null,
        proportion: Float = 1.0f,
        xProportion: Float = 1.0f
    ) : this(
        foreColor = foreColor,
        backColor = backColor,
        fontStyle = fontStyle,
        fontFamily = fontFamily,
        fontSize = fontSize,
        fontAlign = fontAlign,
        proportion = proportion,
        xProportion = xProportion
    ) {
        this.scriptMode = scriptMode
    }

    constructor(
        clickSpan: ClickableSpan,
        @ColorInt foreColor: Int? = null,
        @ColorInt backColor: Int = findByContext { getColor(R.color.transparent) },
        fontStyle: StyleMode = StyleMode.NORMAL,
        fontFamily: TypefaceSpan? = null,
        fontSize: Int? = null,
        fontAlign: Alignment? = null,
        proportion: Float = 1.0f,
        xProportion: Float = 1.0f
    ) : this(
        foreColor = foreColor,
        backColor = backColor,
        fontStyle = fontStyle,
        fontFamily = fontFamily,
        fontSize = fontSize,
        fontAlign = fontAlign,
        proportion = proportion,
        xProportion = xProportion
    ) {
        this.clickSpan = clickSpan
    }

    constructor(
        url: String,
        @ColorInt foreColor: Int? = null,
        @ColorInt backColor: Int = findByContext { getColor(R.color.transparent) },
        fontStyle: StyleMode = StyleMode.NORMAL,
        fontFamily: TypefaceSpan? = null,
        fontSize: Int? = null,
        fontAlign: Alignment? = null,
        proportion: Float = 1.0f,
        xProportion: Float = 1.0f
    ) : this(
        foreColor = foreColor,
        backColor = backColor,
        fontStyle = fontStyle,
        fontFamily = fontFamily,
        fontSize = fontSize,
        fontAlign = fontAlign,
        proportion = proportion,
        xProportion = xProportion
    ) {
        this.url = url
    }

    constructor(
        blurRadius: Float,
        blur: BlurMaskFilter.Blur,
        @ColorInt foreColor: Int? = null,
        @ColorInt backColor: Int = findByContext { getColor(R.color.transparent) },
        fontStyle: StyleMode = StyleMode.NORMAL,
        fontFamily: TypefaceSpan? = null,
        fontSize: Int? = null,
        fontAlign: Alignment? = null,
        proportion: Float = 1.0f,
        xProportion: Float = 1.0f
    ) : this(
        foreColor = foreColor,
        backColor = backColor,
        fontStyle = fontStyle,
        fontFamily = fontFamily,
        fontSize = fontSize,
        fontAlign = fontAlign,
        proportion = proportion,
        xProportion = xProportion
    ) {
        this.blurRadius = blurRadius
        this.blur = blur
    }
}