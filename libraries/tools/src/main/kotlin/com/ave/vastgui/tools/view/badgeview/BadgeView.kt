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

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.PointFEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PointF
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.LinearInterpolator
import android.view.animation.OvershootInterpolator
import androidx.annotation.ColorInt
import androidx.annotation.FloatRange
import androidx.annotation.IntRange
import androidx.annotation.StyleRes
import com.ave.vastgui.core.extension.NotNUllVar
import com.ave.vastgui.core.extension.cast
import com.ave.vastgui.core.extension.nothing_to_do
import com.ave.vastgui.tools.R
import com.ave.vastgui.tools.utils.DensityUtils.DP
import com.ave.vastgui.tools.utils.DensityUtils.SP
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.hypot
import androidx.core.content.withStyledAttributes
import androidx.core.graphics.drawable.toBitmap
import com.ave.vastgui.tools.utils.ColorUtils
import com.ave.vastgui.tools.utils.color
import com.ave.vastgui.tools.utils.drawable

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/9/11
// Documentation: https://sakurajimamaii.github.io/AVE-DOC/documents/tools/core-topics/ui/badge/description/

/**
 * Badge View.
 *
 * @property minOffsetDistance When the coordinate of first touch is
 * smaller than mMoveRadius + MIN_OFFSET_DISTANCE, it means you touched the
 * starting red dot.
 * @since 0.5.3
 */
class BadgeView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.Default_BadgeView_Style,
    @StyleRes defStyleRes: Int = R.style.BaseBadgeView
) : View(context, attrs, defStyleAttr, defStyleRes) {

    /** @since 1.5.2 */
    private val minOffsetDistance = 5F.DP

    /** @since 1.5.2 */
    private var fixedRadius = INIT_RADIUS

    /** @since 1.5.2 */
    private val fixedPoint = PointF()

    /** @since 1.5.2 */
    private var movePoint = PointF()

    /** @since 1.5.2 */
    private val controlPoint = PointF()

    /** @since 1.5.2 */
    private val pointA = PointF()

    /** @since 1.5.2 */
    private val pointB = PointF()

    /** @since 1.5.2 */
    private val pointC = PointF()

    /** @since 1.5.2 */
    private val pointD = PointF()

    /** @since 1.5.2 */
    private val bezierPath = Path()

    /** @since 1.5.2 */
    private val explosionRect: Rect = Rect()

    /** @since 1.5.2 */
    private var explosionBmp = listOf(
        drawable(R.drawable.badge_explode_1)!!.toBitmap(),
        drawable(R.drawable.badge_explode_2)!!.toBitmap(),
        drawable(R.drawable.badge_explode_3)!!.toBitmap(),
        drawable(R.drawable.badge_explode_4)!!.toBitmap(),
        drawable(R.drawable.badge_explode_5)!!.toBitmap())

    /** @since 1.5.2 */
    private var bmpIndex = 0

    /** @since 1.5.2 */
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textAlign = Paint.Align.CENTER
    }

    /** @since 1.5.2 */
    internal val badgePaint = Paint(Paint.ANTI_ALIAS_FLAG)

    /** @since 1.5.2 */
    private val bezierPaint = badgePaint.apply {
        style = Paint.Style.FILL
    }

    /** @since 1.5.2 */
    private val minimumRadius
        get() = when (badgeMode) {
            BadgeMode.BUBBLE.TEXT -> {
                text.getMinimumRadius()
            }

            BadgeMode.BUBBLE.NUMBER -> {
                getBubbleTextNumber().getMinimumRadius()
            }

            else -> 0f
        }

    /** @since 1.5.2 */
    var badgeState by NotNUllVar<BadgeState>()
        private set

    /** @since 1.5.2 */
    var badgeMode by NotNUllVar<BadgeMode>()
        internal set

    /** @since 1.5.2 */
    val badgeColor: Int
        get() = badgePaint.color

    /** @since 1.5.2 */
    var moveRadius = INIT_RADIUS
        private set

    /** @since 1.5.2 */
    var dotRadius = INIT_RADIUS
        private set

    /** @since 1.5.2 */
    var textNumber = INIT_NUMBER
        private set

    /** @since 1.5.2 */
    var textMaxNumber = INIT_NUMBER
        private set

    /** @since 1.5.2 */
    var text = INIT_TEXT
        private set

    /** @since 1.5.2 */
    val textSize
        get() = textPaint.textSize

    /** @since 1.5.2 */
    @get:ColorInt
    val textColor
        get() = textPaint.color

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (badgeMode == BadgeMode.UNSPECIFIED) {
            setMeasuredDimension(0, 0)
            return
        }
        val size = if (badgeMode is BadgeMode.DOT) {
            movePoint.x = dotRadius
            movePoint.y = dotRadius
            ceil(dotRadius * 2).toInt()
        } else {
            movePoint.x = moveRadius
            movePoint.y = moveRadius
            fixedPoint.x = fixedRadius
            fixedPoint.y = fixedRadius
            ceil(moveRadius * 2).coerceAtLeast(minimumRadius).toInt()
        }
        setMeasuredDimension(size, size)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        when (badgeMode) {
            BadgeMode.UNSPECIFIED -> {
                return
            }

            BadgeMode.DOT -> {
                if (badgeState == BadgeState.DOT.SHOW) {
                    canvas.drawCircle(movePoint.x, movePoint.y, dotRadius, badgePaint)
                }
            }

            BadgeMode.BUBBLE.TEXT -> {
                if (badgeState == BadgeState.BUBBLE.DEFAULT) {
                    if (fixedRadius != moveRadius) fixedRadius = moveRadius
                    canvas.drawCircle(fixedPoint.x, fixedPoint.y, fixedRadius, badgePaint)
                    canvas.drawText(
                        text,
                        fixedPoint.x,
                        fixedPoint.y + getTextBaseline(),
                        textPaint
                    )
                }
                if (badgeState == BadgeState.BUBBLE.CONNECT) {
                    canvas.drawBezier()
                    canvas.drawText(
                        text,
                        movePoint.x,
                        movePoint.y + getTextBaseline(),
                        textPaint
                    )
                }
                if (badgeState == BadgeState.BUBBLE.APART) {
                    canvas.drawCircle(movePoint.x, movePoint.y, moveRadius, badgePaint)
                    canvas.drawText(
                        text,
                        movePoint.x,
                        movePoint.y + getTextBaseline(),
                        textPaint
                    )
                }
                if (badgeState == BadgeState.BUBBLE.HIDE && bmpIndex != explosionBmp.size) {
                    explosionRect.left = (movePoint.x - moveRadius).toInt()
                    explosionRect.right = (movePoint.x + moveRadius).toInt()
                    explosionRect.top = (movePoint.y - moveRadius).toInt()
                    explosionRect.bottom = (movePoint.y + moveRadius).toInt()
                    canvas.drawBitmap(explosionBmp[bmpIndex], null, explosionRect, badgePaint)
                }
                if (badgeState == BadgeState.BUBBLE.HIDE && bmpIndex == explosionBmp.size) {
                    resetPoint()
                }
            }

            BadgeMode.BUBBLE.NUMBER -> {
                if (badgeState == BadgeState.BUBBLE.DEFAULT && textNumber != INIT_NUMBER) {
                    if (fixedRadius != moveRadius) fixedRadius = moveRadius
                    canvas.drawCircle(fixedPoint.x, fixedPoint.y, fixedRadius, badgePaint)
                    canvas.drawText(
                        getBubbleTextNumber(),
                        fixedPoint.x,
                        fixedPoint.y + getTextBaseline(),
                        textPaint
                    )
                }
                if (badgeState == BadgeState.BUBBLE.CONNECT) {
                    canvas.drawBezier()
                    canvas.drawText(
                        getBubbleTextNumber(),
                        movePoint.x,
                        movePoint.y + getTextBaseline(),
                        textPaint
                    )
                }
                if (badgeState == BadgeState.BUBBLE.APART) {
                    canvas.drawCircle(movePoint.x, movePoint.y, moveRadius, badgePaint)
                    canvas.drawText(
                        getBubbleTextNumber(),
                        movePoint.x,
                        movePoint.y + getTextBaseline(),
                        textPaint
                    )
                }
                if (badgeState == BadgeState.BUBBLE.HIDE && bmpIndex != explosionBmp.size) {
                    explosionRect.left = (movePoint.x - moveRadius).toInt()
                    explosionRect.right = (movePoint.x + moveRadius).toInt()
                    explosionRect.top = (movePoint.y - moveRadius).toInt()
                    explosionRect.bottom = (movePoint.y + moveRadius).toInt()
                    canvas.drawBitmap(explosionBmp[bmpIndex], null, explosionRect, badgePaint)
                }
                if (badgeState == BadgeState.BUBBLE.HIDE && bmpIndex == explosionBmp.size) {
                    resetPoint()
                }
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (badgeMode == BadgeMode.DOT)
            return super.onTouchEvent(event)
        if (badgeMode == BadgeMode.BUBBLE.NUMBER && textNumber == INIT_NUMBER)
            return super.onTouchEvent(event)
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                val mDistance = hypot(
                    (event.x - fixedPoint.x).toDouble(),
                    (event.y - fixedPoint.y).toDouble()
                )
                badgeState = if (mDistance <= moveRadius + minOffsetDistance) {
                    BadgeState.BUBBLE.CONNECT
                } else {
                    BadgeState.BUBBLE.DEFAULT
                }
            }

            MotionEvent.ACTION_MOVE -> {
                val mDistance = hypot(
                    (event.x - fixedPoint.x).toDouble(),
                    (event.y - fixedPoint.y).toDouble()
                )
                movePoint.set(event.x, event.y)
                if (moveRadius - mDistance / 15 >= 0.0) {
                    fixedRadius = (moveRadius - mDistance / 15).toFloat()
                } else {
                    badgeState = BadgeState.BUBBLE.APART
                }
                invalidate()
            }

            MotionEvent.ACTION_UP -> {
                if (badgeState == BadgeState.BUBBLE.CONNECT) {
                    resetAnimation()
                } else if (badgeState == BadgeState.BUBBLE.APART) {
                    startExplosionAnim()
                }
                performClick()
            }
        }
        return true
    }

    override fun performClick(): Boolean {
        nothing_to_do()
        return super.performClick()
    }

    /**
     * Set badge mode. Only useful when the current value of [badgeMode] is
     * [BadgeMode.UNSPECIFIED]
     *
     * @since 0.5.3
     */
    fun setMode(mode: BadgeMode) {
        if (badgeMode !is BadgeMode.UNSPECIFIED) return
        badgeMode = mode
        visibility = VISIBLE
        badgeState = when (badgeMode) {
            BadgeMode.DOT -> BadgeState.DOT.HIDE
            BadgeMode.BUBBLE.TEXT -> BadgeState.BUBBLE.DEFAULT
            BadgeMode.BUBBLE.NUMBER -> BadgeState.BUBBLE.DEFAULT
            BadgeMode.UNSPECIFIED -> BadgeState.UNSPECIFIED
        }
        invalidate()
    }

    /**
     * Set the color-int of the badge by [colorInt].
     *
     * @since 0.5.3
     */
    fun setColor(@ColorInt colorInt: Int) {
        check(ColorUtils.isColorInt(colorInt)) { "The value of bubble color isn't a valid value." }
        badgePaint.color = colorInt
        invalidate()
    }

    /**
     * The setting will only take effect when the mode is
     * [BadgeMode.BUBBLE.NUMBER] or [BadgeMode.BUBBLE.TEXT].
     *
     * @param bubbleRadius The radius of bubble at the current finger touch
     * position.
     * @since 0.5.3
     */
    fun setBubbleRadius(@FloatRange(from = 0.0) bubbleRadius: Float) {
        moveRadius = bubbleRadius.coerceAtLeast(moveRadius)
        fixedRadius = moveRadius
    }

    /**
     * Set the text to be displayed in bubble.
     *
     * The [text] determines the minimum radius of the bubble. That is, the
     * minimum bubble is the circumscribed circle of the [text] bounding
     * rectangle.
     *
     * The setting will only take effect when the [badgeMode] is
     * [BadgeMode.BUBBLE.TEXT].
     *
     * @since 0.5.3
     */
    fun setBubbleText(text: String) {
        if (text.isBlank()) return
        if (badgeMode == BadgeMode.UNSPECIFIED) return
        this.text = text
        moveRadius = moveRadius.coerceAtLeast(minimumRadius)
        fixedRadius = moveRadius
        invalidate()
    }

    /**
     * Set the number to be displayed in bubble.
     *
     * The setting will only take effect when the [badgeMode] is
     * [BadgeMode.BUBBLE.NUMBER].
     *
     * @since 0.5.3
     */
    fun setBubbleTextNum(@IntRange(from = 0) number: Int) {
        if (badgeMode == BadgeMode.UNSPECIFIED) return
        textNumber = number.coerceIn(0, textMaxNumber)
        invalidate()
    }

    /**
     * Set the max number to be displayed in bubble.
     *
     * The [maxNumber] determines the minimum radius of the bubble. For
     * example, if the value of maxNumber is 99, then the smallest bubble is
     * the circumscribed circle of the bounding rectangle of the string **99+**
     * in the specified style.
     *
     * The setting will only take effect when the [badgeMode] is
     * [BadgeMode.BUBBLE.NUMBER].
     *
     * @since 0.5.3
     */
    fun setBubbleTextMaxNum(@IntRange(from = 0) maxNumber: Int) {
        if (badgeMode == BadgeMode.UNSPECIFIED) return
        textMaxNumber = maxNumber.coerceAtLeast(0)
        moveRadius = moveRadius.coerceAtLeast(minimumRadius)
        fixedRadius = moveRadius
        invalidate()
    }

    /**
     * Set the color-int of text.
     *
     * The setting will only take effect when the mode is
     * [BadgeMode.BUBBLE.NUMBER] or [BadgeMode.BUBBLE.TEXT].
     *
     * @since 0.5.3
     */
    fun setBubbleTextColor(colorInt: Int) {
        check(ColorUtils.isColorInt(colorInt)) { "The font color of text is invalid." }
        textPaint.color = colorInt
        invalidate()
    }

    /**
     * Set the text size of bubble in pixels.
     *
     * The setting will only take effect when the mode is
     * [BadgeMode.BUBBLE.NUMBER] or [BadgeMode.BUBBLE.TEXT].
     *
     * @since 0.5.3
     */
    fun setBubbleTextSize(size: Float) {
        if (badgeMode == BadgeMode.DOT) return
        textPaint.textSize = size.coerceAtLeast(0f)
        invalidate()
    }

    /**
     * The setting will only take effect when the mode is [BadgeMode.DOT].
     *
     * @since 0.5.3
     */
    fun setDotRadius(dotRadius: Float) {
        this.dotRadius = dotRadius
        if (badgeMode == BadgeMode.DOT) {
            invalidate()
        }
    }

    /**
     * Hide dot. The setting will only take effect when the mode is
     * [BadgeMode.DOT].
     *
     * @since 0.5.3
     */
    fun hideDot() {
        if (badgeMode == BadgeMode.DOT) {
            badgeState = BadgeState.DOT.HIDE
        }
    }


    /**
     * Show dot. The setting will only take effect when the mode is
     * [BadgeMode.DOT].
     *
     * @since 0.5.3
     */
    fun showDot() {
        if (badgeMode == BadgeMode.DOT) {
            badgeState = BadgeState.DOT.SHOW
        }
    }

    /**
     * Get number string of [textNumber].
     *
     * @since 0.5.3
     */
    private fun getBubbleTextNumber() =
        if (textNumber == textMaxNumber) "$textMaxNumber+" else "$textNumber"

    private fun startExplosionAnim() {
        badgeState = BadgeState.BUBBLE.HIDE
        ValueAnimator.ofInt(0, explosionBmp.size).apply {
            interpolator = LinearInterpolator()
            duration = 800
            addUpdateListener {
                bmpIndex = cast(it.animatedValue)
                invalidate()
            }
            start()
        }
    }

    private fun resetAnimation() {
        ValueAnimator.ofObject(PointFEvaluator(), movePoint, fixedPoint).apply {
            duration = 1000
            interpolator = OvershootInterpolator(1F)
            addUpdateListener {
                movePoint = cast(it.animatedValue)
                invalidate()
            }
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    badgeState = BadgeState.BUBBLE.DEFAULT
                }
            })
            start()
        }
    }

    /**
     * Reset [movePoint] and [fixedPoint].
     *
     * @since 0.5.3
     */
    private fun resetPoint() {
        badgeState = BadgeState.BUBBLE.DEFAULT
        fixedRadius = moveRadius
        if (badgeMode == BadgeMode.BUBBLE.NUMBER) {
            textNumber = INIT_NUMBER
        }
        movePoint.x = (width / 2.0).toFloat()
        movePoint.y = (height / 2.0).toFloat()
        fixedPoint.x = (width / 2.0).toFloat()
        fixedPoint.y = (height / 2.0).toFloat()
    }

    /**
     * Get minimum radius of the bubble.
     *
     * @since 0.5.3
     */
    private fun String.getMinimumRadius(): Float {
        val textLength = textPaint.measureText(this)
        val fontMetrics = textPaint.fontMetrics
        val textHeight = fontMetrics.bottom - fontMetrics.top
        return (hypot(textLength, textHeight) / 2.0).toFloat()
    }

    /**
     * Get text baseline.
     *
     * @since 0.5.3
     */
    private fun getTextBaseline(): Float {
        val fontMetrics = textPaint.fontMetrics
        val textHeight = fontMetrics.bottom - fontMetrics.top
        return textHeight / 2 - fontMetrics.bottom
    }

    /**
     * Draw a Bezier curve connecting circles centered at [movePoint] and
     * [fixedPoint].
     *
     * @since 0.5.3
     */
    private fun Canvas.drawBezier() {
        drawCircle(fixedPoint.x, fixedPoint.y, fixedRadius, badgePaint)
        drawCircle(movePoint.x, movePoint.y, moveRadius, badgePaint)
        val mDistance = hypot(
            (movePoint.x - fixedPoint.x).toDouble(),
            (movePoint.y - fixedPoint.y).toDouble()
        )
        val sinAlpha = abs(movePoint.y - fixedPoint.y) / mDistance
        val cosAlpha = abs(movePoint.x - fixedPoint.x) / mDistance
        controlPoint.x = (movePoint.x + fixedPoint.x) / 2
        controlPoint.y = (movePoint.y + fixedPoint.y) / 2
        pointA.x = (fixedPoint.x - sinAlpha * fixedRadius).toFloat()
        pointA.y = (fixedPoint.y - cosAlpha * fixedRadius).toFloat()
        pointB.x = (fixedPoint.x + sinAlpha * fixedRadius).toFloat()
        pointB.y = (fixedPoint.y + cosAlpha * fixedRadius).toFloat()
        pointC.x = (movePoint.x - sinAlpha * moveRadius).toFloat()
        pointC.y = (movePoint.y - cosAlpha * moveRadius).toFloat()
        pointD.x = (movePoint.x + sinAlpha * moveRadius).toFloat()
        pointD.y = (movePoint.y + cosAlpha * moveRadius).toFloat()
        bezierPath.reset()
        bezierPath.moveTo(pointA.x, pointA.y)
        bezierPath.quadTo(controlPoint.x, controlPoint.y, pointC.x, pointC.y)
        bezierPath.lineTo(pointD.x, pointD.y)
        bezierPath.quadTo(controlPoint.x, controlPoint.y, pointB.x, pointB.y)
        bezierPath.close()
        drawPath(bezierPath, bezierPaint)
    }

    init {
        context.withStyledAttributes(attrs, R.styleable.BadgeView, defStyleAttr, defStyleRes) {
            badgeMode = when (getInt(R.styleable.BadgeView_badge_mode, BadgeMode.UNSPECIFIED.code)) {
                BadgeMode.DOT.code -> BadgeMode.DOT
                BadgeMode.BUBBLE.TEXT.code -> BadgeMode.BUBBLE.TEXT
                BadgeMode.BUBBLE.NUMBER.code -> BadgeMode.BUBBLE.NUMBER
                else -> {
                    this@BadgeView.visibility = GONE
                    BadgeMode.UNSPECIFIED
                }
            }
            badgeState = when (badgeMode) {
                BadgeMode.DOT -> BadgeState.DOT.HIDE
                BadgeMode.BUBBLE.TEXT -> BadgeState.BUBBLE.DEFAULT
                BadgeMode.BUBBLE.NUMBER -> BadgeState.BUBBLE.DEFAULT
                BadgeMode.UNSPECIFIED -> BadgeState.UNSPECIFIED
            }
            badgePaint.color = getColor(R.styleable.BadgeView_badge_color, color(R.color.md_theme_error))
            dotRadius = getDimension(R.styleable.BadgeView_dot_radius, INIT_DOT_RADIUS)
            text = getString(R.styleable.BadgeView_bubble_text) ?: ""
            textMaxNumber = getInteger(R.styleable.BadgeView_bubble_text_max_num, INIT_MAX_NUMBER).let { if (it <= 0) INIT_MAX_NUMBER else it }
            setBubbleTextNum(INIT_NUMBER)
            setBubbleRadius(getDimension(R.styleable.BadgeView_bubble_radius, DEFAULT_BUBBLE_RADIUS))
            textPaint.color = getColor(R.styleable.BadgeView_bubble_text_color, color(R.color.white))
            textPaint.textSize = getDimension(R.styleable.BadgeView_bubble_text_size, DEFAULT_TEXT_SIZE)
        }
    }

    companion object {
        private const val INIT_RADIUS = 0f
        internal const val INIT_NUMBER = 0
        private const val INIT_TEXT = ""
        internal const val INIT_MAX_NUMBER = 99
        internal val INIT_DOT_RADIUS = 5f.DP
        internal val DEFAULT_TEXT_SIZE = 8f.SP
        internal val DEFAULT_BUBBLE_RADIUS = 10f.DP
    }

}