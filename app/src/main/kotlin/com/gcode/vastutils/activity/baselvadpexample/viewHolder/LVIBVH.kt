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

package com.gcode.vastutils.activity.baselvadpexample.viewHolder

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.gcode.vasttools.adapter.VastBaseAdapter
import com.gcode.vastutils.R
import com.gcode.vastutils.activity.baselvadpexample.model.LVIB


// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/6/20
// Description:
// Documentation:

class LVIBVH(val item: View) :
    VastBaseAdapter.VastBaseViewHolder(item) {

    private val tv: TextView = itemView.findViewById(R.id.lvib_content)

    override fun bindData(item: VastBaseAdapter.BaseItem) {
        tv.text = (item as LVIB).string
    }

    class Factory : BaseFactory {

        override fun getViewHolder(
            context: Context,
            convertView: View?,
            parent: ViewGroup?
        ): VastBaseAdapter.VastBaseViewHolder {
            val view = LayoutInflater.from(context).inflate(R.layout.lv_item_lvib, null)
            return LVIBVH(view)
        }

        override fun getItemType(): String {
            return LVIB::class.java.simpleName
        }

    }

}