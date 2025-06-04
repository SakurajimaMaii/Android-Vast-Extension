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
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PointF
import android.graphics.Rect
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.View
import android.view.animation.LinearInterpolator
import android.view.animation.OvershootInterpolator
import androidx.annotation.ColorInt
import androidx.annotation.FloatRange
import androidx.annotation.IntRange
import androidx.annotation.StyleRes
import androidx.core.content.withStyledAttributes
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.NestedScrollingChild
import androidx.core.view.NestedScrollingChildHelper
import androidx.core.view.ViewCompat
import com.ave.vastgui.core.extension.NotNUllVar
import com.ave.vastgui.core.extension.cast
import com.ave.vastgui.tools.R
import com.ave.vastgui.tools.utils.ColorUtils
import com.ave.vastgui.tools.utils.DensityUtils.DP
import com.ave.vastgui.tools.utils.DensityUtils.SP
import com.ave.vastgui.tools.utils.color
import com.ave.vastgui.tools.utils.drawable
import com.ave.vastgui.tools.view.extension.gone
import com.ave.vastgui.tools.view.extension.visible
import kotlin.math.abs
import kotlin.math.hypot
import kotlin.math.max
import kotlin.math.roundToInt


// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/9/11
// Documentation: https://sakurajimamaii.github.io/AVE-DOC/documents/tools/core-topics/ui/badge/description/

/**
 * Badge View.
 *
 * @since 0.5.3
 */
class BadgeView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.Default_BadgeView_Style,
    @StyleRes defStyleRes: Int = R.style.BaseBadgeView
) : View(context, attrs, defStyleAttr, defStyleRes),
    NestedScrollingChild {

    /**
     * When the coordinate of first touch is smaller than
     * [bubbleRadius] + [minOffsetDistance], it means you touched the
     * [BadgeView].
     *
     * @since 1.5.2
     */
    private val minOffsetDistance = 5F.DP

    /**
     * The radius of badge which the finger at the current touch position when
     * the [badgeMode] is [BadgeMode.Bubble.Text] or [BadgeMode.Bubble.Number].
     *
     * @since 1.5.2
     */
    private var touchBubbleRadius = DEFAULT_BUBBLE_RADIUS

    /**
     * The coordinate of dot which shown in fixed position.
     *
     * @since 1.5.2
     */
    private val dotCoordPointF = PointF()

    /**
     * The coordinate of bubble which shown in fixed position.
     *
     * @since 1.5.2
     */
    private val fixedBubbleCoordPointF = PointF()

    /**
     * The coordinate of bubble which shown in move position.
     *
     * @since 1.5.2
     */
    private var touchBubbleCoordPointF = PointF()

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

    /**
     * The radius of badge when the [badgeMode] is [BadgeMode.Dot] (in pixels).
     *
     * @since 1.5.2
     */
    var dotRadius = DEFAULT_DOT_RADIUS
        private set

    /**
     * The radius of badge when the [badgeMode] is [BadgeMode.Bubble.Text] or
     * [BadgeMode.Bubble.Number] (in pixels).
     *
     * @since 1.5.2
     */
    var bubbleRadius: Float = DEFAULT_BUBBLE_RADIUS
        private set

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
    var textNumber = INIT_NUMBER
        private set

    /** @since 1.5.2 */
    var textMaxNumber = INIT_NUMBER
        private set

    /** @since 1.5.2 */
    var text = INIT_TEXT
        private set

    /**
     * The text of [BadgeMode.Bubble] (in pixels).
     *
     * @since 1.5.2
     */
    val textSize
        get() = textPaint.textSize

    /** @since 1.5.2 */
    @get:ColorInt
    val textColor
        get() = textPaint.color

    /** @since 1.5.2 */
    private val nestedScrollingChildHelper = NestedScrollingChildHelper(this)

    /** @since 1.5.2 */
    private val gestureDetector: GestureDetector = GestureDetector(context, object : SimpleOnGestureListener() {
        override fun onSingleTapUp(e: MotionEvent): Boolean {
            return performClick()
        }

        override fun onLongPress(e: MotionEvent) {
            performLongClick()
        }
    })

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        when (badgeMode) {
            BadgeMode.Unspecified -> setMeasuredDimension(0, 0)
            BadgeMode.Dot -> {
                val neededMinimumWidth = max((dotRadius * 2).roundToInt() + paddingStart + paddingEnd, suggestedMinimumWidth)
                val neededMinimumHeight = max((dotRadius * 2).roundToInt() + paddingTop + paddingBottom, suggestedMinimumHeight)
                val width = resolveSize(neededMinimumWidth, widthMeasureSpec)
                val height = resolveSize(neededMinimumHeight, heightMeasureSpec)
                setMeasuredDimension(width, height)
                dotCoordPointF.set((paddingStart + measuredWidth - paddingEnd) / 2f, (paddingTop + measuredHeight - paddingBottom) / 2f)
            }

            is BadgeMode.Bubble -> {
                val neededMinimumWidth = max((bubbleRadius * 2).roundToInt() + paddingStart + paddingEnd, suggestedMinimumWidth)
                val neededMinimumHeight = max((bubbleRadius * 2).roundToInt() + paddingTop + paddingBottom, suggestedMinimumHeight)
                val width = resolveSize(neededMinimumWidth, widthMeasureSpec)
                val height = resolveSize(neededMinimumHeight, heightMeasureSpec)
                setMeasuredDimension(width, height)
                fixedBubbleCoordPointF.set((paddingStart + measuredWidth - paddingEnd) / 2f, (paddingTop + measuredHeight - paddingBottom) / 2f)
                touchBubbleCoordPointF.set(fixedBubbleCoordPointF.x, fixedBubbleCoordPointF.y)
            }
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        when (badgeMode) {
            BadgeMode.Unspecified -> return

            BadgeMode.Dot -> {
                if (badgeState !is BadgeState.DotState.Show) return
                if (measuredWidth - paddingStart - paddingEnd < 0 || measuredHeight - paddingTop - paddingBottom < 0) return
                canvas.drawCircle(dotCoordPointF.x, dotCoordPointF.y, dotRadius, badgePaint)
            }

            BadgeMode.Bubble.Text -> {
                if (measuredWidth - paddingStart - paddingEnd < 0 || measuredHeight - paddingTop - paddingBottom < 0) return
                if (badgeState == BadgeState.BubbleState.Default && !text.isBlank()) {
                    if (bubbleRadius != touchBubbleRadius) bubbleRadius = touchBubbleRadius
                    canvas.drawCircle(fixedBubbleCoordPointF.x, fixedBubbleCoordPointF.y, bubbleRadius, badgePaint)
                    canvas.drawText(text, fixedBubbleCoordPointF.x, fixedBubbleCoordPointF.y + getTextBaseline(), textPaint)
                }
                if (badgeState == BadgeState.BubbleState.Connect) {
                    canvas.drawBezier()
                    canvas.drawText(text, touchBubbleCoordPointF.x, touchBubbleCoordPointF.y + getTextBaseline(), textPaint)
                }
                if (badgeState == BadgeState.BubbleState.Apart) {
                    canvas.drawCircle(touchBubbleCoordPointF.x, touchBubbleCoordPointF.y, touchBubbleRadius, badgePaint)
                    canvas.drawText(text, touchBubbleCoordPointF.x, touchBubbleCoordPointF.y + getTextBaseline(), textPaint)
                }
                if (badgeState == BadgeState.BubbleState.Hide && bmpIndex != explosionBmp.size) {
                    explosionRect.left = (touchBubbleCoordPointF.x - touchBubbleRadius).toInt()
                    explosionRect.right = (touchBubbleCoordPointF.x + touchBubbleRadius).toInt()
                    explosionRect.top = (touchBubbleCoordPointF.y - touchBubbleRadius).toInt()
                    explosionRect.bottom = (touchBubbleCoordPointF.y + touchBubbleRadius).toInt()
                    canvas.drawBitmap(explosionBmp[bmpIndex], null, explosionRect, badgePaint)
                }
                if (badgeState == BadgeState.BubbleState.Hide && bmpIndex == explosionBmp.size) {
                    resetPoint()
                }
            }

            BadgeMode.Bubble.Number -> {
                if (measuredWidth - paddingStart - paddingEnd < 0 || measuredHeight - paddingTop - paddingBottom < 0) return
                if (badgeState == BadgeState.BubbleState.Default && textNumber != INIT_NUMBER) {
                    if (bubbleRadius != touchBubbleRadius) bubbleRadius = touchBubbleRadius
                    canvas.drawCircle(fixedBubbleCoordPointF.x, fixedBubbleCoordPointF.y, bubbleRadius, badgePaint)
                    canvas.drawText(getBubbleTextNumber(), fixedBubbleCoordPointF.x, fixedBubbleCoordPointF.y + getTextBaseline(), textPaint)
                }
                if (badgeState == BadgeState.BubbleState.Connect) {
                    canvas.drawBezier()
                    canvas.drawText(getBubbleTextNumber(), touchBubbleCoordPointF.x, touchBubbleCoordPointF.y + getTextBaseline(), textPaint)
                }
                if (badgeState == BadgeState.BubbleState.Apart) {
                    canvas.drawCircle(touchBubbleCoordPointF.x, touchBubbleCoordPointF.y, touchBubbleRadius, badgePaint)
                    canvas.drawText(getBubbleTextNumber(), touchBubbleCoordPointF.x, touchBubbleCoordPointF.y + getTextBaseline(), textPaint)
                }
                if (badgeState == BadgeState.BubbleState.Hide && bmpIndex != explosionBmp.size) {
                    explosionRect.left = (touchBubbleCoordPointF.x - touchBubbleRadius).toInt()
                    explosionRect.right = (touchBubbleCoordPointF.x + touchBubbleRadius).toInt()
                    explosionRect.top = (touchBubbleCoordPointF.y - touchBubbleRadius).toInt()
                    explosionRect.bottom = (touchBubbleCoordPointF.y + touchBubbleRadius).toInt()
                    canvas.drawBitmap(explosionBmp[bmpIndex], null, explosionRect, badgePaint)
                }
                if (badgeState == BadgeState.BubbleState.Hide && bmpIndex == explosionBmp.size) {
                    resetPoint()
                }
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (badgeMode == BadgeMode.Dot) {
            return super.onTouchEvent(event)
        } else if (badgeMode == BadgeMode.Bubble.Number && textNumber == INIT_NUMBER) {
            return super.onTouchEvent(event)
        } else {
            gestureDetector.onTouchEvent(event)
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    val distance = hypot((event.x - fixedBubbleCoordPointF.x).toDouble(), (event.y - fixedBubbleCoordPointF.y).toDouble())
                    badgeState = if (distance <= bubbleRadius + minOffsetDistance) {
                        startNestedScroll(ViewCompat.SCROLL_AXIS_HORIZONTAL or ViewCompat.SCROLL_AXIS_VERTICAL)
                        BadgeState.BubbleState.Connect
                    } else {
                        BadgeState.BubbleState.Default
                    }
                }

                MotionEvent.ACTION_MOVE -> {
                    if (badgeState is BadgeState.BubbleState.Connect) {
                        val distance = hypot((event.x - fixedBubbleCoordPointF.x).toDouble(), (event.y - fixedBubbleCoordPointF.y).toDouble())
                        touchBubbleCoordPointF.set(event.x, event.y)
                        if (bubbleRadius - distance / 50 >= 0.0) {
                            bubbleRadius = (bubbleRadius - distance / 50).toFloat()
                        } else {
                            badgeState = BadgeState.BubbleState.Apart
                        }
                        invalidate()
                    }
                }

                MotionEvent.ACTION_UP -> {
                    if (badgeState == BadgeState.BubbleState.Connect) {
                        resetAnimation()
                        stopNestedScroll()
                    } else if (badgeState == BadgeState.BubbleState.Apart) {
                        startExplosionAnim()
                        stopNestedScroll()
                    }
                }
            }
        }
        return true
    }

    /** @since 1.5.2 */
    override fun setNestedScrollingEnabled(enabled: Boolean) {
        nestedScrollingChildHelper.isNestedScrollingEnabled = enabled
    }

    /** @since 1.5.2 */
    override fun isNestedScrollingEnabled(): Boolean {
        return nestedScrollingChildHelper.isNestedScrollingEnabled
    }

    /** @since 1.5.2 */
    override fun startNestedScroll(axes: Int): Boolean {
        return nestedScrollingChildHelper.startNestedScroll(axes)
    }

    /** @since 1.5.2 */
    override fun stopNestedScroll() {
        nestedScrollingChildHelper.stopNestedScroll()
    }

    /** @since 1.5.2 */
    override fun hasNestedScrollingParent(): Boolean {
        return nestedScrollingChildHelper.hasNestedScrollingParent()
    }

    /** @since 1.5.2 */
    override fun dispatchNestedScroll(dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int, offsetInWindow: IntArray?): Boolean {
        return nestedScrollingChildHelper.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow)
    }

    /** @since 1.5.2 */
    override fun dispatchNestedPreScroll(dx: Int, dy: Int, consumed: IntArray?, offsetInWindow: IntArray?): Boolean {
        return nestedScrollingChildHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow)
    }

    /** @since 1.5.2 */
    override fun dispatchNestedFling(velocityX: Float, velocityY: Float, consumed: Boolean): Boolean {
        return nestedScrollingChildHelper.dispatchNestedFling(velocityX, velocityY, consumed)
    }

    /** @since 1.5.2 */
    override fun dispatchNestedPreFling(velocityX: Float, velocityY: Float): Boolean {
        return nestedScrollingChildHelper.dispatchNestedPreFling(velocityX, velocityY)
    }

    /** @since 1.5.2 */
    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        nestedScrollingChildHelper.onDetachedFromWindow()
    }

    /**
     * Set badge mode. Only useful when the current value of [badgeMode] is
     * [BadgeMode.Unspecified]
     *
     * @since 0.5.3
     */
    fun setMode(mode: BadgeMode) {
        if (badgeMode !is BadgeMode.Unspecified) return
        badgeMode = mode
        visible()
        badgeState = when (badgeMode) {
            BadgeMode.Dot -> BadgeState.DotState.Hide
            BadgeMode.Bubble.Text -> BadgeState.BubbleState.Default
            BadgeMode.Bubble.Number -> BadgeState.BubbleState.Default
            BadgeMode.Unspecified -> BadgeState.UnspecifiedState
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
     * Set the radius of bubble(in pixels).
     *
     * The setting will only take effect when the mode is
     * [BadgeMode.Bubble.Number] or [BadgeMode.Bubble.Text].
     *
     * @since 0.5.3
     */
    fun setBubbleRadius(@FloatRange(from = 0.0) bubbleRadius: Float) {
        if (badgeMode is BadgeMode.Bubble) {
            this.bubbleRadius = bubbleRadius.coerceAtLeast(0f)
            touchBubbleRadius = this.bubbleRadius
            requestLayout()
        }
    }

    /**
     * Set the text to be displayed in bubble.
     *
     * The setting will only take effect when the [badgeMode] is
     * [BadgeMode.Bubble.Text].
     *
     * @since 0.5.3
     */
    fun setBubbleText(text: String) {
        if (this.text == text) return
        if (badgeMode is BadgeMode.Bubble.Text) {
            this.text = text
            invalidate()
        }
    }

    /**
     * Set the number to be displayed in bubble.
     *
     * The setting will only take effect when the [badgeMode] is
     * [BadgeMode.Bubble.Number].
     *
     * @since 0.5.3
     */
    fun setBubbleTextNum(@IntRange(from = 0) number: Int) {
        if (textNumber == number) return
        if (badgeMode is BadgeMode.Bubble.Number) {
            textNumber = number.coerceIn(0, textMaxNumber)
            invalidate()
        }
    }

    /**
     * Set the max number to be displayed in bubble.
     *
     * The setting will only take effect when the [badgeMode] is
     * [BadgeMode.Bubble.Number].
     *
     * @since 0.5.3
     */
    fun setBubbleTextMaxNum(@IntRange(from = 0) maxNumber: Int) {
        if (textMaxNumber == maxNumber) return
        if (badgeMode is BadgeMode.Bubble.Number) {
            textMaxNumber = maxNumber.coerceAtLeast(0)
            invalidate()
        }
    }

    /**
     * Set the color-int of text.
     *
     * The setting will only take effect when the mode is
     * [BadgeMode.Bubble.Number] or [BadgeMode.Bubble.Text].
     *
     * @since 0.5.3
     */
    fun setBubbleTextColor(@ColorInt colorInt: Int) {
        if (textPaint.color == colorInt) return
        if (badgeMode is BadgeMode.Bubble) {
            check(ColorUtils.isColorInt(colorInt)) { "The font color of text is invalid." }
            textPaint.color = colorInt
            invalidate()
        }
    }

    /**
     * Set the text size of bubble(in pixels).
     *
     * The setting will only take effect when the mode is
     * [BadgeMode.Bubble.Number] or [BadgeMode.Bubble.Text].
     *
     * @since 0.5.3
     */
    fun setBubbleTextSize(size: Float) {
        if (textPaint.textSize == size) return
        if (badgeMode is BadgeMode.Bubble) {
            textPaint.textSize = size.coerceAtLeast(0f)
            invalidate()
        }
    }

    /**
     * Set the radius of dot badge(in pixels).
     *
     * The setting will only take effect when the mode is [BadgeMode.Dot].
     *
     * @since 0.5.3
     */
    fun setDotRadius(dotRadius: Float) {
        if (this.dotRadius == dotRadius) return
        if (badgeMode is BadgeMode.Dot) {
            this.dotRadius = dotRadius
            requestLayout()
        }
    }

    /**
     * Hide dot.
     *
     * The setting will only take effect when the mode is [BadgeMode.Dot].
     *
     * @since 0.5.3
     */
    fun hideDot() {
        if (badgeMode is BadgeMode.Dot) {
            badgeState = BadgeState.DotState.Hide
            invalidate()
        }
    }


    /**
     * Show dot.
     *
     * The setting will only take effect when the mode is [BadgeMode.Dot].
     *
     * @since 0.5.3
     */
    fun showDot() {
        if (badgeMode is BadgeMode.Dot) {
            badgeState = BadgeState.DotState.Show
            invalidate()
        }
    }

    /**
     * Get number string of [textNumber].
     *
     * The setting will only take effect when the mode is
     * [BadgeMode.Bubble.Number].
     *
     * @since 0.5.3
     */
    fun getBubbleTextNumber(): String = if (badgeMode is BadgeMode.Bubble.Number) {
        if (textNumber == textMaxNumber) "$textMaxNumber+" else "$textNumber"
    } else {
        ""
    }

    private fun startExplosionAnim() {
        badgeState = BadgeState.BubbleState.Hide
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
        ValueAnimator.ofObject(PointFEvaluator(), touchBubbleCoordPointF, fixedBubbleCoordPointF).apply {
            duration = 1000
            interpolator = OvershootInterpolator(1F)
            addUpdateListener {
                touchBubbleCoordPointF = cast(it.animatedValue)
                invalidate()
            }
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    badgeState = BadgeState.BubbleState.Default
                }
            })
            start()
        }
    }

    /**
     * Reset [touchBubbleCoordPointF] and [fixedBubbleCoordPointF].
     *
     * @since 0.5.3
     */
    private fun resetPoint() {
        badgeState = BadgeState.BubbleState.Default
        bubbleRadius = touchBubbleRadius
        if (badgeMode == BadgeMode.Bubble.Number) {
            textNumber = INIT_NUMBER
        }
        fixedBubbleCoordPointF.set((paddingStart + measuredWidth - paddingEnd) / 2f, (paddingTop + measuredHeight - paddingBottom) / 2f)
        touchBubbleCoordPointF.set(fixedBubbleCoordPointF.x, fixedBubbleCoordPointF.y)
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
     * Draw a Bezier curve connecting circles centered at
     * [touchBubbleCoordPointF] and [fixedBubbleCoordPointF].
     *
     * @since 0.5.3
     */
    private fun Canvas.drawBezier() {
        drawCircle(fixedBubbleCoordPointF.x, fixedBubbleCoordPointF.y, bubbleRadius, badgePaint)
        drawCircle(touchBubbleCoordPointF.x, touchBubbleCoordPointF.y, touchBubbleRadius, badgePaint)
        val distance = hypot((touchBubbleCoordPointF.x - fixedBubbleCoordPointF.x).toDouble(),
            (touchBubbleCoordPointF.y - fixedBubbleCoordPointF.y).toDouble())
        val sinAlpha = abs(touchBubbleCoordPointF.y - fixedBubbleCoordPointF.y) / distance
        val cosAlpha = abs(touchBubbleCoordPointF.x - fixedBubbleCoordPointF.x) / distance
        controlPoint.x = (touchBubbleCoordPointF.x + fixedBubbleCoordPointF.x) / 2
        controlPoint.y = (touchBubbleCoordPointF.y + fixedBubbleCoordPointF.y) / 2
        pointA.x = (fixedBubbleCoordPointF.x - sinAlpha * bubbleRadius).toFloat()
        pointA.y = (fixedBubbleCoordPointF.y - cosAlpha * bubbleRadius).toFloat()
        pointB.x = (fixedBubbleCoordPointF.x + sinAlpha * bubbleRadius).toFloat()
        pointB.y = (fixedBubbleCoordPointF.y + cosAlpha * bubbleRadius).toFloat()
        pointC.x = (touchBubbleCoordPointF.x - sinAlpha * touchBubbleRadius).toFloat()
        pointC.y = (touchBubbleCoordPointF.y - cosAlpha * touchBubbleRadius).toFloat()
        pointD.x = (touchBubbleCoordPointF.x + sinAlpha * touchBubbleRadius).toFloat()
        pointD.y = (touchBubbleCoordPointF.y + cosAlpha * touchBubbleRadius).toFloat()
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
            badgeMode = when (getInt(R.styleable.BadgeView_badge_mode, BadgeMode.Unspecified.code)) {
                BadgeMode.Dot.code -> BadgeMode.Dot
                BadgeMode.Bubble.Text.code -> BadgeMode.Bubble.Text
                BadgeMode.Bubble.Number.code -> BadgeMode.Bubble.Number
                else -> gone().let { BadgeMode.Unspecified }
            }
            badgeState = when (badgeMode) {
                BadgeMode.Dot -> BadgeState.DotState.Hide
                BadgeMode.Bubble.Text -> BadgeState.BubbleState.Default
                BadgeMode.Bubble.Number -> BadgeState.BubbleState.Default
                BadgeMode.Unspecified -> BadgeState.UnspecifiedState
            }
            badgePaint.color = getColor(R.styleable.BadgeView_badge_color, color(R.color.md_theme_error))
            dotRadius = getDimension(R.styleable.BadgeView_dot_radius, DEFAULT_DOT_RADIUS).coerceAtLeast(0f)
            bubbleRadius = getDimension(R.styleable.BadgeView_bubble_radius, DEFAULT_BUBBLE_RADIUS).coerceAtLeast(0f)
            touchBubbleRadius = bubbleRadius
            text = getString(R.styleable.BadgeView_bubble_text) ?: ""
            textNumber = getInteger(R.styleable.BadgeView_bubble_text_num, INIT_NUMBER).coerceAtLeast(0)
            textMaxNumber = getInteger(R.styleable.BadgeView_bubble_text_max_num, INIT_MAX_NUMBER).coerceAtLeast(textNumber)
            textPaint.color = getColor(R.styleable.BadgeView_bubble_text_color, color(R.color.white))
            textPaint.textSize = getDimension(R.styleable.BadgeView_bubble_text_size, DEFAULT_TEXT_SIZE)
        }

        isNestedScrollingEnabled = true
    }

    companion object {
        internal const val INIT_TEXT = ""
        internal const val INIT_NUMBER = 0
        internal const val INIT_MAX_NUMBER = 99
        internal val DEFAULT_DOT_RADIUS = 5f.DP
        internal val DEFAULT_TEXT_SIZE = 8f.SP
        internal val DEFAULT_BUBBLE_RADIUS = 10f.DP
    }

}