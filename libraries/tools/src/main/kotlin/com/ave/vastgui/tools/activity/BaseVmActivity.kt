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
import androidx.lifecycle.ViewModel
import com.ave.vastgui.core.extension.NotNUllVar
import com.ave.vastgui.core.extension.defaultLogTag
import com.ave.vastgui.tools.lifecycle.reflectViewModel
import com.google.android.material.snackbar.Snackbar

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/3/10 16:14
// Documentation: https://sakurajimamaii.github.io/AVE-DOC/documents/tools/app-entry-points/activities/activity/

/**
 * [BaseVmActivity].
 *
 * ```kotlin
 * class MainActivity : BaseVmActivity<MainViewModel>() {
 *     override fun initView(savedInstanceState: Bundle?) {
 *          super.onCreate(savedInstanceState)
 *          // Something to do
 *     }
 * }
 * ```
 *
 * @param VM [ViewModel] of the activity.
 */
abstract class BaseVmActivity<VM : ViewModel> : BaseActivity() {

    /** @since 1.5.3 */
    private var snackbar by NotNUllVar<Snackbar>()

    /** @since 1.5.3 */
    abstract val layoutId: Int

    /** @since 1.5.3 */
    private val viewModel: VM by lazy {
        reflectViewModel(this.javaClass, this, BaseVmActivity::class.java) {
            return@reflectViewModel createViewModel(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (0 != layoutId) {
            setContentView(layoutId)
        } else {
            throw RuntimeException("Please set correct layout id for the layoutId .")
        }
        snackbar = Snackbar.make(
            this,
            findViewById(android.R.id.content),
            defaultLogTag(),
            Snackbar.LENGTH_SHORT
        )
    }

    override fun getViewModel(): VM {
        return viewModel
    }

    /**
     * Get default [Snackbar] for activity.
     *
     * @since 0.5.7
     */
    override fun getSnackbar(): Snackbar {
        return snackbar
    }

}