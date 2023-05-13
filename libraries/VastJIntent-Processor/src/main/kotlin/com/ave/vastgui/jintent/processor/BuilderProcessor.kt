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

/*
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

package com.ave.vastgui.jintent.processor

import com.ave.vastgui.jintent.annotation.Builder
import com.ave.vastgui.jintent.annotation.Optional
import com.ave.vastgui.jintent.annotation.Required
import com.ave.vastgui.jintent.processor.template.ActivityClass
import com.ave.vastgui.jintent.processor.template.property.OptionalProperty
import com.ave.vastgui.jintent.processor.template.property.Property
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.validate

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/4/25
// Description:
// Documentation:
// Reference:

/**
 * Builder processor
 *
 * @property supportAnnotations Current support annotations.
 * @since 0.0.1
 */
class BuilderProcessor(
    private val environment: SymbolProcessorEnvironment,
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger
) : SymbolProcessor {

    private val supportAnnotations =
        setOf(Builder::class.java, Required::class.java, Optional::class.java)

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val ret: MutableList<KSAnnotated> = ArrayList()
        val activityClass = HashMap<KSClassDeclaration, ActivityClass>()
        val builderSymbol = resolver
            .getSymbolsWithAnnotation(Builder::class.qualifiedName!!)
        ret.addAll(builderSymbol.filter { !it.validate() }.toList())
        // Get the activity that will be created Builder.
        builderSymbol.filter {
            it is KSClassDeclaration && it.validate()
        }.map {
            it as KSClassDeclaration
        }.toList().forEach { element ->
            activityClass[element] = ActivityClass(element)
        }

        val requiredSymbol = resolver
            .getSymbolsWithAnnotation(Required::class.qualifiedName!!)
        ret.addAll(requiredSymbol.filter { !it.validate() }.toList())
        requiredSymbol.filter {
            it is KSPropertyDeclaration && it.validate()
        }.map {
            it as KSPropertyDeclaration
        }.toList().forEach { element ->
            activityClass[element.parentDeclaration]?.fields?.add(Property(element))
        }

        val optionalSymbol = resolver
            .getSymbolsWithAnnotation(Optional::class.qualifiedName!!)
        ret.addAll(optionalSymbol.filter { !it.validate() }.toList())
        optionalSymbol.filter {
            it is KSPropertyDeclaration && it.validate()
        }.map {
            it as KSPropertyDeclaration
        }.toList().forEach { element ->
            activityClass[element.parentDeclaration]?.fields?.add(OptionalProperty(element))
        }

        activityClass.values.forEach { element ->
            element.jIntent.build(codeGenerator)
        }
        return ret
    }

}