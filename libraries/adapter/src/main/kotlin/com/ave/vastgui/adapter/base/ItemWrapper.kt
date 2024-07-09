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
import com.ave.vastgui.adapter.listener.OnItemChildClickListener
import com.ave.vastgui.adapter.listener.OnItemChildLongClickListener
import com.ave.vastgui.adapter.listener.OnItemClickListener
import com.ave.vastgui.adapter.listener.OnItemLongClickListener

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/12/23
// Documentation: https://ave.entropy2020.cn/documents/VastAdapter/

/**
 * 列表项数据包装器，用于为适配器提供布局 id ，点击事件等相关信息。
 *
 * @property layoutId 参考 [ItemType.layoutId] 。
 * @property clickListener 用于监听列表项点击事件。
 * @property longClickListener 用于监听列表项长按事件。
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
     * 用于为列表项内部的控件添加监听事件
     *
     * @param id 对应控件的 id 。
     * @since 1.1.1
     */
    fun addOnItemChildClickListener(@IdRes id: Int, listener: OnItemChildClickListener<T>) = apply {
        mOnItemChildClickArray =
            (mOnItemChildClickArray ?: SparseArray<OnItemChildClickListener<T>>(2)).apply {
                put(id, listener)
            }
    }

    /**
     * 用于移除 [id] 对应控件的监听事件。
     *
     * @since 1.1.1
     */
    fun removeOnItemChildClickListener(@IdRes id: Int) = apply {
        mOnItemChildClickArray?.remove(id)
    }

    /**
     * 用于为列表项内部的控件添加监听事件
     *
     * @param id 对应控件的 id 。
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
     * 用于移除 [id] 对应控件的监听事件。
     *
     * @since 1.1.1
     */
    fun removeOnItemChildLongClickListener(@IdRes id: Int) = apply {
        mOnItemChildLongClickArray?.remove(id)
    }

}