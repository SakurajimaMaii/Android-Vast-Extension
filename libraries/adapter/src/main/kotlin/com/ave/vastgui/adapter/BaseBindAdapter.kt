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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.core.util.forEach
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.ave.vastgui.adapter.base.ItemBindHolder
import com.ave.vastgui.adapter.base.ItemClickListener
import com.ave.vastgui.adapter.base.ItemWrapper
import com.ave.vastgui.adapter.listener.OnItemClickListener
import com.ave.vastgui.adapter.listener.OnItemLongClickListener

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2021/4/2
// Documentation: https://ave.entropy2020.cn/documents/VastAdapter/

/**
 * [BaseBindAdapter] 。
 *
 * @since 1.2.0
 */
open class BaseBindAdapter<T : Any> @JvmOverloads constructor(
    protected var mContext: Context,
    /**
     * Set the id of the variable if the content in the layout file is as
     * follows:
     * ```xml
     * <data>
     *     <variable
     *          name="image"
     *          type="com.ave.vastgui.app.adapter.entity.Images.Image" />
     * </data>
     * ```
     * Then the value of [mVariableId] is `BR.image`.
     */
    private val mVariableId: Int,
    protected var mItemList: MutableList<ItemWrapper<T>> = mutableListOf()
) : RecyclerView.Adapter<ItemBindHolder<T>>(), ItemClickListener<T> {

    private var mOnItemClickListener: OnItemClickListener<T>? = null
    private var mOnItemLongClickListener: OnItemLongClickListener<T>? = null
    private var mEmptyItem: ItemWrapper<T>? = null

    final override fun getItemCount() = mItemList.size

    final override fun onBindViewHolder(holder: ItemBindHolder<T>, position: Int) {
        val itemData = mItemList[position]
        itemData.data?.apply { holder.onBindData(mVariableId, this) }
        holder.itemView.setOnClickListener {
            if (null != itemData.getOnItemClickListener()) {
                itemData.getOnItemClickListener()
                    ?.onItemClick(holder.itemView, position, itemData.data)
            } else {
                mOnItemClickListener?.onItemClick(holder.itemView, position, itemData.data)
            }
        }
        holder.itemView.setOnLongClickListener {
            val res = if (null != itemData.getOnItemLongClickListener()) {
                itemData.getOnItemLongClickListener()
                    ?.onItemLongClick(holder.itemView, position, itemData.data)
            } else {
                mOnItemLongClickListener?.onItemLongClick(holder.itemView, position, itemData.data)
            }
            return@setOnLongClickListener res ?: false
        }
        itemData.mOnItemChildClickArray?.forEach { key, value ->
            holder.itemView.findViewById<View>(key)?.let { childView ->
                childView.setOnClickListener {
                    value.onItemClick(childView, position, itemData.data)
                }
            }
        }
        itemData.mOnItemChildLongClickArray?.forEach { key, value ->
            holder.itemView.findViewById<View>(key)?.let { childView ->
                childView.setOnClickListener {
                    value.onItemLongClick(childView, position, itemData.data)
                }
            }
        }
    }

    final override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemBindHolder<T> {
        val binding = DataBindingUtil.inflate<ViewDataBinding>(
            LayoutInflater.from(parent.context),
            viewType,
            parent,
            false
        )
        return setViewHolder(binding)
    }

    final override fun getItemViewType(position: Int): Int {
        val viewType = mItemList[position].layoutId
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
     * Adds [item] to the position specified by [position], with the layout
     * being [layout].
     *
     * @since 1.2.0
     */
    fun add(
        item: T,
        @LayoutRes layout: Int,
        position: Int = itemCount,
        scope: ItemWrapper<T>.() -> Unit = {}
    ) {
        var index = position
        if (isEmpty() && null != mEmptyItem) {
            if (0 != position - 1) {
                return
            }
            mItemList.remove(mEmptyItem)
            notifyItemRemoved(0)
            index = 0
        }
        if (index !in 0..itemCount) return
        mItemList.add(index, ItemWrapper(item, layout).also(scope))
        notifyItemInserted(index)
    }

    /**
     * Adds [items] to the position specified by [position], with the layout
     * being [layout].
     *
     * @since 1.2.0
     */
    fun add(
        items: List<T>,
        @LayoutRes layout: Int,
        position: Int = itemCount,
        scope: ItemWrapper<T>.() -> Unit = {}
    ) {
        var index = position
        if (isEmpty() && null != mEmptyItem) {
            if (0 != position - 1) {
                return
            }
            mItemList.remove(mEmptyItem)
            notifyItemRemoved(0)
            index = 0
        }
        if (index !in 0..itemCount) return
        mItemList.addAll(index, items.map { ItemWrapper(it, layout).also(scope) })
        notifyItemRangeInserted(index, items.size)
    }

    /**
     * Updates item at the specified [position] from the [mItemList].
     *
     * @since 1.2.0
     */
    fun update(
        item: T,
        @LayoutRes layout: Int,
        position: Int,
        scope: ItemWrapper<T>.() -> Unit = {}
    ): Boolean {
        if (isEmpty()) return false
        mItemList[position] = ItemWrapper(item, layout).also(scope)
        notifyItemChanged(position)
        return true
    }

    /**
     * Removes item at the specified [position] from the [mItemList].
     *
     * @since 1.2.0
     */
    fun removeAt(position: Int): T? {
        if (isEmpty() || position !in 0 until itemCount) {
            return null
        }
        val item = mItemList.removeAt(position)
            .apply { notifyItemRemoved(position) }.data
        if (isEmpty() && null != mEmptyItem) {
            mItemList.add(itemCount, mEmptyItem!!)
            notifyItemInserted(0)
        }
        return item
    }

    /**
     * Removes all elements from [mItemList].。
     *
     * @since 1.2.0
     */
    fun clear() {
        if (isEmpty()) return
        val count = itemCount
        mItemList.clear()
        notifyItemRangeRemoved(0, count)
        if (isEmpty() && null != mEmptyItem) {
            mItemList.add(itemCount, mEmptyItem!!)
            notifyItemInserted(0)
        }
    }

    /**
     * Query the index of the first element in [mItemList] that matches [data].
     *
     * @since 1.2.0
     */
    fun indexOfFirst(data: T) = if (isEmpty()) -1 else mItemList.indexOfFirst { it.data === data }

    /**
     * Custom empty layout. Specify the layout through [id] and specify the
     * click event related to the layout through [scope].
     *
     * @since 1.2.0
     */
    fun setEmptyView(@LayoutRes id: Int?, scope: ItemWrapper<T>.() -> Unit = {}) {
        if (null == id) {
            if (isEmpty() && null != mEmptyItem) {
                mItemList.removeAt(0)
                notifyItemRemoved(0)
            }
            mEmptyItem = null
            return
        }
        // If the current list is empty, remove the old mEmptyItem.
        if (isEmpty() && null != mEmptyItem) {
            mItemList.remove(mEmptyItem!!)
            notifyItemRemoved(0)
        }
        mEmptyItem = ItemWrapper<T>(null, id).also(scope)
        if (isEmpty()) {
            mItemList.add(mEmptyItem!!)
            notifyItemInserted(0)
        }
    }

    /**
     * [mItemList] will be judged to be empty when it is in the following two
     * situations:
     * 1. [mItemList] itself is empty.
     * 2. [mItemList] has only one element, and this element is [mEmptyItem].
     *
     * @since 1.2.0
     */
    private fun isEmpty() =
        mItemList.isEmpty() || (1 == mItemList.size && mEmptyItem === mItemList.first())

    /**
     * Returns [ItemBindHolder] by default. If you want to customize
     * [ItemBindHolder] you need to inherit [ItemBindHolder] and override the
     * [setViewHolder] method to use your customized [ItemBindHolder] as the
     * return value.
     */
    protected open fun setViewHolder(binding: ViewDataBinding): ItemBindHolder<T> {
        return ItemBindHolder(binding)
    }
}