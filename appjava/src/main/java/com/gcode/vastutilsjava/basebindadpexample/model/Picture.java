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

package com.gcode.vastutilsjava.basebindadpexample.model;

import androidx.annotation.Nullable;

import com.gcode.vastadapter.interfaces.VAapClickEventListener;
import com.gcode.vastadapter.interfaces.VAdpLongClickEventListener;
import com.gcode.vastadapter.interfaces.VastBindAdapterItem;
import com.gcode.vastutilsjava.R;

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/3/31 13:57
// Description:
// Documentation:

public class Picture implements VastBindAdapterItem {

    private int drawable;
    private VAapClickEventListener clickEventListener;
    private VAdpLongClickEventListener longClickEventListener;

    public Picture(int drawable, VAapClickEventListener clickEventListener, VAdpLongClickEventListener longClickEventListener) {
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
    public int getVBAdpItemType() {
        return R.layout.item_bind_imageview;
    }


    @Nullable
    @Override
    public VAapClickEventListener getVbAapClickEventListener() {
        return clickEventListener;
    }

    @Override
    public void setVbAapClickEventListener(@Nullable VAapClickEventListener value) {
        clickEventListener = value;
    }

    @Nullable
    @Override
    public VAdpLongClickEventListener getVbAdpLongClickEventListener() {
        return longClickEventListener;
    }

    @Override
    public void setVbAdpLongClickEventListener(@Nullable VAdpLongClickEventListener value) {
        longClickEventListener = value;
    }
}