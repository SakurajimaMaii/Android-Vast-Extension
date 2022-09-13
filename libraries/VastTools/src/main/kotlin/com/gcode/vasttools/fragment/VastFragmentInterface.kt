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

package com.gcode.vasttools.fragment

import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding


// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/9/13 7:18
// Description: 
// Documentation:

interface VastFragmentInterface {

    companion object{
        const val permission_request_code = 0x01
    }

    /**
     * Default tag for log.
     *
     * The value of [defaultTag] will be the class name that extends
     * [VastVbFragment] , [VastVmFragment] or [VastVbVmFragment].
     *
     * @since 0.0.9
     */
    val defaultTag: String
        get() = this.javaClass.simpleName

    /**
     * When [setVmBySelf] is true, the ViewModel representing the Fragment
     * is retained by itself. When you want the ViewModel to be retained
     * by its associated Activity, please set [setVmBySelf] to false.
     *
     * @since 0.0.9
     */
    fun setVmBySelf(): Boolean

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
     * @param modelClass by default, Fragment will get the [ViewModel]
     *     by `modelClass.newInstance()`.
     * @return the [ViewModel] of the Fragment.
     * @since 0.0.9
     */
    fun createViewModel(modelClass: Class<out ViewModel>): ViewModel

    /**
     * Get the activity [ViewBinding].
     *
     * @since 0.0.9
     */
    fun getBinding(): ViewBinding

    /**
     * Get the activity [ViewModel].
     *
     * @since 0.0.9
     */
    fun getViewModel(): ViewModel

}