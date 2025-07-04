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
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.graphics.Shader
import android.util.AttributeSet
import androidx.annotation.ColorInt
import androidx.core.content.withStyledAttributes
import androidx.core.graphics.withClip
import com.ave.vastgui.tools.R
import com.ave.vastgui.tools.graphics.getBaseLine
import com.ave.vastgui.tools.graphics.getTextHeight
import com.ave.vastgui.tools.utils.ColorUtils
import com.ave.vastgui.tools.utils.color
import com.ave.vastgui.tools.utils.dimension
import java.text.DecimalFormat
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.properties.Delegates

// Author: Vast Gui 
// Email: guihy2019@gmail.com
// Date: 2022/4/17 19:55
// Documentation: https://sakurajimamaii.github.io/AVE-DOC/documents/tools/core-topics/ui/progress/arc-progress-view/

/**
 * [ArcProgressView].
 *
 * @since 0.2.0
 */
class ArcProgressView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.Default_ArcProgressView_Style,
    defStyleRes: Int = R.style.BaseArcProgressView
) : ProgressView(context, attrs, defStyleAttr, defStyleRes) {

    /** @since 1.5.2 */
    @Suppress("PrivatePropertyName")
    private val DEFAULT_RADIUS: Float = dimension(R.dimen.default_arc_progress_radius)

    /** @since 1.5.2 */
    private val progressBackgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
    }

    /** @since 1.5.2 */
    private val progressPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
    }

    /** @since 1.5.2 */
    private val startpointCirclePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }

    /** @since 1.5.2 */
    private val endpointCirclePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }

    /** @since 1.5.2 */
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
    }

    /** @since 1.5.2 */
    private val arcRectF = RectF()

    /** @since 1.5.2 */
    private val textScopePath: Path = Path()

    /**
     * `ture` if the startpoint circle needs to be displayed, `false`
     * otherwise.
     *
     * @since 1.5.2
     */
    private val isStartpointCircleShow: Boolean
        get() = startpointCircleColor != color(R.color.transparent)

    /**
     * `ture` if the endpoint circle needs to be displayed, `false` otherwise.
     *
     * @since 1.5.2
     */
    private val isEndpointCircleShow: Boolean
        get() = endpointCircleColor != color(R.color.transparent)

    override val defaultText: String
        get() = DecimalFormat("##0%").format(_currentProgress / _maximumProgress)


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

    override var progressColor: Int
        set(value) {
            if (progressPaint.color == value || progressShader != null) return
            check(ColorUtils.isColorInt(value)) {
                "The color-int(current=${value.toUInt().toString(16)}) of progress is invalid."
            }
            progressPaint.color = value
            invalidate()
        }
        get() = progressPaint.color

    override var progressBackgroundColor: Int
        set(value) {
            if (progressBackgroundPaint.color == value) return
            check(ColorUtils.isColorInt(value)) {
                "The color-int(current=${value.toUInt().toString(16)}) of progress background is invalid."
            }
            progressBackgroundPaint.color = value
            invalidate()
        }
        get() = progressBackgroundPaint.color

    /** @since 1.5.2 */
    private var _text: String = ""

    override var text: String
        get() = _text
        set(value) {
            _text = value
            invalidate()
        }

    override var textColor: Int
        set(value) {
            if (textPaint.color == value) return
            check(ColorUtils.isColorInt(value)) {
                "The color-int(current=${value.toUInt().toString(16)}) of text is invalid."
            }
            textPaint.color = value
            invalidate()
        }
        get() = textPaint.color

    override var textSize: Float
        set(value) {
            textPaint.textSize = value.coerceAtLeast(0f)
            invalidate()
        }
        get() = textPaint.textSize

    /** @since 1.5.2 */
    private var _showText: Boolean by Delegates.notNull()

    /**
     * `true` if you want to show the text, `false` otherwise.
     *
     * @since 1.5.2
     */
    var showText: Boolean
        set(value) {
            if (_showText == value) return
            _showText = value
            invalidate()
        }
        get() = _showText

    /** @since 1.5.2 */
    var progressShader: Shader? = null
        set(value) {
            field = progressPaint.setShader(value)
            invalidate()
        }

    /** @since 1.5.2 */
    private var _progressRadius = DEFAULT_RADIUS

    /**
     * Radius of the circle(in pixels).
     *
     * @since 1.5.2
     */
    var progressRadius: Float
        set(value) {
            if (_progressRadius == value) return
            _progressRadius = value.coerceAtLeast(0f)
            requestLayout()
        }
        get() = _progressRadius

    /** @since 1.5.2 */
    private var _progressWidth = recommendedWidth()
        set(value) {
            field = value
            progressBackgroundPaint.strokeWidth = field
            progressPaint.strokeWidth = field
        }

    /**
     * Width of the circle progress.
     *
     * @since 1.5.2
     */
    var progressWidth
        set(value) {
            if (_progressWidth == value) return
            _progressWidth = value.coerceAtLeast(0f)
            requestLayout()
        }
        get() = _progressWidth

    /**
     * The color-int of progress startpoint circle.
     *
     * @since 1.5.2
     */
    @get:ColorInt
    @setparam:ColorInt
    var startpointCircleColor: Int
        set(value) {
            if (startpointCirclePaint.color == value) return
            check(ColorUtils.isColorInt(value)) {
                "The color-int(current=${value.toUInt().toString(16)}) of progress startpoint circle is invalid."
            }
            startpointCirclePaint.color = value
            invalidate()
        }
        get() = startpointCirclePaint.color

    /**
     * The color-int of progress endpoint circle.
     *
     * @since 1.5.2
     */
    @get:ColorInt
    @setparam:ColorInt
    var endpointCircleColor: Int
        set(value) {
            if (endpointCirclePaint.color == value) return
            check(ColorUtils.isColorInt(value)) {
                "The color-int(current=${value.toUInt().toString(16)}) of progress endpoint circle is invalid."
            }
            endpointCirclePaint.color = value
            invalidate()
        }
        get() = endpointCirclePaint.color

    /** @since 1.5.2 */
    private var _endpointCircleRadius: Float = recommendedRadius()

    /**
     * The radius of the endpoint circle.
     *
     * @since 1.5.2
     */
    var endpointCircleRadius: Float
        set(value) {
            if (_endpointCircleRadius == value) return
            _endpointCircleRadius = value.coerceAtLeast(0f)
            requestLayout()
        }
        get() = _endpointCircleRadius

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val viewMinimumWidth = (2f * _progressRadius + 2f * max(_progressWidth / 2f, _endpointCircleRadius) + paddingStart + paddingEnd).roundToInt()
        val viewMinimumHeight = (2f * _progressRadius + 2f * max(_progressWidth / 2f, _endpointCircleRadius) + paddingTop + paddingBottom).roundToInt()
        val neededMinimumWidth = max(viewMinimumWidth, suggestedMinimumWidth)
        val neededMinimumHeight = max(viewMinimumHeight, suggestedMinimumHeight)
        val width = resolveSize(neededMinimumWidth, widthMeasureSpec)
        val height = resolveSize(neededMinimumHeight, heightMeasureSpec)
        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas) {
        val centerX: Float = (paddingStart + measuredWidth - paddingEnd) / 2f
        val centerY: Float = (paddingTop + measuredHeight - paddingBottom) / 2f
        canvas.drawCircle(centerX, centerY, _progressRadius, progressBackgroundPaint)
        arcRectF.set(centerX - _progressRadius, centerY - _progressRadius, centerX + _progressRadius, centerY + _progressRadius)
        val range: Float = 360f * (_currentProgress / _maximumProgress)
        canvas.drawArc(arcRectF, -90f, range, false, progressPaint)
        if (isStartpointCircleShow) {
            canvas.drawCircle(centerX, centerY - _progressRadius, _progressWidth / 2f, startpointCirclePaint)
        }
        if (isEndpointCircleShow) {
            val x1 = centerX - _progressRadius * cos((range + 90) * 3.14f / 180f)
            val y1 = centerY - _progressRadius * sin((range + 90) * 3.14f / 180f)
            canvas.drawCircle(x1, y1, _endpointCircleRadius, endpointCirclePaint)
        }
        if (_showText) {
            val x1 = centerX - _progressRadius * cos((range + 90) * 3.14f / 180f)
            val y1 = centerY - _progressRadius * sin((range + 90) * 3.14f / 180f)
            textScopePath.reset()
            textScopePath.addCircle(x1, y1, _endpointCircleRadius, Path.Direction.CW)
            canvas.withClip(textScopePath) {
                canvas.drawText(textOrDefault(), x1, y1 + textPaint.getBaseLine(), textPaint)
            }
        }
    }

    /**
     * Recommended value of [_endpointCircleRadius].
     *
     * @since 0.5.5
     */
    fun recommendedRadius(): Float =
        textPaint.measureText(_text.ifEmpty { "000%" })
            .coerceAtLeast(textPaint.getTextHeight()) / 2f

    /**
     * Recommended value of [_progressWidth].
     *
     * @since 0.5.5
     */
    fun recommendedWidth(): Float = recommendedRadius() * 2f

    init {
        context.withStyledAttributes(attrs, R.styleable.ArcProgressView, defStyleAttr, defStyleRes) {
            _maximumProgress = getFloat(R.styleable.ArcProgressView_progress_maximum_value, DEFAULT_MAXIMUM_PROGRESS)
            _currentProgress = getFloat(R.styleable.ArcProgressView_progress_current_value, DEFAULT_CURRENT_PROGRESS)
            _text = getString(R.styleable.ArcProgressView_progress_text) ?: ""
            textPaint.textSize = getDimension(R.styleable.ArcProgressView_progress_text_size, DEFAULT_TEXT_SIZE)
            textPaint.color = getColor(R.styleable.ArcProgressView_progress_text_color, color(R.color.md_theme_onPrimary))
            progressPaint.color = getColor(R.styleable.ArcProgressView_progress_color, color(R.color.md_theme_primary))
            progressBackgroundPaint.color = getColor(R.styleable.ArcProgressView_progress_background_color, color(R.color.md_theme_primaryContainer))
            _showText = getBoolean(R.styleable.ArcProgressView_arc_progress_show_text, true)
            _progressRadius = getDimension(R.styleable.ArcProgressView_arc_progress_radius, DEFAULT_RADIUS)
            _progressWidth = getDimension(R.styleable.ArcProgressView_arc_progress_width, recommendedWidth())
            startpointCirclePaint.color = getColor(R.styleable.ArcProgressView_arc_progress_startpoint_circle_color, color(R.color.transparent))
            endpointCirclePaint.color = getColor(R.styleable.ArcProgressView_arc_progress_endpoint_circle_color, color(R.color.md_theme_primary))
            _endpointCircleRadius = getDimension(R.styleable.ArcProgressView_arc_progress_endpoint_circle_radius, recommendedRadius())
        }
    }

}