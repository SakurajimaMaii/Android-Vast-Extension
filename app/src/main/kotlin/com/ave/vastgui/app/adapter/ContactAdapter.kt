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

package com.ave.vastgui.app.adapter

import android.content.Context
import com.ave.vastgui.adapter.VastBindAdapter
import com.ave.vastgui.adapter.base.ItemWrapper
import com.ave.vastgui.app.BR
import com.ave.vastgui.app.R
import com.ave.vastgui.app.adapter.entity.Contact

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/9/25

class ContactAdapter(context: Context) :
    VastBindAdapter<Contact>(context, BR.contact) {

    fun getDataSource(): MutableList<ItemWrapper<Contact>> = mDataSource

    /**
     * 向列表中添加通讯录对象。
     *
     * @param name 通讯录名称。
     * @param number 通讯录电话。
     */
    fun addContact(name: String, number: String) {
        val index = itemCount
        mDataSource.add(index, ItemWrapper(Contact(name, number), layoutId = R.layout.item_contact))
        notifyItemChanged(index)
    }

    /**
     * 向列表中添加通讯录对象并定义点击事件。
     *
     * @param name 通讯录名称。
     * @param number 通讯录电话。
     * @param scope 用于定义点击事件。
     */
    inline fun addContact(
        name: String,
        number: String,
        label: String,
        scope: ItemWrapper<Contact>.() -> Unit
    ) {
        val index = itemCount
        val wrapper =
            ItemWrapper(Contact(name, number, label), layoutId = R.layout.item_contact).also(scope)
        getDataSource().add(index, wrapper)
        notifyItemChanged(index)
    }

    fun clearAll() {
        val count = itemCount
        mDataSource.clear()
        notifyItemRangeChanged(0, count)
    }

}