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
import androidx.core.view.children
import androidx.fragment.app.Fragment
import com.ave.vastgui.app.R
import com.ave.vastgui.app.databinding.FragmentBadgeLayoutBinding
import com.ave.vastgui.app.log.logFactory
import com.ave.vastgui.tools.utils.DensityUtils.DP
import com.ave.vastgui.tools.utils.DensityUtils.SP
import com.ave.vastgui.tools.utils.color
import com.ave.vastgui.tools.view.badgeview.BadgeLayout
import com.ave.vastgui.tools.viewbinding.viewBinding
import kotlin.random.Random

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2025/6/4

class BadgeLayoutFragment : Fragment(R.layout.fragment_badge_layout) {

    private val logcat = logFactory("BadgeLayoutFragment")

    private val binding by viewBinding(FragmentBadgeLayoutBinding::bind)

    private val position = intArrayOf(BadgeLayout.POSITION_LEFT_TOP,
        BadgeLayout.POSITION_RIGHT_TOP,
        BadgeLayout.POSITION_RIGHT_BOTTOM,
        BadgeLayout.POSITION_LEFT_BOTTOM,
        BadgeLayout.POSITION_START_TOP,
        BadgeLayout.POSITION_END_TOP,
        BadgeLayout.POSITION_END_BOTTOM,
        BadgeLayout.POSITION_START_BOTTOM)

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

    private var posIndex = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.numBadge.children.forEachIndexed { index, view ->
            logcat.d("index=$index view=${view::class.java.simpleName}")
        }

        // 增加数字
        binding.numBadgeAddBtn.setOnClickListener {
            binding.numBadge.bubbleTextNum++
        }

        // 重置数字
        binding.numBadgeResetBtn.setOnClickListener {
            binding.numBadge.bubbleTextNum = 0
        }

        // 更改徽标位置
        binding.numBadgeChangePositionBtn.setOnClickListener {
            binding.numBadge.badgePosition = position[(posIndex++) % position.size]
        }

        // 更改徽标颜色
        binding.numBadgeChangeColorBtn.setOnClickListener {
            binding.numBadge.badgeColor = badgeColors[Random.nextInt(0, badgeColors.size)]
        }

        // 切换徽标最大值
        binding.numBadgeSetMaxBtn.text =
            String.format(getString(R.string.badge_num_set_max), binding.numBadge.bubbleTextMaxNum)
        binding.numBadgeSetMaxBtn.setOnClickListener {
            binding.numBadge.bubbleTextNum = 0
            binding.numBadge.bubbleTextMaxNum = Random.Default.nextInt(0, 21)
            binding.numBadgeSetMaxBtn.text =
                String.format(getString(R.string.badge_num_set_max), binding.numBadge.bubbleTextMaxNum)
        }

        // 切换徽标文本颜色
        binding.numBadgeChangeTextColorBtn.setOnClickListener {
            binding.numBadge.bubbleTextColor = textColors[Random.nextInt(0, textColors.size)]
        }

        // 切换徽标图标内边距
        binding.numBadgeIconPaddingSlider.setLabelFormatter { String.format(getString(R.string.badge_icon_padding), it) }
        binding.numBadgeIconPaddingSlider.addOnChangeListener { _, value, _ ->
            binding.numBadge.setIconPadding(value.DP, value.DP, value.DP, value.DP)
        }

        // 切换徽标字体大小
        binding.numBadgeTextSizeSlider.addOnChangeListener { _, value, _ ->
            binding.numBadge.bubbleTextSize = value.SP
        }

        // 切换徽标半径
        binding.numBadgeBubbleRadiusSlider.setLabelFormatter { String.format(getString(R.string.badge_bubble_radius), it) }
        binding.numBadgeBubbleRadiusSlider.addOnChangeListener { _, value, _ ->
            binding.numBadge.bubbleRadius = value.DP
        }
    }

}