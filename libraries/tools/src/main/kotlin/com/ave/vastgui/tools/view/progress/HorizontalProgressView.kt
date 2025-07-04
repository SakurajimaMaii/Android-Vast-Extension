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
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.annotation.DrawableRes
import androidx.core.content.withStyledAttributes
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.drawable.toBitmapOrNull
import androidx.core.graphics.withClip
import com.ave.vastgui.tools.R
import com.ave.vastgui.tools.utils.ColorUtils
import com.ave.vastgui.tools.utils.color
import com.ave.vastgui.tools.utils.dimension
import kotlin.math.max
import kotlin.math.min

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/4/3
// Documentation: https://sakurajimamaii.github.io/AVE-DOC/documents/tools/core-topics/ui/progress/horizontal-progress-view/

/**
 * [HorizontalProgressView]
 *
 * @since 0.2.0
 */
class HorizontalProgressView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.Default_HorizontalProgressView_Style,
    defStyleRes: Int = R.style.BaseHorizontalProgressView
) : ProgressView(context, attrs, defStyleAttr, defStyleRes) {

    /**
     * The [Matrix] of [progressBackgroundBitmap].
     *
     * @since 1.5.2
     */
    private val progressBackgroundMatrix: Matrix = Matrix()

    /**
     * The [Matrix] of [progressBitmap].
     *
     * @since 1.5.2
     */
    private val progressMatrix: Matrix = Matrix()

    /**
     * The background bitmap of progress.
     *
     * @since 1.5.2
     */
    private var progressBackgroundBitmap: Bitmap? = null

    /**
     * The background bitmap of progress.
     *
     * @since 1.5.2
     */
    private var progressBitmap: Bitmap? = null

    /** @since 1.5.2 */
    private val progressBackgroundPath = Path()

    /** @since 1.5.2 */
    private val progressPath: Path = Path()

    /** @since 1.5.2 */
    private val rectF = RectF()

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
            _currentProgress = value.coerceIn(0f, maximumProgress)
            invalidate()
        }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val min = measuredWidth.coerceAtMost(measuredHeight)
        _strokeWidth = strokeWidth.coerceAtMost(min / 2f)
    }

    override fun onDraw(canvas: Canvas) {
        val radius = measuredWidth.coerceAtMost(measuredHeight) / 2f
        val progressBackground = progressBackgroundBitmap
        val progress = progressBitmap
        rectF.set(paddingStart.toFloat(), paddingTop.toFloat(),
            (measuredWidth - paddingEnd).toFloat(),
            (measuredHeight - paddingBottom).toFloat())
        if (progressBackgroundColor != 0 && progressBackground == null) {
            canvas.drawRoundRect(rectF, radius, radius, backgroundPaint)
        } else if (progressBackground != null) {
            progressBackgroundPath.reset()
            progressBackgroundPath.addRoundRect(rectF, radius, radius, Path.Direction.CW)
            canvas.withClip(progressBackgroundPath) {
                canvas.drawBitmap(progressBackground, progressBackgroundMatrix, backgroundDrawablePaint)
            }
        }
        val width = (measuredWidth - (2f * strokeWidth) - paddingStart - paddingEnd) * (currentProgress / maximumProgress)
        rectF.set(rectF.left + strokeWidth, rectF.top + strokeWidth, rectF.right - strokeWidth, rectF.bottom - strokeWidth)
        if (progressColor != 0 && progress == null && currentProgress != 0f) {
            drawProgress(canvas, rectF, width, progressPaint)
        } else if (progress != null && currentProgress != 0f) {
            drawProgressDrawable(canvas, rectF, width, drawablePaint)
        }
    }

    /**
     * Set the drawable image of the progress.
     *
     * @since 0.5.4
     */
    fun setProgressDrawable(@DrawableRes id: Int) {
        val bmpById = runCatching { BitmapFactory.decodeResource(context.resources, id) }.getOrNull()
        progressBitmap = bmpById
        if (null != bmpById) {
            progressMatrix.reset()
            val targetX = (measuredWidth - 2 * strokeWidth - paddingStart - paddingEnd)
            val targetY = (measuredHeight - 2 * strokeWidth - paddingTop - paddingBottom)
            val scale = max(targetX / bmpById.width, targetY / bmpById.height)
            progressMatrix.postScale(scale, scale)
            val scaledWidth = bmpById.width * scale
            val scaledHeight = bmpById.height * scale
            val dx: Float = (measuredWidth - paddingEnd + paddingStart - scaledWidth) / 2
            val dy: Float = (measuredHeight - paddingBottom + paddingTop - scaledHeight) / 2
            progressMatrix.postTranslate(dx, dy)
        }
        invalidate()
    }

    /**
     * Set the drawable image of the progress.
     *
     * @since 0.2.0
     */
    fun setProgressDrawable(drawable: Drawable?) {
        val bmpById = drawable?.toBitmapOrNull()
        progressBitmap = bmpById
        if (null != bmpById) {
            progressMatrix.reset()
            val targetX = (measuredWidth - 2 * strokeWidth - paddingStart - paddingEnd)
            val targetY = (measuredHeight - 2 * strokeWidth - paddingTop - paddingBottom)
            val scale = max(targetX / bmpById.width, targetY / bmpById.height)
            progressMatrix.postScale(scale, scale)
            val scaledWidth = bmpById.width * scale
            val scaledHeight = bmpById.height * scale
            val dx: Float = (measuredWidth - paddingEnd + paddingStart - scaledWidth) / 2
            val dy: Float = (measuredHeight - paddingBottom + paddingTop - scaledHeight) / 2
            progressMatrix.postTranslate(dx, dy)
        }
        invalidate()
    }

    /**
     * Set the drawable image of the progress background.
     *
     * @since 0.5.4
     */
    fun setProgressBkDrawable(@DrawableRes id: Int) {
        val bmpById = runCatching { BitmapFactory.decodeResource(context.resources, id) }.getOrNull()
        progressBackgroundBitmap = bmpById
        if (null != bmpById) {
            progressBackgroundMatrix.reset()
            val targetX = (measuredWidth - paddingStart - paddingEnd).toFloat()
            val targetY = (measuredHeight - paddingTop - paddingBottom).toFloat()
            val scale = max(targetX / bmpById.width, targetY / bmpById.height)
            progressBackgroundMatrix.postScale(scale, scale)
            val scaledWidth = bmpById.width * scale
            val scaledHeight = bmpById.height * scale
            val dx: Float = (measuredWidth - paddingEnd + paddingStart - scaledWidth) / 2f
            val dy: Float = (measuredHeight - paddingBottom + paddingTop - scaledHeight) / 2f
            progressBackgroundMatrix.postTranslate(dx, dy)
        }
        invalidate()
    }

    /**
     * Set the drawable image of the progress background.
     *
     * @since 0.2.0
     */
    fun setProgressBkDrawable(drawable: Drawable?) {
        val bmpByDrawable = drawable?.toBitmap()
        progressBackgroundBitmap = bmpByDrawable
        if (null != bmpByDrawable) {
            progressBackgroundMatrix.reset()
            val targetX = (measuredWidth - paddingStart - paddingEnd).toFloat()
            val targetY = (measuredHeight - paddingTop - paddingBottom).toFloat()
            val scale = max(targetX / bmpByDrawable.width, targetY / bmpByDrawable.height)
            progressBackgroundMatrix.postScale(scale, scale)
            val scaledWidth = bmpByDrawable.width * scale
            val scaledHeight = bmpByDrawable.height * scale
            val dx: Float = (measuredWidth - paddingEnd + paddingStart - scaledWidth) / 2f
            val dy: Float = (measuredHeight - paddingBottom + paddingTop - scaledHeight) / 2f
            progressBackgroundMatrix.postTranslate(dx, dy)
        }
        invalidate()
    }

    /**
     * Draw progress.
     *
     * @param rectF The [RectF] of progress.
     * @param width The width of progress.
     * @since 0.5.4
     */
    private fun drawProgress(canvas: Canvas, rectF: RectF, width: Float, paint: Paint) {
        val radius = (bottom - top).coerceAtMost(right - left) / 2f
        progressPath.reset()
        progressPath.addRoundRect(rectF, radius, radius, Path.Direction.CW)
        canvas.withClip(progressPath) {
            rectF.right = rectF.left + width
            drawRect(rectF, paint)
        }
    }

    /**
     * Draw progress drawable.
     *
     * @param rectF The [RectF] of progress.
     * @param width The width of progress.
     * @since 0.5.4
     */
    private fun drawProgressDrawable(canvas: Canvas, rectF: RectF, width: Float, paint: Paint) {
        val bmp = progressBitmap ?: return
        val radius = min(rectF.width(), rectF.height()) / 2f
        progressPath.reset()
        progressPath.addRoundRect(rectF, radius, radius, Path.Direction.CW)
        canvas.withClip(progressPath) {
            rectF.right = rectF.left + width
            canvas.withClip(rectF) {
                drawBitmap(bmp, progressMatrix, paint)
            }
        }
    }


    init {
        context.withStyledAttributes(attrs, R.styleable.HorizontalProgressView, defStyleAttr, defStyleRes) {
            _maximumProgress = getFloat(R.styleable.HorizontalProgressView_progress_maximum_value, DEFAULT_MAXIMUM_PROGRESS)
            _currentProgress = getFloat(R.styleable.HorizontalProgressView_progress_current_value, DEFAULT_CURRENT_PROGRESS)
            progressPaint.color = getColor(R.styleable.HorizontalProgressView_progress_color, color(R.color.md_theme_primary))
            backgroundPaint.color = getColor(R.styleable.HorizontalProgressView_progress_background_color, color(R.color.md_theme_primaryContainer))
            _strokeWidth = getDimension(R.styleable.HorizontalProgressView_horizontal_progress_stroke_width, dimension(R.dimen.default_horizontal_progress_stroke_width))
            progressBackgroundBitmap = getDrawable(R.styleable.HorizontalProgressView_horizontal_progress_background_drawable)?.toBitmap()
            progressBitmap = getDrawable(R.styleable.HorizontalProgressView_horizontal_progress_drawable)?.toBitmap()
        }
    }

}