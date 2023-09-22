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
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.FloatRange
import com.ave.vastgui.core.extension.NotNUllVar
import com.ave.vastgui.tools.R

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/4/3

/**
 * ProgressView
 *
 * @property mMaximumProgress The maximum progress value.
 * @property mCurrentProgress The current progress value.
 * @property mText The text that will be displayed with progress.
 * @property mTextSize Ths size of the [mText].
 * @property mTextColor The color of the text.
 * @property mProgressBackgroundColor The background color of the progress.
 * @property mProgressColor The progress color.
 * @since 0.2.0
 */
sealed class ProgressView(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int,
    defStyleRes: Int
) : View(context, attrs, defStyleAttr, defStyleRes) {

    /**
     * Default value of [mMaximumProgress].
     *
     * @since 0.5.3
     */
    protected val mDefaultMaximumProgress
        get() = TypedValue().apply {
            resources.getValue(R.dimen.default_maximum_progress, this, true)
        }.float

    /**
     * Default value of [mCurrentProgress].
     *
     * @since 0.5.3
     */
    protected val mDefaultCurrentProgress
        get() = TypedValue().apply {
            resources.getValue(R.dimen.default_current_progress, this, true)
        }.float

    /**
     * Default value of [mTextSize].
     *
     * @since 0.5.3
     */
    protected val mDefaultTexSize
        get() = resources.getDimension(R.dimen.default_progress_text_size)

    var mMaximumProgress = mDefaultMaximumProgress
        protected set

    var mCurrentProgress: Float = mDefaultCurrentProgress
        protected set

    var mText by NotNUllVar<String>()
        protected set

    var mTextSize by NotNUllVar<Float>()
        protected set

    var mTextColor by NotNUllVar<Int>()
        protected set

    var mProgressBackgroundColor: Int = 0
        protected set

    var mProgressColor: Int = 0
        protected set

    /**
     * Set the maximum progress value.
     *
     * @since 0.2.0
     */
    open fun setMaximumProgress(@FloatRange(from = 0.0) maximumProgress: Float) {
        mMaximumProgress = maximumProgress
    }

    /**
     * Set the current progress value.
     *
     * @throws IllegalStateException
     * @since 0.2.0
     */
    open fun setCurrentProgress(@FloatRange(from = 0.0) currentProgress: Float) {
        mCurrentProgress = currentProgress.coerceIn(0f, mMaximumProgress)
    }

    /**
     * Reset progress.
     *
     * @since 0.2.0
     */
    open fun resetProgress() {
        mCurrentProgress = 0f
    }

    /**
     * Set the text.
     *
     * @since 0.2.0
     */
    open fun setText(text: String) {
        mText = text
    }

    /**
     * Set text size.
     *
     * @since 0.2.0
     */
    open fun setTextSize(@FloatRange(from = 0.0) size: Float) {
        mTextSize = size
    }

    /**
     * Set text color.
     *
     * @since 0.2.0
     */
    open fun setTextColor(@ColorInt color: Int) {
        mTextColor = color
    }

    /**
     * Set the background color of the progress.
     *
     * @since 0.2.0
     */
    open fun setProgressBackgroundColor(@ColorInt color: Int) {
        mProgressBackgroundColor = color
    }

    /**
     * Set the color of the progress.
     *
     * @since 0.2.0
     */
    open fun setProgressColor(@ColorInt color: Int) {
        mProgressColor = color
    }

}
