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
import android.graphics.Rect
import android.graphics.RectF
import android.util.AttributeSet
import com.ave.vastgui.core.extension.NotNUllVar
import com.ave.vastgui.tools.R
import com.ave.vastgui.tools.utils.DensityUtils
import java.text.DecimalFormat


// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/4/4
// Documentation: https://ave.entropy2020.cn/documents/VastTools/core-topics/ui/progress/HorizontalProgressView/
/**
 * HorizontalTextProgressView
 *
 * @property mProgressHeight The height of the progress. The height should
 *     not be less than 40% of the maximum height.The maximum height is the
 *     maximum height of the widget minus twice the mTextBoxStrokeWidth.
 * @property mTextBoxStrokeWidth The stroke width of the text.
 * @since 0.2.0
 */
class HorizontalTextProgressView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.Default_HorizontalTextProgressView_Style,
    defStyleRes: Int = R.style.BaseHorizontalTextProgressView
) : ProgressView(context, attrs, defStyleAttr, defStyleRes) {

    private var mWidth = 0
    private var mHeight = 0
    private var mBoxWidth = 0

    private var mTextPaint: Paint by NotNUllVar()
    private var mProgressPaint: Paint by NotNUllVar()
    private var mBackgroundPaint: Paint by NotNUllVar()
    private var mBoxPaint: Paint by NotNUllVar()

    private var mProgressHeight = 0f
    private var mTextBoxStrokeWidth = 0f

    init {
        val typedArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.HorizontalTextProgressView,
            defStyleAttr,
            defStyleRes
        )
        mMaximumProgress =
            typedArray.getFloat(R.styleable.HorizontalTextProgressView_progress_maximum_value, 0f)
        mCurrentProgress =
            typedArray.getFloat(R.styleable.HorizontalTextProgressView_progress_current_value, 0f)
        mText = DecimalFormat("0.00%").format(mCurrentProgress / mMaximumProgress)
        mTextColor = typedArray.getColor(
            R.styleable.HorizontalTextProgressView_progress_text_color,
            context.getColor(R.color.md_theme_onPrimary)
        )
        mTextSize = typedArray.getDimension(
            R.styleable.HorizontalTextProgressView_progress_text_size,
            0f
        )
        mProgressColor = typedArray.getColor(
            R.styleable.HorizontalTextProgressView_progress_color,
            context.getColor(R.color.md_theme_primary)
        )
        mProgressBackgroundColor = typedArray.getColor(
            R.styleable.HorizontalTextProgressView_progress_background_color,
            context.getColor(R.color.md_theme_primaryContainer)
        )
        mProgressHeight = typedArray.getDimension(
            R.styleable.HorizontalTextProgressView_horizontal_text_progress_height,
            0f
        )
        mTextBoxStrokeWidth = typedArray.getDimension(
            R.styleable.HorizontalTextProgressView_horizontal_text_progress_textbox_stroke_width,
            0f
        )
        typedArray.recycle()
        setTextPaint()
        setProgressPaint()
        setBackgroundPaint()
        setBoxPaint()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.EXACTLY) {
            mWidth = MeasureSpec.getSize(widthMeasureSpec)
        }
        if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.EXACTLY) {
            mHeight = MeasureSpec.getSize(heightMeasureSpec)
        }
        // Ensure the display of the progress bar.
        val maxHeight = mHeight - 2 * mTextBoxStrokeWidth
        if (mProgressHeight >= maxHeight || mProgressHeight <= 0.4 * maxHeight) {
            mProgressHeight = (maxHeight * 0.4).toFloat()
        }
        mProgressPaint.strokeWidth = mProgressHeight
        mBackgroundPaint.strokeWidth = mProgressHeight
        setMeasuredDimension(mWidth, mHeight)
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val str = "100.00%"
        val mTextRect = Rect()
        mTextPaint.getTextBounds(str, 0, str.length, mTextRect)
        mBoxWidth = (mTextBoxStrokeWidth * 2 + mTextRect.width()).toInt()
        var outWidth = (mCurrentProgress / mMaximumProgress * mWidth).toInt()
        // Ensure that the right side of the TextBox will not cross the border.
        if (outWidth >= mWidth - mBoxWidth / 2) {
            outWidth = mWidth - mBoxWidth / 2
        }

        val inTop = ((mHeight - mProgressHeight) / 2).toInt()
        val outTop = ((mHeight - mProgressHeight) / 2).toInt()
        drawProgress(
            canvas,
            0,
            outTop,
            mWidth,
            mHeight - outTop,
            mBackgroundPaint
        )
        drawProgress(
            canvas,
            0,
            inTop,
            (outWidth + DensityUtils.sp2px(2f)).toInt(),
            mHeight - inTop,
            mProgressPaint
        )

        // Ensure that the left side of the TextBox will not cross the border.
        val boxStart = if (outWidth < mBoxWidth / 2) {
            mBoxWidth / 2
        } else outWidth
        drawBox(canvas, boxStart, mBoxWidth, (mProgressHeight + 2 * mTextBoxStrokeWidth).toInt())
        val metrics = mTextPaint.fontMetricsInt
        val dy = (metrics.bottom - metrics.top) / 2 - metrics.bottom
        val baseLine = mHeight / 2 + dy

        val bound = Rect()
        mTextPaint.getTextBounds(mText, 0, mText!!.length, bound)
        canvas.drawText(
            mText!!,
            (boxStart - bound.width() / 2).toFloat(),
            baseLine.toFloat(),
            mTextPaint
        )
    }

    /**
     * Set [mBoxPaint].
     *
     * @since 0.2.0
     */
    private fun setBoxPaint() {
        mBoxPaint = Paint().apply {
            isAntiAlias = true
            color = mProgressColor
        }
    }

    /**
     * Set [mTextPaint].
     *
     * @since 0.2.0
     */
    private fun setTextPaint() {
        mTextPaint = Paint().apply {
            isAntiAlias = true
            color = mTextColor
            textSize = mTextSize
        }
    }


    /**
     * Set [mProgressPaint].
     *
     * @since 0.2.0
     */
    private fun setProgressPaint() {
        mProgressPaint = Paint().apply {
            isAntiAlias = true
            color = mProgressColor
            strokeWidth = 80f
            strokeCap = Paint.Cap.ROUND
        }
    }

    /**
     * Set [mBackgroundPaint].
     *
     * @since 0.2.0
     */
    private fun setBackgroundPaint() {
        mBackgroundPaint = Paint().apply {
            isAntiAlias = true
            color = mProgressBackgroundColor
            strokeWidth = 80f
            strokeCap = Paint.Cap.ROUND
        }
    }

    /**
     * Draw text box.
     *
     * @since 0.2.0
     */
    private fun drawBox(canvas: Canvas, left: Int, width: Int, height: Int) {
        val rectF =
            RectF(
                (left - width / 2).toFloat(),
                ((mHeight - height) / 2).toFloat(),
                (left + width / 2).toFloat(),
                ((mHeight + height) / 2).toFloat()
            )
        canvas.drawRoundRect(rectF, (height / 2).toFloat(), (height / 2).toFloat(), mBoxPaint)
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
        val height = bottom - top
        val r = height / 2
        val cFirstX = left + r
        val cSecondX = mWidth - left - r
        val cy = top + r

        canvas.save()
        canvas.clipRect(RectF(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat()))
        canvas.drawCircle((left + r).toFloat(), cy.toFloat(), r.toFloat(), paint)
        canvas.restore()

        if (right >= cFirstX) {
            canvas.save()
            var currentRight = right
            if (right > cSecondX) {
                currentRight = cSecondX
            }
            canvas.drawRect(
                RectF(
                    (left + r).toFloat(),
                    top.toFloat(),
                    currentRight.toFloat(),
                    bottom.toFloat()
                ),
                paint
            )
            canvas.restore()
        }

        if (right >= cSecondX) {
            canvas.save()
            canvas.clipRect(
                RectF(
                    cSecondX.toFloat(),
                    top.toFloat(),
                    right.toFloat(),
                    bottom.toFloat()
                )
            )
            canvas.drawCircle(cSecondX.toFloat(), cy.toFloat(), r.toFloat(), paint)
            canvas.restore()
        }
    }

    override fun setCurrentProgress(currentProgress: Float) {
        if (currentProgress > mMaximumProgress && mMaximumProgress != 0.0f)
            throw IllegalStateException("The currentProgress should be smaller than $mMaximumProgress")
        mCurrentProgress = currentProgress
        mText = DecimalFormat("0.00%").format(mCurrentProgress / mMaximumProgress)
    }

    /**
     * You should call [setCurrentProgress] in order to set [mText].
     *
     * @since 0.2.0
     */
    override fun setText(text: String) {
        throw RuntimeException("You shouldn't call this method.")
    }

}