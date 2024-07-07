/*
 * Copyright 2022 VastGui guihy2019@gmail.com
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

package com.ave.vastgui.app.activity.view.rvadapter

import android.content.Context
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.ave.vastgui.adapter.BaseAdapter
import com.ave.vastgui.adapter.VastListAdapter
import com.ave.vastgui.adapter.VastPagingAdapter
import com.ave.vastgui.adapter.base.ItemWrapper
import com.ave.vastgui.adapter.listener.OnItemClickListener
import com.ave.vastgui.app.R
import com.ave.vastgui.app.adapter.entity.ImageDiffUtil
import com.ave.vastgui.app.adapter.entity.Images
import com.ave.vastgui.app.adapter.holder.ComicImageHolder
import com.ave.vastgui.app.adapter.holder.DefaultImageHolder
import com.ave.vastgui.app.databinding.ActivityImageBinding
import com.ave.vastgui.app.log.mLogFactory
import com.ave.vastgui.app.net.OpenApi
import com.ave.vastgui.app.net.OpenApiService
import com.ave.vastgui.app.viewmodel.NetVM
import com.ave.vastgui.tools.activity.VastVbVmActivity
import com.ave.vastgui.tools.network.request.create
import com.ave.vastgui.tools.view.dialog.MaterialAlertDialogBuilder
import com.ave.vastgui.tools.view.toast.SimpleToast
import com.ave.vastgui.tools.view.toast.SimpleToast.showShortMsg
import kotlinx.coroutines.launch

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2024/1/4
// Documentation: https://ave.entropy2020.cn/documents/VastAdapter/

private class ImageAdapter(context: Context) : BaseAdapter<Images.Image>(
    context,
    mutableListOf(DefaultImageHolder.Companion, ComicImageHolder.Companion)
)

private class ImageListAdapter(context: Context) : VastListAdapter<Images.Image>(
    context,
    mutableListOf(DefaultImageHolder.Companion, ComicImageHolder.Companion), ImageDiffUtil
)

private class ImagePagingAdapter(context: Context) : VastPagingAdapter<Images.Image>(
    context,
    mutableListOf(DefaultImageHolder.Companion, ComicImageHolder.Companion), ImageDiffUtil
)

class ImageActivity : VastVbVmActivity<ActivityImageBinding, NetVM>() {

    private val logcat = mLogFactory.getLogCat(this::class.java)

    private val mImageAdapter: ImageAdapter by lazy {
        ImageAdapter(this)
    }
    private val mImageListAdapter: ImageListAdapter by lazy {
        ImageListAdapter(this)
    }
    private val mImagePagingAdapter: ImagePagingAdapter by lazy {
        ImagePagingAdapter(this)
    }

    private val sampleClick1 = OnItemClickListener<Images.Image> { view, _, _ ->
        MaterialAlertDialogBuilder(view.context).setMessage("这是一个点击事件").show()
    }
    private val sampleClick2 = OnItemClickListener<Images.Image> { _, _, _ ->
        getSnackbar().setText("列表项被点击").show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mImageAdapter.apply {
            setOnItemClickListener { _, pos, _ ->
                showShortMsg("Click event and pos is $pos.")
            }
            setOnItemLongClickListener { _, pos, _ ->
                showShortMsg("Long click event and pos is $pos.")
                true
            }
        }

        getBinding().images.layoutManager = LinearLayoutManager(this)
        getBinding().images.adapter = mImageAdapter
        lifecycleScope.launch {
            val images = OpenApi()
                .create(OpenApiService::class.java).getImages(0, 10)
                .result?.list
            if (null == images) return@launch
            // 将列表项填充到列表中
            images.forEach {
                mImageAdapter.add(it, R.layout.item_image_default)
            }
            // 更新列表项
            for (index in 0 until mImageAdapter.itemCount step 2) {
                val item = ItemWrapper(mImageAdapter.data[0], R.layout.item_image_comic).apply {
                    setOnItemClickListener { _, _, _ ->
                        SimpleToast.showShortMsg("HAAAAAAAAA")
                    }
                }
                mImageAdapter.update(index, item)
            }
        }

        getBinding().clear.setOnClickListener {
            mImageAdapter.clear()
        }
        getBinding().removeFirst.setOnClickListener {
            mImageAdapter.removeAt(0)
        }
        getBinding().load.setOnClickListener {
            lifecycleScope.launch {
                val images = OpenApi()
                    .create(OpenApiService::class.java).getImages(0, 10)
                    .result?.list
                if (null == images) return@launch
                mImageAdapter.add(images, R.layout.item_image_default)
            }
        }

        val image = Images.Image(0, "", "", "")
        val item = ItemWrapper(image, R.layout.item_image_default).apply {
            setOnItemClickListener { _, _, _ ->

            }
            setOnItemLongClickListener { _, _, _ ->
                false
            }
            addOnItemChildClickListener(R.id.iidImage) { _, _, _ ->

            }
        }
        mImageAdapter.add(item)

//        getBinding().personRv.adapter = mImageListAdapter
//        lifecycleScope.launch {
//            OpenApi().create(OpenApiService::class.java)
//                .getImages(0, 20)
//                .result?.list?.mapIndexed { index, image ->
//                    if (0 == index % 2) {
//                        ItemWrapper(image, image.getLayoutId(), sampleClick1)
//                    } else {
//                        ItemWrapper(image, image.getLayoutId(), sampleClick2)
//                    }
//                }
//                .apply {
//                    mImageListAdapter.submitList(this)
//                }
//        }

//        getBinding().personRv.adapter = mImagePagingAdapter
//        lifecycleScope.launch {
//            getViewModel().imageFlow.collect {
//                mImagePagingAdapter.submitData(it)
//            }
//        }

//        lifecycleScope.launch {
//            WanAndroidApi().getApi(WanAndroidApiService::class.java) {
//                login(UserBean("xxx", "xxx"))
//            }.collect {
//                when (it) {
//                    is Request2.Success -> logcat.d(it)
//                    else -> nothing_to_do()
//                }
//            }
//        }
    }
}