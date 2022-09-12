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

package com.gcode.vasttools.extension

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.ComponentActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.gcode.vasttools.fragment.VastFragment
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type


// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/9/7 14:21
// Description: 
// Documentation:

internal interface CreateViewModel {
    fun createVM(modelClass: Class<out ViewModel>): ViewModel {
        return modelClass.newInstance()
    }
}

/**
 * Reflection gets ViewBinding.
 *
 * @param aClass current class.
 * @param from layouinflater.
 * @param VB implementation class.
 * @return viewBinding instance.
 * @since 0.0.9
 */
internal fun <VB : ViewBinding?> reflexViewBinding(
    aClass: Class<*>,
    from: LayoutInflater?
): VB {
    return try {
        val genericSuperclass = aClass.genericSuperclass
        if (genericSuperclass is ParameterizedType) {
            val actualTypeArguments =
                genericSuperclass.actualTypeArguments
            for (actualTypeArgument in actualTypeArguments) {
                val tClass: Class<*> = try {
                    actualTypeArgument as Class<*>
                } catch (e: Exception) {
                    continue
                }
                if (ViewBinding::class.java.isAssignableFrom(tClass)) {
                    val inflate = tClass.getMethod("inflate", LayoutInflater::class.java)
                    return cast(inflate.invoke(null, from))
                }
            }
        }
        reflexViewBinding(aClass.superclass, from)
    } catch (e: Exception) {
        e.printStackTrace()
        throw RuntimeException(e.message, e)
    }
}

/**
 * Reflection gets ViewBinding.
 *
 * @param aClass current class.
 * @param from layouinflater.
 * @param viewGroup viewGroup.
 * @param boolean attachToRoot.
 * @param VB implementation class.
 * @return viewBinding instance.
 * @since 0.0.9
 */
internal fun <VB : ViewBinding?> reflexViewBinding(
    aClass: Class<*>,
    from: LayoutInflater?,
    viewGroup: ViewGroup,
    boolean: Boolean
): VB {
    return try {
        val genericSuperclass = aClass.genericSuperclass
        if (genericSuperclass is ParameterizedType) {
            val actualTypeArguments =
                genericSuperclass.actualTypeArguments
            for (actualTypeArgument in actualTypeArguments) {
                val tClass: Class<*> = try {
                    actualTypeArgument as Class<*>
                } catch (e: Exception) {
                    continue
                }
                if (ViewBinding::class.java.isAssignableFrom(tClass)) {
                    val inflate = tClass.getMethod(
                        "inflate",
                        LayoutInflater::class.java,
                        ViewGroup::class.java,
                        Boolean::class.java
                    )
                    return cast(inflate.invoke(null, from, viewGroup, boolean))
                }
            }
        }
        reflexViewBinding(aClass.superclass, from)
    } catch (e: Exception) {
        e.printStackTrace()
        throw RuntimeException(e.message, e)
    }
}

/**
 * Reflection to get ViewModel.
 *
 * @param createViewModel create viewModel with parameters.
 * @param VM ViewModel implementation class
 * @return ViewModel instance.
 * @receiver ComponentActivity.
 * @since 0.0.9
 */
internal fun <VM : ViewModel> ComponentActivity.reflexViewModel(
    createViewModel: CreateViewModel
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
                            return cast(createViewModel.createVM(modelClass))
                        }
                    })[cast(aClass1)]
                }
            }
        }
        reflexViewModel(createViewModel)
    } catch (e: Exception) {
        e.printStackTrace()
        throw RuntimeException(e.message, e)
    }
}

/**
 * Reflection to get ViewModel.
 *
 * @param createViewModel create viewModel with parameters.
 * @param vmBySelf please refer to [VastFragment.setVmBySelf].
 * @param VM ViewModel implementation class
 * @return ViewModel instance.
 * @receiver Fragment.
 * @since 0.0.9
 */
@JvmOverloads
internal fun <VM : ViewModel> Fragment.reflexViewModel(
    createViewModel: CreateViewModel,
    vmBySelf: Boolean = false
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
                                return cast(createViewModel.createVM(modelClass))
                            }
                        })[cast(aClass1)]
                }
            }
        }
        reflexViewModel(createViewModel, vmBySelf)
    } catch (e: Exception) {
        e.printStackTrace()
        throw RuntimeException(e.message, e)
    }
}