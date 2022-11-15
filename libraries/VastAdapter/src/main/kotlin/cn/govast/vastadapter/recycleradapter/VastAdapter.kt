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

package cn.govast.vastadapter.recycleradapter

import android.view.ViewGroup
import cn.govast.vastadapter.AdapterItem
import cn.govast.vastadapter.base.BaseAdapter
import cn.govast.vastadapter.base.BaseHolder

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2021/4/2
// Description: VastAdapter help you to create a recyclerView adapter.
// Documentation: [VastAdapter](https://sakurajimamaii.github.io/VastDocs/document/en/VastAdapter.html)

/**
 * VastAdapter.
 *
 * Here is an example in kotlin:
 * ```kotlin
 * class BaseAdapter(
 *     private val items: MutableList<VastAdapterItem>,
 *     factories: MutableList<VastAdapterVH.BVAdpVHFactory>
 * ) : VastAdapter(items, factories)
 * ```
 * For more settings, please refer to the documentation.
 *
 * @property dataSource data source.
 * @property factories viewHolder factories.
 */
abstract class VastAdapter(
    protected val dataSource: MutableList<AdapterItem>,
    protected val factories: MutableList<BaseHolder.HolderFactory>
) : BaseAdapter<BaseHolder>() {

    private val type2ItemType: MutableMap<String, Int> = HashMap()

    final override fun getItemViewType(position: Int): Int {
        val item: AdapterItem = dataSource[position]
        val type: String = item.getItemType()
        if (type2ItemType[type] == null) {
            throw RuntimeException("Not found the itemType according to the position.")
        } else {
            return type2ItemType[type]!!
        }
    }

    final override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseHolder {
        val targetFactory: BaseHolder.HolderFactory = factories[viewType]
        return targetFactory.onCreateHolder(parent, viewType)
    }

    final override fun onBindViewHolder(holder: BaseHolder, position: Int) {
        val itemData: AdapterItem = dataSource[position]
        holder.onBindData(itemData)
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

    final override fun getItemCount() = dataSource.size

    init {
        for (i in factories.indices) {
            val factory: BaseHolder.HolderFactory = factories[i]
            val type: String = factory.getHolderType()
            val itemType = type2ItemType[type]
            if (itemType != null) {
                val currentFactory: String = factory.javaClass.name
                val sameFactory: String = factories[itemType].javaClass.name
                throw RuntimeException("Same type found: $currentFactory and $sameFactory")
            }
            type2ItemType[type] = i
        }
    }

}