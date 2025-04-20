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
import com.ave.vastgui.tools.R
import androidx.annotation.LayoutRes
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2025/4/21
// Documentation:

open class FullScreenDialogFragment : DialogFragment {

    constructor() : super()

    constructor(@LayoutRes layoutId: Int) : super(layoutId)

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).also {
            it.window?.let { window ->
                WindowCompat.setDecorFitsSystemWindows(window, false)
                val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
                windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
            }
        }
    }

    override fun getTheme(): Int = R.style.FullScreenDialogFragmentStyle

}