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
import androidx.annotation.LayoutRes
import androidx.core.util.forEach
import androidx.recyclerview.widget.ListAdapter
import com.ave.vastgui.adapter.base.EmptyHolderFactory
import com.ave.vastgui.adapter.base.ItemClickListener
import com.ave.vastgui.adapter.base.ItemDiffUtil
import com.ave.vastgui.adapter.base.ItemHolder
import com.ave.vastgui.adapter.base.ItemWrapper
import com.ave.vastgui.adapter.listener.OnItemClickListener
import com.ave.vastgui.adapter.listener.OnItemLongClickListener

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/10/10
// Documentation: https://ave.entropy2020.cn/documents/VastAdapter/

/**
 * [BaseListAdapter].
 *
 * @since 1.1.1
 */
open class BaseListAdapter<T : Any>(
    protected var mContext: Context,
    factories: MutableList<ItemHolder.HolderFactory<T>>,
    diffCallback: ItemDiffUtil<T>
) : ListAdapter<ItemWrapper<T>, ItemHolder<T>>(diffCallback), ItemClickListener<T> {

    private val mType2Factory = SparseArray<ItemHolder.HolderFactory<T>>()
    private var mOnItemClickListener: OnItemClickListener<T>? = null
    private var mOnItemLongClickListener: OnItemLongClickListener<T>? = null
    private var mEmptyItem: ItemWrapper<T>? = null

    final override fun onBindViewHolder(holder: ItemHolder<T>, position: Int) {
        val itemData = getItem(position)
        itemData.data?.apply { holder.onBindData(this) }
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

    final override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder<T> {
        mType2Factory.forEach { key, factory ->
            if (key == viewType) {
                return factory.onCreateHolder(parent, viewType)
            }
        }
        throw RuntimeException("Not found the factory according to the $viewType.")
    }

    final override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        try {
            mContext.resources.getLayout(item.layoutId)
        } catch (e: Resources.NotFoundException) {
            throw IllegalArgumentException("Please check if the return layoutId is correct.")
        }
        return item.layoutId
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
     * Set the new list to be displayed.
     *
     * @since 1.2.0
     */
    fun submitList(
        items: List<T>,
        @LayoutRes id: Int? = null,
        commitCallback: Runnable? = null,
        scope: ItemWrapper<T>.() -> Unit = {}
    ) {
        if (null == id && items.isNotEmpty()) {
            throw IllegalArgumentException("id is allowed to be empty only if items is an empty list.")
        }
        if (items.isEmpty() && null != mEmptyItem) {
            super.submitList(listOf(mEmptyItem!!))
        } else {
            super.submitList(items.map { ItemWrapper(it, id!!).also(scope) }, commitCallback)
        }
    }

    override fun submitList(list: List<ItemWrapper<T>>?) {
        throw RuntimeException("Please call submitList(items: List<T>,id: Int,commitCallback: Runnable,scope: ItemWrapper<T>.() -> Unit)")
    }

    override fun submitList(list: List<ItemWrapper<T>>?, commitCallback: Runnable?) {
        throw RuntimeException("Please call submitList(items: List<T>,id: Int,commitCallback: Runnable,scope: ItemWrapper<T>.() -> Unit)")
    }

    /**
     * Custom empty layout. Specify the layout through [id] and specify the
     * click event related to the layout through [scope].
     *
     * @since 1.2.0
     */
    fun setEmptyView(@LayoutRes id: Int?, scope: ItemWrapper<T>.() -> Unit = {}) {
        mEmptyItem?.apply { mType2Factory.remove(layoutId) }
        mEmptyItem = if (null != id) {
            mType2Factory.put(id, EmptyHolderFactory(id))
            ItemWrapper<T>(null, id).also(scope)
        } else {
            null
        }
        if (0 == itemCount) {
            submitList(emptyList<T>())
        } else if (1 == itemCount && null != getItem(0)) {
            submitList(emptyList<T>())
        }
    }

    init {
        factories.forEach { factory ->
            mType2Factory.put(factory.layoutId, factory)
        }
    }

}