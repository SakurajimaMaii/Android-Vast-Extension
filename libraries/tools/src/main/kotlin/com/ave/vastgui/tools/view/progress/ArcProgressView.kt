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
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Shader
import android.util.AttributeSet
import com.ave.vastgui.core.extension.NotNUllVar
import com.ave.vastgui.tools.R
import com.ave.vastgui.tools.graphics.getBaseLine
import com.ave.vastgui.tools.graphics.getTextHeight
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
 * @property mShowText True if you want to show the text, false otherwise.
 * @property mProgressShader Progress shader.
 * @property mShowStartpointCircle Ture if you want to show the startpoint
 *     circle, false otherwise.
 * @property mShowEndpointCircle Ture if you want to show the endpoint
 *     circle, false otherwise.
 * @property mEndpointCircleRadius The radius of the endpoint circle.
 * @since 0.2.0
 */
class ArcProgressView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.Default_ArcProgressView_Style,
    defStyleRes: Int = R.style.BaseArcProgressView
) : ProgressView(context, attrs, defStyleAttr, defStyleRes) {

    private val mDefaultRadius: Float = resources.getDimension(R.dimen.default_arc_progress_radius)

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
    private val mArcRectF = RectF()
    private val mShowStartpointCircle: Boolean =
        mStartpointCircleColor != context.getColor(R.color.transparent)
    private val mShowEndpointCircle: Boolean =
        mEndpointCircleColor != context.getColor(R.color.transparent)

    override val mDefaultText: String
        get() = DecimalFormat("##0%").format(mCurrentProgress / mMaximumProgress)

    override var mProgressColor: Int
        set(value) {
            mProgressPaint.color = value
        }
        get() = mProgressPaint.color

    override var mProgressBackgroundColor: Int
        set(value) {
            mProgressBackgroundPaint.color = value
        }
        get() = mProgressBackgroundPaint.color

    override var mTextColor: Int
        set(value) {
            mTextPaint.color = value
        }
        get() = mTextPaint.color

    override var mTextSize: Float
        set(value) {
            mTextPaint.textSize = value
        }
        get() = mTextPaint.textSize

    var mShowText: Boolean by NotNUllVar()

    var mProgressShader: Shader? = null
        set(value) {
            field = mProgressPaint.setShader(value)
        }

    var mProgressRadius = mDefaultRadius
        set(value) {
            field = value.coerceAtLeast(0f)
        }

    var mProgressWidth = recommendedWidth()
        set(value) {
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

    var mEndpointCircleRadius: Float = recommendedRadius()
        set(value) {
            field = value.coerceAtLeast(0f)
        }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val requiredSize =
            (2f * mProgressRadius + 2f * mEndpointCircleRadius).toInt()
        val width = resolveSize(requiredSize, widthMeasureSpec)
        val height = resolveSize(requiredSize, heightMeasureSpec)
        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas) {
        val circlePoint: Float = measuredWidth / 2f
        canvas.drawCircle(
            measuredWidth / 2f, measuredHeight / 2f, mProgressRadius, mProgressBackgroundPaint
        )
        mArcRectF.left = measuredWidth / 2f - mProgressRadius
        mArcRectF.top = measuredWidth / 2f - mProgressRadius
        mArcRectF.right = measuredWidth / 2f + mProgressRadius
        mArcRectF.bottom = measuredWidth / 2f + mProgressRadius
        val range: Float = 360f * (mCurrentProgress / mMaximumProgress)
        canvas.drawArc(mArcRectF, -90f, range, false, mProgressPaint)
        if (mShowStartpointCircle) {
            canvas.drawCircle(
                measuredWidth / 2f,
                measuredHeight / 2f - mProgressRadius,
                mProgressWidth / 2f,
                mStartpointCirclePaint
            )
        }
        if (mShowEndpointCircle) {
            val x1 = circlePoint - mProgressRadius * cos((range + 90) * 3.14f / 180f)
            val y1 = circlePoint - mProgressRadius * sin((range + 90) * 3.14f / 180f)
            canvas.drawCircle(
                x1, y1, mEndpointCircleRadius, mEndpointCirclePaint
            )
        }
        if (mShowText) {
            val x1 = circlePoint - mProgressRadius * cos((range + 90) * 3.14f / 180f)
            val y1 = circlePoint - mProgressRadius * sin((range + 90) * 3.14f / 180f)
            canvas.drawText(
                textOrDefault(), x1, y1 + mTextPaint.getBaseLine(), mTextPaint
            )
        }
    }

    /**
     * Recommended value of [mEndpointCircleRadius].
     *
     * @since 0.5.5
     */
    fun recommendedRadius(): Float =
        mTextPaint.measureText(mText.ifEmpty { "000%" }).coerceAtLeast(mTextPaint.getTextHeight()) / 2f

    /**
     * Recommended value of [mProgressWidth].
     *
     * @since 0.5.5
     */
    fun recommendedWidth(): Float =
        recommendedRadius() * 2f

    init {
        val typedArray =
            context.obtainStyledAttributes(
                attrs, R.styleable.ArcProgressView, defStyleAttr, defStyleRes
            )
        mMaximumProgress =
            typedArray.getFloat(
                R.styleable.ArcProgressView_progress_maximum_value, mDefaultMaximumProgress
            )
        mCurrentProgress =
            typedArray.getFloat(
                R.styleable.ArcProgressView_progress_current_value, mDefaultCurrentProgress
            )
        mText =
            typedArray.getString(R.styleable.ArcProgressView_progress_text) ?: ""
        mTextSize =
            typedArray.getDimension(
                R.styleable.ArcProgressView_progress_text_size,
                mDefaultTexSize
            )
        mTextColor =
            typedArray.getColor(
                R.styleable.ArcProgressView_progress_text_color,
                context.getColor(R.color.md_theme_onPrimary)
            )
        mProgressColor =
            typedArray.getColor(
                R.styleable.ArcProgressView_progress_color,
                context.getColor(R.color.md_theme_primary)
            )
        mProgressBackgroundColor =
            typedArray.getColor(
                R.styleable.ArcProgressView_progress_background_color,
                context.getColor(R.color.md_theme_primaryContainer)
            )
        mShowText =
            typedArray.getBoolean(R.styleable.ArcProgressView_arc_progress_show_text, true)
        mProgressRadius =
            typedArray.getDimension(R.styleable.ArcProgressView_arc_progress_radius, mDefaultRadius)
        mProgressWidth =
            typedArray.getDimension(
                R.styleable.ArcProgressView_arc_progress_width,
                recommendedWidth()
            )
        mStartpointCircleColor =
            typedArray.getColor(
                R.styleable.ArcProgressView_arc_progress_startpoint_circle_color,
                context.getColor(R.color.transparent)
            )
        mEndpointCircleColor =
            typedArray.getColor(
                R.styleable.ArcProgressView_arc_progress_endpoint_circle_color,
                context.getColor(R.color.md_theme_primary)
            )
        mEndpointCircleRadius =
            typedArray.getDimension(
                R.styleable.ArcProgressView_arc_progress_endpoint_circle_radius,
                recommendedRadius()
            )
        typedArray.recycle()
    }

}