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

package cn.govast.vastmenufab

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.*
import android.graphics.drawable.*
import android.graphics.drawable.shapes.RoundRectShape
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.View
import android.view.ViewOutlineProvider
import android.view.animation.Animation
import androidx.appcompat.widget.AppCompatTextView
import kotlin.math.abs
import kotlin.math.roundToInt

/**
 * The menu label.
 *
 * @property mShadowRadius the radius of shadow.
 * @property mShadowXOffset
 * @property mShowShadow true if show your customize shadow, false otherwise.
 * @property mColorRipple color int for ripple.
 * @property mFab the [FloatingActionButton] of the label.
 * @property mShowAnimation the animation of show the label.
 *
 * @param context
 * @param attrs
 * @param defStyleAttr
 *
 * @since 0.0.1
 */
class LabelView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = android.R.attr.textViewStyle
) : AppCompatTextView(context, attrs, defStyleAttr) {

    private var mShadowRadius = 0f
    private var mShadowXOffset = 0f
    private var mShadowYOffset = 0f
    private var mShadowColor = 0
    private var mBackgroundDrawable: Drawable? = null
    private var mShowShadow = true
    private var mRawWidth = 0
    private var mRawHeight = 0
    private var mColorNormal = 0
    private var mColorPressed = 0
    private var mColorRipple = 0
    private var mCornerRadius = 0
    private var mFab: FloatingActionButton? = null
    private var mShowAnimation: Animation? = null
    private var mHideAnimation: Animation? = null
    private var mUsingStyle = false
    var isHandleVisibilityChanges = true

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(calculateMeasuredWidth(), calculateMeasuredHeight())
    }

    private fun calculateMeasuredWidth(): Int {
        if (mRawWidth == 0) {
            mRawWidth = measuredWidth
        }
        return measuredWidth + calculateShadowWidth().roundToInt()
    }

    private fun calculateMeasuredHeight(): Int {
        if (mRawHeight == 0) {
            mRawHeight = measuredHeight
        }
        return measuredHeight + calculateShadowHeight().roundToInt()
    }

    fun calculateShadowWidth() = if (mShowShadow) mShadowRadius + abs(mShadowXOffset) else 0f

    fun calculateShadowHeight() = if (mShowShadow) mShadowRadius + abs(mShadowYOffset) else 0f

    /**
     * Update the [LabelView] background.
     *
     * @since 0.0.1
     */
    fun updateBackground() {
        val layerDrawable: LayerDrawable
        if (mShowShadow) {
            layerDrawable = LayerDrawable(arrayOf(Shadow(), createFillDrawable()))
            val leftInset = mShadowRadius + abs(mShadowXOffset)
            val topInset = mShadowRadius + abs(mShadowYOffset)
            val rightInset = mShadowRadius + abs(mShadowXOffset)
            val bottomInset = mShadowRadius + abs(mShadowYOffset)
            layerDrawable.setLayerInset(1,
                leftInset.roundToInt(),
                topInset.roundToInt(),
                rightInset.roundToInt(),
                bottomInset.roundToInt())
        } else {
            layerDrawable = LayerDrawable(arrayOf(createFillDrawable()))
        }
        setBackgroundCompat(layerDrawable)
    }

    private fun createFillDrawable(): Drawable {
        val drawable = StateListDrawable()
        drawable.addState(
            intArrayOf(android.R.attr.state_pressed),
            createRectDrawable(mColorPressed)
        )
        drawable.addState(intArrayOf(), createRectDrawable(mColorNormal))
        val ripple = RippleDrawable(
            ColorStateList(arrayOf(intArrayOf()), intArrayOf(mColorRipple)),
            drawable,
            null
        )
        outlineProvider = object : ViewOutlineProvider() {
            override fun getOutline(view: View, outline: Outline) {
                outline.setOval(0, 0, view.width, view.height)
            }
        }
        clipToOutline = true
        mBackgroundDrawable = ripple
        return ripple
    }

    private fun createRectDrawable(color: Int): Drawable {
        val shape = RoundRectShape(
            floatArrayOf(
                mCornerRadius.toFloat(),
                mCornerRadius.toFloat(),
                mCornerRadius.toFloat(),
                mCornerRadius.toFloat(),
                mCornerRadius.toFloat(),
                mCornerRadius.toFloat(),
                mCornerRadius.toFloat(),
                mCornerRadius.toFloat()
            ), null, null
        )
        val shapeDrawable = ShapeDrawable(shape)
        shapeDrawable.paint.color = color
        return shapeDrawable
    }

    /**
     * Set shadow by [fab].
     *
     * @param fab the [FloatingActionButton].
     * @since 0.0.1
     */
    private fun setShadow(fab: FloatingActionButton) {
        mShadowColor = fab.shadowColor
        mShadowRadius = fab.shadowRadius
        mShadowXOffset = fab.shadowXOffset
        mShadowYOffset = fab.shadowYOffset
        mShowShadow = fab.hasShadow()
    }

    private fun setBackgroundCompat(drawable: Drawable) {
        background = drawable
    }

    private fun playShowAnimation() {
        if (mShowAnimation != null) {
            mHideAnimation!!.cancel()
            startAnimation(mShowAnimation)
        }
    }

    private fun playHideAnimation() {
        if (mHideAnimation != null) {
            mShowAnimation!!.cancel()
            startAnimation(mHideAnimation)
        }
    }

    fun onActionDown() {
        if (mUsingStyle) {
            mBackgroundDrawable = background
        }
        if (mBackgroundDrawable is StateListDrawable) {
            val drawable = mBackgroundDrawable as StateListDrawable
            drawable.state = intArrayOf(android.R.attr.state_pressed)
        } else if (mBackgroundDrawable is RippleDrawable) {
            val ripple = mBackgroundDrawable as RippleDrawable
            ripple.state = intArrayOf(android.R.attr.state_enabled, android.R.attr.state_pressed)
            ripple.setHotspot((measuredWidth / 2).toFloat(), measuredHeight.toFloat() / 2)
            ripple.setVisible(true, true)
        }
    }

    fun onActionUp() {
        if (mUsingStyle) {
            mBackgroundDrawable = background
        }
        if (mBackgroundDrawable is StateListDrawable) {
            val drawable = mBackgroundDrawable as StateListDrawable
            drawable.state = intArrayOf()
        } else if (mBackgroundDrawable is RippleDrawable) {
            val ripple = mBackgroundDrawable as RippleDrawable
            ripple.state = intArrayOf()
            ripple.setHotspot(measuredWidth.toFloat() / 2, measuredHeight.toFloat() / 2)
            ripple.setVisible(true, true)
        }
    }

    /**
     * Set [mFab] for the label by [fab].
     *
     * @since 0.0.1
     */
    fun setFab(fab: FloatingActionButton) {
        mFab = fab
        setShadow(fab)
    }

    /**
     * Determine whether show the customize shadow.
     *
     * @param show true if you want to show customize shadow, false otherwise.
     * @since 0.0.1
     */
    fun setShowShadow(show: Boolean) {
        mShowShadow = show
    }

    fun setCornerRadius(cornerRadius: Int) {
        mCornerRadius = cornerRadius
    }


    fun setColors(colorNormal: Int, colorPressed: Int, colorRipple: Int) {
        mColorNormal = colorNormal
        mColorPressed = colorPressed
        mColorRipple = colorRipple
    }

    fun show(animate: Boolean) {
        if (animate) {
            playShowAnimation()
        }
        visibility = VISIBLE
    }

    fun hide(animate: Boolean) {
        if (animate) {
            playHideAnimation()
        }
        visibility = INVISIBLE
    }

    fun setShowAnimation(showAnimation: Animation?) {
        mShowAnimation = showAnimation
    }

    fun setHideAnimation(hideAnimation: Animation?) {
        mHideAnimation = hideAnimation
    }

    fun setUsingStyle(usingStyle: Boolean) {
        mUsingStyle = usingStyle
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (mFab == null || mFab!!.clickListener == null || !mFab!!.isEnabled) {
            return super.onTouchEvent(event)
        }
        when (event.action) {
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                onActionUp()
                mFab!!.onActionUp()
            }
        }
        mGestureDetector.onTouchEvent(event)
        return super.onTouchEvent(event)
    }

    var mGestureDetector = GestureDetector(context, object : SimpleOnGestureListener() {
        override fun onDown(e: MotionEvent): Boolean {
            onActionDown()
            if (mFab != null) {
                mFab!!.onActionDown()
            }
            return super.onDown(e)
        }

        override fun onSingleTapUp(e: MotionEvent): Boolean {
            onActionUp()
            if (mFab != null) {
                mFab!!.onActionUp()
            }
            return super.onSingleTapUp(e)
        }
    })

    private inner class Shadow : Drawable() {
        private val mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        private val mErase = Paint(Paint.ANTI_ALIAS_FLAG)

        init {
            setLayerType(LAYER_TYPE_SOFTWARE, null)
            mPaint.style = Paint.Style.FILL
            mPaint.color = mColorNormal
            mErase.xfermode = PORTER_DUFF_CLEAR
            if (!isInEditMode) {
                mPaint.setShadowLayer(
                    mShadowRadius,
                    mShadowXOffset,
                    mShadowYOffset,
                    mShadowColor
                )
            }
        }

        override fun draw(canvas: Canvas) {
            val shadowRect = RectF(
                (mShadowRadius + abs(mShadowXOffset)),
                (mShadowRadius + abs(mShadowYOffset)),
                mRawWidth.toFloat(),
                mRawHeight.toFloat()
            )
            canvas.drawRoundRect(
                shadowRect,
                mCornerRadius.toFloat(),
                mCornerRadius.toFloat(),
                mPaint
            )
            canvas.drawRoundRect(
                shadowRect,
                mCornerRadius.toFloat(),
                mCornerRadius.toFloat(),
                mErase
            )
        }

        override fun setAlpha(alpha: Int) {}

        override fun setColorFilter(cf: ColorFilter?) {}

        @Deprecated(
            "Deprecated in Java",
            ReplaceWith("PixelFormat.TRANSPARENT", "android.graphics.PixelFormat"),
            DeprecationLevel.WARNING
        )
        override fun getOpacity(): Int {
            return PixelFormat.TRANSPARENT
        }

    }

    companion object {
        private val PORTER_DUFF_CLEAR: Xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
    }
}