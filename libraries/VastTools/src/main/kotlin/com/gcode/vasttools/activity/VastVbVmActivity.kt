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

package com.gcode.vasttools.activity

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding
import com.gcode.vasttools.extension.CreateViewModel
import com.gcode.vasttools.extension.cast
import com.gcode.vasttools.extension.reflexViewBinding
import com.gcode.vasttools.extension.reflexViewModel
import com.google.android.material.snackbar.Snackbar

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/3/10 16:13
// Description: Please make sure that the activity extends VastVbVmActivity when the activity using viewBinding and viewModel.
// Documentation: [VastBaseActivity](https://sakurajimamaii.github.io/VastDocs/document/en/VastBaseActivity.html)

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
 * @since 0.0.6
 */
abstract class VastVbVmActivity<VB : ViewBinding, VM : ViewModel> : VastActivity() {

    /**
     * Default [Snackbar] for activity.
     *
     * @see getSnackbar
     * @since 0.0.9
     */
    private lateinit var mSnackbar: Snackbar

    private lateinit var mBinding: VB

    private val mViewModel: VM by lazy {
        reflexViewModel(object : CreateViewModel {
            override fun createVM(modelClass: Class<out ViewModel>): ViewModel {
                return createViewModel(modelClass)
            }
        })
    }

    @SuppressLint("ShowToast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = reflexViewBinding(javaClass, layoutInflater)
        setContentView(mBinding.root)
        initWindow()
        mSnackbar = Snackbar.make(mBinding.root, getDefaultTag(), Snackbar.LENGTH_SHORT)
    }

    override fun createViewModel(modelClass: Class<out ViewModel>): ViewModel {
        return modelClass.newInstance()
    }

    final override fun getBinding(): VB {
        return cast(mBinding)
    }

    final override fun getViewModel(): VM {
        return cast(mViewModel)
    }

    final override fun getSnackbar() = mSnackbar

}