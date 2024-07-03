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

package com.ave.vastgui.tools.view.alphabetsidebar

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PointF
import android.graphics.RectF
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.IntDef
import androidx.annotation.Size
import androidx.annotation.StyleRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.withSave
import com.ave.vastgui.core.extension.nothing_to_do
import com.ave.vastgui.tools.R
import com.ave.vastgui.tools.utils.DensityUtils.DP
import kotlin.math.floor
import kotlin.math.sqrt

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/9/28
// Documentation: https://ave.entropy2020.cn/documents/VastTools/core-topics/ui/alphabetsidebar/alphabetsidebar/

/**
 * AlphabetSideBar.
 *
 * @property mPreviousIndicatorIndex Used to save the index of previous
 *     indicator letter in mAlphabet. Prevent mLetterListener from being
 *     called repeatedly.
 * @property mIndicatorIndex The index of the current indicator letter in
 *     mAlphabet.
 * @property mBackgroundColor The color-int of alphabet sidebar, bezier
 *     curve and bubble.
 * @property mBarTextSize The size of letter in the alphabet sidebar.
 * @property mBarTextColor The color-int of letter in the alphabet sidebar.
 * @property mBarIndicatorTextColor The color-int of current indicator
 *     letter in the alphabet sidebar.
 * @property mBubbleTextSize The size of letter in the bubble.
 * @property mBubbleTextColor The color-int of letter in the bubble.
 * @since 0.5.4
 */
class AlphabetSideBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.Default_AlphabetSideBar_Style,
    @StyleRes defStyleRes: Int = R.style.BaseAlphabetSideBar
) : View(context, attrs, defStyleAttr, defStyleRes) {

    companion object {
        const val LEFT = 0
        const val RIGHT = 1
    }

    @IntDef(flag = true, value = [LEFT, RIGHT])
    @Retention(AnnotationRetention.SOURCE)
    annotation class LOCATION

    private val mDefaultLocation: Int =
        resources.getInteger(R.integer.default_alphabetsidebar_location)
    private val mDefaultBarTextSize: Float =
        resources.getDimension(R.dimen.default_alphabet_sidebar_text_size)
    private val mDefaultBubbleTextSize: Float =
        resources.getDimension(R.dimen.default_alphabet_sidebar_bubble_text_size)

    /**
     * Interface definition for a callback to be invoked when the indicator
     * letters is updated.
     *
     * @since 0.5.4
     */
    interface LetterListener {
        /**
         * Called when the indicator letter is updated.
         *
         * @param letter The indicator letter.
         * @param index The index of indicator letter in [mAlphabet].
         * @param target The target index of the indicator letter.
         * @since 0.5.4
         */
        fun onIndicatorLetterUpdate(@Size(value = 1) letter: String, index: Int, target: Int)

        /**
         * Called when the target index of the indicator letter is updated.
         *
         * @param letter The indicator letter.
         * @param target The target index of the indicator letter.
         * @since 0.5.4
         */
        fun onIndicatorLetterTargetUpdate(@Size(value = 1) letter: String, target: Int) {
            return
        }
    }

    private val mAlphabet = listOf(
        "❤" to AlphabetSp::Favorite,
        "A" to AlphabetSp::A,
        "B" to AlphabetSp::B,
        "C" to AlphabetSp::C,
        "D" to AlphabetSp::D,
        "E" to AlphabetSp::E,
        "F" to AlphabetSp::F,
        "G" to AlphabetSp::G,
        "H" to AlphabetSp::H,
        "I" to AlphabetSp::I,
        "J" to AlphabetSp::J,
        "K" to AlphabetSp::K,
        "L" to AlphabetSp::L,
        "M" to AlphabetSp::M,
        "N" to AlphabetSp::N,
        "O" to AlphabetSp::O,
        "P" to AlphabetSp::P,
        "Q" to AlphabetSp::Q,
        "R" to AlphabetSp::R,
        "S" to AlphabetSp::S,
        "T" to AlphabetSp::T,
        "U" to AlphabetSp::U,
        "V" to AlphabetSp::V,
        "W" to AlphabetSp::W,
        "X" to AlphabetSp::X,
        "Y" to AlphabetSp::Y,
        "Z" to AlphabetSp::Z,
        "#" to AlphabetSp::Other
    )

    private val mBackgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }
    private val mBarTextPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textAlign = Paint.Align.CENTER
    }
    private val mBarIndicatorTextPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textAlign = Paint.Align.CENTER
        typeface = Typeface.DEFAULT_BOLD
    }
    private val mBubbleTextPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textAlign = Paint.Align.CENTER
        typeface = Typeface.DEFAULT_BOLD
    }

    // The control point of bezier curve above the part of the connection
    // between the bubble and the sidebar is located at the sidebar.
    private val mPointA = PointF()

    // The control point of bezier curve below the part of the connection
    // between the bubble and the sidebar is located at the sidebar.
    private val mPointB = PointF()

    // The control point of bezier curve. Located on the vertical
    // line from the center of the bubble circle to the sidebar.
    private val mPointC = PointF()

    // The control point of bezier curve above the part of the connection
    // between the bubble and the sidebar is located at the bubble.
    private val mPointD = PointF()

    // The control point of Bezier curve below the part of the connection
    // between the bubble and the sidebar is located at the bubble.
    private val mPointE = PointF()
    private val mBezierPath = Path()

    // The RectF of the sidebar round rectangle background.
    private val mBackgroundRectF = RectF()
    private val mCurrentTouchPointF = PointF(0f, 0f)

    private val mBubbleRadius = 30f.DP
    private val mBubbleDistance = 45f.DP
    private val mTextHorizontalMargin = 8f.DP

    private var mLetterListener: LetterListener? = null

    private val mIndexCount = mAlphabet.size

    private var mPreviousIndicatorIndex = -2
    private var mIndicatorIndex = -1

    @get:LOCATION
    var mLocation: Int = LEFT
        private set

    var mBackgroundColor: Int
        set(value) {
            mBackgroundPaint.color = value
        }
        get() = mBackgroundPaint.color

    var mBarTextSize: Float
        set(value) {
            if (value < 0f) return
            mBarTextPaint.textSize = value
            mBarIndicatorTextPaint.textSize = value
        }
        get() = mBarTextPaint.textSize

    var mBarTextColor: Int
        set(value) {
            mBarTextPaint.color = value
        }
        get() = mBarTextPaint.color

    var mBarIndicatorTextColor: Int
        set(value) {
            mBarIndicatorTextPaint.color = value
        }
        get() = mBarIndicatorTextPaint.color

    var mBubbleTextSize: Float
        set(value) {
            if (value < 0f) return
            mBubbleTextPaint.textSize = value
        }
        get() = mBubbleTextPaint.textSize

    var mBubbleTextColor: Int
        set(value) {
            mBubbleTextPaint.color = value
        }
        get() = mBubbleTextPaint.color

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = resolveSize(
            (getBarWidth() + mBubbleRadius + mBubbleDistance).toInt(),
            widthMeasureSpec
        )
        val height = resolveSize(
            (getBarTextHeight() * mAlphabet.size + getBarWidth() + 2 * getBarBeyondHeight()).toInt(),
            heightMeasureSpec
        )
        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas) {
        val textWidth = getBarWidth()
        val textHeight = getBarTextHeight()
        val radius = getBarCircleRadius()
        val textTop = radius + getBarBeyondHeight()
        val textBottom = textTop + mIndexCount * textHeight
        when (mLocation) {
            LEFT ->
                mBackgroundRectF.set(
                    0f,
                    textTop - radius,
                    textWidth,
                    textBottom + radius
                )

            RIGHT ->
                mBackgroundRectF.set(
                    measuredWidth - textWidth,
                    textTop - radius,
                    measuredWidth.toFloat(),
                    textBottom + radius
                )
        }
        canvas.drawRoundRect(mBackgroundRectF, radius, radius, mBackgroundPaint)
        val textX = if (mLocation == LEFT) textWidth / 2f else measuredWidth - textWidth / 2f
        canvas.withSave {
            if (mLocation == LEFT) {
                clipRect(0f, textTop, textWidth, textBottom)
            } else {
                clipRect(measuredWidth - textWidth, textTop, measuredWidth.toFloat(), textBottom)
            }
            mAlphabet.forEachIndexed { index, letter ->
                drawText(
                    letter.first, textX,
                    textTop + index * textHeight + textHeight / 2f + getBarTextBaseLine(),
                    if (index == mIndicatorIndex) mBarIndicatorTextPaint else mBarTextPaint
                )
            }
        }
        if (!checkIsIndexValid()) return
        // The length of the bubble and the adhesion part of the letter list is based on
        // the central axis of the letter, and takes 30% of the height of the letter upward
        // and downward respectively.
        // For example, if currentIndex is 2, the y of mPointA is (textTop + 2.2 * textHeight),
        // the y of mPointB is (textTop + 2.8 * textHeight).
        val barBezierX =
            if (mLocation == LEFT) textWidth else measuredWidth - textWidth
        mPointA.set(barBezierX, textTop + (mIndicatorIndex + 0.2f) * textHeight)
        mPointB.set(barBezierX, textTop + (mIndicatorIndex + 0.8f) * textHeight)
        // The height of the control point of the bezier curve on the bubble corresponding to
        // the central axis of the letter is also 30% of the height of the letter.
        val distance = sqrt(
            mBubbleRadius * mBubbleRadius -
                    (0.3 * textHeight * 0.3 * textHeight)
        )
        // 2.33dp is the offset of the control point of the Bezier curve on the
        // vertical line from the center of the bubble circle to the sidebar.
        val verticalLineBezierX =
            if (mLocation == LEFT) textWidth + (mBubbleDistance - distance.toFloat()) / 2f + 2.33f.DP
            else measuredWidth - textWidth - (mBubbleDistance - distance.toFloat()) / 2f - 2.33f.DP
        mPointC.set(verticalLineBezierX, textTop + (mIndicatorIndex + 0.5f) * textHeight)
        val bubbleBezierX =
            if (mLocation == LEFT) textWidth + mBubbleDistance - distance.toFloat()
            else measuredWidth - textWidth - mBubbleDistance + distance.toFloat()
        mPointD.set(bubbleBezierX, textTop + (mIndicatorIndex + 0.2f) * textHeight)
        mPointE.set(bubbleBezierX, textTop + (mIndicatorIndex + 0.8f) * textHeight)
        mBezierPath.reset()
        mBezierPath.moveTo(mPointA.x, mPointA.y)
        mBezierPath.quadTo(mPointC.x, mPointC.y, mPointD.x, mPointD.y)
        mBezierPath.lineTo(mPointE.x, mPointE.y)
        mBezierPath.quadTo(mPointC.x, mPointC.y, mPointB.x, mPointB.y)
        mBezierPath.close()
        canvas.drawPath(mBezierPath, mBackgroundPaint)
        val bubbleX =
            if (mLocation == LEFT) textWidth + mBubbleDistance
            else measuredWidth - textWidth - mBubbleDistance
        canvas.drawCircle(
            bubbleX,
            textTop + (mIndicatorIndex + 0.5f) * textHeight,
            mBubbleRadius,
            mBackgroundPaint
        )
        canvas.drawText(
            mAlphabet[mIndicatorIndex].first,
            bubbleX,
            textTop + (mIndicatorIndex + 0.5f) * textHeight + getBubbleTextBaseLine(),
            mBubbleTextPaint
        )
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                // 10dp is the redundant range of the finger detection area.
                if ((event.x > getBarWidth() + 10f.DP ||
                            event.x < (-10f).DP ||
                            event.y < 0f ||
                            event.y > measuredHeight) &&
                    mLocation == LEFT
                ) {
                    mIndicatorIndex = -1
                    return super.onTouchEvent(event)
                } else if ((event.x < measuredWidth - getBarWidth() - 10f.DP ||
                            event.x > measuredWidth + 10f.DP ||
                            event.y < 0 ||
                            event.y > measuredHeight) &&
                    mLocation == RIGHT
                ) {
                    mIndicatorIndex = -1
                    return super.onTouchEvent(event)
                }
                mCurrentTouchPointF.set(event.x, event.y)
                invalidate()
            }

            MotionEvent.ACTION_MOVE -> {
                mCurrentTouchPointF.set(event.x, event.y)
                mIndicatorIndex = getIndicatorLetterIndex()
                if (event.y >= top + getBarBeyondHeight() + getBarCircleRadius() &&
                    event.y <= bottom - getBarBeyondHeight() - getBarCircleRadius() &&
                    checkIsIndexValid()
                ) {
                    if (mPreviousIndicatorIndex != mIndicatorIndex) {
                        mPreviousIndicatorIndex = mIndicatorIndex
                        mLetterListener?.onIndicatorLetterUpdate(
                            mAlphabet[mIndicatorIndex].first,
                            mIndicatorIndex,
                            mAlphabet[mIndicatorIndex].second.get(),
                        )
                    }
                    invalidate()
                } else {
                    mIndicatorIndex = -1
                    mCurrentTouchPointF.set(0f, 0f)
                }
            }

            MotionEvent.ACTION_UP -> {
                mIndicatorIndex = -1
                mPreviousIndicatorIndex = -2
                mCurrentTouchPointF.set(0f, 0f)
                performClick()
                invalidate()
            }
        }
        return true
    }

    override fun performClick(): Boolean {
        nothing_to_do()
        return super.performClick()
    }

    /**
     * Set [mLocation].
     *
     * @since 0.5.4
     */
    fun setLocation(@LOCATION location: Int) {
        mLocation = location
    }

    /**
     * Set favorite icon string.
     *
     * @since 0.5.4
     */
    fun setFavoriteIcon(@Size(value = 1) favicon: String) {
        mAlphabet.toMutableList()[0] = favicon to mAlphabet[0].second
        mAlphabet.toList()
    }

    /**
     * Register a [LetterListener] for the [AlphabetSideBar].
     *
     * @param listener If null, it will remove the listener.
     * @since 0.5.4
     */
    fun setLetterListener(listener: LetterListener? = null) {
        mLetterListener = listener
    }

    /**
     * Set target index of the [alphabet] by [targetIndex].
     * If [mLetterListener] is not null, it will also call
     * [LetterListener.onIndicatorLetterTargetUpdate].
     *
     * @since 0.5.6
     */
    fun setIndicatorLetterTargetIndex(@Size(value = 1) alphabet: Alphabet, targetIndex: Int) {
        val index = mAlphabet.indexOfFirst { it.first == alphabet.letter }
        if (index == -1) return
        mAlphabet[index].second.set(targetIndex)
        mLetterListener?.onIndicatorLetterTargetUpdate(mAlphabet[index].first, targetIndex)
    }

    /**
     * Get the width of alphabet side bar.
     *
     * @since 0.5.4
     */
    private fun getBarWidth(): Float =
        mBarTextPaint.measureText("A") + mTextHorizontalMargin * 2

    /**
     * Returns the radius of the top and bottom semicircles of the sidebar.
     *
     * @since 0.5.4
     */
    private fun getBarCircleRadius(): Float =
        getBarWidth() / 2f

    /**
     * Returns the difference between the distance from the first bubble to the
     * top of the view and the distance from the sidebar to the top of the view
     *
     * @since 0.5.4
     */
    private fun getBarBeyondHeight(): Float {
        return mBubbleRadius - getBarTextHeight() / 2f - getBarWidth() / 2f
    }

    /**
     * Return the height of the letter in the alphabet sidebar.
     *
     * @since 0.5.4
     */
    private fun getBarTextHeight(): Float {
        val fontMetrics = mBarTextPaint.fontMetrics
        return fontMetrics.bottom - fontMetrics.top
    }

    /**
     * Return the distance length between 50% of the letter height which is in
     * the alphabet sidebar and the baseline. Used to ensure that the drawn
     * text is in the center.
     *
     * @since 0.5.4
     */
    private fun getBarTextBaseLine(): Float {
        val fontMetrics = mBarTextPaint.fontMetrics
        val height = fontMetrics.bottom - fontMetrics.top
        return height / 2 - fontMetrics.bottom
    }

    /**
     * Return the distance length between 50% of the letter height which is in
     * the bubble and the baseline. Used to ensure that the drawn text is in
     * the center.
     *
     * @since 0.5.4
     */
    private fun getBubbleTextBaseLine(): Float {
        val fontMetrics = mBubbleTextPaint.fontMetrics
        val height = fontMetrics.bottom - fontMetrics.top
        return height / 2 - fontMetrics.bottom
    }

    /**
     * Get index of current indicator letter in [mAlphabet].
     *
     * @since 0.5.4
     */
    private fun getIndicatorLetterIndex(): Int =
        (((mCurrentTouchPointF.y - top - getBarBeyondHeight() - getBarCircleRadius()) / getBarTextHeight())
            .takeIf { it >= 0 }?.let { floor(it) } ?: -1f).toInt()

    private fun checkIsIndexValid(): Boolean = mIndicatorIndex in 0..27

    init {
        val typeArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.AlphabetSideBar,
            defStyleAttr,
            defStyleRes
        )
        mLocation =
            typeArray.getInt(R.styleable.AlphabetSideBar_alphabetsidebar_location, mDefaultLocation)
        mBackgroundColor =
            typeArray.getColor(
                R.styleable.AlphabetSideBar_alphabetsidebar_background,
                ContextCompat.getColor(context, R.color.md_theme_primaryContainer)
            )
        mBarTextPaint.textSize =
            typeArray.getDimension(
                R.styleable.AlphabetSideBar_alphabetsidebar_text_size,
                mDefaultBarTextSize
            )
        mBarIndicatorTextPaint.textSize =
            typeArray.getDimension(
                R.styleable.AlphabetSideBar_alphabetsidebar_text_size,
                mDefaultBarTextSize
            )
        mBarTextPaint.color =
            typeArray.getColor(
                R.styleable.AlphabetSideBar_alphabetsidebar_text_color,
                ContextCompat.getColor(context, R.color.md_theme_outlineVariant)
            )
        mBarIndicatorTextPaint.color =
            typeArray.getColor(
                R.styleable.AlphabetSideBar_alphabetsidebar_indicator_text_color,
                ContextCompat.getColor(context, R.color.md_theme_error)
            )
        mBubbleTextPaint.textSize =
            typeArray.getDimension(
                R.styleable.AlphabetSideBar_alphabetsidebar_bubble_text_size,
                mDefaultBubbleTextSize
            )
        mBubbleTextPaint.color =
            typeArray.getColor(
                R.styleable.AlphabetSideBar_alphabetsidebar_bubble_text_color,
                ContextCompat.getColor(context, R.color.md_theme_error)
            )
        typeArray.recycle()
    }

}