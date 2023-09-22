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

package com.ave.vastgui.tools.view.progress

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Rect
import android.graphics.RectF
import android.util.AttributeSet
import androidx.annotation.ColorInt
import androidx.annotation.FloatRange
import com.ave.vastgui.core.extension.nothing_to_do
import com.ave.vastgui.tools.R
import com.ave.vastgui.tools.utils.DensityUtils.DP
import java.text.DecimalFormat

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
 * @property mTextBoxStrokeWidth The stroke width of the text box.
 * @since 0.2.0
 */
class LineTextProgressView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.Default_LineTextProgressView_Style,
    defStyleRes: Int = R.style.BaseLineTextProgressView
) : ProgressView(context, attrs, defStyleAttr, defStyleRes) {

    private val mDefaultTextMargin
        get() = resources.getDimension(R.dimen.default_linetext_progress_text_margin)

    private var mWidth = 0
    private var mHeight = 0
    private val mTriangleBaseLength = 12f.DP
    private val mTriangleHeight = 8f.DP

    private var mTextPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var mProgressPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        strokeCap = Paint.Cap.ROUND
    }
    private var mBackgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        strokeCap = Paint.Cap.ROUND
    }
    private var mBoxPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    var mTextBoxStrokeWidth = mDefaultTextMargin
        private set

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.EXACTLY) {
            mWidth = MeasureSpec.getSize(widthMeasureSpec)
        }
        if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.EXACTLY) {
            mHeight = MeasureSpec.getSize(heightMeasureSpec)
        }
        setMeasuredDimension(mWidth, mHeight)
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val fontMetrics = mTextPaint.fontMetrics
        val textHeight = fontMetrics.bottom - fontMetrics.top
        val baseWidth = mTextPaint.measureText("100.00%")

        val boxWidth = baseWidth + (mTextBoxStrokeWidth * 2)
        val boxHeight = textHeight + (mTextBoxStrokeWidth * 2).toInt()
        val boxLeft = (mCurrentProgress / mMaximumProgress * (mWidth - boxWidth)).toInt()
        drawBox(canvas, boxLeft, boxWidth.toInt(), boxHeight.toInt())

        val textWidth = mTextPaint.measureText(mText)
        // The distance between the baseline and text central axis
        val textCenter2Bottom = textHeight / 2 - fontMetrics.bottom
        val textX = boxLeft + (boxWidth / 2 - textWidth / 2)
        val textY = boxHeight / 2 + textCenter2Bottom

        canvas.drawText(mText, textX, textY, mTextPaint)

        // You should not change its draw order.
        drawProgress(
            canvas,
            (boxWidth / 2).toInt(),
            (boxHeight + mTriangleHeight).toInt(),
            mWidth,
            mHeight,
            mBackgroundPaint
        )
        drawProgress(
            canvas,
            (boxWidth / 2).toInt(),
            (boxHeight + mTriangleHeight).toInt(),
            (boxWidth / 2 + boxLeft).toInt(),
            mHeight,
            mProgressPaint
        )
    }

    override fun setCurrentProgress(currentProgress: Float) {
        super.setCurrentProgress(currentProgress)
        mText = DecimalFormat("0.00%").format(mCurrentProgress / mMaximumProgress)
    }

    /**
     * You should call [setCurrentProgress] in order to set [mText].
     *
     * @since 0.2.0
     */
    override fun setText(text: String) {
        nothing_to_do()
    }

    /**
     * Set text size for [mTextSize] and [mTextPaint].
     *
     * @since 0.5.3
     */
    override fun setTextSize(@FloatRange(from = 0.0) size: Float) {
        super.setTextSize(size)
        mTextPaint.textSize = mTextSize
    }

    /**
     * Set text size for [mTextColor] and [mTextPaint].
     *
     * @since 0.5.3
     */
    override fun setTextColor(@ColorInt color: Int) {
        super.setTextColor(color)
        mTextPaint.color = mTextColor
    }

    /**
     * Set color-int for [mProgressBackgroundColor] and [mBackgroundPaint].
     *
     * @since 0.5.3
     */
    override fun setProgressBackgroundColor(@ColorInt color: Int) {
        super.setProgressBackgroundColor(color)
        mBackgroundPaint.color = mProgressBackgroundColor
    }

    /**
     * Set color-int for [mProgressColor] and [mBoxPaint].
     *
     * @since 0.5.3
     */
    override fun setProgressColor(@ColorInt color: Int) {
        super.setProgressColor(color)
        mProgressPaint.color = mProgressColor
        mBoxPaint.color = mProgressColor
    }

    /**
     * Set [mTextBoxStrokeWidth].
     *
     * @since 0.2.0
     */
    fun setTextBoxStrokeWidth(width: Float) {
        mTextBoxStrokeWidth = width
    }

    /** @since 0.2.0 */
    private fun drawBox(canvas: Canvas, left: Int, width: Int, height: Int) {
        val rectF =
            RectF(left.toFloat(), 0f, (width + left).toFloat(), height.toFloat())
        canvas.drawRoundRect(
            rectF,
            (height / 4).toFloat(),
            (height / 4).toFloat(),
            mBoxPaint
        )
        val path = Path()
        path.moveTo(left + width / 2f - mTriangleBaseLength / 2f, height.toFloat())
        path.lineTo(left + width / 2f + mTriangleBaseLength / 2f, height.toFloat())
        path.lineTo((left + width / 2).toFloat(), height + mTriangleHeight - 1f.DP)
        canvas.drawPath(path, mBoxPaint)
    }

    /**
     * Draw progress.
     *
     * @since 0.2.0
     */
    private fun drawProgress(
        canvas: Canvas,
        left: Int,
        top: Int,
        right: Int,
        bottom: Int,
        paint: Paint
    ) {
        val circleRadius = (bottom - top) / 2f
        val circleLeftX = left + circleRadius
        val circleRightX = mWidth - left - circleRadius
        val circleY = top + circleRadius

        canvas.save()
        canvas.clipRect(Rect(left, top, right, bottom))
        canvas.drawCircle(circleLeftX, circleY, circleRadius, paint)
        canvas.restore()

        // Draw a rectangle from circleLeftX to circleRightX
        if (right >= circleLeftX) {
            canvas.save()
            val currentRight = right.coerceAtMost(circleRightX.toInt())
            canvas.drawRect(
                Rect(circleLeftX.toInt(), top, currentRight, bottom),
                paint
            )
            canvas.restore()
        }

        if (right >= circleRightX) {
            canvas.save()
            canvas.clipRect(
                Rect(
                    circleRightX.toInt(),
                    top,
                    right,
                    bottom
                )
            )
            canvas.drawCircle(circleRightX, circleY, circleRadius, paint)
            canvas.restore()
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
        mText = DecimalFormat("0.00%").format(mCurrentProgress / mMaximumProgress)
        val textColor = typedArray.getColor(
            R.styleable.LineTextProgressView_progress_text_color,
            context.getColor(R.color.md_theme_onPrimary)
        )
        setTextColor(textColor)
        val textSize = typedArray.getDimension(
            R.styleable.LineTextProgressView_progress_text_size,
            mDefaultTexSize
        )
        setTextSize(textSize)
        val progressColor = typedArray.getColor(
            R.styleable.LineTextProgressView_progress_color,
            context.getColor(R.color.md_theme_primary)
        )
        setProgressColor(progressColor)
        val progressBackgroundColor = typedArray.getColor(
            R.styleable.LineTextProgressView_progress_background_color,
            context.getColor(R.color.md_theme_primaryContainer)
        )
        setProgressBackgroundColor(progressBackgroundColor)
        mTextBoxStrokeWidth = typedArray.getDimension(
            R.styleable.LineTextProgressView_linetext_progress_text_margin,
            mDefaultTextMargin
        )
        typedArray.recycle()
    }

}