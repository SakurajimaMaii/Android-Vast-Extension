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
import com.ave.vastgui.tools.network.response.ResponseApi

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2024/1/2

/**
 * 通过 Api 请求到的好看视频测试数据。
 *
 * @property code 返回代码。
 * @property message 返回信息。
 * @property result 包含请求到的好看视频列表。
 */
data class Videos(
    val code: Int,
    val message: String,
    val result: Result
) : ResponseApi {
    data class Result(
        val list: List<Data>,
        val total: Int
    )

    data class Data(
        val coverUrl: String,
        val duration: String,
        val id: Int,
        val playUrl: String,
        val title: String,
        val userName: String,
        val userPic: String
    )

    override fun isSuccessful(): Boolean = code == 200

    override fun isEmpty(): Boolean = result.list.isEmpty()

    override fun message(): String = message
}

object VideoDiffUtil : ItemDiffUtil<Videos.Data>() {
    override fun newAreContentsTheSame(oldItem: Videos.Data?, newItem: Videos.Data?): Boolean {
        return oldItem?.id == newItem?.id
    }

    override fun newAreItemsTheSame(oldItem: Videos.Data?, newItem: Videos.Data?): Boolean {
        return oldItem?.id == newItem?.id
    }
}