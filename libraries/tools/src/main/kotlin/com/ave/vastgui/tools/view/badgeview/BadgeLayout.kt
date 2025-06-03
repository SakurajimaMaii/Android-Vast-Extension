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

package com.ave.vastgui.tools.view.badgeview

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.ColorInt
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
import androidx.core.content.withStyledAttributes
import com.ave.vastgui.tools.utils.color

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/9/11
// Documentation: https://sakurajimamaii.github.io/AVE-DOC/documents/tools/core-topics/ui/badge/description/

/**
 * Badge Layout.
 *
 * @property iconResId The icon resources id.
 * @property iconPadding The padding of the [iconResId].
 * @since 0.5.3
 */
class BadgeLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.Default_BadgeLayout_Style,
    @StyleRes defStyleRes: Int = R.style.BaseBadgeLayout
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

    private val binding by viewBinding(BadgeLayoutBinding::bind)

    private val badgeView
        get() = binding.badgeLayoutBadge

    /** @since 1.5.2 */
    private var iconResId by NotNUllVar<Int>()

    /** @since 1.5.2 */
    private var iconPadding by NotNUllVar<Quadruple<Float, Float, Float, Float>>()

    /**
     * @see BadgeView.badgeState
     * @since 1.5.2
     */
    val badgeState
        get() = badgeView.badgeState

    /**
     * @see BadgeView.setMode
     * @since 1.5.2
     */
    var badgeMode: BadgeMode
        set(value) {
            badgeView.badgeMode = value
        }
        get() = badgeView.badgeMode

    /**
     * @see BadgeView.setColor
     * @since 1.5.2
     */
    @get:ColorInt
    @setparam:ColorInt
    var badgeColor: Int
        set(value) {
            badgeView.setColor(value)
        }
        get() = badgeView.badgeColor

    /**
     * @see BadgeView.setDotRadius
     * @since 0.5.3
     */
    var dotRadius: Float
        set(value) = badgeView.setDotRadius(value)
        get() = badgeView.bubbleRadius

    /**
     * @see BadgeView.setBubbleRadius
     * @since 0.5.3
     */
    var bubbleRadius: Float
        set(value) = badgeView.refreshWithInvalidate {
            setBubbleRadius(value)
        }
        get() = badgeView.bubbleRadius

    /**
     * @see BadgeView.setBubbleText
     * @since 1.5.2
     */
    var bubbleText: String
        set(value) {
            if (value.isBlank()) return
            badgeView.setBubbleText(value)
        }
        get() = badgeView.text

    /**
     * @throws IllegalArgumentException The given value should be greater
     * than 0.
     * @see BadgeView.setBubbleTextNum
     * @since 1.5.2
     */
    var bubbleTextNum: Int
        set(value) {
            check(value >= 0) { "The value should be greater than 0." }
            badgeView.setBubbleTextNum(value)
        }
        get() = badgeView.textNumber

    /**
     * @throws IllegalArgumentException The given value should be greater
     * than 0.
     * @see BadgeView.setBubbleTextMaxNum
     * @since 1.5.2
     */
    var bubbleTextMaxNum: Int
        set(value) {
            check(value >= 0) { "The value should be greater than 0." }
            badgeView.setBubbleTextMaxNum(value)
        }
        get() = badgeView.textMaxNumber

    /**
     * @see BadgeView.setBubbleTextColor
     * @since 1.5.2
     */
    @get:ColorInt
    @setparam:ColorInt
    var bubbleTextColor: Int
        set(value) {
            badgeView.setBubbleTextColor(value)
        }
        get() = badgeView.textColor

    /**
     * @see BadgeView.setBubbleTextSize
     * @since 1.5.2
     */
    var bubbleTextSize: Float
        set(value) {
            badgeView.setBubbleTextSize(value)
        }
        get() = badgeView.textSize

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
        iconPadding = iconPadding.copy(top, start, end, bottom)
    }

    /**
     * @see BadgeView.hideDot
     * @since 0.5.3
     */
    fun hideDot() {
        badgeView.refreshWithInvalidate {
            hideDot()
        }
    }

    /**
     * @see BadgeView.showDot
     * @since 0.5.3
     */
    fun showDot() {
        badgeView.refreshWithInvalidate {
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
        context.withStyledAttributes(attrs, R.styleable.BadgeLayout, defStyleAttr, defStyleRes) {
            badgeMode = when (getInt(R.styleable.BadgeLayout_badge_mode, BadgeMode.Unspecified.code)) {
                BadgeMode.Dot.code -> BadgeMode.Dot
                BadgeMode.Bubble.Text.code -> BadgeMode.Bubble.Text
                BadgeMode.Bubble.Number.code -> BadgeMode.Bubble.Number
                else -> BadgeMode.Unspecified
            }
            badgeView.badgePaint.color = getColor(R.styleable.BadgeView_badge_color, color(R.color.md_theme_error))
            dotRadius = getDimension(R.styleable.BadgeLayout_dot_radius, BadgeView.DEFAULT_DOT_RADIUS)
            bubbleRadius = getDimension(R.styleable.BadgeLayout_bubble_radius, BadgeView.DEFAULT_BUBBLE_RADIUS)
            bubbleText = getString(R.styleable.BadgeLayout_bubble_text) ?: ""
            bubbleTextMaxNum = getInteger(R.styleable.BadgeView_bubble_text_max_num, BadgeView.INIT_MAX_NUMBER)
            bubbleTextNum = getInteger(R.styleable.BadgeView_bubble_text_max_num, BadgeView.INIT_NUMBER)
            bubbleTextColor = getColor(R.styleable.BadgeLayout_bubble_text_color, ContextCompat.getColor(context, R.color.white))
            bubbleTextSize = getDimension(R.styleable.BadgeLayout_bubble_text_size, BadgeView.DEFAULT_TEXT_SIZE)
            iconResId = getResourceId(R.styleable.BadgeLayout_icon, R.drawable.ic_badge_default_icon)
            iconPadding = Quadruple(
                getDimension(R.styleable.BadgeLayout_icon_top_margin, 5F.DP),
                getDimension(R.styleable.BadgeLayout_icon_start_margin, 5F.DP),
                getDimension(R.styleable.BadgeLayout_icon_end_margin, 5F.DP),
                getDimension(R.styleable.BadgeLayout_icon_bottom_margin, 5F.DP)
            )
        }
        binding.badgeLayoutIcon.apply {
            setImageResource(iconResId)
            setPadding(iconPadding.param2.toInt(), iconPadding.param1.toInt(), iconPadding.param3.toInt(), iconPadding.param4.toInt())
        }
        clipChildren = false
    }

}