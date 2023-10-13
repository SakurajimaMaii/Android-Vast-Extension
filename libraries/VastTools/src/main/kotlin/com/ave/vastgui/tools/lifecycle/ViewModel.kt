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

package com.ave.vastgui.tools.lifecycle

import android.app.Activity
import android.os.Build
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.ave.vastgui.core.extension.cast
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/3/4
// Documentation: https://ave.entropy2020.cn/documents/VastTools/architecture-components/ui-layer-libraries/lifecycle-aware-components/vm-reflection/

/**
 * Create a [ViewModel] object by [modelClass].
 *
 * Update default constructor instance method since 0.5.3 to adapt to
 * SDK 34.
 */
internal fun createViewModel(modelClass: Class<out ViewModel>): ViewModel {
    return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
        @Suppress("DEPRECATION") modelClass.newInstance()
    } else modelClass.getDeclaredConstructor().newInstance()
}

/**
 * Creates an instance of the view model class to use.
 *
 * @param current The current class, maybe [Activity] or [Fragment].
 * @param store A ViewModelStoreOwner whose ViewModelStore will be used to
 *     retain ViewModels
 * @param base If you provide this parameter, you can effectively reduce
 *     the number of recursions.
 * @param createVM You need to provide this parameter when you need to
 *     create ViewModel instance which have parameters in constructor
 * @param VM The class type of the ViewModel.
 * @since 0.5.2
 */
@JvmOverloads
fun <VM : ViewModel> reflectViewModel(
    current: Class<*>,
    store: ViewModelStoreOwner,
    base: Class<*>? = null,
    createVM: ((Class<out ViewModel>) -> ViewModel) = ::createViewModel
): VM {
    return try {
        if (base?.isAssignableFrom(current) == false) {
            throw RuntimeException("Can't get the ViewModel type.")
        }
        val genericSuperclass = current.genericSuperclass
        if (genericSuperclass is ParameterizedType) {
            val actualTypeArguments: Array<Type> = genericSuperclass.actualTypeArguments
            for (actualTypeArgument in actualTypeArguments) {
                val aClass1: Class<*> = try {
                    cast(actualTypeArgument)
                } catch (e: Exception) {
                    continue
                }
                if (ViewModel::class.java.isAssignableFrom(aClass1)) {
                    return ViewModelProvider(store, object : ViewModelProvider.Factory {
                        override fun <T : ViewModel> create(modelClass: Class<T>): T {
                            return cast(createVM(modelClass))
                        }
                    })[cast(aClass1)]
                }
            }
        }
        reflectViewModel(current.superclass, store, base, createVM)
    } catch (e: Exception) {
        throw RuntimeException(e.message, e)
    }
}