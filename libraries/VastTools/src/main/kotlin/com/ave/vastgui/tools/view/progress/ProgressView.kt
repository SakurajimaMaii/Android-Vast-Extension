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
import com.ave.vastgui.tools.R
import java.text.DecimalFormat

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

    /**
     * Default text.
     *
     * @since 0.5.5
     */
    protected open val mDefaultText
        get() = DecimalFormat("0.00%").format(mCurrentProgress / mMaximumProgress)

    var mMaximumProgress = mDefaultMaximumProgress
        set(value) {
            field = value.coerceAtLeast(0f)
        }

    var mCurrentProgress: Float = mDefaultCurrentProgress
        set(value) {
            field = value.coerceIn(0f, mMaximumProgress)
        }

    open var mText: String = ""

    open var mTextSize: Float = mDefaultTexSize

    open var mTextColor: Int = 0

    open var mProgressBackgroundColor: Int = 0

    open var mProgressColor: Int = 0

    /** @since 0.5.5 */
    protected fun textOrDefault(): String = mText.ifEmpty { mDefaultText }

    /**
     * Reset progress.
     *
     * @since 0.2.0
     */
    fun resetProgress() {
        mCurrentProgress = 0f
    }

}