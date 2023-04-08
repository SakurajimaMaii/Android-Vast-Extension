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
// Description: DownloadCircleView is a circular download progress bar.
// Documentation: https://ave.entropy2020.cn/documents/VastTools/core-topics/ui/progress/ArcProgressView/

/**
 * DownloadCircleView.
 *
 * Here is an example in xml:
 * ```xml
 * <com.ave.vastgui.tools.view.progress.ArcProgressView
 *      android:id="@+id/downloadCv"
 *      android:layout_width="200dp"
 *      android:layout_height="200dp"
 *      app:progress_background_width="20dp"
 *      app:progress_text_size="12sp"/>
 * ```
 *
 * @property mProgressRadius Radius of the download circle.
 * @property progressBackgroundWidth Width of the download circle progress.
 * @property progressStartColorInt Progress start color int.
 * @property progressEndColorInt Progress end color int.
 * @property progressShader Progress shader.
 * @property progressShowStartCircle Ture if you want to show the start and
 *     end circles, false otherwise.
 * @property progressShowEndCircle Ture if you want to show the start and
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
    private val oval = RectF()
    private var mProgressRadius = 0f
    private var progressBackgroundWidth = 0f
    private var progressShader: Shader? = null
    private var progressStartColorInt: Int = context.getColor(R.color.md_theme_primary)
    private var progressEndColorInt: Int = context.getColor(R.color.md_theme_primary)
    private var progressTextColorInt: Int = context.getColor(R.color.md_theme_onPrimary)
    private var progressStrokeCap: Paint.Cap = Paint.Cap.ROUND
    private var progressShowStartCircle: Boolean = true
    private var progressShowEndCircle: Boolean = true


    /** Initialize the attributes for the DownloadCircleView. */
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
        mText =
            typedArray.getString(R.styleable.ArcProgressView_progress_text)
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
        progressBackgroundWidth =
            typedArray.getDimension(R.styleable.ArcProgressView_arc_progress_background_width, 0f)
        progressStartColorInt =
            typedArray.getColor(
                R.styleable.ArcProgressView_arc_progress_start_color,
                context.getColor(R.color.md_theme_primary)
            )
        progressEndColorInt =
            typedArray.getColor(
                R.styleable.ArcProgressView_arc_progress_end_color,
                context.getColor(R.color.md_theme_primary)
            )
        progressShowStartCircle =
            typedArray.getBoolean(R.styleable.ArcProgressView_arc_progress_show_start_circle, true)
        progressShowEndCircle =
            typedArray.getBoolean(R.styleable.ArcProgressView_arc_progress_show_end_circle, true)
        typedArray.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val width = if (widthMode == MeasureSpec.EXACTLY) {
            widthSize
        } else {
            (2 * mProgressRadius + progressBackgroundWidth).toInt()
        }
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val height = if (heightMode == MeasureSpec.EXACTLY) {
            heightSize
        } else {
            (2 * mProgressRadius + progressBackgroundWidth).toInt()
        }
        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        initPaint()
        // Draw progress background.
        val circlePoint = (width / 2).toFloat()
        canvas.drawCircle(
            circlePoint,
            circlePoint,
            circlePoint - progressBackgroundWidth / 2,
            mProgressBackgroundPaint
        )
        // Draw progress.
        oval.left = progressBackgroundWidth / 2
        oval.top = progressBackgroundWidth / 2
        oval.right = width - progressBackgroundWidth / 2
        oval.bottom = width - progressBackgroundWidth / 2
        val range = 360 * (mCurrentProgress / mMaximumProgress)
        canvas.drawArc(oval, -90f, range, false, mProgressPaint)
        if (progressShowStartCircle) {
            // Draw progress start.
            canvas.drawCircle(
                circlePoint,
                progressBackgroundWidth / 2,
                progressBackgroundWidth / 2,
                mProgressStartPaint
            )
        }
        if (progressShowEndCircle) {
            // Draw progress end.
            val radius = circlePoint - progressBackgroundWidth / 2
            val x1 = circlePoint - radius * cos((range + 90) * 3.14 / 180)
            val y1 = circlePoint - radius * sin((range + 90) * 3.14 / 180)
            canvas.drawCircle(
                x1.toFloat(),
                y1.toFloat(),
                progressBackgroundWidth / 2,
                mTextCirclePaint
            )
        }
        val radius = circlePoint - progressBackgroundWidth / 2
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

    /** init paints. */
    private fun initPaint() {
        mProgressBackgroundPaint.apply {
            strokeWidth = progressBackgroundWidth
            color = mProgressBackgroundColor
            isAntiAlias = true
            style = Paint.Style.STROKE
        }
        mProgressPaint.apply {
            if (null == progressShader) {
                color = mProgressColor
            } else {
                shader = progressShader
            }
            strokeCap = progressStrokeCap
            strokeWidth = progressBackgroundWidth
            isAntiAlias = true
            style = Paint.Style.STROKE
        }
        mProgressStartPaint.apply {
            color = progressStartColorInt
            isAntiAlias = true
            style = Paint.Style.FILL
        }
        mTextCirclePaint.apply {
            color = progressEndColorInt
            isAntiAlias = true
            style = Paint.Style.FILL
        }
        mTextPaint.apply {
            textSize = mTextSize
            color = progressTextColorInt
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
        progressShader = shader
    }

    /**
     * Set progress start color.
     *
     * @param color progress start color.
     */
    fun setProgressStartColor(@ColorInt color: Int) {
        progressStartColorInt = color
    }

    /**
     * Set progress end color.
     *
     * @param color progress end color.
     */
    fun setProgressEndColor(@ColorInt color: Int) {
        progressEndColorInt = color
    }

    /**
     * Show progress start circle.
     *
     * @param show true if you want to show,false otherwise.
     * @since 0.2.0
     */
    fun setProgressShowStartCircle(show: Boolean) {
        progressShowStartCircle = show
    }

    /**
     * Show progress end circle.
     *
     * @param show true if you want to show,false otherwise.
     * @since 0.2.0
     */
    fun setProgressShowEndCircle(show: Boolean) {
        progressShowEndCircle = show
    }

}