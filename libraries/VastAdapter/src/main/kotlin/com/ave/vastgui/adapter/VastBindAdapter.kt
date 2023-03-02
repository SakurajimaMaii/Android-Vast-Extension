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

import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.ave.vastgui.adapter.base.BaseAdapter
import com.ave.vastgui.adapter.base.BaseBindHolder
import com.ave.vastgui.adapter.widget.AdapterItemWrapper

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2021/4/2
// Description: VastBindAdapter help you to create a recyclerView adapter.
// Documentation: [VastBindAdapter](https://sakurajimamaii.github.io/VastDocs/document/en/VastBindAdapter.html)

/**
 * VastBindAdapter.
 *
 * Here is an example in kotlin:
 * ```kotlin
 * class BaseBindingAdapter(
 *     dataSource: MutableList<VastBindAdapterItem>,
 *     mContext: Context
 * ) : VastBindAdapter(dataSource, mContext)
 * ```
 *
 * For more settings, please refer to the documentation.
 *
 * @property mDataSource data source.
 * @property mContext [Context].
 */
abstract class VastBindAdapter constructor(
    protected var mDataSource: MutableList<AdapterItemWrapper<*>>,
    protected var mContext: Context
) : BaseAdapter<BaseBindHolder>() {

    final override fun onBindViewHolder(holder: BaseBindHolder, position: Int) {
        val item = mDataSource[position]
        holder.onBindData(setVariableId(), item.getData())
        holder.itemView.setOnClickListener {
            if (null != item.getClickEvent()) {
                item.getClickEvent()?.onItemClick(holder.itemView, position)
            } else {
                onItemClickListener?.onItemClick(holder.itemView, position)
            }
        }
        holder.itemView.setOnLongClickListener {
            val res = if (null != item.getLongClickEvent()) {
                item.getLongClickEvent()?.onItemLongClick(holder.itemView, position)
            } else {
                onItemLongClickListener?.onItemLongClick(holder.itemView, position)
            }
            return@setOnLongClickListener res ?: false
        }
    }

    final override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseBindHolder {
        val binding = DataBindingUtil.inflate<ViewDataBinding>(
            LayoutInflater.from(parent.context),
            viewType,
            parent,
            false
        )
        return setViewHolder(binding)
    }

    final override fun getItemCount() = mDataSource.size

    final override fun getItemViewType(position: Int): Int {
        val viewType = mDataSource[position].getLayoutId()
        try {
            mContext.resources.getLayout(viewType)
        } catch (e: Resources.NotFoundException) {
            throw RuntimeException("Please check if the return value of the getVBAdpItemType method in ${mDataSource[position].javaClass.simpleName} is correct.")
        }
        return viewType
    }

    /**
     * Set VariableId value.For example, in the layout file
     *
     * ```xml
     * <data>
     *     <variable
     *     name="item"
     *     type="com.example.gutilssampledemo.Person" />
     * </data>
     * ```
     *
     * Then the [setVariableId] should be like this:
     * ```kt
     * override fun setVariableId(): Int {
     *      return BR.item
     * }
     * ```
     *
     * @return Int
     */
    protected abstract fun setVariableId(): Int

    /** @return The ViewHolder you want to set. */
    protected open fun setViewHolder(binding: ViewDataBinding): BaseBindHolder {
        return BaseBindHolder(binding)
    }
}