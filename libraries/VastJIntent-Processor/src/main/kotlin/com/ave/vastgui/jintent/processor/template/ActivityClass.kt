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

import com.ave.vastgui.jintent.processor.template.ActivityClass.Companion.JINTENT_EXTENSION_NAME
import com.ave.vastgui.jintent.processor.template.property.Property
import com.google.devtools.ksp.isAbstract
import com.google.devtools.ksp.symbol.KSClassDeclaration
import java.util.TreeSet

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/4/25
// Description: 
// Documentation:
// Reference:

/**
 * Contains the information about the [ActivityJIntent] that will be
 * generated from the activity.
 *
 * @property JINTENT_EXTENSION_NAME the extension name of the builder.
 * @property simpleName The simple name of activity.
 * @property packageName The package name of the activity.
 * @since 0.0.1
 */
class ActivityClass(private val ksClassDeclaration: KSClassDeclaration) {

    companion object {
        const val JINTENT_EXTENSION_NAME = "JIntent"
    }

    val simpleName: String
        get() = ksClassDeclaration.simpleName.asString()

    val packageName: String
        get() = ksClassDeclaration.containingFile!!.packageName.asString()

    val jIntentName: String
        get() = "$simpleName$JINTENT_EXTENSION_NAME"

    val jIntent
        get() = ActivityJIntent(this)

    val fields = TreeSet<Property>()

    val isAbstract = ksClassDeclaration.isAbstract()

}