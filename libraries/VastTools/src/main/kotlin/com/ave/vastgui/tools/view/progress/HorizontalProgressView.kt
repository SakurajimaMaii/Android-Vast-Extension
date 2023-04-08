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
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import com.ave.vastgui.core.extension.NotNUllVar
import com.ave.vastgui.tools.R

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/4/3
// Documentation: https://ave.entropy2020.cn/documents/VastTools/core-topics/ui/progress/HorizontalProgressView/

/**
 * HorizontalProgressView
 *
 * @property mProgressDrawable The drawable used to replace the progress
 *     color.
 * @property mBackgroundDrawable The drawable used to replace the progress
 *     background color.
 * @property mStrokeWidth Progress stroke width.
 * @since 0.2.0
 */
class HorizontalProgressView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.Default_HorizontalProgressView_Style,
    defStyleRes: Int = R.style.BaseHorizontalProgressView
) : ProgressView(context, attrs, defStyleAttr, defStyleRes) {
    private var mBackgroundDrawable: Drawable? = null
    private var mProgressDrawable: Drawable? = null
    private var mStrokeWidth = 0F
    private var mWidth = 0
    private var mHeight = 0

    private var mBackgroundPaint: Paint by NotNUllVar()
    private var mProgressPaint: Paint by NotNUllVar()
    private val mPaint = Paint()

    private var mProgressRect: Rect? = null
    private var mBackgroundRect: Rect? = null

    init {
        val typedArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.HorizontalProgressView,
            defStyleAttr,
            defStyleRes
        )
        mMaximumProgress =
            typedArray.getFloat(R.styleable.HorizontalProgressView_progress_maximum_value, 0f)
        mCurrentProgress =
            typedArray.getFloat(R.styleable.HorizontalProgressView_progress_current_value, 0f)
        mProgressColor =
            typedArray.getColor(
                R.styleable.HorizontalProgressView_progress_color,
                context.getColor(R.color.md_theme_primary)
            )
        mProgressBackgroundColor =
            typedArray.getColor(
                R.styleable.HorizontalProgressView_progress_background_color,
                context.getColor(R.color.md_theme_primaryContainer)
            )
        mBackgroundDrawable =
            typedArray.getDrawable(R.styleable.HorizontalProgressView_horizontal_progress_background_drawable)
        mStrokeWidth =
            typedArray.getDimension(
                R.styleable.HorizontalProgressView_horizontal_progress_stroke_width,
                0F
            )
        mProgressDrawable =
            typedArray.getDrawable(R.styleable.HorizontalProgressView_horizontal_progress_drawable)
        typedArray.recycle()
        setBackgroundPaint()
        setProgressPaint()
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
        mProgressPaint.strokeWidth = mHeight.toFloat() - 2 * mStrokeWidth
        setMeasuredDimension(mWidth, mHeight)
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (mProgressColor != 0) {
            drawProgressBackground(canvas, mWidth.toFloat(), mHeight.toFloat(), mProgressPaint)
        }
        if (mProgressDrawable != null) {
            val bitmap: Bitmap = (mProgressDrawable as BitmapDrawable).bitmap
            mProgressRect = Rect(0, 0, mWidth, mHeight)
            canvas.drawBitmap(bitmap, null, mProgressRect!!, mPaint)
        }
        mProgressRect = null

        val left = mStrokeWidth
        val width = ((mWidth - left) * (mCurrentProgress / mMaximumProgress))
        if (mProgressBackgroundColor != 0) {
            drawBackground(canvas, left, width, mHeight - 2 * mStrokeWidth, mBackgroundPaint)
        }
        if (mBackgroundDrawable != null) {
            val bitmap: Bitmap = (mBackgroundDrawable as BitmapDrawable).bitmap
            mBackgroundRect = Rect(
                left.toInt(),
                mStrokeWidth.toInt(),
                width.toInt(),
                (mHeight - mStrokeWidth).toInt()
            )
            canvas.drawBitmap(bitmap, null, mBackgroundRect!!, mPaint)
            mBackgroundRect = null
        }
    }


    /**
     * Initialize [mProgressPaint].
     *
     * @since 0.2.0
     */
    private fun setProgressPaint() {
        mProgressPaint = Paint().apply {
            isAntiAlias = true
            color = mProgressBackgroundColor
            strokeCap = Paint.Cap.ROUND
        }
    }

    /**
     * Initialize [mBackgroundPaint].
     *
     * @since 0.2.0
     */
    private fun setBackgroundPaint() {
        mBackgroundPaint = Paint().apply {
            isAntiAlias = true
            color = mProgressColor
            strokeCap = Paint.Cap.ROUND
        }
    }

    /** @since 0.2.0 */
    private fun drawProgressBackground(canvas: Canvas, width: Float, height: Float, paint: Paint) {
        val rectF = RectF(0F, (mHeight - height), width, mHeight.toFloat())
        canvas.drawRoundRect(rectF, (height / 2), (height / 2), paint)
    }

    /** @since 0.2.0 */
    private fun drawBackground(
        canvas: Canvas,
        left: Float,
        width: Float,
        height: Float,
        paint: Paint
    ) {
        val top = (mHeight - height) / 2
        if (width - left >= height) {
            val rectF = RectF(left, top, width, (mHeight - top))
            canvas.drawRoundRect(rectF, (height / 2), (height / 2), paint)
        }
        val rectF = RectF(left, top, width, (mHeight - top))
        canvas.clipRect(rectF)
        val r = height / 2
        canvas.drawCircle((left + r), (top + r), r, paint)
    }

    /**
     * @since 0.2.0
     */
    fun setProgressDrawable(drawable: Drawable){
        mProgressDrawable = drawable
    }

    /**
     * @since 0.2.0
     */
    fun setProgressBkDrawable(drawable: Drawable){
        mBackgroundDrawable = drawable
    }

    /**
     * @since 0.2.0
     */
    fun setStrokeColor(width: Float){
        mStrokeWidth = width
    }

}