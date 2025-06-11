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
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import androidx.annotation.ColorInt
import com.ave.vastgui.tools.R
import com.ave.vastgui.tools.utils.dimension
import java.text.DecimalFormat

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/4/3

/**
 * [ProgressView]
 *
 * @since 0.2.0
 */
sealed class ProgressView(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int,
    defStyleRes: Int
) : View(context, attrs, defStyleAttr, defStyleRes) {

    /**
     * Default value of [maximumProgress].
     *
     * @since 1.5.2
     */
    @Suppress("PropertyName")
    protected val DEFAULT_MAXIMUM_PROGRESS = TypedValue()
        .apply { resources.getValue(R.dimen.default_maximum_progress, this, true) }
        .float

    /**
     * Default value of [currentProgress].
     *
     * @since 1.5.2
     */
    @Suppress("PropertyName")
    protected val DEFAULT_CURRENT_PROGRESS = TypedValue()
        .apply { resources.getValue(R.dimen.default_current_progress, this, true) }.float

    /**
     * Default value of [textSize].
     *
     * @since 1.5.2
     */
    @Suppress("PropertyName")
    protected val DEFAULT_TEXT_SIZE = dimension(R.dimen.default_progress_text_size)

    /**
     * Default text.
     *
     * @since 1.5.2
     */
    protected open val defaultText: String
        get() = DecimalFormat("0.00%").format(currentProgress / maximumProgress)

    /**
     * The maximum progress value.
     *
     * @since 1.5.2
     */
    var maximumProgress = DEFAULT_MAXIMUM_PROGRESS
        set(value) {
            field = value.coerceAtLeast(0f)
        }

    /**
     * The current progress value.
     *
     * @since 1.5.2
     */
    var currentProgress: Float = DEFAULT_CURRENT_PROGRESS
        set(value) {
            field = value.coerceIn(0f, maximumProgress)
        }

    /**
     * The text displayed.
     *
     * @since 1.5.2
     */
    open var text: String = ""

    /**
     * The text size of [text] (in pixels).
     *
     * @since 1.5.2
     */
    open var textSize: Float = DEFAULT_TEXT_SIZE

    /**
     * The color-int of [text].
     *
     * @since 1.5.2
     */
    @get:ColorInt
    @setparam:ColorInt
    open var textColor: Int = 0

    /**
     * The color-int of progress background.
     *
     * @since 1.5.2
     */
    @get:ColorInt
    @setparam:ColorInt
    open var progressBackgroundColor: Int = 0

    /**
     * The color-int of progress.
     *
     * @since 1.5.2
     */
    @get:ColorInt
    @setparam:ColorInt
    open var progressColor: Int = 0

    /** @since 0.5.5 */
    protected fun textOrDefault(): String = text.ifEmpty { defaultText }

    /**
     * Reset progress.
     *
     * @since 0.2.0
     */
    fun resetProgress() {
        currentProgress = 0f
    }

}