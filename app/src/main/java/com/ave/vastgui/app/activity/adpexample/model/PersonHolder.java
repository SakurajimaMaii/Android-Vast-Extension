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

package com.ave.vastgui.app.activity.adpexample.model;

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/2/27
// Description: 
// Documentation:
// Reference:

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.ave.vastgui.adapter.base.BaseHolder;
import com.ave.vastgui.app.R;
import com.ave.vastgui.core.extension.Cast;
import com.google.android.material.textview.MaterialTextView;

public class PersonHolder extends BaseHolder {

    private final MaterialTextView firstName;
    private final MaterialTextView lastName;
    private View itemView;

    public PersonHolder(@NonNull View itemView) {
        super(itemView);
        firstName = itemView.findViewById(R.id.firstName);
        lastName = itemView.findViewById(R.id.lastName);
    }

    @Override
    public void onBindData(@NonNull Object item) {
        try {
            Person person = Cast.cast(item);
            firstName.setText(person.getFirstName());
            lastName.setText(person.getLastName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public final static class Factory implements HolderFactory {

        public Factory() {
        }

        @NonNull
        @Override
        public BaseHolder onCreateHolder(@NonNull ViewGroup parent, int viewType) {
            return new PersonHolder(
                    LayoutInflater.from(parent.getContext()).inflate(R.layout.item_textview, parent, false)
            );
        }

        @NonNull
        @Override
        public String getHolderType() {
            return Person.class.getSimpleName();
        }

    }

}
