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

package com.ave.vastgui.tools.view.progress

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import androidx.core.graphics.withSave
import com.ave.vastgui.tools.R
import com.ave.vastgui.tools.graphics.getBaseLine
import com.ave.vastgui.tools.graphics.getTextHeight

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/4/4
// Documentation: https://ave.entropy2020.cn/documents/VastTools/core-topics/ui/progress/horizontal-progress-view/

/**
 * HorizontalTextProgressView
 *
 * @property mTextWidth The width of the text.
 * @property mProgressHeight The height of the progress.
 * @property mTextBoxColor Color-int of the text box.
 * @property mTextMargin The stroke width of the text.
 * @since 0.2.0
 */
class HorizontalTextProgressView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.Default_HorizontalTextProgressView_Style,
    defStyleRes: Int = R.style.BaseHorizontalTextProgressView
) : ProgressView(context, attrs, defStyleAttr, defStyleRes) {

    private val mDefaultProgressHeight =
        resources.getDimension(R.dimen.default_horizontal_text_progress_height)
    private val mDefaultTextMargin =
        resources.getDimension(R.dimen.default_horizontal_text_progress_text_margin)

    private var mTextPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var mProgressPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var mBackgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var mBoxPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mProgressRectF = RectF()
    private val mBoxRectF = RectF()
    private val mTextWidth: Float
        get() = mTextPaint.measureText(textOrDefault())

    override var mTextSize: Float
        set(value) {
            mTextPaint.textSize = value
        }
        get() = mTextPaint.textSize

    override var mTextColor: Int
        set(value) {
            mTextPaint.color = value
        }
        get() = mTextPaint.color

    override var mProgressColor: Int =
        context.getColor(R.color.md_theme_primary)
        set(value) {
            field = value
            mBoxPaint.color = value
            mProgressPaint.color = value
        }

    override var mProgressBackgroundColor: Int
        set(value) {
            mBackgroundPaint.color = value
        }
        get() = mBackgroundPaint.color

    var mTextBoxColor: Int
        set(value) {
            mBoxPaint.color = value
        }
        get() = mBoxPaint.color

    var mProgressHeight = mDefaultProgressHeight
        set(value) {
            field = value.coerceAtLeast(mTextPaint.getTextHeight())
        }

    var mTextMargin = mDefaultTextMargin
        set(value) {
            field = value.coerceAtLeast(0f)
        }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val maxHeight = mTextPaint.getTextHeight().coerceAtLeast(mProgressHeight)
        val neededHeight = resolveSize((maxHeight + 2 * mTextMargin).toInt(), heightMeasureSpec)
        setMeasuredDimension(measuredWidth, neededHeight)
    }

    override fun onDraw(canvas: Canvas) {
        val backgroundTop = (measuredHeight - mProgressHeight) / 2f
        val radius = (bottom - top).coerceAtMost(right - left) / 2f
        canvas.drawRoundRect(
            0f,
            backgroundTop,
            measuredWidth.toFloat(),
            measuredHeight - backgroundTop,
            radius, radius,
            mBackgroundPaint
        )
        drawProgress(
            canvas,
            mProgressPaint
        )
        drawBox(canvas)
    }

    /**
     * Draw text box.
     *
     * @since 0.5.3
     */
    private fun drawBox(canvas: Canvas) {
        val progressWidth: Float = (mCurrentProgress / mMaximumProgress * measuredWidth)
        // The width of text box.
        val boxWidth: Float = (mTextMargin * 2 + mTextWidth)
        // The maximum value in order to ensure that the right side of
        // the TextBox will not cross the border.
        // The minimum value in order to ensure that the left side of
        // the TextBox will not cross the border.
        val boxStart: Float = progressWidth.coerceIn(boxWidth / 2f, measuredWidth - boxWidth / 2f)
        val boxHeight: Float = (mProgressHeight + 2 * mTextMargin)
        mBoxRectF.set(
            (boxStart - boxWidth / 2f),
            ((measuredHeight - boxHeight) / 2f),
            (boxStart + boxWidth / 2f),
            ((measuredHeight + boxHeight) / 2f)
        )
        canvas.drawRoundRect(
            mBoxRectF, (boxHeight / 2f), (boxHeight / 2f), mBoxPaint
        )
        // Draw text of box.
        canvas.drawText(
            textOrDefault(),
            (boxStart - mTextWidth / 2f),
            measuredHeight / 2f + mTextPaint.getBaseLine(),
            mTextPaint
        )
    }

    /**
     * Draw progress.
     *
     * @since 0.2.0
     */
    private fun drawProgress(canvas: Canvas, paint: Paint) {
        val left = 0f
        val top = (measuredHeight - mProgressHeight) / 2f
        val right = measuredWidth.toFloat()
        val bottom = measuredHeight - top
        val width = (mCurrentProgress / mMaximumProgress) * measuredWidth
        val radius = (bottom - top).coerceAtMost(right - left) / 2f
        mProgressRectF.set(left, top, width, bottom)
        canvas.withSave {
            clipRect(mProgressRectF)
            drawRoundRect(left, top, right, bottom, radius, radius, paint)
        }

        if (width >= left + radius) {
            mProgressRectF.set(left + radius, top, width.coerceAtMost(right - radius), bottom)
            canvas.drawRect(mProgressRectF, paint)
        }

        if (width >= right - radius) {
            mProgressRectF.set(right - radius, top, left + width, bottom)
            canvas.withSave {
                clipRect(mProgressRectF)
                drawRoundRect(left, top, right, bottom, radius, radius, paint)
            }
        }
    }

    init {
        val typedArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.HorizontalTextProgressView,
            defStyleAttr,
            defStyleRes
        )
        mMaximumProgress =
            typedArray.getFloat(
                R.styleable.HorizontalTextProgressView_progress_maximum_value,
                mDefaultMaximumProgress
            )
        mCurrentProgress =
            typedArray.getFloat(
                R.styleable.HorizontalTextProgressView_progress_current_value,
                mDefaultCurrentProgress
            )
        mText =
            typedArray.getString(R.styleable.HorizontalTextProgressView_progress_text) ?: ""
        mTextColor =
            typedArray.getColor(
                R.styleable.HorizontalTextProgressView_progress_text_color,
                context.getColor(R.color.md_theme_onPrimary)
            )
        mTextSize =
            typedArray.getDimension(
                R.styleable.HorizontalTextProgressView_progress_text_size,
                mDefaultTexSize
            )
        mProgressColor =
            typedArray.getColor(
                R.styleable.HorizontalTextProgressView_progress_color,
                context.getColor(R.color.md_theme_primary)
            )
        mProgressBackgroundColor =
            typedArray.getColor(
                R.styleable.HorizontalTextProgressView_progress_background_color,
                context.getColor(R.color.md_theme_primaryContainer)
            )
        mProgressHeight =
            typedArray.getDimension(
                R.styleable.HorizontalTextProgressView_horizontal_text_progress_height,
                mDefaultProgressHeight
            )
        mTextBoxColor =
            typedArray.getColor(
                R.styleable.HorizontalTextProgressView_horizontal_text_progress_box_color,
                context.getColor(R.color.md_theme_primary)
            )
        mTextMargin =
            typedArray.getDimension(
                R.styleable.HorizontalTextProgressView_horizontal_text_progress_text_margin,
                mDefaultTextMargin
            )
        typedArray.recycle()
    }

}