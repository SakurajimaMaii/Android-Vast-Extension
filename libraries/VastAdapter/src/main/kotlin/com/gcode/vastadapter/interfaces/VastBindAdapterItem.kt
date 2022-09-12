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

import com.gcode.vastadapter.adapter.VastBindAdapter

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/3/30 7:34
// Description: Please make sure that your list class implement VastBindAdapterItem when you use VastBindAdapter.
// Documentation: [VastBindAdapter](https://sakurajimamaii.github.io/VastDocs/document/en/VastBindAdapter.html)

/**
 * Please make sure that your list class implement [VastBindAdapterItem]
 * when you use [VastBindAdapter]
 */
interface VastBindAdapterItem {

    /**
     * Item click event listener.
     */
    var vbAapClickEventListener:VAapClickEventListener?

    /**
     * Item long click event listener.
     */
    var vbAdpLongClickEventListener:VAdpLongClickEventListener?

    /**
     * @return The item layout resource id.
     */
    fun getVBAdpItemType(): Int

}