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
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.annotation.FloatRange
import androidx.annotation.IntDef
import androidx.annotation.StyleRes
import androidx.core.view.children
import com.ave.vastgui.core.extension.cast
import com.ave.vastgui.tools.R
import androidx.core.content.withStyledAttributes
import com.ave.vastgui.tools.utils.color
import com.ave.vastgui.tools.utils.dimension
import com.ave.vastgui.tools.view.avatar.Avatar.Companion.SHAPE_CIRCLE
import com.ave.vastgui.tools.view.avatar.Avatar.Companion.SHAPE_ROUND_CORNER

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/9/26
// Documentation: https://sakurajimamaii.github.io/AVE-DOC/documents/tools/core-topics/ui/avatar/avatargroup/

/**
 * AvatarGroup.
 *
 * @since 0.5.4
 */
class AvatarGroup @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.Default_AvatarGroup_Style,
    @StyleRes defStyleRes: Int = R.style.BaseAvatarGroup
) : ViewGroup(context, attrs, defStyleAttr, defStyleRes) {

    @IntDef(flag = true, value = [START, END])
    @Retention(AnnotationRetention.SOURCE)
    annotation class OverlapFrom

    /** @since 1.5.2 */
    @Suppress("PrivatePropertyName")
    private val DEFAULT_AVATAR_SIZE = dimension(R.dimen.default_avatar_size)

    /** @since 1.5.2 */
    @Suppress("PrivatePropertyName")
    private val DEFAULT_AVATAR_OVERLAP_DISTANCE = dimension(R.dimen.default_avatar_overlap_distance)

    /** @since 1.5.2 */
    @Suppress("PrivatePropertyName")
    private val DEFAULT_TEXT_SIZE = dimension(R.dimen.default_avatar_text_size)

    /** @since 1.5.2 */
    @Suppress("PrivatePropertyName")
    private val DEFAULT_STROKE_WIDTH = dimension(R.dimen.default_avatar_stroke_width)

    /** @since 1.5.2 */
    @Suppress("PrivatePropertyName")
    private val DEFAULT_CORNER_RADIUS = dimension(R.dimen.default_avatar_corner_radius)

    /**
     * The avatar overlay method.
     *
     * @since 1.5.2
     */
    @get:OverlapFrom
    var overlapFrom: Int = START
        private set

    /**
     * The length of the overlap between two avatars. The range is `[0,
     * avatarSize]`.
     *
     * @since 1.5.2
     */
    var overlapDistance: Float = DEFAULT_AVATAR_OVERLAP_DISTANCE
        private set

    /**
     * The Shape of avatar. Current support [SHAPE_CIRCLE] and
     * [SHAPE_ROUND_CORNER].
     *
     * @since 1.5.2
     */
    @get:Avatar.Shape
    var shape: Int = SHAPE_CIRCLE
        private set

    /**
     * The avatar size of avatar.
     *
     * @since 1.5.2
     */
    var size: Float = DEFAULT_AVATAR_SIZE
        private set

    /**
     * The text size shown when the avatar image is null.
     *
     * @since 1.5.2
     */
    var textSize: Float = DEFAULT_TEXT_SIZE
        set(value) {
            field = value.coerceAtLeast(0f)
            children.forEach { (it as Avatar).srcTextSize = field }
        }

    /**
     * The corner radius of avatar when the shape is [SHAPE_ROUND_CORNER].
     *
     * @since 1.5.2
     */
    var cornerRadius: Float = DEFAULT_CORNER_RADIUS
        set(value) {
            field = value.coerceAtLeast(0f)
            children.forEach { (it as Avatar).cornerRadius = field }
        }

    /** @since 1.5.2 */
    private var _strokeWidth: Float = DEFAULT_STROKE_WIDTH

    /**
     * The stroke width of avatar.
     *
     * @since 1.5.2
     */
    var strokeWidth: Float
        get() = _strokeWidth
        set(value) {
            _strokeWidth = value.coerceAtLeast(0f)
            requestLayout()
        }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        var childWidth = 0
        var childHeight = 0
        children.forEach pointer@{ view ->
            try {
                val avatar = cast<Avatar>(view)
                avatar.size = size
                avatar.setShape(shape)
                avatar.srcTextSize = textSize
                avatar.strokeWidth = _strokeWidth
                avatar.cornerRadius = cornerRadius
            } catch (ex: Exception) {
                ex.printStackTrace()
                removeView(view)
                return@pointer
            }
            measureChild(view, widthMeasureSpec, heightMeasureSpec)
            childWidth = childWidth.coerceAtLeast(view.measuredWidth)
            childHeight = childHeight.coerceAtLeast(view.measuredHeight)
        }
        val width = resolveSize(
            childCount * childWidth - (childCount - 1) * overlapDistance.toInt()
                    + paddingStart + paddingEnd,
            widthMeasureSpec
        )
        val height = resolveSize(childHeight + paddingTop + paddingBottom, heightMeasureSpec)
        setMeasuredDimension(width, height)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var left = paddingStart
        children.forEachIndexed { index, child ->
            val avatar = cast<Avatar>(child)
            avatar.layout(left, paddingTop, left + avatar.measuredWidth, paddingTop + avatar.measuredHeight)
            left += avatar.measuredWidth - overlapDistance.toInt()
            when (overlapFrom) {
                START -> avatar.z = (childCount - index).toFloat()
                END -> avatar.z = index.toFloat()
            }
            avatar.setBackgroundColor(color(R.color.transparent))
        }
    }

    /** @since 1.5.2 */
    override fun addView(child: View?) {
        if (child == null) return
        check(child is Avatar) { "AvatarGroup only supports Avatar as children." }
        super.addView(child)
        child.syncWithGroup()
    }

    /** @since 1.5.2 */
    override fun addView(child: View?, params: LayoutParams?) {
        if (child == null) return
        check(child is Avatar) { "AvatarGroup only supports Avatar as children." }
        super.addView(child, params)
        child.syncWithGroup()
    }

    /** @since 1.5.2 */
    override fun addView(child: View?, index: Int) {
        if (child == null) return
        check(child is Avatar) { "AvatarGroup only supports Avatar as children." }
        super.addView(child, index)
        child.syncWithGroup()
    }

    /** @since 1.5.2 */
    override fun addView(child: View?, index: Int, params: LayoutParams?) {
        if (child == null) return
        check(child is Avatar) { "AvatarGroup only supports Avatar as children." }
        super.addView(child, index, params)
        child.syncWithGroup()
    }

    /** @since 1.5.2 */
    override fun addView(child: View?, width: Int, height: Int) {
        if (child == null) return
        check(child is Avatar) { "AvatarGroup only supports Avatar as children." }
        super.addView(child, width, height)
        child.syncWithGroup()
    }

    /**
     * Set [overlapFrom].
     *
     * @since 0.5.4
     */
    fun setOverlapFrom(@OverlapFrom overlapFrom: Int) {
        check(overlapFrom == START || overlapFrom == END) {
            "overlapFrom(current=$overlapFrom) should be one of two values: START($START) or END($END)"
        }
        if (this.overlapFrom == overlapFrom) return
        this.overlapFrom = overlapFrom
        requestLayout()
    }

    /**
     * Set the length of the overlap between two avatars.
     *
     * @since 0.5.4
     */
    fun setOverlapDistance(@FloatRange(from = 0.0) distance: Float) {
        check(distance in 0f..size) {
            "overlapDistance(current=$distance) should in the range of [0, ${size}]"
        }
        if (overlapDistance == distance) return
        overlapDistance = distance
        requestLayout()
    }

    /**
     * Set size for each avatar.
     *
     * @since 1.5.2
     */
    fun setSize(@FloatRange(from = 0.0) size: Float) {
        this@AvatarGroup.size = size.coerceAtLeast(0f)
        requestLayout()
    }

    /**
     * Set [shape].
     *
     * @since 0.5.4
     */
    fun setShape(@Avatar.Shape shape: Int) {
        check(shape == SHAPE_CIRCLE || shape == SHAPE_ROUND_CORNER) { "shape(current=$shape) should be one of two values: SHAPE_CIRCLE($SHAPE_CIRCLE) or SHAPE_ROUND_CORNER($SHAPE_ROUND_CORNER)" }
        if (this.shape == shape) return
        this@AvatarGroup.shape = shape
        children.forEach { (it as Avatar).setShape(this@AvatarGroup.shape) }
    }

    /** @since 1.5.2 */
    private fun Avatar.syncWithGroup() {
        size = size
        setShape(shape)
        srcTextSize = textSize
        strokeWidth = _strokeWidth
        cornerRadius = cornerRadius
    }

    init {
        context.withStyledAttributes(attrs, R.styleable.AvatarGroup, defStyleAttr, defStyleRes) {
            overlapFrom = getInt(R.styleable.AvatarGroup_avatar_overlap_from, START)
            overlapDistance = getDimension(R.styleable.AvatarGroup_avatar_overlap_distance, DEFAULT_AVATAR_OVERLAP_DISTANCE)
            shape = getInt(R.styleable.AvatarGroup_avatar_shape, SHAPE_CIRCLE)
            size = getDimension(R.styleable.AvatarGroup_avatar_size, DEFAULT_AVATAR_SIZE)
            textSize = getDimension(R.styleable.AvatarGroup_avatar_text_size, DEFAULT_TEXT_SIZE)
            cornerRadius = getDimension(R.styleable.AvatarGroup_avatar_corner_radius, DEFAULT_CORNER_RADIUS)
            _strokeWidth = getDimension(R.styleable.AvatarGroup_avatar_stroke_width, DEFAULT_STROKE_WIDTH)
        }
    }

    companion object {
        const val START = 0
        const val END = 1
    }

}