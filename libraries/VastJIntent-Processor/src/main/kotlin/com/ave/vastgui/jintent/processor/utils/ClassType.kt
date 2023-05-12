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

package com.ave.vastgui.jintent.processor.utils

import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.ClassName as KotlinClassName
import com.squareup.kotlinpoet.TypeName as KotlinTypeName

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/4/30
// Description: 
// Documentation:
// Reference: TieGuanYin

class ClassType(
    private val jvmClassName: String,
    private vararg val typeParameterClassTypes: ClassType
) {

    val kotlin: KotlinTypeName by lazy {
        val className = KotlinClassName.bestGuess(jvmClassName)
        if (typeParameterClassTypes.isNotEmpty()) {
            className.parameterizedBy(*(Array(typeParameterClassTypes.size) { i -> typeParameterClassTypes[i].kotlin }))
        } else {
            className
        }
    }

    fun parameterized(vararg typeParameterClassTypes: ClassType) =
        ClassType(jvmClassName, *typeParameterClassTypes)

    operator fun get(vararg typeParameterClassTypes: ClassType) =
        ClassType(jvmClassName, *typeParameterClassTypes)

    override fun toString(): String {
        return "${jvmClassName}[${typeParameterClassTypes.joinToString()}]"
    }
}