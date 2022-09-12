/*
 * Copyright 2022 VastGui
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gcode.vasttools.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Shader
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.FloatRange
import com.gcode.vasttools.R
import com.gcode.vasttools.utils.ColorUtils
import com.gcode.vasttools.utils.LogUtils
import com.gcode.vasttools.view.interfaces.DownloadCircleViewInterface
import kotlin.math.cos
import kotlin.math.sin

// Author: Vast Gui 
// Email: guihy2019@gmail.com
// Date: 2022/4/17 19:55
// Description: DownloadCircleView is a circular download progress bar.
// Documentation: [DownloadCircleView](https://sakurajimamaii.github.io/VastDocs/document/en/DownloadCircleView.html)

/**
 * DownloadCircleView.
 *
 * Here is an example in xml:
 * ```xml
 * <com.gcode.vasttools.view.DownloadCircleView
 *      android:id="@+id/downloadCv"
 *      android:layout_width="200dp"
 *      android:layout_height="200dp"
 *      app:progress_background_width="20dp"
 *      app:progress_text_size="12sp"/>
 * ```
 *
 * @since 0.0.8
 */
class DownloadCircleView : View,DownloadCircleViewInterface {

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        initAttr(attrs, context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initAttr(attrs, context)
    }

    constructor(context: Context) : super(context) {
        initAttr(null, context)
    }

    private var mProgressBackgroundPaint = Paint()
    private var mProgressPaint = Paint()
    private var mProgressStartPaint = Paint()
    private var mTextCirclePaint = Paint()
    private var mTextPaint = Paint()

    /**
     * The rect containing the progress arc.
     *
     * @since 0.0.8
     */
    private val oval = RectF()

    /**
     * Progress text size.
     *
     * @since 0.0.8
     */
    var progressTextSize = 0f
        private set

    /**
     * Radius of the download circle.
     *
     * @since 0.0.8
     */
    var circleRadius = 0f
        private set

    /**
     * Width of the download circle progress.
     *
     * @since 0.0.8
     */
    var progressBackgroundWidth = 0f
        private set

    /**
     * Download progress.Range from 0.0 to 1.0.
     *
     * @since 0.0.8
     */
    var progress = 0f
        private set

    /**
     * Progress background color int.
     *
     * @since 0.0.8
     */
    var progressBackgroundColorInt: Int = Color.GRAY
        private set

    /**
     * Progress color int.
     *
     * @since 0.0.8
     */
    var progressColorInt: Int = ColorUtils.colorHex2Int("#3B4463")
        private set

    /**
     * Progress shader.
     *
     * @since 0.0.8
     */
    var progressShader: Shader? = null
        private set

    /**
     * Progress start color int.
     *
     * @since 0.0.8
     */
    var progressStartColorInt: Int = ColorUtils.colorHex2Int("#f0932b")
        private set

    /**
     * Progress end color int.
     *
     * @since 0.0.8
     */
    var progressEndColorInt: Int = ColorUtils.colorHex2Int("#f0932b")
        private set

    /**
     * Progress text color int.
     *
     * @since 0.0.8
     */
    var progressTextColorInt: Int = Color.WHITE
        private set

    /**
     * Progress stroke cap.
     *
     * @since 0.0.8
     */
    var progressStrokeCap:Paint.Cap = Paint.Cap.ROUND
        private set

    /**
     * Progress start and end circle.
     *
     * @since 0.0.8
     */
    var progressStartAndEnd:Boolean = true

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val width = if (widthMode == MeasureSpec.EXACTLY) {
            widthSize
        } else {
            (2 * circleRadius + progressBackgroundWidth).toInt()
        }
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val height = if (heightMode == MeasureSpec.EXACTLY) {
            heightSize
        } else {
            (2 * circleRadius + progressBackgroundWidth).toInt()
        }
        setMeasuredDimension(width, height)
    }

    /**
     * Initialize the attributes for the [DownloadCircleView].
     *
     * @since 0.0.8
     */
    private fun initAttr(attrs: AttributeSet?, context: Context) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.DownloadCircleView)
        circleRadius = ta.getDimension(R.styleable.DownloadCircleView_circle_radius, 0f)
        progressBackgroundWidth =
            ta.getDimension(R.styleable.DownloadCircleView_progress_background_width, 0f)
        progressTextSize = ta.getDimension(R.styleable.DownloadCircleView_progress_text_size, 0f)
        progressBackgroundColorInt =
            ta.getColor(R.styleable.DownloadCircleView_progress_background_color, Color.GRAY)
        progressColorInt =
            ta.getColor(R.styleable.DownloadCircleView_progress_color, ColorUtils.colorHex2Int("#3B4463"))
        progressStartColorInt =
            ta.getColor(R.styleable.DownloadCircleView_progress_start_color, ColorUtils.colorHex2Int("#f0932b"))
        progressEndColorInt =
            ta.getColor(R.styleable.DownloadCircleView_progress_end_color, ColorUtils.colorHex2Int("#f0932b"))
        progressTextColorInt =
            ta.getColor(R.styleable.DownloadCircleView_progress_text_color, Color.WHITE)
        LogUtils.i("test", "$circleRadius and $progressBackgroundWidth")
        ta.recycle()
    }

    /**
     * init paints.
     *
     * @since 0.0.8
     */
    private fun initPaint() {
        mProgressBackgroundPaint.apply {
            strokeWidth = progressBackgroundWidth
            color = progressBackgroundColorInt
            isAntiAlias = true
            style = Paint.Style.STROKE
        }
        mProgressPaint.apply {
            if (null == progressShader) {
                color = progressColorInt
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
            textSize = progressTextSize
            color = progressTextColorInt
            isAntiAlias = true
            style = Paint.Style.FILL
        }
    }

    override fun setProgress(@FloatRange(from = 0.0, to = 1.0) progress: Float) {
        this.progress = progress
        invalidate()
    }

    @Throws(RuntimeException::class)
    override fun setProgress(
        @FloatRange(from = 0.0) currentProgress: Float,
        @FloatRange(from = 0.0) totalProgress: Float
    ) {
        if (currentProgress > totalProgress) {
            throw RuntimeException("currentProgress must less then totalProgress!")
        }
        progress = currentProgress / totalProgress
        invalidate()
    }

    override fun resetProgress() {
        progress = 0f
        invalidate()
    }

    override fun setProgressShader(shader: Shader?) {
        progressShader = shader
        invalidate()
    }

    override fun setProgressBackgroundColor(@ColorInt color: Int) {
        progressBackgroundColorInt = color
        invalidate()
    }

    override fun setProgressStartColor(@ColorInt color: Int) {
        progressStartColorInt = color
        invalidate()
    }

    override fun setProgressEndColor(@ColorInt color: Int) {
        progressEndColorInt = color
        invalidate()
    }

    override fun setProgressTextColor(@ColorInt color: Int) {
        progressTextColorInt = color
        invalidate()
    }

    override fun setProgressStartAndEndEnabled(show: Boolean) {
        progressStartAndEnd = show
        invalidate()
    }

    override fun setProgressStrokeCap(cap:Paint.Cap){
        progressStrokeCap = cap
        invalidate()
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
        val range = 360 * progress
        canvas.drawArc(oval, -90f, range, false, mProgressPaint)
        if(progressStartAndEnd){
            // Draw progress start.
            canvas.drawCircle(
                circlePoint,
                progressBackgroundWidth / 2,
                progressBackgroundWidth / 2,
                mProgressStartPaint
            )
            // Draw progress end.
            val radius = circlePoint - progressBackgroundWidth / 2
            val x1 = circlePoint - radius * cos((range + 90) * 3.14 / 180)
            val y1 = circlePoint - radius * sin((range + 90) * 3.14 / 180)
            canvas.drawCircle(x1.toFloat(), y1.toFloat(), progressBackgroundWidth / 2, mTextCirclePaint)
        }
        val radius = circlePoint - progressBackgroundWidth / 2
        val x1 = circlePoint - radius * cos((range + 90) * 3.14 / 180)
        val y1 = circlePoint - radius * sin((range + 90) * 3.14 / 180)
        val txt = "${progress * 100}%"
        val stringWidth = mTextPaint.measureText(txt)
        canvas.drawText(
            txt,
            x1.toFloat() - stringWidth / 2,
            y1.toFloat() + progressTextSize / 2,
            mTextPaint
        )
    }
}