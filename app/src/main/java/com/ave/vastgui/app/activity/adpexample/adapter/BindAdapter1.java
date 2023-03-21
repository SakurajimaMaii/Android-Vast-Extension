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

package com.ave.vastgui.app.activity.adpexample.adapter;

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/2/27
// Description: 
// Documentation:
// Reference:

import android.content.Context;

import androidx.annotation.NonNull;

import com.ave.vastgui.adapter.VastBindAdapter;
import com.ave.vastgui.adapter.widget.AdapterItemWrapper;
import com.ave.vastgui.app.BR;

import java.util.List;

public class BindAdapter1 extends VastBindAdapter {

    public BindAdapter1(@NonNull List<AdapterItemWrapper<?>> mDataSource, @NonNull Context mContext) {
        super(mDataSource, mContext);
    }

    @Override
    public int setVariableId() {
        return BR.item;
    }

}
