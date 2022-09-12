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

import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.gcode.vastadapter.interfaces.VastBindAdapterItem

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
 * For more settings, please refer to the documentation.
 *
 * @property dataSource data source.
 * @property mContext [Context].
 *
 * @since 0.0.1
 */
abstract class VastBindAdapter constructor(
    protected var dataSource: MutableList<VastBindAdapterItem>,
    protected var mContext: Context
) : RecyclerView.Adapter<VastBindAdapter.BindingHolder>() {

    /**
     * Register click listener for item in RecyclerView.
     *
     * @see VastBindAdapter.setOnItemClickListener
     * @since 0.0.4
     */
    private var onItemClickListener: OnItemClickListener? = null

    /**
     * Register long click listener for item in RecyclerView.
     *
     * @see VastBindAdapter.setOnItemLongClickListener
     * @since 0.0.4
     */
    private var onItemLongClickListener: OnItemLongClickListener? = null

    /**
     * @since 0.0.1
     */
    final override fun onBindViewHolder(holder: BindingHolder, position: Int) {
        val item = dataSource[position]
        holder.bindData(setVariableId(), item)
        holder.itemView.setOnClickListener {
            if (null != item.vbAapClickEventListener) {
                item.vbAapClickEventListener!!.vAapClickEvent(holder.itemView,position)
            } else {
                onItemClickListener?.onItemClick(holder.itemView, position)
            }
        }
        holder.itemView.setOnLongClickListener {
            val res = if (null != item.vbAdpLongClickEventListener) {
                item.vbAdpLongClickEventListener!!.vAdpLongClickEvent(holder.itemView, position)
            } else {
                onItemLongClickListener?.onItemLongClick(holder.itemView, position)
            }
            return@setOnLongClickListener res ?: false
        }
    }

    /**
     * @since 0.0.1
     */
    final override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingHolder {
        val binding = DataBindingUtil.inflate<ViewDataBinding>(
            LayoutInflater.from(parent.context),
            viewType,
            parent,
            false
        )
        return setViewHolder(binding)
    }

    /**
     * @since 0.0.1
     */
    final override fun getItemCount() = dataSource.size

    /**
     * @since 0.0.1
     */
    final override fun getItemViewType(position: Int):Int {
        val viewType = dataSource[position].getVBAdpItemType()
        try {
            // Identify whether there is a resource file for the resource id.
            mContext.resources.getLayout(viewType)
        }catch(e:Resources.NotFoundException){
            throw RuntimeException("Please check if the return value of the getVBAdpItemType method in ${dataSource[position].javaClass.simpleName} is correct.")
        }
        return viewType
    }

    /**
     * Set VariableId value.For example, in the layout file
     * ```xml
     * <data>
     *     <variable
     *     name="item"
     *     type="com.example.gutilssampledemo.Person" />
     * </data>
     * ```
     *
     * Then the [setVariableId] should be like this:
     *
     * ```kt
     * override fun setVariableId(): Int {
     *      return BR.item
     * }
     * ```
     *
     * @return Int
     */
    abstract fun setVariableId(): Int

    /**
     * The ViewHolder of the [VastBindAdapter].If you want
     * to set your own ViewHolder for [VastBindAdapter],you
     * should making your ViewHolder extends the [BindingHolder]
     * and make [setViewHolder] return it.
     *
     * @property binding
     *
     * @since 0.0.1
     */
    open class BindingHolder(protected var binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindData(variableId: Int, item: VastBindAdapterItem?) {
            binding.setVariable(variableId, item)
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

    /**
     * @return The ViewHolder you want to set.
     *
     * @since 0.0.6
     */
    open fun setViewHolder(binding:ViewDataBinding): BindingHolder {
        return BindingHolder(binding)
    }
}