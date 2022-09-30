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

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowInsetsCompat
import cn.govast.vasttools.base.BaseActivity
import cn.govast.vasttools.extension.NotNUllVar

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
sealed class VastActivity : AppCompatActivity(), BaseActivity {

    /**
     * True if you want to show the ActionBar,false otherwise,
     *
     * @see enableActionBar
     * @since 0.0.6
     */
    private var mEnableActionBar = true

    /**
     * True if you want to set fullscreen,false otherwise.
     *
     * @see enableFullScreen
     * @since 0.0.6
     */
    private var mEnableFullScreen = false

    /**
     * The [Context] of the activity.
     *
     * @see getContext
     * @since 0.0.8
     */
    private var mContext by NotNUllVar<Context>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = this
    }

    final override fun getContext() = mContext

    final override fun getDefaultTag(): String = this.javaClass.simpleName

    final override fun enableActionBar(enable: Boolean) {
        mEnableActionBar = enable
    }

    final override fun enableFullScreen(enable: Boolean) {
        mEnableFullScreen = enable
    }

    final override fun getBaseActivity() = this

    /**
     * initialize activity window.
     *
     * @since 0.0.9
     */
    internal fun initWindow() {
        if (!mEnableActionBar) {
            supportActionBar?.hide()
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