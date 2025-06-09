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
import com.ave.vastgui.tools.view.breadcrumb.BreadCrumb
import com.ave.vastgui.tools.viewbinding.viewBinding

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2025/6/7
// Documentation:

class BreadcrumbActivity : ComponentActivity(R.layout.activity_breadcrumb) {

    private val binding by viewBinding(ActivityBreadcrumbBinding::bind)

    private var index = 0

    private val items = arrayOf("首页", "当这个页面标题很长很长很长时需要省略", "详情页")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        items.forEach { binding.breadCrumbView.addBreadCrumbItem(BreadCrumb(it)) }

        binding.addItemBtn.setOnClickListener {
            binding.breadCrumbView.addBreadCrumbItem(BreadCrumb("${index++}"))
        }
    }

}