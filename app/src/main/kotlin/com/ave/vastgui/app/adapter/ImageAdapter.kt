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
import com.ave.vastgui.adapter.BR
import com.ave.vastgui.adapter.VastAdapter
import com.ave.vastgui.adapter.VastBindAdapter
import com.ave.vastgui.adapter.base.ItemWrapper
import com.ave.vastgui.app.R
import com.ave.vastgui.app.adapter.entity.Images

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2024/1/6 20:45

class ImageAdapter(context: Context) :
    VastBindAdapter<Images.Image>(context, BR.image) {
    fun addImage(image: Images.Image) {
        val index = itemCount
        mDataSource.add(index, ItemWrapper(image, layoutId = R.layout.item_image_default))
        notifyItemChanged(index)
    }

    fun clearAll() {
        val count = itemCount
        mDataSource.clear()
        notifyItemRangeChanged(0, count)
    }
}