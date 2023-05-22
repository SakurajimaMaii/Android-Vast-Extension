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
import android.text.Layout
import android.text.style.BulletSpan
import android.text.style.ClickableSpan
import android.text.style.LeadingMarginSpan
import android.text.style.QuoteSpan
import android.text.style.TypefaceSpan
import androidx.annotation.ColorRes
import com.ave.vastgui.tools.R

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/5/22
// Description: 
// Documentation:
// Reference:

/**
 * The append style.
 *
 * @since 0.5.1
 */
class AppendableStyle(
    @ColorRes internal val foreColor: Int = R.color.black,
    @ColorRes internal val backColor: Int = R.color.transparent,
    internal val fontStyle: StyleMode = StyleMode.NORMAL,
    internal val fontFamily: TypefaceSpan? = null,
    internal val fontSize: Int? = null,
    internal val fontAlign: Layout.Alignment? = null,
    internal val proportion:Float = 1.0f,
    internal val xProportion:Float = 1.0f
) {
    internal var strikeMode: StrikeMode = StrikeMode.NONE
    internal var scriptMode: ScriptMode = ScriptMode.None
    internal var linesIndent: LeadingMarginSpan.Standard? = null
    internal var quoteSpan: QuoteSpan? = null
    internal var bulletSpan: BulletSpan? = null
    internal var clickSpan: ClickableSpan? = null
    internal var url: String? = null
    internal var blurRadius: Float = 0.0f
    internal var blur: BlurMaskFilter.Blur? = null

    constructor(
        linesIndent: LeadingMarginSpan.Standard,
        @ColorRes foreColor: Int = R.color.black,
        @ColorRes backColor: Int = R.color.transparent,
        fontStyle: StyleMode = StyleMode.NORMAL,
        fontFamily: TypefaceSpan? = null,
        fontSize: Int? = null,
        fontAlign: Layout.Alignment? = null,
        proportion:Float = 1.0f,
        xProportion:Float = 1.0f
    ) : this(
        foreColor = foreColor,
        backColor = backColor,
        fontStyle = fontStyle,
        fontFamily = fontFamily,
        fontSize = fontSize,
        fontAlign = fontAlign,
        proportion = proportion,
        xProportion = xProportion
    ){
        this.linesIndent = linesIndent
    }

    constructor(
        quoteSpan: QuoteSpan,
        @ColorRes foreColor: Int = R.color.black,
        @ColorRes backColor: Int = R.color.transparent,
        fontStyle: StyleMode = StyleMode.NORMAL,
        fontFamily: TypefaceSpan? = null,
        fontSize: Int? = null,
        fontAlign: Layout.Alignment? = null,
        proportion:Float = 1.0f,
        xProportion:Float = 1.0f
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
        @ColorRes foreColor: Int = R.color.black,
        @ColorRes backColor: Int = R.color.transparent,
        fontStyle: StyleMode = StyleMode.NORMAL,
        fontFamily: TypefaceSpan? = null,
        fontSize: Int? = null,
        fontAlign: Layout.Alignment? = null,
        proportion:Float = 1.0f,
        xProportion:Float = 1.0f
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
        @ColorRes foreColor: Int = R.color.black,
        @ColorRes backColor: Int = R.color.transparent,
        fontStyle: StyleMode = StyleMode.NORMAL,
        fontFamily: TypefaceSpan? = null,
        fontSize: Int? = null,
        fontAlign: Layout.Alignment? = null,
        proportion:Float = 1.0f,
        xProportion:Float = 1.0f
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
        scriptMode: ScriptMode = ScriptMode.None,
        @ColorRes foreColor: Int = R.color.black,
        @ColorRes backColor: Int = R.color.transparent,
        fontStyle: StyleMode = StyleMode.NORMAL,
        fontFamily: TypefaceSpan? = null,
        fontSize: Int? = null,
        fontAlign: Layout.Alignment? = null,
        proportion:Float = 1.0f,
        xProportion:Float = 1.0f
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
        @ColorRes foreColor: Int = R.color.black,
        @ColorRes backColor: Int = R.color.transparent,
        fontStyle: StyleMode = StyleMode.NORMAL,
        fontFamily: TypefaceSpan? = null,
        fontSize: Int? = null,
        fontAlign: Layout.Alignment? = null,
        proportion:Float = 1.0f,
        xProportion:Float = 1.0f
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
        @ColorRes foreColor: Int = R.color.black,
        @ColorRes backColor: Int = R.color.transparent,
        fontStyle: StyleMode = StyleMode.NORMAL,
        fontFamily: TypefaceSpan? = null,
        fontSize: Int? = null,
        fontAlign: Layout.Alignment? = null,
        proportion:Float = 1.0f,
        xProportion:Float = 1.0f
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
        @ColorRes foreColor: Int = R.color.black,
        @ColorRes backColor: Int = R.color.transparent,
        fontStyle: StyleMode = StyleMode.NORMAL,
        fontFamily: TypefaceSpan? = null,
        fontSize: Int? = null,
        fontAlign: Layout.Alignment? = null,
        proportion:Float = 1.0f,
        xProportion:Float = 1.0f
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