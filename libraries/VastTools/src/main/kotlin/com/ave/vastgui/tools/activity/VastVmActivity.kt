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

package com.ave.vastgui.tools.activity

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.lifecycle.ViewModel
import com.ave.vastgui.core.extension.NotNUllVar
import com.ave.vastgui.tools.lifecycle.reflexViewModel
import com.google.android.material.snackbar.Snackbar

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/3/10 16:14
// Description: Please make sure that the activity extends VastVmActivity when the activity using viewModel.
// Documentation: [VastBaseActivity](https://sakurajimamaii.github.io/VastDocs/document/en/VastBaseActivity.html)

/**
 * VastVmActivity.
 *
 * ```kotlin
 * class MainActivity : VastVmActivity<MainViewModel>() {
 *     override fun initView(savedInstanceState: Bundle?) {
 *          super.onCreate(savedInstanceState)
 *          // Something to do
 *     }
 * }
 * ```
 *
 * @param VM [ViewModel] of the activity.
 */
abstract class VastVmActivity<VM : ViewModel> : VastActivity() {

    // Snackbar
    private var mSnackbar by NotNUllVar<Snackbar>()

    // The layout resource id for this activity.
    abstract val layoutId: Int

    // ViewModel
    private val mViewModel: VM by lazy {
        reflexViewModel(this.javaClass, this) {
            return@reflexViewModel createViewModel(it)
        }
    }

    @SuppressLint("ShowToast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (0 != layoutId) {
            setContentView(layoutId)
        } else {
            throw RuntimeException("Please set correct layout id for the layoutId .")
        }
        mSnackbar = Snackbar.make(findViewById(layoutId), getDefaultTag(), Snackbar.LENGTH_SHORT)
    }

    override fun getViewModel(): VM {
        return mViewModel
    }

}