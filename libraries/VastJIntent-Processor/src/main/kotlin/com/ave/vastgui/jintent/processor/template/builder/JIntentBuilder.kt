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

import com.ave.vastgui.jintent.annotation.Generated
import com.ave.vastgui.jintent.processor.template.ActivityClass
import com.ave.vastgui.jintent.processor.template.property.Property
import com.ave.vastgui.jintent.processor.template.types.ACTIVITY
import com.ave.vastgui.jintent.processor.template.types.ACTIVITYRESULTLAUNCHER
import com.ave.vastgui.jintent.processor.template.types.BUNDLE
import com.ave.vastgui.jintent.processor.template.types.BUNDLEUTILS
import com.ave.vastgui.jintent.processor.template.types.CONTEXT
import com.ave.vastgui.jintent.processor.template.types.INTENT
import com.ave.vastgui.jintent.processor.template.types.JINTENTREGISTER
import com.ave.vastgui.jintent.processor.utils.ClassType
import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/5/4
// Description:
// Documentation:
// Reference:

class JIntentBuilder(activityClass: ActivityClass, fb: FileSpec.Builder) :
    BaseBuilder(activityClass, fb) {

    companion object {
        private const val FILL_INTENT_FUNCTION_NAME = "fillIntent"
        private const val INJECT_FUNCTION_NAME = "inject"
        private const val CREATOR_FUNCTION_NAME = "create"
        private const val JUMPER_FUNCTION_NAME = "jump"
        private const val LAUNCHER_FUNCTION_NAME = "launcher"
    }

    private lateinit var jIntent: TypeSpec.Builder
    private lateinit var companionObject: TypeSpec.Builder

    override fun build() {
        // Start jIntent
        jIntentBuilder()
        jIntentParamsBuilder()
        jIntentJumpFuncBuilder()
        jIntentLauncherBuilder()
        jIntentFillIntentFuncBuilder()
        companionBuilder()
        fileBuilder.addType(jIntent.build())
    }

    // -------------------------------------------------- jIntent --------------------------------------------------

    /**
     * Generating the ActivityJIntent with private constructor.
     *
     * @since 0.0.1
     */
    private fun jIntentBuilder() {
        jIntent = TypeSpec
            .classBuilder(activityClass.jIntentName)
            .addAnnotation(AnnotationSpec.builder(Generated::class).build())
            .primaryConstructor(null)
            .addModifiers(KModifier.PRIVATE, KModifier.FINAL)
    }

    /**
     * Params builder for JIntent.
     *
     * @since 0.0.1
     */
    private fun jIntentParamsBuilder() {
        activityClass.fields.forEach { property: Property ->
            jIntent.addProperty(
                PropertySpec.builder(
                    property.name,
                    property.asKotlinTypeName().copy(nullable = true),
                    KModifier.PUBLIC
                ).mutable().initializer("null").build()
            )
        }
    }

    /**
     * Jump func builder for JIntent.
     *
     * @since 0.0.1
     */
    private fun jIntentJumpFuncBuilder() {
        FunSpec.builder(JUMPER_FUNCTION_NAME)
            .addModifiers(KModifier.PUBLIC)
            .addParameter("context", CONTEXT.kotlin)
            .addStatement(
                "val intent = %T(context,%T::class.java)",
                INTENT.kotlin,
                ClassType("${activityClass.packageName}.${activityClass.simpleName}").kotlin
            )
            .addStatement("fillIntent(%L)", "intent")
            .addStatement("%T.startActivity(%L,%L)", JINTENTREGISTER.kotlin, "context", "intent")
            .build().also {
                jIntent.addFunction(it)
            }
    }

    private fun jIntentLauncherBuilder(){
        FunSpec.builder(LAUNCHER_FUNCTION_NAME)
            .addModifiers(KModifier.PUBLIC)
            .addParameter("context", CONTEXT.kotlin)
            .addParameter("launcher", ACTIVITYRESULTLAUNCHER.kotlin)
            .addStatement(
                "val intent = %T(context,%T::class.java)",
                INTENT.kotlin,
                ClassType("${activityClass.packageName}.${activityClass.simpleName}").kotlin
            )
            .addStatement("fillIntent(%L)", "intent")
            .addStatement("launcher.launch(intent)")
            .build().also {
                jIntent.addFunction(it)
            }
    }

    private fun jIntentFillIntentFuncBuilder() {
        FunSpec.builder(FILL_INTENT_FUNCTION_NAME)
            .addModifiers(KModifier.PUBLIC)
            .addParameter(
                ParameterSpec("intent", INTENT.kotlin)
            )
            .apply {
                for (property in requiredFields) {
                    addStatement("intent.putExtra(%S , %L)", property.name, property.name)
                }
            }.build().also {
                jIntent.addFunction(it)
            }
    }

    // -------------------------------------------------- companionObject --------------------------------------------------

    /**
     * JIntent companion object builder.
     *
     * @since 0.0.1
     */
    private fun companionBuilder() {
        companionObject = TypeSpec.companionObjectBuilder()
        activityClass.fields.forEach { property: Property ->
            companionObject.addProperty(
                PropertySpec.builder(
                    property.name, String::class, KModifier.PUBLIC, KModifier.FINAL
                ).initializer("%S", property.name).build()
            )
        }
        createFuncBuilder()
        injectFuncBuilder()
        jIntent.addType(companionObject.build())
    }

    /**
     * JIntent create func builder
     *
     * @since 0.0.1
     */
    private fun createFuncBuilder() {
        FunSpec.builder(CREATOR_FUNCTION_NAME)
            .addModifiers(KModifier.PUBLIC)
            .addStatement("val jIntent = %L()", activityClass.jIntentName)
            .apply {
                for (property in requiredFields) {
                    val param =
                        ParameterSpec.builder(property.name, property.asKotlinTypeName())
                            .build()
                    this.addParameter(param)
                    addStatement("jIntent.%L = %L", param.name, param.name)
                }
            }
            .addStatement("return jIntent")
            .returns(ClassName(activityClass.packageName, activityClass.jIntentName))
            .build().also {
                companionObject.addFunction(it)
            }
    }

    private fun injectFuncBuilder() {
        FunSpec.builder(INJECT_FUNCTION_NAME)
            .addModifiers(KModifier.PUBLIC)
            .addAnnotation(AnnotationSpec.builder(JvmStatic::class).build())
            .addParameter(ParameterSpec("instance", ACTIVITY.kotlin))
            .addParameter(ParameterSpec("bundle", BUNDLE.kotlin))
            .beginControlFlow("if (instance is %N)", activityClass.simpleName)
            .apply {
                for (property in requiredFields) {
                    addStatement(
                        "%L.%L = %T.get(bundle,%L)",
                        "instance",
                        property.name,
                        BUNDLEUTILS.kotlin,
                        property.name
                    )
                }
            }
            .endControlFlow()
            .build().also {
                companionObject.addFunction(it)
            }
    }

}