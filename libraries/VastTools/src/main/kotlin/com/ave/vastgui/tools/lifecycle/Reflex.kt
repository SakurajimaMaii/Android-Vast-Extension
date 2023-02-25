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

@file:JvmName("ReflexViewModel")

package com.ave.vastgui.tools.lifecycle

import androidx.activity.ComponentActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ave.vastgui.core.extension.cast
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/11/8
// Description: 
// Documentation:
// Reference:

/**
 * Create a [ViewModel] object by [modelClass].
 */
private fun createViewModel(modelClass: Class<out ViewModel>): ViewModel {
    return modelClass.newInstance()
}

/**
 * Reflection to get ViewModel.
 *
 * @param createVM create viewModel with parameters.
 * @param VM ViewModel implementation class
 * @return ViewModel instance.
 * @receiver ComponentActivity.
 */
@JvmOverloads
fun <VM : ViewModel> ComponentActivity.reflexViewModel(
    createVM: ((vm: Class<out ViewModel>) -> ViewModel) = ::createViewModel
): VM {
    return try {
        val genericSuperclass = javaClass.genericSuperclass
        if (genericSuperclass is ParameterizedType) {
            val actualTypeArguments: Array<Type> =
                genericSuperclass.actualTypeArguments
            for (actualTypeArgument in actualTypeArguments) {
                val aClass1: Class<*> = try {
                    cast(actualTypeArgument)
                } catch (e: Exception) {
                    continue
                }
                if (ViewModel::class.java.isAssignableFrom(aClass1)) {
                    return ViewModelProvider(this, object : ViewModelProvider.Factory {
                        override fun <T : ViewModel> create(modelClass: Class<T>): T {
                            return cast(createVM(modelClass))
                        }
                    })[cast(aClass1)]
                }
            }
        }
        reflexViewModel(createVM)
    } catch (e: Exception) {
        throw RuntimeException(e.message, e)
    }
}

/**
 * Reflection to get ViewModel.
 *
 * @param vmBySelf please refer to [BaseFragment.setVmBySelf].
 * @param createVM create viewModel with parameters.
 * @param VM ViewModel implementation class
 * @return ViewModel instance.
 * @receiver Fragment.
 */
@JvmOverloads
fun <VM : ViewModel> Fragment.reflexViewModel(
    vmBySelf: Boolean = false,
    createVM: ((vm: Class<out ViewModel>) -> ViewModel) = ::createViewModel
): VM {
    return try {
        val genericSuperclass = javaClass.genericSuperclass
        if (genericSuperclass is ParameterizedType) {
            val actualTypeArguments: Array<Type> =
                genericSuperclass.actualTypeArguments
            for (actualTypeArgument in actualTypeArguments) {
                val aClass1: Class<*> = try {
                    cast(actualTypeArgument)
                } catch (e: Exception) {
                    continue
                }
                if (ViewModel::class.java.isAssignableFrom(aClass1)) {
                    return ViewModelProvider(if (vmBySelf) this else requireActivity(),
                        object : ViewModelProvider.Factory {
                            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                                return cast(createVM(modelClass))
                            }
                        })[cast(aClass1)]
                }
            }
        }
        reflexViewModel(vmBySelf, createVM)
    } catch (e: Exception) {
        throw RuntimeException(e.message, e)
    }
}