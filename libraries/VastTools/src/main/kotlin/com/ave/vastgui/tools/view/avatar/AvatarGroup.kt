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

package com.ave.vastgui.tools.view.avatar

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import androidx.annotation.FloatRange
import androidx.annotation.IntDef
import androidx.annotation.StyleRes
import androidx.core.view.children
import com.ave.vastgui.core.extension.cast
import com.ave.vastgui.tools.R

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/9/26
// Documentation: https://ave.entropy2020.cn/documents/VastTools/core-topics/ui/avatar/avatargroup/

/**
 * AvatarGroup.
 *
 * @property mOverlapFrom The avatar overlay method.
 * @property mOverlapDistance The length of the overlap between two
 *     avatars.
 * @property mShape The Shape of avatar. Current support circle and round
 *     rectangle.
 * @property mAvatarSize The avatar size of avatar.
 * @property mTextSize The text size shown when the avatar image is null.
 * @property mStrokeWidth The stroke width of avatar.
 * @property mCornerRadius The corner radius of avatar when the shape is
 *     SHAPE_ROUND_CORNER.
 * @since 0.5.4
 */
class AvatarGroup @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.Default_AvatarGroup_Style,
    @StyleRes defStyleRes: Int = R.style.BaseAvatarGroup
) : ViewGroup(context, attrs) {

    companion object {
        const val START = 0
        const val END = 1
    }

    @IntDef(flag = true, value = [START, END])
    @Retention(AnnotationRetention.SOURCE)
    annotation class OverlapFrom

    private val mDefaultAvatarSize =
        resources.getDimension(R.dimen.default_avatar_size)
    private val mDefaultAvatarOverlapDistance =
        resources.getDimension(R.dimen.default_avatar_overlap_distance)
    private val mDefaultTextSize =
        resources.getDimension(R.dimen.default_avatar_text_size)
    private val mDefaultStrokeWidth =
        resources.getDimension(R.dimen.default_avatar_stroke_width)
    private val mDefaultCornerRadius =
        resources.getDimension(R.dimen.default_avatar_corner_radius)

    @get:OverlapFrom
    var mOverlapFrom: Int = START
        private set

    var mOverlapDistance: Float = mDefaultAvatarOverlapDistance
        private set

    @get:Avatar.SHAPE
    var mShape: Int = Avatar.SHAPE_CIRCLE
        private set

    var mAvatarSize: Float = mDefaultAvatarSize
        private set

    var mTextSize: Float = mDefaultTextSize

    var mStrokeWidth: Float = mDefaultStrokeWidth

    var mCornerRadius: Float = mDefaultCornerRadius

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        var childWidth = 0
        var childHeight = 0
        children.forEach pointer@{ view ->
            try {
                cast<Avatar>(view).apply {
                    mAvatarSize = this@AvatarGroup.mAvatarSize
                    setShape(this@AvatarGroup.mShape)
                    mTextSize = this@AvatarGroup.mTextSize
                    mStrokeWidth = this@AvatarGroup.mStrokeWidth
                    mCornerRadius = this@AvatarGroup.mCornerRadius
                    mAvatar?.let { avatar -> setAvatar(avatar) }
                }
            } catch (exception: Exception) {
                removeView(view)
                return@pointer
            }
            measureChild(view, widthMeasureSpec, heightMeasureSpec)
            childWidth = childWidth.coerceAtLeast(view.measuredWidth)
            childHeight = childHeight.coerceAtLeast(view.measuredHeight)
        }
        val width = resolveSize(
            childCount * childWidth - (childCount - 1) * mOverlapDistance.toInt()
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
            avatar.layout(
                left,
                paddingTop,
                left + avatar.measuredWidth,
                paddingTop + avatar.measuredHeight
            )
            left += avatar.measuredWidth - mOverlapDistance.toInt()
            when (mOverlapFrom) {
                START -> avatar.z = (childCount - index).toFloat()
                END -> avatar.z = index.toFloat()
            }
            avatar.setBackgroundColor(context.getColor(R.color.transparent))
        }
    }

    /**
     * Set [mOverlapFrom].
     *
     * @since 0.5.4
     */
    fun setOverlapFrom(@OverlapFrom overlapFrom: Int) {
        mOverlapFrom = overlapFrom
    }

    /**
     * Set the length of the overlap between two avatars.
     *
     * @since 0.5.4
     */
    fun setOverlapDistance(@FloatRange(from = 0.0) distance: Float) {
        mOverlapDistance = distance.coerceAtMost(mAvatarSize / 2f)
    }

    /**
     * Set size for each avatar. At the same time, [mOverlapDistance] will be
     * adjusted to ensure that its length is reasonable.
     *
     * @since 0.5.4
     */
    fun setAvatarSize(@FloatRange(from = 0.0) size: Float) {
        mAvatarSize = size
        mOverlapDistance = mOverlapDistance.coerceAtMost(mAvatarSize / 2f)
    }

    /**
     * Set mode.
     *
     * @since 0.5.4
     */
    fun setShape(@Avatar.SHAPE shape: Int) {
        mShape = shape
    }

    init {
        val typeArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.AvatarGroup,
            defStyleAttr,
            defStyleRes
        )
        mAvatarSize =
            typeArray.getDimension(R.styleable.AvatarGroup_avatar_size, mDefaultAvatarSize)
        mShape =
            typeArray.getInt(R.styleable.AvatarGroup_avatar_shape, Avatar.SHAPE_CIRCLE)
        mTextSize =
            typeArray.getDimension(R.styleable.AvatarGroup_avatar_text_size, mDefaultTextSize)
        mStrokeWidth =
            typeArray.getDimension(R.styleable.AvatarGroup_avatar_stroke_width, mDefaultStrokeWidth)
        mCornerRadius =
            typeArray.getDimension(R.styleable.AvatarGroup_avatar_corner_radius, mDefaultCornerRadius)
        mOverlapFrom =
            typeArray.getInt(R.styleable.AvatarGroup_avatar_overlap_from, START)
        mOverlapDistance =
            typeArray.getDimension(
                R.styleable.AvatarGroup_avatar_overlap_distance,
                mDefaultAvatarOverlapDistance
            )
        typeArray.recycle()
    }

}