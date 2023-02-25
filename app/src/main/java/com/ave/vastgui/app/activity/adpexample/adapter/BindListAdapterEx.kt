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

package com.ave.vastgui.app.activity.adpexample.adapter

import android.content.Context
import com.ave.vastgui.adapter.VastBindListAdapter
import com.ave.vastgui.adapter.widget.AdapterDiffUtil
import com.ave.vastgui.app.BR
import com.ave.vastgui.app.activity.adpexample.model.Person

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/1/21
// Description: 
// Documentation:
// Reference:

class BindListAdapterEx(context: Context): VastBindListAdapter<Person>(context, PersonDiffUtil()){

    class PersonDiffUtil : AdapterDiffUtil<Person>() {
        override fun newAreItemsTheSame(oldItem: Person, newItem: Person): Boolean {
            return oldItem.firstName == newItem.firstName && oldItem.lastName == newItem.lastName
        }

        override fun newAreContentsTheSame(oldItem: Person, newItem: Person): Boolean {
            return oldItem.firstName == newItem.firstName && oldItem.lastName == newItem.lastName
        }
    }

    override fun setVariableId(): Int {
        return BR.item
    }

}