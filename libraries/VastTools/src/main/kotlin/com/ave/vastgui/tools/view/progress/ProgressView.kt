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
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.FloatRange

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/4/3
// Description: 
// Documentation:
// Reference:

/**
 * ProgressView
 *
 * @property mMaximumProgress The maximum progress value.
 * @property mCurrentProgress The current progress value.
 * @property mText The text that will be displayed with progress.
 * @property mTextSize Ths size of the [mText].
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


    var mMaximumProgress: Float = 0.0f
        protected set

    var mCurrentProgress: Float = 0.0f
        protected set

    var mText: String? = null
        protected set

    var mTextSize: Float = 0.0f
        protected set

    var mTextColor: Int = 0
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
        if (currentProgress > mMaximumProgress && mMaximumProgress != 0.0f)
            throw IllegalStateException("The currentProgress should be smaller than $mMaximumProgress")
        mCurrentProgress = currentProgress
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
     * @throws RuntimeException
     * @since 0.2.0
     */
    open fun setText(text: String) {
        if (this is HorizontalProgressView)
            throw RuntimeException("You shouldn't call this method.")
        mText = text
    }

    /**
     * Set text size.
     *
     * @throws RuntimeException
     * @since 0.2.0
     */
    open fun setTextSize(@FloatRange(from = 0.0) size: Float) {
        if (this is HorizontalProgressView)
            throw RuntimeException("You shouldn't call this method.")
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
