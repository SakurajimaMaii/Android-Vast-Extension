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

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/1/17
// Documentation: https://ave.entropy2020.cn/documents/VastAdapter/

/** @since 1.1.1 */
abstract class ItemHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {

    /** @since 1.1.1 */
    abstract fun onBindData(item: T)

    interface HolderFactory<T> : ItemType {
        /**
         * 创建当前的 ViewHolder 实例。
         *
         * @since 1.1.1
         */
        fun onCreateHolder(parent: ViewGroup, viewType: Int): ItemHolder<T>
    }
}