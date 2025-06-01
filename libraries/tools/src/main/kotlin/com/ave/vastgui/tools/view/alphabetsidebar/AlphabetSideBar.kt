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
import androidx.core.graphics.withSave
import com.ave.vastgui.core.extension.nothing_to_do
import com.ave.vastgui.tools.R
import com.ave.vastgui.tools.utils.DensityUtils.DP
import kotlin.math.floor
import kotlin.math.sqrt
import androidx.core.content.withStyledAttributes
import com.ave.vastgui.tools.utils.color
import com.ave.vastgui.tools.utils.dimension
import com.ave.vastgui.tools.utils.integer

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/9/28
// Documentation: https://sakurajimamaii.github.io/AVE-DOC/documents/tools/core-topics/ui/alphabetsidebar/alphabetsidebar/

/**
 * AlphabetSideBar.
 *
 * @property previousIndicatorIndex Used to save the index of previous
 * indicator letter in mAlphabet. Prevent mLetterListener from being called
 * repeatedly.
 * @property indicatorIndex The index of the current indicator letter in
 * mAlphabet.
 * @property barBackgroundColor The color-int of alphabet sidebar, bezier
 * curve and bubble.
 * @property barTextSize The size of letter in the alphabet sidebar.
 * @property barTextColor The color-int of letter in the alphabet sidebar.
 * @property barIndicatorTextColor The color-int of current indicator
 * letter in the alphabet sidebar.
 * @property bubbleTextSize The size of letter in the bubble.
 * @property bubbleTextColor The color-int of letter in the bubble.
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
    annotation class Location

    /** @since 1.5.2 */
    @Suppress("PrivatePropertyName")
    private val DEFAULT_LOCATION: Int = integer(R.integer.default_alphabetsidebar_location)

    /** @since 1.5.2 */
    @Suppress("PrivatePropertyName")
    private val DEFAULT_BAR_TEXT_SIZE: Float = dimension(R.dimen.default_alphabet_sidebar_text_size)

    /** @since 1.5.2 */
    @Suppress("PrivatePropertyName")
    private val DEFAULT_BUBBLE_TEXT_SIZE: Float = dimension(R.dimen.default_alphabet_sidebar_bubble_text_size)

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
         * @param index The index of indicator letter in [alphabet].
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

    /** @since 1.5.2 */
    private val alphabet = listOf(
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

    /** @since 1.5.2 */
    private val backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }

    /** @since 1.5.2 */
    private val barTextPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textAlign = Paint.Align.CENTER
    }

    /** @since 1.5.2 */
    private val barIndicatorTextPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textAlign = Paint.Align.CENTER
        typeface = Typeface.DEFAULT_BOLD
    }

    /** @since 1.5.2 */
    private val bubbleTextPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textAlign = Paint.Align.CENTER
        typeface = Typeface.DEFAULT_BOLD
    }

    /**
     * The control point of bezier curve above the part of the connection
     * between the bubble and the sidebar is located at the sidebar.
     *
     * @since 1.5.2
     */
    private val pointA = PointF()

    /**
     * The control point of bezier curve below the part of the connection
     * between the bubble and the sidebar is located at the sidebar.
     *
     * @since 1.5.2
     */
    private val pointB = PointF()

    /**
     * The control point of bezier curve. Located on the vertical line from the
     * center of the bubble circle to the sidebar.
     *
     * @since 1.5.2
     */
    private val pointC = PointF()

    /**
     * The control point of bezier curve above the part of the connection
     * between the bubble and the sidebar is located at the bubble.
     *
     * @since 1.5.2
     */
    private val pointD = PointF()

    /**
     * The control point of Bezier curve below the part of the connection
     * between the bubble and the sidebar is located at the bubble.
     *
     * @since 1.5.2
     */
    private val pointE = PointF()

    /** @since 1.5.2 */
    private val bezierPath = Path()

    /**
     * The RectF of the sidebar round rectangle background.
     *
     * @since 1.5.2
     */
    private val backgroundRectF = RectF()

    /** @since 1.5.2 */
    private val currentTouchPointF = PointF(0f, 0f)

    /** @since 1.5.2 */
    private val bubbleRadius = 30f.DP

    /** @since 1.5.2 */
    private val bubbleDistance = 45f.DP

    /** @since 1.5.2 */
    private val textHorizontalMargin = 8f.DP

    /** @since 1.5.2 */
    private var letterListener: LetterListener? = null

    /** @since 1.5.2 */
    private val indexCount = alphabet.size

    /** @since 1.5.2 */
    private var previousIndicatorIndex = -2

    /** @since 1.5.2 */
    private var indicatorIndex = -1

    /** @since 1.5.2 */
    @get:Location
    var location: Int = LEFT
        private set

    /** @since 1.5.2 */
    var barBackgroundColor: Int
        set(value) {
            backgroundPaint.color = value
        }
        get() = backgroundPaint.color

    /** @since 1.5.2 */
    var barTextSize: Float
        set(value) {
            if (value < 0f) return
            barTextPaint.textSize = value
            barIndicatorTextPaint.textSize = value
        }
        get() = barTextPaint.textSize

    /** @since 1.5.2 */
    var barTextColor: Int
        set(value) {
            barTextPaint.color = value
        }
        get() = barTextPaint.color

    /** @since 1.5.2 */
    var barIndicatorTextColor: Int
        set(value) {
            barIndicatorTextPaint.color = value
        }
        get() = barIndicatorTextPaint.color

    /** @since 1.5.2 */
    var bubbleTextSize: Float
        set(value) {
            if (value < 0f) return
            bubbleTextPaint.textSize = value
        }
        get() = bubbleTextPaint.textSize

    /** @since 1.5.2 */
    var bubbleTextColor: Int
        set(value) {
            bubbleTextPaint.color = value
        }
        get() = bubbleTextPaint.color

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = resolveSize(
            (getBarWidth() + bubbleRadius + bubbleDistance).toInt(),
            widthMeasureSpec
        )
        val height = resolveSize(
            (getBarTextHeight() * alphabet.size + getBarWidth() + 2 * getBarBeyondHeight()).toInt(),
            heightMeasureSpec
        )
        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas) {
        val textWidth = getBarWidth()
        val textHeight = getBarTextHeight()
        val radius = getBarCircleRadius()
        val textTop = radius + getBarBeyondHeight()
        val textBottom = textTop + indexCount * textHeight
        when (location) {
            LEFT ->
                backgroundRectF.set(
                    0f,
                    textTop - radius,
                    textWidth,
                    textBottom + radius
                )

            RIGHT ->
                backgroundRectF.set(
                    measuredWidth - textWidth,
                    textTop - radius,
                    measuredWidth.toFloat(),
                    textBottom + radius
                )
        }
        canvas.drawRoundRect(backgroundRectF, radius, radius, backgroundPaint)
        val textX = if (location == LEFT) textWidth / 2f else measuredWidth - textWidth / 2f
        canvas.withSave {
            if (location == LEFT) {
                clipRect(0f, textTop, textWidth, textBottom)
            } else {
                clipRect(measuredWidth - textWidth, textTop, measuredWidth.toFloat(), textBottom)
            }
            alphabet.forEachIndexed { index, letter ->
                drawText(
                    letter.first, textX,
                    textTop + index * textHeight + textHeight / 2f + getBarTextBaseLine(),
                    if (index == indicatorIndex) barIndicatorTextPaint else barTextPaint
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
            if (location == LEFT) textWidth else measuredWidth - textWidth
        pointA.set(barBezierX, textTop + (indicatorIndex + 0.2f) * textHeight)
        pointB.set(barBezierX, textTop + (indicatorIndex + 0.8f) * textHeight)
        // The height of the control point of the bezier curve on the bubble corresponding to
        // the central axis of the letter is also 30% of the height of the letter.
        val distance = sqrt(
            bubbleRadius * bubbleRadius -
                    (0.3 * textHeight * 0.3 * textHeight)
        )
        // 2.33dp is the offset of the control point of the Bezier curve on the
        // vertical line from the center of the bubble circle to the sidebar.
        val verticalLineBezierX =
            if (location == LEFT) textWidth + (bubbleDistance - distance.toFloat()) / 2f + 2.33f.DP
            else measuredWidth - textWidth - (bubbleDistance - distance.toFloat()) / 2f - 2.33f.DP
        pointC.set(verticalLineBezierX, textTop + (indicatorIndex + 0.5f) * textHeight)
        val bubbleBezierX =
            if (location == LEFT) textWidth + bubbleDistance - distance.toFloat()
            else measuredWidth - textWidth - bubbleDistance + distance.toFloat()
        pointD.set(bubbleBezierX, textTop + (indicatorIndex + 0.2f) * textHeight)
        pointE.set(bubbleBezierX, textTop + (indicatorIndex + 0.8f) * textHeight)
        bezierPath.reset()
        bezierPath.moveTo(pointA.x, pointA.y)
        bezierPath.quadTo(pointC.x, pointC.y, pointD.x, pointD.y)
        bezierPath.lineTo(pointE.x, pointE.y)
        bezierPath.quadTo(pointC.x, pointC.y, pointB.x, pointB.y)
        bezierPath.close()
        canvas.drawPath(bezierPath, backgroundPaint)
        val bubbleX =
            if (location == LEFT) textWidth + bubbleDistance
            else measuredWidth - textWidth - bubbleDistance
        canvas.drawCircle(
            bubbleX,
            textTop + (indicatorIndex + 0.5f) * textHeight,
            bubbleRadius,
            backgroundPaint
        )
        canvas.drawText(
            alphabet[indicatorIndex].first,
            bubbleX,
            textTop + (indicatorIndex + 0.5f) * textHeight + getBubbleTextBaseLine(),
            bubbleTextPaint
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
                    location == LEFT
                ) {
                    indicatorIndex = -1
                    return super.onTouchEvent(event)
                } else if ((event.x < measuredWidth - getBarWidth() - 10f.DP ||
                            event.x > measuredWidth + 10f.DP ||
                            event.y < 0 ||
                            event.y > measuredHeight) &&
                    location == RIGHT
                ) {
                    indicatorIndex = -1
                    return super.onTouchEvent(event)
                }
                currentTouchPointF.set(event.x, event.y)
                invalidate()
            }

            MotionEvent.ACTION_MOVE -> {
                currentTouchPointF.set(event.x, event.y)
                indicatorIndex = getIndicatorLetterIndex()
                if (event.y >= top + getBarBeyondHeight() + getBarCircleRadius() &&
                    event.y <= bottom - getBarBeyondHeight() - getBarCircleRadius() &&
                    checkIsIndexValid()
                ) {
                    if (previousIndicatorIndex != indicatorIndex) {
                        previousIndicatorIndex = indicatorIndex
                        letterListener?.onIndicatorLetterUpdate(
                            alphabet[indicatorIndex].first,
                            indicatorIndex,
                            alphabet[indicatorIndex].second.get(),
                        )
                    }
                    invalidate()
                } else {
                    indicatorIndex = -1
                    currentTouchPointF.set(0f, 0f)
                }
            }

            MotionEvent.ACTION_UP -> {
                indicatorIndex = -1
                previousIndicatorIndex = -2
                currentTouchPointF.set(0f, 0f)
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
     * Set [location].
     *
     * @since 0.5.4
     */
    fun setLocation(@Location location: Int) {
        this@AlphabetSideBar.location = location
    }

    /**
     * Set favorite icon string.
     *
     * @since 0.5.4
     */
    fun setFavoriteIcon(@Size(value = 1) favicon: String) {
        alphabet.toMutableList()[0] = favicon to alphabet[0].second
        alphabet.toList()
    }

    /**
     * Register a [LetterListener] for the [AlphabetSideBar].
     *
     * @param listener If null, it will remove the listener.
     * @since 0.5.4
     */
    fun setLetterListener(listener: LetterListener? = null) {
        letterListener = listener
    }

    /**
     * Set target index of the [alphabet] by [targetIndex].
     * If [letterListener] is not null, it will also call
     * [LetterListener.onIndicatorLetterTargetUpdate].
     *
     * @since 0.5.6
     */
    fun setIndicatorLetterTargetIndex(@Size(value = 1) alphabet: Alphabet, targetIndex: Int) {
        val index = this.alphabet.indexOfFirst { it.first == alphabet.letter }
        if (index == -1) return
        this@AlphabetSideBar.alphabet[index].second.set(targetIndex)
        letterListener?.onIndicatorLetterTargetUpdate(this@AlphabetSideBar.alphabet[index].first, targetIndex)
    }

    /**
     * Get the width of alphabet side bar.
     *
     * @since 0.5.4
     */
    private fun getBarWidth(): Float =
        barTextPaint.measureText("A") + textHorizontalMargin * 2

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
        return bubbleRadius - getBarTextHeight() / 2f - getBarWidth() / 2f
    }

    /**
     * Return the height of the letter in the alphabet sidebar.
     *
     * @since 0.5.4
     */
    private fun getBarTextHeight(): Float {
        val fontMetrics = barTextPaint.fontMetrics
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
        val fontMetrics = barTextPaint.fontMetrics
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
        val fontMetrics = bubbleTextPaint.fontMetrics
        val height = fontMetrics.bottom - fontMetrics.top
        return height / 2 - fontMetrics.bottom
    }

    /**
     * Get index of current indicator letter in [alphabet].
     *
     * @since 0.5.4
     */
    private fun getIndicatorLetterIndex(): Int =
        (((currentTouchPointF.y - top - getBarBeyondHeight() - getBarCircleRadius()) / getBarTextHeight())
            .takeIf { it >= 0 }?.let { floor(it) } ?: -1f).toInt()

    private fun checkIsIndexValid(): Boolean = indicatorIndex in 0..27

    init {
        context.withStyledAttributes(attrs, R.styleable.AlphabetSideBar, defStyleAttr, defStyleRes) {
            location = getInt(R.styleable.AlphabetSideBar_alphabetsidebar_location, DEFAULT_LOCATION)
            barBackgroundColor = getColor(R.styleable.AlphabetSideBar_alphabetsidebar_background, context.color(R.color.md_theme_primaryContainer))
            barTextPaint.textSize = getDimension(R.styleable.AlphabetSideBar_alphabetsidebar_text_size, DEFAULT_BAR_TEXT_SIZE)
            barIndicatorTextPaint.textSize = getDimension(R.styleable.AlphabetSideBar_alphabetsidebar_text_size, DEFAULT_BAR_TEXT_SIZE)
            barTextPaint.color = getColor(R.styleable.AlphabetSideBar_alphabetsidebar_text_color, color(R.color.md_theme_outlineVariant))
            barIndicatorTextPaint.color = getColor(R.styleable.AlphabetSideBar_alphabetsidebar_indicator_text_color, color(R.color.md_theme_error))
            bubbleTextPaint.textSize = getDimension(R.styleable.AlphabetSideBar_alphabetsidebar_bubble_text_size, DEFAULT_BUBBLE_TEXT_SIZE)
            bubbleTextPaint.color = getColor(R.styleable.AlphabetSideBar_alphabetsidebar_bubble_text_color, color(R.color.md_theme_error))
        }
    }

}