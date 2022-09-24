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

package cn.govast.vasttools.utils

import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.GradientDrawable.*
import androidx.annotation.ColorInt
import androidx.annotation.FloatRange
import androidx.annotation.IntDef
import androidx.annotation.IntRange

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/3/10 15:27
// Description: Help you to quickly build GradientDrawable.
// Documentation: [ShapeAndStateUtils](https://sakurajimamaii.github.io/VastDocs/document/en/ShapeAndStateUtils.html)

/**
 * ShapeAndStateUtils
 *
 * Here is an example:
 * ```kotlin
 * // Use ShapeAndStateUtils to create a background drawable
 * val btnbk1 = ShapeAndStateUtils
 *                   .create()
 *                   .setShape(RECTANGLE)
 *                   .setRadius(50f)
 *                   .setGradient(45, colorHex2Int("#12c2e9"),colorHex2Int("#c471ed"),colorHex2Int("#f64f59"))
 *                   .build()
 * ```
 *
 * @since 0.0.5
 */
class ShapeAndStateUtils private constructor(){

    /**
     * The gradient type [ShapeAndStateUtils] supports.
     *
     * @since 0.0.5
     */
    @Retention(AnnotationRetention.SOURCE)
    @IntDef(
        LINEAR_GRADIENT,
        RADIAL_GRADIENT,
        SWEEP_GRADIENT
    )
    annotation class GradientType

    /**
     * The shape [ShapeAndStateUtils] supports.
     *
     * @since 0.0.5
     */
    @Retention(AnnotationRetention.SOURCE)
    @IntDef(RECTANGLE, OVAL, LINE, RING)
    annotation class DrawableShape

    /**
     * Gradient type.
     *
     * @since 0.0.5
     */
    @setparam:GradientType
    var gradientType: Int = LINEAR_GRADIENT
        private set

    /**
     * Gradient orientation,please check [GradientDrawable.Orientation].
     *
     * @since 0.0.5
     */
    var gradientOrientation: Orientation = Orientation.TOP_BOTTOM
        private set

    /**
     * GradientDrawable Colors。
     *
     * @since 0.0.5
     */
    var colors: IntArray = intArrayOf()
        private set

    /**
     * Shape of GradientDrawable.
     *
     * @since 0.0.5
     */
    @setparam:DrawableShape
    var shape: Int = RECTANGLE
        private set

    /**
     * Gradient radius
     *
     * When [gradientType] is set to [GradientDrawable.RADIAL_GRADIENT],you should set
     * [gradientRadius].Otherwise, **0** will be used as the default value。
     *
     * @since 0.0.5
     */
    var gradientRadius: Float = 0f
        private set

    /**
     * Specifies the radius for the corners of the gradient. If this is > 0,
     * then the drawable is drawn in a round-rectangle, rather than a
     * rectangle. This property is honored only when the shape is of type
     * [GradientDrawable.RECTANGLE].
     *
     * @since 0.0.5
     */
    var radius: Float = 0f
        private set

    /**
     * Specifies radii for each of the 4 corners.
     *
     * @since 0.0.5
     */
    var radii: FloatArray = floatArrayOf(0f,0f,0f,0f,0f,0f,0f,0f)
        private set

    /**
     * Left top corner radius(in pixels).
     *
     * @since 0.0.5
     */
    var leftTopCornerRadius: Float = 0f
        private set
        get() = radii[0]

    /**
     * Left bottom corner radius(in pixels).
     *
     * @since 0.0.5
     */
    var leftBottomCornerRadius: Float = 0f
        private set
        get() = radii[6]

    /**
     * Right top corner radius(in pixels).
     *
     * @since 0.0.5
     */
    var rightTopCornerRadius: Float = 0f
        private set
        get() = radii[2]

    /**
     * Right bottom corner radius(in pixels).
     *
     * @since 0.0.5
     */
    var rightBottomCornerRadius: Float = 0f
        private set
        get() = radii[4]

    /**
     * Stroke width(in pixels).
     *
     * @since 0.0.5
     */
    var strokeWidth: Float = 0f
        private set

    /**
     * Stroke color(in 0xAARRGGBB)
     *
     * @since 0.0.5
     */
    var strokeColor: Int = 0x00000000
        private set

    /**
     * Dash width of stroke(in pixels).
     *
     * @since 0.0.5
     */
    var dashWidth: Float = 0f
        private set

    /**
     * Dash gap of stroke(in pixels).
     *
     * @since 0.0.5
     */
    var dashGap: Float = 0f
        private set

    /**
     * Used to define background colors in different states.
     *
     * @since 0.0.5
     */
    var bgColorStateList:ColorStateList? = null
        private set

    /**
     * Used to define stroke colors in different states.
     *
     * @since 0.0.5
     */
    var strokeColorStateList:ColorStateList? = null
        private set

    /**
     * Density.
     *
     * @since 0.0.5
     */
    private val density = Resources.getSystem().displayMetrics.density

    /**
     * Set gradient type of GradientDrawable.
     *
     * @param type You can choose from [GradientDrawable.LINEAR_GRADIENT],[GradientDrawable.SWEEP_GRADIENT],[GradientDrawable.RADIAL_GRADIENT]
     * @return [ShapeAndStateUtils] object.
     *
     * @since 0.0.5
     */
    fun setGradientType(@GradientType type: Int): ShapeAndStateUtils {
        gradientType = type
        return this
    }

    /**
     * Set background color of GradientDrawable.
     *
     * @param color color int value.
     * @return [ShapeAndStateUtils] object.
     *
     * @since 0.0.5
     */
    fun setBkColor(@ColorInt color: Int):ShapeAndStateUtils{
        return setGradient(0,color,color)
    }

    /**
     * Set gradient color and gradient angle.
     *
     * @param angle Must be an integer multiple of 45.
     * @param startColor Start gradient color.
     * @param endColor End gradient color.
     * @return [ShapeAndStateUtils] object.
     *
     * @since 0.0.5
     */
    fun setGradient(
        angle: Int,
        @ColorInt startColor: Int,
        @ColorInt endColor: Int
    ): ShapeAndStateUtils {
        val orientation = angle2Orientation(angle)
        return setGradient(orientation, startColor, endColor)
    }

    /**
     * Set gradient color and gradient orientation.
     *
     * @param orientation Gradient Orientation.
     * @param startColor Start gradient color.
     * @param endColor End gradient color.
     * @return [ShapeAndStateUtils] object.
     *
     * @since 0.0.5
     */
    fun setGradient(
        orientation: Orientation,
        @ColorInt startColor: Int,
        @ColorInt endColor: Int
    ): ShapeAndStateUtils {
        gradientOrientation = orientation
        colors = intArrayOf(startColor, endColor)
        return this
    }

    /**
     * Set gradient color and gradient angle.
     *
     * @param angle Must be an integer multiple of 45.
     * @param startColor Start gradient color.
     * @param centerColor Center gradient color.
     * @param endColor End gradient color.
     * @return [ShapeAndStateUtils] object.
     *
     * @since 0.0.5
     */
    fun setGradient(
        angle: Int,
        @ColorInt startColor: Int,
        @ColorInt centerColor: Int,
        @ColorInt endColor: Int
    ): ShapeAndStateUtils {
        val orientation = angle2Orientation(angle)
        return setGradient(orientation, startColor, centerColor, endColor)
    }

    /**
     * Set gradient color and gradient orientation.
     *
     * @param orientation Gradient Orientation.
     * @param startColor Start gradient color.
     * @param centerColor Center gradient color.
     * @param endColor End gradient color.
     * @return [ShapeAndStateUtils] object.
     *
     * @since 0.0.5
     */
    fun setGradient(
        orientation: Orientation,
        @ColorInt startColor: Int,
        @ColorInt centerColor: Int,
        @ColorInt endColor: Int
    ): ShapeAndStateUtils {
        gradientOrientation = orientation
        colors = intArrayOf(startColor, centerColor, endColor)
        return this
    }

    /**
     * Sets the type of shape used to draw the gradient.
     *
     * @param shape You can choose from [GradientDrawable.OVAL],[GradientDrawable.LINE],[GradientDrawable.RECTANGLE],[GradientDrawable.RING].
     * @return [ShapeAndStateUtils] object.
     *
     * @since 0.0.5
     */
    fun setShape(@DrawableShape shape: Int): ShapeAndStateUtils {
        this.shape = shape
        return this
    }

    /**
     * Set gradient radius when [gradientType] is [GradientDrawable.RADIAL_GRADIENT].
     *
     * @param radius the radius of the gradient.
     * @return [ShapeAndStateUtils] object.
     *
     * @since 0.0.5
     */
    fun setGradientRadius(@FloatRange(from = 0.0) radius: Float): ShapeAndStateUtils {
        gradientRadius = radius
        return this
    }

    /**
     * Set specifies radii for corners.
     *
     * @param radius Must be >= 0
     * @return [ShapeAndStateUtils] object.
     *
     * @since 0.0.5
     */
    fun setRadius(@FloatRange(from = 0.0) radius: Float): ShapeAndStateUtils {
        this.radius = radius
        return setRadius(radius, radius, radius, radius)
    }

    /**
     * Set specifies radii for each of the 4 corners.
     *
     * @param topLeft radius of top left corner.
     * @param topRight radius of top right corner.
     * @param bottomLeft radius of bottom left corner.
     * @param bottomRight radius of bottom right corner.
     * @return [ShapeAndStateUtils] object.
     *
     * @since 0.0.5
     */
    fun setRadius(
        @FloatRange(from = 0.0) topLeft: Float,
        @FloatRange(from = 0.0) topRight: Float,
        @FloatRange(from = 0.0) bottomLeft: Float,
        @FloatRange(from = 0.0) bottomRight: Float
    ): ShapeAndStateUtils {
        radii = floatArrayOf(
            topLeft,
            topLeft,
            topRight,
            topRight,
            bottomRight,
            bottomRight,
            bottomLeft,
            bottomLeft
        )
        return this
    }

    /**
     * Set stroke.
     *
     * @param width The width of the stroke.
     * @param color The color of the stroke.
     * @return [ShapeAndStateUtils] object.
     *
     * @since 0.0.5
     */
    fun setStroke(
        @FloatRange(from = 0.0) width: Float,
        @ColorInt color: Int
    ): ShapeAndStateUtils {
        strokeWidth = width
        strokeColor = color
        return this
    }

    /**
     * Set stroke.
     *
     * @param width The width of the stroke.
     * @param color The color of the stroke.
     * @param dashWidth The width of the dash.
     * @param dashGap The width of the dash.
     * @return [ShapeAndStateUtils] object.
     *
     * @since 0.0.5
     */
    fun setStroke(
        @IntRange(from = 0) width: Int,
        @ColorInt color: Int,
        @IntRange(from = 0) dashWidth: Int,
        @IntRange(from = 0) dashGap: Int
    ): ShapeAndStateUtils {
        strokeWidth = width * density
        strokeColor = color
        this.dashWidth = dashWidth * density
        this.dashGap = dashGap * density
        return this
    }

    /**
     * Creates a background ColorStateList that returns the specified mapping from
     * states to colors.
     *
     * @since 0.0.5
     */
    fun setBgColorStateList(state: Array<IntArray?>, colors:IntArray):ShapeAndStateUtils{
        bgColorStateList = ColorStateList(state, colors)
        return this
    }

    /**
     * Creates a stroke ColorStateList that returns the specified mapping from
     * states to colors.
     *
     * @since 0.0.5
     */
    fun setStrokeColorStateList(state: Array<IntArray?>, colors:IntArray):ShapeAndStateUtils{
        strokeColorStateList = ColorStateList(state, colors)
        return this
    }

    /**
     * Angle convert to orientation.
     *
     * @param angle Must be an integer multiple of 45.
     * @return Default value is [GradientDrawable.Orientation.LEFT_RIGHT]。
     *
     * @since 0.0.5
     */
    private fun angle2Orientation(angle: Int): Orientation {
        return when (angle) {
            0 -> Orientation.LEFT_RIGHT
            45 -> Orientation.BL_TR
            90 -> Orientation.BOTTOM_TOP
            135 -> Orientation.BR_TL
            180 -> Orientation.RIGHT_LEFT
            225 -> Orientation.TR_BL
            270 -> Orientation.TOP_BOTTOM
            315 -> Orientation.TL_BR
            else -> Orientation.LEFT_RIGHT
        }
    }

    /**
     * Build a [GradientDrawable] object.
     *
     * @return [GradientDrawable] object.
     *
     * @since 0.0.5
     */
    fun build(): GradientDrawable {
        val drawable = GradientDrawable()
        drawable.apply {
            gradientType = this@ShapeAndStateUtils.gradientType

            shape = this@ShapeAndStateUtils.shape

            gradientRadius = this@ShapeAndStateUtils.gradientRadius

            orientation = gradientOrientation

            if(null == bgColorStateList){
                colors = this@ShapeAndStateUtils.colors
            }else{
                color = bgColorStateList
            }

            cornerRadii = radii

            if(null == strokeColorStateList){
                this.setStroke(strokeWidth.toInt(),strokeColor,dashWidth,dashGap)
            }else{
                this.setStroke(strokeWidth.toInt(),strokeColorStateList,dashWidth,dashGap)
            }

        }
        return drawable
    }

    companion object{
        /**
         * @return [ShapeAndStateUtils] object.
         *
         * @since 0.0.5
         */
        @JvmStatic
        fun create():ShapeAndStateUtils{
            return ShapeAndStateUtils()
        }
    }
}