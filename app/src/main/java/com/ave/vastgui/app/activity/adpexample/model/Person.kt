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

package com.ave.vastgui.app.activity.adpexample.model

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ave.vastgui.adapter.base.BaseHolder
import com.ave.vastgui.adapter.widget.AdapterItemWrapper
import com.ave.vastgui.app.R
import com.ave.vastgui.core.extension.cast
import com.ave.vastgui.tools.extension.MapKey
import com.google.android.material.textview.MaterialTextView

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2021/4/2 15:54
// Description:
// Documentation:

class PersonWrapper(
    mData: Person
) : AdapterItemWrapper<Person>(mData, "Person", R.layout.item_bind_textview)

class PersonHolder(itemView: View) : BaseHolder(itemView) {

    private val firstName: MaterialTextView
    private val lastName: MaterialTextView

    override fun onBindData(item: Any) {
        firstName.text = cast<Person>(item).firstName
        lastName.text = cast<Person>(item).lastName
    }

    class Factory : HolderFactory {
        override fun onCreateHolder(parent: ViewGroup, viewType: Int): BaseHolder {
            return PersonHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_textview, parent, false)
            )
        }

        override fun getHolderType(): String {
            return Person::class.java.simpleName
        }
    }

    init {
        firstName = itemView.findViewById(R.id.firstName)
        lastName = itemView.findViewById(R.id.lastName)
    }
}

class Person constructor(
    @MapKey("fn") val firstName: String,
    val lastName: String
)