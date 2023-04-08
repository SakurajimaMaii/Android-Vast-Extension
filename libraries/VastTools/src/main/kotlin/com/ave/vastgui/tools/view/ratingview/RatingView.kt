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

package com.ave.vastgui.tools.view.ratingview

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.DrawableRes
import androidx.annotation.FloatRange
import androidx.annotation.IntRange
import com.ave.vastgui.tools.R
import com.ave.vastgui.tools.utils.BmpUtils
import com.ave.vastgui.tools.view.ratingview.RatingView.Orientation
import com.ave.vastgui.tools.view.ratingview.RatingView.SelectMethod
import kotlin.math.max

// Author: Vast Gui
// Email: sakurajimamai2020@qq.com
// Date: 2021/7/28
// Documentation: https://ave.entropy2020.cn/documents/VastTools/core-topics/ui/ratingview/RatingView/

/**
 * RatingView.
 *
 * ```xml
 * <com.ave.vastgui.tools.view.ratingview.RatingView
 *     android:id="@+id/ratingView"
 *     android:layout_width="wrap_content"
 *     android:layout_height="wrap_content"
 *     app:star_rating="2.5" />
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
 * @property Orientation Star orientation.
 * @property SelectMethod Star select method.
 * @property starSelectedBitmap The bitmap of the selected star.
 * @property starSelectedBitmap The bitmap of the unselected star.
 * @property starIntervalWidth Star interval width(in pixels).
 * @property starWidth Star Bitmap width(in pixels).
 * @property starHeight Star Bitmap height(in pixels).
 * @property starCountNumber Max number of stars.
 * @property starRating The progress of the currently selected stars.
 * @property starSolidNumber The number of currently selected stars.
 */
class RatingView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int = R.attr.Default_RatingView_Style,
    defStyleRes: Int = R.style.BaseRatingView
) : View(context, attrs, defStyleAttr, defStyleRes) {

    enum class Orientation {
        HORIZONTAL, VERTICAL
    }

    enum class SelectMethod {
        Unable, Click, Sliding
    }

    data class StarSize(val width: Int, val height: Int)

    private var starSelectedBitmap: Bitmap
    private var starUnselectedBitmap: Bitmap
    private var starIntervalWidth: Int
    private var starWidth: Int
    private var starHeight: Int
    private var starCountNumber: Int
    private var starRating: Float
    private var starSolidNumber = 0

    private var rectSrc: Rect = Rect()
    private var dstF: Rect = Rect()
    private val paint: Paint = Paint()

    /**
     * Star select method
     *
     * @see [SelectMethod.Click] [SelectMethod.Sliding] [SelectMethod.Unable]
     */
    private var starSelectMethod: SelectMethod

    /**
     * Star orientation
     *
     * @see Orientation
     */
    private val starOrientation: Orientation

    private val density = context.resources.displayMetrics.density

    override fun onDraw(canvas: Canvas) {
        // Draw selected star.
        var solidStartPoint = 0
        when (starOrientation) {
            Orientation.HORIZONTAL -> for (i in 1..starSolidNumber) {
                canvas.drawBitmap(starSelectedBitmap, solidStartPoint.toFloat(), 0f, paint)
                solidStartPoint += starIntervalWidth + starSelectedBitmap.width
            }

            Orientation.VERTICAL -> for (i in 1..starSolidNumber) {
                canvas.drawBitmap(starSelectedBitmap, 0f, solidStartPoint.toFloat(), paint)
                solidStartPoint += starIntervalWidth + starSelectedBitmap.height
            }
        }
        // Unselected Star Bitmap start point.
        var hollowStartPoint = solidStartPoint
        // Unselected Star number.
        val hollowStarNum = starCountNumber - starSolidNumber
        when (starOrientation) {
            Orientation.HORIZONTAL -> for (j in 1..hollowStarNum) {
                canvas.drawBitmap(starUnselectedBitmap, hollowStartPoint.toFloat(), 0f, paint)
                hollowStartPoint += starIntervalWidth + starUnselectedBitmap.width
            }

            Orientation.VERTICAL -> for (j in 1..hollowStarNum) {
                canvas.drawBitmap(starUnselectedBitmap, 0f, hollowStartPoint.toFloat(), paint)
                hollowStartPoint += starIntervalWidth + starUnselectedBitmap.width
            }
        }
        // Extra selected star bitmap Length.
        when (starOrientation) {
            Orientation.HORIZONTAL -> {
                canvas.drawBitmap(starSelectedBitmap, rectSrc, dstF, paint)
            }

            Orientation.VERTICAL -> {
                canvas.drawBitmap(starSelectedBitmap, rectSrc, dstF, paint)
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (starSelectMethod) {
            SelectMethod.Sliding -> {
                if (starOrientation == Orientation.HORIZONTAL) {
                    val totalWidth = (starCountNumber * (starWidth + starIntervalWidth)).toFloat()
                    if (event.x <= totalWidth) {
                        val newStarRating = event.x / (starWidth + starIntervalWidth) + 1
                        setStarRating(max(newStarRating, 1f))
                    }
                } else {
                    val totalHeight = (starCountNumber * (starHeight + starIntervalWidth)).toFloat()
                    if (event.y <= totalHeight) {
                        val newStarRating = event.y / (starHeight + starIntervalWidth) + 1
                        setStarRating(max(newStarRating, 1f))
                    }
                }
                performClick()
                return true
            }

            else -> {
                if (starSelectMethod == SelectMethod.Click) {
                    if (event.action == MotionEvent.ACTION_DOWN) {
                        if (starOrientation == Orientation.HORIZONTAL) {
                            val totalWidth =
                                (starCountNumber * (starWidth + starIntervalWidth)).toFloat()
                            if (event.x <= totalWidth) {
                                val newStarRating =
                                    (event.x.toInt() / (starHeight + starIntervalWidth) + 1).toFloat()
                                setStarRating(newStarRating)
                            }
                        } else {
                            val totalHeight =
                                (starCountNumber * (starHeight + starIntervalWidth)).toFloat()
                            if (event.y <= totalHeight) {
                                val newStarRating =
                                    (event.y.toInt() / (starHeight + starIntervalWidth) + 1).toFloat()
                                setStarRating(newStarRating)
                            }
                        }
                    }
                    performClick()
                }
                return super.onTouchEvent(event)
            }
        }
    }

    override fun performClick(): Boolean {
        super.performClick()
        return true
    }

    /**
     * Set star rating.
     *
     * @param starRating Float
     * @throws IllegalArgumentException
     */
    @Throws(IllegalArgumentException::class)
    fun setStarRating(@FloatRange(from = 1.0) starRating: Float) = apply {
        when {
            starRating.toInt() > starCountNumber -> {
                throw IllegalArgumentException("The number of selected stars must less than $starCountNumber")
            }

            else -> {
                this.starRating = starRating
                starSolidNumber = starRating.toInt()
                val extraSolidLength =
                    ((starRating - starSolidNumber) * starUnselectedBitmap.width).toInt()
                var extraSolidStarPoint = 0
                when (starOrientation) {
                    Orientation.HORIZONTAL -> {
                        for (i in 1..starSolidNumber) {
                            extraSolidStarPoint += starIntervalWidth + starSelectedBitmap.width
                        }
                        rectSrc = Rect(0, 0, extraSolidLength, starUnselectedBitmap.height)
                        dstF = Rect(
                            extraSolidStarPoint,
                            0,
                            extraSolidStarPoint + extraSolidLength,
                            starUnselectedBitmap.height
                        )
                    }

                    Orientation.VERTICAL -> {
                        for (i in 1..starSolidNumber) {
                            extraSolidStarPoint += starIntervalWidth + starSelectedBitmap.height
                        }
                        rectSrc = Rect(0, 0, starUnselectedBitmap.width, extraSolidLength)
                        dstF = Rect(
                            0,
                            extraSolidStarPoint,
                            starUnselectedBitmap.width,
                            extraSolidStarPoint + extraSolidLength
                        )
                    }
                }
                invalidate()
            }
        }
    }

    fun getStarRating() = starRating

    /**
     * Set Star Select Method
     *
     * @param starSelectMethod Int
     */
    fun setStarSelectMethod(starSelectMethod: SelectMethod) = apply {
        this.starSelectMethod = starSelectMethod
    }

    /**
     * Set star bitmap size(in pixels).
     *
     * @param starWidth Int
     * @param starHeight Int
     */
    fun setStarBitMapSize(@IntRange(from = 0) starWidth: Int, @IntRange(from = 0) starHeight: Int) =
        apply {
            this.starWidth = (starWidth * density).toInt()
            this.starHeight = (starHeight * density).toInt()
            starSelectedBitmap = getZoomBitmap(starSelectedBitmap)
            starUnselectedBitmap = getZoomBitmap(starUnselectedBitmap)
            invalidate()
        }

    /**
     * Get star bitmap size(in pixels).
     *
     * @return starSize
     */
    fun getStarBitMapSize() =
        StarSize((starWidth / density).toInt(), (starHeight / density).toInt())

    /** Get star bitmap width(in pixels). */
    fun getStarBitMapWidth() = (starWidth / density).toInt()

    /** Get star bitmap height(in pixels). */
    fun getStarBitMapHeight() = (starWidth / density).toInt()

    /**
     * Set star bitmap interval(in pixels).
     *
     * @param starSpaceWidth Int
     * @throws RatingViewIllegalParamException
     */
    fun setStarIntervalWidth(@IntRange(from = 0) starSpaceWidth: Int) = apply {
        this.starIntervalWidth = (starSpaceWidth * density).toInt()
        invalidate()
    }

    /** Get star bitmap interval(in pixels). */
    fun getStarIntervalWidth() = (starIntervalWidth / density).toInt()

    /** Set the number of star. */
    fun setStarCountNumber(@IntRange(from = 0) starCountNumber: Int) = apply {
        this.starCountNumber = starCountNumber
        invalidate()
    }

    /** Get the number of star. */
    fun getStarMaxNumber() = starCountNumber

    /** Set the bitmap of the be selected star. */
    fun setStarSelectedBitmap(bitmap: Bitmap) = apply {
        starSelectedBitmap = getZoomBitmap(bitmap)
    }

    /**
     * Set the bitmap of the be selected star by drawableId.
     *
     * @param drawableId Int
     */
    @Throws(IllegalArgumentException::class)
    fun setStarSelectedBitmap(@DrawableRes drawableId: Int) = apply {
        starSelectedBitmap =
            getZoomBitmap(BmpUtils.getBitmapFromDrawable(drawableId, context))
    }

    /**
     * Get the bitmap of the be selected star.
     *
     * @return Bitmap
     */
    fun getStarSelectedBitmap() = starSelectedBitmap

    /**
     * Set the bitmap of the be unselected star.
     *
     * @param bitmap Bitmap
     */
    fun setStarUnselectedBitmap(bitmap: Bitmap) = apply {
        starUnselectedBitmap = getZoomBitmap(bitmap)
    }

    /**
     * Set the bitmap of the be unselected star by drawableId.
     *
     * @param drawableId Int
     */
    @Throws(IllegalArgumentException::class)
    fun setStarUnselectedBitmap(@DrawableRes drawableId: Int) = apply {
        starUnselectedBitmap =
            getZoomBitmap(BmpUtils.getBitmapFromDrawable(drawableId, context))
    }

    /**
     * Get the bitmap of the be unselected star.
     *
     * @return Bitmap
     */
    fun getStarUnselectedBitmap() = starUnselectedBitmap

    private fun getZoomBitmap(bitmap: Bitmap): Bitmap {
        if (starWidth == 0 || starHeight == 0) {
            return bitmap
        }
        // Calculate the zoom ratio, the desired size/the original width and height of the image
        val scaleWidth = starWidth.toFloat() / bitmap.width
        val scaleHeight = starHeight.toFloat() / bitmap.height
        // Get the matrix parameters you want to scale
        val matrix = Matrix()
        matrix.postScale(scaleWidth, scaleHeight)
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        when (starOrientation) {
            Orientation.HORIZONTAL -> setMeasuredDimension(
                measureLong(widthMeasureSpec),
                measureShort(heightMeasureSpec)
            )

            Orientation.VERTICAL -> setMeasuredDimension(
                measureShort(widthMeasureSpec),
                measureLong(heightMeasureSpec)
            )
        }
    }

    private fun measureLong(measureSpec: Int): Int {
        var result: Int
        val specMode = MeasureSpec.getMode(measureSpec)
        val specSize = MeasureSpec.getSize(measureSpec)
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize
        } else {
            result =
                (paddingLeft + paddingRight + (starIntervalWidth + starWidth) * starCountNumber)
            if (specMode == MeasureSpec.AT_MOST) {
                result = result.coerceAtMost(specSize)
            }
        }
        return result
    }

    private fun measureShort(measureSpec: Int): Int {
        var result: Int
        val specMode = MeasureSpec.getMode(measureSpec)
        val specSize = MeasureSpec.getSize(measureSpec)
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize
        } else {
            result = (starHeight + paddingTop + paddingBottom)
            if (specMode == MeasureSpec.AT_MOST) {
                result = result.coerceAtMost(specSize)
            }
        }
        return result
    }

    init {
        val a =
            context.obtainStyledAttributes(attrs, R.styleable.RatingView, defStyleAttr, defStyleRes)
        starIntervalWidth = a.getDimensionPixelSize(R.styleable.RatingView_star_interval_width, 0)
        starWidth = a.getDimensionPixelSize(R.styleable.RatingView_star_width, 0)
        starHeight = a.getDimensionPixelSize(R.styleable.RatingView_star_height, 0)
        starCountNumber = a.getInt(R.styleable.RatingView_star_count, 0)
        starRating = a.getFloat(R.styleable.RatingView_star_rating, 0f)
        // This prevents the set resource from failing to parse and returning null, so a default value is given
        val tempSelectedBitmap = BmpUtils.getBitmapFromDrawable(
            a.getResourceId(
                R.styleable.RatingView_star_selected,
                R.drawable.ic_star_default_selected
            ),
            context
        )
        starSelectedBitmap = getZoomBitmap(tempSelectedBitmap)
        val tempUnselectedBitmap = BmpUtils.getBitmapFromDrawable(
            a.getResourceId(
                R.styleable.RatingView_star_unselected,
                R.drawable.ic_star_default_unselected
            ),
            context
        )
        starUnselectedBitmap = getZoomBitmap(tempUnselectedBitmap)
        starOrientation = when (a.getInt(R.styleable.RatingView_star_orientation, 0)) {
            0 -> Orientation.HORIZONTAL
            1 -> Orientation.VERTICAL
            else -> Orientation.HORIZONTAL
        }
        starSelectMethod = when (a.getInt(R.styleable.RatingView_star_select_method, 0)) {
            0 -> SelectMethod.Unable
            1 -> SelectMethod.Click
            2 -> SelectMethod.Sliding
            else -> SelectMethod.Sliding
        }
        a.recycle()
    }
}