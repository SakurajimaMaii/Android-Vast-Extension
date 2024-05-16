/*
 * Copyright 2024 VastGui guihy2019@gmail.com
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
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.annotation.DrawableRes
import androidx.annotation.FloatRange
import androidx.annotation.IntRange
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.res.getResourceIdOrThrow
import com.ave.vastgui.core.extension.NotNUllVar
import com.ave.vastgui.tools.R
import com.ave.vastgui.tools.graphics.BmpUtils
import com.ave.vastgui.tools.graphics.BmpUtils.getBitmapFromDrawable
import com.ave.vastgui.tools.graphics.getBaseLine

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/10/31
// Documentation: https://ave.entropy2020.cn/documents/VastTools/core-topics/ui/progress/wave-progress-view/

/**
 * WaveProgressView
 *
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
 * @since 0.2.0
 */
class WaveProgressView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.Default_WaveProgressView_Style,
    defStyleRes: Int = R.style.BaseWaveProgressView
) : ProgressView(context, attrs, defStyleAttr, defStyleRes) {

    private val mBackgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }
    private val mWaveBitmapPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var mWavePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }
    private var mStrokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
    }
    private var mTextPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textAlign = Paint.Align.CENTER
    }
    private val mWavePath = Path()
    private val mWaveRectF = RectF()
    private val mXfermode = PorterDuffXfermode(PorterDuff.Mode.DST_ATOP)

    private val mIsAutoBack: Boolean
        get() = mImage == null
    private val mBackground: Bitmap
        get() = if (mIsAutoBack) {
            autoCreateBitmap(mWidth / 2)
        } else {
            getBitmapFromDrawable(mImage!!)
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

    override var mProgressBackgroundColor: Int
        get() = mBackgroundPaint.color
        set(value) {
            mBackgroundPaint.color = value
        }

    override var mProgressColor: Int
        get() = mWavePaint.color
        set(value) {
            mWavePaint.color = value
        }

    override var mTextColor: Int
        get() = mTextPaint.color
        set(value) {
            mTextPaint.color = value
        }

    override var mTextSize: Float
        get() = mTextPaint.textSize
        set(value) {
            mTextPaint.textSize = value
        }

    var mRadius: Float = 0f
        set(value) {
            field = value.coerceAtLeast(0f)
            requestLayout()
        }

    var mStrokeWidth: Float = 0f
        set(value) {
            field = value.coerceAtLeast(0f)
            mStrokePaint.strokeWidth = field
            requestLayout()
        }

    var mSpaceWidth: Float = 0f
        set(value) {
            field = value.coerceAtLeast(0f)
            requestLayout()
        }

    var mStrokeColor: Int
        get() = mStrokePaint.color
        set(value) {
            mStrokePaint.color = value
        }

    var mShowText: Boolean by NotNUllVar()

    var mImage: Drawable? = null
        private set

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (mIsAutoBack) {
            val neededSize: Int = (2 * (mSpaceWidth + mRadius + mStrokeWidth)).toInt()
            mWidth = (2 * mRadius).toInt()
            mHeight = (2 * mRadius).toInt()
            setMeasuredDimension(
                resolveSize(neededSize, widthMeasureSpec),
                resolveSize(neededSize, heightMeasureSpec)
            )
        } else {
            val neededWidth: Int = mBackground.width
            val neededHeight: Int = mBackground.height
            mWidth = resolveSize(neededWidth, widthMeasureSpec)
            mHeight = resolveSize(neededHeight, heightMeasureSpec)
            setMeasuredDimension(mWidth, mHeight)
        }
        mWaveCount = calWaveCount(mWidth, mWaveWidth)
    }

    override fun onDraw(canvas: Canvas) {
        val bitmap: Bitmap = createWaveBitmap(mWidth, mHeight)
        val left: Float = (measuredWidth - mWidth) / 2f
        val top: Float = (measuredHeight - mHeight) / 2f
        if (mIsAutoBack) {
            if (mStrokePaint.strokeWidth != 0f) {
                canvas.drawCircle(
                    measuredWidth / 2f,
                    measuredHeight / 2f,
                    mRadius + mSpaceWidth + mStrokeWidth / 2f,
                    mStrokePaint
                )
            }
            canvas.drawBitmap(bitmap, left, top, mWaveBitmapPaint)
        } else {
            canvas.drawBitmap(bitmap, left, top, mWaveBitmapPaint)
        }
        if (mShowText && mImage == null) {
            canvas.drawText(
                textOrDefault(),
                measuredWidth / 2f,
                measuredHeight / 2f + mTextPaint.getBaseLine(),
                mTextPaint
            )
        }
        if (mUpdateInterval != -1L) {
            postInvalidateDelayed(mUpdateInterval)
        }
    }

    /**
     * Set [mImage]. Click
     * [link](https://ave.entropy2020.cn/documents/VastTools/core-topics/ui/progress/wave-progress-view/#_2)
     * to see the example.
     *
     * @since 0.5.5
     */
    fun setImage(image: Drawable?) {
        mImage = image
    }

    /**
     * Set [mImage]. Click
     * [link](https://ave.entropy2020.cn/documents/VastTools/core-topics/ui/progress/wave-progress-view/#_2)
     * to see the example. If the resource is not exists, the [mImage] will be
     * set to null.
     *
     * @since 0.5.5
     */
    fun setImage(@DrawableRes resid: Int) {
        mImage = try {
            AppCompatResources.getDrawable(context, resid)
        } catch (e: Exception) {
            null
        }
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
     * Create a circular Bimap with radius [radius] and color
     * [mProgressBackgroundColor].
     *
     * @since 0.2.0
     */
    private fun autoCreateBitmap(radius: Int): Bitmap {
        val bitmap = Bitmap.createBitmap(2 * radius, 2 * radius, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawCircle(radius.toFloat(), radius.toFloat(), radius.toFloat(), mBackgroundPaint)
        return bitmap
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
        val mCurY =
            if (mMaximumProgress == mCurrentProgress) (-0.05f * height).toInt()
            else if (0f == mCurrentProgress) (1.05f * height).toInt()
            else (height * (mMaximumProgress - mCurrentProgress) / mMaximumProgress).toInt()
        mWavePath.reset()
        mWavePath.moveTo(-mWaveOffsetDistance, mCurY.toFloat())
        for (i in 0 until mWaveCount) {
            mWavePath.quadTo(
                i * mWaveWidth + mHalfWaveWidth - mWaveOffsetDistance,
                mCurY - mWaveHeight,
                i * mWaveWidth + mHalfWaveWidth * 2 - mWaveOffsetDistance,
                mCurY.toFloat()
            )
            mWavePath.quadTo(
                i * mWaveWidth + mHalfWaveWidth * 3 - mWaveOffsetDistance,
                mCurY + mWaveHeight,
                i * mWaveWidth + mHalfWaveWidth * 4 - mWaveOffsetDistance,
                mCurY.toFloat()
            )
        }
        mWavePath.lineTo(width.toFloat(), height.toFloat())
        mWavePath.lineTo(0f, height.toFloat())
        mWavePath.close()
        mWaveRectF.set(0f, 0f, width.toFloat(), height.toFloat())
        val cs = canvas.saveLayer(mWaveRectF, null)
        canvas.drawPath(mWavePath, mWavePaint)
        mWaveOffsetDistance += mWaveSpeed
        mWaveOffsetDistance %= mWaveWidth
        mWavePaint.xfermode = mXfermode
        canvas.drawBitmap(
            BmpUtils.scaleBitmap(mBackground, width, height),
            0f, 0f,
            mWavePaint
        )
        mWavePaint.xfermode = null
        canvas.restoreToCount(cs)
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

    init {
        val typedArray: TypedArray = context.obtainStyledAttributes(
            attrs, R.styleable.WaveProgressView, defStyleAttr, defStyleRes
        )
        mMaximumProgress =
            typedArray.getFloat(
                R.styleable.WaveProgressView_progress_maximum_value,
                mDefaultMaximumProgress
            )
        mCurrentProgress =
            typedArray.getFloat(
                R.styleable.WaveProgressView_progress_current_value,
                mDefaultCurrentProgress
            )
        mImage =
            try {
                AppCompatResources.getDrawable(
                    context,
                    typedArray.getResourceIdOrThrow(R.styleable.WaveProgressView_wave_progress_image)
                )
            } catch (exception: Exception) {
                null
            }
        mShowText =
            typedArray.getBoolean(
                R.styleable.WaveProgressView_wave_progress_show_text, true
            )
        mText = typedArray.getString(R.styleable.WaveProgressView_progress_text) ?: ""
        mTextSize =
            typedArray.getDimension(R.styleable.WaveProgressView_progress_text_size, mDefaultTexSize)
        mTextColor =
            typedArray.getColor(
                R.styleable.WaveProgressView_progress_text_color,
                context.getColor(R.color.md_theme_onPrimary)
            )
        mProgressBackgroundColor =
            typedArray.getColor(
                R.styleable.WaveProgressView_progress_background_color,
                context.getColor(R.color.md_theme_primaryContainer)
            )
        mProgressColor =
            typedArray.getColor(
                R.styleable.WaveProgressView_progress_color, context.getColor(R.color.md_theme_primary)
            )
        mWaveWidth =
            typedArray.getFloat(R.styleable.WaveProgressView_wave_progress_wave_width, 200f)
        mHalfWaveWidth = mWaveWidth / 4
        mWaveHeight =
            typedArray.getFloat(R.styleable.WaveProgressView_wave_progress_wave_height, 20f)
        mWaveSpeed =
            typedArray.getFloat(
                R.styleable.WaveProgressView_wave_progress_wave_speed, mWaveWidth / 70
            )
        mStrokeColor =
            typedArray.getColor(
                R.styleable.WaveProgressView_wave_progress_stroke_color,
                context.getColor(R.color.md_theme_primary)
            )
        mRadius =
            typedArray.getDimension(
                R.styleable.WaveProgressView_wave_progress_radius, 0f
            )
        mStrokeWidth =
            typedArray.getDimension(
                R.styleable.WaveProgressView_wave_progress_stroke_width, 0f
            )
        mSpaceWidth =
            typedArray.getDimension(
                R.styleable.WaveProgressView_wave_progress_space_width, 0f
            )
        typedArray.recycle()
        setLayerType(LAYER_TYPE_SOFTWARE, null)
    }

}