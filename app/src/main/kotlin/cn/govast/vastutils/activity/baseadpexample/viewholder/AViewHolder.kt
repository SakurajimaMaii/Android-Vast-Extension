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

package cn.govast.vastutils.activity.baseadpexample.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import cn.govast.vastadapter.AdapterItem
import cn.govast.vastadapter.base.BaseViewHolder
import cn.govast.vasttools.extension.cast
import cn.govast.vastutils.R
import cn.govast.vastutils.activity.baseadpexample.model.AExample

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/1/19
// Description:
// Documentation:

class AViewHolder(itemView: View): BaseViewHolder(itemView) {
    private val tv:TextView

    override fun onBindData(item: AdapterItem) {
        super.onBindData(item)
        tv.text = cast<AExample>(item).data
    }

    class Factory:BVAdpVHFactory{
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
            return AViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_textview,parent,false))
        }

        override fun getVAdpVHType(): String {
            return AExample::class.java.simpleName
        }
    }

    init {
        tv = itemView.findViewById(R.id.text)
    }
}