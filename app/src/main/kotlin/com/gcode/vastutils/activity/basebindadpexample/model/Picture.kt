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

package com.gcode.vastutils.activity.basebindadpexample.model

import com.gcode.vastadapter.interfaces.VAapClickEventListener
import com.gcode.vastadapter.interfaces.VAdpLongClickEventListener
import com.gcode.vastadapter.interfaces.VastBindAdapterItem
import com.gcode.vastutils.R

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/1/20 15:54
// Description:
// Documentation:

class Picture(
    val drawable: Int,
    override var vbAapClickEventListener: VAapClickEventListener?,
    override var vbAdpLongClickEventListener: VAdpLongClickEventListener? = null,
) : VastBindAdapterItem {

    override fun getVBAdpItemType(): Int {
        return R.layout.item_bind_imageview
    }

}
