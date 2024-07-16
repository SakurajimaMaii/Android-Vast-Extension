/*
 * Copyright 2021-2024 VastGui
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

package com.ave.vastgui.tools.view.extension

import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Build
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/9/28
// Documentation: https://ave.entropy2020.cn/documents/tools/core-topics/ui/extension/snapshot-view/

/**
 * Snapshot option.
 *
 * @since 0.5.4
 */
data class SnapshotOption(val width: Int, val height: Int)

/**
 * View snapshot.
 *
 * @param view The [View] or [ViewGroup] that needs to get snapshot.
 * @param option If the view is not inflated, you need to specify the size
 *     through this parameter.
 * @since 0.5.4
 */
@JvmOverloads
fun viewSnapshot(view: View, option: SnapshotOption? = null): Bitmap {
    if (null != option) {
        val measuredWidth =
            View.MeasureSpec.makeMeasureSpec(option.width, View.MeasureSpec.EXACTLY)
        val measuredHeight =
            View.MeasureSpec.makeMeasureSpec(option.height, View.MeasureSpec.EXACTLY)
        view.measure(measuredWidth, measuredHeight)
        view.layout(0, 0, view.measuredWidth, view.measuredHeight)
    }
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        viewSnapshotApi28Impl(view)
    } else {
        viewSnapshotImpl(view)
    }
}

/** @since 0.5.4 */
@RequiresApi(Build.VERSION_CODES.P)
private fun viewSnapshotApi28Impl(view: View): Bitmap {
    val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    view.draw(canvas)
    return bitmap
}

/** @since 0.5.4 */
@Suppress("DEPRECATION")
private fun viewSnapshotImpl(view: View): Bitmap {
    view.isDrawingCacheEnabled = true
    var drawingCache = view.drawingCache
    drawingCache = Bitmap.createBitmap(drawingCache!!)
    view.isDrawingCacheEnabled = false
    return drawingCache
}