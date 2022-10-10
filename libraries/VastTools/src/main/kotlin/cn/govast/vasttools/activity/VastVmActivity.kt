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

package cn.govast.vasttools.activity

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.lifecycle.ViewModel
import cn.govast.vasttools.activity.delegate.ActivityVmDelegate
import cn.govast.vasttools.extension.NotNUllVar

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
 * @since 0.0.6
 */
abstract class VastVmActivity<VM : ViewModel> : VastActivity() {

    /**
     * The layout resource id for this activity.
     *
     * @since 0.0.6
     */
    abstract val layoutId: Int

    // Activity Delegate
    protected inner class AVD : ActivityVmDelegate<VM>(this, layoutId) {
        override fun createViewModel(modelClass: Class<out ViewModel>): ViewModel {
            return this@VastVmActivity.createViewModel(modelClass)
        }
    }

    private var mActivityDelegate by NotNUllVar<ActivityVmDelegate<VM>>()

    @SuppressLint("ShowToast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivityDelegate = AVD()
        if (0 != layoutId) {
            setContentView(layoutId)
        } else {
            throw RuntimeException("Please set correct layout id for the ${mActivityDelegate.getDefaultTag()} .")
        }
    }

    protected open fun createViewModel(modelClass: Class<out ViewModel>): ViewModel {
        return modelClass.newInstance()
    }

    protected fun getViewModel(): VM {
        return mActivityDelegate.getViewModel()
    }

    final override fun createActivityDelegate(): ActivityVmDelegate<VM> {
        return mActivityDelegate
    }

}