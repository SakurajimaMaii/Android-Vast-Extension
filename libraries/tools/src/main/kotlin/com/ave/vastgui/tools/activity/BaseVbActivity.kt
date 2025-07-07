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

package com.ave.vastgui.tools.activity

import android.os.Bundle
import androidx.viewbinding.ViewBinding
import com.ave.vastgui.core.extension.NotNUllVar
import com.ave.vastgui.core.extension.defaultLogTag
import com.ave.vastgui.tools.viewbinding.reflectViewBinding
import com.google.android.material.snackbar.Snackbar

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/3/10 16:05
// Documentation: https://sakurajimamaii.github.io/AVE-DOC/documents/tools/app-entry-points/activities/activity/

/**
 * VastVbActivity.
 *
 * If your Activity contains to 0 or more ViewModels, I recommend you use
 * [BaseVbActivity].
 *
 * Here is an example in kotlin:
 * ```kotlin
 * class MainActivity : BaseVbActivity<ActivityMainBinding>() {
 *     override fun onCreate(savedInstanceState: Bundle?) {
 *          super.onCreate(savedInstanceState)
 *          // Something to do
 *     }
 * }
 * ```
 *
 * @param VB [ViewBinding] of the activity layout.
 */
abstract class BaseVbActivity<VB : ViewBinding>() : BaseActivity() {

    /** @since 1.5.3 */
    private var snackbar by NotNUllVar<Snackbar>()

    /** @since 1.5.3 */
    private val binding: VB by lazy {
        reflectViewBinding(BaseVbActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        snackbar = Snackbar.make(binding.root, defaultLogTag(), Snackbar.LENGTH_SHORT)
    }

    override fun getBinding(): VB {
        return binding
    }

    override fun getSnackbar() = snackbar

}