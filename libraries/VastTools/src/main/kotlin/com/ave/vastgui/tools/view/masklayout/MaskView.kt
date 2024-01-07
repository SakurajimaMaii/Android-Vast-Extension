/*
 * Copyright 2024 VastGui guihy2019@gmail.com
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
import androidx.core.animation.addListener
import com.ave.vastgui.core.extension.NotNUllVar

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/10/21

/**
 * Mask View.
 *
 * @since 0.5.6
 */
class MaskView internal constructor(context: Context) : View(context) {

    private val mPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }
    private val mExpandedXfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
    private val mCollapsedXfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
    private var mMaskRadius: Float = 0f

    internal lateinit var mBitmap: Bitmap
    internal var mTargetMaskRadius: Float = 0f
    internal var mMaskAnimation: MaskAnimation = MaskAnimation.COLLAPSED
    internal var mMaskDuration: Long = 1000L
    internal var mMaskTimeInterpolator: TimeInterpolator = AccelerateDecelerateInterpolator()
    internal var mMaskCenterX: Float by NotNUllVar()
    internal var mMaskCenterY: Float by NotNUllVar()

    override fun onDraw(canvas: Canvas) = with(canvas) {
        val layer = saveLayer(0f, 0f, width.toFloat(), height.toFloat(), null)
        when (mMaskAnimation) {
            MaskAnimation.EXPANDED -> {
                drawBitmap(mBitmap, 0f, 0f, null)
                mPaint.xfermode = mExpandedXfermode
                drawCircle(mMaskCenterX, mMaskCenterY, mMaskRadius, mPaint)
            }

            MaskAnimation.COLLAPSED -> {
                drawCircle(mMaskCenterX, mMaskCenterY, mMaskRadius, mPaint)
                mPaint.xfermode = mCollapsedXfermode
                drawBitmap(mBitmap, 0f, 0f, mPaint)
            }
        }
        mPaint.xfermode = null
        restoreToCount(layer)
    }

    /**
     * Active Mask.
     *
     * @since 0.5.6
     */
    fun activeMask(animation: MaskAnimation, animFinish: () -> Unit) {
        mMaskAnimation = animation
        val radiusRange = when (animation) {
            MaskAnimation.EXPANDED -> 0f to mTargetMaskRadius
            MaskAnimation.COLLAPSED -> mTargetMaskRadius to 0f
        }
        ValueAnimator.ofFloat(radiusRange.first, radiusRange.second).apply {
            duration = mMaskDuration
            interpolator = mMaskTimeInterpolator
            addUpdateListener { valueAnimator ->
                mMaskRadius = valueAnimator.animatedValue as Float
                invalidate()
            }
            addListener(onEnd = {
                animFinish()
            })
            start()
        }
    }

}