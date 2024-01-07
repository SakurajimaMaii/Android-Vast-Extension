/*
 * Copyright 2024 VastGui guihy2019@gmail.com
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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.ave.vastgui.core.extension.defaultLogTag
import com.ave.vastgui.tools.activity.widget.screenConfig
import com.ave.vastgui.tools.network.response.ResponseBuilder
import com.ave.vastgui.tools.network.response.getResponseBuilder
import com.google.android.material.snackbar.Snackbar
import com.ave.vastgui.tools.lifecycle.createViewModel as viewModelInstance

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/3/10 16:20
// Documentation: https://ave.entropy2020.cn/documents/VastTools/app-entry-points/activities/activity/

/**
 * The parent class for [VastVmActivity] , [VastVbActivity] ,
 * [VastVbVmActivity].
 */
sealed class VastActivity : AppCompatActivity() {

    /**
     * True if you want to show the ActionBar,false otherwise,
     *
     * @see enableActionBar
     */
    protected var mEnableActionBar = true

    /**
     * True if you want to set fullscreen,false otherwise.
     *
     * @see enableFullScreen
     */
    protected var mEnableFullScreen = false

    protected fun getContext() = this

    protected fun getDefaultTag(): String = this.defaultLogTag()

    /**
     * True if you want to show the ActionBar,false otherwise.
     *
     * ```kotlin
     * override fun onCreate(savedInstanceState: Bundle?) {
     *      super.onCreate(savedInstanceState)
     *      enableActionBar(true)
     *      ... //Other setting
     * }
     * ```
     */
    protected fun enableActionBar(enable: Boolean) {
        mEnableActionBar = enable
        screenConfig(mEnableActionBar, mEnableFullScreen)
    }

    /**
     * True if you want to set fullscreen,false otherwise. If you set
     * [enableFullScreen] to true,the ActionBar will not be shown.
     *
     * ```kotlin
     * override fun onCreate(savedInstanceState: Bundle?) {
     *      super.onCreate(savedInstanceState)
     *      enableFullScreen(true)
     *      ... //Other setting
     * }
     * ```
     */
    protected fun enableFullScreen(enable: Boolean) {
        mEnableFullScreen = enable
        screenConfig(mEnableActionBar, mEnableFullScreen)
    }

    /** Construct a network request builder. */
    @Deprecated(
        level = DeprecationLevel.WARNING,
        message = "Please use Request or Request2 to replace ResponseBuilder.",
        replaceWith = ReplaceWith("")
    )
    protected fun getResponseBuilder(): ResponseBuilder {
        return lifecycleScope.getResponseBuilder()
    }

    /**
     * Get the [ViewBinding]. By default, it will throw a
     * [IllegalStateException].
     *
     * @throws IllegalStateException
     */
    protected open fun getBinding(): ViewBinding {
        throw IllegalStateException("You should not call getBinding().")
    }

    /**
     * Get the [ViewModel]. By default, it will throw a
     * [IllegalStateException].
     *
     * @throws IllegalStateException
     */
    protected open fun getViewModel(): ViewModel {
        throw IllegalStateException("You should not call getViewModel().")
    }

    /** Get default [Snackbar] for activity. */
    protected open fun getSnackbar(): Snackbar {
        throw IllegalStateException("You should not call getSnackbar().")
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
     * @param modelClass by default, Activity or Fragment will get the
     *     [ViewModel] by `modelClass.newInstance()`.
     * @return the [ViewModel] of the Activity or Fragment.
     */
    protected open fun createViewModel(modelClass: Class<out ViewModel>): ViewModel {
        return viewModelInstance(modelClass)
    }

}