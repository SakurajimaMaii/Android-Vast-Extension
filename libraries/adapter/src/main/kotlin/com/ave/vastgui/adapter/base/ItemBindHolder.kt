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

package com.ave.vastgui.adapter.base

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.ave.vastgui.adapter.VastBindAdapter
import com.ave.vastgui.adapter.VastBindListAdapter
import com.ave.vastgui.adapter.VastBindPagingAdapter

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/10/10
// Documentation: https://ave.entropy2020.cn/documents/VastAdapter/

/**
 * 适用于 [VastBindAdapter] ， [VastBindListAdapter] 和 [VastBindPagingAdapter]
 * 的 ViewHolder。如果你想自定义 自己的 ViewHolder ，你应该让你的 ViewHolder 继承
 * [ItemBindHolder] 并将其作为 setViewHolder 的返回值。
 *
 * @since 1.1.1
 */
open class ItemBindHolder<T>(protected var binding: ViewDataBinding) :
    RecyclerView.ViewHolder(binding.root) {

    /** @since 1.1.1 */
    open fun onBindData(variableId: Int, item: T) {
        binding.setVariable(variableId, item)
    }

}