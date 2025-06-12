/*
 * Copyright 2021-2025 VastGui
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
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.toRect
import androidx.core.graphics.withSave
import com.ave.vastgui.tools.R
import com.ave.vastgui.tools.graphics.BmpUtils
import com.ave.vastgui.tools.utils.dimension
import androidx.core.content.withStyledAttributes
import com.ave.vastgui.tools.utils.ColorUtils
import com.ave.vastgui.tools.utils.color
import androidx.core.graphics.createBitmap

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/4/3
// Documentation: https://sakurajimamaii.github.io/AVE-DOC/documents/tools/core-topics/ui/progress/horizontal-progress-view/

/**
 * HorizontalProgressView
 *
 * @property backgroundPath The path for the canvas to draw the
 * [progressDrawable].
 * @property rectF The scope of the background and progress in the canvas.
 * @property progressDrawable The drawable used to replace the progress
 * color.
 * @property backgroundDrawable The drawable used to replace the progress
 * background color.
 * @since 0.2.0
 */
class HorizontalProgressView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.Default_HorizontalProgressView_Style,
    defStyleRes: Int = R.style.BaseHorizontalProgressView
) : ProgressView(context, attrs, defStyleAttr, defStyleRes) {

    private val xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)

    /** @since 1.5.2 */
    private var backgroundDrawable: Drawable? = null

    /** @since 1.5.2 */
    private var backgroundBitmap: Bitmap? = null

    /** @since 1.5.2 */
    private val backgroundPath = Path()

    /** @since 1.5.2 */
    private var progressDrawable: Drawable? = null

    /** @since 1.5.2 */
    private var progressBitmap: Bitmap? = null

    /** @since 1.5.2 */
    private val rectF = RectF()

    /** @since 1.5.2 */
    private val progressDrawableRectF = RectF()

    /** @since 1.5.2 */
    private val backgroundDrawablePaint = Paint(Paint.ANTI_ALIAS_FLAG)

    /** @since 1.5.2 */
    private val drawablePaint = Paint(Paint.ANTI_ALIAS_FLAG)

    /** @since 1.5.2 */
    private val backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    /** @since 1.5.2 */
    private val progressPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    override var text: String
        get() = throw RuntimeException("You can't get ${this::text.name}.")
        set(_) = throw RuntimeException("You can't set ${this::text.name}.")

    override var textColor: Int
        get() = throw RuntimeException("You can't get ${this::textColor.name}.")
        set(_) = throw RuntimeException("You can't set ${this::textColor.name}.")

    override var textSize: Float
        get() = throw RuntimeException("You can't get ${this::textSize.name}.")
        set(_) = throw RuntimeException("You can't set ${this::textSize.name}.")

    override var progressColor: Int
        get() = progressPaint.color
        set(value) {
            if (progressPaint.color == value) return
            check(ColorUtils.isColorInt(value)) {
                "The color-int(current=${value.toUInt().toString(16)}) of progress is invalid."
            }
            progressPaint.color = value
            invalidate()
        }

    override var progressBackgroundColor: Int
        get() = backgroundPaint.color
        set(value) {
            if (backgroundPaint.color == value) return
            check(ColorUtils.isColorInt(value)) {
                "The color-int(current=${value.toUInt().toString(16)}) of progress background is invalid."
            }
            backgroundPaint.color = value
            invalidate()
        }

    /** @since 1.5.2 */
    private var _strokeWidth: Float = dimension(R.dimen.default_horizontal_progress_stroke_width)

    /**
     * The width of stroke.
     *
     * @since 1.5.2
     */
    var strokeWidth: Float
        get() = _strokeWidth
        set(value) {
            if (_strokeWidth == value) return
            _strokeWidth = value.coerceAtLeast(0f)
            invalidate()
        }

    /** @since 1.5.2 */
    private var _maximumProgress = DEFAULT_MAXIMUM_PROGRESS

    override var maximumProgress: Float
        get() = _maximumProgress
        set(value) {
            _maximumProgress = value.coerceAtLeast(0f)
            resetProgress()
            invalidate()
        }

    /** @since 1.5.2 */
    private var _currentProgress = DEFAULT_CURRENT_PROGRESS

    override var currentProgress: Float
        get() = _currentProgress
        set(value) {
            _currentProgress = value.coerceIn(0f, _maximumProgress)
            invalidate()
        }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val min = measuredWidth.coerceAtMost(measuredHeight)
        _strokeWidth = _strokeWidth.coerceAtMost(min / 2f)
    }

    override fun onDraw(canvas: Canvas) {
        val radius = measuredWidth.coerceAtMost(measuredHeight) / 2f
        val background = backgroundDrawable
        val progress = progressDrawable
        if (progressBackgroundColor != 0 && background == null) {
            rectF.set(paddingStart.toFloat(), paddingTop.toFloat(),
                (measuredWidth - paddingEnd).toFloat(),
                (measuredHeight - paddingBottom).toFloat())
            canvas.drawRoundRect(rectF, radius, radius, backgroundPaint)
        }
        if (background != null) {
            rectF.set(0f, 0f, measuredWidth.toFloat(), measuredHeight.toFloat())
            backgroundBitmap = BmpUtils.getBitmapFromDrawable(background).let {
                BmpUtils.scaleBitmap(it, (right - left), (bottom - top))
            }
            backgroundPath.apply {
                reset()
                addRoundRect(rectF, radius, radius, Path.Direction.CW)
                close()
            }
            canvas.withSave {
                clipPath(backgroundPath)
                canvas.drawBitmap(
                    backgroundBitmap!!, rectF.toRect(), rectF, backgroundDrawablePaint
                )
            }
        }
        val width = (measuredWidth - 2f * _strokeWidth - paddingStart - paddingEnd) * (_currentProgress / _maximumProgress)
        if (progressColor != 0 && progress == null && _currentProgress != 0f) {
            drawProgress(canvas,
                paddingStart + _strokeWidth,
                paddingTop + _strokeWidth,
                measuredWidth - paddingEnd - _strokeWidth,
                measuredHeight - paddingBottom - _strokeWidth,
                width, progressPaint
            )
        }
        if (progress != null && _currentProgress != 0f) {
            drawProgressDrawable(canvas,
                paddingStart + _strokeWidth,
                paddingTop + _strokeWidth,
                measuredWidth - paddingEnd - _strokeWidth,
                measuredHeight - paddingBottom - _strokeWidth,
                width, drawablePaint
            )
        }
    }

    /**
     * Set the drawable image of the progress.
     *
     * @since 0.5.4
     */
    fun setProgressDrawable(@DrawableRes drawable: Int) {
        progressDrawable = runCatching { AppCompatResources.getDrawable(context, drawable) }.getOrNull()
        invalidate()
    }

    /**
     * Set the drawable image of the progress.
     *
     * @since 0.2.0
     */
    fun setProgressDrawable(drawable: Drawable) {
        progressDrawable = drawable
    }

    /**
     * Set the drawable image of the progress background.
     *
     * @since 0.5.4
     */
    fun setProgressBkDrawable(@DrawableRes drawable: Int) {
        backgroundDrawable = AppCompatResources.getDrawable(context, drawable)
    }

    /**
     * Set the drawable image of the progress background.
     *
     * @since 0.2.0
     */
    fun setProgressBkDrawable(drawable: Drawable) {
        backgroundDrawable = drawable
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
    private fun drawProgress(canvas: Canvas, left: Float, top: Float, right: Float, bottom: Float, width: Float, paint: Paint) {
        val radius = (bottom - top).coerceAtMost(right - left) / 2f
        rectF.set(left, top, width, bottom)
        canvas.withSave {
            clipRect(rectF)
            drawRoundRect(left, top, right, bottom, radius, radius, paint)
        }

        if (width - left >= left + radius) {
            canvas.drawRect(
                left + radius, top, width.coerceAtMost(right - radius), bottom, paint
            )
        }

        if (width >= right - radius) {
            rectF.set(right - radius, top, left + width, bottom)
            canvas.withSave {
                clipRect(rectF)
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
    private fun drawProgressDrawable(canvas: Canvas, left: Float, top: Float, right: Float, bottom: Float, width: Float, paint: Paint) {
        val bitmap = createBitmap((right - left).toInt(), (bottom - top).toInt())
        val bitmapCanvas = Canvas(bitmap)
        progressBitmap = BmpUtils.getBitmapFromDrawable(progressDrawable!!).let {
            BmpUtils.scaleBitmap(it, (right - left).toInt(), (bottom - top).toInt())
        }
        val radius = (bottom - top).coerceAtMost(right - left) / 2f
        rectF.set(0f, 0f, width, bottom - top)
        bitmapCanvas.withSave {
            clipRect(rectF)
            drawRoundRect(0f, 0f, right - left, bottom - top, radius, radius, paint)
        }
        if (width >= radius) {
            bitmapCanvas.drawRect(
                radius, 0f, width.coerceAtMost(right - left - radius), bottom - top, paint
            )
        }
        if (width >= right - left - radius) {
            rectF.set(right - left - radius, 0f, width, bottom - top)
            bitmapCanvas.withSave {
                clipRect(rectF)
                drawRoundRect(0f, 0f, right - left, bottom - top, radius, radius, paint)
            }
        }
        progressDrawableRectF.set(0f, 0f, right - left, bottom - top)
        rectF.set(left, top, right, bottom)
        val cs = canvas.saveLayer(rectF, null)
        canvas.drawBitmap(bitmap, progressDrawableRectF.toRect(), rectF, paint)
        paint.xfermode = xfermode
        canvas.drawBitmap(progressBitmap!!, progressDrawableRectF.toRect(), rectF, paint)
        paint.xfermode = null
        canvas.restoreToCount(cs)
    }


    init {
        context.withStyledAttributes(attrs, R.styleable.HorizontalProgressView, defStyleAttr, defStyleRes) {
            _maximumProgress = getFloat(R.styleable.HorizontalProgressView_progress_maximum_value, DEFAULT_MAXIMUM_PROGRESS)
            _currentProgress = getFloat(R.styleable.HorizontalProgressView_progress_current_value, DEFAULT_CURRENT_PROGRESS)
            progressPaint.color = getColor(R.styleable.HorizontalProgressView_progress_color, color(R.color.md_theme_primary))
            backgroundPaint.color = getColor(R.styleable.HorizontalProgressView_progress_background_color, color(R.color.md_theme_primaryContainer))
            _strokeWidth = getDimension(R.styleable.HorizontalProgressView_horizontal_progress_stroke_width, dimension(R.dimen.default_horizontal_progress_stroke_width))
            backgroundDrawable = getDrawable(R.styleable.HorizontalProgressView_horizontal_progress_background_drawable)
            progressDrawable = getDrawable(R.styleable.HorizontalProgressView_horizontal_progress_drawable)
        }
    }

}