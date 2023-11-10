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
import androidx.paging.PagingDataAdapter
import com.ave.vastgui.adapter.base.BaseBindHolder
import com.ave.vastgui.adapter.widget.AdapterClickListener
import com.ave.vastgui.adapter.widget.AdapterClickRegister
import com.ave.vastgui.adapter.widget.AdapterDiffUtil
import com.ave.vastgui.adapter.widget.AdapterItemWrapper
import com.ave.vastgui.adapter.widget.AdapterLongClickListener
import com.ave.vastgui.core.extension.cast

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/11/11

/**
 * Paging Adapter for RecyclerView with ViewBinding.
 *
 * @param T
 * @property mContext The context of adapter.
 * @property mDiffCallback
 */
abstract class VastBindPagingAdapter<T, R : AdapterItemWrapper<T>> constructor(
    protected var mContext: Context, protected val mDiffCallback: AdapterDiffUtil<T, R>
) : PagingDataAdapter<R, BaseBindHolder>(mDiffCallback), AdapterClickRegister {

    protected var onItemClickListener: AdapterClickListener? = null
    protected var onItemLongClickListener: AdapterLongClickListener? = null

    final override fun onBindViewHolder(holder: BaseBindHolder, position: Int) {
        val item = getItem(position)
        item?.apply {
            holder.onBindData(setVariableId(), cast<T>(this.getData()))
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
    }

    final override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): BaseBindHolder {
        val binding = DataBindingUtil.inflate<ViewDataBinding>(
            LayoutInflater.from(parent.context), viewType, parent, false
        )
        return setViewHolder(binding)
    }

    final override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        if (null != item) {
            // Maybe throw RuntimeException.
            val viewType = item.getLayoutId()
            try {
                // Identify whether there is a resource file for the resource id.
                mContext.resources.getLayout(viewType)
            } catch (e: Resources.NotFoundException) {
                throw RuntimeException(
                    "Please check if the return value of the getVBAdpItemType method in ${
                        item.javaClass.simpleName
                    } is correct."
                )
            }
            return viewType
        } else {
            throw NullPointerException("The item is null.")
        }
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