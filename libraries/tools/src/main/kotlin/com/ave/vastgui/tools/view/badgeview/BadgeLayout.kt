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

package com.ave.vastgui.tools.view.badgeview

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.FloatRange
import androidx.annotation.StyleRes
import androidx.core.content.ContextCompat
import androidx.core.view.children
import com.ave.vastgui.core.extension.NotNUllVar
import com.ave.vastgui.core.extension.nothing_to_do
import com.ave.vastgui.core.utils.Quadruple
import com.ave.vastgui.tools.R
import com.ave.vastgui.tools.databinding.BadgeLayoutBinding
import com.ave.vastgui.tools.utils.DensityUtils.DP
import com.ave.vastgui.tools.view.extension.refreshWithInvalidate
import com.ave.vastgui.tools.viewbinding.viewBinding

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/9/11
// Documentation: https://ave.entropy2020.cn/documents/tools/core-topics/ui/badge/description/

/**
 * Badge Layout.
 *
 * @property mIcon The icon resources id.
 * @property mIconPadding The padding of the [mIcon].
 * @since 0.5.3
 */
class BadgeLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.Default_BadgeLayout_Style,
    @StyleRes defStyleRes: Int = R.style.BaseBadgeLayout
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

    private val mBinding by viewBinding(BadgeLayoutBinding::bind)

    private val mBadgeView
        get() = mBinding.badgeLayoutBadge

    private var mIcon by NotNUllVar<Int>()
    private var mIconPadding by NotNUllVar<Quadruple<Float, Float, Float, Float>>()

    /**
     * @see BadgeView.mBadgeState
     * @since 0.5.3
     */
    val mState
        get() = mBadgeView.mBadgeState

    /**
     * @see BadgeView.setMode
     * @since 0.5.3
     */
    var mMode: BadgeMode
        set(value) = mBadgeView.refreshWithInvalidate {
            setMode(value)
        }
        get() = mBadgeView.mBadgeMode

    /**
     * @see BadgeView.setColor
     * @since 0.5.3
     */
    var mColor: Int
        set(value) = mBadgeView.refreshWithInvalidate {
            setColor(value)
        }
        get() = mBadgeView.mBadgeColor

    /**
     * @see BadgeView.setDotRadius
     * @since 0.5.3
     */
    var mDotRadius: Float
        set(value) = mBadgeView.refreshWithInvalidate {
            setDotRadius(value)
        }
        get() = mBadgeView.mDotRadius

    /**
     * @see BadgeView.setBubbleRadius
     * @since 0.5.3
     */
    var mBubbleRadius: Float
        set(value) = mBadgeView.refreshWithInvalidate {
            setBubbleRadius(value)
        }
        get() = mBadgeView.mMoveRadius

    /**
     * @see BadgeView.setBubbleText
     * @since 0.5.3
     */
    var mBubbleText: String
        set(value) = mBadgeView.refreshWithInvalidate {
            setBubbleText(value)
        }
        get() = mBadgeView.mText

    /**
     * @throws IllegalArgumentException The given value should be greater
     *     than 0.
     * @see BadgeView.setBubbleTextNum
     * @since 0.5.3
     */
    var mBubbleTextNum: Int
        set(value) {
            if (value < 0)
                throw IllegalArgumentException("The value should be greater than 0.")
            mBadgeView.refreshWithInvalidate {
                setBubbleTextNum(value)
            }
        }
        get() = mBadgeView.mTextNumber

    /**
     * @throws IllegalArgumentException The given value should be greater
     *     than 0.
     * @see BadgeView.setBubbleTextMaxNum
     * @since 0.5.3
     */
    var mBubbleTextMaxNum: Int
        set(value) {
            if (value < 0)
                throw IllegalArgumentException("The value should be greater than 0.")
            mBadgeView.refreshWithInvalidate {
                setBubbleTextMaxNum(value)
            }
        }
        get() = mBadgeView.mTextMaxNumber

    /**
     * @see BadgeView.setBubbleTextColor
     * @since 0.5.3
     */
    var mBubbleTextColor: Int
        set(value) = mBadgeView.refreshWithInvalidate {
            setBubbleTextColor(value)
        }
        get() = mBadgeView.mTextColor

    /**
     * @see BadgeView.setBubbleTextSize
     * @since 0.5.3
     */
    var mBubbleTextSize: Float
        set(value) = mBadgeView.refreshWithInvalidate {
            setBubbleTextSize(value)
        }
        get() = mBadgeView.mTextSize

    /**
     * Set icon margin.
     *
     * @since 0.5.3
     */
    @JvmOverloads
    fun setIconPadding(
        @FloatRange(from = 0.0) top: Float = 0f,
        @FloatRange(from = 0.0) start: Float = 0f,
        @FloatRange(from = 0.0) end: Float = 0f,
        @FloatRange(from = 0.0) bottom: Float = 0f
    ) {
        mIconPadding = mIconPadding.copy(top, start, end, bottom)
    }

    /**
     * @see BadgeView.hideDot
     * @since 0.5.3
     */
    fun hideDot() {
        mBadgeView.refreshWithInvalidate {
            hideDot()
        }
    }

    /**
     * @see BadgeView.showDot
     * @since 0.5.3
     */
    fun showDot() {
        mBadgeView.refreshWithInvalidate {
            showDot()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        val widthMeasureMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMeasureMode = MeasureSpec.getMode(heightMeasureSpec)
        var height = 0
        var width = 0
        for (child in children) {
            measureChild(child, widthMeasureSpec, heightMeasureSpec)
            height += child.measuredHeight
            width = width.coerceAtLeast(child.measuredWidth)
        }
        setMeasuredDimension(
            if (widthMeasureMode == MeasureSpec.EXACTLY) widthSize else width,
            if (heightMeasureMode == MeasureSpec.EXACTLY) heightSize else height
        )
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        when (childCount) {
            1 -> nothing_to_do()
            2 -> if (getChildAt(0) !is TextView && getChildAt(1) !is TextView) {
                throw IllegalArgumentException("BadgeView can only contain one child element of type TextView.")
            }

            else -> throw IllegalArgumentException("BadgeView can only contain one child element of type TextView.")
        }
        var childTop = 0
        var childHeight: Int
        var childWidth: Int
        for (child in children) {
            childHeight = child.measuredHeight
            childWidth = child.measuredWidth
            val childLeft = (width - childWidth) / 2
            child.layout(childLeft, childTop, childLeft + childWidth, childTop + childHeight)
            childTop += childHeight
        }
    }

    init {
        inflate(context, R.layout.badge_layout, this)
        val typeArray = context.obtainStyledAttributes(
            attrs, R.styleable.BadgeLayout, defStyleAttr, defStyleRes
        )
        mMode = when (typeArray.getInt(
            R.styleable.BadgeLayout_badge_mode,
            BadgeMode.UNSPECIFIED.code
        )) {
            BadgeMode.DOT.code -> BadgeMode.DOT
            BadgeMode.BUBBLE.TEXT.code -> BadgeMode.BUBBLE.TEXT
            BadgeMode.BUBBLE.NUMBER.code -> BadgeMode.BUBBLE.NUMBER
            else -> BadgeMode.UNSPECIFIED
        }
        mColor = typeArray.getColor(
            R.styleable.BadgeView_badge_color,
            ContextCompat.getColor(context, R.color.md_theme_error)
        )
        mDotRadius =
            typeArray.getDimension(R.styleable.BadgeLayout_dot_radius, BadgeView.initDotRadius)
        mBubbleRadius =
            typeArray.getDimension(
                R.styleable.BadgeLayout_bubble_radius,
                BadgeView.initBubbleRadius
            )
        typeArray.getString(R.styleable.BadgeLayout_bubble_text)?.also { mBubbleText = it }
        mBubbleTextMaxNum =
            typeArray.getInteger(
                R.styleable.BadgeView_bubble_text_max_num,
                BadgeView.INIT_MAX_NUMBER
            )
        mBubbleTextNum =
            typeArray.getInteger(R.styleable.BadgeView_bubble_text_max_num, BadgeView.INIT_NUMBER)
        mBubbleTextColor = typeArray.getColor(
            R.styleable.BadgeLayout_bubble_text_color,
            ContextCompat.getColor(context, R.color.white)
        )
        mBubbleTextSize =
            typeArray.getDimension(R.styleable.BadgeLayout_bubble_text_size, BadgeView.initTextSize)
        mIcon =
            typeArray.getResourceId(R.styleable.BadgeLayout_icon, R.drawable.ic_badge_default_icon)
        mIconPadding = Quadruple(
            typeArray.getDimension(R.styleable.BadgeLayout_icon_top_margin, 5F.DP),
            typeArray.getDimension(R.styleable.BadgeLayout_icon_start_margin, 5F.DP),
            typeArray.getDimension(R.styleable.BadgeLayout_icon_end_margin, 5F.DP),
            typeArray.getDimension(R.styleable.BadgeLayout_icon_bottom_margin, 5F.DP)
        )
        typeArray.recycle()
        mBinding.badgeLayoutIcon.apply {
            setImageResource(mIcon)
            setPadding(
                mIconPadding.param2.toInt(),
                mIconPadding.param1.toInt(),
                mIconPadding.param3.toInt(),
                mIconPadding.param4.toInt()
            )
        }
        clipChildren = false
    }

}