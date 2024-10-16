/*
 * Copyright 2021-2024 VastGui
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
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.VectorDrawable
import android.util.AttributeSet
import android.view.View
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

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/4/1
// Documentation: https://ave.entropy2020.cn/documents/tools/core-topics/ui/viewpager2/vp2-indicator-view/vp2-indicator-view/

/**
 * Vp2IndicatorView.
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
 * @property mColorSelected Indicator selected color.
 * @property mColorUnSelected Indicator unselected color.
 * @property mBitmapSelectedWidth The width of mBitmapSelected and
 *     mBitmapUnSelected.
 * @property mBitmapSelectedHeight The height of mBitmapSelected and
 *     mBitmapUnSelected.
 * @property mBitmapSelected The bitmap when indicator is selected.
 * @property mBitmapUnSelected The bitmap when indicator is unselected.
 * @property mIndicatorItemDistance The indicator item distance in pixels.
 * @property mIndicatorStyle Indicator style. By default the value is
 *     CIRCLE.
 * @property mIndicatorCircleRadius The radius of the indicator circle in
 *     pixels.
 * @property mIndicatorItemWidth The width of the indicator view.
 * @property mIndicatorItemHeight The height of the indicator view.
 * @property mIndicatorItemCount The count of the indicator item.
 * @property mCurrentSelectedPosition The current position of the
 *     indicator.
 * @property mViewPager2 The [ViewPager2] that the [Vp2IndicatorView] will
 *     attach to.
 * @since 0.2.0
 */
class Vp2IndicatorView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.Default_Vp2IndicatorView_Style,
    defStyleRes: Int = R.style.BaseVp2Indicator
) : View(context, attrs, defStyleAttr, defStyleRes) {

    private val mDefaultindicatorCircleRadius
        get() = context.resources.getDimension(R.dimen.default_indicator_circle_radius)
    private val mDefaultIndicatorItemDistance
        get() = context.resources.getDimension(R.dimen.default_indicator_item_distance)
    private val mDefaultIndicatorItemCount
        get() = context.resources.getInteger(R.integer.default_indicator_item_count)

    private val mUnSelectedPaint = Paint().apply {
        style = Paint.Style.FILL
        isAntiAlias = true
    }
    private val mSelectedPaint = Paint().apply {
        style = Paint.Style.FILL
        isAntiAlias = true
    }
    private val mBitmapPaint = Paint()
    private var mIndicatorItemWidth = 0
    private var mIndicatorItemHeight = 0
    private var mViewPager2: ViewPager2? = null

    var mColorSelected: Int by NotNUllVar()
        private set
    var mColorUnSelected: Int by NotNUllVar()
        private set
    var mBitmapSelectedWidth: Int by NotNUllVar()
        private set
    var mBitmapSelectedHeight: Int by NotNUllVar()
        private set
    var mBitmapSelected: Bitmap by NotNUllVar()
        private set
    var mBitmapUnSelected: Bitmap by NotNUllVar()
        private set
    var mIndicatorItemDistance: Float = 0f
        private set
    var mIndicatorStyle: Vp2IndicatorType = Vp2IndicatorType.CIRCLE
        private set
    var mIndicatorCircleRadius: Float = 0f
        private set
    var mIndicatorItemCount = 0
        private set
    var mCurrentSelectedPosition = 0
        private set

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        when (mIndicatorStyle) {
            Vp2IndicatorType.CIRCLE -> {
                mIndicatorItemWidth = widthSize.toFloat().coerceAtMost(
                    2 * mIndicatorCircleRadius * mIndicatorItemCount
                            + (mIndicatorItemCount - 1) * mIndicatorItemDistance
                ).toInt()
                mIndicatorItemHeight =
                    heightSize.toFloat().coerceAtMost(2 * mIndicatorCircleRadius).toInt()
            }

            Vp2IndicatorType.BITMAP -> {
                mIndicatorItemWidth = widthSize.toFloat().coerceAtMost(
                    mBitmapSelectedWidth * mIndicatorItemCount
                            + (mIndicatorItemCount - 1) * mIndicatorItemDistance
                ).toInt()
                mIndicatorItemHeight =
                    heightSize.coerceAtMost(mBitmapSelectedHeight)
            }
        }
        setMeasuredDimension(mIndicatorItemWidth, mIndicatorItemHeight)
    }

    override fun onDraw(canvas: Canvas) {
        mSelectedPaint.color = mColorSelected
        mUnSelectedPaint.color = mColorUnSelected
        when (mIndicatorStyle) {
            Vp2IndicatorType.CIRCLE -> {
                val itemStart = (width - mIndicatorItemWidth) / 2 + mIndicatorCircleRadius
                val cy = (mIndicatorItemHeight / 2).toFloat()
                for (i in 0 until mIndicatorItemCount) {
                    val cx = itemStart + i * (mIndicatorCircleRadius * 2 + mIndicatorItemDistance)
                    canvas.drawCircle(
                        cx, cy, mIndicatorCircleRadius,
                        (if (i == mCurrentSelectedPosition) mSelectedPaint else mUnSelectedPaint)
                    )
                }
            }

            Vp2IndicatorType.BITMAP -> {
                for (i in 0 until mIndicatorItemCount) {
                    val cx = i * (mBitmapSelectedWidth + mIndicatorItemDistance)
                    val bmp =
                        if (i == mCurrentSelectedPosition) mBitmapSelected else mBitmapUnSelected
                    canvas.drawBitmap(bmp, cx, 0f, mBitmapPaint)
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
        mIndicatorStyle = style
    }

    /**
     * Set indicator selected color.
     *
     * @since 0.2.0
     */
    fun setSelectedColor(@ColorRes id: Int) {
        mColorSelected = ContextCompat.getColor(context, id)
    }

    /**
     * Set indicator unselected color.
     *
     * @since 0.2.0
     */
    fun setUnSelectedColor(@ColorRes id: Int) {
        mColorUnSelected = ContextCompat.getColor(context, id)
    }

    /**
     * Set the size of [mBitmapSelected] and [mBitmapUnSelected].
     *
     * @since 0.5.0
     */
    fun setBitmapSize(@IntRange(from = 0) width: Int, @IntRange(from = 0) height: Int) {
        mBitmapSelectedWidth = width
        mBitmapSelectedHeight = height
    }

    /**
     * Set selected bitmap, now supports [BitmapDrawable] , [VectorDrawable] ,
     * [VectorDrawableCompat]
     *
     * @since 0.5.0
     */
    fun setSelectedBitmap(@DrawableRes id: Int) {
        val bitmap = BmpUtils.getBitmapFromDrawable(id, context)
        mBitmapSelected = BmpUtils.scaleBitmap(bitmap, mBitmapSelectedWidth, mBitmapSelectedHeight)
    }

    /**
     * Set Unselected bitmap, now supports [BitmapDrawable] , [VectorDrawable]
     * , [VectorDrawableCompat]
     *
     * @since 0.5.0
     */
    fun setUnSelectedBitmap(@DrawableRes id: Int) {
        val bitmap = BmpUtils.getBitmapFromDrawable(id, context)
        mBitmapUnSelected =
            BmpUtils.scaleBitmap(bitmap, mBitmapSelectedWidth, mBitmapSelectedHeight)
    }

    /**
     * Set indicator circle radius.
     *
     * @since 0.2.0
     */
    fun setIndicatorCircleRadius(@FloatRange(from = 0.0) radius: Float) {
        mIndicatorCircleRadius = radius
    }

    /**
     * Set indicator item count.
     *
     * @throws RuntimeException
     * @since 0.2.0
     */
    fun setIndicatorItemCount(@IntRange(from = 0) count: Int) {
        if (null != mViewPager2) {
            throw RuntimeException("You should not call this method when Vp2IndicatorView is attached to ViewPager2.")
        }
        mIndicatorItemCount = count
        verifyItemCount()
    }

    /**
     * Set the distance of the indicator item.
     *
     * @since 0.2.0
     */
    fun setIndicatorItemDistance(@FloatRange(from = 0.0) distance: Float) {
        mIndicatorItemDistance = distance
    }

    /**
     * Set current selected indicator item position.
     *
     * @throws RuntimeException If you have set the specified ViewPager2,
     *     calling this method will throw [RuntimeException].
     * @since 0.2.0
     */
    @Throws(RuntimeException::class)
    fun setCurrentSelectedPosition(position: Int) {
        if (null != mViewPager2) {
            throw RuntimeException("You should not call this method when Vp2IndicatorView is attached to ViewPager2.")
        }
        mCurrentSelectedPosition = position.coerceIn(0, mIndicatorItemCount)
    }

    /**
     * Set the [ViewPager2] that the [Vp2IndicatorView] will attach to.
     *
     * @since 0.2.0
     */
    fun attachToViewPager2(vp2: ViewPager2) {
        mViewPager2 = vp2
        val pagerAdapter = mViewPager2!!.adapter
        if (pagerAdapter != null) {
            mIndicatorItemCount = pagerAdapter.itemCount
            mCurrentSelectedPosition = mViewPager2!!.currentItem
            verifyItemCount()
        }
        vp2.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                if (pagerAdapter != null) {
                    mCurrentSelectedPosition = mViewPager2!!.currentItem
                }
                postInvalidate()
            }
        })
    }

    /**
     * Verifies that the indicator item count.
     *
     * If [mIndicatorItemCount] is 0, it will set the [View.GONE] as the
     * visibility value.
     *
     * @since 0.2.0
     */
    private fun verifyItemCount() {
        if (mCurrentSelectedPosition >= mIndicatorItemCount) {
            mCurrentSelectedPosition = mIndicatorItemCount - 1
        }
        visibility = if (mIndicatorItemCount <= 0) GONE else VISIBLE
    }

    init {
        val typeArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.Vp2IndicatorView,
            defStyleAttr,
            defStyleRes
        )
        mIndicatorStyle =
            when (typeArray.getInt(
                R.styleable.Vp2IndicatorView_indicator_style,
                Vp2IndicatorType.CIRCLE.ordinal
            )) {
                Vp2IndicatorType.CIRCLE.ordinal -> Vp2IndicatorType.CIRCLE
                Vp2IndicatorType.BITMAP.ordinal -> Vp2IndicatorType.BITMAP
                else -> Vp2IndicatorType.CIRCLE
            }
        mColorSelected =
            typeArray.getColor(
                R.styleable.Vp2IndicatorView_indicator_selected_color,
                ContextCompat.getColor(context, R.color.md_theme_primary)
            )
        mColorUnSelected =
            typeArray.getColor(
                R.styleable.Vp2IndicatorView_indicator_unselected_color,
                ContextCompat.getColor(context, R.color.md_theme_primaryContainer)
            )
        mIndicatorCircleRadius =
            typeArray.getDimension(
                R.styleable.Vp2IndicatorView_indicator_circle_radius,
                mDefaultindicatorCircleRadius
            )
        mIndicatorItemCount =
            typeArray.getInt(
                R.styleable.Vp2IndicatorView_indicator_item_count,
                mDefaultIndicatorItemCount
            )
        mIndicatorItemDistance =
            typeArray.getDimension(
                R.styleable.Vp2IndicatorView_indicator_item_distance,
                mDefaultIndicatorItemDistance
            )
        typeArray.recycle()
        verifyItemCount()
    }
}