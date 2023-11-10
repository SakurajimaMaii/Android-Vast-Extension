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

package com.ave.vastgui.adapter.widget

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/12/23
// Documentation: [AdapterItemWrapper](https://ave.entropy2020.cn/documents/VastAdapter/Widget/)

/**
 * The wrapper of the adapter item.
 *
 * @property mHolderType this string value should be the same as the
 *     [BaseHolder.HolderFactory.getHolderType].
 * @property mLayoutId layout id for the adapter item.
 * @property c Click listener.
 * @property lc Long click listener.
 */
open class AdapterItemWrapper<out T> @JvmOverloads constructor(
    protected val mData: T,
    protected val mHolderType: String? = null,
    protected val mLayoutId: Int? = null,
    protected var c: AdapterClickListener? = null,
    protected var lc: AdapterLongClickListener? = null
) : AdapterItem {

    fun getData(): T = mData

    override fun registerClickEvent(l: AdapterClickListener?) {
        c = l
    }

    override fun getClickEvent(): AdapterClickListener? {
        return c
    }

    override fun registerLongClickEvent(l: AdapterLongClickListener?) {
        lc = l
    }

    override fun getLongClickEvent(): AdapterLongClickListener? {
        return lc
    }

    final override fun getLayoutId(): Int {
        return mLayoutId ?: super.getLayoutId()
    }

    final override fun getHolderType(): String {
        return mHolderType ?: super.getHolderType()
    }

}