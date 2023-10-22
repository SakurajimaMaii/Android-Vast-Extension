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

package com.ave.vastgui.tools.view.masklayout

import android.animation.TimeInterpolator
import android.content.Context
import android.util.AttributeSet
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.FrameLayout
import com.ave.vastgui.core.extension.NotNUllVar
import com.ave.vastgui.tools.view.extension.viewSnapshot
import kotlin.math.hypot

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/10/20
// Description: 
// Documentation:
// Reference:

/**
 * Mask Layout.
 *
 * @since 0.5.6
 */
class MaskLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : FrameLayout(context, attrs) {

    private var mAnimationRunning: Boolean = false
    private var mTargetMaskRadius: Float = 0f

    var mMaskCenterX: Float by NotNUllVar()

    var mMaskCenterY: Float by NotNUllVar()

    var mMaskDuration: Long = 1000L
        set(value) {
            field = value.coerceAtLeast(0L)
        }

    var mMaskTimeInterpolator: TimeInterpolator =
        AccelerateDecelerateInterpolator()

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mTargetMaskRadius =
            hypot(measuredWidth.toFloat(), measuredHeight.toFloat())
    }

    /**
     * Active mask animation.
     *
     * @since 0.5.6
     */
    fun activeMask(animation: MaskAnimation, onMaskComplete: () -> Unit) {
        if (mAnimationRunning) {
            return
        } else {
            mAnimationRunning = true
            val maskView = MaskView(context).apply {
                mBitmap = viewSnapshot(this@MaskLayout)
                mTargetMaskRadius = this@MaskLayout.mTargetMaskRadius
                mMaskDuration = this@MaskLayout.mMaskDuration
                mMaskTimeInterpolator = this@MaskLayout.mMaskTimeInterpolator
                mMaskCenterX = this@MaskLayout.mMaskCenterX
                mMaskCenterY = this@MaskLayout.mMaskCenterY
            }
            addView(maskView)
            onMaskComplete()
            maskView.activeMask(animation) {
                removeView(maskView)
                mAnimationRunning = false
            }
        }
    }

    init {
        mMaskDuration = 1000L
        mMaskTimeInterpolator = AccelerateDecelerateInterpolator()
    }
}