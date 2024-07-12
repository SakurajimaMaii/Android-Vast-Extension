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

import androidx.recyclerview.widget.DiffUtil

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/2/27
// Documentation: https://ave.entropy2020.cn/documents/VastAdapter/

/**
 * Because [ItemWrapper] is used, the original [DiffUtil] will be
 * inconvenient to use, so [ItemDiffUtil] was designed to replace it.
 *
 * @since 1.1.1
 */
abstract class ItemDiffUtil<T : Any> : DiffUtil.ItemCallback<ItemWrapper<T>>() {

    /** @see newAreContentsTheSame */
    final override fun areContentsTheSame(
        oldItem: ItemWrapper<T>,
        newItem: ItemWrapper<T>
    ): Boolean {
        return newAreContentsTheSame(oldItem.data, newItem.data)
    }

    /** @see newAreItemsTheSame */
    final override fun areItemsTheSame(oldItem: ItemWrapper<T>, newItem: ItemWrapper<T>): Boolean {
        return newAreItemsTheSame(oldItem.data, newItem.data) &&
                oldItem.layoutId == newItem.layoutId &&
                oldItem.getOnItemClickListener() == newItem.getOnItemClickListener() &&
                oldItem.getOnItemLongClickListener() == newItem.getOnItemLongClickListener() &&
                oldItem.mOnItemChildClickArray == newItem.mOnItemChildClickArray &&
                oldItem.mOnItemChildLongClickArray == newItem.mOnItemChildLongClickArray
    }

    abstract fun newAreContentsTheSame(oldItem: T?, newItem: T?): Boolean

    abstract fun newAreItemsTheSame(oldItem: T?, newItem: T?): Boolean

}