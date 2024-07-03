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
import androidx.core.content.ContextCompat
import com.ave.vastgui.core.extension.NotNUllVar
import com.ave.vastgui.core.extension.cast
import com.ave.vastgui.core.extension.nothing_to_do
import com.ave.vastgui.tools.R
import com.ave.vastgui.tools.graphics.BmpUtils
import com.ave.vastgui.tools.utils.DensityUtils.DP
import com.ave.vastgui.tools.utils.DensityUtils.SP
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.hypot

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/9/11
// Documentation: https://ave.entropy2020.cn/documents/VastTools/core-topics/ui/badge/description/

/**
 * Badge View.
 *
 * @property mMinOffsetDistance When the coordinate of first touch is
 *     smaller than mMoveRadius + MIN_OFFSET_DISTANCE, it means you touched
 *     the starting red dot.
 * @since 0.5.3
 */
class BadgeView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.Default_BadgeView_Style,
    @StyleRes defStyleRes: Int = R.style.BaseBadgeView
) : View(context, attrs, defStyleAttr, defStyleRes) {

    companion object {
        private const val INIT_RADIUS = 0f
        internal const val INIT_NUMBER = 0
        private const val INIT_TEXT = ""
        internal const val INIT_MAX_NUMBER = 99
        internal val initDotRadius = 5f.DP
        internal val initTextSize = 8f.SP
        internal val initBubbleRadius = 10f.DP
    }

    private val mMinOffsetDistance = 5F.DP
    private var mFixedRadius = INIT_RADIUS
    private val mFixedPoint = PointF()
    private var mMovePoint = PointF()
    private val mControlPoint = PointF()
    private val mPointA = PointF()
    private val mPointB = PointF()
    private val mPointC = PointF()
    private val mPointD = PointF()

    private val mBezierPath = Path()

    private val mExplosionRect: Rect = Rect()
    private var mExplosionBmp = if (!isInEditMode) listOf(
        BmpUtils.getBitmapFromDrawable(R.drawable.badge_explode_1, context),
        BmpUtils.getBitmapFromDrawable(R.drawable.badge_explode_2, context),
        BmpUtils.getBitmapFromDrawable(R.drawable.badge_explode_3, context),
        BmpUtils.getBitmapFromDrawable(R.drawable.badge_explode_4, context),
        BmpUtils.getBitmapFromDrawable(R.drawable.badge_explode_5, context)
    ) else listOf()
    private var mBmpIndex = 0

    private val mTextPaint = Paint().apply {
        isAntiAlias = true
        textAlign = Paint.Align.CENTER
    }
    private val mBadgePaint = Paint().apply {
        isAntiAlias = true
    }
    private val mBezierPaint
        get() = mBadgePaint.apply {
            style = Paint.Style.FILL
        }

    private val mMinimumRadius
        get() = when (mBadgeMode) {
            BadgeMode.BUBBLE.TEXT -> {
                mText.getMinimumRadius()
            }

            BadgeMode.BUBBLE.NUMBER -> {
                getBubbleTextNumber().getMinimumRadius()
            }

            else -> 0f
        }

    var mBadgeState by NotNUllVar<BadgeState>()
        private set
    var mBadgeMode by NotNUllVar<BadgeMode>()
        private set
    val mBadgeColor: Int
        get() = mBadgePaint.color
    var mMoveRadius = INIT_RADIUS
        private set
    var mDotRadius = INIT_RADIUS
        private set
    var mTextNumber = INIT_NUMBER
        private set
    var mTextMaxNumber = INIT_NUMBER
        private set
    var mText = INIT_TEXT
        private set
    val mTextSize
        get() = mTextPaint.textSize
    val mTextColor
        get() = mTextPaint.color

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (mBadgeMode == BadgeMode.UNSPECIFIED) {
            setMeasuredDimension(0, 0)
            return
        }
        val size = if (mBadgeMode is BadgeMode.DOT) {
            mMovePoint.x = mDotRadius
            mMovePoint.y = mDotRadius
            ceil(mDotRadius * 2).toInt()
        } else {
            mMovePoint.x = mMoveRadius
            mMovePoint.y = mMoveRadius
            mFixedPoint.x = mFixedRadius
            mFixedPoint.y = mFixedRadius
            ceil(mMoveRadius * 2).coerceAtLeast(mMinimumRadius).toInt()
        }
        setMeasuredDimension(size, size)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        when (mBadgeMode) {
            BadgeMode.UNSPECIFIED -> {
                return
            }

            BadgeMode.DOT -> {
                if (mBadgeState == BadgeState.DOT.SHOW) {
                    canvas.drawCircle(mMovePoint.x, mMovePoint.y, mDotRadius, mBadgePaint)
                }
            }

            BadgeMode.BUBBLE.TEXT -> {
                if (mBadgeState == BadgeState.BUBBLE.DEFAULT) {
                    if (mFixedRadius != mMoveRadius) mFixedRadius = mMoveRadius
                    canvas.drawCircle(mFixedPoint.x, mFixedPoint.y, mFixedRadius, mBadgePaint)
                    canvas.drawText(
                        mText,
                        mFixedPoint.x,
                        mFixedPoint.y + getTextBaseline(),
                        mTextPaint
                    )
                }
                if (mBadgeState == BadgeState.BUBBLE.CONNECT) {
                    canvas.drawBezier()
                    canvas.drawText(
                        mText,
                        mMovePoint.x,
                        mMovePoint.y + getTextBaseline(),
                        mTextPaint
                    )
                }
                if (mBadgeState == BadgeState.BUBBLE.APART) {
                    canvas.drawCircle(mMovePoint.x, mMovePoint.y, mMoveRadius, mBadgePaint)
                    canvas.drawText(
                        mText,
                        mMovePoint.x,
                        mMovePoint.y + getTextBaseline(),
                        mTextPaint
                    )
                }
                if (mBadgeState == BadgeState.BUBBLE.HIDE && mBmpIndex != mExplosionBmp.size) {
                    mExplosionRect.left = (mMovePoint.x - mMoveRadius).toInt()
                    mExplosionRect.right = (mMovePoint.x + mMoveRadius).toInt()
                    mExplosionRect.top = (mMovePoint.y - mMoveRadius).toInt()
                    mExplosionRect.bottom = (mMovePoint.y + mMoveRadius).toInt()
                    canvas.drawBitmap(mExplosionBmp[mBmpIndex], null, mExplosionRect, mBadgePaint)
                }
                if (mBadgeState == BadgeState.BUBBLE.HIDE && mBmpIndex == mExplosionBmp.size) {
                    resetPoint()
                }
            }

            BadgeMode.BUBBLE.NUMBER -> {
                if (mBadgeState == BadgeState.BUBBLE.DEFAULT && mTextNumber != INIT_NUMBER) {
                    if (mFixedRadius != mMoveRadius) mFixedRadius = mMoveRadius
                    canvas.drawCircle(mFixedPoint.x, mFixedPoint.y, mFixedRadius, mBadgePaint)
                    canvas.drawText(
                        getBubbleTextNumber(),
                        mFixedPoint.x,
                        mFixedPoint.y + getTextBaseline(),
                        mTextPaint
                    )
                }
                if (mBadgeState == BadgeState.BUBBLE.CONNECT) {
                    canvas.drawBezier()
                    canvas.drawText(
                        getBubbleTextNumber(),
                        mMovePoint.x,
                        mMovePoint.y + getTextBaseline(),
                        mTextPaint
                    )
                }
                if (mBadgeState == BadgeState.BUBBLE.APART) {
                    canvas.drawCircle(mMovePoint.x, mMovePoint.y, mMoveRadius, mBadgePaint)
                    canvas.drawText(
                        getBubbleTextNumber(),
                        mMovePoint.x,
                        mMovePoint.y + getTextBaseline(),
                        mTextPaint
                    )
                }
                if (mBadgeState == BadgeState.BUBBLE.HIDE && mBmpIndex != mExplosionBmp.size) {
                    mExplosionRect.left = (mMovePoint.x - mMoveRadius).toInt()
                    mExplosionRect.right = (mMovePoint.x + mMoveRadius).toInt()
                    mExplosionRect.top = (mMovePoint.y - mMoveRadius).toInt()
                    mExplosionRect.bottom = (mMovePoint.y + mMoveRadius).toInt()
                    canvas.drawBitmap(mExplosionBmp[mBmpIndex], null, mExplosionRect, mBadgePaint)
                }
                if (mBadgeState == BadgeState.BUBBLE.HIDE && mBmpIndex == mExplosionBmp.size) {
                    resetPoint()
                }
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (mBadgeMode == BadgeMode.DOT)
            return super.onTouchEvent(event)
        if (mBadgeMode == BadgeMode.BUBBLE.NUMBER && mTextNumber == INIT_NUMBER)
            return super.onTouchEvent(event)
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                val mDistance = hypot(
                    (event.x - mFixedPoint.x).toDouble(),
                    (event.y - mFixedPoint.y).toDouble()
                )
                mBadgeState = if (mDistance <= mMoveRadius + mMinOffsetDistance) {
                    BadgeState.BUBBLE.CONNECT
                } else {
                    BadgeState.BUBBLE.DEFAULT
                }
            }

            MotionEvent.ACTION_MOVE -> {
                val mDistance = hypot(
                    (event.x - mFixedPoint.x).toDouble(),
                    (event.y - mFixedPoint.y).toDouble()
                )
                mMovePoint.set(event.x, event.y)
                if (mMoveRadius - mDistance / 15 >= 0.0) {
                    mFixedRadius = (mMoveRadius - mDistance / 15).toFloat()
                } else {
                    mBadgeState = BadgeState.BUBBLE.APART
                }
                invalidate()
            }

            MotionEvent.ACTION_UP -> {
                if (mBadgeState == BadgeState.BUBBLE.CONNECT) {
                    resetAnimation()
                } else if (mBadgeState == BadgeState.BUBBLE.APART) {
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
     * Set badge mode. Only useful when the current value of [mBadgeMode] is
     * [BadgeMode.UNSPECIFIED]
     *
     * @since 0.5.3
     */
    fun setMode(mode: BadgeMode) {
        if (mBadgeMode == BadgeMode.UNSPECIFIED) {
            mBadgeMode = mode
            visibility = VISIBLE
        }
        mBadgeState = when (mBadgeMode) {
            BadgeMode.DOT -> BadgeState.DOT.HIDE
            BadgeMode.BUBBLE.TEXT -> BadgeState.BUBBLE.DEFAULT
            BadgeMode.BUBBLE.NUMBER -> BadgeState.BUBBLE.DEFAULT
            BadgeMode.UNSPECIFIED -> BadgeState.UNSPECIFIED
        }
    }

    /**
     * Set the color-int of the badge by [colorInt].
     *
     * @since 0.5.3
     */
    fun setColor(@ColorInt colorInt: Int) {
        mBadgePaint.color = colorInt
    }

    /**
     * The setting will only take effect when the mode is
     * [BadgeMode.BUBBLE.NUMBER] or [BadgeMode.BUBBLE.TEXT].
     *
     * @param bubbleRadius The radius of bubble at the current finger touch
     *     position.
     * @since 0.5.3
     */
    fun setBubbleRadius(@FloatRange(from = 0.0) bubbleRadius: Float) {
        mMoveRadius = bubbleRadius.coerceAtLeast(mMoveRadius)
        mFixedRadius = mMoveRadius
    }

    /**
     * Set the text to be displayed in bubble.
     *
     * The [text] determines the minimum radius of the bubble. That is, the
     * minimum bubble is the circumscribed circle of the [text] bounding
     * rectangle.
     *
     * The setting will only take effect when the [mBadgeMode] is
     * [BadgeMode.BUBBLE.TEXT].
     *
     * @since 0.5.3
     */
    fun setBubbleText(text: String) {
        if (text.isEmpty()) return
        if (mBadgeMode == BadgeMode.UNSPECIFIED) return
        mText = text
        mMoveRadius = mMoveRadius.coerceAtLeast(mMinimumRadius)
        mFixedRadius = mMoveRadius
    }

    /**
     * Set the number to be displayed in bubble.
     *
     * The setting will only take effect when the [mBadgeMode] is
     * [BadgeMode.BUBBLE.NUMBER].
     *
     * @since 0.5.3
     */
    fun setBubbleTextNum(@IntRange(from = 0) number: Int) {
        if (mBadgeMode == BadgeMode.UNSPECIFIED) return
        mTextNumber = number.coerceAtMost(mTextMaxNumber)
    }

    /**
     * Set the max number to be displayed in bubble.
     *
     * The [maxNumber] determines the minimum radius of the bubble. For
     * example, if the value of maxNumber is 99, then the smallest bubble is
     * the circumscribed circle of the bounding rectangle of the string **99+**
     * in the specified style.
     *
     * The setting will only take effect when the [mBadgeMode] is
     * [BadgeMode.BUBBLE.NUMBER].
     *
     * @since 0.5.3
     */
    fun setBubbleTextMaxNum(@IntRange(from = 0) maxNumber: Int) {
        if (mBadgeMode == BadgeMode.UNSPECIFIED) return
        mTextMaxNumber = maxNumber
        mMoveRadius = mMoveRadius.coerceAtLeast(mMinimumRadius)
        mFixedRadius = mMoveRadius
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
        mTextPaint.color = colorInt
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
        if (mBadgeMode == BadgeMode.DOT) {
            mTextPaint.textSize = size
        }
    }

    /**
     * The setting will only take effect when the mode is [BadgeMode.DOT].
     *
     * @since 0.5.3
     */
    fun setDotRadius(dotRadius: Float) {
        if (mBadgeMode == BadgeMode.DOT) {
            mDotRadius = dotRadius
        }
    }

    /**
     * Hide dot. The setting will only take effect when the mode is
     * [BadgeMode.DOT].
     *
     * @since 0.5.3
     */
    fun hideDot() {
        if (mBadgeMode == BadgeMode.DOT) {
            mBadgeState = BadgeState.DOT.HIDE
        }
    }


    /**
     * Show dot. The setting will only take effect when the mode is
     * [BadgeMode.DOT].
     *
     * @since 0.5.3
     */
    fun showDot() {
        if (mBadgeMode == BadgeMode.DOT) {
            mBadgeState = BadgeState.DOT.SHOW
        }
    }

    /**
     * Get number string of [mTextNumber].
     *
     * @since 0.5.3
     */
    private fun getBubbleTextNumber() =
        if (mTextNumber == mTextMaxNumber) "$mTextMaxNumber+" else "$mTextNumber"

    private fun startExplosionAnim() {
        mBadgeState = BadgeState.BUBBLE.HIDE
        ValueAnimator.ofInt(0, mExplosionBmp.size).apply {
            interpolator = LinearInterpolator()
            duration = 800
            addUpdateListener {
                mBmpIndex = cast(it.animatedValue)
                invalidate()
            }
            start()
        }
    }

    private fun resetAnimation() {
        ValueAnimator.ofObject(PointFEvaluator(), mMovePoint, mFixedPoint).apply {
            duration = 1000
            interpolator = OvershootInterpolator(1F)
            addUpdateListener {
                mMovePoint = cast(it.animatedValue)
                invalidate()
            }
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    mBadgeState = BadgeState.BUBBLE.DEFAULT
                }
            })
            start()
        }
    }

    /**
     * Reset [mMovePoint] and [mFixedPoint].
     *
     * @since 0.5.3
     */
    private fun resetPoint() {
        mBadgeState = BadgeState.BUBBLE.DEFAULT
        mFixedRadius = mMoveRadius
        if (mBadgeMode == BadgeMode.BUBBLE.NUMBER) {
            mTextNumber = INIT_NUMBER
        }
        mMovePoint.x = (width / 2.0).toFloat()
        mMovePoint.y = (height / 2.0).toFloat()
        mFixedPoint.x = (width / 2.0).toFloat()
        mFixedPoint.y = (height / 2.0).toFloat()
    }

    /**
     * Get minimum radius of the bubble.
     *
     * @since 0.5.3
     */
    private fun String.getMinimumRadius(): Float {
        val textLength = mTextPaint.measureText(this)
        val fontMetrics = mTextPaint.fontMetrics
        val textHeight = fontMetrics.bottom - fontMetrics.top
        return (hypot(textLength, textHeight) / 2.0).toFloat()
    }

    /**
     * Get text baseline.
     *
     * @since 0.5.3
     */
    private fun getTextBaseline(): Float {
        val fontMetrics = mTextPaint.fontMetrics
        val textHeight = fontMetrics.bottom - fontMetrics.top
        return textHeight / 2 - fontMetrics.bottom
    }

    /**
     * Draw a Bezier curve connecting circles centered at [mMovePoint] and
     * [mFixedPoint].
     *
     * @since 0.5.3
     */
    private fun Canvas.drawBezier() {
        drawCircle(mFixedPoint.x, mFixedPoint.y, mFixedRadius, mBadgePaint)
        drawCircle(mMovePoint.x, mMovePoint.y, mMoveRadius, mBadgePaint)
        val mDistance = hypot(
            (mMovePoint.x - mFixedPoint.x).toDouble(),
            (mMovePoint.y - mFixedPoint.y).toDouble()
        )
        val sinAlpha = abs(mMovePoint.y - mFixedPoint.y) / mDistance
        val cosAlpha = abs(mMovePoint.x - mFixedPoint.x) / mDistance
        mControlPoint.x = (mMovePoint.x + mFixedPoint.x) / 2
        mControlPoint.y = (mMovePoint.y + mFixedPoint.y) / 2
        mPointA.x = (mFixedPoint.x - sinAlpha * mFixedRadius).toFloat()
        mPointA.y = (mFixedPoint.y - cosAlpha * mFixedRadius).toFloat()
        mPointB.x = (mFixedPoint.x + sinAlpha * mFixedRadius).toFloat()
        mPointB.y = (mFixedPoint.y + cosAlpha * mFixedRadius).toFloat()
        mPointC.x = (mMovePoint.x - sinAlpha * mMoveRadius).toFloat()
        mPointC.y = (mMovePoint.y - cosAlpha * mMoveRadius).toFloat()
        mPointD.x = (mMovePoint.x + sinAlpha * mMoveRadius).toFloat()
        mPointD.y = (mMovePoint.y + cosAlpha * mMoveRadius).toFloat()
        mBezierPath.reset()
        mBezierPath.moveTo(mPointA.x, mPointA.y)
        mBezierPath.quadTo(mControlPoint.x, mControlPoint.y, mPointC.x, mPointC.y)
        mBezierPath.lineTo(mPointD.x, mPointD.y)
        mBezierPath.quadTo(mControlPoint.x, mControlPoint.y, mPointB.x, mPointB.y)
        mBezierPath.close()
        drawPath(mBezierPath, mBezierPaint)
    }

    init {
        val typeArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.BadgeView,
            defStyleAttr,
            defStyleRes
        )
        mBadgeMode =
            when (typeArray.getInt(R.styleable.BadgeView_badge_mode, BadgeMode.UNSPECIFIED.code)) {
                BadgeMode.DOT.code -> BadgeMode.DOT
                BadgeMode.BUBBLE.TEXT.code -> BadgeMode.BUBBLE.TEXT
                BadgeMode.BUBBLE.NUMBER.code -> BadgeMode.BUBBLE.NUMBER
                else -> {
                    this.visibility = GONE
                    BadgeMode.UNSPECIFIED
                }
            }
        mBadgeState = when (mBadgeMode) {
            BadgeMode.DOT -> BadgeState.DOT.HIDE
            BadgeMode.BUBBLE.TEXT -> BadgeState.BUBBLE.DEFAULT
            BadgeMode.BUBBLE.NUMBER -> BadgeState.BUBBLE.DEFAULT
            BadgeMode.UNSPECIFIED -> BadgeState.UNSPECIFIED
        }
        mBadgePaint.color = typeArray.getColor(
            R.styleable.BadgeView_badge_color,
            ContextCompat.getColor(context, R.color.md_theme_error)
        )
        mDotRadius = typeArray.getDimension(
            R.styleable.BadgeView_dot_radius, initDotRadius
        )
        typeArray.getString(R.styleable.BadgeView_bubble_text)?.also { string ->
            setBubbleText(string)
        }
        typeArray.getInteger(R.styleable.BadgeView_bubble_text_max_num, INIT_MAX_NUMBER)
            .also { num ->
                val value = if (num <= 0) INIT_MAX_NUMBER else num
                setBubbleTextMaxNum(value)
            }
        setBubbleTextNum(INIT_NUMBER)
        setBubbleRadius(
            typeArray.getDimension(R.styleable.BadgeView_bubble_radius, initBubbleRadius)
        )
        mTextPaint.color = typeArray.getColor(
            R.styleable.BadgeView_bubble_text_color,
            ContextCompat.getColor(context, R.color.white)
        )
        mTextPaint.textSize = typeArray.getDimension(
            R.styleable.BadgeView_bubble_text_size, initTextSize
        )
        typeArray.recycle()
    }

}