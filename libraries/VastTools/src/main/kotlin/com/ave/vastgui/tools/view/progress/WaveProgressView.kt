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
package com.ave.vastgui.tools.view.progress

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.VectorDrawable
import android.text.TextUtils
import android.util.AttributeSet
import androidx.annotation.ColorInt
import androidx.annotation.FloatRange
import androidx.annotation.IntRange
import androidx.annotation.RestrictTo
import androidx.appcompat.content.res.AppCompatResources
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import com.ave.vastgui.core.extension.NotNUllVar
import com.ave.vastgui.tools.R
import com.ave.vastgui.tools.utils.DensityUtils.DP
import com.ave.vastgui.tools.graphics.BmpUtils.getBitmapFromDrawable
import com.ave.vastgui.tools.view.progress.WaveProgressView.Companion.DEFAULT_SPACE_RATIO
import com.ave.vastgui.tools.view.progress.WaveProgressView.Companion.DEFAULT_STROKE_RATIO

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/10/31
// Documentation: https://ave.entropy2020.cn/documents/VastTools/core-topics/ui/progress/WaveProgressView/

/**
 * WaveProgressView
 *
 * @property DEFAULT_STROKE_RATIO The ratio of the stroke width to the
 *     WaveProgressView width by default.
 * @property DEFAULT_SPACE_RATIO The ratio of the distance between the
 *     stroke and wave progress to the WaveProgressView width by default.
 * @property mIsAutoBack Used to determine whether a default background is
 *     required (the default is a circular background).
 * @property mBackground Background for saving graph calculations with
 *     waves.
 * @property mUpdateInterval Interval time between every frame in
 *     milliseconds.
 * @property mProgressColor The wave color.
 * @property mProgressBackgroundColor The wave background color.
 * @property mWaveCount The count number of waves.
 * @property mWaveWidth The width of the each wave.
 * @property mHalfWaveWidth A quarter of the [mWaveWidth].
 * @property mWaveHeight The height of each wave.
 * @property mWaveOffsetDistance The wave offset distance.
 * @property mWaveSpeed Offset per frame. By default, the value is
 *     mWaveWidth / 70.
 * @property mSpaceWidth The space width.
 * @property mStrokeWidth The stroke width.
 * @property mStrokeColor The stroke color.
 * @property mHintColor The hint color.
 * @property mHint The hint text.
 * @property mHintSize The hint text size.
 * @property mTextSize The spacing between [mHint] and [mText].
 * @property mStrokeRatio Reference to DEFAULT_STROKE_RATIO. The sum of
 *     mStrokeRatio and mSpaceRatio must be less than 1.0.
 * @property mSpaceRatio Reference to DEFAULT_SPACE_RATIO. The sum of
 *     mStrokeRatio and mSpaceRatio must be less than 1.0.
 * @since 0.2.0
 */
class WaveProgressView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.Default_WaveProgressView_Style,
    defStyleRes: Int = R.style.BaseWaveProgressView
) : ProgressView(context, attrs, defStyleAttr, defStyleRes) {

    companion object {
        private const val DEFAULT_STROKE_RATIO = 1f / 100
        private const val DEFAULT_SPACE_RATIO = 1f / 20
    }

    private var mWavePaint: Paint by NotNUllVar()
    private var mStrokePaint: Paint by NotNUllVar()
    private var mTextPaint: Paint by NotNUllVar()
    private var mPath: Path by NotNUllVar()
    private var mTextRect: Rect by NotNUllVar()

    private val mIsAutoBack: Boolean
        get() = !(background is BitmapDrawable || background is VectorDrawable || background is VectorDrawableCompat)
    private val mBackground: Bitmap
        get() = if (mIsAutoBack) {
            autoCreateBitmap(mWidth / 2)
        } else {
            getBitmapFromDrawable(background)
        }

    private var mWidth = 0
    private var mHeight = 0

    private var mWaveCount = 0
    private var mWaveWidth = 0f
    private var mWaveHeight = 0f
    private var mHalfWaveWidth = mWaveWidth / 4
    private var mWaveOffsetDistance = 0f
    private var mWaveSpeed = 0f

    private var mUpdateInterval: Long = 20

    private var mSpaceWidth = 0f
    private var mStrokeWidth = 0f
    private var mStrokeColor = 0

    private var mHint: String? = null
    private var mHintColor = 0
    private var mHintSize = 0f

    private var mTextSpace = 10f

    private var mStrokeRatio = DEFAULT_STROKE_RATIO
    private var mSpaceRatio = DEFAULT_SPACE_RATIO

    init {
        val typedArray: TypedArray = context.obtainStyledAttributes(
            attrs, R.styleable.WaveProgressView, defStyleAttr, defStyleRes
        )
        mMaximumProgress =
            typedArray.getFloat(R.styleable.WaveProgressView_progress_maximum_value, 0f)
        mCurrentProgress =
            typedArray.getFloat(R.styleable.WaveProgressView_progress_current_value, 0f)
        mText = typedArray.getString(R.styleable.WaveProgressView_progress_text)
        mTextSize = typedArray.getDimension(R.styleable.WaveProgressView_progress_text_size, 0f)
        mTextColor = typedArray.getColor(
            R.styleable.WaveProgressView_progress_text_color,
            context.getColor(R.color.md_theme_onPrimary)
        )
        mProgressBackgroundColor = typedArray.getColor(
            R.styleable.WaveProgressView_progress_background_color,
            context.getColor(R.color.md_theme_primaryContainer)
        )
        mProgressColor = typedArray.getColor(
            R.styleable.WaveProgressView_progress_color, context.getColor(R.color.md_theme_primary)
        )
        mWaveWidth =
            typedArray.getFloat(R.styleable.WaveProgressView_wave_progress_wave_width, 200f)
        mHalfWaveWidth = mWaveWidth / 4
        mWaveHeight =
            typedArray.getFloat(R.styleable.WaveProgressView_wave_progress_wave_height, 20f)
        mWaveSpeed = typedArray.getFloat(
            R.styleable.WaveProgressView_wave_progress_wave_speed, mWaveWidth / 70
        )
        mStrokeColor = typedArray.getColor(
            R.styleable.WaveProgressView_wave_progress_stroke_color,
            context.getColor(R.color.md_theme_primary)
        )
        mHint = typedArray.getString(R.styleable.WaveProgressView_wave_progress_hint_text)
        mHintColor = typedArray.getColor(
            R.styleable.WaveProgressView_wave_progress_hint_color,
            context.getColor(R.color.md_theme_onPrimary)
        )
        mHintSize = typedArray.getDimension(
            R.styleable.WaveProgressView_wave_progress_hint_size, 0f
        )
        mTextSpace =
            typedArray.getDimension(R.styleable.WaveProgressView_wave_progress_text_space, 10f)
        mStrokeRatio = typedArray.getFloat(
            R.styleable.WaveProgressView_wave_progress_stroke_ratio, DEFAULT_STROKE_RATIO
        )
        mSpaceRatio = typedArray.getFloat(
            R.styleable.WaveProgressView_wave_progress_space_ratio, DEFAULT_SPACE_RATIO
        )
        typedArray.recycle()
        mPath = Path()
        mWavePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.FILL
            color = mProgressColor
        }
        mStrokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
        }
        mTextPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mTextRect = Rect()
        setBackgroundColor(Color.TRANSPARENT)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val measuredWidth = measureWidth(widthMeasureSpec)
        val measuredHeight = measureHeight(heightMeasureSpec)
        setMeasuredDimension(measuredWidth, measuredHeight)
        if (mIsAutoBack) {
            val min = measuredWidth.coerceAtMost(measuredHeight)
            mStrokeWidth = mStrokeRatio * min
            // Refer to https://github.com/SakurajimaMaii/Android-Vast-Extension/issues/51
            mSpaceWidth = (mSpaceRatio * min).let {
                if (it == 0.0f) (0.01 * min).toFloat() else it
            }
            mStrokePaint.apply {
                strokeWidth = mStrokeWidth
                color = if (strokeWidth.toDouble() == 0.0) context.getColor(R.color.transparent)
                else mStrokeColor
            }
            val tempWith = (min - (mStrokeWidth + mSpaceWidth) * 2).toInt()
            mWidth = if (tempWith < 2) {
                (min - (DEFAULT_STROKE_RATIO * min + DEFAULT_SPACE_RATIO * min) * 2).toInt()
            } else tempWith
            mHeight = mWidth
        } else {
            mWidth = mBackground.width
            mHeight = mBackground.height
        }
        mWaveCount = calWaveCount(mWidth, mWaveWidth)
    }


    override fun onDraw(canvas: Canvas) {
        val bitmap = createWaveBitmap(mWidth, mHeight)
        if (mIsAutoBack) {
            val radius = (measuredWidth / 2).coerceAtMost(measuredHeight / 2).toFloat()
            canvas.drawCircle(
                (measuredWidth / 2).toFloat(),
                (measuredHeight / 2).toFloat(),
                radius - mStrokeWidth / 2,
                mStrokePaint
            )
            val left = (measuredWidth / 2 - mWidth / 2).toFloat()
            val top = (measuredHeight / 2 - mHeight / 2).toFloat()
            canvas.drawBitmap(bitmap, left, top, null)
        } else {
            canvas.drawBitmap(bitmap, 0f, 0f, null)
        }
        if (!TextUtils.isEmpty(mText)) {
            mTextPaint.apply {
                color = mTextColor
                textSize = mTextSize
                getTextBounds(mText, 0, mText!!.length - 1, mTextRect)
            }
            val textLength = mTextPaint.measureText(mText)
            val metrics = mTextPaint.fontMetrics
            val baseLine =
                mTextRect.height() / 2 + (metrics.descent - metrics.ascent) / 2 - metrics.descent
            canvas.drawText(
                mText!!,
                measuredWidth / 2 - textLength / 2,
                measuredHeight / 2 + baseLine,
                mTextPaint
            )
        }
        if (!TextUtils.isEmpty(mHint)) {
            mTextPaint.apply {
                color = mHintColor
                textSize = mHintSize
            }
            val hintLength = mTextPaint.measureText(mHint)
            canvas.drawText(
                mHint!!,
                measuredWidth / 2 - hintLength / 2,
                measuredHeight / 2 - mTextRect.height() - mTextSpace,
                mTextPaint
            )
        }
        if (mUpdateInterval != -1L) {
            postInvalidateDelayed(mUpdateInterval)
        }
    }

    /**
     * Create a circular Bimap with radius [radius] and color
     * [mProgressBackgroundColor].
     *
     * @since 0.2.0
     */
    private fun autoCreateBitmap(radius: Int): Bitmap {
        val bitmap = Bitmap.createBitmap(2 * radius, 2 * radius, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val p = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = mProgressBackgroundColor
            style = Paint.Style.FILL
        }
        canvas.drawCircle(radius.toFloat(), radius.toFloat(), radius.toFloat(), p)
        return bitmap
    }

    /**
     * Measure the view width, if it is wrap content, the default value is
     * 200dp.
     */
    private fun measureHeight(heightMeasureSpec: Int): Int {
        val mode = MeasureSpec.getMode(heightMeasureSpec)
        val size = MeasureSpec.getSize(heightMeasureSpec)
        return when {
            mode == MeasureSpec.EXACTLY -> size
            (mode == MeasureSpec.AT_MOST && !mIsAutoBack) -> mBackground.height
            else -> 200F.DP.toInt()
        }
    }

    /**
     * Measure the view width, if it is wrap content, the default value is
     * 200dp.
     */
    private fun measureWidth(widthMeasureSpec: Int): Int {
        val mode = MeasureSpec.getMode(widthMeasureSpec)
        val size = MeasureSpec.getSize(widthMeasureSpec)
        return when {
            mode == MeasureSpec.EXACTLY -> size
            (mode == MeasureSpec.AT_MOST && !mIsAutoBack) -> mBackground.width
            else -> 200F.DP.toInt()
        }
    }

    /**
     * Draw a wave bitmap.
     *
     * @param width The width of the bitmap.
     * @param height The height of the bitmap.
     * @since 0.2.0
     */
    private fun createWaveBitmap(width: Int, height: Int): Bitmap {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val mCurY = (height * (mMaximumProgress - mCurrentProgress) / mMaximumProgress).toInt()
        mPath.reset()
        mPath.moveTo(-mWaveOffsetDistance, mCurY.toFloat())
        for (i in 0 until mWaveCount) {
            mPath.quadTo(
                i * mWaveWidth + mHalfWaveWidth - mWaveOffsetDistance,
                mCurY - mWaveHeight,
                i * mWaveWidth + mHalfWaveWidth * 2 - mWaveOffsetDistance,
                mCurY.toFloat()
            )
            mPath.quadTo(
                i * mWaveWidth + mHalfWaveWidth * 3 - mWaveOffsetDistance,
                mCurY + mWaveHeight,
                i * mWaveWidth + mHalfWaveWidth * 4 - mWaveOffsetDistance,
                mCurY.toFloat()
            )
        }
        mPath.lineTo(width.toFloat(), height.toFloat())
        mPath.lineTo(0f, height.toFloat())
        mPath.close()
        canvas.drawPath(mPath, mWavePaint)
        mWaveOffsetDistance += mWaveSpeed
        mWaveOffsetDistance %= mWaveWidth
        mWavePaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_ATOP)
        canvas.drawBitmap(mBackground, 0f, 0f, mWavePaint)
        return bitmap
    }

    /**
     * Calculate the number of waves.
     *
     * @param width The width of the [WaveProgressView].
     * @param waveWidth The width of the wave.
     * @since 0.2.0
     */
    private fun calWaveCount(width: Int, waveWidth: Float): Int {
        val count: Int = if (width % waveWidth == 0f) {
            (width / waveWidth + 1).toInt()
        } else {
            (width / waveWidth + 2).toInt()
        }
        return count
    }

    /**
     * Set the [mWaveSpeed] of the wave.
     *
     * @since 0.2.0
     */
    fun setSpeed(@FloatRange(from = 0.0) speed: Float) {
        mWaveSpeed = speed
    }

    /**
     * Set update interval time between every frame in milliseconds.
     *
     * @param interval If the value of [interval] is -1, the wave will stop
     *     move.
     * @since 0.5.2
     */
    fun setUpdateInterval(@IntRange(from = -1) interval: Long) {
        mUpdateInterval = interval
    }

    /**
     * Set the width and height of the wave.
     *
     * @since 0.2.0
     */
    fun setWave(
        @FloatRange(from = 0.0) waveWidth: Float,
        @FloatRange(from = 0.0) waveHeight: Float
    ) {
        mWaveWidth = waveWidth
        mWaveHeight = waveHeight
        mHalfWaveWidth = waveWidth / 4
        mWaveCount = calWaveCount(mWidth, mWaveWidth)
    }

    /**
     * Set wave color.
     *
     * @since 0.2.0
     */
    fun setWaveColor(@ColorInt color: Int) {
        mProgressColor = color
        mWavePaint.color = mProgressColor
    }

    /**
     * Set wave background color.
     *
     * @since 0.2.0
     */
    fun setWaveBackgroundColor(@ColorInt color: Int) {
        mProgressBackgroundColor = color
        super.setBackground(null)
    }

    /**
     * Set the stroke color. It must be effective without background.
     *
     * @since 0.2.0
     */
    fun setStrokeColor(strokeColor: Int) {
        mStrokeColor = strokeColor
        mStrokePaint.color = mStrokeColor
    }

    /**
     * Set hint text.
     *
     * @since 0.2.0
     */
    fun setHint(hint: String) {
        mHint = hint
    }

    /**
     * Set hint text size.
     *
     * @since 0.2.0
     */
    fun setHintSize(hintSize: Float) {
        mHintSize = hintSize
    }

    /**
     * Set hint text color.
     *
     * @since 0.2.0
     */
    fun setHintColor(@ColorInt color: Int) {
        mHintColor = color
    }

    /**
     * Set hints and text spacing.
     *
     * @since 0.2.0
     */
    fun setTextSpace(textSpace: Float) {
        mTextSpace = textSpace
    }

    /**
     * Set the background. Current support type is [BitmapDrawable] ,
     * [VectorDrawable] and [VectorDrawableCompat].
     *
     * @since 0.2.0
     */
    override fun setBackground(background: Drawable) {
        if (background is BitmapDrawable || background is VectorDrawable || background is VectorDrawableCompat) {
            super.setBackground(background)
        }
    }

    /**
     * Set the background. Current support type is [BitmapDrawable] ,
     * [VectorDrawable] and [VectorDrawableCompat].
     *
     * @since 0.2.0
     */
    override fun setBackgroundResource(resid: Int) {
        try {
            background = AppCompatResources.getDrawable(context, resid)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Set [mStrokeRatio].
     *
     * @see DEFAULT_STROKE_RATIO
     * @since 0.2.0
     */
    @RestrictTo(RestrictTo.Scope.TESTS)
    fun setStrokeRatio(@FloatRange(from = 0.0) ratio: Float) {
        mStrokeRatio = if (ratio + mSpaceRatio > 1.0) {
            throw IllegalArgumentException("The sum of mStrokeRatio and mSpaceRatio must be less than 1.0")
        } else ratio
        requestLayout()
        invalidate()
    }

    /**
     * Set [mSpaceRatio].
     *
     * @see DEFAULT_SPACE_RATIO
     * @since 0.2.0
     */
    @RestrictTo(RestrictTo.Scope.TESTS)
    fun setSpaceRatio(@FloatRange(from = 0.0) ratio: Float) {
        mSpaceRatio = if (ratio + mStrokeRatio > 1.0) {
            throw IllegalArgumentException("The sum of mStrokeRatio and mSpaceRatio must be less than 1.0")
        } else ratio
        requestLayout()
        invalidate()
    }

}