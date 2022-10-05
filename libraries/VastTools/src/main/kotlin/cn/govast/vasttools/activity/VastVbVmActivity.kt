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
import androidx.viewbinding.ViewBinding
import cn.govast.vasttools.delegate.ActivityDelegate
import cn.govast.vasttools.extension.*

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

    // ViewBinding
    private var mBinding by NotNUllVar<VB>()
    // ViewModel
    private val mViewModel: VM by lazy {
        reflexViewModel(object : CreateViewModel {
            override fun createVM(modelClass: Class<out ViewModel>): ViewModel {
                return createViewModel(modelClass)
            }
        })
    }
    // Activity Delegate
    private var mActivityDelegate by NotNUllVar<ActivityDelegate>()

    @SuppressLint("ShowToast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = reflexViewBinding(javaClass, layoutInflater)
        mActivityDelegate = object : ActivityDelegate(this, mBinding.root) {
            override fun getBinding(): ViewBinding {
                return mBinding
            }

            override fun getViewModel(): ViewModel {
                return mViewModel
            }
        }
        setContentView(mBinding.root)
    }

    /**
     * Return a [ViewModel].
     *
     * If you want to initialization a [ViewModel] with parameters,just do like
     * this:
     * ```kotlin
     * override fun createViewModel(modelClass: Class<out ViewModel>): ViewModel {
     *      return MainSharedVM("MyVM")
     * }
     * ```
     *
     * @param modelClass by default, Activity or Fragment will get
     *     the [ViewModel] by `modelClass.newInstance()`.
     * @return the [ViewModel] of the Fragment.
     */
    protected open fun createViewModel(modelClass: Class<out ViewModel>): ViewModel {
        return modelClass.newInstance()
    }

    protected fun getBinding(): VB {
        return cast(mActivityDelegate.getBinding())
    }

    protected fun getViewModel(): VM {
        return cast(mActivityDelegate.getViewModel())
    }

    final override fun createActivityDelegate(): ActivityDelegate {
        return mActivityDelegate
    }

}