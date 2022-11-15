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

package cn.govast.vastadapter.pagingadapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import cn.govast.vastadapter.AdapterClickListener
import cn.govast.vastadapter.AdapterClickRegister
import cn.govast.vastadapter.AdapterItem
import cn.govast.vastadapter.AdapterLongClickListener
import cn.govast.vastadapter.base.BaseBindHolder

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/11/11
// Description: 
// Documentation:
// Reference:

abstract class VastBindPagingAdapter <T: AdapterItem> constructor(
    protected var mContext: Context,
    protected val layoutId:Int,
    protected var diffCallback: DiffUtil.ItemCallback<T>
) : PagingDataAdapter<T, BaseBindHolder>(diffCallback), AdapterClickRegister {

    protected var onItemClickListener: AdapterClickListener? = null
    protected var onItemLongClickListener: AdapterLongClickListener? = null

    final override fun onBindViewHolder(holder: BaseBindHolder, position: Int) {
        val item = getItem(position)
        holder.onBindData(setVariableId(), item)
        holder.itemView.setOnClickListener {
            if (null != item?.getClickEvent()) {
                item.getClickEvent()?.onItemClick(holder.itemView, position)
            } else {
                onItemClickListener?.onItemClick(holder.itemView, position)
            }
        }
        holder.itemView.setOnLongClickListener {
            val res = if (null != item?.getLongClickEvent()) {
                item.getLongClickEvent()?.onItemLongClick(holder.itemView, position)
            } else {
                onItemLongClickListener?.onItemLongClick(holder.itemView, position)
            }
            return@setOnLongClickListener res ?: false
        }
    }

    final override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseBindHolder {
        val binding = DataBindingUtil.inflate<ViewDataBinding>(
            LayoutInflater.from(parent.context),
            viewType,
            parent,
            false
        )
        return setViewHolder(binding)
    }

    final override fun getItemViewType(position: Int):Int {
        return layoutId
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