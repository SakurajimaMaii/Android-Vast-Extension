/*
 * Copyright 2022 VastGui
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gcode.vastutilsjava.basebindadpexample;

import android.content.Context;

import androidx.annotation.NonNull;

import com.gcode.vastadapter.adapter.VastBindAdapter;
import com.gcode.vastadapter.interfaces.VastBindAdapterItem;
import com.gcode.vastutilsjava.BR;

import java.util.ArrayList;
import java.util.List;

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/3/31 14:11
// Description:
// Documentation:

public class BaseBindAdapter extends VastBindAdapter {

    public BaseBindAdapter(@NonNull List<VastBindAdapterItem> dataSource,@NonNull Context mContext) {
        super(dataSource,mContext);
    }

    @Override
    public int setVariableId() {
        return BR.item;
    }

    public Boolean isEmpty(){
        return getDataSource().isEmpty();
    }

}