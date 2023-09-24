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

package com.ave.vastgui.tools.fragment

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.ave.vastgui.core.extension.defaultLogTag
import com.ave.vastgui.tools.network.response.ResponseBuilder
import com.ave.vastgui.tools.network.response.getResponseBuilder
import com.ave.vastgui.tools.lifecycle.createViewModel as viewModelInstance

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/9/14 17:11
// Documentation: https://ave.entropy2020.cn/documents/VastTools/app-entry-points/fragments/fragment/

abstract class VastFragment : Fragment() {

    protected fun getDefaultTag(): String {
        return defaultLogTag()
    }

    /** Construct a network request builder. */
    @Deprecated(
        level = DeprecationLevel.WARNING,
        message = "Please use Request or Request2 to replace ResponseBuilder.",
        replaceWith = ReplaceWith("")
    )
    protected fun getResponseBuilder(): ResponseBuilder {
        return requireActivity().lifecycleScope.getResponseBuilder()
    }

    /**
     * When [setVmBySelf] is true, it means that ViewModelStoreOwner
     * is Fragment itself. When you want ViewModelStoreOwner to be the
     * [androidx.fragment.app.FragmentActivity] this fragment is currently
     * associated with, set setVmBySelf to false.
     */
    protected open fun setVmBySelf(): Boolean = false

    /**
     * Get the [ViewBinding]. By default, it will throw a
     * [IllegalStateException].
     *
     * @throws IllegalStateException
     */
    protected open fun getBinding(): ViewBinding {
        throw IllegalStateException("You should not call getViewModel().")
    }

    /**
     * Clear the [ViewBinding]. By default, it will throw a
     * [IllegalStateException].
     *
     * @throws IllegalStateException
     */
    protected open fun clearBinding() {
        throw IllegalStateException("You should not call clearBinding().")
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