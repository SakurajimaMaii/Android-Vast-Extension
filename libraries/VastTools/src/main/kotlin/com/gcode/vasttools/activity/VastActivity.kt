/*
 * Copyright 2022 VastGui
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gcode.vasttools.activity

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModel
import com.google.android.material.snackbar.Snackbar

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/3/10 16:20
// Description: BaseVastActivity.

/**
 * The parent class for [VastVmActivity] , [VastVbActivity] ,
 * [VastVbVmActivity].
 *
 * @since 0.0.9
 */
sealed class VastActivity : AppCompatActivity() {

    /**
     * True if you want to show the ActionBar,false otherwise,
     *
     * ```kotlin
     * override fun onCreate(savedInstanceState: Bundle?) {
     *      enableActionBar = false
     *      super.onCreate(savedInstanceState)
     *      ... // Do something
     * }
     * ```
     *
     * @since 0.0.6
     */
    protected var enableActionBar = true

    /**
     * True if you want to set fullscreen,false otherwise.
     *
     * If you set [enableFullScreen] to true,the ActionBar
     * will not be shown.
     *
     * ```kotlin
     * override fun onCreate(savedInstanceState: Bundle?) {
     *      enableFullScreen = true
     *      super.onCreate(savedInstanceState)
     *      ... // Do something
     * }
     * ```
     *
     * @since 0.0.6
     */
    protected var enableFullScreen = false

    /**
     * The [Context] of the activity.
     *
     * @since 0.0.8
     */
    protected lateinit var mContext: Context

    /**
     * Default tag for log.
     *
     * The value of [defaultTag] will be the class name that extends
     * [VastVbActivity] , [VastVmActivity] or [VastVbVmActivity].
     *
     * @since 0.0.9
     */
    protected val defaultTag: String
        get() = this.javaClass.simpleName

    /**
     * Default [Snackbar] for activity.
     *
     * @since 0.0.9
     */
    protected lateinit var mSnackbar:Snackbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = this
        initWindow()
    }

    /**
     * Return a [ViewModel].
     *
     * If you want to initialization a [ViewModel] with parameters,just do like this:
     * ```kotlin
     * override fun createViewModel(modelClass: Class<out ViewModel>): ViewModel {
     *      return MainSharedVM("MyVM")
     * }
     * ```
     *
     * @param modelClass by default, Activity will get the [ViewModel] by `modelClass.newInstance()`.
     * @return the [ViewModel] of the Activity.
     * @since 0.0.9
     */
    protected abstract fun createViewModel(modelClass: Class<out ViewModel>): ViewModel

    /**
     * initialize activity window.
     *
     * @since 0.0.9
     */
    internal fun initWindow() {
        if (!enableActionBar) {
            supportActionBar?.hide()
        }
        if (enableFullScreen) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
                @Suppress("DEPRECATION")
                val flags = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
                @Suppress("DEPRECATION")
                window.decorView.systemUiVisibility = flags
            } else {
                window.setDecorFitsSystemWindows(false)
                window.insetsController?.apply {
                    hide(WindowInsetsCompat.Type.systemBars())
                    systemBarsBehavior =
                        WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                }
            }
            supportActionBar?.hide()
            // In order to solve the black bar when state bar is gone.
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                val lp: WindowManager.LayoutParams = window.attributes
                lp.layoutInDisplayCutoutMode =
                    WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
                window.attributes = lp
            }
        }
    }

}