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
import com.ave.vastgui.adapter.BaseBindAdapter
import com.ave.vastgui.adapter.base.ItemWrapper
import com.ave.vastgui.app.BR
import com.ave.vastgui.app.R
import com.ave.vastgui.app.adapter.entity.Contact

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/9/25

class ContactAdapter(context: Context) :
    BaseBindAdapter<Contact>(context, BR.contact) {

    /**
     * 向列表中添加通讯录对象。
     *
     * @param name 通讯录名称。
     * @param number 通讯录电话。
     */
    fun addContact(name: String, number: String) {
        add(Contact(name, number), R.layout.item_contact)
    }

    /**
     * 向列表中添加通讯录对象并定义点击事件。
     *
     * @param name 通讯录名称。
     * @param number 通讯录电话。
     * @param scope 用于定义点击事件。
     */
    fun addContact(
        name: String,
        number: String,
        label: String,
        scope: ItemWrapper<Contact>.() -> Unit
    ) {
        add(Contact(name, number, label), R.layout.item_contact) { scope() }
    }

}