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
import com.ave.vastgui.app.R
import com.ave.vastgui.app.databinding.ActivityBadgeViewBinding
import com.ave.vastgui.app.log.logFactory
import com.ave.vastgui.tools.utils.DensityUtils.DP
import com.ave.vastgui.tools.utils.DensityUtils.SP
import com.ave.vastgui.tools.utils.color
import com.ave.vastgui.tools.view.badgeview.BadgeMode
import com.ave.vastgui.tools.viewbinding.viewBinding
import kotlin.random.Random

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/9/11
// Documentation: https://sakurajimamaii.github.io/AVE-DOC/documents/tools/core-topics/ui/badge/description/

class BadgeViewActivity : ComponentActivity(R.layout.activity_badge_view) {

    private val logger = logFactory(BadgeViewActivity::class.java)

    private val binding by viewBinding(ActivityBadgeViewBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        logger.d("The mode of binding.numbadge is ${binding.dotBadge.badgeMode}")

        binding.numBadge.bubbleTextNum = 90
        binding.numBadge.bubbleTextMaxNum = 99
        binding.numBadge.bubbleTextSize = 12f.SP
        binding.numBadge.bubbleTextColor = color(com.ave.vastgui.tools.R.color.md_theme_secondaryContainer)

        binding.textBadge.bubbleText = "你好"

        binding.add.setOnClickListener {
            binding.textBadge.badgeColor = color(com.ave.vastgui.tools.R.color.amour)
            binding.numBadge.bubbleTextNum++
        }

        binding.reset.setOnClickListener {
            binding.numBadge.bubbleTextNum = 90
        }

        binding.showDot.setOnClickListener {
            binding.dotBadge.badgeMode = BadgeMode.DOT
            binding.dotBadge.dotRadius = Random(System.currentTimeMillis()).nextInt(0, 10).toFloat().DP
            binding.dotBadge.showDot()
        }

        binding.hideDot.setOnClickListener {
            binding.dotBadge.hideDot()
        }
    }

}