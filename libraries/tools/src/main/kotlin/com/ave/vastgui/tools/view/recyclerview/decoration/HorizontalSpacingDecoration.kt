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
// Documentation: https://sakurajimamaii.github.io/AVE-DOC/documents/tools/core-topics/ui/recyclerview/decoration/spacing-decoration/

/**
 * Horizontal spacing decoration of [RecyclerView].
 *
 * @since 1.5.2
 */
class HorizontalSpacingDecoration : RecyclerView.ItemDecoration {

    /**
     * The size of space(in pixels).
     *
     * @since 1.5.2
     */
    val spacePx: Float

    /**
     * Whether to set space of top and bottom item.
     *
     * @since 1.5.2
     */
    val setLeftAndRight: Boolean

    /**
     * @param spacePx Refer to [HorizontalSpacingDecoration.spacePx].
     * @since 1.5.2
     */
    constructor(spacePx: Float) {
        this.spacePx = spacePx
        setLeftAndRight = false
    }

    /**
     * @param spacePx Refer to [HorizontalSpacingDecoration.spacePx].
     * @param setLeftAndRight Refer to
     * [HorizontalSpacingDecoration.setLeftAndRight].
     * @since 1.5.2
     */
    constructor(spacePx: Float, setLeftAndRight: Boolean) {
        this.spacePx = spacePx
        this.setLeftAndRight = setLeftAndRight
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val manager = parent.layoutManager as? LinearLayoutManager ?: return
        val totalCount = parent.adapter?.itemCount ?: return
        val position = parent.getChildAdapterPosition(view)
        if (manager.orientation == RecyclerView.HORIZONTAL) {
            when (position) {
                0 -> {
                    outRect.left = if (setLeftAndRight) spacePx.roundToInt() else 0
                    outRect.right = (spacePx / 2f).roundToInt()
                }

                totalCount - 1 -> {
                    outRect.left = (spacePx / 2f).roundToInt()
                    outRect.right = if (setLeftAndRight) spacePx.roundToInt() else 0
                }

                else -> {
                    outRect.left = (spacePx / 2f).roundToInt()
                    outRect.right = (spacePx / 2f).roundToInt()
                }
            }
        } else {
            outRect.left = spacePx.roundToInt()
            outRect.right = spacePx.roundToInt()
        }
    }
}