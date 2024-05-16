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
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewTreeObserver
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatDelegate
import com.ave.vastgui.core.extension.NotNUllVar
import com.ave.vastgui.tools.annotation.ExperimentalView
import com.ave.vastgui.tools.view.extension.viewSnapshot
import kotlin.math.hypot

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/10/20

/**
 * Mask Layout.
 *
 * @since 0.5.6
 */
@ExperimentalView
class MaskLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : FrameLayout(context, attrs) {

    interface MaskAnimationListener {
        /**
         * Callback when [MaskView] is already prepared. At this time, you should
         * switch the display of page elements, such as adjusting the text color
         * from black in day mode to white in dark night mode.
         *
         * **Warning: You cannot call method like
         * [AppCompatDelegate.setDefaultNightMode] in this callback to modify the
         * mode ofthe activity, because modifying the activity mode will cause
         * activity recreate, leading to serious consequences such as animation
         * abnormalities.**
         *
         * @since 0.5.6
         */
        fun onMaskComplete()

        /**
         * Callback when the mask animation has finished. At this time you can
         * modify the app to dark night mode.
         *
         * @since 0.5.6
         */
        fun onMaskFinished()
    }

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
     * @see MaskAnimationListener.onMaskComplete
     * @see MaskAnimationListener.onMaskFinished
     * @since 0.5.6
     */
    fun activeMask(animation: MaskAnimation, listener: MaskAnimationListener) {
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
            listener.onMaskComplete()
            maskView.activeMask(animation) {
                removeView(maskView)
                mAnimationRunning = false
                listener.onMaskFinished()
            }
        }
    }

    /**
     * Update Coordinate of the mask circle center by [view].
     *
     * @since 0.5.6
     */
    fun updateCoordinate(view: View) {
        view.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                mMaskCenterX = view.left + view.width / 2f
                mMaskCenterY = view.top + view.height / 2f
                view.getViewTreeObserver().removeOnGlobalLayoutListener(this)
            }
        })
    }

    init {
        mMaskDuration = 1000L
        mMaskTimeInterpolator = AccelerateDecelerateInterpolator()
    }
}