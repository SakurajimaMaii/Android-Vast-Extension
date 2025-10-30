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

import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding
import com.google.android.material.snackbar.Snackbar
import com.ave.vastgui.tools.lifecycle.createViewModel as viewModelInstance

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/3/10 16:20
// Documentation: https://sakurajimamaii.github.io/AVE-DOC/documents/tools/app-entry-points/activities/activity/

/**
 * The parent class for [BaseVmActivity] , [BaseVbActivity] ,
 * [BaseVbVmActivity].
 *
 * @since 1.5.3
 */
sealed class BaseActivity : AppCompatActivity() {

    /** @since 1.5.3 */
    protected var enableActionBar: Boolean = true
        set(value) {
            field = value
            if (field) supportActionBar?.show() else supportActionBar?.hide()
        }

    /**
     * When [enableSystemBar] is `true` , the behavior of system bars is set to
     * [WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE] And
     * hides the status bar and navigation bar, when `false` sets, the behavior
     * of system bars is set to [WindowInsetsControllerCompat.BEHAVIOR_DEFAULT]
     * and displays the status bar and navigation bar.
     *
     * @since 1.5.3
     */
    protected var enableSystemBar: Boolean = false
        set(value) {
            field = value
            with(WindowCompat.getInsetsController(window, window.decorView)) {
                systemBarsBehavior = if (field) WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE else WindowInsetsControllerCompat.BEHAVIOR_DEFAULT
                if (field) hide(WindowInsetsCompat.Type.systemBars()) else show(WindowInsetsCompat.Type.systemBars())
            }
        }

    /** @since 1.5.3 */
    protected open val snackBar: Snackbar
        get() = throw IllegalStateException("You should not call getSnackbar().")

    /**
     * Get the [ViewBinding]. By default, it will throw a
     * [IllegalStateException].
     *
     * @throws IllegalStateException
     * @since 1.5.3
     */
    protected open val binding: ViewBinding
        get() = throw IllegalStateException("You should not call getViewModel().")

    /**
     * Get the [ViewModel]. By default, it will throw a
     * [IllegalStateException].
     *
     * @throws IllegalStateException
     * @since 1.5.3
     */
    protected open val viewModel: ViewModel
        get() = throw IllegalStateException("You should not call getViewModel().")

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
     * @param modelClass by default, Activity or Fragment will get the
     * [ViewModel] by `modelClass.newInstance()`.
     * @return the [ViewModel] of the Activity or Fragment.
     * @since 1.5.3
     */
    protected open fun createViewModel(modelClass: Class<out ViewModel>): ViewModel {
        return viewModelInstance(modelClass)
    }

}