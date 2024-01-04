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

package com.ave.vastgui.netstatelayout.listener

import android.annotation.SuppressLint
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.children
import com.ave.vastgui.netstatelayout.NetState
import com.google.android.material.textview.MaterialTextView

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2024/1/3 23:30

/**
 * @since 1.1.1
 */
@JvmDefaultWithCompatibility
interface OnLoadingErrorListener {
    /**
     * Net state listener for the [NetState.LOADING_ERROR] state.
     *
     * @since 1.1.1
     */
    @SuppressLint("SetTextI18n")
    fun onLoadingErrorInflate(view: View, code: Int?, message: String?) {
        val textView: MaterialTextView =
            (view as ConstraintLayout).children.first() as MaterialTextView
        textView.text = "$code $message"
    }

    /**
     * @since 1.1.1
     */
    fun onLoadingErrorClick() {}
}