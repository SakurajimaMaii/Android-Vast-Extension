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

package com.ave.vastgui.tools.viewbinding

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.ComponentActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.viewbinding.ViewBinding
import com.ave.vastgui.core.extension.cast
import java.lang.reflect.ParameterizedType
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/3/4
// Description: 
// Documentation:
// Reference:

// ----------------------------------------------- Without base class ------------------------------------------------

/**
 * Creates an instance of the binding class for the activity to use.
 *
 * @param VB ViewBinding class.
 */
inline fun <reified VB : ViewBinding> Activity.reflexViewBinding() = lazy {
    reflexViewBinding<VB>(layoutInflater).apply { setContentView(root) }
}

inline fun <reified VB : ViewBinding> reflexViewBinding(layoutInflater: LayoutInflater) =
    cast<VB>(
        VB::class.java.getMethod("inflate", LayoutInflater::class.java).invoke(null, layoutInflater)
    )

/**
 * Creates an instance of the binding class for the fragment to use.
 *
 * @param VB ViewBinding class.
 */
inline fun <reified VB : ViewBinding> Fragment.reflexViewBinding() =
    FragmentViewBindingDelegate(VB::class.java, this)

class FragmentViewBindingDelegate<VB : ViewBinding>(
    private val bindingClass: Class<VB>,
    val fragment: Fragment,
) : ReadOnlyProperty<Fragment, VB> {
    private var binding: VB? = null

    init {
        fragment.lifecycle.addObserver(object : DefaultLifecycleObserver {
            val viewLifecycleOwnerObserver = Observer<LifecycleOwner?> { owner ->
                if (owner == null) {
                    binding = null
                }
            }

            override fun onCreate(owner: LifecycleOwner) {
                fragment.viewLifecycleOwnerLiveData.observeForever(viewLifecycleOwnerObserver)
            }

            override fun onDestroy(owner: LifecycleOwner) {
                fragment.viewLifecycleOwnerLiveData.removeObserver(viewLifecycleOwnerObserver)
            }
        })
    }

    override fun getValue(thisRef: Fragment, property: KProperty<*>): VB {
        val binding = binding

        if (binding != null && binding.root === thisRef.view) {
            return binding
        }

        return cast(
            bindingClass.getMethod("bind", View::class.java).invoke(null, thisRef.requireView())
        )
    }
}

// ------------------------------------------------- With base class ------------------------------------------------

/**
 * Creates an instance of the binding class for the activity to use.
 *
 * @param VB ViewBinding class.
 */
fun <VB : ViewBinding> reflexViewBinding(activity: ComponentActivity): VB =
    reflexViewBinding(activity.javaClass, activity.layoutInflater)

/**
 * Creates an instance of the binding class for the fragment to use.
 *
 * @param VB ViewBinding class.
 */
fun <VB : ViewBinding> reflexViewBinding(fragment: Fragment, container: ViewGroup?): VB =
    reflexViewBinding(fragment.javaClass, fragment.layoutInflater, container)

/**
 * Creates an instance of the binding class for the fragment to use.
 *
 * @param VB ViewBinding class.
 */
fun <VB : ViewBinding> reflexViewBinding(fragment: Fragment): VB =
    reflexViewBinding(fragment.javaClass, fragment.requireView())

/**
 * Creates an instance of the binding class for the activity to use.
 *
 * @param aClass current class.
 * @param from get from [ComponentActivity.getLayoutInflater].
 * @param VB ViewBinding class.
 * @return an instance of the binding class for the activity to use.
 */
private fun <VB : ViewBinding> reflexViewBinding(
    aClass: Class<*>, from: LayoutInflater
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
 * Creates an instance of the binding class for the fragment to use.
 *
 * @param aClass current class.
 * @param from get from [Fragment.getLayoutInflater].
 * @param VB ViewBinding class.
 * @return an instance of the binding class for the fragment to use.
 */
private fun <VB : ViewBinding> reflexViewBinding(
    aClass: Class<*>,
    from: LayoutInflater,
    container: ViewGroup?
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
                    return cast(inflate.invoke(null, from, container, false))
                }
            }
        }
        reflexViewBinding(aClass.superclass, from, container)
    } catch (e: Exception) {
        throw RuntimeException(e.message, e)
    }
}

/**
 * Creates an instance of the binding class for the fragment to use.
 *
 * @param aClass current class.
 * @param view get from [Fragment.requireView].
 * @param VB ViewBinding class.
 * @return an instance of the binding class for the fragment to use.
 */
private fun <VB : ViewBinding> reflexViewBinding(aClass: Class<*>, view: View): VB {
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
                    val inflate = tClass.getMethod("bind", View::class.java)
                    return cast(inflate.invoke(null, view))
                }
            }
        }
        reflexViewBinding(aClass.superclass, view)
    } catch (e: Exception) {
        throw RuntimeException(e.message, e)
    }
}