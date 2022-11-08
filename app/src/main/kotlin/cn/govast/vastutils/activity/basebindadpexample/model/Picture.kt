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

package cn.govast.vastutils.activity.basebindadpexample.model

import cn.govast.vastadapter.AdapterClickListener
import cn.govast.vastadapter.AdapterItem
import cn.govast.vastadapter.AdapterLongClickListener
import cn.govast.vastutils.R

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/1/20 15:54
// Description:
// Documentation:

class Picture(
    val drawable: Int,
    private val clickListener: AdapterClickListener?,
    private val longClickListener: AdapterLongClickListener?
) : AdapterItem {

    override fun getBindType(): Int {
        return R.layout.item_bind_imageview
    }

    override fun getClickEvent(): AdapterClickListener? {
        return clickListener
    }

    override fun getLongClickEvent(): AdapterLongClickListener? {
        return longClickListener
    }

}