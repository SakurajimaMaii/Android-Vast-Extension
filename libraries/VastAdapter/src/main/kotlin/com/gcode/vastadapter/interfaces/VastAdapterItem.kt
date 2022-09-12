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

package com.gcode.vastadapter.interfaces

import com.gcode.vastadapter.adapter.VastAdapter
import com.gcode.vastadapter.adapter.VastAdapterVH

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/1/19
// Description: Please make sure that your list class implement VastAdapterItem when you use VastAdapter.
// Documentation: [VastAdapter](https://sakurajimamaii.github.io/VastDocs/document/en/VastAdapter.html)

/**
 * Please make sure that your list class implement [VastAdapterItem]
 * when you use [VastAdapter]
 */
interface VastAdapterItem {

    /**
     * Item click event listener.
     */
    var vAapClickEventListener:VAapClickEventListener?

    /**
     * Item long click event listener.
     */
    var vAdpLongClickEventListener:VAdpLongClickEventListener?


    /**
     * @return A string which is same as the value you set in [VastAdapterVH.BVAdpVHFactory.getVAdpVHType].
     *         In this way, the data item can be matched to the corresponding ViewHolder.
     */
    fun getVAdpItemType(): String

}