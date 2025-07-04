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

package com.ave.vastgui.app.fragment.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.ave.vastgui.app.R
import com.ave.vastgui.app.databinding.FragmentBadgeViewBinding
import com.ave.vastgui.tools.utils.DensityUtils.DP
import com.ave.vastgui.tools.utils.DensityUtils.SP
import com.ave.vastgui.tools.utils.color
import com.ave.vastgui.tools.utils.string
import com.ave.vastgui.tools.view.toast.SimpleToast
import com.ave.vastgui.tools.viewbinding.viewBinding
import kotlin.random.Random

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2025/6/4

class BadgeViewFragment : Fragment(R.layout.fragment_badge_view) {

    private val binding by viewBinding(FragmentBadgeViewBinding::bind)

    private val badgeColors by lazy {
        intArrayOf(requireContext().color(R.color.orangered),
            requireContext().color(R.color.lightgreen),
            requireContext().color(R.color.deepskyblue))
    }

    private val textColors by lazy {
        intArrayOf(requireContext().color(R.color.honeydew),
            requireContext().color(R.color.lightslategray),
            requireContext().color(R.color.lime))
    }

    private val texts by lazy {
        arrayOf(requireContext().string(R.string.badge_text_message),
            requireContext().string(R.string.badge_text_receive))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.dotBadgeChangeColorBtn.setOnClickListener {
            binding.dotBadge.setColor(badgeColors[Random.Default.nextInt(0, badgeColors.size)])
        }

        binding.dotBadgeRadiusSlider.addOnChangeListener { _, value, _ ->
            binding.dotBadge.setDotRadius(value.DP)
        }

        binding.showDotBadgeBtn.setOnClickListener {
            binding.dotBadge.showDot()
        }

        binding.hideDotBadgeBtn.setOnClickListener {
            binding.dotBadge.hideDot()
        }

        binding.numberBadgeAddBtn.setOnClickListener {
            binding.numberBadge.setBubbleTextNum(binding.numberBadge.textNumber + 1)
        }

        binding.numberBadgeSetMaxBtn.text =
            String.format(getString(R.string.badge_num_set_max), binding.numberBadge.textMaxNumber)
        binding.numberBadgeSetMaxBtn.setOnClickListener {
            binding.numberBadge.setBubbleTextNum(0)
            binding.numberBadge.setBubbleTextMaxNum(Random.Default.nextInt(0, 21))
            binding.numberBadgeSetMaxBtn.text =
                String.format(getString(R.string.badge_num_set_max), binding.numberBadge.textMaxNumber)
        }

        binding.numberBadgeResetBtn.setOnClickListener {
            binding.numberBadge.setBubbleTextNum(0)
        }

        binding.numberBadgeChangeColorBtn.setOnClickListener {
            binding.numberBadge.setBubbleTextColor(textColors[Random.Default.nextInt(0, badgeColors.size)])
        }

        // 设置半径
        binding.numberBadgeRadiusSlider.setLabelFormatter { String.format(getString(R.string.badge_bubble_radius), it) }
        binding.numberBadgeRadiusSlider.addOnChangeListener { _, value, _ ->
            binding.numberBadge.setBubbleRadius(value.DP)
        }

        // 设置字体大小
        binding.numberBadgeTextSizeSlider.setLabelFormatter { String.format(getString(R.string.badge_bubble_text_size), it) }
        binding.numberBadgeTextSizeSlider.addOnChangeListener { _, value, _ ->
            binding.numberBadge.setBubbleTextSize(value.SP)
        }

        binding.numberBadge.setOnClickListener {
            SimpleToast.showShortMsg(binding.numberBadge.getBubbleTextNumber())
        }

        binding.textBadge.setOnClickListener {
            SimpleToast.showShortMsg(R.string.badge_click)
        }

        binding.textBadge.setOnLongClickListener {
            SimpleToast.showShortMsg(R.string.badge_long_click)
            true
        }

        // 切换徽标文本
        binding.textBadgeChangeTextBtn.setOnClickListener {
            binding.textBadge.setBubbleText(texts[Random.Default.nextInt(0, texts.size)])
        }

        // 清空徽标文本
        binding.textBadgeClearBtn.setOnClickListener {
            binding.textBadge.setBubbleText("")
        }
    }

}