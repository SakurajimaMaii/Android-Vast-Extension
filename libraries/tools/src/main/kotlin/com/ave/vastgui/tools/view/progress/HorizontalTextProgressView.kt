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
import android.util.AttributeSet
import androidx.annotation.ColorInt
import androidx.core.content.withStyledAttributes
import androidx.core.graphics.withClip
import com.ave.vastgui.tools.R
import com.ave.vastgui.tools.graphics.getBaseLine
import com.ave.vastgui.tools.utils.ColorUtils
import com.ave.vastgui.tools.utils.color
import com.ave.vastgui.tools.utils.dimension
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/4/4
// Documentation: https://sakurajimamaii.github.io/AVE-DOC/documents/tools/core-topics/ui/progress/horizontal-progress-view/

/**
 * [HorizontalTextProgressView]
 *
 * @since 0.2.0
 */
class HorizontalTextProgressView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.Default_HorizontalTextProgressView_Style,
    defStyleRes: Int = R.style.BaseHorizontalTextProgressView
) : ProgressView(context, attrs, defStyleAttr, defStyleRes) {

    /** @since 1.5.2 */
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textAlign = Paint.Align.CENTER
    }

    /** @since 1.5.2 */
    private val progressPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    /** @since 1.5.2 */
    private val backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    /** @since 1.5.2 */
    private val boxPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    /** @since 1.5.2 */
    private val progressPath = Path()

    /** @since 1.5.2 */
    private val progressRectF = RectF()

    /** @since 1.5.2 */
    private val boxRectF = RectF()

    /**
     * The width of the text(in pixels).
     *
     * @since 1.5.2
     */
    private val textWidth: Float
        get() = textPaint.measureText(textOrDefault())

    override var textSize: Float
        get() = textPaint.textSize
        set(value) {
            if (textPaint.textSize == value) return
            textPaint.textSize = value.coerceAtLeast(0f)
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

    override var progressColor: Int
        get() = progressPaint.color
        set(value) {
            if (progressPaint.color == value) return
            check(ColorUtils.isColorInt(value)) {
                "The color-int(current=${value.toUInt().toString(16)}) of progress is invalid."
            }
            progressPaint.color = value
            invalidate()
        }

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

    private var _progressHeight: Float = dimension(R.dimen.default_horizontal_text_progress_height)

    /**
     * The height of the progress(in pixels).
     *
     * @since 1.5.2
     */
    var progressHeight: Float
        get() = _progressHeight
        set(value) {
            if(_progressHeight == value) return
            _progressHeight = value.coerceAtLeast(0f)
            requestLayout()
        }

    /**
     * The color-int of text box.
     *
     * @since 1.5.2
     */
    @get:ColorInt
    @setparam:ColorInt
    var textBoxColor: Int
        get() = boxPaint.color
        set(value) {
            if (boxPaint.color == value) return
            check(ColorUtils.isColorInt(value)) {
                "The color-int(current=${value.toUInt().toString(16)}) of text box is invalid."
            }
            boxPaint.color = value
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

    /** @since 1.5.2 */
    private var _textMargin: Float = dimension(R.dimen.default_horizontal_text_progress_text_margin)

    /**
     * The margin width of the text(in pixels).
     *
     * @since 1.5.2
     */
    var textMargin
        get() = _textMargin
        set(value) {
            if(_textMargin == value) return
            _textMargin = value.coerceAtLeast(0f)
            requestLayout()
        }

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
            _currentProgress = value.coerceIn(0f, maximumProgress)
            invalidate()
        }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val contentMinimumWidth = 2f * textMargin
        val contentMinimumHeight = progressHeight + 2f * textMargin
        val neededMinimumWidth = max(contentMinimumWidth.roundToInt() + paddingStart + paddingEnd, suggestedMinimumWidth)
        val neededMinimumHeight = max(contentMinimumHeight.roundToInt() + paddingTop + paddingBottom, suggestedMinimumHeight)
        val width = resolveSize(neededMinimumWidth, widthMeasureSpec)
        val height = resolveSize(neededMinimumHeight, heightMeasureSpec)
        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas) {
        val left = paddingStart.toFloat()
        val top = (paddingTop + measuredHeight - paddingBottom) / 2f - progressHeight / 2f
        val right = (measuredWidth - paddingEnd).toFloat()
        val bottom = (paddingTop + measuredHeight - paddingBottom) / 2f + progressHeight / 2f
        progressRectF.set(left, top, right, bottom)
        val radius = min(progressRectF.width(), progressRectF.height()) / 2f

        // Draw background
        canvas.drawRoundRect(progressRectF, radius, radius, backgroundPaint)

        // Draw progress
        progressPath.reset()
        progressPath.addRoundRect(progressRectF, radius, radius, Path.Direction.CW)
        canvas.withClip(progressPath) {
            val width = (currentProgress / maximumProgress) * (measuredWidth - paddingStart - paddingEnd)
            progressRectF.set(left, top, left + width, bottom)
            canvas.drawRect(progressRectF, progressPaint)
        }

        // Draw text box
        drawBox(canvas)
    }

    /**
     * Draw text box.
     *
     * @since 0.5.3
     */
    private fun drawBox(canvas: Canvas) {
        val progressWidth: Float = (currentProgress / maximumProgress) * (measuredWidth - paddingStart - paddingEnd)
        val centerY = (paddingTop + measuredHeight - paddingBottom) / 2f
        // The width of text box.
        val boxWidth = textMargin * 2f + textWidth
        // The maximum value in order to ensure that the right side of
        // the TextBox will not cross the border.
        // The minimum value in order to ensure that the left side of
        // the TextBox will not cross the border.
        val centerX = (progressWidth + paddingStart).coerceIn(paddingStart + boxWidth / 2f, measuredWidth - paddingEnd - boxWidth / 2f)
        val boxHeight = (progressHeight + 2 * textMargin)
        boxRectF.set(centerX - boxWidth / 2f, centerY - boxHeight / 2f,
            centerX + boxWidth / 2f, centerY + boxHeight / 2f)
        val radius = min(boxRectF.width(), boxRectF.height()) / 2f
        canvas.drawRoundRect(boxRectF, radius, radius, boxPaint)
        // Draw text of box.
        canvas.drawText(textOrDefault(), centerX, centerY + textPaint.getBaseLine(), textPaint)
    }

    init {
        context.withStyledAttributes(attrs, R.styleable.HorizontalTextProgressView, defStyleAttr, defStyleRes) {
            _maximumProgress = getFloat(R.styleable.HorizontalTextProgressView_progress_maximum_value, DEFAULT_MAXIMUM_PROGRESS)
            _currentProgress = getFloat(R.styleable.HorizontalTextProgressView_progress_current_value, DEFAULT_CURRENT_PROGRESS)
            _text = getString(R.styleable.HorizontalTextProgressView_progress_text) ?: ""
            textPaint.color = getColor(R.styleable.HorizontalTextProgressView_progress_text_color, color(R.color.md_theme_onPrimary))
            textPaint.textSize = getDimension(R.styleable.HorizontalTextProgressView_progress_text_size, DEFAULT_TEXT_SIZE)
            boxPaint.color = getColor(R.styleable.HorizontalTextProgressView_progress_color, color(R.color.md_theme_primary))
            progressPaint.color = getColor(R.styleable.HorizontalTextProgressView_progress_color, color(R.color.md_theme_primary))
            backgroundPaint.color = getColor(R.styleable.HorizontalTextProgressView_progress_background_color, color(R.color.md_theme_primaryContainer))
            _progressHeight = getDimension(R.styleable.HorizontalTextProgressView_horizontal_text_progress_height, dimension(R.dimen.default_horizontal_text_progress_height))
            textMargin = getDimension(R.styleable.HorizontalTextProgressView_horizontal_text_progress_text_margin, dimension(R.dimen.default_horizontal_text_progress_text_margin))
        }
    }

}