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
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.roundToInt

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2025/5/3
// Documentation:

/**
 * Grid [RecyclerView.ItemDecoration].
 *
 * @since 1.5.2
 */
class GridItemDecoration : RecyclerView.ItemDecoration {

    private val spanCount: Int
    private val space: Int

    constructor(spanCount: Int, space: Float) {
        this.spanCount = spanCount
        this.space = space.coerceAtLeast(0f).roundToInt()
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val layoutParams: GridLayoutManager.LayoutParams =
            view.layoutParams as GridLayoutManager.LayoutParams
        val spanIndex: Int = layoutParams.spanIndex
        val position: Int = parent.getChildAdapterPosition(view)
        outRect.bottom = space
        if (position == 0 || position == 1) {
            outRect.top = space * spanCount
        } else {
            outRect.top = 0
        }
        if (spanIndex % spanCount == 0) {
            outRect.left = space
            outRect.right = space / spanCount
        } else {
            outRect.left = space / spanCount
            outRect.right = space
        }
    }
}