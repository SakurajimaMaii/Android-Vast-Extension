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

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/1/17
// Documentation: https://ave.entropy2020.cn/documents/adapter/

/** @since 1.1.1 */
open class ItemHolder<T : Any>(itemView: View) : RecyclerView.ViewHolder(itemView) {

    /**
     * If the current [ItemHolder] represents an empty view, this method will
     * not be called.
     *
     * @since 1.1.1
     */
    open fun onBindData(item: T) {

    }

    interface HolderFactory<T : Any> : ItemType {
        /** @since 1.1.1 */
        fun onCreateHolder(parent: ViewGroup, viewType: Int): ItemHolder<T>
    }
}

/** @since 1.2.0 */
class EmptyHolderFactory<T : Any> internal constructor(override val layoutId: Int) : ItemHolder.HolderFactory<T> {
    override fun onCreateHolder(parent: ViewGroup, viewType: Int): ItemHolder<T> {
        val view =
            LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
        return ItemHolder(view)
    }
}