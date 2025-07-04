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
import com.ave.vastgui.app.databinding.FragmentCropViewBinding
import com.ave.vastgui.app.log.logFactory
import com.ave.vastgui.tools.utils.DensityUtils.DP
import com.ave.vastgui.tools.utils.color
import com.ave.vastgui.tools.view.cropview.CropFrameType
import com.ave.vastgui.tools.viewbinding.viewBinding

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2025/6/5

class CropViewFragment : Fragment(R.layout.fragment_crop_view) {

    private val logcat = logFactory("CropViewFragment")

    private val binding by viewBinding(FragmentCropViewBinding::bind)

    private val types = CropFrameType.entries

    private var typeIndex = 0

    private val maskColors by lazy {
        intArrayOf(requireContext().color(R.color.lightcoral),
            requireContext().color(R.color.paleturquoise),
            requireContext().color(R.color.chartreuse))
    }

    private var maskIndex = 0

    private val strokeColors by lazy {
        intArrayOf(requireContext().color(R.color.dodgerblue),
            requireContext().color(R.color.blue),
            requireContext().color(R.color.grassgreen))
    }

    private var strokeIndex = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.cropSwitchTypeBtn.setOnClickListener {
            binding.cropView.setCropFrameType(types[(typeIndex++) % types.size])
            binding.cropSwitchTypeBtn.text = String.format(getString(R.string.crop_switch_type_fmt), binding.cropView.cropFrameType)
            logcat.d { "当前裁剪框类型：${binding.cropView.cropFrameType}" }
        }

        binding.cropSwitchMaskColorBtn.setOnClickListener {
            binding.cropView.setCropMaskColor(maskColors[(maskIndex++) % maskColors.size], 75)
            logcat.d { "当前裁剪框颜色：${binding.cropView.cropMaskColor.toUInt().toString(16)}" }
        }

        binding.cropSwitchStrokeColorBtn.setOnClickListener {
            binding.cropView.setCropFrameStrokeColor(strokeColors[(strokeIndex++) % strokeColors.size])
            logcat.d { "当前裁剪边框颜色：${binding.cropView.cropFrameStrokeColor.toUInt().toString(16)}" }
        }

        binding.cropChangeWidthSlider.setLabelFormatter { String.format(getString(R.string.crop_change_size_fmt), it) }
        binding.cropChangeWidthSlider.addOnChangeListener { _, value, _ ->
            with(binding.cropView) {
                setCropFrameSize(value.DP, cropFrameHeight)
                logcat.d { "当前裁剪框尺寸 w=$cropFrameWidth h=$cropFrameHeight" }
            }
        }

        binding.cropChangeHeightSlider.setLabelFormatter { String.format(getString(R.string.crop_change_size_fmt), it) }
        binding.cropChangeHeightSlider.addOnChangeListener { _, value, _ ->
            with(binding.cropView) {
                setCropFrameSize(cropFrameWidth, value.DP)
                logcat.d { "当前裁剪框尺寸 w=$cropFrameWidth h=$cropFrameHeight" }
            }
        }

        binding.cropChangeSizeSlider.setLabelFormatter { String.format(getString(R.string.crop_change_size_fmt), it) }
        binding.cropChangeSizeSlider.addOnChangeListener { _, value, _ ->
            with(binding.cropView) {
                setCropFrameSize(value.DP)
                logcat.d { "当前裁剪框尺寸 w=$cropFrameWidth h=$cropFrameHeight" }
            }
        }
    }

}