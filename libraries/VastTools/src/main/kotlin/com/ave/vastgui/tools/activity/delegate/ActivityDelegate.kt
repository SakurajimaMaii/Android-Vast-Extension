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

package com.ave.vastgui.tools.activity.delegate

import android.content.Context
import android.os.Build
import android.view.View
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowInsetsCompat
import com.ave.vastgui.core.extension.cast
import com.ave.vastgui.tools.activity.base.BaseActivity

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/10/5
// Description: 
// Documentation:
// Reference:

sealed class ActivityDelegate(
    protected val activity: ComponentActivity,
) : BaseActivity {

    /**
     * True if you want to show the ActionBar,false otherwise,
     *
     * @see enableActionBar
     */
    private var mEnableActionBar = true

    /**
     * True if you want to set fullscreen,false otherwise.
     *
     * @see enableFullScreen
     */
    private var mEnableFullScreen = false

    override fun getContext(): Context {
        return activity
    }

    override fun enableActionBar(enable: Boolean) {
        mEnableActionBar = enable
        initWindow()
    }

    override fun enableFullScreen(enable: Boolean) {
        mEnableFullScreen = enable
        initWindow()
    }

    override fun getBaseActivity() = activity

    override fun getDefaultTag(): String {
        return activity::class.java.simpleName
    }

    private fun initWindow() {
        if (!mEnableActionBar && activity is AppCompatActivity) {
            cast<AppCompatActivity>(activity).supportActionBar?.hide()
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
                activity.window.decorView.systemUiVisibility = flags
            } else {
                activity.window.setDecorFitsSystemWindows(false)
                activity.window.insetsController?.apply {
                    hide(WindowInsetsCompat.Type.systemBars())
                    systemBarsBehavior =
                        WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                }
            }
            if (activity is AppCompatActivity) {
                cast<AppCompatActivity>(activity).supportActionBar?.hide()
            }
            // In order to solve the black bar when state bar is gone.
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                val lp: WindowManager.LayoutParams = activity.window.attributes
                lp.layoutInDisplayCutoutMode =
                    WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
                activity.window.attributes = lp
            }
        }
    }

}