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

package com.ave.vastgui.tools.view.masklayout

import android.animation.TimeInterpolator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.core.animation.doOnEnd
import com.ave.vastgui.core.extension.NotNUllVar

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/10/21
// Documentation: https://sakurajimamaii.github.io/AVE-DOC/documents/tools/core-topics/ui/masklayout/masklayout/

/**
 * Mask View.
 *
 * @since 0.5.6
 */
internal class MaskView(context: Context) : View(context) {

    /** @since 1.5.2 */
    private val paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }

    /** @since 1.5.2 */
    private val expandedXfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)

    /** @since 1.5.2 */
    private val collapsedXfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)

    /** @since 1.5.2 */
    private var maskRadius: Float = 0f

    /** @since 1.5.2 */
    internal lateinit var bitmap: Bitmap

    /** @since 1.5.2 */
    internal var targetMaskRadius: Float = 0f

    /** @since 1.5.2 */
    internal var maskAnimation: MaskAnimation = MaskAnimation.COLLAPSED

    /** @since 1.5.2 */
    internal var maskDuration: Long = 1000L

    /** @since 1.5.2 */
    internal var maskTimeInterpolator: TimeInterpolator = AccelerateDecelerateInterpolator()

    /** @since 1.5.2 */
    internal var maskCenterX: Float by NotNUllVar()

    /** @since 1.5.2 */
    internal var maskCenterY: Float by NotNUllVar()

    override fun onDraw(canvas: Canvas) = with(canvas) {
        val layer = saveLayer(0f, 0f, width.toFloat(), height.toFloat(), null)
        when (maskAnimation) {
            MaskAnimation.EXPANDED -> {
                drawBitmap(bitmap, 0f, 0f, null)
                paint.xfermode = expandedXfermode
                drawCircle(maskCenterX, maskCenterY, maskRadius, paint)
            }

            MaskAnimation.COLLAPSED -> {
                drawCircle(maskCenterX, maskCenterY, maskRadius, paint)
                paint.xfermode = collapsedXfermode
                drawBitmap(bitmap, 0f, 0f, paint)
            }
        }
        paint.xfermode = null
        restoreToCount(layer)
    }

    /**
     * Active Mask.
     *
     * @since 0.5.6
     */
    fun activeMask(animation: MaskAnimation, animFinish: () -> Unit) {
        maskAnimation = animation
        val radiusRange = when (animation) {
            MaskAnimation.EXPANDED -> 0f to targetMaskRadius
            MaskAnimation.COLLAPSED -> targetMaskRadius to 0f
        }
        ValueAnimator.ofFloat(radiusRange.first, radiusRange.second).apply {
            duration = maskDuration
            interpolator = maskTimeInterpolator
            addUpdateListener {
                maskRadius = it.animatedValue as Float
                invalidate()
            }
            doOnEnd { animFinish() }
            start()
        }
    }

}