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
import com.ave.vastgui.tools.graphics.getTextHeight
import com.ave.vastgui.tools.utils.ColorUtils
import com.ave.vastgui.tools.utils.DensityUtils.DP
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
 * [LineTextProgressView]
 *
 * @since 0.2.0
 */
class LineTextProgressView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.Default_LineTextProgressView_Style,
    defStyleRes: Int = R.style.BaseLineTextProgressView
) : ProgressView(context, attrs, defStyleAttr, defStyleRes) {

    /**
     * The base length of the triangle at the bottom of the text box.
     *
     * @since 1.5.2
     */
    private val triangleBaseLength = 12f.DP

    /**
     * The height of the triangle at the bottom of the text box.
     *
     * @since 1.5.2
     */
    private val triangleHeight = 8f.DP

    /** @since 1.5.2 */
    private val trianglePath = Path()

    /** @since 1.5.2 */
    private var boxPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }

    /** @since 1.5.2 */
    private val boxRectF = RectF()

    /** @since 1.5.2 */
    private var textPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    /** @since 1.5.2 */
    private var progressPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    /** @since 1.5.2 */
    private var backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    /** @since 1.5.2 */
    private val progressRectF = RectF()

    /** @since 1.5.2 */
    private val progressPath = Path()

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

    /** @since 1.5.2 */
    private var _textboxWidth: Float = 0f.DP

    /**
     * The width of the box(in pixels).
     *
     * @since 1.5.2
     */
    var textboxWidth: Float
        get() = _textboxWidth
        set(value) {
            if (_textboxWidth == value) return
            _textboxWidth = value.coerceAtLeast(0f)
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
    private var _progressHeight: Float = dimension(R.dimen.default_horizontal_text_progress_height)

    /**
     * The height of the progress(in pixels).
     *
     * @since 1.5.2
     */
    var progressHeight: Float
        get() = _progressHeight
        set(value) {
            if (_progressHeight == value) return
            _progressHeight = value.coerceAtLeast(0f)
            requestLayout()
        }

    /** @since 1.5.2 */
    private var _text: String = ""

    override var text: String
        get() = _text
        set(value) {
            _text = value
            invalidate()
        }

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

    /** @since 1.5.2 */
    private var _textMargin: Float = dimension(R.dimen.default_linetext_progress_text_margin)

    /**
     * The margin width of the text(in pixels).
     *
     * @since 1.5.2
     */
    var textMargin
        get() = _textMargin
        set(value) {
            if (_textMargin == value) return
            _textMargin = value.coerceAtLeast(0f)
            requestLayout()
        }

    /**
     * The width of the text(in pixels).
     *
     * @since 1.5.2
     */
    private val textWidth: Float
        get() = textPaint.measureText(textOrDefault())

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
        val contentMinimumWidth = textboxWidth + textMargin * 2f
        val contentMinimumHeight = textPaint.getTextHeight() + 2f * textMargin + triangleHeight + progressHeight
        val neededMinimumWidth = max(contentMinimumWidth.roundToInt() + paddingStart + paddingEnd, suggestedMinimumWidth)
        val neededMinimumHeight = max(contentMinimumHeight.roundToInt() + paddingTop + paddingBottom, suggestedMinimumHeight)
        val width = resolveSize(neededMinimumWidth, widthMeasureSpec)
        val height = resolveSize(neededMinimumHeight, heightMeasureSpec)
        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas) {
        val fontMetrics = textPaint.fontMetrics
        val textHeight = fontMetrics.bottom - fontMetrics.top

        val boxWidth = textboxWidth + textMargin * 2f
        val boxHeight = textHeight + textMargin * 2f
        val boxCenterX = (currentProgress / maximumProgress) *
                (measuredWidth - paddingStart - paddingEnd - boxWidth) + boxWidth / 2f + paddingStart
        val boxCenterY = paddingTop + boxHeight / 2f
        boxRectF.set(boxCenterX - boxWidth / 2f, boxCenterY - boxHeight / 2f,
            boxCenterX + boxWidth / 2f, boxCenterY + boxHeight / 2f)
        drawBox(canvas, boxRectF)

        // The distance between the baseline and text central axis
        val textCenter2Bottom: Float = textHeight / 2f - fontMetrics.bottom
        val textX: Float = boxRectF.centerX() - textWidth / 2f
        val textY: Float = boxRectF.centerY() + textCenter2Bottom
        canvas.drawText(textOrDefault(), textX, textY, textPaint)

        val progressLeft: Float = paddingStart + boxRectF.width() / 2f
        val progressTop: Float = boxRectF.bottom + triangleHeight
        val progressRight: Float = measuredWidth - paddingEnd - (boxRectF.width() / 2f)
        val progressBottom: Float = (measuredHeight - paddingBottom).toFloat()
        progressRectF.set(progressLeft, progressTop, progressRight, progressBottom)
        val radius = min(progressRectF.width(), progressRectF.height()) / 2f

        canvas.drawRoundRect(progressRectF, radius, radius, backgroundPaint)

        progressPath.reset()
        progressPath.addRoundRect(progressRectF, radius, radius, Path.Direction.CW)
        canvas.withClip(progressPath) {
            progressRectF.right = progressRectF.left + (currentProgress / maximumProgress) * progressRectF.width()
            canvas.drawRect(progressRectF, progressPaint)
        }
    }

    /** @since 0.5.4 */
    private fun drawBox(canvas: Canvas, boxRectF: RectF) {
        val radius = min(boxRectF.width(), boxRectF.height())
        canvas.drawRoundRect(boxRectF, radius / 4f, radius / 4f, boxPaint)
        trianglePath.reset()
        trianglePath.moveTo(boxRectF.left + boxRectF.width() / 2f - triangleBaseLength / 2f, boxRectF.bottom - 1f.DP)
        trianglePath.lineTo(boxRectF.left + boxRectF.width() / 2f + triangleBaseLength / 2f, boxRectF.bottom - 1f.DP)
        trianglePath.lineTo(boxRectF.left + boxRectF.width() / 2f, boxRectF.bottom + triangleHeight - 1f.DP)
        trianglePath.close()
        canvas.drawPath(trianglePath, boxPaint)
    }

    init {
        context.withStyledAttributes(attrs, R.styleable.LineTextProgressView, defStyleAttr, defStyleRes) {
            _maximumProgress = getFloat(R.styleable.LineTextProgressView_progress_maximum_value, DEFAULT_MAXIMUM_PROGRESS)
            _currentProgress = getFloat(R.styleable.LineTextProgressView_progress_current_value, DEFAULT_CURRENT_PROGRESS)
            _text = getString(R.styleable.LineTextProgressView_progress_text) ?: ""
            textPaint.color = getColor(R.styleable.LineTextProgressView_progress_text_color, color(R.color.md_theme_onPrimary))
            textPaint.textSize = getDimension(R.styleable.LineTextProgressView_progress_text_size, DEFAULT_TEXT_SIZE)
            progressPaint.color = getColor(R.styleable.LineTextProgressView_progress_color, color(R.color.md_theme_primary))
            backgroundPaint.color = getColor(R.styleable.LineTextProgressView_progress_background_color, color(R.color.md_theme_primaryContainer))
            _progressHeight = getDimension(R.styleable.LineTextProgressView_linetext_progress_height, dimension(R.dimen.default_horizontal_text_progress_height))
            boxPaint.color = getColor(R.styleable.LineTextProgressView_linetext_progress_box_color, color(R.color.md_theme_primary))
            _textMargin = getDimension(R.styleable.LineTextProgressView_linetext_progress_text_margin, dimension(R.dimen.default_linetext_progress_text_margin))
            _textboxWidth = getDimension(R.styleable.LineTextProgressView_linetext_progress_box_width, dimension(R.dimen.default_linetext_progress_box_width))
        }
    }

}