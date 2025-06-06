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

package com.ave.vastgui.tools.view.ratingview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.annotation.DrawableRes
import androidx.annotation.FloatRange
import androidx.annotation.IntRange
import androidx.core.content.withStyledAttributes
import androidx.core.graphics.withClip
import com.ave.vastgui.tools.R
import com.ave.vastgui.tools.graphics.BmpUtils
import com.ave.vastgui.tools.utils.dimension
import com.ave.vastgui.tools.utils.integer
import kotlin.math.round
import kotlin.properties.Delegates

// Author: Vast Gui
// Email: sakurajimamai2020@qq.com
// Date: 2021/7/28
// Documentation: https://sakurajimamaii.github.io/AVE-DOC/documents/tools/core-topics/ui/rating/rating-view/

/** [RatingView]. */
class RatingView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int = R.attr.Default_RatingView_Style,
    defStyleRes: Int = R.style.BaseRatingView
) : View(context, attrs, defStyleAttr, defStyleRes) {

    /**
     * Listener that the rating has changed.
     *
     * @since 0.5.6
     */
    interface OnStarRatingChangeListener {

        /**
         * Callback when rating changed
         *
         * @param rating Current rating.
         * @since 0.5.6
         */
        fun onRatingChanged(rating: Float)
    }

    /** @since 1.5.2 */
    @Suppress("PrivatePropertyName")
    private val DEFAULT_STAR_INTERVAL_WIDTH
        get() = dimension(R.dimen.default_star_interval_width)

    /** @since 1.5.2 */
    @Suppress("PrivatePropertyName")
    private val DEFAULT_STAR_BITMAP_WIDTH
        get() = dimension(R.dimen.default_star_width)

    /** @since 1.5.2 */
    @Suppress("PrivatePropertyName")
    private val DEFAULT_STAR_BITMAP_HEIGHT
        get() = dimension(R.dimen.default_star_height)

    /** @since 1.5.2 */
    @Suppress("PrivatePropertyName")
    private val DEFAULT_SELECT_METHOD
        get() = integer(R.integer.default_rating_select_method)

    /** @since 1.5.2 */
    @Suppress("PrivatePropertyName")
    private val DEFAULT_ORIENTATION
        get() = integer(R.integer.default_rating_star_orientation)

    /** @since 1.5.2 */
    @Suppress("PrivatePropertyName")
    private val DEFAULT_STAR_COUNT
        get() = integer(R.integer.default_rating_star_count)

    /** @since 1.5.2 */
    @Suppress("PrivatePropertyName")
    private val DEFAULT_RATING
        get() = integer(R.integer.default_rating_star_rating).toFloat()

    /** @since 1.5.2 */
    private val paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    /** @since 1.5.2 */
    private var _selectedBitmap: Bitmap by Delegates.notNull()

    /** @since 1.5.2 */
    private var _unselectedBitmap: Bitmap by Delegates.notNull()

    /** @since 1.5.2 */
    private var listener: OnStarRatingChangeListener? = null

    /**
     * The bitmap of the selected star.
     *
     * @since 1.5.2
     */
    var selectedBitmap: Bitmap by Delegates.notNull()
        private set

    /**
     * The bitmap of the unselected star.
     *
     * @since 1.5.2
     */
    var unselectedBitmap: Bitmap by Delegates.notNull()
        private set

    /** @since 1.5.2 */
    private var _rating: Float by Delegates.notNull()

    /** @since 1.5.2 */
    val rating: Float
        get() = _rating

    /** @since 1.5.2 */
    private var _starIntervalWidth: Float by Delegates.notNull()

    /**
     * Star interval width(in pixels).
     *
     * @since 1.5.2
     */
    val starIntervalWidth: Float
        get() = _starIntervalWidth

    /** @since 1.5.2 */
    private var _starCountNumber: Int by Delegates.notNull()

    /**
     * Max number of stars.
     *
     * @since 1.5.2
     */
    val starCountNumber: Int
        get() = _starCountNumber

    /** @since 1.5.2 */
    private var _starSelectMethod: StarSelectMethod by Delegates.notNull()

    /**
     * The star selection method.
     *
     * @since 1.5.2
     */
    val starSelectMethod: StarSelectMethod
        get() = _starSelectMethod

    /** @since 1.5.2 */
    private var _starOrientation: StarOrientation by Delegates.notNull()

    /**
     * The star orientation.
     *
     * @since 1.5.2
     */
    val starOrientation: StarOrientation
        get() = _starOrientation

    /**
     * Star Bitmap width(in pixels).
     *
     * @since 1.5.2
     */
    var starBitmapWidth: Float by Delegates.notNull()
        private set

    /**
     * Star Bitmap height(in pixels).
     *
     * @since 1.5.2
     */
    var starBitmapHeight: Float by Delegates.notNull()
        private set

    override fun onDraw(canvas: Canvas) {
        when (_starOrientation) {
            StarOrientation.Horizontal -> {
                (1.._starCountNumber).forEach { index ->
                    val offset = (index - 1) * (_starIntervalWidth + starBitmapWidth)
                    canvas.drawBitmap(
                        _unselectedBitmap,
                        paddingStart + offset,
                        paddingTop.toFloat(),
                        paint
                    )
                }
                canvas.withClip(
                    paddingStart.toFloat(),
                    0f,
                    paddingStart + _rating * _starCountNumber * (_starIntervalWidth + starBitmapWidth),
                    measuredHeight.toFloat()
                ) {
                    (1.._starCountNumber).forEach { index ->
                        val offset = (index - 1) * (_starIntervalWidth + starBitmapWidth)
                        canvas.drawBitmap(
                            _selectedBitmap,
                            paddingStart + offset,
                            paddingTop.toFloat(),
                            paint
                        )
                    }
                }
            }

            StarOrientation.Vertical -> {
                (1.._starCountNumber).forEach { index ->
                    val offset = (index - 1) * (_starIntervalWidth + starBitmapHeight)
                    canvas.drawBitmap(
                        _unselectedBitmap,
                        paddingStart.toFloat(),
                        paddingTop + offset,
                        paint
                    )
                }
                canvas.withClip(
                    0f,
                    paddingTop.toFloat(),
                    measuredWidth.toFloat(),
                    paddingTop + _rating * _starCountNumber * (_starIntervalWidth + starBitmapHeight)
                ) {
                    (1.._starCountNumber).forEach { index ->
                        val offset = (index - 1) * (_starIntervalWidth + starBitmapHeight)
                        canvas.drawBitmap(
                            _selectedBitmap,
                            paddingStart.toFloat(),
                            paddingTop + offset,
                            paint
                        )
                    }
                }
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        when (_starOrientation) {
            StarOrientation.Horizontal -> {
                val requiredWidth = _starCountNumber * starBitmapWidth +
                        (_starCountNumber - 1) * _starIntervalWidth +
                        paddingStart + paddingEnd
                val width = resolveSize(requiredWidth.toInt(), widthMeasureSpec)
                val requiredHeight = starBitmapHeight + paddingTop + paddingBottom
                val height = resolveSize(requiredHeight.toInt(), heightMeasureSpec)
                setMeasuredDimension(width, height)
            }

            StarOrientation.Vertical -> {
                val requiredWidth = starBitmapWidth + paddingStart + paddingEnd
                val width = resolveSize(requiredWidth.toInt(), widthMeasureSpec)
                val requiredHeight = _starCountNumber * starBitmapHeight +
                        (_starCountNumber - 1) * _starIntervalWidth +
                        paddingTop + paddingBottom
                val height = resolveSize(requiredHeight.toInt(), heightMeasureSpec)
                setMeasuredDimension(width, height)
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (_starSelectMethod == StarSelectMethod.Sliding) {
            when (event.action) {
                MotionEvent.ACTION_MOVE -> {
                    parent.requestDisallowInterceptTouchEvent(true)
                    if (_starOrientation == StarOrientation.Horizontal) {
                        val newStarRating = event.x.coerceIn(0f, measuredWidth.toFloat()) / measuredWidth
                        setStarRating(newStarRating)
                    } else if (_starOrientation == StarOrientation.Vertical) {
                        val newStarRating = event.y.coerceIn(0f, measuredHeight.toFloat()) / measuredHeight
                        setStarRating(newStarRating)
                    }
                }

                MotionEvent.ACTION_UP ->
                    parent.requestDisallowInterceptTouchEvent(false)
            }
        } else if (_starSelectMethod == StarSelectMethod.Click) {
            if (_starOrientation == StarOrientation.Horizontal) {
                val newStarRating = round((event.x.coerceIn(0f, measuredWidth.toFloat()) / measuredWidth) * _starCountNumber) / _starCountNumber
                setStarRating(newStarRating)
            } else if (_starOrientation == StarOrientation.Vertical) {
                val newStarRating = round((event.x.coerceIn(0f, measuredHeight.toFloat()) / measuredHeight) * _starCountNumber) / _starCountNumber
                setStarRating(newStarRating)
            }
            return true
        }

        return super.onTouchEvent(event)
    }

    /**
     * Set star rating by [starRating]. If [starRating] is greater than
     * [starCountNumber], it will be set to [starCountNumber].
     */
    fun setStarRating(@FloatRange(from = 0.0, to = 1.0) starRating: Float) {
        val rating = starRating.coerceIn(0f, 1f)
        if (_rating != rating) {
            _rating = rating
            listener?.onRatingChanged(_rating)
            invalidate()
        }
    }

    /**
     * Set Star Select Method
     *
     * @param starSelectMethod Int
     */
    fun setStarSelectMethod(starSelectMethod: StarSelectMethod) {
        _starSelectMethod = starSelectMethod
    }

    /**
     * Set bitmap size of [_selectedBitmap] and [_unselectedBitmap].
     *
     * @since 0.5.3
     */
    fun setStarBitmapSize(@FloatRange(from = 0.0) starWidth: Float, @FloatRange(from = 0.0) starHeight: Float) {
        starBitmapWidth = starWidth.coerceAtLeast(0f)
        starBitmapHeight = starHeight.coerceAtLeast(0f)
        _selectedBitmap = BmpUtils.scaleBitmap(selectedBitmap, starBitmapWidth.toInt(), starBitmapHeight.toInt())
        _unselectedBitmap = BmpUtils.scaleBitmap(unselectedBitmap, starBitmapWidth.toInt(), starBitmapHeight.toInt())
        requestLayout()
    }

    /**
     * Set star bitmap interval(in pixels).
     *
     * @param starSpaceWidth Int
     */
    fun setStarIntervalWidth(@FloatRange(from = 0.0) starSpaceWidth: Float) {
        _starIntervalWidth = starSpaceWidth.coerceAtLeast(0f)
        requestLayout()
    }

    /** Set the number of star. */
    fun setStarCountNumber(@IntRange(from = 0) starCountNumber: Int) {
        _starCountNumber = starCountNumber.coerceAtLeast(0)
        requestLayout()
    }

    /** Set the bitmap of the be selected star. */
    fun setStarSelectedBitmap(bitmap: Bitmap) {
        selectedBitmap = bitmap
        _selectedBitmap = BmpUtils.scaleBitmap(selectedBitmap, starBitmapWidth.toInt(), starBitmapHeight.toInt())
        invalidate()
    }

    /**
     * Set the bitmap of the be selected star by drawableId.
     *
     * @param drawableId Int
     */
    fun setStarSelectedBitmap(@DrawableRes drawableId: Int) {
        selectedBitmap = BmpUtils.getBitmapFromDrawable(drawableId, context)
        _selectedBitmap = BmpUtils.scaleBitmap(selectedBitmap, starBitmapWidth.toInt(), starBitmapHeight.toInt())
        invalidate()
    }

    /**
     * Set the bitmap of the be unselected star.
     *
     * @param bitmap Bitmap
     */
    fun setStarUnselectedBitmap(bitmap: Bitmap) {
        unselectedBitmap = bitmap
        _unselectedBitmap = BmpUtils.scaleBitmap(unselectedBitmap, starBitmapWidth.toInt(), starBitmapHeight.toInt())
        invalidate()
    }

    /**
     * Set the bitmap of the be unselected star by drawableId.
     *
     * @param drawableId Int
     */
    fun setStarUnselectedBitmap(@DrawableRes drawableId: Int) {
        unselectedBitmap = BmpUtils.getBitmapFromDrawable(drawableId, context)
        _unselectedBitmap = BmpUtils.scaleBitmap(unselectedBitmap, starBitmapWidth.toInt(), starBitmapHeight.toInt())
        invalidate()
    }

    /**
     * Set star orientation.
     *
     * @since 0.5.3
     */
    fun setStarOrientation(starOrientation: StarOrientation) {
        _starOrientation = starOrientation
        requestLayout()
    }

    /**
     * Sets the listener to be called when the rating changes.
     *
     * @since 0.5.6
     */
    fun setOnStarRatingChangeListener(listener: OnStarRatingChangeListener?) {
        this.listener = listener
    }

    init {
        context.withStyledAttributes(attrs, R.styleable.RatingView, defStyleAttr, defStyleRes) {
            _starIntervalWidth = getDimension(R.styleable.RatingView_star_interval_width, DEFAULT_STAR_INTERVAL_WIDTH)
            starBitmapWidth = getDimension(R.styleable.RatingView_star_width, DEFAULT_STAR_BITMAP_WIDTH)
            starBitmapHeight = getDimension(R.styleable.RatingView_star_height, DEFAULT_STAR_BITMAP_HEIGHT)
            _starCountNumber = getInt(R.styleable.RatingView_star_count, DEFAULT_STAR_COUNT)
            _starSelectMethod = when (getInt(R.styleable.RatingView_star_select_method, DEFAULT_SELECT_METHOD)
                .also { Log.d("Test", "starSelectMethod=$it") }) {
                StarSelectMethod.Unable.ordinal -> StarSelectMethod.Unable
                StarSelectMethod.Click.ordinal -> StarSelectMethod.Click
                StarSelectMethod.Sliding.ordinal -> StarSelectMethod.Sliding
                else -> StarSelectMethod.Unable
            }
            val rating = getFloat(R.styleable.RatingView_star_rating, DEFAULT_RATING).coerceIn(0f, 1f)
            _rating = if (_starSelectMethod == StarSelectMethod.Click) {
                round(_starCountNumber * rating) / _starCountNumber
            } else {
                rating
            }
            selectedBitmap = BmpUtils
                .getBitmapFromDrawable(getResourceId(R.styleable.RatingView_star_selected, R.drawable.ic_star_default_selected), context)
            _selectedBitmap = BmpUtils
                .scaleBitmap(selectedBitmap, starBitmapWidth.toInt(), starBitmapHeight.toInt())
            unselectedBitmap = BmpUtils
                .getBitmapFromDrawable(getResourceId(R.styleable.RatingView_star_unselected, R.drawable.ic_star_default_unselected), context)
            _unselectedBitmap = BmpUtils
                .scaleBitmap(unselectedBitmap, starBitmapWidth.toInt(), starBitmapHeight.toInt())
            _starOrientation = when (getInt(R.styleable.RatingView_star_orientation, DEFAULT_ORIENTATION)) {
                StarOrientation.Horizontal.ordinal -> StarOrientation.Horizontal
                StarOrientation.Vertical.ordinal -> StarOrientation.Vertical
                else -> StarOrientation.Horizontal
            }
        }
    }
}