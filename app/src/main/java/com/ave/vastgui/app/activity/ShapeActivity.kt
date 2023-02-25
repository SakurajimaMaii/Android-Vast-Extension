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

package com.ave.vastgui.app.activity

import android.graphics.drawable.GradientDrawable.RECTANGLE
import android.os.Bundle
import com.ave.vastgui.app.databinding.ActivityShapeBinding
import com.ave.vastgui.tools.activity.VastVbActivity
import com.ave.vastgui.tools.utils.ColorUtils
import com.ave.vastgui.tools.utils.DensityUtils.SP
import com.ave.vastgui.tools.utils.drawable.ShapeAndStateDrawable
import com.ave.vastgui.tools.utils.spannablestring.ScriptMode
import com.ave.vastgui.tools.utils.spannablestring.SpanMode
import com.ave.vastgui.tools.utils.spannablestring.SpanStrUtils

class ShapeActivity : VastVbActivity<ActivityShapeBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val states = arrayOfNulls<IntArray>(6).apply {
            set(0, intArrayOf(android.R.attr.state_pressed, android.R.attr.state_enabled))
            set(1, intArrayOf(android.R.attr.state_focused, android.R.attr.state_enabled))
            set(2, intArrayOf(-android.R.attr.state_focused, android.R.attr.state_enabled))
            set(3, intArrayOf(android.R.attr.state_focused))
            set(4, intArrayOf(android.R.attr.state_window_focused))
            set(5, intArrayOf())
        }

        val colorList = IntArray(6).apply {
            set(0, ColorUtils.colorHex2Int("#00F260"))
            set(1, ColorUtils.colorHex2Int("#FFFFFF"))
            set(2, ColorUtils.colorHex2Int("#0575E6"))
            set(3, ColorUtils.colorHex2Int("#FFFFFF"))
            set(4, ColorUtils.colorHex2Int("#EF3B36"))
            set(5, ColorUtils.colorHex2Int("#0575E6"))
        }

        val btnBK = ShapeAndStateDrawable.create()
            .setShape(RECTANGLE)
            .setRadius(50f)
            .setGradient(45, ColorUtils.colorHex2Int("#0F2027"),ColorUtils.colorHex2Int("#78ffd6"))
            .setBgColorStateList(states,colorList)
            .build()

        getBinding().btn1.background = btnBK

        getBinding().tv1.text = SpanStrUtils
            .getBuilder("Hello")
            .setFlag(SpanMode.SEE)
            .setTextSize(15F.SP.toInt())
            .append("你好")
            .setFlag(SpanMode.SEE)
            .setScriptMode(ScriptMode.Superscript)
            .build()
    }

}