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
import com.ave.vastgui.core.extension.NotNUllVar
import com.ave.vastgui.tools.R
import com.ave.vastgui.tools.utils.ColorUtils
import com.ave.vastgui.tools.utils.DensityUtils
import com.ave.vastgui.tools.utils.DensityUtils.DP
import com.ave.vastgui.tools.utils.ScreenSizeUtils

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/4/18
// Documentation: https://ave.entropy2020.cn/documents/VastTools/core-topics/ui/cropview/CropView/

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
class CropView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.Default_CropView_Style,
    defStyleRes: Int = R.style.BaseCropView
) : View(context, attrs, defStyleAttr, defStyleAttr), CropFrame {

    private val DEFAULT_FRAME_WIDTH = DensityUtils.dp2px(100F.DP)
    private val DEFAULT_FRAME_HEIGHT = DensityUtils.dp2px(100F.DP)

    private var mCropViewHeight: Int by NotNUllVar()
    private var mCropViewWidth: Int by NotNUllVar()

    private val mXfermodeDstOut = PorterDuffXfermode(PorterDuff.Mode.DST_OUT)
    private var mCropFramePaint: Paint by NotNUllVar()
    private var mCropFrameStrokePaint: Paint by NotNUllVar()
    private var mCropFrameGuidelinePaint: Paint by NotNUllVar()
    private var mCropFrameStrokeCornerPaint: Paint by NotNUllVar()

    private val mCropFrameStrokeWidth: Float = DensityUtils.dp2px(1.5f)
    private val mCropFrameGuidelineWidth: Float = DensityUtils.dp2px(1.5f)
    private val mCropFrameStrokeCornerWidth: Float = DensityUtils.dp2px(3.5f)
    private val mCropFrameStrokeCornerLength: Float = DensityUtils.dp2px(25f)

    var mCropMaskColor: Int by NotNUllVar()
        private set

    var mCropFrameType: CropFrameType by NotNUllVar()
        private set

    var mCropFrameWidth: Float by NotNUllVar()
        private set

    var mCropFrameHeight: Float by NotNUllVar()
        private set

    var mCropFrameStrokeColor: Int by NotNUllVar()
        private set

    override fun onDraw(canvas: Canvas) {
        canvas.saveLayer(0f, 0f, mCropViewWidth.toFloat(), mCropViewHeight.toFloat(), null)
        canvas.drawColor(mCropMaskColor)
        mCropFramePaint.xfermode = mXfermodeDstOut
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
     * @since 0.5.0
     */
    override fun setCropMaskColor(@ColorInt color: Int) {
        mCropMaskColor = color
    }

    /**
     * Set crop frame shape type.
     *
     * @see CropView.FrameType
     * @since 0.5.0
     */
    override fun setCropFrameType(type: CropFrameType) {
        mCropFrameType = type
    }

    /**
     * Set crop frame size.
     *
     * @param width The frame width.
     * @param height The frame height.
     * @since 0.5.0
     */
    override fun setCropFrameSize(width: Float, height: Float) {
        val maxWidthContainer =
            (mCropViewWidth - 2 * mCropFrameStrokeWidth).coerceAtMost(width)
        val maxHeightContainer =
            (mCropViewHeight - 2 * mCropFrameStrokeWidth).coerceAtMost(height)
        when (mCropFrameType) {
            CropFrameType.RECTANGLE -> {
                mCropFrameWidth = maxWidthContainer
                mCropFrameHeight = maxHeightContainer
            }

            else -> {
                val minValue = maxHeightContainer.coerceAtMost(maxWidthContainer)
                mCropFrameWidth = minValue
                mCropFrameHeight = minValue
            }
        }
    }

    /**
     * Set crop frame stroke width in pixels.
     *
     * @since 0.5.0
     */
    override fun setCropFrameStrokeColor(@ColorInt color: Int) {
        mCropFrameStrokeColor = color
    }

    /**
     * Init [mCropFramePaint]
     *
     * @since 0.5.0
     */
    private fun initCropFramePaint() {
        mCropFramePaint = Paint().apply {
            isAntiAlias = true
            style = Paint.Style.FILL
        }
    }

    /**
     * Init [mCropFrameStrokePaint]
     *
     * @since 0.5.0
     */
    private fun initCropFrameStrokePaint() {
        mCropFrameStrokePaint = Paint().apply {
            strokeWidth = mCropFrameStrokeWidth
            isAntiAlias = true
            style = Paint.Style.STROKE
        }
    }

    /**
     * Init [mCropFrameGuidelinePaint].
     *
     * @since 0.5.0
     */
    private fun initCropFrameGuidelinePaint() {
        mCropFrameGuidelinePaint = Paint().apply {
            strokeWidth = mCropFrameGuidelineWidth
            isAntiAlias = true
            style = Paint.Style.STROKE
        }
    }

    /**
     * Init [mCropFrameStrokeCornerPaint].
     *
     * @since 0.5.0
     */
    private fun initCropFrameStrokeCornerPaint() {
        mCropFrameStrokeCornerPaint = Paint().apply {
            strokeWidth = mCropFrameStrokeCornerWidth
            isAntiAlias = true
            style = Paint.Style.STROKE
        }
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
            ColorUtils.colorHex2Int(DEFAULT_MASK_COLOR)
        )
        mCropFrameType = when (
            typeArray.getInt(R.styleable.CropView_crop_frame_type, 0)
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
        mCropFrameWidth = typeArray.getDimension(
            R.styleable.CropView_crop_frame_width, DEFAULT_FRAME_WIDTH
        )
        mCropFrameHeight = typeArray.getDimension(
            R.styleable.CropView_crop_frame_height, DEFAULT_FRAME_HEIGHT
        )
        mCropViewWidth =
            ScreenSizeUtils.getMobileScreenSize(context).width
        mCropViewHeight =
            ScreenSizeUtils.getMobileScreenSize(context).height
        initCropFramePaint()
        initCropFrameStrokePaint()
        initCropFrameGuidelinePaint()
        initCropFrameStrokeCornerPaint()
        typeArray.recycle()
    }

    companion object {
        const val DEFAULT_MASK_COLOR = "#A8000000"
    }

}