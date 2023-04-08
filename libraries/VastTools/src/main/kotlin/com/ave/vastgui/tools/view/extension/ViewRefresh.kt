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

package com.ave.vastgui.tools.view.extension

import android.view.View

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/4/6
// Documentation: https://ave.entropy2020.cn/documents/VastTools/core-topics/ui/extension/RefreshView/

/**
 * Refresh with [View.invalidate].
 *
 * ```kotlin
 * mBindings.waveProgressView.refreshWithInvalidate {
 *     setHint("Hello")
 *     setCurrentProgress(progress.toFloat())
 * }
 * ```
 *
 * @since 0.2.0
 */
inline fun <reified T : View> T.refreshWithInvalidate(refresh: T.() -> Unit) {
    with(this) {
        this.refresh()
    }
    invalidate()
}

/**
 * Refresh with [View.postInvalidate]
 *
 * ```kotlin
 * mBindings.waveProgressView.refreshWithPostInvalidate {
 *     setHint("Hello")
 *     setCurrentProgress(progress.toFloat())
 * }
 * ```
 *
 * @since 0.2.0
 */
inline fun <reified T : View> T.refreshWithPostInvalidate(refresh: T.() -> Unit) {
    with(this) {
        this.refresh()
    }
    postInvalidate()
}

/**
 * Refresh with [View.requestLayout]
 *
 * ```kotlin
 * mBindings.waveProgressView.refreshWithRequestLayout {
 *     setHint("Hello")
 *     setCurrentProgress(progress.toFloat())
 * }
 * ```
 *
 * @since 0.2.0
 */
inline fun <reified T : View> T.refreshWithRequestLayout(refresh: T.() -> Unit) {
    with(this) {
        this.refresh()
    }
    requestLayout()
}