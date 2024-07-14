/*
 * Copyright 2021-2024 VastGui
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
import com.ave.vastgui.adapter.BaseBindAdapter
import com.ave.vastgui.adapter.BaseBindListAdapter
import com.ave.vastgui.adapter.BaseBindPagingAdapter

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/10/10
// Documentation: https://ave.entropy2020.cn/documents/adapter/

/**
 * Applies to [BaseBindAdapter] , [BaseBindListAdapter] and
 * [BaseBindPagingAdapter] ViewHolder. If you want to customize your own
 * ViewHolder, you should make your ViewHolder inherit [ItemBindHolder] and
 * use it as the return value of setViewHolder.
 *
 * @since 1.1.1
 */
open class ItemBindHolder<T : Any>(protected var binding: ViewDataBinding) :
    RecyclerView.ViewHolder(binding.root) {

    /** @since 1.1.1 */
    open fun onBindData(variableId: Int, item: T) {
        binding.setVariable(variableId, item)
    }

}