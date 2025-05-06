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
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.ave.vastgui.app.R
import com.ave.vastgui.app.activity.view.rvadapter.ImagePagingAdapter
import com.ave.vastgui.app.databinding.ActivitySnappingRecyclerviewBinding
import com.ave.vastgui.app.viewmodel.NetVM
import com.ave.vastgui.tools.utils.DensityUtils.DP
import com.ave.vastgui.tools.view.recyclerview.decoration.HorizontalItemDecoration
import com.ave.vastgui.tools.viewbinding.viewBinding
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2025/5/3
// Documentation:

class SnappingRecyclerViewActivity : ComponentActivity(R.layout.activity_snapping_recyclerview) {

    private val binding by viewBinding(ActivitySnappingRecyclerviewBinding::bind)
    private val viewModel by viewModels<NetVM>()

    private var imageAdapter: ImagePagingAdapter by Delegates.notNull()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        imageAdapter = ImagePagingAdapter(this)
        binding.imageRv.adapter = imageAdapter
        binding.imageRv.layoutManager = object : GridLayoutManager(this, 4) {}
        // binding.imageRv.enableViewScaling(true)
        binding.imageRv.addItemDecoration(HorizontalItemDecoration(20f.DP))

        lifecycleScope.launch {
            viewModel.imageFlow.collect {
                imageAdapter.submitData(it)
            }
        }
    }

}