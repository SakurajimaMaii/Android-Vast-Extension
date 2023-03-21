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
import android.widget.ImageView
import com.ave.vastgui.adapter.base.BaseHolder
import com.ave.vastgui.adapter.widget.AdapterClickListener
import com.ave.vastgui.adapter.widget.AdapterItemWrapper
import com.ave.vastgui.adapter.widget.AdapterLongClickListener
import com.ave.vastgui.app.R
import com.ave.vastgui.core.extension.cast

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/1/20 15:54
// Description:
// Documentation:

class PictureWrapper(
    picture: Picture,
    clickListener: AdapterClickListener?,
    longClickListener: AdapterLongClickListener?
) : AdapterItemWrapper<Picture>(
    picture,
    "Picture",
    R.layout.item_bind_imageview,
    clickListener,
    longClickListener
)

class PictureHolder(itemView: View) : BaseHolder(itemView) {

    private val iv: ImageView

    override fun onBindData(item: Any?) {
        iv.setImageResource(cast<Picture>(item).drawable)
    }

    class Factory : HolderFactory {

        override fun onCreateHolder(
            parent: ViewGroup,
            viewType: Int
        ): BaseHolder {
            val inflater = LayoutInflater.from(parent.context)
            val itemView: View = inflater.inflate(R.layout.item_imageview, parent, false)
            return PictureHolder(itemView)
        }

        override fun getHolderType(): String {
            return Picture::class.java.simpleName
        }

    }

    init {
        iv = itemView.findViewById(R.id.item_image)
    }
}

data class Picture(val drawable: Int)
