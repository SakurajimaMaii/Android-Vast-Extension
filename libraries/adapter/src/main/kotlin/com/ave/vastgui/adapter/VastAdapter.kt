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

package com.ave.vastgui.adapter

import android.content.Context
import android.content.res.Resources
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import androidx.core.util.forEach
import androidx.recyclerview.widget.RecyclerView
import com.ave.vastgui.adapter.base.ItemClickListener
import com.ave.vastgui.adapter.base.ItemHolder
import com.ave.vastgui.adapter.base.ItemWrapper
import com.ave.vastgui.adapter.listener.OnItemClickListener
import com.ave.vastgui.adapter.listener.OnItemLongClickListener

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2021/4/2
// Documentation: https://ave.entropy2020.cn/documents/VastAdapter/

/**
 * [VastAdapter] 。
 *
 * @since 1.1.1
 */
open class VastAdapter<T> @JvmOverloads constructor(
    protected var mContext: Context,
    factories: MutableList<ItemHolder.HolderFactory<T>>,
    protected val mDataSource: MutableList<ItemWrapper<T>> = mutableListOf(),
) : RecyclerView.Adapter<ItemHolder<T>>(), ItemClickListener<T> {

    private val mType2Factory = SparseArray<ItemHolder.HolderFactory<T>>()
    private var mOnItemClickListener: OnItemClickListener<T>? = null
    private var mOnItemLongClickListener: OnItemLongClickListener<T>? = null

    final override fun getItemCount() = mDataSource.size

    final override fun onBindViewHolder(holder: ItemHolder<T>, position: Int) {
        val itemData = mDataSource[position]
        holder.onBindData(itemData.getData())
        holder.itemView.setOnClickListener {
            if (null != itemData.getOnItemClickListener()) {
                itemData.getOnItemClickListener()
                    ?.onItemClick(holder.itemView, position, itemData)
            } else {
                mOnItemClickListener?.onItemClick(holder.itemView, position, itemData)
            }
        }
        holder.itemView.setOnLongClickListener {
            val res = if (null != itemData.getOnItemLongClickListener()) {
                itemData.getOnItemLongClickListener()
                    ?.onItemLongClick(holder.itemView, position, itemData)
            } else {
                mOnItemLongClickListener?.onItemLongClick(holder.itemView, position, itemData)
            }
            return@setOnLongClickListener res ?: false
        }
        itemData.mOnItemChildClickArray?.forEach { key, value ->
            holder.itemView.findViewById<View>(key)?.let { childView ->
                childView.setOnClickListener {
                    value.onItemClick(childView, position, itemData)
                }
            }
        }
        itemData.mOnItemChildLongClickArray?.forEach { key, value ->
            holder.itemView.findViewById<View>(key)?.let { childView ->
                childView.setOnClickListener {
                    value.onItemLongClick(childView, position, itemData)
                }
            }
        }
    }

    final override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder<T> {
        mType2Factory.forEach { key, factory ->
            if (key == viewType) {
                return factory.onCreateHolder(parent, viewType)
            }
        }
        throw RuntimeException("Not found the factory according to the $viewType.")
    }

    final override fun getItemViewType(position: Int): Int {
        val viewType = mDataSource[position].layoutId
        try {
            mContext.resources.getLayout(viewType)
        } catch (e: Resources.NotFoundException) {
            throw IllegalArgumentException("Please check if the return layoutId is correct.")
        }
        return viewType
    }

    final override fun setOnItemClickListener(listener: OnItemClickListener<T>?) {
        mOnItemClickListener = listener
    }

    final override fun setOnItemLongClickListener(listener: OnItemLongClickListener<T>?) {
        mOnItemLongClickListener = listener
    }

    final override fun getOnItemClickListener(): OnItemClickListener<T>? =
        mOnItemClickListener

    final override fun getOnItemLongClickListener(): OnItemLongClickListener<T>? =
        mOnItemLongClickListener

    /**
     * 将 [itemWrapper] 添加到列表尾部。
     *
     * @since 1.2.0
     */
    fun add(itemWrapper: ItemWrapper<T>) {
        val index = itemCount
        mDataSource.add(index, itemWrapper)
        notifyItemChanged(index)
    }

    /**
     * 更新 [position] 位置上的元素，仅更新数据部分。
     *
     * @since 1.2.0
     */
    fun update(position: Int, data: T): Boolean {
        if (position !in 0 until itemCount) return false
        val old = mDataSource[position]
        mDataSource[position] = ItemWrapper(
            data,
            old.layoutId,
            old.getOnItemClickListener(),
            old.getOnItemLongClickListener()
        )
        notifyItemChanged(position)
        return true
    }

    /**
     * 更新 [position] 位置上的元素。
     *
     * @since 1.2.0
     */
    fun update(position: Int, itemWrapper: ItemWrapper<T>): Boolean {
        if (position !in 0 until itemCount) return false
        mDataSource[position] = itemWrapper
        notifyItemChanged(position)
        return true
    }

    /**
     * 清除 [position] 位置的元素。
     *
     * @since 1.2.0
     */
    fun removeAt(position: Int): ItemWrapper<T> = mDataSource.removeAt(position).apply {
        notifyItemRemoved(position)
    }

    /**
     * 清空列表内的全部元素。
     *
     * @since 1.2.0
     */
    fun clear() {
        val count = itemCount
        mDataSource.clear()
        notifyItemRangeChanged(0, count)
    }

    /**
     * 查询 [ItemWrapper.getData] 和 [data] 匹配的第一个元素的索引。
     *
     * @since 1.2.0
     */
    fun indexOfFirst(data: T) = mDataSource.indexOfFirst { it.getData() === data }

    init {
        factories.forEach { factory ->
            mType2Factory.put(factory.layoutId, factory)
        }
    }

}