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

package com.ave.vastgui.tools.view.vp2indicator

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.VectorDrawable
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.FloatRange
import androidx.annotation.IntRange
import androidx.core.content.ContextCompat
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.ave.vastgui.core.extension.NotNUllVar
import com.ave.vastgui.tools.R
import com.ave.vastgui.tools.graphics.BmpUtils
import com.ave.vastgui.tools.utils.dimension
import androidx.core.content.withStyledAttributes
import com.ave.vastgui.tools.utils.ColorUtils
import com.ave.vastgui.tools.utils.color
import com.ave.vastgui.tools.utils.integer
import com.ave.vastgui.tools.view.extension.gone
import com.ave.vastgui.tools.view.extension.visible
import kotlin.math.roundToInt

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/4/1
// Documentation: https://sakurajimamaii.github.io/AVE-DOC/documents/tools/core-topics/ui/viewpager2/vp2-indicator-view/vp2-indicator-view/

/**
 * [Vp2IndicatorView].
 *
 * ```xml
 * <com.ave.vastgui.tools.view.vp2indicatorview.Vp2IndicatorView
 *     android:id="@+id/vp2indicator"
 *     android:layout_width="wrap_content"
 *     android:layout_height="wrap_content"
 *     android:layout_margin="20dp"
 *     app:indicator_item_count="3"
 * ```
 *
 * @since 0.2.0
 */
class Vp2IndicatorView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.Default_Vp2IndicatorView_Style,
    defStyleRes: Int = R.style.BaseVp2Indicator
) : View(context, attrs, defStyleAttr, defStyleRes) {

    /** @since 1.5.2 */
    @Suppress("PrivatePropertyName")
    private val DEFAULT_INDICATOR_CIRCLE_RADIUS
        get() = dimension(R.dimen.default_indicator_circle_radius)

    /** @since 1.5.2 */
    @Suppress("PrivatePropertyName")
    private val DEFAULT_INDICATOR_ITEM_DISTANCE
        get() = dimension(R.dimen.default_indicator_item_distance)

    /** @since 1.5.2 */
    @Suppress("PrivatePropertyName")
    private val DEFAULT_INDICATOR_ITEM_COUNT
        get() = integer(R.integer.default_indicator_item_count)

    /** @since 1.5.2 */
    @Suppress("PrivatePropertyName")
    private val DEFAULT_INDICATOR_BITMAP_WIDTH
        get() = dimension(R.dimen.default_indicator_bitmap_width)

    /** @since 1.5.2 */
    @Suppress("PrivatePropertyName")
    private val DEFAULT_INDICATOR_BITMAP_HEIGHT
        get() = dimension(R.dimen.default_indicator_bitmap_height)

    /** @since 1.5.2 */
    private val bmpSrcRect = Rect()

    /** @since 1.5.2 */
    private val bmpDstRectF = RectF()

    /** @since 1.5.2 */
    private val unselectedPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }

    /** @since 1.5.2 */
    private val selectedPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }

    /** @since 1.5.2 */
    private val bmpPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    /**
     * The [ViewPager2] that the [Vp2IndicatorView] will attach to.
     *
     * @since 1.5.2
     */
    private var viewPager2: ViewPager2? = null

    /** @since 1.5.2 */
    @get:ColorInt
    @setparam:ColorInt
    private var _selectedColor: Int
        set(value) {
            check(ColorUtils.isColorInt(value)) { "The selected color of text is invalid." }
            selectedPaint.setColor(value)
        }
        get() = selectedPaint.color


    /**
     * Indicator selected color.
     *
     * @since 1.5.2
     */
    @get:ColorInt
    val selectedColor: Int
        get() = _selectedColor

    /** @since 1.5.2 */
    @get:ColorInt
    @setparam:ColorInt
    private var _unselectedColor: Int
        set(value) {
            check(ColorUtils.isColorInt(value)) { "The unselected color of text is invalid." }
            unselectedPaint.setColor(value)
        }
        get() = unselectedPaint.color

    /**
     * Indicator unselected color.
     *
     * @since 1.5.2
     */
    @get:ColorInt
    val unselectedColor: Int
        get() = _unselectedColor

    /**
     * The width of [selectedBmp] and [unselectedBmp].
     *
     * @since 1.5.2
     */
    var bmpWidth: Int by NotNUllVar()
        private set

    /**
     * The height of [selectedBmp] and [unselectedBmp].
     *
     * @since 1.5.2
     */
    var bmpHeight: Int by NotNUllVar()
        private set

    /**
     * The bitmap shown when indicator is selected.
     *
     * @since 1.5.2
     */
    var selectedBmp: Bitmap? = null
        private set

    /**
     * The bitmap shown when indicator is unselected.
     *
     * @since 1.5.2
     */
    var unselectedBmp: Bitmap? = null
        private set

    /**
     * The indicator item distance(in pixels).
     *
     * @since 1.5.2
     */
    var indicatorItemDistance: Float = 0f
        private set

    /**
     * Indicator style. By default the value is [Vp2IndicatorType.Circle].
     *
     * @since 1.5.2
     */
    var indicatorStyle: Vp2IndicatorType = Vp2IndicatorType.Circle
        private set

    /**
     * The radius of the indicator circle(in pixels).
     *
     * @since 1.5.2
     */
    var indicatorCircleRadius: Float = 0f
        private set

    /**
     * The count of the indicator item.
     *
     * @since 1.5.2
     */
    var indicatorItemCount = 0
        private set

    /**
     * The current position of the indicator.
     *
     * @since 1.5.2
     */
    var currentSelectedPosition = 0
        private set

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        when (indicatorStyle) {
            Vp2IndicatorType.Circle -> {
                val neededMinimumWidth = (2 * indicatorCircleRadius * indicatorItemCount) +
                        (indicatorItemDistance * (indicatorItemCount - 1)) +
                        paddingStart + paddingEnd
                val neededMinimumHeight = (2 * indicatorCircleRadius) + paddingTop + paddingBottom
                val width = resolveSize(neededMinimumWidth.roundToInt(), widthMeasureSpec)
                val height = resolveSize(neededMinimumHeight.roundToInt(), heightMeasureSpec)
                setMeasuredDimension(width, height)
            }

            Vp2IndicatorType.Bitmap -> {
                val neededMinimumWidth = (bmpWidth * indicatorItemCount) +
                        (indicatorItemDistance * (indicatorItemCount - 1)) +
                        paddingStart + paddingEnd
                val neededMinimumHeight = bmpHeight + paddingTop + paddingBottom
                val width = resolveSize(neededMinimumWidth.roundToInt(), widthMeasureSpec)
                val height = resolveSize(neededMinimumHeight, heightMeasureSpec)
                setMeasuredDimension(width, height)
            }
        }

    }

    override fun onDraw(canvas: Canvas) {
        when (indicatorStyle) {
            Vp2IndicatorType.Circle -> {
                val itemStart = paddingStart + indicatorCircleRadius
                val cy = (paddingTop + measuredHeight - paddingBottom) / 2f
                for (i in 0 until indicatorItemCount) {
                    val cx = itemStart + i * (indicatorCircleRadius * 2 + indicatorItemDistance)
                    canvas.drawCircle(
                        cx, cy, indicatorCircleRadius,
                        (if (i == currentSelectedPosition) selectedPaint else unselectedPaint)
                    )
                }
            }

            Vp2IndicatorType.Bitmap -> {
                val selectedBitmap = BmpUtils.scaleBitmap(selectedBmp!!, bmpWidth, bmpHeight)
                val unselectedBitmap = BmpUtils.scaleBitmap(unselectedBmp!!, bmpWidth, bmpHeight)
                bmpSrcRect.set(0, 0, bmpWidth, bmpHeight)
                val itemStart = paddingStart + bmpWidth / 2f
                val cy = (paddingTop + measuredHeight - paddingBottom) / 2f
                for (i in 0 until indicatorItemCount) {
                    val bmp = if (i == currentSelectedPosition) selectedBitmap else unselectedBitmap
                    val cx = itemStart + i * (bmpWidth + indicatorItemDistance)
                    bmpDstRectF.set(cx - bmpWidth / 2f, cy - bmpHeight / 2f, cx + bmpWidth / 2f, cy + bmpHeight / 2f)
                    canvas.drawBitmap(bmp, bmpSrcRect, bmpDstRectF, bmpPaint)
                }
            }
        }
    }

    /**
     * Set indicator style.
     *
     * @since 0.5.0
     */
    fun setIndicatorStyle(style: Vp2IndicatorType) {
        if (indicatorStyle == style) return
        if (style == Vp2IndicatorType.Bitmap && (selectedBmp == null || unselectedBmp == null)) {
            return
        }
        indicatorStyle = style
        requestLayout()
    }

    /**
     * Set indicator selected color.
     *
     * @since 0.2.0
     */
    fun setSelectedColor(@ColorRes id: Int) {
        _selectedColor = ContextCompat.getColor(context, id)
        invalidate()
    }

    /**
     * Set indicator selected color-int.
     *
     * @since 1.5.2
     */
    fun setSelectedColorInt(@ColorInt color: Int) {
        _selectedColor = color
        invalidate()
    }

    /**
     * Set indicator unselected color.
     *
     * @since 0.2.0
     */
    fun setUnSelectedColor(@ColorRes id: Int) {
        _unselectedColor = ContextCompat.getColor(context, id)
        invalidate()
    }

    /**
     * Set indicator unselected color-int.
     *
     * @since 1.5.2
     */
    fun setUnSelectedColorInt(@ColorInt color: Int) {
        _unselectedColor = color
        invalidate()
    }

    /**
     * Set the size of [selectedBmp] and [unselectedBmp] (in pixels).
     *
     * @since 0.5.0
     */
    fun setBitmapSize(@IntRange(from = 0) width: Int, @IntRange(from = 0) height: Int) {
        bmpWidth = width.coerceAtLeast(0)
        bmpHeight = height.coerceAtLeast(0)
        requestLayout()
    }

    /**
     * Set selected bitmap, now supports [BitmapDrawable] , [VectorDrawable] ,
     * [VectorDrawableCompat]
     *
     * @since 0.5.0
     */
    fun setSelectedBitmap(@DrawableRes id: Int) {
        selectedBmp = BmpUtils.getBitmapFromDrawable(id, context)
        invalidate()
    }

    /**
     * Set Unselected bitmap, now supports [BitmapDrawable] , [VectorDrawable]
     * , [VectorDrawableCompat]
     *
     * @since 0.5.0
     */
    fun setUnSelectedBitmap(@DrawableRes id: Int) {
        unselectedBmp = BmpUtils.getBitmapFromDrawable(id, context)
        invalidate()
    }

    /**
     * Set indicator circle radius.
     *
     * @since 0.2.0
     */
    fun setIndicatorCircleRadius(@FloatRange(from = 0.0) radius: Float) {
        if (indicatorCircleRadius == radius) return
        indicatorCircleRadius = radius.coerceAtLeast(0f)
        requestLayout()
    }

    /**
     * Set indicator item count.
     *
     * @throws IllegalStateException If you have set the specified ViewPager2,
     * calling this method will throw [IllegalStateException].
     * @since 0.2.0
     */
    fun setIndicatorItemCount(@IntRange(from = 0) count: Int) {
        if (indicatorItemCount == count) return
        check(null == viewPager2) {
            "You should not call setIndicatorItemCount() when Vp2IndicatorView is attached to ViewPager2."
        }
        indicatorItemCount = count
        verifyItemCount()
        requestLayout()
    }

    /**
     * Set the distance of the indicator item.
     *
     * @since 0.2.0
     */
    fun setIndicatorItemDistance(@FloatRange(from = 0.0) distance: Float) {
        if (indicatorItemDistance == distance) return
        indicatorItemDistance = distance.coerceAtLeast(0f)
        requestLayout()
    }

    /**
     * Set current selected indicator item position.
     *
     * @throws IllegalStateException If you have set the specified ViewPager2,
     * calling this method will throw [IllegalStateException].
     * @since 0.2.0
     */
    @Throws(RuntimeException::class)
    fun setCurrentSelectedPosition(position: Int) {
        check(null == viewPager2) {
            "You shouldn't call setCurrentSelectedPosition() when Vp2IndicatorView is attached to ViewPager2."
        }
        currentSelectedPosition = position.coerceIn(0, indicatorItemCount - 1)
        invalidate()
    }

    /**
     * Set the [ViewPager2] that the [Vp2IndicatorView] will attach to.
     *
     * @since 0.2.0
     */
    fun attachToViewPager2(vp2: ViewPager2) {
        viewPager2 = vp2
        val pagerAdapter = viewPager2!!.adapter
        if (pagerAdapter != null) {
            indicatorItemCount = pagerAdapter.itemCount
            currentSelectedPosition = viewPager2!!.currentItem
            verifyItemCount()
        }
        vp2.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                if (pagerAdapter != null) {
                    currentSelectedPosition = viewPager2!!.currentItem
                }
                postInvalidate()
            }
        })
    }

    /**
     * Verifies that the indicator item count.
     *
     * If [indicatorItemCount] is 0, it will set the [View.GONE] as the
     * visibility value.
     *
     * @since 0.2.0
     */
    private fun verifyItemCount() {
        if (currentSelectedPosition >= indicatorItemCount) {
            currentSelectedPosition = indicatorItemCount - 1
        }
        if (indicatorItemCount <= 0) gone() else visible()
    }

    init {
        context.withStyledAttributes(attrs, R.styleable.Vp2IndicatorView, defStyleAttr, defStyleRes) {
            indicatorStyle = when (getInt(R.styleable.Vp2IndicatorView_indicator_style, Vp2IndicatorType.Circle.ordinal)) {
                Vp2IndicatorType.Circle.ordinal -> Vp2IndicatorType.Circle
                Vp2IndicatorType.Bitmap.ordinal -> Vp2IndicatorType.Bitmap
                else -> Vp2IndicatorType.Circle
            }
            _selectedColor = getColor(R.styleable.Vp2IndicatorView_indicator_selected_color, color(R.color.md_theme_primary))
            _unselectedColor = getColor(R.styleable.Vp2IndicatorView_indicator_unselected_color, color(R.color.md_theme_primaryContainer))
            bmpWidth = getDimension(R.styleable.Vp2IndicatorView_indicator_bitmap_width, DEFAULT_INDICATOR_BITMAP_WIDTH).roundToInt()
            bmpHeight = getDimension(R.styleable.Vp2IndicatorView_indicator_bitmap_height, DEFAULT_INDICATOR_BITMAP_HEIGHT).roundToInt()
            indicatorCircleRadius = getDimension(R.styleable.Vp2IndicatorView_indicator_circle_radius, DEFAULT_INDICATOR_CIRCLE_RADIUS)
            indicatorItemCount = getInt(R.styleable.Vp2IndicatorView_indicator_item_count, DEFAULT_INDICATOR_ITEM_COUNT)
            indicatorItemDistance = getDimension(R.styleable.Vp2IndicatorView_indicator_item_distance, DEFAULT_INDICATOR_ITEM_DISTANCE)
        }
        verifyItemCount()
    }
}