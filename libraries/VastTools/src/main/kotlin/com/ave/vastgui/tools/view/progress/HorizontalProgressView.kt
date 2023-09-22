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
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.annotation.ColorInt
import com.ave.vastgui.core.extension.nothing_to_do
import com.ave.vastgui.tools.R

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/4/3
// Documentation: https://ave.entropy2020.cn/documents/VastTools/core-topics/ui/progress/horizontal-progress-view/

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

    private val mDefaultStrokeWidth
        get() = resources.getDimension(R.dimen.default_horizontal_progress_stroke_width)

    private var mBackgroundDrawable: Drawable? = null
    private var mProgressDrawable: Drawable? = null
    private var mWidth = 0
    private var mHeight = 0
    private val mRectF = RectF()
    private val mPaint = Paint()
    private val mProgressRect: Rect = Rect()
    private val mBackgroundRect: Rect = Rect()

    private val mBackgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        strokeCap = Paint.Cap.ROUND
    }
    private val mProgressPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        strokeCap = Paint.Cap.ROUND
    }

    var mStrokeWidth: Float = mDefaultStrokeWidth

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.EXACTLY) {
            mWidth = MeasureSpec.getSize(widthMeasureSpec)
        }
        if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.EXACTLY) {
            mHeight = MeasureSpec.getSize(heightMeasureSpec)
        }
        mStrokeWidth = mStrokeWidth.coerceAtMost((mHeight / 2).toFloat())
        setMeasuredDimension(mWidth, mHeight)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (mProgressBackgroundColor != 0) {
            mRectF.set(0f, 0f, mWidth.toFloat(), mHeight.toFloat())
            canvas.drawRoundRect(
                mRectF,
                ((mHeight / 2).toFloat()),
                ((mHeight / 2).toFloat()),
                mBackgroundPaint
            )
        }
        if (mProgressDrawable != null) {
            val bitmap: Bitmap = (mProgressDrawable as BitmapDrawable).bitmap
            mProgressRect.set(0, 0, mWidth, mHeight)
            canvas.drawBitmap(bitmap, null, mProgressRect, mPaint)
        }

        val left = mStrokeWidth
        val width = ((mWidth - left) * (mCurrentProgress / mMaximumProgress))
        if (mProgressColor != 0) {
            drawProgress(canvas, left, width, mHeight - 2 * mStrokeWidth, mProgressPaint)
        }
        if (mBackgroundDrawable != null) {
            val bitmap: Bitmap = (mBackgroundDrawable as BitmapDrawable).bitmap
            mBackgroundRect.set(
                left.toInt(),
                mStrokeWidth.toInt(),
                width.toInt(),
                (mHeight - mStrokeWidth).toInt()
            )
            canvas.drawBitmap(bitmap, null, mBackgroundRect, mPaint)
        }
    }

    /**
     * @see nothing_to_do
     * @since 0.5.3
     */
    override fun setText(text: String) {
        nothing_to_do()
    }

    /**
     * @see nothing_to_do
     * @since 0.5.3
     */
    override fun setTextColor(color: Int) {
        nothing_to_do()
    }

    /**
     * @see nothing_to_do
     * @since 0.5.3
     */
    override fun setTextSize(size: Float) {
        nothing_to_do()
    }

    /**
     * Set color-int for [mProgressColor] and [mProgressPaint].
     *
     * @see ProgressView.setProgressColor
     * @since 0.5.3
     */
    override fun setProgressColor(@ColorInt color: Int) {
        super.setProgressColor(color)
        mProgressPaint.color = color
    }

    /**
     * Set color-int for [mProgressBackgroundColor] and [mBackgroundPaint].
     *
     * @see ProgressView.setProgressBackgroundColor
     * @since 0.5.3
     */
    override fun setProgressBackgroundColor(@ColorInt color: Int) {
        super.setProgressBackgroundColor(color)
        mBackgroundPaint.color = color
    }

    /** @since 0.2.0 */
    fun setProgressDrawable(drawable: Drawable) {
        mProgressDrawable = drawable
    }

    /** @since 0.2.0 */
    fun setProgressBkDrawable(drawable: Drawable) {
        mBackgroundDrawable = drawable
    }

    /** @since 0.5.3 */
    private fun drawProgress(
        canvas: Canvas,
        left: Float,
        width: Float,
        height: Float,
        paint: Paint
    ) {
        val top = (mHeight - height) / 2
        if (width - left >= height) {
            mRectF.set(left, top, width, (mHeight - top))
            canvas.drawRoundRect(mRectF, (height / 2), (height / 2), paint)
        }
        mRectF.set(left, top, width, (mHeight - top))
        canvas.clipRect(mRectF)
        val r = height / 2
        canvas.drawCircle((left + r), (top + r), r, paint)
    }

    init {
        val typedArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.HorizontalProgressView,
            defStyleAttr,
            defStyleRes
        )
        mMaximumProgress =
            typedArray.getFloat(
                R.styleable.HorizontalProgressView_progress_maximum_value,
                mDefaultMaximumProgress
            )
        mCurrentProgress =
            typedArray.getFloat(
                R.styleable.HorizontalProgressView_progress_current_value,
                mDefaultCurrentProgress
            )
        val progressColor =
            typedArray.getColor(
                R.styleable.HorizontalProgressView_progress_color,
                context.getColor(R.color.md_theme_primary)
            )
        setProgressColor(progressColor)
        val progressBackgroundColor =
            typedArray.getColor(
                R.styleable.HorizontalProgressView_progress_background_color,
                context.getColor(R.color.md_theme_primaryContainer)
            )
        setProgressBackgroundColor(progressBackgroundColor)
        mBackgroundDrawable =
            typedArray.getDrawable(R.styleable.HorizontalProgressView_horizontal_progress_background_drawable)
        mStrokeWidth =
            typedArray.getDimension(
                R.styleable.HorizontalProgressView_horizontal_progress_stroke_width,
                mDefaultStrokeWidth
            )
        mProgressDrawable =
            typedArray.getDrawable(R.styleable.HorizontalProgressView_horizontal_progress_drawable)
        typedArray.recycle()
    }

}