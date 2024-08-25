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

package com.ave.vastgui.app.activity.graphics

import android.content.res.ColorStateList
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.GradientDrawable.RECTANGLE
import android.os.Bundle
import com.ave.vastgui.app.databinding.ActivityGradientDrawableBinding
import com.ave.vastgui.tools.activity.VastVbActivity
import com.ave.vastgui.tools.graphics.setGradient
import com.ave.vastgui.tools.utils.ColorUtils.colorHex2Int

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Documentation: https://ave.entropy2020.cn/documents/tools/core-topics/graphics/gradient-drawable/gradient-drawable/

private val states = arrayOfNulls<IntArray>(6).apply {
    set(0, intArrayOf(android.R.attr.state_pressed, android.R.attr.state_enabled))
    set(1, intArrayOf(android.R.attr.state_focused, android.R.attr.state_enabled))
    set(2, intArrayOf(-android.R.attr.state_focused, android.R.attr.state_enabled))
    set(3, intArrayOf(android.R.attr.state_focused))
    set(4, intArrayOf(android.R.attr.state_window_focused))
    set(5, intArrayOf())
}

private val colorList = IntArray(6).apply {
    set(0, colorHex2Int("#00F260"))
    set(1, colorHex2Int("#FFFFFF"))
    set(2, colorHex2Int("#0575E6"))
    set(3, colorHex2Int("#FFFFFF"))
    set(4, colorHex2Int("#EF3B36"))
    set(5, colorHex2Int("#0575E6"))
}

class GradientDrawableActivity : VastVbActivity<ActivityGradientDrawableBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getBinding().image.setImageDrawable(GradientDrawable().apply {
            shape = RECTANGLE
            cornerRadius = 50f
            setGradient(
                45,
                colorHex2Int("#0F2027"),
                colorHex2Int("#78ffd6")
            )
        })

        getBinding().btn.background = GradientDrawable()
        (getBinding().btn.background.mutate() as GradientDrawable).apply {
            shape = RECTANGLE
            cornerRadius = 30f
            color = ColorStateList(states, colorList)
            setSize(50, 50)
        }
    }

}