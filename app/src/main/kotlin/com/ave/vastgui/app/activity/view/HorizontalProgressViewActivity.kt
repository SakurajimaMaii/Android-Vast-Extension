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
import android.widget.SeekBar
import androidx.activity.ComponentActivity
import com.ave.vastgui.app.R
import com.ave.vastgui.app.databinding.ActivityHorizontalProgressViewBinding
import com.ave.vastgui.tools.view.extension.refreshWithInvalidate
import com.ave.vastgui.tools.viewbinding.viewBinding

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Documentation: https://sakurajimamaii.github.io/AVE-DOC/documents/tools/core-topics/ui/progress/horizontal-progress-view/

class HorizontalProgressViewActivity : ComponentActivity() {

    private val binding by viewBinding(ActivityHorizontalProgressViewBinding::inflate)

    private val gradientImages = arrayOf(R.drawable.img_gradient_256dp, R.drawable.img_horizontal_pv_foreground, 0)
    private var gradientIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.progressSlider.addOnChangeListener { _, value, _ ->
            binding.horizontalPv.currentProgress = value
            binding.horizontalTextPv.currentProgress = value
            binding.lineTextPv.currentProgress = value
        }

        binding.horizontalPvSwitchBackground.setOnClickListener {
            binding.horizontalPv.setProgressDrawable(gradientImages[(gradientIndex++) % gradientImages.size])
        }
    }

}