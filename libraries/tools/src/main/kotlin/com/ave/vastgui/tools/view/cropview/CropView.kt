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

package com.ave.vastgui.tools.view.cropview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.FloatRange
import androidx.annotation.IntRange
import androidx.core.content.withStyledAttributes
import com.ave.vastgui.tools.R
import com.ave.vastgui.tools.utils.ColorUtils
import com.ave.vastgui.tools.utils.DensityUtils.DP
import com.ave.vastgui.tools.utils.color
import com.ave.vastgui.tools.utils.dimension
import kotlin.math.roundToInt
import kotlin.properties.Delegates

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/4/18
// Documentation: https://sakurajimamaii.github.io/AVE-DOC/documents/tools/core-topics/ui/cropview/crop-view/

/**
 * [CropView].
 *
 * @since 0.5.0
 */
class CropView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.Default_CropView_Style,
    defStyleRes: Int = R.style.BaseCropView
) : View(context, attrs, defStyleAttr, defStyleRes) {

    /** @since 1.5.2 */
    @Suppress("PrivatePropertyName")
    private val DEFAULT_CROP_FRAME_SIZE get() = dimension(R.dimen.default_crop_frame_size)

    /** @since 1.5.2 */
    @Suppress("PrivatePropertyName")
    private val DEFAULT_CROP_FRAME_WIDTH get() = dimension(R.dimen.default_crop_frame_width)

    /** @since 1.5.2 */
    @Suppress("PrivatePropertyName")
    private val DEFAULT_CROP_FRAME_HEIGHT get() = dimension(R.dimen.default_crop_frame_height)

    /**
     * The size of frame(in pixels).
     *
     * The setting will only take effect when the [cropFrameType] is
     * [CropFrameType.CIRCLE], [CropFrameType.SQUARE] or [CropFrameType.GRID9].
     *
     * @since 1.5.2
     */
    private var _frameSize: Float by Delegates.notNull()

    /**
     * The width of frame(in pixels).
     *
     * The setting will only take effect when the [cropFrameType] is
     * [CropFrameType.RECTANGLE].
     *
     * @since 1.5.2
     */
    private var _frameWidth: Float by Delegates.notNull()

    /**
     * The height of frame(in pixels).
     *
     * The setting will only take effect when the [cropFrameType] is
     * [CropFrameType.RECTANGLE].
     *
     * @since 1.5.2
     */
    private var _frameHeight: Float by Delegates.notNull()

    /**
     * The minimum width of the [CropView] (in pixels).
     *
     * @since 1.5.2
     */
    private val neededMinimumWidth
        get() = when (_cropFrameType) {
            CropFrameType.GRID9 -> cropFrameWidth + cropFrameStrokeCornerWidth
            else -> cropFrameWidth + cropFrameStrokeWidth
        }

    /**
     * The minimum height of the [CropView] (in pixels).
     *
     * @since 1.5.2
     */
    private val neededMinimumHeight
        get() = when (_cropFrameType) {
            CropFrameType.GRID9 -> cropFrameHeight + cropFrameStrokeCornerWidth
            else -> cropFrameHeight + cropFrameStrokeWidth
        }

    /** @since 1.5.2 */
    private val xfermodeDstOut
        get() = PorterDuffXfermode(PorterDuff.Mode.DST_OUT)

    /**
     * The stroke width of frame(in pixels).
     *
     * @since 1.5.2
     */
    private val cropFrameStrokeWidth: Float = 2f.DP

    /**
     * The guideline width of frame when the [cropFrameType] is
     * [CropFrameType.GRID9] (in pixels).
     *
     * @since 1.5.2
     */
    private val cropFrameGuidelineWidth: Float = 2f.DP

    /**
     * The stroke corner width of frame when the [cropFrameType] is
     * [CropFrameType.GRID9] (in pixels).
     *
     * @since 1.5.2
     */
    private val cropFrameStrokeCornerWidth: Float = 4f.DP

    /**
     * The stroke corner length of frame when the [cropFrameType] is
     * [CropFrameType.GRID9] (in pixels).
     *
     * @since 1.5.2
     */
    private val cropFrameStrokeCornerLength: Float
        get() = (0.1 * _frameSize).toFloat()

    /** @since 1.5.2 */
    private val cropFramePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        xfermode = xfermodeDstOut
        style = Paint.Style.FILL
    }

    /** @since 1.5.2 */
    private val cropFrameStrokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        strokeWidth = cropFrameStrokeWidth
        style = Paint.Style.STROKE
    }

    /** @since 1.5.2 */
    private val cropFrameGuidelinePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        strokeWidth = cropFrameGuidelineWidth
        style = Paint.Style.STROKE
    }

    /** @since 1.5.2 */
    private val cropFrameStrokeCornerPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        strokeWidth = cropFrameStrokeCornerWidth
        style = Paint.Style.STROKE
    }

    /**
     * The crop mask layer color.
     *
     * @since 1.5.2
     */
    @get:ColorInt
    var cropMaskColor: Int by Delegates.notNull()
        private set

    /** @since 1.5.2 */
    private var _cropFrameType: CropFrameType by Delegates.notNull()

    /**
     * The crop frame shape type.
     *
     * @since 1.5.2
     */
    val cropFrameType: CropFrameType
        get() = _cropFrameType

    /** @since 1.5.2 */
    private var _cropFrameStrokeColor: Int by Delegates.notNull()

    /**
     * The color of frame stroke.
     *
     * @since 1.5.2
     */
    @get:ColorInt
    val cropFrameStrokeColor: Int
        get() = _cropFrameStrokeColor

    /**
     * The width of frame(in pixels).
     *
     * @since 1.5.2
     */
    val cropFrameWidth: Float
        get() = if (cropFrameType == CropFrameType.RECTANGLE) _frameWidth else _frameSize

    /**
     * The height of frame(in pixels).
     *
     * @since 1.5.2
     */
    val cropFrameHeight: Float
        get() = if (cropFrameType == CropFrameType.RECTANGLE) _frameHeight else _frameSize

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = resolveSize(neededMinimumWidth.roundToInt(), widthMeasureSpec)
        val height = resolveSize(neededMinimumHeight.roundToInt(), heightMeasureSpec)
        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas) {
        canvas.saveLayer(0f, 0f, measuredWidth.toFloat(), measuredHeight.toFloat(), null)
        canvas.drawColor(cropMaskColor)
        cropFrameStrokePaint.color = _cropFrameStrokeColor
        cropFrameGuidelinePaint.color = _cropFrameStrokeColor
        cropFrameStrokeCornerPaint.color = _cropFrameStrokeColor
        when (_cropFrameType) {
            CropFrameType.CIRCLE -> {
                canvas.drawCircle(
                    (measuredWidth / 2).toFloat(),
                    (measuredHeight / 2).toFloat(),
                    _frameSize / 2,
                    cropFramePaint
                )
                canvas.drawCircle(
                    (measuredWidth / 2).toFloat(),
                    (measuredHeight / 2).toFloat(),
                    _frameSize / 2,
                    cropFrameStrokePaint
                )
            }

            CropFrameType.SQUARE -> {
                canvas.drawRect(
                    (measuredWidth - _frameSize) / 2,
                    (measuredHeight - _frameSize) / 2,
                    (measuredWidth + _frameSize) / 2,
                    (measuredHeight + _frameSize) / 2,
                    cropFramePaint
                )
                canvas.drawRect(
                    (measuredWidth - _frameSize) / 2,
                    (measuredHeight - _frameSize) / 2,
                    (measuredWidth + _frameSize) / 2,
                    (measuredHeight + _frameSize) / 2,
                    cropFrameStrokePaint
                )
            }

            CropFrameType.RECTANGLE -> {
                canvas.drawRect(
                    (measuredWidth - _frameWidth) / 2,
                    (measuredHeight - _frameHeight) / 2,
                    (measuredWidth + _frameWidth) / 2,
                    (measuredHeight + _frameHeight) / 2,
                    cropFramePaint
                )
                canvas.drawRect(
                    (measuredWidth - _frameWidth) / 2,
                    (measuredHeight - _frameHeight) / 2,
                    (measuredWidth + _frameWidth) / 2,
                    (measuredHeight + _frameHeight) / 2,
                    cropFrameStrokePaint
                )
            }

            CropFrameType.GRID9 -> {
                canvas.drawRect(
                    (measuredWidth - _frameSize) / 2,
                    (measuredHeight - _frameSize) / 2,
                    (measuredWidth + _frameSize) / 2,
                    (measuredHeight + _frameSize) / 2,
                    cropFramePaint
                )

                val rect = getCropFrameRect()
                drawBorder(canvas, rect)
                drawGuidelines(canvas, rect)
                drawCorners(canvas, rect)
            }
        }
        canvas.restore()
    }

    /**
     * Set crop mask layer color.
     *
     * @see ColorUtils.getColorIntWithTransparency
     * @since 0.5.3
     */
    fun setCropMaskColor(@ColorInt colorInt: Int, @IntRange(from = 0, to = 100) transparency: Int? = null) {
        check(ColorUtils.isColorInt(colorInt)) {
            "The value of mask color(current=${colorInt.toUInt().toString(16)}) is invalid"
        }
        cropMaskColor = if (transparency == null) colorInt else ColorUtils.getColorIntWithTransparency(transparency, colorInt)
        invalidate()
    }

    /**
     * Set crop frame shape type.
     *
     * @see CropFrameType
     * @since 0.5.0
     */
    fun setCropFrameType(type: CropFrameType) {
        if (_cropFrameType == type) return
        _cropFrameType = type
        invalidate()
    }

    /**
     * Set crop frame size.
     *
     * The setting will only take effect when the [cropFrameType] is
     * [CropFrameType.RECTANGLE].
     *
     * @param width The frame width(in pixels).
     * @param height The frame height(in pixels).
     * @since 0.5.0
     */
    fun setCropFrameSize(@FloatRange(from = 0.0) width: Float, @FloatRange(from = 0.0) height: Float) {
        if (cropFrameType == CropFrameType.RECTANGLE) {
            _frameWidth = width.coerceAtLeast(0f)
            _frameHeight = height.coerceAtLeast(0f)
            requestLayout()
        }
    }

    /**
     * Set crop frame size.
     *
     * The setting will only take effect when the [cropFrameType] is
     * [CropFrameType.CIRCLE], [CropFrameType.SQUARE] or [CropFrameType.GRID9].
     *
     * @param size The frame width and height(in pixels).
     * @since 1.5.2
     */
    fun setCropFrameSize(@FloatRange(from = 0.0) size: Float) {
        if (cropFrameType != CropFrameType.RECTANGLE) {
            _frameSize = size.coerceAtLeast(0f)
            requestLayout()
        }
    }

    /**
     * Set crop frame stroke color.
     *
     * @since 0.5.0
     */
    fun setCropFrameStrokeColor(@ColorInt colorInt: Int) {
        check(ColorUtils.isColorInt(colorInt)) {
            "The value of frame stroke color(current=${colorInt.toUInt().toString(16)}) is invalid."
        }
        _cropFrameStrokeColor = colorInt
        invalidate()
    }

    /**
     * Draw border when [_cropFrameType] is [CropFrameType.GRID9].
     *
     * @since 0.5.0
     */
    private fun drawBorder(canvas: Canvas, clipRect: Rect) {
        canvas.drawRect(
            clipRect.left.toFloat(),
            clipRect.top.toFloat(),
            clipRect.right.toFloat(),
            clipRect.bottom.toFloat(),
            cropFrameStrokePaint
        )
    }

    /**
     * Draw guidelines when [_cropFrameType] is [CropFrameType.GRID9].
     *
     * @since 0.5.0
     */
    private fun drawGuidelines(canvas: Canvas, clipRect: Rect) {
        val left = clipRect.left.toFloat()
        val top = clipRect.top.toFloat()
        val right = clipRect.right.toFloat()
        val bottom = clipRect.bottom.toFloat()
        val oneThirdCropWidth = (right - left) / 3
        val x1 = left + oneThirdCropWidth
        canvas.drawLine(x1, top, x1, bottom, cropFrameGuidelinePaint)
        val x2 = right - oneThirdCropWidth
        canvas.drawLine(x2, top, x2, bottom, cropFrameGuidelinePaint)
        val oneThirdCropHeight = (bottom - top) / 3
        val y1 = top + oneThirdCropHeight
        canvas.drawLine(left, y1, right, y1, cropFrameGuidelinePaint)
        val y2 = bottom - oneThirdCropHeight
        canvas.drawLine(left, y2, right, y2, cropFrameGuidelinePaint)
    }

    /**
     * Draw corners when [_cropFrameType] is [CropFrameType.GRID9].
     *
     * @since 0.5.0
     */
    private fun drawCorners(canvas: Canvas, clipRect: Rect) {
        val left = clipRect.left.toFloat()
        val top = clipRect.top.toFloat()
        val right = clipRect.right.toFloat()
        val bottom = clipRect.bottom.toFloat()

        val lateralOffset: Float = (cropFrameStrokeCornerWidth - cropFrameStrokeWidth) / 2f
        val startOffset: Float = cropFrameStrokeCornerWidth - (cropFrameStrokeWidth / 2f)

        canvas.drawLine(
            left - lateralOffset,
            top - startOffset,
            left - lateralOffset,
            top + cropFrameStrokeCornerLength,
            cropFrameStrokeCornerPaint
        )
        canvas.drawLine(
            left - startOffset,
            top - lateralOffset,
            left + cropFrameStrokeCornerLength,
            top - lateralOffset,
            cropFrameStrokeCornerPaint
        )
        canvas.drawLine(
            right + lateralOffset,
            top - startOffset,
            right + lateralOffset,
            top + cropFrameStrokeCornerLength,
            cropFrameStrokeCornerPaint
        )
        canvas.drawLine(
            right + startOffset,
            top - lateralOffset,
            right - cropFrameStrokeCornerLength,
            top - lateralOffset,
            cropFrameStrokeCornerPaint
        )
        canvas.drawLine(
            left - lateralOffset,
            bottom + startOffset,
            left - lateralOffset,
            bottom - cropFrameStrokeCornerLength,
            cropFrameStrokeCornerPaint
        )
        canvas.drawLine(
            left - startOffset,
            bottom + lateralOffset,
            left + cropFrameStrokeCornerLength,
            bottom + lateralOffset,
            cropFrameStrokeCornerPaint
        )
        canvas.drawLine(
            right + lateralOffset,
            bottom + startOffset,
            right + lateralOffset,
            bottom - cropFrameStrokeCornerLength,
            cropFrameStrokeCornerPaint
        )
        canvas.drawLine(
            right + startOffset,
            bottom + lateralOffset,
            right - cropFrameStrokeCornerLength,
            bottom + lateralOffset,
            cropFrameStrokeCornerPaint
        )
    }

    /**
     * Get crop frame rect.
     *
     * @since 0.5.0
     */
    internal fun getCropFrameRect(): Rect {
        return Rect(((measuredWidth - cropFrameWidth) / 2f).roundToInt(),
            ((measuredHeight - cropFrameHeight) / 2f).roundToInt(),
            ((measuredWidth + cropFrameWidth) / 2f).roundToInt(),
            ((measuredHeight + cropFrameHeight) / 2).roundToInt())
    }

    init {
        context.withStyledAttributes(attrs, R.styleable.CropView, defStyleAttr, defStyleRes) {
            cropMaskColor = getColor(R.styleable.CropView_crop_mask_layer_color, color(R.color.default_crop_frame_mask_color))
            _cropFrameType = when (getInt(R.styleable.CropView_crop_frame_type, CropFrameType.CIRCLE.ordinal)) {
                CropFrameType.CIRCLE.ordinal -> CropFrameType.CIRCLE
                CropFrameType.SQUARE.ordinal -> CropFrameType.SQUARE
                CropFrameType.GRID9.ordinal -> CropFrameType.GRID9
                CropFrameType.RECTANGLE.ordinal -> CropFrameType.RECTANGLE
                else -> CropFrameType.CIRCLE
            }
            _cropFrameStrokeColor = getColor(R.styleable.CropView_crop_frame_stroke_color, color(R.color.md_theme_primaryFixedDim))
            _frameSize = getDimension(R.styleable.CropView_crop_frame_size, DEFAULT_CROP_FRAME_SIZE)
            _frameWidth = getDimension(R.styleable.CropView_crop_frame_width, DEFAULT_CROP_FRAME_WIDTH)
            _frameHeight = getDimension(R.styleable.CropView_crop_frame_height, DEFAULT_CROP_FRAME_HEIGHT)
        }
    }

}