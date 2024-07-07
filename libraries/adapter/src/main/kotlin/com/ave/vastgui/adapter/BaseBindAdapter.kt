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
 * @since 1.1.1
 */
open class BaseBindAdapter<T> @JvmOverloads constructor(
    protected var mContext: Context,
    /**
     * 设置变量的id，如果在布局文件中内容以下所示：
     *
     * ```xml
     * <data>
     *     <variable
     *          name="person"
     *          type="com.example.gutilssampledemo.Person" />
     * </data>
     * ```
     *
     * 则 [mVariableId] 的值为 person
     */
    private val mVariableId: Int,
    protected var mDataSource: MutableList<ItemWrapper<T>> = mutableListOf()
) : RecyclerView.Adapter<ItemBindHolder<T>>(), ItemClickListener<T> {

    private var mOnItemClickListener: OnItemClickListener<T>? = null
    private var mOnItemLongClickListener: OnItemLongClickListener<T>? = null

    final override fun getItemCount() = mDataSource.size

    final override fun onBindViewHolder(holder: ItemBindHolder<T>, position: Int) {
        val itemData = mDataSource[position]
        holder.onBindData(mVariableId, itemData.data)
        holder.itemView.setOnClickListener {
            if (null != itemData.getOnItemClickListener()) {
                itemData.getOnItemClickListener()?.onItemClick(holder.itemView, position, itemData)
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

    /** @return 您要设置的 ViewHolder 。 */
    protected open fun setViewHolder(binding: ViewDataBinding): ItemBindHolder<T> {
        return ItemBindHolder(binding)
    }
}