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
import android.graphics.Path
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.toRect
import androidx.core.graphics.withSave
import com.ave.vastgui.core.extension.nothing_to_do
import com.ave.vastgui.tools.R
import com.ave.vastgui.tools.graphics.BmpUtils

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/4/3
// Documentation: https://ave.entropy2020.cn/documents/VastTools/core-topics/ui/progress/horizontal-progress-view/

/**
 * HorizontalProgressView
 *
 * @property mBackgroundPath The path for the canvas to draw the
 *     [mProgressDrawable].
 * @property mRectF The scope of the background and progress in the canvas.
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

    private val mXfermode
        get() = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
    private var mBackgroundDrawable: Drawable? = null
    private var mBackgroundBitmap: Bitmap? = null
    private val mBackgroundPath = Path()
    private var mProgressDrawable: Drawable? = null
    private var mProgressBitmap: Bitmap? = null
    private val mRectF = RectF()
    private val mProgressDrawableRectF = RectF()
    private val mBackgroundDrawablePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mDrawablePaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val mBackgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        strokeCap = Paint.Cap.ROUND
    }
    private val mProgressPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        strokeCap = Paint.Cap.ROUND
    }

    var mStrokeWidth: Float = mDefaultStrokeWidth
        set(value) {
            if (value < 0f) return
            field = value
        }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val min = measuredWidth.coerceAtMost(measuredHeight)
        mStrokeWidth = mStrokeWidth.coerceAtMost(min / 2f)
    }

    override fun onDraw(canvas: Canvas) {
        val radius = measuredWidth.coerceAtMost(measuredHeight) / 2f
        if (mProgressBackgroundColor != 0 && mBackgroundDrawable == null) {
            mRectF.set(0f, 0f, measuredWidth.toFloat(), measuredHeight.toFloat())
            canvas.drawRoundRect(
                mRectF, radius, radius, mBackgroundPaint
            )
        }
        if (mBackgroundDrawable != null) {
            mRectF.set(0f, 0f, measuredWidth.toFloat(), measuredHeight.toFloat())
            mBackgroundBitmap = BmpUtils.getBitmapFromDrawable(mBackgroundDrawable!!).let {
                BmpUtils.scaleBitmap(it, (right - left), (bottom - top))
            }
            mBackgroundPath.apply {
                reset()
                addRoundRect(mRectF, radius, radius, Path.Direction.CW)
                close()
            }
            canvas.withSave {
                clipPath(mBackgroundPath)
                canvas.drawBitmap(
                    mBackgroundBitmap!!, mRectF.toRect(), mRectF, mBackgroundDrawablePaint
                )
            }
        }
        val width = ((measuredWidth - 2 * mStrokeWidth) * (mCurrentProgress / mMaximumProgress))
        if (mProgressColor != 0 && mProgressDrawable == null && mCurrentProgress != 0f) {
            drawProgress(
                canvas,
                mStrokeWidth,
                mStrokeWidth,
                measuredWidth - mStrokeWidth,
                measuredHeight - mStrokeWidth,
                width,
                mProgressPaint
            )
        }
        if (mProgressDrawable != null && mCurrentProgress != 0f) {
            drawProgressDrawable(
                canvas,
                mStrokeWidth,
                mStrokeWidth,
                measuredWidth - mStrokeWidth,
                measuredHeight - mStrokeWidth,
                width,
                mDrawablePaint
            )
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

    /**
     * Set the drawable image of the progress.
     *
     * @since 0.5.4
     */
    fun setProgressDrawable(@DrawableRes drawable: Int) {
        mProgressDrawable = AppCompatResources.getDrawable(context, drawable)
    }

    /**
     * Set the drawable image of the progress.
     *
     * @since 0.2.0
     */
    fun setProgressDrawable(drawable: Drawable) {
        mProgressDrawable = drawable
    }

    /**
     * Set the drawable image of the progress background.
     *
     * @since 0.5.4
     */
    fun setProgressBkDrawable(@DrawableRes drawable: Int) {
        mBackgroundDrawable = AppCompatResources.getDrawable(context, drawable)
    }

    /**
     * Set the drawable image of the progress background.
     *
     * @since 0.2.0
     */
    fun setProgressBkDrawable(drawable: Drawable) {
        mBackgroundDrawable = drawable
    }

    /**
     * Draw progress.
     *
     * @param left The left of the progress.
     * @param top The left of the progress.
     * @param right The right of the progress.
     * @param bottom The bottom of the progress.
     * @param width The width of the progress from [left].
     * @since 0.5.4
     */
    private fun drawProgress(
        canvas: Canvas,
        left: Float,
        top: Float,
        right: Float,
        bottom: Float,
        width: Float,
        paint: Paint
    ) {
        val radius = (bottom - top).coerceAtMost(right - left) / 2f
        mRectF.set(left, top, width, bottom)
        canvas.withSave {
            clipRect(mRectF)
            drawRoundRect(left, top, right, bottom, radius, radius, paint)
        }

        if (width - left >= left + radius) {
            canvas.drawRect(
                left + radius, top, width.coerceAtMost(right - radius), bottom, paint
            )
        }

        if (width >= right - radius) {
            mRectF.set(right - radius, top, left + width, bottom)
            canvas.withSave {
                clipRect(mRectF)
                drawRoundRect(left, top, right, bottom, radius, radius, paint)
            }
        }
    }

    /**
     * Draw progress drawable.
     *
     * @param left The left of the progress.
     * @param top The left of the progress.
     * @param right The right of the progress.
     * @param bottom The bottom of the progress.
     * @param width The width of the progress from [left].
     * @since 0.5.4
     */
    private fun drawProgressDrawable(
        canvas: Canvas,
        left: Float,
        top: Float,
        right: Float,
        bottom: Float,
        width: Float,
        paint: Paint
    ) {
        setLayerType(LAYER_TYPE_SOFTWARE, null)
        val bitmap = Bitmap.createBitmap(
            (right - left).toInt(), (bottom - top).toInt(), Bitmap.Config.ARGB_8888
        )
        val bitmapCanvas = Canvas(bitmap)
        mProgressBitmap = BmpUtils.getBitmapFromDrawable(mProgressDrawable!!).let {
            BmpUtils.scaleBitmap(it, (right - left).toInt(), (bottom - top).toInt())
        }
        val radius = (bottom - top).coerceAtMost(right - left) / 2f
        mRectF.set(0f, 0f, width, bottom - top)
        bitmapCanvas.withSave {
            clipRect(mRectF)
            drawRoundRect(0f, 0f, right - left, bottom - top, radius, radius, paint)
        }
        if (width >= radius) {
            bitmapCanvas.drawRect(
                radius, 0f, width.coerceAtMost(right - left - radius), bottom - top, paint
            )
        }
        if (width >= right - left - radius) {
            mRectF.set(right - left - radius, 0f, width, bottom - top)
            bitmapCanvas.withSave {
                clipRect(mRectF)
                drawRoundRect(0f, 0f, right - left, bottom - top, radius, radius, paint)
            }
        }
        mProgressDrawableRectF.set(0f, 0f, right - left, bottom - top)
        mRectF.set(left, top, right, bottom)
        val cs = canvas.saveLayer(mRectF, null)
        canvas.drawBitmap(bitmap, mProgressDrawableRectF.toRect(), mRectF, paint)
        paint.xfermode = mXfermode
        canvas.drawBitmap(mProgressBitmap!!, mProgressDrawableRectF.toRect(), mRectF, paint)
        paint.xfermode = null
        canvas.restoreToCount(cs)
    }


    init {
        val typedArray = context.obtainStyledAttributes(
            attrs, R.styleable.HorizontalProgressView, defStyleAttr, defStyleRes
        )
        mMaximumProgress = typedArray.getFloat(
            R.styleable.HorizontalProgressView_progress_maximum_value, mDefaultMaximumProgress
        )
        mCurrentProgress = typedArray.getFloat(
            R.styleable.HorizontalProgressView_progress_current_value, mDefaultCurrentProgress
        )
        val progressColor = typedArray.getColor(
            R.styleable.HorizontalProgressView_progress_color,
            context.getColor(R.color.md_theme_primary)
        )
        setProgressColor(progressColor)
        val progressBackgroundColor = typedArray.getColor(
            R.styleable.HorizontalProgressView_progress_background_color,
            context.getColor(R.color.md_theme_primaryContainer)
        )
        setProgressBackgroundColor(progressBackgroundColor)
        mStrokeWidth = typedArray.getDimension(
            R.styleable.HorizontalProgressView_horizontal_progress_stroke_width, mDefaultStrokeWidth
        )
        mBackgroundDrawable =
            typedArray.getDrawable(R.styleable.HorizontalProgressView_horizontal_progress_background_drawable)
        mProgressDrawable =
            typedArray.getDrawable(R.styleable.HorizontalProgressView_horizontal_progress_drawable)
        typedArray.recycle()
    }

}