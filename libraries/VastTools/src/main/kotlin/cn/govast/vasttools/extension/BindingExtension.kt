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

package cn.govast.vasttools.extension

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.ComponentActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import cn.govast.vasttools.base.BaseFragment
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type


// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/9/7 14:21
// Description: 
// Documentation:

/**
 * Create a [ViewModel] object by [modelClass].
 *
 * @since 0.0.9
 */
private fun createViewModel(modelClass: Class<out ViewModel>): ViewModel {
    return modelClass.newInstance()
}

@JvmOverloads
fun <VB : ViewBinding?> ComponentActivity.reflexViewBinding(from: LayoutInflater = this.layoutInflater): VB =
    reflexViewBinding(this.javaClass, from)

@JvmOverloads
fun <VB : ViewBinding?> Fragment.reflexViewBinding(from: LayoutInflater = this.layoutInflater): VB =
    reflexViewBinding(this.javaClass, from)

/**
 * Reflection gets ViewBinding.
 *
 * @param aClass current class.
 * @param from layouinflater.
 * @param VB implementation class.
 * @return viewBinding instance.
 * @since 0.0.9
 */
private fun <VB : ViewBinding?> reflexViewBinding(
    aClass: Class<*>,
    from: LayoutInflater
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
private fun <VB : ViewBinding?> reflexViewBinding(
    aClass: Class<*>,
    from: LayoutInflater,
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
        reflexViewBinding(aClass.superclass, from, viewGroup, boolean)
    } catch (e: Exception) {
        throw RuntimeException(e.message, e)
    }
}

/**
 * Reflection to get ViewModel.
 *
 * @param createVM create viewModel with parameters.
 * @param VM ViewModel implementation class
 * @return ViewModel instance.
 * @receiver ComponentActivity.
 * @since 0.0.9
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
 * @since 0.0.9
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