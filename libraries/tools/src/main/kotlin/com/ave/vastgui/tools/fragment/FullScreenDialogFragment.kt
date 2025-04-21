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

package com.ave.vastgui.tools.fragment

import android.app.Dialog
import android.os.Bundle
import androidx.annotation.ColorInt
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.DialogFragment
import com.ave.vastgui.core.annotation.ExperimentalApi
import com.ave.vastgui.tools.R

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2025/4/21
// Documentation:

@ExperimentalApi
open class FullScreenDialogFragment : DialogFragment {

    constructor() : super()

    constructor(@LayoutRes layoutId: Int) : super(layoutId)

    @get:ColorInt
    protected open val statusBarColor: Int
        get() = ContextCompat.getColor(requireContext(), R.color.md_theme_primary)

    protected open val isImmersionBar: Boolean = true

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).also {
            it.window?.let { window ->
                WindowCompat.setDecorFitsSystemWindows(window, !isImmersionBar)
                if (isImmersionBar) {
                    val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
                    windowInsetsController.systemBarsBehavior = WindowInsetsControllerCompat
                        .BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                    windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
                }
                window.statusBarColor = statusBarColor
            }
        }
    }

    override fun getTheme(): Int = R.style.FullScreenDialogFragmentStyle

}