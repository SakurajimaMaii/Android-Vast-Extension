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

package com.ave.vastgui.tools.view.progress

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import androidx.core.graphics.withSave
import com.ave.vastgui.tools.R
import com.ave.vastgui.tools.graphics.getTextHeight
import com.ave.vastgui.tools.utils.DensityUtils.DP

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/4/4
// Documentation: https://ave.entropy2020.cn/documents/VastTools/core-topics/ui/progress/horizontal-progress-view/

/**
 * LineTextProgressView
 *
 * @property mTriangleBaseLength The base length of the triangle at the
 *     bottom of the text box.
 * @property mTriangleHeight The height of the triangle at the bottom of
 *     the text box.
 * @property mProgressHeight The height of progress.
 * @property mTextBoxColor Color-int of the text box.
 * @property mTextMargin The stroke width of the text box.
 * @since 0.2.0
 */
class LineTextProgressView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.Default_LineTextProgressView_Style,
    defStyleRes: Int = R.style.BaseLineTextProgressView
) : ProgressView(context, attrs, defStyleAttr, defStyleRes) {

    private val mDefaultProgressHeight =
        resources.getDimension(R.dimen.default_horizontal_text_progress_height)
    private val mDefaultTextMargin
        get() = resources.getDimension(R.dimen.default_linetext_progress_text_margin)
    private val mTriangleBaseLength = 12f.DP
    private val mTriangleHeight = 8f.DP

    private var mTextPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var mProgressPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var mBackgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var mBoxPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mBoxRectF = RectF()
    private val mProgressRectF = RectF()
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
        ContextCompat.getColor(context, R.color.md_theme_primary)
        set(value) {
            field = value
            mProgressPaint.color = value
            mBoxPaint.color = value
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
        val neededHeight = resolveSize(
            (mProgressHeight + 2 * mTextMargin + mTriangleHeight + mTextPaint.getTextHeight()).toInt(),
            heightMeasureSpec
        )
        setMeasuredDimension(measuredWidth, neededHeight)
    }

    override fun onDraw(canvas: Canvas) {
        val fontMetrics = mTextPaint.fontMetrics
        val textHeight = fontMetrics.bottom - fontMetrics.top
        val baseWidth = mTextWidth

        val boxWidth: Float = baseWidth + (mTextMargin * 2f)
        val boxHeight: Float = textHeight + (mTextMargin * 2f)
        val boxLeft: Float = mCurrentProgress / mMaximumProgress * (measuredWidth - boxWidth)
        drawBox(canvas, boxLeft, boxWidth, boxHeight)

        // The distance between the baseline and text central axis
        val textCenter2Bottom: Float = textHeight / 2f - fontMetrics.bottom
        val textX: Float = boxLeft + (boxWidth / 2f - mTextWidth / 2f)
        val textY: Float = boxHeight / 2f + textCenter2Bottom

        canvas.drawText(textOrDefault(), textX, textY, mTextPaint)

        val progressLeft: Float = boxWidth / 2f
        val progressTop: Float = boxHeight + mTriangleHeight
        val progressRight: Float = measuredWidth - (boxWidth / 2f)
        val progressBottom: Float = measuredHeight.toFloat()
        val radius = (progressRight - progressLeft)
            .coerceAtMost(progressBottom - progressTop) / 2f
        mProgressRectF.set(progressLeft, progressTop, progressRight, progressBottom)
        canvas.drawRoundRect(mProgressRectF, radius, radius, mBackgroundPaint)
        drawProgress(
            canvas,
            progressLeft,
            progressTop,
            progressRight,
            progressBottom,
            mProgressPaint
        )
    }

    /** @since 0.5.4 */
    private fun drawBox(canvas: Canvas, left: Float, width: Float, height: Float) {
        mBoxRectF.set(left, 0f, (width + left), height)
        canvas.drawRoundRect(
            mBoxRectF,
            (height / 4),
            (height / 4),
            mBoxPaint
        )
        val path = Path()
        path.moveTo(left + width / 2f - mTriangleBaseLength / 2f, height)
        path.lineTo(left + width / 2f + mTriangleBaseLength / 2f, height)
        path.lineTo((left + width / 2), height + mTriangleHeight - 1f.DP)
        canvas.drawPath(path, mBoxPaint)
    }

    /**
     * Draw progress.
     *
     * @since 0.5.4
     */
    private fun drawProgress(
        canvas: Canvas,
        left: Float,
        top: Float,
        right: Float,
        bottom: Float,
        paint: Paint
    ) {
        val width = (mCurrentProgress / mMaximumProgress) * (right - left) + left
        mProgressRectF.set(left, top, width, bottom)
        val radius = (right - left)
            .coerceAtMost(bottom - top) / 2f
        canvas.withSave {
            clipRect(mProgressRectF)
            drawRoundRect(left, top, right, bottom, radius, radius, paint)
        }

        if (width >= left + radius) {
            mProgressRectF.set(left + radius, top, width.coerceAtMost(right - radius), bottom)
            canvas.drawRect(mProgressRectF, paint)
        }

        if (width >= right - radius) {
            mProgressRectF.set(right - radius, top, width, bottom)
            canvas.withSave {
                clipRect(mProgressRectF)
                drawRoundRect(left, top, right, bottom, radius, radius, paint)
            }
        }
    }

    init {
        val typedArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.LineTextProgressView,
            defStyleAttr,
            defStyleRes
        )
        mMaximumProgress =
            typedArray.getFloat(
                R.styleable.LineTextProgressView_progress_maximum_value,
                mDefaultMaximumProgress
            )
        mCurrentProgress =
            typedArray.getFloat(
                R.styleable.LineTextProgressView_progress_current_value,
                mDefaultCurrentProgress
            )
        mText =
            typedArray.getString(R.styleable.LineTextProgressView_progress_text) ?: ""
        mTextColor =
            typedArray.getColor(
                R.styleable.LineTextProgressView_progress_text_color,
                ContextCompat.getColor(context, R.color.md_theme_onPrimary)
            )
        mTextSize =
            typedArray.getDimension(
                R.styleable.LineTextProgressView_progress_text_size,
                mDefaultTexSize
            )
        mProgressColor =
            typedArray.getColor(
                R.styleable.LineTextProgressView_progress_color,
                ContextCompat.getColor(context, R.color.md_theme_primary)
            )
        mProgressBackgroundColor =
            typedArray.getColor(
                R.styleable.LineTextProgressView_progress_background_color,
                ContextCompat.getColor(context, R.color.md_theme_primaryContainer)
            )
        mProgressHeight =
            typedArray.getDimension(
                R.styleable.LineTextProgressView_linetext_progress_height,
                mDefaultProgressHeight
            )
        mTextBoxColor =
            typedArray.getColor(
                R.styleable.LineTextProgressView_linetext_progress_box_color,
                ContextCompat.getColor(context, R.color.md_theme_primary)
            )
        mTextMargin =
            typedArray.getDimension(
                R.styleable.LineTextProgressView_linetext_progress_text_margin,
                mDefaultTextMargin
            )
        typedArray.recycle()
    }

}