/*
 * Copyright 2024 VastGui guihy2019@gmail.com
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

package com.ave.vastgui.app.fragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.ave.vastgui.adapter.VastBindPagingAdapter
import com.ave.vastgui.app.BR
import com.ave.vastgui.app.adapter.entity.VideoDiffUtil
import com.ave.vastgui.app.databinding.FragmentVideosBinding
import com.ave.vastgui.app.viewmodel.SharedVM
import com.ave.vastgui.tools.fragment.VastVbVmFragment
import com.ave.vastgui.tools.view.toast.SimpleToast
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

// Author: SakurajimaMai
// Email: guihy2019@gmail.com
// Documentation: https://ave.entropy2020.cn/documents/VastTools/app-entry-points/fragments/fragment/
// Documentation: https://ave.entropy2020.cn/documents/VastAdapter/

class VideosFragment : VastVbVmFragment<FragmentVideosBinding, SharedVM>() {

    private val mAdapter by lazy {
        VastBindPagingAdapter(requireContext(), BR.video, VideoDiffUtil)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mAdapter.setOnItemClickListener { _, position, item ->
            val data = item.getData()
            SimpleToast.showShortMsg("位置是$position，数据是${data.userName}")
        }

        getBinding().imagesRv.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        lifecycleScope.launch {
            getViewModel().videoFlow.collectLatest {
                mAdapter.submitData(it)
            }
        }
    }
}