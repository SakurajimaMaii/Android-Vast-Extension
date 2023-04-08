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

package com.ave.vastgui.tools.view.vp2indicator

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorRes
import androidx.annotation.FloatRange
import androidx.annotation.IntRange
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.ave.vastgui.core.extension.NotNUllVar
import com.ave.vastgui.tools.R
import com.ave.vastgui.tools.utils.DensityUtils.PX


// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/4/1
// Documentation: https://ave.entropy2020.cn/documents/VastTools/core-topics/ui/viewpager2/Vp2IndicatorView/

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
 * @param context The Context the view is running in, through which it can
 *     access the current theme, resources, etc.
 * @param attrs The attributes of the XML tag that is inflating the view.
 * @param defStyleAttr An attribute in the current theme that contains a
 *     reference to a style resource that supplies default values for the
 *     view. Can be 0 to not look for defaults.
 * @param defStyleRes A resource identifier of a style resource that
 *     supplies default values for the view, used only if defStyleAttr is 0
 *     or can not be found in the theme. Can be 0 to not look for defaults.
 * @property mColorSelected Indicator selected color.
 * @property mColorUnSelected Indicator unselected color.
 * @property mIndicatorItemDistance The indicator item distance.
 * @property mIndicatorStyle Indicator style. By default the value is
 *     [STYLE_CIRCLE].
 * @property mIndicatorCircleRadius The radius of the indicator circle.
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

    companion object {
        /** Circle indicator style */
        const val STYLE_CIRCLE = 0
    }

    private var mColorSelected: ColorStateList? = null
    private var mColorUnSelected: ColorStateList? = null
    private var mIndicatorItemDistance: Float = 0f
    private var mIndicatorStyle: Int = STYLE_CIRCLE
    private var mIndicatorCircleRadius: Float = 0f
    private var mIndicatorItemWidth = 0
    private var mIndicatorItemHeight = 0
    private var mIndicatorItemCount: Int by NotNUllVar()
    private var mCurrentSelectedPosition = 0
    private var mUnSelectedPaint: Paint by NotNUllVar()
    private var mSelectedPaint: Paint by NotNUllVar()
    private var mIndicatorItemRectF: RectF by NotNUllVar()
    private var mViewPager2: ViewPager2? = null

    init {
        val a = context.obtainStyledAttributes(
            attrs,
            R.styleable.Vp2Indicator,
            defStyleAttr,
            defStyleRes
        )
        mIndicatorStyle = a.getInt(R.styleable.Vp2Indicator_indicator_style, STYLE_CIRCLE)
        mColorSelected = a.getColorStateList(R.styleable.Vp2Indicator_indicator_selected_color)
        mColorUnSelected =
            a.getColorStateList(R.styleable.Vp2Indicator_indicator_unselected_color)
        mIndicatorCircleRadius =
            a.getDimension(R.styleable.Vp2Indicator_indicator_circle_radius, 0F.PX)
        mIndicatorItemCount = a.getInt(R.styleable.Vp2Indicator_indicator_item_count, 0)
        mIndicatorItemDistance =
            a.getDimension(R.styleable.Vp2Indicator_indicator_item_distance, 0F.PX)
        a.recycle()
        mUnSelectedPaint = Paint().apply {
            style = Paint.Style.FILL
            isAntiAlias = true
            color = if (mColorUnSelected == null) Color.GRAY else mColorUnSelected!!.defaultColor
        }
        mSelectedPaint = Paint().apply {
            style = Paint.Style.FILL
            isAntiAlias = true
            color = if (mColorSelected == null) Color.RED else mColorSelected!!.defaultColor
        }
        mIndicatorItemRectF = RectF()
        verifyItemCount()
    }

    /**
     * Set indicator selected color.
     *
     * @since 0.2.0
     */
    fun setSelectedColor(@ColorRes id: Int) {
        mColorSelected = context.getColorStateList(id)
    }

    /**
     * Set indicator unselected color.
     *
     * @since 0.2.0
     */
    fun setUnSelectedColor(@ColorRes id: Int) {
        mColorUnSelected = context.getColorStateList(id)
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
     * @throws RuntimeException
     * @since 0.2.0
     */
    fun setCurrentSelectedPosition(position: Int) {
        if (null != mViewPager2) {
            throw RuntimeException("You should not call this method when Vp2IndicatorView is attached to ViewPager2.")
        }
        if (position < 0 || position >= mIndicatorItemCount) {
            throw RuntimeException("The value of $position is error.")
        }
        mCurrentSelectedPosition = position
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

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        mIndicatorItemWidth = widthSize.toFloat().coerceAtMost(
            2 * mIndicatorCircleRadius * mIndicatorItemCount
                    + (mIndicatorItemCount - 1) * mIndicatorItemDistance
        ).toInt()
        mIndicatorItemHeight =
            heightSize.toFloat().coerceAtMost(2 * mIndicatorCircleRadius).toInt()
        setMeasuredDimension(mIndicatorItemWidth, mIndicatorItemHeight)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val cy = (mIndicatorItemHeight / 2).toFloat()
        for (i in 0 until mIndicatorItemCount) {
            val cx: Float = (i + 1) * mIndicatorCircleRadius + i * mIndicatorItemDistance
            canvas.drawCircle(
                cx.toFloat(), cy, mIndicatorCircleRadius,
                (if (i == mCurrentSelectedPosition) mSelectedPaint else mUnSelectedPaint)
            )
        }
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
}