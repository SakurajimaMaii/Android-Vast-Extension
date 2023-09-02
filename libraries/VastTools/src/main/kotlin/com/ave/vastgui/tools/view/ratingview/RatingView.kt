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

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.DrawableRes
import androidx.annotation.FloatRange
import androidx.annotation.IntRange
import com.ave.vastgui.tools.R
import com.ave.vastgui.tools.utils.image.BmpUtils

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
 * @property mStarSelectedBitmap The bitmap of the selected star.
 * @property mStarUnselectedBitmap The bitmap of the unselected star.
 * @property mStarIntervalWidth Star interval width(in pixels).
 * @property mStarBitmapWidth Star Bitmap width(in pixels).
 * @property mStarBitmapHeight Star Bitmap height(in pixels).
 * @property mStarCountNumber Max number of stars.
 * @property mStarRating The progress of the currently selected stars.
 * @property starSolidNumber The number of currently selected stars.
 * @property mStarSelectMethod The star selection method.
 * @property mStarOrientation The star orientation.
 */
class RatingView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int = R.attr.Default_RatingView_Style,
    defStyleRes: Int = R.style.BaseRatingView
) : View(context, attrs, defStyleAttr, defStyleRes) {

    private var rectSrc: Rect = Rect()
    private var dstF: Rect = Rect()
    private val paint: Paint = Paint()
    private var mStarSelectedBitmap: Bitmap
    private var mStarUnselectedBitmap: Bitmap

    private var starSolidNumber = 0

    var mStarIntervalWidth: Float
        private set

    var mStarBitmapWidth: Float
        private set

    var mStarBitmapHeight: Float
        private set

    var mStarCountNumber: Int
        private set

    var mStarRating: Float
        private set

    var mStarSelectMethod: RatingSelectMethod
        private set

    var mStarOrientation: RatingOrientation
        private set

    override fun onDraw(canvas: Canvas) {
        // Draw selected star.
        var solidStartPoint = 0
        when (mStarOrientation) {
            RatingOrientation.HORIZONTAL -> for (i in 1..starSolidNumber) {
                canvas.drawBitmap(mStarSelectedBitmap, solidStartPoint.toFloat(), 0f, paint)
                solidStartPoint += mStarIntervalWidth.toInt() + mStarSelectedBitmap.width
            }

            RatingOrientation.VERTICAL -> for (i in 1..starSolidNumber) {
                canvas.drawBitmap(mStarSelectedBitmap, 0f, solidStartPoint.toFloat(), paint)
                solidStartPoint += mStarIntervalWidth.toInt() + mStarSelectedBitmap.height
            }
        }
        // Unselected Star Bitmap start point.
        var hollowStartPoint = solidStartPoint
        // Unselected Star number.
        val hollowStarNum = mStarCountNumber - starSolidNumber
        when (mStarOrientation) {
            RatingOrientation.HORIZONTAL -> for (j in 1..hollowStarNum) {
                canvas.drawBitmap(mStarUnselectedBitmap, hollowStartPoint.toFloat(), 0f, paint)
                hollowStartPoint += mStarIntervalWidth.toInt() + mStarUnselectedBitmap.width
            }

            RatingOrientation.VERTICAL -> for (j in 1..hollowStarNum) {
                canvas.drawBitmap(mStarUnselectedBitmap, 0f, hollowStartPoint.toFloat(), paint)
                hollowStartPoint += mStarIntervalWidth.toInt() + mStarUnselectedBitmap.width
            }
        }
        // Extra selected star bitmap Length.
        when (mStarOrientation) {
            RatingOrientation.HORIZONTAL -> {
                canvas.drawBitmap(mStarSelectedBitmap, rectSrc, dstF, paint)
            }

            RatingOrientation.VERTICAL -> {
                canvas.drawBitmap(mStarSelectedBitmap, rectSrc, dstF, paint)
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        when (mStarOrientation) {
            RatingOrientation.HORIZONTAL -> setMeasuredDimension(
                measureLong(widthMeasureSpec),
                measureShort(heightMeasureSpec)
            )

            RatingOrientation.VERTICAL -> setMeasuredDimension(
                measureShort(widthMeasureSpec),
                measureLong(heightMeasureSpec)
            )
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (mStarSelectMethod) {
            RatingSelectMethod.SLIDING -> {
                if (mStarOrientation == RatingOrientation.HORIZONTAL) {
                    val totalWidth =
                        (mStarCountNumber * (mStarBitmapWidth + mStarIntervalWidth))
                    if (event.x <= totalWidth) {
                        val newStarRating = event.x / (mStarBitmapWidth + mStarIntervalWidth)
                        setStarRating(newStarRating)
                    }
                } else {
                    val totalHeight =
                        (mStarCountNumber * (mStarBitmapHeight + mStarIntervalWidth))
                    if (event.y <= totalHeight) {
                        val newStarRating = event.y / (mStarBitmapHeight + mStarIntervalWidth)
                        setStarRating(newStarRating)
                    }
                }
                performClick()
                return true
            }

            else -> {
                if (mStarSelectMethod == RatingSelectMethod.CLICK) {
                    if (event.action == MotionEvent.ACTION_DOWN) {
                        if (mStarOrientation == RatingOrientation.HORIZONTAL) {
                            val totalWidth =
                                (mStarCountNumber * (mStarBitmapWidth + mStarIntervalWidth))
                            if (event.x <= totalWidth) {
                                val newStarRating =
                                    event.x.toInt() / (mStarBitmapHeight + mStarIntervalWidth)
                                setStarRating(newStarRating)
                            }
                        } else {
                            val totalHeight =
                                (mStarCountNumber * (mStarBitmapHeight + mStarIntervalWidth))
                            if (event.y <= totalHeight) {
                                val newStarRating =
                                    event.y.toInt() / (mStarBitmapHeight + mStarIntervalWidth)
                                setStarRating(newStarRating)
                            }
                        }
                    }
                }
                return super.onTouchEvent(event)
            }
        }
    }

    /**
     * Set star rating.
     *
     * @param starRating Float
     * @throws IllegalArgumentException
     */
    @Throws(IllegalArgumentException::class)
    fun setStarRating(@FloatRange(from = 0.0) starRating: Float) {
        when {
            starRating.toInt() > mStarCountNumber -> {
                throw IllegalArgumentException("The number of selected stars must less than $mStarCountNumber")
            }

            else -> {
                this.mStarRating = starRating
                starSolidNumber = starRating.toInt()
                val extraSolidLength =
                    ((starRating - starSolidNumber) * mStarUnselectedBitmap.width).toInt()
                var extraSolidStarPoint = 0
                when (mStarOrientation) {
                    RatingOrientation.HORIZONTAL -> {
                        for (i in 1..starSolidNumber) {
                            extraSolidStarPoint += mStarIntervalWidth.toInt() + mStarSelectedBitmap.width
                        }
                        rectSrc = Rect(0, 0, extraSolidLength, mStarUnselectedBitmap.height)
                        dstF = Rect(
                            extraSolidStarPoint,
                            0,
                            extraSolidStarPoint + extraSolidLength,
                            mStarUnselectedBitmap.height
                        )
                    }

                    RatingOrientation.VERTICAL -> {
                        for (i in 1..starSolidNumber) {
                            extraSolidStarPoint += mStarIntervalWidth.toInt() + mStarSelectedBitmap.height
                        }
                        rectSrc = Rect(0, 0, mStarUnselectedBitmap.width, extraSolidLength)
                        dstF = Rect(
                            0,
                            extraSolidStarPoint,
                            mStarUnselectedBitmap.width,
                            extraSolidStarPoint + extraSolidLength
                        )
                    }
                }
            }
        }
        invalidate()
    }

    /**
     * Set Star Select Method
     *
     * @param starSelectMethod Int
     */
    fun setStarSelectMethod(starSelectMethod: RatingSelectMethod) {
        this.mStarSelectMethod = starSelectMethod
    }

    /**
     * Set star bitmap size.
     *
     * @param starWidth Int
     * @param starHeight Int
     */
    fun setStarBitMapSize(
        @FloatRange(from = 0.0) starWidth: Float,
        @FloatRange(from = 0.0) starHeight: Float
    ) {
        this.mStarBitmapWidth = starWidth
        this.mStarBitmapHeight = starHeight
        BmpUtils.scaleBitmap(mStarSelectedBitmap, starWidth.toInt(), starHeight.toInt()).let {
            mStarSelectedBitmap = it
        }
        BmpUtils.scaleBitmap(mStarUnselectedBitmap, starWidth.toInt(), starHeight.toInt())
            .let {
                mStarUnselectedBitmap = it
            }
    }

    /**
     * Set star bitmap interval.
     *
     * @param starSpaceWidth Int
     */
    fun setStarIntervalWidth(@FloatRange(from = 0.0) starSpaceWidth: Float) {
        this.mStarIntervalWidth = starSpaceWidth
    }

    /** Set the number of star. */
    fun setStarCountNumber(@IntRange(from = 0) starCountNumber: Int) {
        this.mStarCountNumber = starCountNumber
    }

    /** Set the bitmap of the be selected star. */
    fun setStarSelectedBitmap(bitmap: Bitmap) {
        BmpUtils.scaleBitmap(
            bitmap, mStarBitmapWidth.toInt(), mStarBitmapHeight.toInt()
        ).let {
            mStarSelectedBitmap = it
        }
    }

    /**
     * Set the bitmap of the be selected star by drawableId.
     *
     * @param drawableId Int
     */
    @Throws(IllegalArgumentException::class)
    fun setStarSelectedBitmap(@DrawableRes drawableId: Int) {
        BmpUtils.scaleBitmap(
            BmpUtils.getBitmapFromDrawable(drawableId, context),
            mStarBitmapWidth.toInt(), mStarBitmapHeight.toInt()
        ).let {
            mStarSelectedBitmap = it
        }
    }

    /**
     * Set the bitmap of the be unselected star.
     *
     * @param bitmap Bitmap
     */
    fun setStarUnselectedBitmap(bitmap: Bitmap) {
        BmpUtils.scaleBitmap(
            bitmap, mStarBitmapWidth.toInt(), mStarBitmapHeight.toInt()
        ).let {
            mStarUnselectedBitmap = it
        }
    }

    /**
     * Set the bitmap of the be unselected star by drawableId.
     *
     * @param drawableId Int
     */
    @Throws(IllegalArgumentException::class)
    fun setStarUnselectedBitmap(@DrawableRes drawableId: Int) {
        BmpUtils.scaleBitmap(
            BmpUtils.getBitmapFromDrawable(drawableId, context),
            mStarBitmapWidth.toInt(), mStarBitmapHeight.toInt()
        ).let {
            mStarUnselectedBitmap = it
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
                (paddingLeft + paddingRight + (mStarIntervalWidth + mStarBitmapWidth) * mStarCountNumber).toInt()
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
            result = (mStarBitmapHeight + paddingTop + paddingBottom).toInt()
            if (specMode == MeasureSpec.AT_MOST) {
                result = result.coerceAtMost(specSize)
            }
        }
        return result
    }

    init {
        val typedArray =
            context.obtainStyledAttributes(attrs, R.styleable.RatingView, defStyleAttr, defStyleRes)
        mStarIntervalWidth = typedArray.getDimension(R.styleable.RatingView_star_interval_width, 0f)
        mStarBitmapWidth = typedArray.getDimension(R.styleable.RatingView_star_width, 0f)
        mStarBitmapHeight = typedArray.getDimension(R.styleable.RatingView_star_height, 0f)
        mStarCountNumber = typedArray.getInt(R.styleable.RatingView_star_count, 0)
        mStarRating = typedArray.getFloat(R.styleable.RatingView_star_rating, 0f)
        val tempSelectedBitmap = BmpUtils.getBitmapFromDrawable(
            typedArray.getResourceId(
                R.styleable.RatingView_star_selected,
                R.drawable.ic_star_default_selected
            ),
            context
        )
        BmpUtils.scaleBitmap(
            tempSelectedBitmap,
            mStarBitmapWidth.toInt(),
            mStarBitmapHeight.toInt()
        ).let {
            mStarSelectedBitmap = it
        }
        val tempUnselectedBitmap = BmpUtils.getBitmapFromDrawable(
            typedArray.getResourceId(
                R.styleable.RatingView_star_unselected,
                R.drawable.ic_star_default_unselected
            ),
            context
        )
        BmpUtils.scaleBitmap(
            tempUnselectedBitmap,
            mStarBitmapWidth.toInt(),
            mStarBitmapHeight.toInt()
        ).let {
            mStarUnselectedBitmap = it
        }
        mStarOrientation = when (typedArray.getInt(
            R.styleable.RatingView_star_orientation,
            RatingOrientation.HORIZONTAL.ordinal
        )) {
            RatingOrientation.HORIZONTAL.ordinal -> RatingOrientation.HORIZONTAL
            RatingOrientation.VERTICAL.ordinal -> RatingOrientation.VERTICAL
            else -> RatingOrientation.HORIZONTAL
        }
        mStarSelectMethod = when (typedArray.getInt(
            R.styleable.RatingView_star_select_method,
            RatingSelectMethod.SLIDING.ordinal
        )) {
            RatingSelectMethod.UNABLE.ordinal -> RatingSelectMethod.UNABLE
            RatingSelectMethod.CLICK.ordinal -> RatingSelectMethod.CLICK
            RatingSelectMethod.SLIDING.ordinal -> RatingSelectMethod.SLIDING
            else -> RatingSelectMethod.SLIDING
        }
        typedArray.recycle()
    }
}