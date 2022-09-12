/*
 * Copyright 2022 VastGui
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gcode.vastadapter.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gcode.vastadapter.interfaces.VastAdapterItem

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
 *
 * @since 0.0.1
 */
abstract class VastAdapter(
    protected val dataSource: MutableList<VastAdapterItem>,
    protected val factories: MutableList<VastAdapterVH.BVAdpVHFactory>
) : RecyclerView.Adapter<VastAdapterVH>() {

    private val type2ItemType: MutableMap<String, Int> = HashMap()

    // Fix https://github.com/SakurajimaMaii/VastUtils/issues/36
    private var onItemClickListener: OnItemClickListener? = null
    private var onItemLongClickListener: OnItemLongClickListener? = null

    final override fun getItemViewType(position: Int): Int {
        val item: VastAdapterItem = dataSource[position]
        val type: String = item.getVAdpItemType()
        if (type2ItemType[type] == null) {
            throw RuntimeException("Not found the itemType according to the position.")
        } else {
            return type2ItemType[type]!!
        }
    }

    final override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VastAdapterVH {
        val targetFactory: VastAdapterVH.BVAdpVHFactory = factories[viewType]
        return targetFactory.onCreateViewHolder(parent, viewType)
    }

    final override fun onBindViewHolder(holder: VastAdapterVH, position: Int) {
        val itemData: VastAdapterItem = dataSource[position]
        holder.onBindData(itemData)
        holder.itemView.setOnClickListener {
            if (null != itemData.vAapClickEventListener) {
                itemData.vAapClickEventListener!!.vAapClickEvent(holder.itemView, position)
            } else {
                onItemClickListener?.onItemClick(holder.itemView, position)
            }
        }
        holder.itemView.setOnLongClickListener {
            val res = if (null != itemData.vAdpLongClickEventListener) {
                itemData.vAdpLongClickEventListener!!.vAdpLongClickEvent(holder.itemView, position)
            } else {
                onItemLongClickListener?.onItemLongClick(holder.itemView, position)
            }
            return@setOnLongClickListener res ?: false
        }
    }

    final override fun getItemCount() = dataSource.size

    init {
        for (i in factories.indices) {
            val factory: VastAdapterVH.BVAdpVHFactory = factories[i]
            val type: String = factory.getVAdpVHType()
            val itemType = type2ItemType[type]
            if (itemType != null) {
                val currentFactory: String = factory.javaClass.name
                val sameFactory: String = factories[itemType].javaClass.name
                throw RuntimeException("Same type found: $currentFactory and $sameFactory")
            }
            type2ItemType[type] = i
        }
    }

    /**
     * Register a click listener for adapter.
     *
     * @param onItemClickListener a click listener.
     *
     * @since 0.0.4
     */
    fun setOnItemClickListener(onItemClickListener: OnItemClickListener?) {
        this.onItemClickListener = onItemClickListener
    }

    /**
     * Register a long click listener for adapter.
     *
     * @param onItemLongClickListener a long click listener.
     *
     * @since 0.0.4
     */
    fun setOnItemLongClickListener(onItemLongClickListener: OnItemLongClickListener?) {
        this.onItemLongClickListener = onItemLongClickListener
    }

    /**
     * Adapter item click listener.
     *
     * @since 0.0.4
     */
    interface OnItemClickListener {
        fun onItemClick(view: View, position: Int)
    }

    /**
     * Adapter item click listener.
     *
     * @since 0.0.4
     */
    interface OnItemLongClickListener {
        fun onItemLongClick(view: View, position: Int): Boolean
    }

}