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

package com.ave.vastgui.app.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import androidx.core.graphics.withClip
import com.ave.vastgui.app.R
import com.ave.vastgui.tools.graphics.BmpUtils

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/12/5
// Description: 
// Documentation:
// Reference:

class TestView(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    override fun onDraw(canvas: Canvas) {
        val bitmap = BmpUtils.getBitmapFromDrawable(R.drawable.ic_star_unselected)
        val rect = Rect(0, 0, bitmap.width, bitmap.height)
        canvas.drawBitmap(bitmap, null, rect, paint)
        val mask = BmpUtils.getBitmapFromDrawable(R.drawable.ic_star_selected)
        canvas.withClip(0, 0, (bitmap.width * 0.1f).toInt(), bitmap.height) {
            drawBitmap(mask, null, rect, paint)
        }
    }

}