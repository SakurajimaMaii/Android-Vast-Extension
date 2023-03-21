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

package com.ave.vastgui.app.activity.adpexample.widget

import com.ave.vastgui.adapter.widget.AdapterDiffUtil
import com.ave.vastgui.app.activity.adpexample.model.Picture
import com.ave.vastgui.app.activity.adpexample.model.PictureWrapper


// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/3/3
// Description: 
// Documentation:
// Reference:

class PictureDiffUtil : AdapterDiffUtil<Picture, PictureWrapper>() {
    override fun newAreItemsTheSame(oldItem: Picture, newItem: Picture): Boolean {
        return oldItem.drawable == newItem.drawable
    }

    override fun newAreContentsTheSame(oldItem: Picture, newItem: Picture): Boolean {
        return oldItem.drawable == newItem.drawable
    }
}