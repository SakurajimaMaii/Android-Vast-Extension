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
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Shader
import android.util.AttributeSet
import androidx.annotation.ColorInt
import androidx.annotation.FloatRange
import com.ave.vastgui.core.extension.nothing_to_do
import com.ave.vastgui.tools.R
import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.math.cos
import kotlin.math.sin

// Author: Vast Gui 
// Email: guihy2019@gmail.com
// Date: 2022/4/17 19:55
// Documentation: https://ave.entropy2020.cn/documents/VastTools/core-topics/ui/progress/arc-progress-view/

/**
 * ArcProgressView.
 *
 * @property mArcRectF The scope of the arc.
 * @property mProgressRadius Radius of the circle.
 * @property mProgressWidth Width of the circle progress.
 * @property mStartpointCircleColor Progress startpoint circle color-int.
 * @property mEndpointCircleColor Progress endpoint circle color-int.
 * @property mProgressShader Progress shader.
 * @property mShowStartpointCircle Ture if you want to show the start and
 *     end circles, false otherwise.
 * @property mShowEndpointCircle Ture if you want to show the start and end
 *     circles, false otherwise.
 * @since 0.2.0
 */
class ArcProgressView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.Default_ArcProgressView_Style,
    defStyleRes: Int = R.style.BaseArcProgressView
) : ProgressView(context, attrs, defStyleAttr, defStyleRes) {

    private val mDefaultRadius: Float = resources.getDimension(R.dimen.default_arc_progress_radius)
    private val mDefaultWidth: Float = resources.getDimension(R.dimen.default_arc_progress_width)

    private var mProgressBackgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
    }
    private var mProgressPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
    }
    private var mStartpointCirclePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }
    private var mEndpointCirclePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }
    private var mTextPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
    }
    private val mDecimalFormat = DecimalFormat("0.##").also {
        it.roundingMode = RoundingMode.FLOOR
    }
    private val mArcRectF = RectF()
    private var mProgressShader: Shader? = null
    private val mShowStartpointCircle: Boolean =
        mStartpointCircleColor != context.getColor(R.color.transparent)
    private val mShowEndpointCircle: Boolean =
        mStartpointCircleColor != context.getColor(R.color.transparent)

    var mProgressRadius = mDefaultRadius
        set(value) {
            if (value < 0f) return
            field = value
        }

    var mProgressWidth = mDefaultWidth
        set(value) {
            if (value < 0f) return
            field = value
            mProgressBackgroundPaint.strokeWidth = value
            mProgressPaint.strokeWidth = value
        }

    var mStartpointCircleColor: Int
        set(value) {
            mStartpointCirclePaint.color = value
        }
        get() = mStartpointCirclePaint.color

    var mEndpointCircleColor: Int
        set(value) {
            mEndpointCirclePaint.color = value
        }
        get() = mEndpointCirclePaint.color

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val requiredSize = (2 * mProgressRadius + mProgressWidth).toInt()
        val width = resolveSize(requiredSize, widthMeasureSpec)
        val height = resolveSize(requiredSize, heightMeasureSpec)
        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // Draw progress background.
        val circlePoint = (width / 2).toFloat()
        canvas.drawCircle(
            measuredWidth / 2f, measuredHeight / 2f, mProgressRadius, mProgressBackgroundPaint
        )
        // Draw progress.
        mArcRectF.left = mProgressWidth / 2
        mArcRectF.top = mProgressWidth / 2
        mArcRectF.right = measuredWidth - mProgressWidth / 2
        mArcRectF.bottom = measuredHeight - mProgressWidth / 2
        val range = 360 * (mCurrentProgress / mMaximumProgress)
        canvas.drawArc(mArcRectF, -90f, range, false, mProgressPaint)
        if (mShowStartpointCircle && mProgressShader == null) {
            // Draw progress start.
            canvas.drawCircle(
                measuredWidth / 2f, mProgressWidth / 2f, mProgressWidth / 2f, mStartpointCirclePaint
            )
        }
        if (mShowEndpointCircle && mProgressShader == null) {
            // Draw progress end.
            val x1 = circlePoint - mProgressRadius * cos((range + 90) * 3.14f / 180f)
            val y1 = circlePoint - mProgressRadius * sin((range + 90) * 3.14f / 180f)
            canvas.drawCircle(
                x1, y1, mProgressWidth / 2, mEndpointCirclePaint
            )
        }
        val x1 = circlePoint - mProgressRadius * cos((range + 90) * 3.14f / 180f)
        val y1 = circlePoint - mProgressRadius * sin((range + 90) * 3.14f / 180f)
        mText = "${mDecimalFormat.format((mCurrentProgress / mMaximumProgress) * 100)}%"
        canvas.drawText(
            mText, x1, y1 + getTextBaseline(), mTextPaint
        )
    }

    /**
     * @see ProgressView.setProgressColor
     * @since 0.5.4
     */
    override fun setProgressColor(@ColorInt color: Int) {
        super.setProgressColor(color)
        mProgressPaint.color = mProgressColor
    }

    /**
     * @see ProgressView.setProgressBackgroundColor
     * @since 0.5.4
     */
    override fun setProgressBackgroundColor(@ColorInt color: Int) {
        super.setProgressBackgroundColor(color)
        mProgressBackgroundPaint.color = color
    }

    /**
     * The function will do nothing.
     *
     * @since 0.5.4
     */
    override fun setText(text: String) {
        nothing_to_do()
    }

    /**
     * @see ProgressView.setTextColor
     * @since 0.5.4
     */
    override fun setTextColor(@ColorInt color: Int) {
        super.setTextColor(color)
        mTextPaint.color = color
    }

    /**
     * @see ProgressView.setTextSize
     * @since 0.5.4
     */
    override fun setTextSize(@FloatRange(from = 0.0) size: Float) {
        super.setTextSize(size)
        mTextPaint.textSize = size
    }

    /**
     * Set progress paint shader.
     *
     * @param shader progress paint shader.
     * @since 0.2.0
     */
    fun setProgressShader(shader: Shader?) {
        mProgressShader = mProgressPaint.setShader(shader)
    }

    /**
     * Get text baseline
     *
     * @since 0.5.4
     */
    private fun getTextBaseline(): Float {
        val fontMetrics = mTextPaint.fontMetrics
        val height = fontMetrics.bottom - fontMetrics.top
        return height / 2f - fontMetrics.bottom
    }

    init {
        val typedArray = context.obtainStyledAttributes(
            attrs, R.styleable.ArcProgressView, defStyleAttr, defStyleRes
        )
        mMaximumProgress = typedArray.getFloat(
            R.styleable.ArcProgressView_progress_maximum_value, mDefaultMaximumProgress
        )
        mCurrentProgress = typedArray.getFloat(
            R.styleable.ArcProgressView_progress_current_value, mDefaultCurrentProgress
        )
        mTextSize =
            typedArray.getDimension(R.styleable.ArcProgressView_progress_text_size, mDefaultTexSize)
        mTextPaint.textSize = mTextSize
        mTextColor = typedArray.getColor(
            R.styleable.ArcProgressView_progress_text_color,
            context.getColor(R.color.md_theme_onPrimary)
        )
        mTextPaint.color = mTextColor
        mProgressColor = typedArray.getColor(
            R.styleable.ArcProgressView_progress_color, context.getColor(R.color.md_theme_primary)
        )
        mProgressPaint.color = mProgressColor
        mProgressBackgroundColor = typedArray.getColor(
            R.styleable.ArcProgressView_progress_background_color,
            context.getColor(R.color.md_theme_primaryContainer)
        )
        mProgressBackgroundPaint.color = mProgressBackgroundColor
        mProgressRadius =
            typedArray.getDimension(R.styleable.ArcProgressView_arc_progress_radius, mDefaultRadius)
        mProgressWidth =
            typedArray.getDimension(R.styleable.ArcProgressView_arc_progress_width, mDefaultWidth)
        mStartpointCircleColor = typedArray.getColor(
            R.styleable.ArcProgressView_arc_progress_startpoint_circle_color,
            context.getColor(R.color.md_theme_primary)
        )
        mEndpointCircleColor = typedArray.getColor(
            R.styleable.ArcProgressView_arc_progress_endpoint_circle_color,
            context.getColor(R.color.md_theme_primary)
        )
        typedArray.recycle()
    }

}