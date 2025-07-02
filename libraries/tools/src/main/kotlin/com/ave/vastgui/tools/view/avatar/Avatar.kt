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

package com.ave.vastgui.tools.view.avatar

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.IntDef
import androidx.annotation.StyleRes
import androidx.core.content.res.getResourceIdOrThrow
import androidx.core.content.withStyledAttributes
import androidx.lifecycle.AtomicReference
import com.ave.vastgui.tools.R
import com.ave.vastgui.tools.graphics.BmpUtils
import com.ave.vastgui.tools.utils.ColorUtils
import com.ave.vastgui.tools.utils.color
import com.ave.vastgui.tools.utils.dimension
import com.ave.vastgui.tools.view.avatar.Avatar.Companion.SHAPE_CIRCLE
import com.ave.vastgui.tools.view.avatar.Avatar.Companion.SHAPE_ROUND_CORNER
import java.io.File
import java.io.FileInputStream
import kotlin.math.max
import kotlin.math.roundToInt
import kotlin.properties.Delegates

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/9/25
// Documentation: https://sakurajimamaii.github.io/AVE-DOC/documents/tools/core-topics/ui/avatar/avatar/
// Reference: https://github.com/jhbxyz/ArticleRecord/blob/master/articles/%E8%87%AA%E5%AE%9A%E4%B9%89View/2%E5%9C%86%E5%BD%A2%E5%A4%B4%E5%83%8F.md

/**
 * Avatar.
 *
 * @since 0.5.4
 */
class Avatar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.Default_Avatar_Style,
    @StyleRes defStyleRes: Int = R.style.BaseAvatar
) : View(context, attrs, defStyleAttr, defStyleRes) {

    /** @since 1.5.2 */
    @IntDef(value = [SHAPE_CIRCLE, SHAPE_ROUND_CORNER])
    @Retention(AnnotationRetention.SOURCE)
    annotation class Shape

    /** @since 1.5.2 */
    @Suppress("PrivatePropertyName")
    private val DEFAULT_AVATAR_SIZE = dimension(R.dimen.default_avatar_size)

    /** @since 1.5.2 */
    @Suppress("PrivatePropertyName")
    private val DEFAULT_TEXT_SIZE = dimension(R.dimen.default_avatar_text_size)

    /** @since 1.5.2 */
    @Suppress("PrivatePropertyName")
    private val DEFAULT_STROKE_WIDTH = dimension(R.dimen.default_avatar_stroke_width)

    /** @since 1.5.2 */
    @Suppress("PrivatePropertyName")
    private val DEFAULT_CORNER_RADIUS = dimension(R.dimen.default_avatar_corner_radius)

    /** @since 1.5.2 */
    private val xfermodeSrcIn = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)

    /** @since 1.5.2 */
    private val strokePath = Path()

    /** @since 1.5.2 */
    private val srcBmpSrcRect = Rect()

    /** @since 1.5.2 */
    private val srcBmpDstRectF = RectF()

    /** @since 1.5.2 */
    private val strokeDstRectF = RectF()

    /** @since 1.5.2 */
    private val srcBmpPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    /** @since 1.5.2 */
    private val srcTextPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textAlign = Paint.Align.CENTER
    }

    /** @since 1.5.2 */
    private val srcColorPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }

    /** @since 1.5.2 */
    private val strokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
    }

    /**
     * The Shape of avatar. Current support [SHAPE_CIRCLE] and round
     * [SHAPE_ROUND_CORNER].
     *
     * @since 1.5.2
     */
    @get:Shape
    var shape: Int = SHAPE_CIRCLE
        private set

    /** @since 1.5.2 */
    private var _size = DEFAULT_AVATAR_SIZE

    /**
     * The avatar size.
     *
     * @since 1.5.2
     */
    var size: Float
        get() = _size
        set(value) {
            if (_size == value) return
            _size = value.coerceAtLeast(0f)
            requestLayout()
        }

    private var _srcBmp: AtomicReference<Bitmap> = AtomicReference<Bitmap>()

    /**
     * The bitmap image of the avatar.
     *
     * @since 1.5.2
     */
    val srcBmp: Bitmap
        get() = _srcBmp.get()

    /**
     * The color shown when [srcBmp] is null.
     *
     * @since 1.5.2
     */
    @get:ColorInt
    @setparam:ColorInt
    var srcColor: Int
        get() = srcColorPaint.color
        set(value) {
            if (srcColorPaint.color == value) return
            check(ColorUtils.isColorInt(value)) { "The value of srcColor(current=$value) isn't a valid value." }
            srcColorPaint.color = value
            invalidate()
        }

    /** @since 1.5.2 */
    private var _srcText: String by Delegates.notNull()

    /**
     * The text shown when [srcBmp] is null.
     *
     * @since 1.5.2
     */
    var srcText: String
        get() = _srcText
        set(value) {
            if (_srcText == value) return
            _srcText = value
            invalidate()
        }

    /**
     * The font size of [srcText] shown when the [srcBmp] is null.
     *
     * @since 1.5.2
     */
    var srcTextSize: Float
        set(value) {
            if (srcTextPaint.textSize == value) return
            srcTextPaint.textSize = value.coerceAtLeast(0f)
            invalidate()
        }
        get() = srcTextPaint.textSize

    /**
     * The font color of [srcText] shown when the [srcBmp] is null.
     *
     * @since 1.5.2
     */
    @get:ColorInt
    @setparam:ColorInt
    var srcTextColor: Int
        set(value) {
            if (srcTextPaint.color == value) return
            check(ColorUtils.isColorInt(value)) { "The value of srcTextColor(current=$value) isn't a valid value." }
            srcTextPaint.color = value
            invalidate()
        }
        get() = srcTextPaint.color

    /**
     * The stroke width of avatar.
     *
     * @since 1.5.2
     */
    var strokeWidth: Float
        get() = strokePaint.strokeWidth
        set(value) {
            if (strokePaint.strokeWidth == value) return
            strokePaint.strokeWidth = value.coerceAtLeast(0f)
            requestLayout()
        }

    /**
     * The stroke color of avatar.
     *
     * @since 1.5.2
     */
    @get:ColorInt
    @setparam:ColorInt
    var strokeColor: Int
        get() = strokePaint.color
        set(value) {
            if (strokePaint.color == value) return
            check(ColorUtils.isColorInt(value)) { "The value of strokeColor(current=$value) isn't a valid value." }
            strokePaint.color = value
            invalidate()
        }

    /** @since 1.5.2 */
    private var _cornerRadius: Float = DEFAULT_CORNER_RADIUS

    /**
     * The corner radius of avatar when the shape is [SHAPE_ROUND_CORNER].
     *
     * @since 1.5.2
     */
    var cornerRadius: Float
        get() = _cornerRadius
        set(value) {
            if (_cornerRadius == value) return
            _cornerRadius = value.coerceAtLeast(0f)
            invalidate()
        }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val neededMinimumWidth = max(_size + 2 * strokeWidth, suggestedMinimumWidth.toFloat())
        val neededMinimumHeight = max(_size + 2 * strokeWidth, suggestedMinimumHeight.toFloat())
        val width = resolveSize(neededMinimumWidth.roundToInt(), widthMeasureSpec)
        val height = resolveSize(neededMinimumHeight.roundToInt(), heightMeasureSpec)
        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas) {
        if (_srcBmp.get() != null) {
            canvas.apply {
                srcBmpDstRectF.set((measuredWidth - _size) / 2f, (measuredHeight - _size) / 2f,
                    (measuredWidth + _size) / 2f, (measuredHeight + _size) / 2f)
                val count = saveLayer(srcBmpDstRectF.left - strokeWidth, srcBmpDstRectF.top - strokeWidth,
                    srcBmpDstRectF.right + strokeWidth, srcBmpDstRectF.bottom + strokeWidth, null)
                strokePath.reset()
                when (shape) {
                    SHAPE_CIRCLE ->
                        strokePath.addCircle(measuredWidth / 2f, measuredHeight / 2f, _size / 2f, Path.Direction.CW)

                    SHAPE_ROUND_CORNER -> {
                        strokeDstRectF.set((measuredWidth - _size) / 2f,
                            (measuredHeight - _size) / 2f,
                            (measuredWidth + _size) / 2f,
                            (measuredHeight + _size) / 2f)
                        strokePath.addRoundRect(strokeDstRectF, _cornerRadius - strokeWidth / 2f, _cornerRadius - strokeWidth / 2f, Path.Direction.CW)
                    }
                }
                drawPath(strokePath, srcBmpPaint)
                srcBmpPaint.xfermode = xfermodeSrcIn
                // Use public void drawBitmap(@NonNull Bitmap bitmap, @Nullable Rect src,
                // @NonNull RectF dst, @Nullable Paint paint)
                // Because this function ignores the density associated with the bitmap.
                // This is because the source and destination rectangle coordinate spaces
                // are in their respective densities, so must already have the appropriate
                // scaling factor applied.
                srcBmpSrcRect.set(0, 0, _size.toInt(), _size.toInt())
                _srcBmp.set(BmpUtils.scaleBitmap(_srcBmp.get(), _size.toInt(), _size.toInt()))
                drawBitmap(_srcBmp.get(), srcBmpSrcRect, srcBmpDstRectF, srcBmpPaint)
                srcBmpPaint.xfermode = null
                restoreToCount(count)
            }
        } else {
            when (shape) {
                SHAPE_CIRCLE ->
                    canvas.drawCircle(measuredWidth / 2f, measuredHeight / 2f, _size / 2f, srcColorPaint)

                SHAPE_ROUND_CORNER -> {
                    strokeDstRectF.set((measuredWidth - _size) / 2f,
                        (measuredHeight - _size) / 2f,
                        (measuredWidth + _size) / 2f,
                        (measuredHeight + _size) / 2f)
                    canvas.drawRoundRect(strokeDstRectF, _cornerRadius - strokeWidth / 2f, _cornerRadius - strokeWidth / 2f, srcColorPaint)
                }
            }

            canvas.drawText(_srcText, measuredWidth / 2f, measuredHeight / 2f + getBaseLine(), srcTextPaint)
        }

        strokePath.reset()
        when (shape) {
            SHAPE_CIRCLE ->
                strokePath.addCircle(measuredWidth / 2f, measuredHeight / 2f, (_size + strokeWidth) / 2f, Path.Direction.CW)

            SHAPE_ROUND_CORNER -> {
                strokeDstRectF.set((measuredWidth - _size - strokeWidth) / 2f,
                    (measuredHeight - _size - strokeWidth) / 2f,
                    (measuredWidth + _size + strokeWidth) / 2f,
                    (measuredHeight + _size + strokeWidth) / 2f)
                strokePath.addRoundRect(strokeDstRectF, _cornerRadius, _cornerRadius, Path.Direction.CW)
            }
        }
        canvas.drawPath(strokePath, strokePaint)
    }

    /**
     * Set shape of avatar.
     *
     * @since 0.5.4
     */
    fun setShape(@Shape shape: Int) {
        if (this.shape == shape) return
        check(shape == SHAPE_CIRCLE || shape == SHAPE_ROUND_CORNER) {
            "shape(current=$shape) should be one of two values: SHAPE_CIRCLE($SHAPE_CIRCLE) or SHAPE_ROUND_CORNER($SHAPE_ROUND_CORNER)"
        }
        this.shape = shape
        invalidate()
    }

    /**
     * Set avatar by [bitmap].
     *
     * @since 0.5.4
     */
    fun setAvatar(bitmap: Bitmap) {
        _srcBmp.set(bitmap)
        invalidate()
    }

    /**
     * Set avatar by [resId].
     *
     * @since 0.5.4
     */
    fun setAvatar(@DrawableRes resId: Int) {
        try {
            _srcBmp.set(BmpUtils.getBitmapFromDrawable(resId, context))
            invalidate()
        } catch (exception: Exception) {
            exception.printStackTrace()
            return
        }
    }

    /**
     * Set avatar by [file].
     *
     * @since 0.5.4
     */
    fun setAvatar(file: File) {
        try {
            _srcBmp.set(BitmapFactory.decodeStream(FileInputStream(file)))
            invalidate()
        } catch (exception: Exception) {
            exception.printStackTrace()
            return
        }
    }

    /**
     * Get base line
     *
     * @since 0.5.4
     */
    private fun getBaseLine(): Float {
        val fontMetrics = srcTextPaint.fontMetrics
        val height = fontMetrics.bottom - fontMetrics.top
        return height / 2f - fontMetrics.bottom
    }

    init {
        context.withStyledAttributes(attrs, R.styleable.Avatar, defStyleAttr, defStyleRes) {
            _size = getDimension(R.styleable.Avatar_avatar_size, DEFAULT_AVATAR_SIZE)
            _srcBmp.set(runCatching { BmpUtils.getBitmapFromDrawable(getResourceIdOrThrow(R.styleable.Avatar_avatar_src), context) }.getOrNull())
            shape = getInt(R.styleable.Avatar_avatar_shape, SHAPE_CIRCLE)
            srcColorPaint.color = getColor(R.styleable.Avatar_avatar_background, color(R.color.md_theme_primary))
            _srcText = getString(R.styleable.Avatar_avatar_text) ?: DEFAULT_TEXT
            srcTextPaint.textSize = getDimension(R.styleable.Avatar_avatar_text_size, DEFAULT_TEXT_SIZE)
            srcTextPaint.color = getColor(R.styleable.Avatar_avatar_text_color, color(R.color.md_theme_onPrimary))
            strokePaint.strokeWidth = getDimension(R.styleable.Avatar_avatar_stroke_width, DEFAULT_STROKE_WIDTH)
            strokePaint.color = getColor(R.styleable.Avatar_avatar_stroke_color, color(R.color.md_theme_primaryContainer))
            _cornerRadius = getDimension(R.styleable.Avatar_avatar_corner_radius, DEFAULT_CORNER_RADIUS)
        }
    }

    companion object {
        const val SHAPE_CIRCLE = 0
        const val SHAPE_ROUND_CORNER = 1

        /** @since 1.5.2 */
        private const val DEFAULT_TEXT = "A"
    }

}