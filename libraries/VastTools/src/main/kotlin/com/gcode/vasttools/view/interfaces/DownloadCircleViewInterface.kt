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

package com.gcode.vasttools.view.interfaces

import android.graphics.Shader
import androidx.annotation.ColorInt
import androidx.annotation.FloatRange
import android.graphics.Paint
import com.gcode.vasttools.view.DownloadCircleView

// Author: Vast Gui 
// Email: guihy2019@gmail.com
// Date: 2022/4/18 20:44
// Description:
// Documentation:

/**
 * Interface for [DownloadCircleView].
 *
 * @since 0.0.8
 */
internal interface DownloadCircleViewInterface {

    /**
     * Set download progress.
     *
     * @param progress currently completed progress,range from 0.0 to 1.0.
     *
     * @since 0.0.8
     */
    fun setProgress(@FloatRange(from = 0.0, to = 1.0) progress: Float)

    /**
     * Set download progress.
     *
     * @param currentProgress currently completed progress.
     * @param totalProgress total progress.
     * @throws RuntimeException when currentProgress is greater than the totalProgress.
     *
     * @since 0.0.8
     */
    fun setProgress(@FloatRange(from = 0.0) currentProgress: Float, @FloatRange(from = 0.0) totalProgress: Float)

    /**
     * Reset progress.
     *
     * @since 0.0.8
     */
    fun resetProgress()

    /**
     * Set progress paint shader.
     *
     * @param shader progress paint shader.
     *
     * @since 0.0.8
     */
    fun setProgressShader(shader: Shader?)

    /**
     * Set progress stroke cap.
     *
     * @param cap progress stroke cap.
     *
     * @since 0.0.8
     */
    fun setProgressStrokeCap(cap:Paint.Cap)

    /**
     * Set progress background color.
     *
     * @param color progress background color.
     *
     * @since 0.0.8
     */
    fun setProgressBackgroundColor(@ColorInt color: Int)

    /**
     * Set progress start color.
     *
     * @param color progress start color.
     *
     * @since 0.0.8
     */
    fun setProgressStartColor(@ColorInt color: Int)

    /**
     * Set progress end color.
     *
     * @param color progress end color.
     *
     * @since 0.0.8
     */
    fun setProgressEndColor(@ColorInt color: Int)

    /**
     * Set progress text color.
     *
     * @param color progress text color.
     *
     * @since 0.0.8
     */
    fun setProgressTextColor(@ColorInt color: Int)

    /**
     * Show progress start and end circle.
     *
     * @param show true if you want to show,false otherwise.
     *
     * @since 0.0.8
     */
    fun setProgressStartAndEndEnabled(show: Boolean)

}