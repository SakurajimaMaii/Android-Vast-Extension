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

package com.ave.vastgui.app.activity.adpexample.widget;

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/3/3
// Description: 
// Documentation:
// Reference:

import com.ave.vastgui.adapter.widget.AdapterDiffUtil;
import com.ave.vastgui.app.activity.adpexample.model.Person;
import com.ave.vastgui.app.activity.adpexample.model.PersonWrapper;

import java.util.Objects;

public class PersonDiffUtil extends AdapterDiffUtil<Person, PersonWrapper> {

    public PersonDiffUtil() {
    }

    @Override
    public boolean newAreContentsTheSame(Person oldItem, Person newItem) {
        return Objects.equals(oldItem.getFirstName(), newItem.getFirstName()) && Objects.equals(oldItem.getLastName(), newItem.getLastName());
    }

    @Override
    public boolean newAreItemsTheSame(Person oldItem, Person newItem) {
        return Objects.equals(oldItem.getFirstName(), newItem.getFirstName()) && Objects.equals(oldItem.getLastName(), newItem.getLastName());
    }

}
