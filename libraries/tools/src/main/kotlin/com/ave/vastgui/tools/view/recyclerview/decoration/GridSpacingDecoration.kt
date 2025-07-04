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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.roundToInt

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2025/5/3
// Documentation: https://sakurajimamaii.github.io/AVE-DOC/documents/tools/core-topics/ui/recyclerview/decoration/spacing-decoration/
// Reference: https://juejin.cn/post/6844904116859174926

/**
 * Spacing decoration of grid layout for [RecyclerView].
 *
 * @since 1.5.2
 */
class GridSpacingDecoration(val spanCount: Int, val rowSpacingPx: Float, val columnSpacingPx: Float) : RecyclerView.ItemDecoration() {

    /** @since 1.5.2 */
    constructor(spanCount: Int, spacingPx: Float) : this(spanCount, spacingPx, spacingPx)

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val manager = parent.layoutManager as? GridLayoutManager
        if (null == manager) return
        val position = parent.getChildAdapterPosition(view)
        val column = position % spanCount
        outRect.left = (column * columnSpacingPx / spanCount).roundToInt()
        outRect.right = (columnSpacingPx - (column + 1) * columnSpacingPx / spanCount).roundToInt()
        if (position >= spanCount) {
            outRect.top = rowSpacingPx.roundToInt()
        }
    }

}