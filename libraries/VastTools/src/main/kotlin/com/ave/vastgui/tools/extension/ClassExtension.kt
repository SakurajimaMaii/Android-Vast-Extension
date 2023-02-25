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

@file:JvmName("ClassExtension")

package com.ave.vastgui.tools.extension

import java.lang.reflect.Field
import kotlin.reflect.jvm.kotlinProperty

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/7/7
// Description: 
// Documentation:

/**
 * Using the string defined by [value] to replace the enumeration class
 * object to store in the map when using [convertClassToMap].
 *
 * Note: that if you use the [MapEnumValue] annotation, when you get the
 * field according to the index, you will get the string instead of the
 * original enumeration class object.
 *
 * ```kotlin
 * // map will store "m" to replace Gender.MAN.
 * enum class Gender{
 *      @AutoEnumValue("m") MAN,
 *      @AutoEnumValue("wm") WOMAN
 * }
 * ```
 *
 * @see convertClassToMap
 */
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class MapEnumValue(val value: String)

/**
 * Setting the [value] as the key of the class field in map when using
 * [convertClassToMap].
 *
 * ```kotlin
 * // For example:
 * data class Person(
 *      @AutoField("fn") val firstName: String,
 *      @AutoField("ln") val lastName:String,
 *      @AutoField("g") val gender: Gender)
 * ```
 *
 * @see convertClassToMap
 */
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class MapKey(val value: String = "")

/**
 * Return object as a map, where key is the declare name by [MapKey] and
 * value is the parameter value. If use [MapEnumValue],map will use the
 * string value declared by AutoEnumValue as the value of the enumeration
 * field.
 *
 * @see MapKey
 * @see MapEnumValue
 */
fun Any.convertClassToMap(): HashMap<String, Any> {
    val handlerType = this::class.java
    val clazz = handlerType.superclass
    val fields = handlerType.declaredFields.filter { it.kotlinProperty != null }.toTypedArray()
    val superfields = clazz.declaredFields.filter { it.kotlinProperty != null }.toTypedArray()
    if (fields.isNotEmpty()) {
        Field.setAccessible(fields, true)
        Field.setAccessible(superfields, true)
        val parts = HashMap<String, Any>()
        for (field in fields) {
            try {
                val mapKey = field.getAnnotation(MapKey::class.java)
                val value = field.get(this) ?: continue
                if (mapKey != null) {
                    val key = mapKey.value.ifEmpty { field.name }
                    if (value is Enum<*>) {
                        val declaredFields = value.javaClass.declaredFields
                        Field.setAccessible(declaredFields, true)
                        for (enumField in declaredFields) {
                            val mapEnumValue = enumField.getAnnotation(MapEnumValue::class.java)
                            val enumValue = mapEnumValue?.value ?: enumField.get(value)
                            if (enumValue != null) {
                                parts[key] = enumValue
                                break
                            }
                        }
                    } else {
                        parts[key] = value
                    }
                } else {
                    parts[field.name] = value
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        for (field in superfields) {
            try {
                val mapKey = field.getAnnotation(MapKey::class.java)
                val value = field.get(this) ?: continue
                if (mapKey != null) {
                    val key = mapKey.value.ifEmpty { field.name }
                    if (value is Enum<*>) {
                        val declaredFields = value.javaClass.declaredFields
                        Field.setAccessible(declaredFields, true)
                        for (enumField in declaredFields) {
                            val mapEnumValue = enumField.getAnnotation(MapEnumValue::class.java)
                            val enumValue = mapEnumValue?.value ?: enumField.get(value)
                            if (enumValue != null) {
                                parts[key] = enumValue
                                break
                            }
                        }
                    } else {
                        parts[key] = value
                    }
                } else {
                    parts[field.name] = value
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return parts
    }
    return HashMap()
}

/**
 * Return object as a map, where key is the parameter name and value is the
 * parameter value.
 */
fun Any.convertClassToDefaultMap() = this::class.java
    .declaredFields
    .associate {
        it.isAccessible = true
        it.name to it.get(this)
    }