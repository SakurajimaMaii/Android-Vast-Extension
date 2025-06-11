/*
 * Copyright 2021-2025 VastGui
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
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
import com.ave.vastgui.app.databinding.ActivityBreadcrumbBinding
import com.ave.vastgui.tools.utils.DensityUtils.DP
import com.ave.vastgui.tools.utils.DensityUtils.SP
import com.ave.vastgui.tools.utils.color
import com.ave.vastgui.tools.utils.drawable
import com.ave.vastgui.tools.view.breadcrumb.BreadCrumb
import com.ave.vastgui.tools.viewbinding.viewBinding
import java.util.Stack

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2025/6/7
// Documentation:

class BreadcrumbActivity : ComponentActivity(R.layout.activity_breadcrumb) {

    private val binding by viewBinding(ActivityBreadcrumbBinding::bind)

    private var index = 0

    private val items by lazy {
        arrayOf(BreadCrumb(drawable(R.drawable.ic_home_24dp)!!),
            BreadCrumb(drawable(R.drawable.ic_menu_24dp), "当这个页面标题很长很长很长时需要省略"),
            BreadCrumb("详情页"))
    }

    private val textColors by lazy {
        intArrayOf(color(R.color.honeydew), color(R.color.lightslategray), color(R.color.lime))
    }

    private var textColorsIndex = 0

    private val icons = intArrayOf(R.drawable.ic_breadcrumb_interval_icon_32dp,
        com.ave.vastgui.tools.R.drawable.ic_breadcrumb_default_interval_icon)

    private var iconIndex = 0

    private val breadcrumbIds = Stack<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding.breadCrumbLayout.addItems(items).forEach { id ->
            breadcrumbIds.push(id)
        }

        // 添加导航路径
        binding.addItemBtn.setOnClickListener {
            binding.breadCrumbLayout.addItem(BreadCrumb(path = "${index++}")).also { id ->
                breadcrumbIds.push(id)
            }
        }

        // 移除导航路径
        binding.removeItemBtn.setOnClickListener {
            if (breadcrumbIds.isEmpty()) return@setOnClickListener
            binding.breadCrumbLayout.removeLastItem(breadcrumbIds.pop())
        }

        // 切换导航字体颜色
        binding.switchTextColorBtn.setOnClickListener {
            binding.breadCrumbLayout.setTextColor(textColors[(textColorsIndex++) % textColors.size])
        }

        // 切换导航字体大小
        binding.textSizeSlider.addOnChangeListener { _, value, _ ->
            binding.breadCrumbLayout.setTextSize(value.SP)
        }

        // 切换分隔符
        binding.switchInternalIconBtn.setOnClickListener {
            binding.breadCrumbLayout.setIntervalIcon(icons[(iconIndex++) % icons.size])
        }

        // 改变分隔符大小
        binding.internalIconSizeSlider.addOnChangeListener { _, value, _ ->
            binding.breadCrumbLayout.setIntervalIconSize(value.DP, value.DP)
        }

        binding.itemWidthSlider.addOnChangeListener { _, value, _ ->
            binding.breadCrumbLayout.setItemWidth(value.DP)
        }

        binding.breadCrumbLayout.addBreadCrumbClickListener { _, id ->
            binding.breadCrumbLayout.removeAfterItem(id)
        }
    }

}