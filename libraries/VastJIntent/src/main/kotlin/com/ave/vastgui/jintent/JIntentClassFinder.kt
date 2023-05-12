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

package com.ave.vastgui.jintent

import android.app.Activity

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/5/11
// Description: 
// Documentation:
// Reference: TieGuanYin/BuilderClassFinder

/**
 * Builder class finder
 *
 * @since 0.0.1
 */
object JIntentClassFinder {

    private const val JINTENT_EXTENSION_NAME = "JIntent"

    private val builderClassCache = HashMap<String, Class<*>>()

    /**
     * Find the JIntent corresponding to the [activity].
     *
     * @since 0.0.1
     */
    fun findJIntentClass(activity: Activity): Class<*>? {
        val key = activity::class.qualifiedName ?: return null
        return builderClassCache[key] ?: let {
            try{
                val jIntentClass = Class.forName(findBuilderClassName(activity))
                builderClassCache[key] = jIntentClass
                jIntentClass
            } catch (e: ClassNotFoundException){
                null
            }
        }
    }

    private fun findBuilderClassName(activity: Activity): String {
        val cls: Class<*> = activity.javaClass
        var enclosingClass = cls.enclosingClass
        val names = ArrayList<String>()
        names.add(cls.simpleName)
        while (enclosingClass != null) {
            names.add(enclosingClass.simpleName)
            enclosingClass = enclosingClass.enclosingClass
        }
        val stringBuilder = StringBuilder()
        cls.`package`?.name.also { name ->
            stringBuilder.append(name).append(".")
        }
        for (i in names.indices.reversed()) {
            stringBuilder.append(names[i])
            if (i != 0) {
                stringBuilder.append("_")
            }
        }
        stringBuilder.append(JINTENT_EXTENSION_NAME)
        return stringBuilder.toString()
    }
}
