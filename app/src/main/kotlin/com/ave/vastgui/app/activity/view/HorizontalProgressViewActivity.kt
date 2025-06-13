/*
 * Copyright 2021-2025 VastGui
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

package com.ave.vastgui.app.activity.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.core.content.ContextCompat
import com.ave.vastgui.app.R
import com.ave.vastgui.app.databinding.ActivityHorizontalProgressViewBinding
import com.ave.vastgui.app.log.logFactory
import com.ave.vastgui.tools.utils.ColorUtils
import com.ave.vastgui.tools.utils.DensityUtils
import com.ave.vastgui.tools.utils.DensityUtils.DP
import com.ave.vastgui.tools.viewbinding.viewBinding

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Documentation: https://sakurajimamaii.github.io/AVE-DOC/documents/tools/core-topics/ui/progress/horizontal-progress-view/

class HorizontalProgressViewActivity : ComponentActivity() {

    private val logcat = logFactory(HorizontalProgressViewActivity::class.java)

    private val binding by viewBinding(ActivityHorizontalProgressViewBinding::inflate)

    private val gradientImages = arrayOf(R.drawable.img_horizontal_pv_background,
        R.drawable.img_horizontal_pv_foreground,
        R.drawable.ic_cat_256dp, 0)

    private var backgroundIndex = 0

    private var progressIndex = 0

    private val colors = intArrayOf(
        ColorUtils.colorHex2Int("#55efc4"),
        ColorUtils.colorHex2Int("#81ecec"),
        ColorUtils.colorHex2Int("#6c5ce7"),
        ColorUtils.colorHex2Int("#0984e3"),
        ColorUtils.colorHex2Int("#fdcb6e"),
        ColorUtils.colorHex2Int("#e17055"),
        ColorUtils.colorHex2Int("#636e72")
    )

    private var progressColorIndex = 0

    private var progressBackgroundColorIndex = 0

    private var boxColorIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.progressSlider.addOnChangeListener { _, value, _ ->
            binding.horizontalPv.currentProgress = value
            binding.horizontalTextPv.currentProgress = value
            binding.lineTextPv.currentProgress = value
        }

        binding.horizontalPvSwitchBackgroundBtn.setOnClickListener {
            // val index = gradientIndex
            // binding.horizontalPv.setProgressDrawable(gradientImages[(index) % gradientImages.size])
            // gradientIndex++

            val image = runCatching { ContextCompat.getDrawable(this, gradientImages[(backgroundIndex++) % gradientImages.size]) }.getOrNull()
            binding.horizontalPv.setProgressBkDrawable(image)
        }

        binding.horizontalPvSwitchProgressBtn.setOnClickListener {
            val image = runCatching { ContextCompat.getDrawable(this, gradientImages[(progressIndex++) % gradientImages.size]) }.getOrNull()
            binding.horizontalPv.setProgressDrawable(image)
        }

        binding.horizontalPvSwitchProgressColorBtn.setOnClickListener {
            binding.horizontalPv.progressColor = colors[(progressColorIndex++) % colors.size]
        }

        binding.horizontalPvSwitchBackgroundColorBtn.setOnClickListener {
            binding.horizontalPv.progressBackgroundColor = colors[(progressBackgroundColorIndex++) % colors.size]
        }

        binding.horizontalPvSwitchTextBoxColorBtn.setOnClickListener {
            val color = colors[(boxColorIndex++) % colors.size]
            binding.horizontalTextPv.textBoxColor = color
            binding.lineTextPv.textBoxColor = color
        }

        binding.textMarginSlider.value = DensityUtils.px2dp(binding.horizontalTextPv.textMargin)
        binding.textMarginSlider.addOnChangeListener { _, value, _ ->
            binding.horizontalTextPv.textMargin = value.DP
            binding.lineTextPv.textMargin = value.DP
        }

        binding.progressHeightSlider.value = DensityUtils.px2dp(binding.horizontalTextPv.progressHeight)
        binding.progressHeightSlider.addOnChangeListener { _, value, _ ->
            binding.horizontalTextPv.progressHeight = value.DP
            binding.lineTextPv.progressHeight = value.DP
        }
    }

}