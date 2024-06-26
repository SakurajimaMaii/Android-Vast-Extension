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
import com.ave.vastgui.adapter.VastAdapter
import com.ave.vastgui.adapter.VastListAdapter
import com.ave.vastgui.adapter.VastPagingAdapter
import com.ave.vastgui.adapter.base.ItemWrapper
import com.ave.vastgui.adapter.listener.OnItemClickListener
import com.ave.vastgui.app.R
import com.ave.vastgui.app.adapter.entity.ImageDiffUtil
import com.ave.vastgui.app.adapter.entity.Images
import com.ave.vastgui.app.adapter.holder.ComicImageHolder
import com.ave.vastgui.app.adapter.holder.DefaultImageHolder
import com.ave.vastgui.app.databinding.ActivityPersonBinding
import com.ave.vastgui.app.viewmodel.NetVM
import com.ave.vastgui.tools.activity.VastVbVmActivity
import com.ave.vastgui.tools.view.dialog.MaterialAlertDialogBuilder
import com.ave.vastgui.tools.view.toast.SimpleToast.showShortMsg
import kotlinx.coroutines.launch

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2024/1/4
// Documentation: https://ave.entropy2020.cn/documents/VastAdapter/

private class ImageAdapter(context: Context) : VastAdapter<Images.Image>(
    context,
    mutableListOf(DefaultImageHolder.Companion, ComicImageHolder.Companion)
) {

    fun addImage(image: Images.Image) {
        val index = itemCount
        mDataSource.add(index, ItemWrapper(image, layoutId = R.layout.item_image_default))
        notifyItemChanged(index)
    }


    fun addTypeImage(image: Images.Image) {
        val index = itemCount
        mDataSource.add(index, ItemWrapper(image, layoutId = image.getLayoutId()))
        notifyItemChanged(index)
    }

}

private class ImageListAdapter(context: Context) : VastListAdapter<Images.Image>(
    context,
    mutableListOf(DefaultImageHolder.Companion, ComicImageHolder.Companion), ImageDiffUtil
)

private class ImagePagingAdapter(context: Context) : VastPagingAdapter<Images.Image>(
    context,
    mutableListOf(DefaultImageHolder.Companion, ComicImageHolder.Companion), ImageDiffUtil
)

class ImageActivity : VastVbVmActivity<ActivityPersonBinding, NetVM>() {

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

        getBinding().personRv.layoutManager = LinearLayoutManager(this)

//        getBinding().personRv.adapter = mImageAdapter
//        lifecycleScope.launch {
//            OpenApi().create(OpenApiService::class.java)
//                .getImages(0, 10)
//                .result.list.forEach {
//                    mImageAdapter.addTypeImage(it)
//                }
//        }

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

        getBinding().personRv.adapter = mImagePagingAdapter
        lifecycleScope.launch {
            getViewModel().imageFlow.collect{
                mImagePagingAdapter.submitData(it)
            }
        }
    }
}