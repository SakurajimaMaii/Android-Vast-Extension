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

package com.ave.vastgui.adapter.base

import android.util.SparseArray
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.ave.vastgui.adapter.listener.OnItemChildClickListener
import com.ave.vastgui.adapter.listener.OnItemChildLongClickListener
import com.ave.vastgui.adapter.listener.OnItemClickListener
import com.ave.vastgui.adapter.listener.OnItemLongClickListener

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/12/23
// Documentation: https://ave.entropy2020.cn/documents/VastAdapter/

/**
 * List item wrapper, used to provide layout id, click events and other
 * related information to the adapter.
 *
 * @property layoutId [RecyclerView.Adapter] can find the layout and
 * [RecyclerView.ViewHolder] corresponding to the item through [layoutId].
 * @since 1.1.1
 */
class ItemWrapper<T : Any> @JvmOverloads constructor(
    val data: T?,
    @LayoutRes override val layoutId: Int,
    private var clickListener: OnItemClickListener<T>? = null,
    private var longClickListener: OnItemLongClickListener<T>? = null
) : ItemType, ItemClickListener<T> {

    internal var mOnItemChildClickArray: SparseArray<OnItemChildClickListener<T>>? = null
    internal var mOnItemChildLongClickArray: SparseArray<OnItemChildLongClickListener<T>>? = null

    override fun setOnItemClickListener(listener: OnItemClickListener<T>?) {
        clickListener = listener
    }

    override fun getOnItemClickListener(): OnItemClickListener<T>? {
        return clickListener
    }

    override fun setOnItemLongClickListener(listener: OnItemLongClickListener<T>?) {
        longClickListener = listener
    }

    override fun getOnItemLongClickListener(): OnItemLongClickListener<T>? {
        return longClickListener
    }

    /**
     * Register a callback to be invoked when this [id] corresponding view is
     * clicked.
     *
     * @since 1.1.1
     */
    fun addOnItemChildClickListener(@IdRes id: Int, listener: OnItemChildClickListener<T>) = apply {
        mOnItemChildClickArray =
            (mOnItemChildClickArray ?: SparseArray<OnItemChildClickListener<T>>(2)).apply {
                put(id, listener)
            }
    }

    /**
     * Remove a callback to be invoked when this [id] corresponding view is
     * clicked.
     *
     * @since 1.1.1
     */
    fun removeOnItemChildClickListener(@IdRes id: Int) = apply {
        mOnItemChildClickArray?.remove(id)
    }

    /**
     * Register a callback to be invoked when this [id] corresponding view is
     * clicked and held.
     *
     * @since 1.1.1
     */
    fun addOnItemChildLongClickListener(@IdRes id: Int, listener: OnItemChildLongClickListener<T>) =
        apply {
            mOnItemChildLongClickArray =
                (mOnItemChildLongClickArray
                    ?: SparseArray<OnItemChildLongClickListener<T>>(2)).apply {
                    put(id, listener)
                }
        }

    /**
     * Remove a callback to be invoked when this [id] corresponding view is
     * clicked and held.
     *
     * @since 1.1.1
     */
    fun removeOnItemChildLongClickListener(@IdRes id: Int) = apply {
        mOnItemChildLongClickArray?.remove(id)
    }

}