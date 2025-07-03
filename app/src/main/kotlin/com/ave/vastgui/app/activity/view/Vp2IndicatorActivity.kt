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
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.ave.vastgui.app.R
import com.ave.vastgui.app.databinding.ActivityVp2IndicatorBinding
import com.ave.vastgui.app.fragment.ImagesFragment
import com.ave.vastgui.app.fragment.ReceiverFragment
import com.ave.vastgui.app.fragment.SenderFragment
import com.ave.vastgui.app.fragment.VideosFragment
import com.ave.vastgui.core.extension.defaultLogTag
import com.ave.vastgui.tools.utils.DensityUtils.DP
import com.ave.vastgui.tools.utils.color
import com.ave.vastgui.tools.view.viewpager2.adapter.BaseFragmentStateAdapter
import com.ave.vastgui.tools.view.viewpager2.indicator.Vp2IndicatorView
import com.ave.vastgui.tools.viewbinding.viewBinding
import com.log.vastgui.android.lifecycle.LogLifecycle
import kotlin.math.roundToInt

// Author: Vast Gui 
// Email: guihy2019@gmail.com
// Date: 2022/4/13 19:45
// Documentation: https://sakurajimamaii.github.io/AVE-DOC/documents/tools/core-topics/ui/viewpager2/fragment-adapter/
// Documentation: https://sakurajimamaii.github.io/AVE-DOC/documents/tools/core-topics/ui/viewpager2/vp2-indicator-view/vp2-indicator-view/

@LogLifecycle
class Vp2IndicatorActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityVp2IndicatorBinding::inflate)
    private val fragments = ArrayList<Fragment>().apply {
        add(VideosFragment())
        add(ImagesFragment())
        add(ReceiverFragment())
        add(SenderFragment())
    }

    private var styleIndex = 0

    private val selectedColor by lazy {
        intArrayOf(color(R.color.lightcoral),
            color(R.color.limegreen),
            color(R.color.darkturquoise))
    }

    private var selectedColorIndex = 0

    private val unselectedColor by lazy {
        intArrayOf(color(R.color.lavender),
            color(R.color.gray),
            color(R.color.darkgreen))
    }

    private var unselectedColorIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        // 切换指示器数量
        binding.switchCountBtn.text = String.format(getString(R.string.vp2_indicator_switch_count_fmt), binding.vp2indicator.indicatorItemCount)
        binding.switchCountBtn.setOnClickListener {
            binding.vp2indicator.setCurrentSelectedPosition(0)
            binding.vp2indicator.setIndicatorItemCount(((binding.vp2indicator.indicatorItemCount + 1) % 6).coerceAtLeast(1))
            binding.switchCountBtn.text = String.format(getString(R.string.vp2_indicator_switch_count_fmt), binding.vp2indicator.indicatorItemCount)
            binding.changePositionSlider.value = (binding.vp2indicator.indicatorItemCount - 1).toFloat()
        }

        // 切换指示器风格
        binding.switchStyleBtn.text = String.format(getString(R.string.vp2_indicator_switch_style_fmt), binding.vp2indicator.indicatorStyle)
        binding.switchStyleBtn.setOnClickListener {
            val style = Vp2IndicatorView.Style.entries[(++styleIndex) % Vp2IndicatorView.Style.entries.size]
            if (style == Vp2IndicatorView.Style.BITMAP) {
                binding.vp2indicator.setSelectedBitmap(R.drawable.ic_indicator_select)
                binding.vp2indicator.setUnSelectedBitmap(R.drawable.ic_indicator_unselect)
            }
            binding.vp2indicator.setIndicatorStyle(style)
            binding.switchStyleBtn.text = String.format(getString(R.string.vp2_indicator_switch_style_fmt), binding.vp2indicator.indicatorStyle)
        }

        // 切换指示器选中颜色
        binding.switchSelectedColor.setOnClickListener {
            binding.vp2indicator.setSelectedColorInt(selectedColor[(selectedColorIndex++) % selectedColor.size])
        }

        // 切换指示器未选中颜色
        binding.switchUnselectedColor.setOnClickListener {
            binding.vp2indicator.setUnSelectedColorInt(unselectedColor[(unselectedColorIndex++) % unselectedColor.size])
        }

        // 切换指示器位图尺寸
        binding.changeBitmapSizeSlider.addOnChangeListener { _, value, _ ->
            binding.vp2indicator.setBitmapSize(value.DP.roundToInt(), value.DP.roundToInt())
        }

        // 切换指示器圆形半径
        binding.changeRadiusSlider.addOnChangeListener { _, value, _ ->
            binding.vp2indicator.setIndicatorCircleRadius(value.DP)
        }

        binding.changeDistanceSlider.addOnChangeListener { _, value, _ ->
            binding.vp2indicator.setIndicatorItemDistance(value.DP)
        }

        binding.changePositionSlider.addOnChangeListener { _, value, _ ->
            binding.vp2indicator.setCurrentSelectedPosition(value.toInt())
        }

        binding.vp2.apply {
            adapter = BaseFragmentStateAdapter(this@Vp2IndicatorActivity, fragments)
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    binding.toolbar.title = fragments[position].defaultLogTag()
                }
            })
        }
        binding.vp2indicator.apply {
            setIndicatorStyle(Vp2IndicatorView.Style.BITMAP)
            setBitmapSize(20f.DP.toInt(), 20f.DP.toInt())
            setIndicatorCircleRadius(8F.DP)
            attachToViewPager2(binding.vp2)
        }
    }

}