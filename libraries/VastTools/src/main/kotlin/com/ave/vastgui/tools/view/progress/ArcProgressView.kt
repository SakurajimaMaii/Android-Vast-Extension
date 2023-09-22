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
 * @property mProgressRadius Radius of the circle.
 * @property mProgressBackgroundWidth Width of the circle progress.
 * @property mProgressStartColorInt Progress start color int.
 * @property mProgressEndColorInt Progress end color int.
 * @property mProgressShader Progress shader.
 * @property mProgressShowStartCircle Ture if you want to show the start
 *     and end circles, false otherwise.
 * @property mProgressShowEndCircle Ture if you want to show the start and
 *     end circles, false otherwise.
 */
class ArcProgressView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.Default_ArcProgressView_Style,
    defStyleRes: Int = R.style.BaseArcProgressView
) : ProgressView(context, attrs, defStyleAttr, defStyleRes) {

    private var mProgressBackgroundPaint = Paint()
    private var mProgressPaint = Paint()
    private var mProgressStartPaint = Paint()
    private var mTextCirclePaint = Paint()
    private var mTextPaint = Paint()
    private val mDecimalFormat = DecimalFormat("0.##").also {
        it.roundingMode = RoundingMode.FLOOR
    }
    private var mProgressStrokeCap: Paint.Cap = Paint.Cap.ROUND
    private val oval = RectF()

    private var mProgressRadius = 0f
    private var mProgressBackgroundWidth = 0f
    private var mProgressShader: Shader? = null
    private var mProgressStartColorInt: Int
    private var mProgressEndColorInt: Int
    private var mProgressShowStartCircle: Boolean = true
    private var mProgressShowEndCircle: Boolean = true

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // Draw progress background.
        val circlePoint = (width / 2).toFloat()
        canvas.drawCircle(
            circlePoint,
            circlePoint,
            circlePoint - mProgressBackgroundWidth / 2,
            mProgressBackgroundPaint
        )
        // Draw progress.
        oval.left = mProgressBackgroundWidth / 2
        oval.top = mProgressBackgroundWidth / 2
        oval.right = width - mProgressBackgroundWidth / 2
        oval.bottom = width - mProgressBackgroundWidth / 2
        val range = 360 * (mCurrentProgress / mMaximumProgress)
        canvas.drawArc(oval, -90f, range, false, mProgressPaint)
        if (mProgressShowStartCircle) {
            // Draw progress start.
            canvas.drawCircle(
                circlePoint,
                mProgressBackgroundWidth / 2,
                mProgressBackgroundWidth / 2,
                mProgressStartPaint
            )
        }
        if (mProgressShowEndCircle) {
            // Draw progress end.
            val radius = circlePoint - mProgressBackgroundWidth / 2
            val x1 = circlePoint - radius * cos((range + 90) * 3.14 / 180)
            val y1 = circlePoint - radius * sin((range + 90) * 3.14 / 180)
            canvas.drawCircle(
                x1.toFloat(),
                y1.toFloat(),
                mProgressBackgroundWidth / 2,
                mTextCirclePaint
            )
        }
        val radius = circlePoint - mProgressBackgroundWidth / 2
        val x1 = circlePoint - radius * cos((range + 90) * 3.14 / 180)
        val y1 = circlePoint - radius * sin((range + 90) * 3.14 / 180)
        val txt = "${mDecimalFormat.format((mCurrentProgress / mMaximumProgress) * 100)}%"
        val stringWidth = mTextPaint.measureText(txt)
        canvas.drawText(
            txt,
            x1.toFloat() - stringWidth / 2,
            y1.toFloat() + mTextSize / 2,
            mTextPaint
        )
    }

    private fun initPaint() {
        mProgressBackgroundPaint.apply {
            strokeWidth = mProgressBackgroundWidth
            color = mProgressBackgroundColor
            isAntiAlias = true
            style = Paint.Style.STROKE
        }
        mProgressPaint.apply {
            if (null == mProgressShader) {
                color = mProgressColor
            } else {
                shader = mProgressShader
            }
            strokeCap = mProgressStrokeCap
            strokeWidth = mProgressBackgroundWidth
            isAntiAlias = true
            style = Paint.Style.STROKE
        }
        mProgressStartPaint.apply {
            color = mProgressStartColorInt
            isAntiAlias = true
            style = Paint.Style.FILL
        }
        mTextCirclePaint.apply {
            color = mProgressEndColorInt
            isAntiAlias = true
            style = Paint.Style.FILL
        }
        mTextPaint.apply {
            textSize = mTextSize
            color = mTextColor
            isAntiAlias = true
            style = Paint.Style.FILL
        }
    }

    /**
     * Set progress paint shader.
     *
     * @param shader progress paint shader.
     */
    fun setProgressShader(shader: Shader?) {
        mProgressShader = shader
    }

    /**
     * Set progress start color.
     *
     * @param color progress start color.
     */
    fun setProgressStartColor(@ColorInt color: Int) {
        mProgressStartColorInt = color
    }

    /**
     * Set progress end color.
     *
     * @param color progress end color.
     */
    fun setProgressEndColor(@ColorInt color: Int) {
        mProgressEndColorInt = color
    }

    /**
     * Show progress start circle.
     *
     * @param show true if you want to show,false otherwise.
     * @since 0.2.0
     */
    fun setProgressShowStartCircle(show: Boolean) {
        mProgressShowStartCircle = show
    }

    /**
     * Show progress end circle.
     *
     * @param show true if you want to show,false otherwise.
     * @since 0.2.0
     */
    fun setProgressShowEndCircle(show: Boolean) {
        mProgressShowEndCircle = show
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val requiredSize = (2 * mProgressRadius + mProgressBackgroundWidth).toInt()

        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)

        val maxContentSize = widthSize.coerceAtMost(heightSize)

        val width = when {
            widthMode == MeasureSpec.EXACTLY -> widthSize
            requiredSize > widthSize -> maxContentSize
            else -> requiredSize
        }
        val height = when {
            heightMode == MeasureSpec.EXACTLY -> heightSize
            requiredSize > heightSize -> maxContentSize
            requiredSize > heightSize -> maxContentSize
            else -> requiredSize
        }
        setMeasuredDimension(width, height)
    }

    init {
        val typedArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.ArcProgressView,
            defStyleAttr,
            defStyleRes
        )
        mMaximumProgress =
            typedArray.getFloat(R.styleable.ArcProgressView_progress_maximum_value, 0f)
        mCurrentProgress =
            typedArray.getFloat(R.styleable.ArcProgressView_progress_current_value, 0f)
        mTextSize =
            typedArray.getDimension(R.styleable.ArcProgressView_progress_text_size, 0f)
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
        mProgressRadius =
            typedArray.getDimension(R.styleable.ArcProgressView_arc_progress_radius, 0f)
        mProgressBackgroundWidth =
            typedArray.getDimension(R.styleable.ArcProgressView_arc_progress_background_width, 0f)
        mProgressStartColorInt =
            typedArray.getColor(
                R.styleable.ArcProgressView_arc_progress_start_color,
                context.getColor(R.color.md_theme_primary)
            )
        mProgressEndColorInt =
            typedArray.getColor(
                R.styleable.ArcProgressView_arc_progress_end_color,
                context.getColor(R.color.md_theme_primary)
            )
        mProgressShowStartCircle =
            typedArray.getBoolean(R.styleable.ArcProgressView_arc_progress_show_start_circle, true)
        mProgressShowEndCircle =
            typedArray.getBoolean(R.styleable.ArcProgressView_arc_progress_show_end_circle, true)
        typedArray.recycle()
        initPaint()
    }

}