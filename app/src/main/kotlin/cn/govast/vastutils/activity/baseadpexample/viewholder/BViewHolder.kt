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
import android.widget.ImageView
import cn.govast.vastadapter.AdapterItem
import cn.govast.vastadapter.base.BaseViewHolder
import cn.govast.vasttools.extension.cast
import cn.govast.vastutils.R
import cn.govast.vastutils.activity.baseadpexample.model.BExample

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/1/19
// Description:
// Documentation:

class BViewHolder(itemView: View) : BaseViewHolder(itemView) {

    private val iv: ImageView

    override fun onBindData(item: AdapterItem) {
        super.onBindData(item)
        iv.setImageResource(cast<BExample>(item).drawable)
    }

    class Factory : BVAdpVHFactory {

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): BaseViewHolder {
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