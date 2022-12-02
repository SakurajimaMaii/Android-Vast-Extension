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

package cn.govast.vastadapter.base

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cn.govast.vastadapter.AdapterItem

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/1/17
// Description: VastAdapterVH help you to create a recyclerView adapter viewHolder.
// Documentation: [VastAdapter](https://sakurajimamaii.github.io/VastDocs/document/en/VastAdapter.html)

abstract class BaseHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    abstract fun onBindData(item: AdapterItem)

    interface HolderFactory{
        /**
         * Create the current ViewHolder instance.
         */
        fun onCreateHolder(parent: ViewGroup, viewType:Int): BaseHolder

        /**
         * Return a string which is same as the value you set in [AdapterItem.getHolderType].
         * In this way, the data item can be matched to the corresponding ViewHolder.
         */
        fun getHolderType():String
    }
}