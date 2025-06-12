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
import androidx.appcompat.app.AppCompatActivity
import com.ave.vastgui.app.R
import com.ave.vastgui.app.databinding.ActivityWaveProgressViewBinding
import com.ave.vastgui.tools.utils.ColorUtils
import com.ave.vastgui.tools.utils.DensityUtils.DP
import com.ave.vastgui.tools.utils.DensityUtils.SP
import com.ave.vastgui.tools.utils.drawable
import com.ave.vastgui.tools.viewbinding.viewBinding

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Documentation: https://sakurajimamaii.github.io/AVE-DOC/documents/tools/core-topics/ui/progress/wave-progress-view/

class WaveProgressViewActivity : AppCompatActivity(R.layout.activity_wave_progress_view) {

    private val binding by viewBinding(ActivityWaveProgressViewBinding::bind)

    private val icons = arrayOf(R.drawable.ic_dog_64dp, R.drawable.ic_bird_128dp, R.drawable.ic_cat_256dp, 0)

    private var iconIndex = 0

    private val colors = intArrayOf(
        ColorUtils.colorHex2Int("#F60C0C"),
        ColorUtils.colorHex2Int("#F3B913"),
        ColorUtils.colorHex2Int("#E7F716"),
        ColorUtils.colorHex2Int("#3DF30B"),
        ColorUtils.colorHex2Int("#0DF6EF"),
        ColorUtils.colorHex2Int("#0829FB"),
        ColorUtils.colorHex2Int("#B709F4")
    )

    private var textColorIndex = 0

    private var strokeColorIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.switchIconBtn.setOnClickListener {
            binding.waveProgressView.setImage(drawable(icons[(iconIndex++) % icons.size]))
            // binding.waveProgressView.setImage(R.drawable.ic_cat_256dp)
        }

        binding.switchTextColorBtn.setOnClickListener {
            binding.waveProgressView.textColor = colors[(textColorIndex++) % colors.size]
        }

        binding.switchTextShowBtn.setOnClickListener {
            binding.waveProgressView.showText = !binding.waveProgressView.showText
        }

        binding.switchStrokeColorBtn.setOnClickListener {
            binding.waveProgressView.strokeColor = colors[(strokeColorIndex++) % colors.size]
        }

        binding.progressChangeUpdateSlider.addOnChangeListener { _, value, _ ->
            binding.waveProgressView.setUpdateInterval(value.toLong())
        }

        binding.progressTextSizeSlider.addOnChangeListener { _, value, _ ->
            binding.waveProgressView.textSize = value.SP
        }

        binding.progressSlider.addOnChangeListener { _, value, _ ->
            binding.waveProgressView.currentProgress = value
        }

        binding.strokeSlider.addOnChangeListener { _, value, _ ->
            binding.waveProgressView.strokeWidth = 20f.DP * (value / 100f)
        }

        binding.spaceSlider.addOnChangeListener { _, value, _ ->
            binding.waveProgressView.spaceWidth = 20f.DP * (value / 100f)
        }

        binding.changeWaveSpeedSlider.addOnChangeListener { _, value, _ ->
            binding.waveProgressView.setSpeed(value.DP)
        }

        binding.changeWaveWidthSlider.addOnChangeListener { _, value, _ ->
            with(binding.waveProgressView) {
                setWave(value.DP, waveHeight)
            }
        }

        binding.changeWaveHeightSlider.addOnChangeListener { _, value, _ ->
            with(binding.waveProgressView) {
                setWave(waveWidth, value.DP)
            }
        }
    }

}