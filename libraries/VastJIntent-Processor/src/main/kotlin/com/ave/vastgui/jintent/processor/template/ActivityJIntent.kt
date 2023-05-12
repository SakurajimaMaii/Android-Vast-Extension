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

package com.ave.vastgui.jintent.processor.template

import com.ave.vastgui.jintent.processor.template.builder.JIntentBuilder
import com.google.devtools.ksp.processing.CodeGenerator
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.ksp.writeTo

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/4/27
// Description: 
// Documentation:
// Reference:

/**
 * ActivityJIntent
 *
 * @constructor Create empty Activity j intent
 * @property activityClass
 */
class ActivityJIntent(private val activityClass: ActivityClass) {

    companion object {
        const val METHOD_NAME = "start"
    }

    /**
     * Generating the [ActivityJIntent] for the [activityClass].
     *
     * @since 0.0.1
     */
    fun build(codeGenerator: CodeGenerator) {
        if (activityClass.isAbstract) return
        // Generating the file.
        var builder: FileSpec.Builder? = null
        try {
            builder = FileSpec
                .builder(activityClass.packageName, activityClass.jIntentName)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        // Generating the ActivityJIntent class.
        builder?.let {
            JIntentBuilder(activityClass, it).build()
            it.build().writeTo(codeGenerator, false)
        } ?: return
    }

}