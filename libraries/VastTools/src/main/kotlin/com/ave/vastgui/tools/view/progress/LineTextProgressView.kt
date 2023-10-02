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

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import androidx.annotation.ColorInt
import androidx.annotation.FloatRange
import androidx.core.graphics.withSave
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
 * @property mProgressHeight The height of progress.
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

    var mProgressHeight = mDefaultProgressHeight
        private set
    var mTextMargin = mDefaultTextMargin
        private set

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val neededHeight = resolveSize(
            (mProgressHeight + 2 * mTextMargin + mTriangleHeight + getTextHeight()).toInt(),
            heightMeasureSpec
        )
        setMeasuredDimension(measuredWidth, neededHeight)
    }

    override fun onDraw(canvas: Canvas) {
        val fontMetrics = mTextPaint.fontMetrics
        val textHeight = fontMetrics.bottom - fontMetrics.top
        val baseWidth = mTextPaint.measureText("100.00%")

        val boxWidth: Float = baseWidth + (mTextMargin * 2f)
        val boxHeight: Float = textHeight + (mTextMargin * 2f)
        val boxLeft: Float = mCurrentProgress / mMaximumProgress * (measuredWidth - boxWidth)
        drawBox(canvas, boxLeft, boxWidth, boxHeight)

        val textWidth: Float = mTextPaint.measureText(mText)
        // The distance between the baseline and text central axis
        val textCenter2Bottom: Float = textHeight / 2 - fontMetrics.bottom
        val textX: Float = boxLeft + (boxWidth / 2 - textWidth / 2)
        val textY: Float = boxHeight / 2 + textCenter2Bottom

        canvas.drawText(mText, textX, textY, mTextPaint)

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
     * Set progress height.
     *
     * @since 0.5.4
     */
    fun setProgressHeight(@FloatRange(from = 0.0) height: Float) {
        mProgressHeight = height
    }

    /**
     * Set [mTextMargin].
     *
     * @since 0.5.4
     */
    fun setTextMargin(width: Float) {
        mTextMargin = width
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

    /**
     * Get text height.
     *
     * @since 0.5.4
     */
    private fun getTextHeight(): Float {
        val fontMetrics = mTextPaint.fontMetrics
        return fontMetrics.bottom - fontMetrics.top
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
        mProgressHeight = typedArray.getDimension(
            R.styleable.LineTextProgressView_linetext_progress_height,
            mDefaultProgressHeight
        )
        mTextMargin = typedArray.getDimension(
            R.styleable.LineTextProgressView_linetext_progress_text_margin,
            mDefaultTextMargin
        )
        typedArray.recycle()
    }

}