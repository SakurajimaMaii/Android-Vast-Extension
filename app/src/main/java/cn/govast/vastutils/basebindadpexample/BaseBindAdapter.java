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

package cn.govast.vastutils.basebindadpexample;

import android.content.Context;

import androidx.annotation.NonNull;

import java.util.List;

import cn.govast.vastadapter.AdapterItem;
import cn.govast.vastadapter.recycleradpter.VastBindAdapter;
import cn.govast.vastutils.BR;

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/3/31 14:11
// Description:
// Documentation:

public class BaseBindAdapter extends VastBindAdapter {

    public BaseBindAdapter(@NonNull List<AdapterItem> dataSource, @NonNull Context mContext) {
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