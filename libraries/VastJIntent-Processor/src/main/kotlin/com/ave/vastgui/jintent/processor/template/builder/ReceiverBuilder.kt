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

package com.ave.vastgui.jintent.processor.template.builder

import com.ave.vastgui.jintent.processor.template.ActivityClass
import com.ave.vastgui.jintent.processor.template.types.ACTIVITYRESULTLAUNCHER
import com.ave.vastgui.jintent.processor.template.types.CONTEXT
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.ParameterSpec

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/5/13
// Description: 
// Documentation:
// Reference:

class ReceiverBuilder(
    activityClass: ActivityClass,
    fb: FileSpec.Builder
) : BaseBuilder(activityClass, fb) {

    companion object {
        private const val CREATOR_FUNCTION_NAME = "create"
        private const val JUMPER_FUNCTION_NAME = "jump"
        private const val LAUNCHER_FUNCTION_NAME = "launcher"
    }

    override fun build() {
        jumpActivityFuncBuilder()
        resultActivityFuncBuilder()
    }

    private fun jumpActivityFuncBuilder() {
        FunSpec.builder("${JUMPER_FUNCTION_NAME}${activityClass.simpleName}")
            .receiver(CONTEXT.kotlin)
            .addStatement(
                "val jIntent = %L.${CREATOR_FUNCTION_NAME}(%L)",
                activityClass.jIntentName,
                requiredFields.joinToString { it.name }
            )
            .addStatement("jIntent.%L(this)", JUMPER_FUNCTION_NAME)
            .apply {
                for (property in requiredFields) {
                    val param = ParameterSpec
                        .builder(property.name, property.asKotlinTypeName())
                        .build()
                    this.addParameter(param)
                }
            }
            .build().also {
                fileBuilder.addFunction(it)
            }
    }

    private fun resultActivityFuncBuilder() {
        FunSpec.builder("${LAUNCHER_FUNCTION_NAME}${activityClass.simpleName}")
            .receiver(CONTEXT.kotlin)
            .addStatement(
                "val jIntent = %L.${CREATOR_FUNCTION_NAME}(%L)",
                activityClass.jIntentName,
                requiredFields.joinToString { it.name }
            )
            .addStatement("jIntent.%L(this,launcher)", LAUNCHER_FUNCTION_NAME)
            .apply {
                for (property in requiredFields) {
                    val param = ParameterSpec
                        .builder(property.name, property.asKotlinTypeName())
                        .build()
                    this.addParameter(param)
                }
                this.addParameter(
                    ParameterSpec.builder("launcher", ACTIVITYRESULTLAUNCHER.kotlin).build()
                )
            }
            .build().also {
                fileBuilder.addFunction(it)
            }
    }

}