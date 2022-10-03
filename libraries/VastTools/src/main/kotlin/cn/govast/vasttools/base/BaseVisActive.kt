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

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding
import cn.govast.vasttools.activity.VastActivity

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/9/27
// Description: 
// Documentation:
// Reference:

interface BaseVisActive : BaseActive {

    /**
     * @return self or attached [Activity].
     * @since 0.0.9
     */
    fun getBaseActivity(): Activity

    /**
     * Return a [ViewModel].
     *
     * If you want to initialization a [ViewModel] with parameters,just
     * do like this:
     *
     * ```kotlin
     * override fun createViewModel(modelClass: Class<out ViewModel>): ViewModel {
     *      return MainSharedVM("MyVM")
     * }
     * ```
     *
     * @param modelClass by default, [BaseFragment] or [VastActivity] will get the [ViewModel]
     *     by `modelClass.newInstance()`.
     * @return the [ViewModel] of the Fragment.
     * @since 0.0.9
     */
    fun createViewModel(modelClass: Class<out ViewModel>): ViewModel

    /**
     * Get the [ViewBinding].
     *
     * @since 0.0.9
     */
    fun getBinding(): ViewBinding

    /**
     * Get the [ViewModel].
     *
     * @since 0.0.9
     */
    fun getViewModel(): ViewModel

}