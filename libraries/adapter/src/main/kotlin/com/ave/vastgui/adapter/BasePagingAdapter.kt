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

package com.ave.vastgui.adapter

import android.content.Context
import android.content.res.Resources
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.core.util.forEach
import androidx.lifecycle.Lifecycle
import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.PagingDataAdapter
import androidx.paging.PagingSource
import androidx.paging.map
import com.ave.vastgui.adapter.base.ItemClickListener
import com.ave.vastgui.adapter.base.ItemDiffUtil
import com.ave.vastgui.adapter.base.ItemHolder
import com.ave.vastgui.adapter.base.ItemWrapper
import com.ave.vastgui.adapter.listener.OnItemClickListener
import com.ave.vastgui.adapter.listener.OnItemLongClickListener
import kotlinx.coroutines.flow.Flow

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/11/17
// Documentation: https://ave.entropy2020.cn/documents/adapter/

/**
 * [BasePagingAdapter] 。
 *
 * @since 1.2.0
 */
open class BasePagingAdapter<T : Any>(
    protected var mContext: Context,
    factories: MutableList<ItemHolder.HolderFactory<T>>,
    diffCallback: ItemDiffUtil<T>
) : PagingDataAdapter<ItemWrapper<T>, ItemHolder<T>>(diffCallback), ItemClickListener<T> {

    private val mType2Factory = SparseArray<ItemHolder.HolderFactory<T>>()
    private var mOnItemClickListener: OnItemClickListener<T>? = null
    private var mOnItemLongClickListener: OnItemLongClickListener<T>? = null

    final override fun onBindViewHolder(holder: ItemHolder<T>, position: Int) {
        val itemData = getItem(position) ?: return
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
        val item =
            getItem(position) ?: throw NullPointerException("Can't get the item by $position")
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
     * Present a [PagingData] until it is invalidated by a call to [refresh] or
     * [PagingSource.invalidate].
     *
     * This method is typically used when collecting from a [Flow] produced by [Pager]. For RxJava
     * or LiveData support, use the non-suspending overload of [submitData], which accepts a
     * [Lifecycle].
     *
     * Note: This method suspends while it is actively presenting page loads from a [PagingData],
     * until the [PagingData] is invalidated. Although cancellation will propagate to this call
     * automatically, collecting from a [Pager.flow] with the intention of presenting the most
     * up-to-date representation of your backing dataset should typically be done using
     * [collectLatest][kotlinx.coroutines.flow.collectLatest].
     *
     * @see [Pager]
     * @since 1.2.0
     */
    suspend fun submitData(
        pagingData: PagingData<T>,
        @LayoutRes id: Int,
        scope: ItemWrapper<T>.() -> Unit = {}
    ) {
        submitData(pagingData.map { ItemWrapper(it, id).also(scope) })
    }

    /**
     * Present a [PagingData] until it is either invalidated or another call to [submitData] is
     * made.
     *
     * This method is typically used when observing a RxJava or LiveData stream produced by [Pager].
     * For [Flow] support, use the suspending overload of [submitData], which automates cancellation
     * via [CoroutineScope][kotlinx.coroutines.CoroutineScope] instead of relying of [Lifecycle].
     *
     * @see submitData
     * @see [Pager]
     * @since 1.2.0
     */
    fun submitData(
        lifecycle: Lifecycle, pagingData: PagingData<T>,
        @LayoutRes id: Int,
        scope: ItemWrapper<T>.() -> Unit = {}
    ) {
        submitData(lifecycle, pagingData.map { ItemWrapper(it, id).also(scope) })
    }

    init {
        factories.forEach { factory ->
            mType2Factory.put(factory.layoutId, factory)
        }
    }

}