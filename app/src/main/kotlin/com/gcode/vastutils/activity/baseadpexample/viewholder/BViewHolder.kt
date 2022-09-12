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

package com.gcode.vastutils.activity.baseadpexample.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.gcode.vastadapter.adapter.VastAdapterVH
import com.gcode.vastadapter.interfaces.VastAdapterItem
import com.gcode.vastutils.R
import com.gcode.vastutils.activity.baseadpexample.model.BExample

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/1/19
// Description:
// Documentation:

class BViewHolder(itemView: View) : VastAdapterVH(itemView) {

    private val iv: ImageView

    override fun onBindData(item: VastAdapterItem) {
        super.onBindData(item)
        iv.setImageResource((item as BExample).drawable)
    }

    class Factory : BVAdpVHFactory {

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): VastAdapterVH {
            val inflater = LayoutInflater.from(parent.context)
            val itemView: View = inflater.inflate(R.layout.item_imageview, parent, false)
            return BViewHolder(itemView)
        }

        override fun getVAdpVHType(): String {
            return BExample::class.java.simpleName
        }

    }

    init {
        iv = itemView.findViewById(R.id.item_image)
    }
}