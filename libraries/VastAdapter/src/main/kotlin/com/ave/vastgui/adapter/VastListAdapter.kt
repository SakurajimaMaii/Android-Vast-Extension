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
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.ave.vastgui.adapter.base.BaseHolder
import com.ave.vastgui.adapter.widget.AdapterClickListener
import com.ave.vastgui.adapter.widget.AdapterClickRegister
import com.ave.vastgui.adapter.widget.AdapterItemWrapper
import com.ave.vastgui.adapter.widget.AdapterLongClickListener

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/10/10
// Description: 
// Documentation:
// Reference:

abstract class VastListAdapter<T>(
    protected val mFactories: MutableList<BaseHolder.HolderFactory>,
    protected var mDiffCallback: DiffUtil.ItemCallback<AdapterItemWrapper<T>>
) : ListAdapter<AdapterItemWrapper<T>, BaseHolder>(mDiffCallback), AdapterClickRegister {

    private val type2ItemType: MutableMap<String, Int> = HashMap()
    protected var onItemClickListener: AdapterClickListener? = null
    protected var onItemLongClickListener: AdapterLongClickListener? = null

    final override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
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
        val itemData = getItem(position)
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

    final override fun registerClickEvent(l: AdapterClickListener?) {
        onItemClickListener = l
    }

    final override fun registerLongClickEvent(l: AdapterLongClickListener?) {
        onItemLongClickListener = l
    }

    final override fun getClickEvent(): AdapterClickListener? {
        throw RuntimeException("You shouldn't call this method.")
    }

    final override fun getLongClickEvent(): AdapterLongClickListener? {
        throw RuntimeException("You shouldn't call this method.")
    }

}