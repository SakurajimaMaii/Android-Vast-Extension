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

package com.ave.vastgui.adapter

import android.view.ViewGroup
import com.ave.vastgui.adapter.base.BaseAdapter
import com.ave.vastgui.adapter.base.BaseHolder
import com.ave.vastgui.adapter.widget.AdapterItemWrapper

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2021/4/2
// Description: VastAdapter help you to create a recyclerView adapter.
// Documentation: [VastAdapter](https://ave.entropy2020.cn/documents/VastAdapter/VastAdapter/)

/**
 * VastAdapter.
 *
 * @property mDataSource data source.
 * @property mFactories viewHolder factories.
 */
abstract class VastAdapter(
    protected val mDataSource: MutableList<AdapterItemWrapper<*>>,
    protected val mFactories: MutableList<BaseHolder.HolderFactory>
) : BaseAdapter<BaseHolder>() {

    private val type2ItemType: MutableMap<String, Int> = HashMap()

    final override fun getItemViewType(position: Int): Int {
        val item = mDataSource[position]
        val type: String = item.getHolderType()
        if (type2ItemType[type] == null) {
            throw RuntimeException("Not found the itemType according to the position.")
        } else {
            return type2ItemType[type]!!
        }
    }

    final override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseHolder {
        val targetFactory: BaseHolder.HolderFactory = mFactories[viewType]
        return targetFactory.onCreateHolder(parent, viewType)
    }

    final override fun onBindViewHolder(holder: BaseHolder, position: Int) {
        val itemData = mDataSource[position]
        itemData.getData()?.apply {
            holder.onBindData(this)
            holder.itemView.setOnClickListener {
                if (null != itemData.getClickEvent()) {
                    itemData.getClickEvent()?.onItemClick(holder.itemView, position)
                } else {
                    onItemClickListener?.onItemClick(holder.itemView, position)
                }
            }
            holder.itemView.setOnLongClickListener {
                val res = if (null != itemData.getLongClickEvent()) {
                    itemData.getLongClickEvent()?.onItemLongClick(holder.itemView, position)
                } else {
                    onItemLongClickListener?.onItemLongClick(holder.itemView, position)
                }
                return@setOnLongClickListener res ?: false
            }
        }
    }

    final override fun getItemCount() = mDataSource.size

    init {
        for (i in mFactories.indices) {
            val factory: BaseHolder.HolderFactory = mFactories[i]
            val type: String = factory.getHolderType()
            val itemType = type2ItemType[type]
            if (itemType != null) {
                val currentFactory: String = factory.javaClass.name
                val sameFactory: String = mFactories[itemType].javaClass.name
                throw RuntimeException("Same type found: $currentFactory and $sameFactory")
            }
            type2ItemType[type] = i
        }
    }

}