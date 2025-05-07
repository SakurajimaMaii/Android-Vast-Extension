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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.roundToInt

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2025/5/3
// Documentation:

/**
 * Vertical [RecyclerView.ItemDecoration].
 *
 * @since 1.5.2
 */
class VerticalIntervalDecoration : RecyclerView.ItemDecoration {
    private val space: Int
    private var setTopAndBottom = false

    constructor(space: Float) {
        this.space = space.coerceAtLeast(0f).roundToInt()
    }

    constructor(space: Float, setLeftAndRight: Boolean) {
        this.space = space.coerceAtLeast(0f).roundToInt()
        this.setTopAndBottom = setLeftAndRight
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)
        val totalCount = parent.adapter?.itemCount ?: return
        val manager = parent.layoutManager as? LinearLayoutManager ?: return
        if (manager.orientation == RecyclerView.VERTICAL) {
            if (position == 0) {
                outRect.top = if (setTopAndBottom) space else 0
                outRect.bottom = space / 2
            } else if (position == totalCount - 1) {
                outRect.top = space / 2
                outRect.bottom = if (setTopAndBottom) space else 0
            } else {
                outRect.top = space / 2
                outRect.bottom = space / 2
            }
        } else {
            outRect.top = space
            outRect.bottom = space
        }
    }
}