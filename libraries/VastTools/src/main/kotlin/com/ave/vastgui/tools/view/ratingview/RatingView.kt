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
import com.ave.vastgui.core.extension.nothing_to_do
import com.ave.vastgui.tools.R
import com.ave.vastgui.tools.utils.image.BmpUtils

// Author: Vast Gui
// Email: sakurajimamai2020@qq.com
// Date: 2021/7/28
// Documentation: https://ave.entropy2020.cn/documents/VastTools/core-topics/ui/ratingview/RatingView/

/**
 * RatingView.
 *
 * @property mOriginalSelectedBitmap The original bitmap of the selected star.
 * @property mOriginalUnselectedBitmap The original bitmap of the unselected star.
 * @property mStarSelectedBitmap The bitmap of the selected star with required size.
 * @property mStarUnselectedBitmap The bitmap of the unselected star with required size.
 * @property mStarIntervalWidth Star interval width(in pixels).
 * @property mStarBitmapWidth Star Bitmap width(in pixels).
 * @property mStarBitmapHeight Star Bitmap height(in pixels).
 * @property mStarCountNumber Max number of stars.
 * @property mStarRating The progress of the currently selected stars.
 * @property mStarSolidNumber The number of currently selected stars.
 * @property mStarSelectMethod The star selection method.
 * @property mStarOrientation The star orientation.
 */
class RatingView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int = R.attr.Default_RatingView_Style,
    defStyleRes: Int = R.style.BaseRatingView
) : View(context, attrs, defStyleAttr, defStyleRes) {

    private val mDefaultStarIntervalWidth
        get() = context.resources.getDimension(R.dimen.default_star_interval_width)
    private val mDefaultStarBitmapWidth
        get() = context.resources.getDimension(R.dimen.default_star_width)
    private val mDefaultStarBitmapHeight
        get() = context.resources.getDimension(R.dimen.default_star_height)
    private val mDefaultSelectMethod
        get() = context.resources.getInteger(R.integer.default_rating_select_method)
    private val mDefaultOrientation
        get() = context.resources.getInteger(R.integer.default_rating_star_orientation)

    private val mExtraSrc: Rect = Rect()
    private val mExtraDst: Rect = Rect()
    private val mPaint: Paint = Paint()
    private var mStarSelectedBitmap: Bitmap
    private var mStarUnselectedBitmap: Bitmap
    private var mStarSolidNumber = 0

    var mOriginalSelectedBitmap: Bitmap
        private set

    var mOriginalUnselectedBitmap: Bitmap
        private set

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

    var mStarSelectMethod: StarSelectMethod
        private set

    var mStarOrientation: StarOrientation
        private set

    override fun onDraw(canvas: Canvas) {
        // Draw selected star.
        var solidStartPoint = 0f
        when (mStarOrientation) {
            StarOrientation.HORIZONTAL -> for (i in 1..mStarSolidNumber) {
                canvas.drawBitmap(mStarSelectedBitmap, solidStartPoint, 0f, mPaint)
                solidStartPoint += mStarIntervalWidth.toInt() + mStarSelectedBitmap.width
            }

            StarOrientation.VERTICAL -> for (i in 1..mStarSolidNumber) {
                canvas.drawBitmap(mStarSelectedBitmap, 0f, solidStartPoint, mPaint)
                solidStartPoint += mStarIntervalWidth.toInt() + mStarSelectedBitmap.height
            }

            StarOrientation.UNSPECIFIED -> nothing_to_do()
        }
        // Unselected Star Bitmap start point.
        var hollowStartPoint = solidStartPoint
        // Unselected Star number.
        val hollowStarNum = mStarCountNumber - mStarSolidNumber
        when (mStarOrientation) {
            StarOrientation.HORIZONTAL -> for (j in 1..hollowStarNum) {
                canvas.drawBitmap(mStarUnselectedBitmap, hollowStartPoint, 0f, mPaint)
                hollowStartPoint += mStarIntervalWidth.toInt() + mStarUnselectedBitmap.width
            }

            StarOrientation.VERTICAL -> for (j in 1..hollowStarNum) {
                canvas.drawBitmap(mStarUnselectedBitmap, 0f, hollowStartPoint, mPaint)
                hollowStartPoint += mStarIntervalWidth.toInt() + mStarUnselectedBitmap.width
            }

            StarOrientation.UNSPECIFIED -> nothing_to_do()
        }
        // Extra selected star bitmap Length.
        when (mStarOrientation) {
            StarOrientation.HORIZONTAL -> {
                canvas.drawBitmap(mStarSelectedBitmap, mExtraSrc, mExtraDst, mPaint)
            }

            StarOrientation.VERTICAL -> {
                canvas.drawBitmap(mStarSelectedBitmap, mExtraSrc, mExtraDst, mPaint)
            }

            StarOrientation.UNSPECIFIED -> nothing_to_do()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        when (mStarOrientation) {
            StarOrientation.HORIZONTAL -> setMeasuredDimension(
                measureLong(widthMeasureSpec),
                measureShort(heightMeasureSpec)
            )

            StarOrientation.VERTICAL -> setMeasuredDimension(
                measureShort(widthMeasureSpec),
                measureLong(heightMeasureSpec)
            )

            StarOrientation.UNSPECIFIED -> setMeasuredDimension(0,0)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (mStarSelectMethod) {
            StarSelectMethod.SLIDING -> {
                if (mStarOrientation == StarOrientation.HORIZONTAL) {
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

            StarSelectMethod.CLICK -> {
                if (event.action == MotionEvent.ACTION_DOWN) {
                    if (mStarOrientation == StarOrientation.HORIZONTAL) {
                        val totalWidth =
                            (mStarCountNumber * mStarBitmapWidth) + (mStarCountNumber - 1) * mStarIntervalWidth
                        val newStarRating = if (event.x <= totalWidth) {
                            event.x.toInt() / (mStarBitmapWidth + mStarIntervalWidth)
                        } else mStarCountNumber.toFloat()
                        setStarRating(newStarRating)
                    } else {
                        val totalHeight =
                            (mStarCountNumber * mStarBitmapHeight) + (mStarCountNumber - 1) * mStarIntervalWidth
                        val newStarRating = if (event.y <= totalHeight) {
                            event.y.toInt() / (mStarBitmapHeight + mStarIntervalWidth)
                        } else mStarCountNumber.toFloat()
                        setStarRating(newStarRating)
                    }
                }
                performClick()
                return true
            }

            StarSelectMethod.UNABLE -> {
                performClick()
                return super.onTouchEvent(event)
            }
        }
    }

    /**
     * Set star rating by [starRating]. If [mStarRating] is greater than
     * [mStarCountNumber], it will be set to [mStarCountNumber].
     */
    fun setStarRating(@FloatRange(from = 0.0) starRating: Float) {
        mStarRating = starRating.coerceAtMost(mStarCountNumber.toFloat())
        mStarSolidNumber = starRating.toInt()
        val extraSolidLength =
            ((starRating - mStarSolidNumber) * mStarUnselectedBitmap.width).toInt()
        var extraSolidStarPoint = 0
        when (mStarOrientation) {
            StarOrientation.HORIZONTAL -> {
                for (i in 1..mStarSolidNumber) {
                    extraSolidStarPoint += mStarIntervalWidth.toInt() + mStarSelectedBitmap.width
                }
                mExtraSrc.set(0, 0, extraSolidLength, mStarUnselectedBitmap.height)
                mExtraDst.set(
                    extraSolidStarPoint,
                    0,
                    extraSolidStarPoint + extraSolidLength,
                    mStarUnselectedBitmap.height
                )
            }

            StarOrientation.VERTICAL -> {
                for (i in 1..mStarSolidNumber) {
                    extraSolidStarPoint += mStarIntervalWidth.toInt() + mStarSelectedBitmap.height
                }
                mExtraSrc.set(0, 0, mStarUnselectedBitmap.width, extraSolidLength)
                mExtraDst.set(
                    0,
                    extraSolidStarPoint,
                    mStarUnselectedBitmap.width,
                    extraSolidStarPoint + extraSolidLength
                )
            }

            StarOrientation.UNSPECIFIED -> nothing_to_do()
        }
        invalidate()
    }

    /**
     * Set Star Select Method
     *
     * @param starSelectMethod Int
     */
    fun setStarSelectMethod(starSelectMethod: StarSelectMethod) {
        this.mStarSelectMethod = starSelectMethod
    }

    /**
     * Set bitmap size of [mStarSelectedBitmap] and [mStarUnselectedBitmap].
     *
     * @since 0.5.3
     */
    fun setStarBitmapSize(
        @FloatRange(from = 0.0) starWidth: Float,
        @FloatRange(from = 0.0) starHeight: Float
    ) {
        this.mStarBitmapWidth = starWidth
        this.mStarBitmapHeight = starHeight
        mStarSelectedBitmap =
            BmpUtils.scaleBitmap(
                mOriginalSelectedBitmap,
                starWidth.toInt(),
                starHeight.toInt()
            )
        mStarUnselectedBitmap =
            BmpUtils.scaleBitmap(
                mOriginalUnselectedBitmap,
                starWidth.toInt(),
                starHeight.toInt()
            )
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
        mOriginalSelectedBitmap = bitmap
        mStarSelectedBitmap = BmpUtils.scaleBitmap(
            mOriginalSelectedBitmap,
            mStarBitmapWidth.toInt(), mStarBitmapHeight.toInt()
        )
    }

    /**
     * Set the bitmap of the be selected star by drawableId.
     *
     * @param drawableId Int
     */
    fun setStarSelectedBitmap(@DrawableRes drawableId: Int) {
        mOriginalSelectedBitmap = BmpUtils.getBitmapFromDrawable(drawableId, context)
        mStarSelectedBitmap = BmpUtils.scaleBitmap(
            mOriginalSelectedBitmap,
            mStarBitmapWidth.toInt(), mStarBitmapHeight.toInt()
        )
    }

    /**
     * Set the bitmap of the be unselected star.
     *
     * @param bitmap Bitmap
     */
    fun setStarUnselectedBitmap(bitmap: Bitmap) {
        mOriginalUnselectedBitmap = bitmap
        mStarUnselectedBitmap = BmpUtils.scaleBitmap(
            mOriginalUnselectedBitmap,
            mStarBitmapWidth.toInt(), mStarBitmapHeight.toInt()
        )
    }

    /**
     * Set the bitmap of the be unselected star by drawableId.
     *
     * @param drawableId Int
     */
    fun setStarUnselectedBitmap(@DrawableRes drawableId: Int) {
        mOriginalUnselectedBitmap = BmpUtils.getBitmapFromDrawable(drawableId, context)
        mStarUnselectedBitmap = BmpUtils.scaleBitmap(
            mOriginalUnselectedBitmap,
            mStarBitmapWidth.toInt(), mStarBitmapHeight.toInt()
        )
    }

    /**
     * Set star orientation.
     *
     * The setting is only valid when the [mStarOrientation] value is
     * [StarOrientation.UNSPECIFIED].
     *
     * @since 0.5.3
     */
    fun setStarOrientation(starOrientation: StarOrientation) {
        if (mStarOrientation == StarOrientation.UNSPECIFIED) {
            mStarOrientation = starOrientation
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
            context.obtainStyledAttributes(
                attrs,
                R.styleable.RatingView,
                defStyleAttr,
                defStyleRes
            )
        mStarIntervalWidth =
            typedArray.getDimension(
                R.styleable.RatingView_star_interval_width,
                mDefaultStarIntervalWidth
            )
        mStarBitmapWidth =
            typedArray.getDimension(
                R.styleable.RatingView_star_width,
                mDefaultStarBitmapWidth
            )
        mStarBitmapHeight =
            typedArray.getDimension(
                R.styleable.RatingView_star_height,
                mDefaultStarBitmapHeight
            )
        mStarCountNumber = typedArray.getInt(R.styleable.RatingView_star_count, 5)
        mStarRating = typedArray.getFloat(R.styleable.RatingView_star_rating, 0f)
        mOriginalSelectedBitmap =
            BmpUtils.getBitmapFromDrawable(
                typedArray.getResourceId(
                    R.styleable.RatingView_star_selected,
                    R.drawable.ic_star_default_selected
                ),
                context
            )
        mStarSelectedBitmap = BmpUtils.scaleBitmap(
            mOriginalSelectedBitmap,
            mStarBitmapWidth.toInt(),
            mStarBitmapHeight.toInt()
        )
        mOriginalUnselectedBitmap = BmpUtils.getBitmapFromDrawable(
            typedArray.getResourceId(
                R.styleable.RatingView_star_unselected,
                R.drawable.ic_star_default_unselected
            ),
            context
        )
        mStarUnselectedBitmap = BmpUtils.scaleBitmap(
            mOriginalUnselectedBitmap,
            mStarBitmapWidth.toInt(),
            mStarBitmapHeight.toInt()
        )
        mStarOrientation = when (typedArray.getInt(
            R.styleable.RatingView_star_orientation, mDefaultOrientation
        )) {
            StarOrientation.HORIZONTAL.code -> StarOrientation.HORIZONTAL
            StarOrientation.VERTICAL.code -> StarOrientation.VERTICAL
            else -> StarOrientation.UNSPECIFIED
        }
        mStarSelectMethod = when (typedArray.getInt(
            R.styleable.RatingView_star_select_method, mDefaultSelectMethod
        )) {
            StarSelectMethod.UNABLE.code -> StarSelectMethod.UNABLE
            StarSelectMethod.CLICK.code -> StarSelectMethod.CLICK
            StarSelectMethod.SLIDING.code -> StarSelectMethod.SLIDING
            else -> StarSelectMethod.UNABLE
        }
        typedArray.recycle()
    }
}