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
package com.ave.vastgui.tools.view.progress

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.FloatRange
import androidx.annotation.IntRange
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.res.getResourceIdOrThrow
import androidx.core.content.withStyledAttributes
import androidx.core.graphics.createBitmap
import com.ave.vastgui.tools.R
import com.ave.vastgui.tools.graphics.BmpUtils.getBitmapFromDrawable
import com.ave.vastgui.tools.graphics.getBaseLine
import com.ave.vastgui.tools.utils.ColorUtils
import com.ave.vastgui.tools.utils.DensityUtils.DP
import com.ave.vastgui.tools.utils.color
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract
import kotlin.math.max
import kotlin.math.roundToInt
import kotlin.properties.Delegates

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/10/31
// Documentation: https://sakurajimamaii.github.io/AVE-DOC/documents/tools/core-topics/ui/progress/wave-progress-view/

/**
 * WaveProgressView
 *
 * @since 0.2.0
 */
class WaveProgressView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.Default_WaveProgressView_Style,
    defStyleRes: Int = R.style.BaseWaveProgressView
) : ProgressView(context, attrs, defStyleAttr, defStyleRes) {

    /** @since 1.5.2 */
    private val backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }

    /** @since 1.5.2 */
    private val waveBitmapPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    /** @since 1.5.2 */
    private val wavePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }

    /** @since 1.5.2 */
    private val strokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
    }

    /** @since 1.5.2 */
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textAlign = Paint.Align.CENTER
    }

    /** @since 1.5.2 */
    private val wavePath = Path()

    /** @since 1.5.2 */
    private val waveRectF = RectF()

    /** @since 1.5.2 */
    private val xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_ATOP)

    /**
     * The count number of waves.
     *
     * @since 1.5.2
     */
    private var waveCount = 0

    /**
     * The width of the each wave(in pixels).
     *
     * @since 1.5.2
     */
    var waveWidth = 0f
        private set

    /**
     * The height of each wave(in pixels).
     *
     * @since 1.5.2
     */
    var waveHeight = 0f
        private set

    /**
     * A quarter of the [waveWidth].
     *
     * @since 1.5.2
     */
    private var halfWaveWidth = waveWidth / 4

    /**
     * The wave offset distance.
     *
     * @since 1.5.2
     */
    private var waveOffsetDistance = 0f

    /**
     * Offset per frame (in pixels). The default value is [waveWidth] / 70.
     *
     * @since 1.5.2
     */
    private var waveSpeed = 0f

    /**
     * Interval time between every frame(in milliseconds).
     *
     * @since 1.5.2
     */
    private var updateInterval: Long = 20

    override var progressBackgroundColor: Int
        get() = backgroundPaint.color
        set(value) {
            if (backgroundPaint.color == value) return
            check(ColorUtils.isColorInt(value)) {
                "The color-int(current=${value.toUInt().toString(16)}) of progress background is invalid."
            }
            backgroundPaint.color = value
            invalidate()
        }

    override var progressColor: Int
        get() = wavePaint.color
        set(value) {
            if (wavePaint.color == value) return
            check(ColorUtils.isColorInt(value)) {
                "The color-int(current=${value.toUInt().toString(16)}) of progress is invalid."
            }
            wavePaint.color = value
            invalidate()
        }

    /** @since 1.5.2 */
    private var _radius: Float = 0f

    /**
     * The radius of progress(in pixels).
     *
     * @since 1.5.2
     */
    var radius: Float
        get() = _radius
        set(value) {
            _radius = value.coerceAtLeast(0f)
            _image = createDefaultImageBitmap(value.coerceAtLeast(0f).roundToInt())
            requestLayout()
        }

    /** @since 1.5.2 */
    private var _spaceWidth: Float = 0f

    /**
     * The space width between progress and stroke(in pixels.)
     *
     * @since 1.5.2
     */
    var spaceWidth: Float
        get() = _spaceWidth
        set(value) {
            _spaceWidth = value.coerceAtLeast(0f)
            requestLayout()
        }

    /**
     * The width of stroke(in pixels).
     *
     * @since 1.5.2
     */
    var strokeWidth: Float
        get() = strokePaint.strokeWidth
        set(value) {
            strokePaint.strokeWidth = value.coerceAtLeast(0f)
            requestLayout()
        }

    /**
     * The color-int of stroke.
     *
     * @since 1.5.2
     */
    @get:ColorInt
    @setparam:ColorInt
    var strokeColor: Int
        get() = strokePaint.color
        set(value) {
            if (strokePaint.color == value) return
            check(ColorUtils.isColorInt(value)) {
                "The color-int(current=${value.toUInt().toString(16)}) of stroke is invalid."
            }
            strokePaint.color = value
            invalidate()
        }

    /** @since 1.5.2 */
    private var _showText: Boolean by Delegates.notNull()

    /**
     * `true` if you want to show the text, `false` otherwise.
     *
     * @since 1.5.2
     */
    var showText: Boolean
        get() = _showText
        set(value) {
            if (_showText == value) return
            _showText = value
            invalidate()
        }

    /** @since 1.5.2 */
    private var _text: String = ""

    override var text: String
        get() = _text
        set(value) {
            _text = value
            invalidate()
        }

    override var textColor: Int
        get() = textPaint.color
        set(value) {
            if (textPaint.color == value) return
            check(ColorUtils.isColorInt(value)) {
                "The color-int(current=${value.toUInt().toString(16)}) of text is invalid."
            }
            textPaint.color = value
            invalidate()
        }

    override var textSize: Float
        get() = textPaint.textSize
        set(value) {
            if (textPaint.textSize == value) return
            textPaint.textSize = value.coerceAtLeast(0f)
            invalidate()
        }

    /**
     * Background for saving graph calculations with waves.
     *
     * @since 1.5.2
     */
    private var _image: Bitmap by Delegates.notNull()

    /** @since 1.5.2 */
    var image: Drawable? = null
        private set

    /** @since 1.5.2 */
    private var _maximumProgress = DEFAULT_MAXIMUM_PROGRESS

    override var maximumProgress: Float
        get() = _maximumProgress
        set(value) {
            _maximumProgress = value.coerceAtLeast(0f)
            resetProgress()
            invalidate()
        }

    /** @since 1.5.2 */
    private var _currentProgress = DEFAULT_CURRENT_PROGRESS

    override var currentProgress: Float
        get() = _currentProgress
        set(value) {
            _currentProgress = value.coerceIn(0f, _maximumProgress)
            invalidate()
        }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (isAutoBackground(image)) {
            val viewContentSize = (2 * (_spaceWidth + _radius + strokePaint.strokeWidth)).roundToInt()
            val viewMinimumWidth = viewContentSize + paddingStart + paddingEnd
            val viewMinimumHeight = viewContentSize + paddingTop + paddingBottom
            val neededMinimumWidth = max(viewMinimumWidth, suggestedMinimumWidth)
            val neededMinimumHeight = max(viewMinimumHeight, suggestedMinimumHeight)
            setMeasuredDimension(resolveSize(neededMinimumWidth, widthMeasureSpec), resolveSize(neededMinimumHeight, heightMeasureSpec))
        } else {
            val viewMinimumWidth = _image.width + paddingStart + paddingEnd
            val viewMinimumHeight = _image.height + paddingTop + paddingBottom
            val neededMinimumWidth = max(viewMinimumWidth, suggestedMinimumWidth)
            val neededMinimumHeight = max(viewMinimumHeight, suggestedMinimumHeight)
            setMeasuredDimension(resolveSize(neededMinimumWidth, widthMeasureSpec), resolveSize(neededMinimumHeight, heightMeasureSpec))
        }
    }

    override fun onDraw(canvas: Canvas) {
        val bitmap: Bitmap = createWaveBitmap(_image.width, _image.height)
        val centerX = (paddingStart + measuredWidth - paddingEnd) / 2f
        val centerY = (paddingTop + measuredHeight - paddingBottom) / 2f
        val left: Float = centerX - _image.width / 2f
        val top: Float = centerY - _image.height / 2f
        if (isAutoBackground(image)) {
            if (strokePaint.strokeWidth != 0f) {
                canvas.drawCircle(centerX, centerY, _radius + _spaceWidth + strokePaint.strokeWidth / 2f, strokePaint)
            }
            canvas.drawBitmap(bitmap, left, top, waveBitmapPaint)
        } else {
            canvas.drawBitmap(bitmap, left, top, waveBitmapPaint)
        }
        if (_showText) {
            canvas.drawText(textOrDefault(), centerX, centerY + textPaint.getBaseLine(), textPaint)
        }
        if (updateInterval != -1L) {
            postInvalidateDelayed(updateInterval)
        }
    }

    /**
     * Set [image].
     *
     * @since 0.5.5
     */
    fun setImage(drawable: Drawable?) {
        if (drawable != null) {
            image = drawable
            _image = getBitmapFromDrawable(drawable)
        } else {
            image = null
            _image = createDefaultImageBitmap(_radius.roundToInt())
        }
        requestLayout()
    }

    /**
     * Set [image].
     *
     * @since 0.5.5
     */
    fun setImage(@DrawableRes id: Int) {
        runCatching {
            val drawable = AppCompatResources.getDrawable(context, id)
            if (drawable != null) {
                image = drawable
                _image = getBitmapFromDrawable(drawable)
            } else {
                image = null
                _image = createDefaultImageBitmap(_radius.roundToInt())
            }
        }.onFailure {
            image = null
            _image = createDefaultImageBitmap(_radius.roundToInt())
        }
        requestLayout()
    }

    /**
     * Set the [waveSpeed] of the wave.
     *
     * @since 0.2.0
     */
    fun setSpeed(@FloatRange(from = 0.0) speed: Float) {
        waveSpeed = speed.coerceAtLeast(0f)
        invalidate()
    }

    /**
     * Set update interval time between every frame in milliseconds.
     *
     * @param interval If the value of [interval] is -1, the wave will stop
     * move.
     * @since 0.5.2
     */
    fun setUpdateInterval(@IntRange(from = -1) interval: Long) {
        updateInterval = interval.coerceAtLeast(-1)
        invalidate()
    }

    /**
     * Set the width and height of the wave.
     *
     * @since 0.2.0
     */
    fun setWave(@FloatRange(from = 0.0) width: Float, @FloatRange(from = 0.0) height: Float) {
        if (waveWidth == width && waveHeight == height) return
        waveWidth = width.coerceAtLeast(0f)
        waveHeight = height.coerceAtLeast(0f)
        halfWaveWidth = width / 4
        waveCount = calWaveCount(_image.width, waveWidth)
        invalidate()
    }

    /**
     * Create a circular Bimap with radius [radius] and color
     * [progressBackgroundColor].
     *
     * @since 1.5.2
     */
    private fun createDefaultImageBitmap(radius: Int): Bitmap {
        return createBitmap(2 * radius, 2 * radius).apply {
            Canvas(this).drawCircle(radius.toFloat(), radius.toFloat(), radius.toFloat(), backgroundPaint)
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
        val bitmap = createBitmap(width, height)
        val canvas = Canvas(bitmap)
        val currentY =
            if (_maximumProgress == _currentProgress) (-0.05f * height).toInt()
            else if (0f == _currentProgress) (1.05f * height).toInt()
            else (height * (_maximumProgress - _currentProgress) / _maximumProgress).toInt()
        wavePath.reset()
        wavePath.moveTo(-waveOffsetDistance, currentY.toFloat())
        for (i in 0..waveCount) {
            wavePath.quadTo(
                i * waveWidth + halfWaveWidth - waveOffsetDistance,
                currentY - waveHeight,
                i * waveWidth + halfWaveWidth * 2 - waveOffsetDistance,
                currentY.toFloat()
            )
            wavePath.quadTo(
                i * waveWidth + halfWaveWidth * 3 - waveOffsetDistance,
                currentY + waveHeight,
                i * waveWidth + halfWaveWidth * 4 - waveOffsetDistance,
                currentY.toFloat()
            )
        }
        wavePath.lineTo(width.toFloat(), height.toFloat())
        wavePath.lineTo(0f, height.toFloat())
        wavePath.close()
        waveRectF.set(0f, 0f, width.toFloat(), height.toFloat())
        val cs = canvas.saveLayer(waveRectF, null)
        canvas.drawPath(wavePath, wavePaint)
        waveOffsetDistance += waveSpeed
        waveOffsetDistance %= waveWidth
        wavePaint.xfermode = xfermode
        val alpha = wavePaint.alpha
        wavePaint.alpha = 255 // FIX Avoid wavePaint transparency affecting _image paint.
        canvas.drawBitmap(_image, 0f, 0f, wavePaint)
        wavePaint.alpha = alpha
        wavePaint.xfermode = null
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
            (width / waveWidth + 1).roundToInt()
        } else {
            (width / waveWidth + 2).roundToInt()
        }
        return count
    }

    /**
     * Used to determine whether a default background is required (the default
     * is a circular background).
     *
     * @since 1.5.2
     */
    @OptIn(ExperimentalContracts::class)
    private fun isAutoBackground(drawable: Drawable?): Boolean {
        contract {
            returns(false) implies (drawable != null)
        }
        return drawable == null
    }

    init {
        context.withStyledAttributes(attrs, R.styleable.WaveProgressView, defStyleAttr, defStyleRes) {
            _maximumProgress = getFloat(R.styleable.WaveProgressView_progress_maximum_value, DEFAULT_MAXIMUM_PROGRESS)
            _currentProgress = getFloat(R.styleable.WaveProgressView_progress_current_value, DEFAULT_CURRENT_PROGRESS)
            _showText = getBoolean(R.styleable.WaveProgressView_wave_progress_show_text, true)
            _text = getString(R.styleable.WaveProgressView_progress_text) ?: ""
            textPaint.textSize = getDimension(R.styleable.WaveProgressView_progress_text_size, DEFAULT_TEXT_SIZE)
            textPaint.color = getColor(R.styleable.WaveProgressView_progress_text_color, color(R.color.md_theme_onPrimary))
            backgroundPaint.color = getColor(R.styleable.WaveProgressView_progress_background_color, color(R.color.md_theme_primaryContainer))
            wavePaint.color = getColor(R.styleable.WaveProgressView_progress_color, color(R.color.md_theme_primary))
            waveWidth = getFloat(R.styleable.WaveProgressView_wave_progress_wave_width, 100f.DP)
            halfWaveWidth = waveWidth / 4
            waveHeight = getFloat(R.styleable.WaveProgressView_wave_progress_wave_height, 10f.DP)
            waveSpeed = getFloat(R.styleable.WaveProgressView_wave_progress_wave_speed, waveWidth / 70)
            strokePaint.color = getColor(R.styleable.WaveProgressView_wave_progress_stroke_color, color(R.color.md_theme_primary))
            strokePaint.strokeWidth = getDimension(R.styleable.WaveProgressView_wave_progress_stroke_width, 0f)
            _radius = getDimension(R.styleable.WaveProgressView_wave_progress_radius, 0f).coerceAtLeast(0f)
            _spaceWidth = getDimension(R.styleable.WaveProgressView_wave_progress_space_width, 0f)
            val src = runCatching { AppCompatResources.getDrawable(context, getResourceIdOrThrow(R.styleable.WaveProgressView_wave_progress_image)) }
                .getOrNull().also { image = it }
            _image = if (src != null) getBitmapFromDrawable(src) else createDefaultImageBitmap(_radius.roundToInt())
            waveCount = calWaveCount(_image.width, waveWidth)
        }
        setLayerType(LAYER_TYPE_SOFTWARE, null)
    }

}