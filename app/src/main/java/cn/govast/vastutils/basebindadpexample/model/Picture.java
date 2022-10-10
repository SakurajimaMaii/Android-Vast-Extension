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

package cn.govast.vastutils.basebindadpexample.model;

import androidx.annotation.Nullable;

import cn.govast.vastadapter.AdapterClickListener;
import cn.govast.vastadapter.AdapterItem;
import cn.govast.vastadapter.AdapterLongClickListener;
import cn.govast.vastutils.R;

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/3/31 13:57
// Description:
// Documentation:

public class Picture implements AdapterItem {

    private int drawable;
    private AdapterClickListener clickEventListener;
    private AdapterLongClickListener longClickEventListener;

    public Picture(int drawable, AdapterClickListener clickEventListener, AdapterLongClickListener longClickEventListener) {
        this.drawable = drawable;
        this.clickEventListener = clickEventListener;
        this.longClickEventListener = longClickEventListener;
    }

    public int getDrawable() {
        return drawable;
    }

    public void setDrawable(int drawable) {
        this.drawable = drawable;
    }

    @Override
    public int getBindType() {
        return R.layout.item_bind_imageview;
    }

    @Override
    public void registerClickEvent(@Nullable AdapterClickListener l) {
        clickEventListener = l;
    }

    @Nullable
    @Override
    public AdapterClickListener getClickEvent() {
        return clickEventListener;
    }

    @Override
    public void registerLongClickEvent(@Nullable AdapterLongClickListener l) {
        longClickEventListener = l;
    }

    @Nullable
    @Override
    public AdapterLongClickListener getLongClickEvent() {
        return longClickEventListener;
    }
}