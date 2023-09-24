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

import android.os.Bundle
import androidx.viewbinding.ViewBinding
import com.ave.vastgui.core.extension.NotNUllVar
import com.ave.vastgui.tools.viewbinding.reflectViewBinding
import com.google.android.material.snackbar.Snackbar

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/3/10 16:05
// Documentation: https://ave.entropy2020.cn/documents/VastTools/app-entry-points/activities/activity/

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
 */
abstract class VastVbActivity<VB : ViewBinding>() : VastActivity() {

    // Snackbar
    private var mSnackbar by NotNUllVar<Snackbar>()

    // ViewBinding
    private val mBinding: VB by lazy {
        reflectViewBinding(VastVbActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
        mSnackbar = Snackbar.make(mBinding.root, getDefaultTag(), Snackbar.LENGTH_SHORT)
    }

    override fun getBinding(): VB {
        return mBinding
    }

    override fun getSnackbar() = mSnackbar

}