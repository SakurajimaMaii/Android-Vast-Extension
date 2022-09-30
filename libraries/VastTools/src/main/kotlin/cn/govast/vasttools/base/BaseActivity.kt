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

package cn.govast.vasttools.base

import android.content.Context
import com.google.android.material.snackbar.Snackbar

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/9/14 12:22
// Description: 
// Documentation:

interface BaseActivity : BaseVisActive {

    /**
     * Get the [Context].
     *
     * @since 0.0.9
     */
    fun getContext(): Context

    /**
     * Get default [Snackbar] for activity.
     *
     * @since 0.0.9
     */
    fun getSnackbar(): Snackbar

    /**
     * True if you want to show the ActionBar,false otherwise. Please call the
     * method before super.onCreate.
     *
     * ```kotlin
     * override fun onCreate(savedInstanceState: Bundle?) {
     *      enableActionBar(true)
     *      super.onCreate(savedInstanceState)
     *      ... //Other setting
     * }
     * ```
     *
     * @since 0.0.9
     */
    fun enableActionBar(enable: Boolean)

    /**
     * True if you want to set fullscreen,false otherwise. If you set
     * [enableFullScreen] to true,the ActionBar will not be shown. Please call
     * the method before super.onCreate.
     *
     * ```kotlin
     * override fun onCreate(savedInstanceState: Bundle?) {
     *      enableFullScreen(true)
     *      super.onCreate(savedInstanceState)
     *      ... //Other setting
     * }
     * ```
     *
     * @since 0.0.9
     */
    fun enableFullScreen(enable: Boolean)

}