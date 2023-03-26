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
import androidx.viewbinding.ViewBinding
import com.ave.vastgui.core.extension.NotNUllVar
import com.ave.vastgui.tools.lifecycle.reflexViewModel
import com.ave.vastgui.tools.viewbinding.reflexViewBinding
import com.google.android.material.snackbar.Snackbar

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/3/10 16:13
// Description: Please make sure that the activity extends VastVbVmActivity when the activity using viewBinding and viewModel.
// Documentation: https://ave.entropy2020.cn/documents/VastTools/app-entry-points/activities/Activity/

/**
 * VastVbVmActivity.
 *
 * ```kotlin
 * class MainActivity : VastVbVmActivity<ActivityMainBinding,MainViewModel>() {
 *     override fun onCreate(savedInstanceState: Bundle?) {
 *          super.onCreate(savedInstanceState)
 *          // Something to do
 *     }
 * }
 * ```
 *
 * @param VB [ViewBinding] of the activity layout.
 * @param VM [ViewModel] of the activity.
 */
abstract class VastVbVmActivity<VB : ViewBinding, VM : ViewModel> : VastActivity() {

    // Snackbar
    private var mSnackbar by NotNUllVar<Snackbar>()

    // ViewBinding
    private val mBinding: VB by lazy {
        reflexViewBinding(this)
    }

    // ViewModel
    private val mViewModel: VM by lazy {
        reflexViewModel(this.javaClass, this) {
            return@reflexViewModel createViewModel(it)
        }
    }

    @SuppressLint("ShowToast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getBinding().root)
        mSnackbar = Snackbar.make(mBinding.root, getDefaultTag(), Snackbar.LENGTH_SHORT)
    }

    override fun getBinding(): VB {
        return mBinding
    }

    override fun getViewModel(): VM {
        return mViewModel
    }

    override fun getSnackbar(): Snackbar {
        return mSnackbar
    }

}