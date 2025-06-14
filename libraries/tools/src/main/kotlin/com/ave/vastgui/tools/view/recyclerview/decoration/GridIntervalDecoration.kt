/*
 * Copyright 2021-2025 VastGui
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

package com.ave.vastgui.tools.view.recyclerview.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2025/5/3
// Documentation:
// Reference: https://juejin.cn/post/6844904116859174926

/**
 * [GridIntervalDecoration]
 *
 * @since 1.5.2
 */
class GridIntervalDecoration(private val spanCount: Int, private val rowSpacing: Int, private val columnSpacing: Int) : RecyclerView.ItemDecoration() {

    constructor(spanCount: Int, spacing: Int) : this(spanCount, spacing, spacing)

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)
        val column = position % spanCount
        outRect.left = column * columnSpacing / spanCount
        outRect.right = columnSpacing - (column + 1) * columnSpacing / spanCount
        if (position >= spanCount) {
            outRect.top = rowSpacing
        }
    }

}