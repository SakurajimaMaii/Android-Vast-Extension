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

package com.ave.vastgui.tools.graphics

import android.graphics.Paint

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/10/9

/**
 * Return the text height.
 *
 * @since 0.5.5
 */
internal fun Paint.getTextHeight(): Float {
    val fontMetrics = this.fontMetrics
    return fontMetrics.bottom - fontMetrics.top
}

/**
 * Return the distance from the text middleline to the baseline.
 *
 * @since 0.5.5
 */
internal fun Paint.getBaseLine(): Float {
    val fontMetrics = this.fontMetrics
    val textHeight = fontMetrics.bottom - fontMetrics.top
    return textHeight / 2f - fontMetrics.bottom
}