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
import android.content.res.TypedArray
import android.graphics.*
import android.graphics.drawable.*
import android.graphics.drawable.shapes.OvalShape
import android.graphics.drawable.shapes.Shape
import android.os.Parcel
import android.os.Parcelable
import android.os.SystemClock
import android.util.AttributeSet
import android.view.*
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.ViewGroup.MarginLayoutParams
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.annotation.DimenRes
import androidx.annotation.IntDef
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.content.res.ResourcesCompat
import cn.govast.vastmenufab.Util.dp2px
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.roundToInt

open class FloatingActionButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = androidx.appcompat.R.attr.imageButtonStyle
) : AppCompatImageButton(context, attrs, defStyleAttr) {

    /**
     * The log tag for the [FloatingActionButton].
     *
     * @since 0.0.1
     */
    private val tag = this.javaClass.simpleName

    /**
     * The size of the [FloatingActionButton].Accepted values: [SIZE_NORMAL],
     * [SIZE_MINI].
     *
     * @see setFabSize
     * @since 0.0.1
     */
    var fabSize = SIZE_NORMAL
        private set

    /**
     * True if you want to show the shadow, false otherwise. Default value is
     * false.
     *
     * @see setShowShadow
     * @since 0.0.1
     */
    var showShadow = false
        private set

    /**
     * Shadow color int value.
     *
     * @see setShadowColorResId
     * @since 0.0.1
     */
    var shadowColor = 0
        set(color) {
            if (field != color) {
                field = color
                updateBackground()
            }
        }

    /**
     * Sets the shadow radius of the [FloatingActionButton]
     *
     * @see setShadowRadius
     * @since 0.0.1
     */
    var shadowRadius = dp2px(4f)
        private set

    /**
     * The shadow x offset of the [FloatingActionButton].
     *
     * @see setShadowXOffset
     * @since 0.0.1
     */
    var shadowXOffset = dp2px(1f)
        private set

    /**
     * The shadow y offset of the [FloatingActionButton].
     *
     * @see setShadowYOffset
     * @since 0.0.1
     */
    var shadowYOffset = dp2px(3f)
        private set

    /**
     * The color [FloatingActionButton] in normal state.
     *
     * @see setColorNormalResId
     * @see setColors
     * @since 0.0.1
     */
    var colorNormal = 0
        set(value) {
            if (field != value) {
                field = value
                updateBackground()
            }
        }

    /**
     * The color [FloatingActionButton] in pressed state.
     *
     * @see setColorPressedResId
     * @see setColors
     * @since 0.0.1
     */
    var colorPressed = 0
        set(value) {
            if (field != value) {
                field = value
                updateBackground()
            }
        }

    /**
     * The color [FloatingActionButton] in disabled state.
     *
     * @see setColorDisabledResId
     * @see setColors
     * @since 0.0.1
     */
    var colorDisabled = 0
        set(value) {
            if (field != value) {
                field = value
                updateBackground()
            }
        }

    /**
     * The color ripple [FloatingActionButton].
     *
     * @see setColorRippleResId
     * @see setColors
     * @since 0.0.1
     */
    var colorRipple = 0
        set(value) {
            if (field != value) {
                field = value
                updateBackground()
            }
        }

    /**
     * The icon drawable.
     *
     * @see setImageDrawable
     * @see setImageResource
     * @since 0.0.1
     */
    private var mIcon: Drawable? = null

    /**
     * The icon drawable size.Default size is 24dp.
     *
     * @since 0.0.1
     */
    private val mIconSize = dp2px(24f)

    val iconDrawable: Drawable?
        get() = if (mIcon != null) {
            mIcon
        } else {
            ColorDrawable(Color.TRANSPARENT)
        }

    /**
     * [FloatingActionButton] click listener.
     *
     * @see setOnClickListener
     * @since 0.0.1
     */
    var clickListener: OnClickListener? = null
        private set

    /**
     * The [LabelView] of the button.
     *
     * @see setLabelView
     * @since 0.0.1
     */
    var labelView: LabelView? = null
        private set

    /**
     * The text of the label.
     *
     * @since 0.0.1
     */
    var labelText: String = ""
        get() = if(null != labelView) labelView!!.text.toString() else field
        set(text) {
            field = text
            labelView?.text = text
        }

    /**
     * The visibility of the label.
     *
     * @since 0.0.1
     */
    var labelVisibility: Int
        get() {
            return labelView?.visibility ?: -1
        }
        set(visibility) {
            labelView?.visibility = visibility
            labelView?.isHandleVisibilityChanges = visibility == VISIBLE
        }

    var showAnimation: Animation? = null
    var hideAnimation: Animation? = null

    /**
     * True if it is the main fab in [FloatingActionMenu], false otherwise.
     * Default value is false.
     *
     * @see setMenuMain
     * @since 0.0.1
     */
    internal var isMenuMain = false
        private set

    private var mBackgroundDrawable: Drawable? = null
    private var mUsingElevation = false
    private var mUsingElevationCompat = false

    // Progress
    private var mProgressBarEnabled = false
    private var mProgressWidth = dp2px(6f)
    private var mProgressColor = 0
    private var mProgressBackgroundColor = 0
    private var mShouldUpdateButtonPosition = false
    private var mOriginalX = -1f
    private var mOriginalY = -1f
    private var mButtonPositionSaved = false
    private var mProgressCircleBounds = RectF()
    private val mBackgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mProgressPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var mProgressIndeterminate = false
    private var mLastTimeAnimated: Long = 0
    private var mSpinSpeed = 195.0f //The amount of degrees per second
    private var mPausedTimeWithoutGrowing: Long = 0
    private var mTimeStartGrowing = 0.0
    private var mBarGrowingFromFront = true
    private val mBarLength = 16
    private var mBarExtraLength = 0f
    private var mCurrentProgress = 0f
    private var mTargetProgress = 0f
    private var mProgress = 0
    private var mAnimateProgress = false
    private var mShouldProgressIndeterminate = false
    private var mShouldSetProgress = false

    @get:Synchronized
    @set:Synchronized
    var max = 100

    @get:Synchronized
    var isProgressBackgroundShown = false
        private set

    private fun initShowAnimation(attr: TypedArray) {
        val resourceId = attr.getResourceId(
            R.styleable.FloatingActionButton_fab_showAnimation,
            R.anim.fab_scale_up
        )
        showAnimation = AnimationUtils.loadAnimation(context, resourceId)
    }

    private fun initHideAnimation(attr: TypedArray) {
        val resourceId = attr.getResourceId(
            R.styleable.FloatingActionButton_fab_hideAnimation,
            R.anim.fab_scale_down
        )
        hideAnimation = AnimationUtils.loadAnimation(context, resourceId)
    }

    private val circleSize: Int
        get() = resources.getDimensionPixelSize(if (fabSize == SIZE_NORMAL) R.dimen.fab_size_normal else R.dimen.fab_size_mini)

    private fun calculateMeasuredWidth(): Int {
        var width = circleSize + calculateShadowWidth()
        if (mProgressBarEnabled) {
            width += mProgressWidth.roundToInt() * 2
        }
        return width
    }

    private fun calculateMeasuredHeight(): Int {
        var height = circleSize + calculateShadowHeight()
        if (mProgressBarEnabled) {
            height += mProgressWidth.roundToInt() * 2
        }
        return height
    }

    fun calculateShadowWidth(): Int {
        return if (hasShadow()) (shadowX * 2).roundToInt() else 0
    }

    fun calculateShadowHeight(): Int {
        return if (hasShadow()) (shadowY * 2).roundToInt() else 0
    }

    private val shadowX: Float
        get() = shadowRadius + abs(shadowXOffset)

    private val shadowY: Float
        get() = shadowRadius + abs(shadowYOffset)

    private fun calculateCenterX(): Float {
        return (measuredWidth / 2).toFloat()
    }

    private fun calculateCenterY(): Float {
        return (measuredHeight / 2).toFloat()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(calculateMeasuredWidth(), calculateMeasuredHeight())
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (mProgressBarEnabled) {
            if (isProgressBackgroundShown) {
                canvas.drawArc(mProgressCircleBounds, 360f, 360f, false, mBackgroundPaint)
            }
            var shouldInvalidate = false
            if (mProgressIndeterminate) {
                shouldInvalidate = true
                val deltaTime = SystemClock.uptimeMillis() - mLastTimeAnimated
                val deltaNormalized = deltaTime * mSpinSpeed / 1000.0f
                updateProgressLength(deltaTime)
                mCurrentProgress += deltaNormalized
                if (mCurrentProgress > 360f) {
                    mCurrentProgress -= 360f
                }
                mLastTimeAnimated = SystemClock.uptimeMillis()
                var from = mCurrentProgress - 90
                var to = mBarLength + mBarExtraLength
                if (isInEditMode) {
                    from = 0f
                    to = 135f
                }
                canvas.drawArc(mProgressCircleBounds, from, to, false, mProgressPaint)
            } else {
                if (mCurrentProgress != mTargetProgress) {
                    shouldInvalidate = true
                    val deltaTime =
                        (SystemClock.uptimeMillis() - mLastTimeAnimated).toFloat() / 1000
                    val deltaNormalized = deltaTime * mSpinSpeed
                    mCurrentProgress = if (mCurrentProgress > mTargetProgress) {
                        (mCurrentProgress - deltaNormalized).coerceAtLeast(mTargetProgress)
                    } else {
                        (mCurrentProgress + deltaNormalized).coerceAtMost(mTargetProgress)
                    }
                    mLastTimeAnimated = SystemClock.uptimeMillis()
                }
                canvas.drawArc(mProgressCircleBounds, -90f, mCurrentProgress, false, mProgressPaint)
            }
            if (shouldInvalidate) {
                invalidate()
            }
        }
    }

    private fun updateProgressLength(deltaTimeInMillis: Long) {
        if (mPausedTimeWithoutGrowing >= PAUSE_GROWING_TIME) {
            mTimeStartGrowing += deltaTimeInMillis.toDouble()
            if (mTimeStartGrowing > BAR_SPIN_CYCLE_TIME) {
                mTimeStartGrowing -= BAR_SPIN_CYCLE_TIME
                mPausedTimeWithoutGrowing = 0
                mBarGrowingFromFront = !mBarGrowingFromFront
            }
            val distance = cos((mTimeStartGrowing / BAR_SPIN_CYCLE_TIME + 1) * Math.PI)
                .toFloat() / 2 + 0.5f
            val length = (BAR_MAX_LENGTH - mBarLength).toFloat()
            if (mBarGrowingFromFront) {
                mBarExtraLength = distance * length
            } else {
                val newLength = length * (1 - distance)
                mCurrentProgress += mBarExtraLength - newLength
                mBarExtraLength = newLength
            }
        } else {
            mPausedTimeWithoutGrowing += deltaTimeInMillis
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        saveButtonOriginalPosition()
        if (mShouldProgressIndeterminate) {
            setIndeterminate(true)
            mShouldProgressIndeterminate = false
        } else if (mShouldSetProgress) {
            setProgress(mProgress, mAnimateProgress)
            mShouldSetProgress = false
        } else if (mShouldUpdateButtonPosition) {
            updateButtonPosition()
            mShouldUpdateButtonPosition = false
        }
        super.onSizeChanged(w, h, oldw, oldh)
        setupProgressBounds()
        setupProgressBarPaints()
        updateBackground()
    }

    override fun setLayoutParams(params: ViewGroup.LayoutParams) {
        if (params is MarginLayoutParams && mUsingElevationCompat) {
            params.leftMargin += shadowX.roundToInt()
            params.topMargin += shadowY.roundToInt()
            params.rightMargin += shadowX.roundToInt()
            params.bottomMargin += shadowY.roundToInt()
        }
        super.setLayoutParams(params)
    }

    fun updateBackground() {
        val layerDrawable: LayerDrawable = if (hasShadow()) {
            LayerDrawable(
                arrayOf(
                    Shadow(),
                    createFillDrawable(),
                    iconDrawable
                )
            )
        } else {
            LayerDrawable(
                arrayOf(
                    createFillDrawable(),
                    iconDrawable
                )
            )
        }
        var iconSize = -1
        if (iconDrawable != null) {
            iconSize = iconDrawable!!.intrinsicWidth.coerceAtLeast(iconDrawable!!.intrinsicHeight)
        }
        val iconOffset = (circleSize - if (iconSize > 0) iconSize else mIconSize.roundToInt()) / 2
        var circleInsetHorizontal = if (hasShadow()) shadowRadius + abs(shadowXOffset) else 0f
        var circleInsetVertical = if (hasShadow()) shadowRadius + abs(shadowYOffset) else 0f
        if (mProgressBarEnabled) {
            circleInsetHorizontal += mProgressWidth
            circleInsetVertical += mProgressWidth
        }
        layerDrawable.setLayerInset(
            if (hasShadow()) 2 else 1,
            circleInsetHorizontal.roundToInt() + iconOffset,
            circleInsetVertical.roundToInt() + iconOffset,
            circleInsetHorizontal.roundToInt() + iconOffset,
            circleInsetVertical.roundToInt() + iconOffset
        )
        setBackgroundCompat(layerDrawable)
    }

    private fun createFillDrawable(): Drawable {
        val drawable = StateListDrawable()
        drawable.addState(
            intArrayOf(-android.R.attr.state_enabled),
            createCircleDrawable(colorDisabled)
        )
        drawable.addState(
            intArrayOf(android.R.attr.state_pressed),
            createCircleDrawable(colorPressed)
        )
        drawable.addState(intArrayOf(), createCircleDrawable(colorNormal))
        val ripple = RippleDrawable(
            ColorStateList(arrayOf(intArrayOf()), intArrayOf(colorRipple)),
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

    private fun createCircleDrawable(color: Int): Drawable {
        val shapeDrawable = CircleDrawable(OvalShape())
        shapeDrawable.paint.color = color
        return shapeDrawable
    }

    private fun setBackgroundCompat(drawable: Drawable) {
        background = drawable
    }

    private fun saveButtonOriginalPosition() {
        if (!mButtonPositionSaved) {
            if (mOriginalX == -1f) {
                mOriginalX = x
            }
            if (mOriginalY == -1f) {
                mOriginalY = y
            }
            mButtonPositionSaved = true
        }
    }

    private fun updateButtonPosition() {
        val x: Float
        val y: Float
        if (mProgressBarEnabled) {
            x = if (mOriginalX > getX()) getX() + mProgressWidth else getX() - mProgressWidth
            y = if (mOriginalY > getY()) getY() + mProgressWidth else getY() - mProgressWidth
        } else {
            x = mOriginalX
            y = mOriginalY
        }
        setX(x)
        setY(y)
    }

    private fun setupProgressBarPaints() {
        mBackgroundPaint.color = mProgressBackgroundColor
        mBackgroundPaint.style = Paint.Style.STROKE
        mBackgroundPaint.strokeWidth = mProgressWidth.toFloat()
        mProgressPaint.color = mProgressColor
        mProgressPaint.style = Paint.Style.STROKE
        mProgressPaint.strokeWidth = mProgressWidth.toFloat()
    }

    private fun setupProgressBounds() {
        val circleInsetHorizontal = if (hasShadow()) shadowX else 0f
        val circleInsetVertical = if (hasShadow()) shadowY else 0f
        mProgressCircleBounds = RectF(
            (circleInsetHorizontal + mProgressWidth / 2),
            (circleInsetVertical + mProgressWidth / 2),
            (calculateMeasuredWidth() - circleInsetHorizontal - mProgressWidth / 2),
            (calculateMeasuredHeight() - circleInsetVertical - mProgressWidth / 2)
        )
    }

    fun playShowAnimation() {
        hideAnimation!!.cancel()
        startAnimation(showAnimation)
    }

    fun playHideAnimation() {
        showAnimation!!.cancel()
        startAnimation(hideAnimation)
    }

    @JvmOverloads
    fun setColors(
        colorNormal: Int = 0,
        colorPressed: Int = 0,
        colorDisabled: Int = 0,
        colorRipple: Int = 0
    ) {
        this.colorNormal = colorNormal
        this.colorPressed = colorPressed
        this.colorDisabled = colorDisabled
        this.colorRipple = colorRipple
    }

    fun onActionDown() {
        if (mBackgroundDrawable is StateListDrawable) {
            val drawable = mBackgroundDrawable as StateListDrawable
            drawable.state = intArrayOf(android.R.attr.state_enabled, android.R.attr.state_pressed)
        } else {
            val ripple = mBackgroundDrawable as RippleDrawable?
            ripple!!.state = intArrayOf(android.R.attr.state_enabled, android.R.attr.state_pressed)
            ripple.setHotspot(calculateCenterX(), calculateCenterY())
            ripple.setVisible(true, true)
        }
    }

    fun onActionUp() {
        if (mBackgroundDrawable is StateListDrawable) {
            val drawable = mBackgroundDrawable as StateListDrawable
            drawable.state = intArrayOf(android.R.attr.state_enabled)
        } else {
            val ripple = mBackgroundDrawable as RippleDrawable?
            ripple!!.state = intArrayOf(android.R.attr.state_enabled)
            ripple.setHotspot(calculateCenterX(), calculateCenterY())
            ripple.setVisible(true, true)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (clickListener != null && isEnabled) {
            when (event.action) {
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    labelView?.onActionUp()
                    onActionUp()
                }
            }
            mGestureDetector.onTouchEvent(event)
        }
        return super.onTouchEvent(event)
    }

    var mGestureDetector = GestureDetector(getContext(), object : SimpleOnGestureListener() {
        override fun onDown(e: MotionEvent): Boolean {
            labelView?.onActionDown()
            onActionDown()
            return super.onDown(e)
        }

        override fun onSingleTapUp(e: MotionEvent): Boolean {
            labelView?.onActionUp()
            onActionUp()
            return super.onSingleTapUp(e)
        }
    })

    public override fun onSaveInstanceState(): Parcelable {
        val superState = super.onSaveInstanceState()
        val ss = ProgressSavedState(superState)
        ss.mCurrentProgress = mCurrentProgress
        ss.mTargetProgress = mTargetProgress
        ss.mSpinSpeed = mSpinSpeed
        ss.mProgressWidth = mProgressWidth.roundToInt()
        ss.mProgressColor = mProgressColor
        ss.mProgressBackgroundColor = mProgressBackgroundColor
        ss.mShouldProgressIndeterminate = mProgressIndeterminate
        ss.mShouldSetProgress = mProgressBarEnabled && mProgress > 0 && !mProgressIndeterminate
        ss.mProgress = mProgress
        ss.mAnimateProgress = mAnimateProgress
        ss.mShowProgressBackground = isProgressBackgroundShown
        return ss
    }

    public override fun onRestoreInstanceState(state: Parcelable) {
        if (state !is ProgressSavedState) {
            super.onRestoreInstanceState(state)
            return
        }
        super.onRestoreInstanceState(state.superState)
        mCurrentProgress = state.mCurrentProgress
        mTargetProgress = state.mTargetProgress
        mSpinSpeed = state.mSpinSpeed
        mProgressWidth = state.mProgressWidth.toFloat()
        mProgressColor = state.mProgressColor
        mProgressBackgroundColor = state.mProgressBackgroundColor
        mShouldProgressIndeterminate = state.mShouldProgressIndeterminate
        mShouldSetProgress = state.mShouldSetProgress
        mProgress = state.mProgress
        mAnimateProgress = state.mAnimateProgress
        isProgressBackgroundShown = state.mShowProgressBackground
        mLastTimeAnimated = SystemClock.uptimeMillis()
    }

    private inner class CircleDrawable(s: Shape) : ShapeDrawable(s) {

        private var circleInsetHorizontal = 0
        private var circleInsetVertical = 0

        init {
            circleInsetHorizontal = if (hasShadow()) (shadowRadius + abs(shadowXOffset)).roundToInt() else 0
            circleInsetVertical = if (hasShadow()) (shadowRadius + abs(shadowYOffset)).roundToInt() else 0
            if (mProgressBarEnabled) {
                circleInsetHorizontal += mProgressWidth.roundToInt()
                circleInsetVertical += mProgressWidth.roundToInt()
            }
        }

        override fun draw(canvas: Canvas) {
            setBounds(
                circleInsetHorizontal, circleInsetVertical, calculateMeasuredWidth()
                        - circleInsetHorizontal, calculateMeasuredHeight() - circleInsetVertical
            )
            super.draw(canvas)
        }
    }

    private inner class Shadow : Drawable() {
        private val mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        private val mErase = Paint(Paint.ANTI_ALIAS_FLAG)
        private var mRadius = 0f

        init {
            this.init()
        }

        private fun init() {
            setLayerType(LAYER_TYPE_SOFTWARE, null)
            mPaint.style = Paint.Style.FILL
            mPaint.color = colorNormal
            mErase.xfermode = PORTER_DUFF_CLEAR
            if (!isInEditMode) {
                mPaint.setShadowLayer(
                    shadowRadius,
                    shadowXOffset,
                    shadowYOffset,
                    shadowColor
                )
            }
            mRadius = (circleSize / 2).toFloat()
            if (mProgressBarEnabled && isProgressBackgroundShown) {
                mRadius += mProgressWidth.toFloat()
            }
        }

        override fun draw(canvas: Canvas) {
            canvas.drawCircle(calculateCenterX(), calculateCenterY(), mRadius, mPaint)
            canvas.drawCircle(calculateCenterX(), calculateCenterY(), mRadius, mErase)
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

    internal class ProgressSavedState : BaseSavedState {
        var mCurrentProgress = 0f
        var mTargetProgress = 0f
        var mSpinSpeed = 0f
        var mProgress = 0
        var mProgressWidth = 0
        var mProgressColor = 0
        var mProgressBackgroundColor = 0
        var mProgressBarEnabled = false
        var mProgressBarVisibilityChanged = false
        var mProgressIndeterminate = false
        var mShouldProgressIndeterminate = false
        var mShouldSetProgress = false
        var mAnimateProgress = false
        var mShowProgressBackground = false

        constructor(superState: Parcelable?) : super(superState)
        private constructor(`in`: Parcel) : super(`in`) {
            mCurrentProgress = `in`.readFloat()
            mTargetProgress = `in`.readFloat()
            mProgressBarEnabled = `in`.readInt() != 0
            mSpinSpeed = `in`.readFloat()
            mProgress = `in`.readInt()
            mProgressWidth = `in`.readInt()
            mProgressColor = `in`.readInt()
            mProgressBackgroundColor = `in`.readInt()
            mProgressBarVisibilityChanged = `in`.readInt() != 0
            mProgressIndeterminate = `in`.readInt() != 0
            mShouldProgressIndeterminate = `in`.readInt() != 0
            mShouldSetProgress = `in`.readInt() != 0
            mAnimateProgress = `in`.readInt() != 0
            mShowProgressBackground = `in`.readInt() != 0
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeFloat(mCurrentProgress)
            out.writeFloat(mTargetProgress)
            out.writeInt(if (mProgressBarEnabled) 1 else 0)
            out.writeFloat(mSpinSpeed)
            out.writeInt(mProgress)
            out.writeInt(mProgressWidth)
            out.writeInt(mProgressColor)
            out.writeInt(mProgressBackgroundColor)
            out.writeInt(if (mProgressBarVisibilityChanged) 1 else 0)
            out.writeInt(if (mProgressIndeterminate) 1 else 0)
            out.writeInt(if (mShouldProgressIndeterminate) 1 else 0)
            out.writeInt(if (mShouldSetProgress) 1 else 0)
            out.writeInt(if (mAnimateProgress) 1 else 0)
            out.writeInt(if (mShowProgressBackground) 1 else 0)
        }

        companion object {
            @JvmField
            val CREATOR: Parcelable.Creator<ProgressSavedState?> =
                object : Parcelable.Creator<ProgressSavedState?> {
                    override fun createFromParcel(`in`: Parcel): ProgressSavedState {
                        return ProgressSavedState(`in`)
                    }

                    override fun newArray(size: Int): Array<ProgressSavedState?> {
                        return arrayOfNulls(size)
                    }
                }
        }
    }

    override fun setImageDrawable(drawable: Drawable?) {
        if (mIcon !== drawable) {
            mIcon = drawable
            updateBackground()
        }
    }

    override fun setImageResource(resId: Int) {
        val drawable = ResourcesCompat.getDrawable(resources, resId, context.theme)
        if (mIcon !== drawable) {
            mIcon = drawable
            updateBackground()
        }
    }

    /**
     * Set [labelView] by [labelView].
     *
     * @since 0.0.1
     */
    internal fun setLabelView(labelView: LabelView?) {
        this.labelView = labelView
    }

    /**
     * Sets the size of the [FloatingActionButton] and invalidates its layout.
     *
     * @param size size of the [FloatingActionButton]. Accepted values:
     *     [SIZE_NORMAL], [SIZE_MINI].
     * @since 0.0.1
     */
    fun setFabSize(@FabSize size: Int) {
        if (fabSize != size) {
            fabSize = size
            updateBackground()
        }
    }

    /**
     * True if you want to show the shadow, false otherwise.
     *
     * @since 0.0.1
     */
    fun setShowShadow(show: Boolean) {
        if (showShadow != show) {
            showShadow = show
            updateBackground()
        }
    }

    /**
     * Set shadow color by [colorResId]
     *
     * @param colorResId color resource id.
     * @since 0.0.1
     */
    fun setShadowColorResId(colorResId: Int) {
        val newShadowColor = resources.getColor(colorResId, null)
        if (shadowColor != newShadowColor) {
            shadowColor = newShadowColor
            updateBackground()
        }
    }

    /**
     * Sets the shadow radius of the [FloatingActionButton] and invalidates its
     * layout.
     *
     * @param dimenResId the resource identifier of the dimension
     */
    fun setShadowRadiusResId(@DimenRes dimenResId: Int) {
        val newShadowRadius = resources.getDimensionPixelSize(dimenResId)
        if (shadowRadius != newShadowRadius.toFloat()) {
            shadowRadius = newShadowRadius.toFloat()
            requestLayout()
            updateBackground()
        }
    }

    /**
     * Sets the shadow radius of the [FloatingActionButton] and invalidates its
     * layout.
     *
     * Must be specified in density-independent (dp) pixels, which are then
     * converted into actual pixels (px).
     *
     * @param shadowRadiusDp shadow radius specified in density-independent
     *     (dp) pixels
     * @since 0.0.1
     */
    fun setShadowRadius(shadowRadiusDp: Float) {
        shadowRadius = dp2px(shadowRadiusDp)
        requestLayout()
        updateBackground()
    }

    /**
     * Sets the shadow x offset of the [FloatingActionButton] and invalidates
     * its layout.
     *
     * @param dimenResId the resource identifier of the dimension.
     * @since 0.0.1
     */
    fun setShadowXOffsetResId(@DimenRes dimenResId: Int) {
        val newShadowYOffset = resources.getDimensionPixelSize(dimenResId)
        if (shadowXOffset != newShadowYOffset.toFloat()) {
            shadowXOffset = newShadowYOffset.toFloat()
            requestLayout()
            updateBackground()
        }
    }

    /**
     * Sets the shadow x offset of the [FloatingActionButton] and invalidates
     * its layout.
     *
     * Must be specified in density-independent (dp) pixels, which are then
     * converted into actual pixels (px).
     *
     * @param shadowXOffsetDp shadow radius specified in density-independent
     *     (dp) pixels
     */
    fun setShadowXOffset(shadowXOffsetDp: Float) {
        shadowXOffset = dp2px(shadowXOffsetDp)
        requestLayout()
        updateBackground()
    }

    /**
     * Sets the shadow y offset of the [FloatingActionButton] and invalidates
     * its layout.
     *
     * @param dimenResId the resource identifier of the dimension.
     * @since 0.0.1
     */
    fun setShadowYOffsetResId(@DimenRes dimenResId: Int) {
        val newShadowYOffset = resources.getDimensionPixelSize(dimenResId)
        if (shadowYOffset != newShadowYOffset.toFloat()) {
            shadowYOffset = newShadowYOffset.toFloat()
            requestLayout()
            updateBackground()
        }
    }

    /**
     * Sets the shadow y offset of the **FloatingActionButton** and invalidates
     * its layout.
     *
     * Must be specified in density-independent (dp) pixels, which are then
     * converted into actual pixels (px).
     *
     * @param shadowYOffsetDp shadow radius specified in density-independent
     *     (dp) pixels
     */
    fun setShadowYOffset(shadowYOffsetDp: Float) {
        shadowYOffset = dp2px(shadowYOffsetDp)
        requestLayout()
        updateBackground()
    }

    fun setColorNormalResId(colorResId: Int) {
        colorNormal = resources.getColor(colorResId, context.theme)
    }

    fun setColorPressedResId(colorResId: Int) {
        colorPressed = resources.getColor(colorResId, context.theme)
    }

    fun setColorRippleResId(colorResId: Int) {
        colorRipple = resources.getColor(colorResId, context.theme)
    }

    fun setColorDisabledResId(colorResId: Int) {
        colorDisabled = resources.getColor(colorResId, context.theme)
    }

    fun hasShadow(): Boolean {
        return !mUsingElevation && showShadow
    }

    /**
     * Checks whether **FloatingActionButton** is hidden
     *
     * @return true if **FloatingActionButton** is hidden, false otherwise
     */
    val isMenuButtonHidden: Boolean
        get() = isHidden()

    /**
     * Makes the **FloatingActionButton** to appear and sets its visibility to
     * [.VISIBLE]
     *
     * @param animate if true - plays "show animation"
     */
    fun show(animate: Boolean) {
        if (isHidden()) {
            if (animate) {
                playShowAnimation()
            }
            super.setVisibility(VISIBLE)
        }
    }

    /**
     * Makes the **FloatingActionButton** to disappear and sets its visibility
     * to [.INVISIBLE]
     *
     * @param animate if true - plays "hide animation"
     */
    fun hide(animate: Boolean) {
        if (!isHidden()) {
            if (animate) {
                playHideAnimation()
            }
            super.setVisibility(INVISIBLE)
        }
    }

    fun toggle(animate: Boolean) {
        if (isHidden()) {
            show(animate)
        } else {
            hide(animate)
        }
    }

    override fun setElevation(elevation: Float) {
        if (elevation > 0) {
            super.setElevation(elevation)
            if (!isInEditMode) {
                mUsingElevation = true
                showShadow = false
            }
            updateBackground()
        }
    }

    /**
     * Sets the shadow color and radius to mimic the native elevation.
     *
     * **API 21+**: Sets the native elevation of this view, in pixels. Updates
     * margins to make the view hold its position in layout across different
     * platform versions.
     */
    fun setElevationCompat(elevation: Float) {
        shadowColor = 0x26000000
        shadowRadius = (elevation / 2)
        shadowXOffset = 0f
        shadowYOffset =
            if (fabSize == SIZE_NORMAL) elevation else (elevation / 2)
        super.setElevation(elevation)
        mUsingElevationCompat = true
        showShadow = false
        updateBackground()
        val layoutParams = layoutParams
        layoutParams?.let { setLayoutParams(it) }
    }

    /**
     * Change the indeterminate mode for the progress bar. In indeterminate
     * mode, the progress is ignored and the progress bar shows an infinite
     * animation instead.
     *
     * @param indeterminate true to enable the indeterminate mode
     */
    @Synchronized
    fun setIndeterminate(indeterminate: Boolean) {
        if (!indeterminate) {
            mCurrentProgress = 0.0f
        }
        mProgressBarEnabled = indeterminate
        mShouldUpdateButtonPosition = true
        mProgressIndeterminate = indeterminate
        mLastTimeAnimated = SystemClock.uptimeMillis()
        setupProgressBounds()
        //        saveButtonOriginalPosition();
        updateBackground()
    }

    @Synchronized
    fun setProgress(originProgress: Int, animate: Boolean) {
        var progress = originProgress
        if (mProgressIndeterminate) return
        mProgress = progress
        mAnimateProgress = animate
        if (!mButtonPositionSaved) {
            mShouldSetProgress = true
            return
        }
        mProgressBarEnabled = true
        mShouldUpdateButtonPosition = true
        setupProgressBounds()
        saveButtonOriginalPosition()
        updateBackground()
        if (progress < 0) {
            progress = 0
        } else if (progress > max) {
            progress = max
        }
        if (progress.toFloat() == mTargetProgress) {
            return
        }
        mTargetProgress = if (max > 0) progress / max.toFloat() * 360 else 0F
        mLastTimeAnimated = SystemClock.uptimeMillis()
        if (!animate) {
            mCurrentProgress = mTargetProgress
        }
        invalidate()
    }

    @get:Synchronized
    val progress: Int
        get() = if (mProgressIndeterminate) 0 else mProgress

    @Synchronized
    fun hideProgress() {
        mProgressBarEnabled = false
        mShouldUpdateButtonPosition = true
        updateBackground()
    }

    @Synchronized
    fun setShowProgressBackground(show: Boolean) {
        isProgressBackgroundShown = show
    }

    /**
     * Set the enabled state of the [FloatingActionButton].
     *
     * @param enabled True if this view is enabled, false otherwise.
     * @since 0.0.1
     */
    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        labelView?.isEnabled = enabled
    }

    /**
     * Set the visibility state of this view.
     *
     * @param visibility One of [View.VISIBLE], [View.INVISIBLE], or
     *     [View.GONE].
     * @since 0.0.1
     */
    override fun setVisibility(visibility: Int) {
        super.setVisibility(visibility)
        labelView?.visibility = visibility
    }

    /**
     * This will clear all AnimationListeners.
     *
     * @since 0.0.1
     */
    fun hideButtonInMenu(animate: Boolean) {
        if (!isHidden() && visibility != GONE) {
            hide(animate)
            labelView?.hide(animate)
            hideAnimation!!.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation) {}
                override fun onAnimationEnd(animation: Animation) {
                    visibility = GONE
                    hideAnimation!!.setAnimationListener(null)
                }

                override fun onAnimationRepeat(animation: Animation) {}
            })
        }
    }

    fun showButtonInMenu(animate: Boolean) {
        if (visibility == VISIBLE) return
        visibility = INVISIBLE
        show(animate)
        labelView?.show(animate)
    }

    /**
     * Set the label's background colors
     *
     * @since 0.0.1
     */
    fun setLabelColors(colorNormal: Int, colorPressed: Int, colorRipple: Int) {
        if (labelView == null)
            return
        val left = labelView!!.paddingLeft
        val top = labelView!!.paddingTop
        val right = labelView!!.paddingRight
        val bottom = labelView!!.paddingBottom
        labelView!!.apply {
            setColors(colorNormal, colorPressed, colorRipple)
            updateBackground()
            setPadding(left, top, right, bottom)
        }
    }

    /**
     * Set the label text color by [color].
     *
     * @param color text color int value.
     * @since 0.0.1
     */
    fun setLabelTextColor(color: Int) {
        if (labelView == null)
            return
        labelView!!.setTextColor(color)
    }

    /**
     * Use [colors] to set the color of the text in different states.
     *
     * @since 0.0.1
     */
    fun setLabelTextColor(colors: ColorStateList?) {
        if (labelView == null)
            return
        labelView!!.setTextColor(colors)
    }

    fun isHidden(): Boolean {
        return visibility == INVISIBLE
    }

    /**
     * Set [isMenuMain].
     *
     * @since 0.0.1
     */
    internal fun setMenuMain(isMain:Boolean){
        isMenuMain = isMain
    }

    init {
        val attr =
            context.obtainStyledAttributes(attrs, R.styleable.FloatingActionButton, defStyleAttr, 0)
        colorNormal = attr.getColor(R.styleable.FloatingActionButton_fab_colorNormal, -0x25bcca)
        colorPressed = attr.getColor(R.styleable.FloatingActionButton_fab_colorPressed, -0x18afbd)
        colorDisabled =
            attr.getColor(R.styleable.FloatingActionButton_fab_colorDisabled, -0x555556)
        colorRipple = attr.getColor(R.styleable.FloatingActionButton_fab_colorRipple, -0x66000001)
        showShadow = attr.getBoolean(R.styleable.FloatingActionButton_fab_showShadow, true)
        shadowColor = attr.getColor(R.styleable.FloatingActionButton_fab_shadowColor, 0x66000000)
        shadowRadius = attr.getDimensionPixelSize(
            R.styleable.FloatingActionButton_fab_shadowRadius,
            shadowRadius.roundToInt()
        ).toFloat()
        shadowXOffset = attr.getDimensionPixelSize(
            R.styleable.FloatingActionButton_fab_shadowXOffset,
            shadowXOffset.roundToInt()
        ).toFloat()
        shadowYOffset = attr.getDimensionPixelSize(
            R.styleable.FloatingActionButton_fab_shadowYOffset,
            shadowYOffset.roundToInt()
        ).toFloat()
        fabSize = attr.getInt(R.styleable.FloatingActionButton_fab_size, SIZE_NORMAL)
        mShouldProgressIndeterminate =
            attr.getBoolean(R.styleable.FloatingActionButton_fab_progress_indeterminate, false)
        mProgressColor =
            attr.getColor(R.styleable.FloatingActionButton_fab_progress_color, -0xff6978)
        mProgressBackgroundColor =
            attr.getColor(R.styleable.FloatingActionButton_fab_progress_backgroundColor, 0x4D000000)
        max = attr.getInt(R.styleable.FloatingActionButton_fab_progress_max, max)
        isProgressBackgroundShown =
            attr.getBoolean(R.styleable.FloatingActionButton_fab_progress_showBackground, true)
        if (attr.hasValue(R.styleable.FloatingActionButton_fab_progress)) {
            mProgress = attr.getInt(R.styleable.FloatingActionButton_fab_progress, 0)
            mShouldSetProgress = true
        }
        if (attr.hasValue(R.styleable.FloatingActionButton_fab_elevationCompat)) {
            val elevation = attr.getDimensionPixelOffset(
                R.styleable.FloatingActionButton_fab_elevationCompat,
                0
            ).toFloat()
            if (isInEditMode) {
                setElevation(elevation)
            } else {
                setElevationCompat(elevation)
            }
        }
        initShowAnimation(attr)
        initHideAnimation(attr)
        labelText = attr.getString(R.styleable.FloatingActionButton_fab_label_text).toString()
        attr.recycle()
        if (isInEditMode) {
            if (mShouldProgressIndeterminate) {
                setIndeterminate(true)
            } else if (mShouldSetProgress) {
                saveButtonOriginalPosition()
                setProgress(mProgress, false)
            }
        }
        isClickable = true
    }

    companion object {
        const val SIZE_NORMAL = 0
        const val SIZE_MINI = 1
        private val PORTER_DUFF_CLEAR: Xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        private const val PAUSE_GROWING_TIME: Long = 200
        private const val BAR_SPIN_CYCLE_TIME = 500.0
        private const val BAR_MAX_LENGTH = 270
    }

    @Retention(AnnotationRetention.SOURCE)
    @IntDef(SIZE_MINI, SIZE_NORMAL)
    annotation class FabSize
}