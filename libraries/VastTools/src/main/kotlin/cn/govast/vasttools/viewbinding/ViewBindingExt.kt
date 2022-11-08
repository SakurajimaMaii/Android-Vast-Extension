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

@file:JvmName("ViewBindingExt")

package cn.govast.vasttools.viewbinding

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.ComponentActivity
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import cn.govast.vasttools.extension.cast
import java.lang.reflect.ParameterizedType

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/11/8
// Description: 
// Documentation:
// Reference:

@JvmOverloads
fun <VB : ViewBinding> ComponentActivity.reflexViewBinding(from: LayoutInflater = this.layoutInflater): VB =
    reflexViewBinding(this.javaClass, from)

@JvmOverloads
fun <VB : ViewBinding> Fragment.reflexViewBinding(from: LayoutInflater = this.layoutInflater): VB =
    reflexViewBinding(this.javaClass, from)

/**
 * Reflection gets ViewBinding.
 *
 * @param aClass current class.
 * @param from [LayoutInflater].
 * @param VB implementation class.
 * @return viewBinding instance.
 */
private fun <VB : ViewBinding> reflexViewBinding(
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
 * @param from [LayoutInflater].
 * @param viewGroup viewGroup.
 * @param boolean attachToRoot.
 * @param VB implementation class.
 * @return viewBinding instance.
 */
private fun <VB : ViewBinding> reflexViewBinding(
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