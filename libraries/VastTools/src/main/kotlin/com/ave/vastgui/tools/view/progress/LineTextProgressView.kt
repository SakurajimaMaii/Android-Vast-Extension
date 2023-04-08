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
import com.ave.vastgui.core.extension.NotNUllVar
import com.ave.vastgui.tools.R
import com.ave.vastgui.tools.utils.DensityUtils.sp2px
import java.text.DecimalFormat


// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/4/4
// Documentation: https://ave.entropy2020.cn/documents/VastTools/core-topics/ui/progress/HorizontalProgressView/

/**
 * LineTextProgressView
 *
 * @property mTextBoxStrokeWidth The stroke width of the text box.
 * @since 0.2.0
 */
class LineTextProgressView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.Default_LineTextProgressView_Style,
    defStyleRes: Int = R.style.BaseLineTextProgressView
) : ProgressView(context, attrs, defStyleAttr, defStyleRes) {

    private var mWidth = 0
    private var mHeight = 0
    private var mTriangleValue = 8F

    private var mTextPaint: Paint by NotNUllVar()
    private var mProgressPaint: Paint by NotNUllVar()
    private var mBackgroundPaint: Paint by NotNUllVar()
    private var mBoxPaint: Paint by NotNUllVar()
    private var mTextRect: Rect? = null

    private var mTextBoxStrokeWidth = 0f

    init {
        val typedArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.LineTextProgressView,
            defStyleAttr,
            defStyleRes
        )
        mMaximumProgress =
            typedArray.getFloat(R.styleable.LineTextProgressView_progress_maximum_value, 0f)
        mCurrentProgress =
            typedArray.getFloat(R.styleable.LineTextProgressView_progress_current_value, 0f)
        mText = DecimalFormat("0.00%").format(mCurrentProgress / mMaximumProgress)
        mTextColor = typedArray.getColor(
            R.styleable.LineTextProgressView_progress_text_color,
            context.getColor(R.color.md_theme_onPrimary)
        )
        mTextSize = typedArray.getDimension(
            R.styleable.LineTextProgressView_progress_text_size,
            0f
        )
        mProgressColor = typedArray.getColor(
            R.styleable.LineTextProgressView_progress_color,
            context.getColor(R.color.md_theme_primary)
        )
        mProgressBackgroundColor = typedArray.getColor(
            R.styleable.LineTextProgressView_progress_background_color,
            context.getColor(R.color.md_theme_primaryContainer)
        )
        mTextBoxStrokeWidth = typedArray.getDimension(
            R.styleable.LineTextProgressView_linetext_progress_textbox_stroke_width,
            0f
        )
        typedArray.recycle()
        setBoxPaint()
        setTextPaint()
        setProgressPaint()
        setBackgroundPaint()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.EXACTLY) {
            mWidth = MeasureSpec.getSize(widthMeasureSpec)
        }
        if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.EXACTLY) {
            mHeight = MeasureSpec.getSize(heightMeasureSpec)
        }
        mProgressPaint.strokeWidth = mHeight.toFloat()
        mBackgroundPaint.strokeWidth = mHeight.toFloat()
        setMeasuredDimension(mWidth, mHeight)
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val str = "100.00%"
        mTextRect = Rect()
        mTextPaint.getTextBounds(str, 0, str.length, mTextRect)
        val boxWidth = mTextRect!!.width() + (mTextBoxStrokeWidth * 2).toInt()
        val boxHeight = mTextRect!!.height() + (mTextBoxStrokeWidth * 2).toInt()
        val outWidth = (mCurrentProgress / mMaximumProgress * (mWidth - boxWidth)).toInt()
        drawBox(canvas, outWidth, boxWidth, boxHeight)
        mTextRect = null

        val metrics = mTextPaint.fontMetricsInt
        val dy = (metrics.bottom - metrics.top) / 2 - metrics.bottom
        val baseLine = boxHeight / 2 + dy

        mTextRect = Rect()
        mTextPaint.getTextBounds(mText, 0, mText!!.length, mTextRect)
        canvas.drawText(
            mText!!,
            (outWidth + (boxWidth / 2 - mTextRect!!.width() / 2)).toFloat(),
            baseLine.toFloat(),
            mTextPaint
        )
        mTextRect = null

        // You should not change its draw order.
        drawProgress(
            canvas,
            boxWidth / 2,
            boxHeight + sp2px(mTriangleValue).toInt(),
            mWidth,
            mHeight,
            mBackgroundPaint
        )
        drawProgress(
            canvas,
            boxWidth / 2,
            boxHeight + sp2px(mTriangleValue).toInt(),
            boxWidth / 2 + outWidth,
            mHeight,
            mProgressPaint
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
            strokeCap = Paint.Cap.ROUND
        }
    }

    /**
     * @since 0.2.0
     */
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
        path.moveTo(left + width / 2 - sp2px(4f), height.toFloat())
        path.lineTo(left + width / 2 + sp2px(4f), height.toFloat())
        path.lineTo((left + width / 2).toFloat(), height + sp2px(5f))
        canvas.drawPath(path, mBoxPaint)
    }

    /**
     * Draw progress.
     *
     * @since 0.2.0
     */
    private fun drawProgress(canvas: Canvas, left: Int, top: Int, right: Int, bottom: Int, paint: Paint) {
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

    /**
     * Set [mTextBoxStrokeWidth].
     *
     * @since 0.2.0
     */
    fun setTextBoxStrokeWidth(width: Float){
        mTextBoxStrokeWidth = width
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