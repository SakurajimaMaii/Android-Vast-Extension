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

package com.ave.vastgui.app.adapter.entity

import com.ave.vastgui.adapter.base.ItemDiffUtil
import com.ave.vastgui.app.R
import com.ave.vastgui.tools.network.response.ResponseApi

/** 通过 api 请求的图片数据 */
data class Images(
    val code: Int,
    val message: String,
    val result: Result?
) : ResponseApi {
    data class Result(
        val list: List<Image>,
        val total: Int
    )

    /**
     * 请求到的图片对象。
     *
     * @property id 图片的id。
     * @property title 图片的标题。
     * @property type 图片的类型。
     * @property url 图片的链接。
     */
    data class Image(
        val id: Int,
        val title: String,
        val type: String,
        val url: String
    ) {
        fun getLayoutId() =
            if (id % 2 == 0) R.layout.item_image_comic else R.layout.item_image_default
    }

    override fun isSuccessful(): Boolean = code == 200

    override fun message(): String = message
}

object ImageDiffUtil : ItemDiffUtil<Images.Image>() {
    override fun newAreContentsTheSame(oldItem: Images.Image?, newItem: Images.Image?): Boolean {
        return oldItem?.id == newItem?.id
    }

    override fun newAreItemsTheSame(oldItem: Images.Image?, newItem: Images.Image?): Boolean {
        return oldItem?.id == newItem?.id
    }
}