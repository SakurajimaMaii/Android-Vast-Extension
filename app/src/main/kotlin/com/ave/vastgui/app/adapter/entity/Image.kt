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

package com.ave.vastgui.app.adapter.entity

import androidx.annotation.DrawableRes
import com.ave.vastgui.adapter.widget.AdapterItemWrapper
import com.ave.vastgui.app.R

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/9/25

data class Image(@DrawableRes val mResId: Int)

class ImageWrapper private constructor(data: Image) :
    AdapterItemWrapper<Image>(data, mLayoutId = R.layout.item_image) {
    constructor(@DrawableRes mResId: Int) : this(Image(mResId))
}