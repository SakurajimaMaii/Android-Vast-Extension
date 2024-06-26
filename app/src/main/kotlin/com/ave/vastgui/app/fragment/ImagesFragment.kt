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
import androidx.recyclerview.widget.LinearLayoutManager
import com.ave.vastgui.app.adapter.ImageAdapter
import com.ave.vastgui.app.databinding.FragmentImagesBinding
import com.ave.vastgui.app.log.mLogFactory
import com.ave.vastgui.app.net.OpenApi
import com.ave.vastgui.app.net.OpenApiService
import com.ave.vastgui.tools.fragment.VastVbFragment
import com.ave.vastgui.tools.network.request.create

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2024/1/2 20:33
// Documentation: https://ave.entropy2020.cn/documents/VastTools/app-entry-points/fragments/fragment/

class ImagesFragment : VastVbFragment<FragmentImagesBinding>() {

    private val mAdapter by lazy { ImageAdapter(requireContext()) }
    private val mLogger = mLogFactory.getLogCat(VideosFragment::class.java)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        getBinding().imagesRv.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(requireContext())
            if (childCount > 0) {
                removeAllViews()
                mAdapter.clearAll()
            }
        }

        getBinding().refresh.setOnRefreshListener {
            getResponseBuilder().suspendWithListener({
                OpenApi().create(OpenApiService::class.java).getImages(0, 10)
            }) {
                getBinding().refresh.isRefreshing = false
                onSuccess = {
                    it.result?.list?.forEach { image ->
                        mAdapter.addImage(image)
                    }
                }
                onError = {
                    mLogger.d(getDefaultTag(), it?.message.toString())
                }
            }
        }
    }

}