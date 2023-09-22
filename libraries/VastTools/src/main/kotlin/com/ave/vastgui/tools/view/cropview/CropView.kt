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
import com.ave.vastgui.core.extension.NotNUllVar
import com.ave.vastgui.tools.R
import com.ave.vastgui.tools.utils.ColorUtils
import com.ave.vastgui.tools.utils.DensityUtils.DP

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/4/18
// Documentation: https://ave.entropy2020.cn/documents/VastTools/core-topics/ui/cropview/crop-view/

/**
 * Crop view
 *
 * @property mCropMaskColor The crop mask layer color.
 * @property mCropFrameType The crop frame shape type.
 * @property mCropFrameWidth The crop frame width. mCropViewWidth and
 *     mCropViewHeight are only different when the frame type is RECTANGLE
 * @property mCropFrameHeight The crop frame width. mCropViewWidth and
 *     mCropViewHeight are only different when the frame type is RECTANGLE
 * @property mCropFrameStrokeWidth The crop frame stroke width.
 * @property mCropFrameGuidelineWidth The crop frame guideline width when
 *     the frame type is GRID9.
 * @property mCropFrameStrokeCornerWidth The crop frame stroke corner width
 *     when the frame type is GRID9.
 * @property mCropFrameStrokeCornerLength The crop frame stroke corner
 *     length when the frame type is GRID9.
 * @property mCropFrameStrokeColor The crop frame stroke width.
 * @since 0.5.0
 */
class CropView @JvmOverloads internal constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.Default_CropView_Style,
    defStyleRes: Int = R.style.BaseCropView
) : View(context, attrs, defStyleAttr, defStyleRes) {

    private val mDefaultCropFrameWidth
        get() = context.resources.getDimension(R.dimen.default_crop_frame_width)
    private val mDefaultCropFrameHeight
        get() = context.resources.getDimension(R.dimen.default_crop_frame_height)

    /**
     * The setting value of frame height.
     *
     * @since 0.5.3
     */
    private var mFrameNeededHeight by NotNUllVar<Float>()

    /**
     * The setting value of frame width.
     *
     * @since 0.5.3
     */
    private var mFrameNeededWidth by NotNUllVar<Float>()

    /**
     * The minimum height of the [CropView].
     *
     * @since 0.5.3
     */
    private val mNeededHeight
        get() = when (mCropFrameType) {
            CropFrameType.GRID9 -> mCropFrameHeight + mCropFrameStrokeCornerWidth
            else -> mCropFrameHeight + mCropFrameStrokeWidth
        }

    /**
     * The minimum width of the [CropView].
     *
     * @since 0.5.3
     */
    private val mNeededWidth
        get() = when (mCropFrameType) {
            CropFrameType.GRID9 -> mCropFrameWidth + mCropFrameStrokeCornerWidth
            else -> mCropFrameWidth + mCropFrameStrokeWidth
        }

    private val mXfermodeDstOut
        get() = PorterDuffXfermode(PorterDuff.Mode.DST_OUT)
    private val mCropFrameStrokeWidth: Float = 2f.DP
    private val mCropFrameGuidelineWidth: Float = 2f.DP
    private val mCropFrameStrokeCornerWidth: Float = 4f.DP
    private val mCropFrameStrokeCornerLength: Float
        get() = (0.1 * mNeededWidth.coerceAtMost(mNeededHeight)).toFloat()
    private val mCropFramePaint = Paint().apply {
        xfermode = mXfermodeDstOut
        isAntiAlias = true
        style = Paint.Style.FILL
    }
    private val mCropFrameStrokePaint = Paint().apply {
        strokeWidth = mCropFrameStrokeWidth
        isAntiAlias = true
        style = Paint.Style.STROKE
    }
    private val mCropFrameGuidelinePaint = Paint().apply {
        strokeWidth = mCropFrameGuidelineWidth
        isAntiAlias = true
        style = Paint.Style.STROKE
    }
    private val mCropFrameStrokeCornerPaint = Paint().apply {
        strokeWidth = mCropFrameStrokeCornerWidth
        isAntiAlias = true
        style = Paint.Style.STROKE
    }

    var mCropMaskColor: Int by NotNUllVar()
        private set

    var mCropFrameType: CropFrameType by NotNUllVar()
        private set

    var mCropFrameStrokeColor: Int by NotNUllVar()
        private set

    val mCropFrameHeight: Float
        get() = when (mCropFrameType) {
            CropFrameType.RECTANGLE -> mFrameNeededHeight
            else -> mFrameNeededWidth.coerceAtMost(mFrameNeededHeight)
        }

    val mCropFrameWidth: Float
        get() = when (mCropFrameType) {
            CropFrameType.RECTANGLE -> mFrameNeededWidth
            else -> mFrameNeededWidth.coerceAtMost(mFrameNeededHeight)
        }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val needWidth = when (mCropFrameType) {
            CropFrameType.RECTANGLE -> mNeededWidth
            else -> mNeededWidth.coerceAtMost(mNeededHeight)
        }.toInt()
        val needHeight = when (mCropFrameType) {
            CropFrameType.RECTANGLE -> mNeededHeight
            else -> mNeededWidth.coerceAtMost(mNeededHeight)
        }.toInt()
        val width = resolveSize(needWidth, widthMeasureSpec)
        val height = resolveSize(needHeight, heightMeasureSpec)
        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas) {
        canvas.saveLayer(0f, 0f, measuredWidth.toFloat(), measuredHeight.toFloat(), null)
        canvas.drawColor(mCropMaskColor)
        mCropFrameStrokePaint.color = mCropFrameStrokeColor
        mCropFrameGuidelinePaint.color = mCropFrameStrokeColor
        mCropFrameStrokeCornerPaint.color = mCropFrameStrokeColor
        when (mCropFrameType) {
            CropFrameType.CIRCLE -> {
                canvas.drawCircle(
                    (width / 2).toFloat(),
                    (height / 2).toFloat(),
                    mCropFrameWidth / 2,
                    mCropFramePaint
                )
                canvas.drawCircle(
                    (width / 2).toFloat(),
                    (height / 2).toFloat(),
                    mCropFrameWidth / 2,
                    mCropFrameStrokePaint
                )
            }

            CropFrameType.SQUARE -> {
                canvas.drawRect(
                    (width - mCropFrameWidth) / 2,
                    (height - mCropFrameWidth) / 2,
                    (width + mCropFrameWidth) / 2,
                    (height + mCropFrameWidth) / 2,
                    mCropFramePaint
                )
                canvas.drawRect(
                    (width - mCropFrameWidth) / 2,
                    (height - mCropFrameWidth) / 2,
                    (width + mCropFrameWidth) / 2,
                    (height + mCropFrameWidth) / 2,
                    mCropFrameStrokePaint
                )
            }

            CropFrameType.RECTANGLE -> {
                canvas.drawRect(
                    (width - mCropFrameWidth) / 2,
                    (height - mCropFrameHeight) / 2,
                    (width + mCropFrameWidth) / 2,
                    (height + mCropFrameHeight) / 2,
                    mCropFramePaint
                )
                canvas.drawRect(
                    (width - mCropFrameWidth) / 2,
                    (height - mCropFrameHeight) / 2,
                    (width + mCropFrameWidth) / 2,
                    (height + mCropFrameHeight) / 2,
                    mCropFrameStrokePaint
                )
            }

            CropFrameType.GRID9 -> {
                canvas.drawRect(
                    (width - mCropFrameWidth) / 2,
                    (height - mCropFrameWidth) / 2,
                    (width + mCropFrameWidth) / 2,
                    (height + mCropFrameWidth) / 2,
                    mCropFramePaint
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
    fun setCropMaskColor(
        @ColorInt color: Int,
        @IntRange(from = 0, to = 100) transparency: Int? = null
    ) {
        mCropMaskColor = if (transparency == null) color
        else ColorUtils.getColorIntWithTransparency(color, transparency)
    }

    /**
     * Set crop frame shape type.
     *
     * @see CropFrameType
     * @since 0.5.0
     */
    fun setCropFrameType(type: CropFrameType) {
        mCropFrameType = type
    }

    /**
     * Set crop frame size.
     *
     * @param width The frame width.
     * @param height The frame height.
     * @since 0.5.0
     */
    fun setCropFrameSize(
        @FloatRange(from = 0.0) width: Float,
        @FloatRange(from = 0.0) height: Float
    ) {
        mFrameNeededWidth = width
        mFrameNeededHeight = height
    }

    /**
     * Set crop frame stroke width in pixels.
     *
     * @since 0.5.0
     */
    fun setCropFrameStrokeColor(@ColorInt color: Int) {
        mCropFrameStrokeColor = color
    }

    /**
     * Draw border when [mCropFrameType] is [CropFrameType.GRID9].
     *
     * @since 0.5.0
     */
    private fun drawBorder(canvas: Canvas, clipRect: Rect) {
        canvas.drawRect(
            clipRect.left.toFloat(),
            clipRect.top.toFloat(),
            clipRect.right.toFloat(),
            clipRect.bottom.toFloat(),
            mCropFrameStrokePaint
        )
    }

    /**
     * Draw guidelines when [mCropFrameType] is [CropFrameType.GRID9].
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
        canvas.drawLine(x1, top, x1, bottom, mCropFrameGuidelinePaint)
        val x2 = right - oneThirdCropWidth
        canvas.drawLine(x2, top, x2, bottom, mCropFrameGuidelinePaint)
        val oneThirdCropHeight = (bottom - top) / 3
        val y1 = top + oneThirdCropHeight
        canvas.drawLine(left, y1, right, y1, mCropFrameGuidelinePaint)
        val y2 = bottom - oneThirdCropHeight
        canvas.drawLine(left, y2, right, y2, mCropFrameGuidelinePaint)
    }

    /**
     * Draw corners when [mCropFrameType] is [CropFrameType.GRID9].
     *
     * @since 0.5.0
     */
    private fun drawCorners(canvas: Canvas, clipRect: Rect) {
        val left = clipRect.left.toFloat()
        val top = clipRect.top.toFloat()
        val right = clipRect.right.toFloat()
        val bottom = clipRect.bottom.toFloat()

        val lateralOffset: Float = (mCropFrameStrokeCornerWidth - mCropFrameStrokeWidth) / 2f
        val startOffset: Float = mCropFrameStrokeCornerWidth - (mCropFrameStrokeWidth / 2f)

        canvas.drawLine(
            left - lateralOffset,
            top - startOffset,
            left - lateralOffset,
            top + mCropFrameStrokeCornerLength,
            mCropFrameStrokeCornerPaint
        )
        canvas.drawLine(
            left - startOffset,
            top - lateralOffset,
            left + mCropFrameStrokeCornerLength,
            top - lateralOffset,
            mCropFrameStrokeCornerPaint
        )
        canvas.drawLine(
            right + lateralOffset,
            top - startOffset,
            right + lateralOffset,
            top + mCropFrameStrokeCornerLength,
            mCropFrameStrokeCornerPaint
        )
        canvas.drawLine(
            right + startOffset,
            top - lateralOffset,
            right - mCropFrameStrokeCornerLength,
            top - lateralOffset,
            mCropFrameStrokeCornerPaint
        )
        canvas.drawLine(
            left - lateralOffset,
            bottom + startOffset,
            left - lateralOffset,
            bottom - mCropFrameStrokeCornerLength,
            mCropFrameStrokeCornerPaint
        )
        canvas.drawLine(
            left - startOffset,
            bottom + lateralOffset,
            left + mCropFrameStrokeCornerLength,
            bottom + lateralOffset,
            mCropFrameStrokeCornerPaint
        )
        canvas.drawLine(
            right + lateralOffset,
            bottom + startOffset,
            right + lateralOffset,
            bottom - mCropFrameStrokeCornerLength,
            mCropFrameStrokeCornerPaint
        )
        canvas.drawLine(
            right + startOffset,
            bottom + lateralOffset,
            right - mCropFrameStrokeCornerLength,
            bottom + lateralOffset,
            mCropFrameStrokeCornerPaint
        )
    }

    /**
     * Get crop frame rect.
     *
     * @since 0.5.0
     */
    internal fun getCropFrameRect(): Rect {
        val rect = Rect()
        rect.left = ((width - mCropFrameWidth) / 2).toInt()
        rect.right = ((width + mCropFrameWidth) / 2).toInt()
        rect.top = ((height - mCropFrameHeight) / 2).toInt()
        rect.bottom = ((height + mCropFrameHeight) / 2).toInt()
        return rect
    }

    init {
        val typeArray = context.obtainStyledAttributes(
            attrs, R.styleable.CropView, defStyleAttr, defStyleRes
        )
        mCropMaskColor = typeArray.getColor(
            R.styleable.CropView_crop_mask_layer_color,
            context.getColor(R.color.default_crop_frame_mask_color)
        )
        mCropFrameType = when (
            typeArray.getInt(R.styleable.CropView_crop_frame_type, CropFrameType.CIRCLE.ordinal)
        ) {
            CropFrameType.CIRCLE.ordinal -> CropFrameType.CIRCLE
            CropFrameType.SQUARE.ordinal -> CropFrameType.SQUARE
            CropFrameType.GRID9.ordinal -> CropFrameType.GRID9
            CropFrameType.RECTANGLE.ordinal -> CropFrameType.RECTANGLE
            else -> CropFrameType.CIRCLE
        }
        mCropFrameStrokeColor = typeArray.getColor(
            R.styleable.CropView_crop_frame_stroke_color,
            context.getColor(R.color.md_theme_primaryFixedDim)
        )
        mFrameNeededWidth = typeArray.getDimension(
            R.styleable.CropView_crop_frame_width, mDefaultCropFrameWidth
        )
        mFrameNeededHeight = typeArray.getDimension(
            R.styleable.CropView_crop_frame_height, mDefaultCropFrameHeight
        )
        typeArray.recycle()
    }

}