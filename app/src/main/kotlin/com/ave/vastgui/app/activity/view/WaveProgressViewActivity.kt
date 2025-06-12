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

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.ave.vastgui.app.R
import com.ave.vastgui.app.databinding.ActivityWaveProgressViewBinding
import com.ave.vastgui.tools.utils.ColorUtils
import com.ave.vastgui.tools.utils.DensityUtils.DP
import com.ave.vastgui.tools.utils.DensityUtils.SP
import com.ave.vastgui.tools.utils.drawable
import com.ave.vastgui.tools.viewbinding.viewBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Documentation: https://sakurajimamaii.github.io/AVE-DOC/documents/tools/core-topics/ui/progress/wave-progress-view/

class WaveProgressViewActivity : AppCompatActivity(R.layout.activity_wave_progress_view) {

    private val binding by viewBinding(ActivityWaveProgressViewBinding::bind)

    private val icons = arrayOf(R.drawable.ic_dog_64dp, R.drawable.ic_bird_128dp, R.drawable.ic_cat_256dp, 0)

    private var iconIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.waveProgressView.apply {
//            progressBackgroundColor = ColorUtils.colorHex2Int("#e74c3c")
//            progressColor = ColorUtils.colorHex2Int("#27ae60")
//            textColor = ColorUtils.colorHex2Int("#000000")
//            mRadius = 100f.DP
//            strokeColor = ColorUtils.colorHex2Int("#8e44ad")
//            showText = false
//            setImage(R.drawable.ic_github)
//            textSize = 30f.SP
        }

//        binding.imageProgressView.apply {
//            textSize = 30f.SP
//        }
        binding.waveProgressView.setUpdateInterval(-1)

//        binding.waveProgressView.progressColor = ColorUtils.getColorIntWithTransparency(60, Color.GREEN)

        lifecycleScope.launch {
            delay(500L)
            binding.waveProgressView.setUpdateInterval(20)
        }

//        binding.switchIconBtn.setOnClickListener {
//            binding.waveProgressView.setImage(drawable(icons[(iconIndex++) % icons.size]))
//        }
//
//        binding.progressSlider.addOnChangeListener { _, value, _ ->
//            binding.waveProgressView.currentProgress = value
//        }
//
//        binding.strokeSlider.addOnChangeListener { _, value, _ ->
//            binding.waveProgressView.strokeWidth = 20f.DP * (value / 100f)
//        }
//
//        binding.spaceSlider.addOnChangeListener { _, value, _ ->
//            binding.waveProgressView.spaceWidth = 20f.DP * (value / 100f)
//        }
    }

}