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

package com.ave.vastgui.tools.activity.widget

import android.os.Build
import android.view.View
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowInsetsCompat

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/3/6
// Documentation: https://ave.entropy2020.cn/documents/VastTools/app-entry-points/activities/extension/#_3

/**
 * @param mEnableActionBar True if you want to show the ActionBar,false otherwise
 * @param mEnableFullScreen True if you want to set fullscreen,false otherwise.
 */
fun AppCompatActivity.screenConfig(
    mEnableActionBar: Boolean,
    mEnableFullScreen: Boolean
) {
    if (!mEnableActionBar) {
        this.supportActionBar?.hide()
    }
    if (mEnableFullScreen) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            @Suppress("DEPRECATION")
            val flags = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
            @Suppress("DEPRECATION")
            this.window.decorView.systemUiVisibility = flags
        } else {
            this.window.setDecorFitsSystemWindows(false)
            this.window.insetsController?.apply {
                hide(WindowInsetsCompat.Type.systemBars())
                systemBarsBehavior =
                    WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        }
        this.supportActionBar?.hide()
        // In order to solve the black bar when state bar is gone.
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
            val lp: WindowManager.LayoutParams = this.window.attributes
            lp.layoutInDisplayCutoutMode =
                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
            this.window.attributes = lp
        }
    }
}