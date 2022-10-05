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

import android.os.Bundle
import androidx.viewbinding.ViewBinding
import cn.govast.vasttools.delegate.ActivityDelegate
import cn.govast.vasttools.extension.NotNUllVar
import cn.govast.vasttools.extension.cast
import cn.govast.vasttools.extension.reflexViewBinding
import com.google.android.material.snackbar.Snackbar

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/3/10 16:05
// Description: Please make sure that the activity extends VastVbActivity when the activity using viewBinding.
// Documentation: [VastBaseActivity](https://sakurajimamaii.github.io/VastDocs/document/en/VastBaseActivity.html)

/**
 * VastVbActivity.
 *
 * If your Activity contains to 0 or more ViewModels, I recommend you use
 * [VastVbActivity].
 *
 * Here is an example in kotlin:
 * ```kotlin
 * class MainActivity : VastVbActivity<ActivityMainBinding>() {
 *     override fun onCreate(savedInstanceState: Bundle?) {
 *          super.onCreate(savedInstanceState)
 *          // Something to do
 *     }
 * }
 * ```
 *
 * @param VB [ViewBinding] of the activity layout.
 * @since 0.0.6
 */
abstract class VastVbActivity<VB : ViewBinding> : VastActivity() {

    // Snackbar
    private var mSnackbar by NotNUllVar<Snackbar>()

    // Binding View
    private var mBinding by NotNUllVar<VB>()

    // Activity Delegate
    private var mActivityDelegate by NotNUllVar<ActivityDelegate>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = reflexViewBinding(javaClass, layoutInflater)
        mActivityDelegate = object : ActivityDelegate(this, mBinding.root) {
            override fun getBinding(): ViewBinding {
                return cast(mBinding)
            }
        }
        setContentView(mBinding.root)
    }

    protected fun getBinding(): VB {
        return cast(mActivityDelegate.getBinding())
    }

    final override fun createActivityDelegate(): ActivityDelegate {
        return mActivityDelegate
    }

}