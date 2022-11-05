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

package cn.govast.vasttools.extension

import java.lang.reflect.Field

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/7/7
// Description: 
// Documentation:

/**
 * Using the string defined by [value] to replace the enumeration class
 * object to store in the map when using [getKeyAndValue].
 *
 * Note: that if you use the [AutoEnumValue] annotation, when you get
 * the field according to the index, you will get the string instead
 * of the original enumeration class object
 *
 * @see getKeyAndValue
 */
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class AutoEnumValue(val value: String)

/**
 * Setting the [value] as the key of the class field in map when using
 * [getKeyAndValue].
 *
 * @see getKeyAndValue
 */
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class AutoField(val value: String = "")

/**
 * Return object as a map, where key is the declare name by [AutoField] and value is the parameter value.
 * If use [AutoEnumValue],map will use the string value declared by AutoEnumValue as the value of the
 * enumeration field.
 *
 * @see AutoField
 * @see AutoEnumValue
 */
fun Any.getKeyAndValue(): HashMap<String,Any>{
    val handlerType = this::class.java
    val clazz = handlerType.superclass
    val fields = handlerType.declaredFields
    val superfields = clazz.declaredFields
    if (fields.isNotEmpty()) {
        Field.setAccessible(fields, true)
        Field.setAccessible(superfields, true)
        val parts = HashMap<String,Any>()
        for (field in fields) {
            try {
                val autoField = field.getAnnotation(AutoField::class.java)
                if (autoField != null) {
                    val value = field.get(this) ?: continue
                    val key = autoField.value.ifEmpty { field.name }
                    if (value is Enum<*>) {
                        val declaredFields = value.javaClass.declaredFields
                        Field.setAccessible(declaredFields, true)
                        for (enumField in declaredFields) {
                            val autoEnumValue = enumField.getAnnotation(AutoEnumValue::class.java)
                            val enumValue = autoEnumValue?.value ?: enumField.get(value)
                            if (enumValue != null) {
                                parts[key] = enumValue
                                break
                            }
                        }
                    } else {
                        parts[key] = value
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        for (field in superfields) {
            try {
                val autoField = field.getAnnotation(AutoField::class.java)
                if (autoField != null) {
                    val value = field.get(this) ?: continue
                    val key = autoField.value.ifEmpty { field.name }
                    if (value is Enum<*>) {
                        val declaredFields = value.javaClass.declaredFields
                        Field.setAccessible(declaredFields, true)
                        for (enumField in declaredFields) {
                            val autoEnumValue = enumField.getAnnotation(AutoEnumValue::class.java)
                            val enumValue = autoEnumValue?.value ?: enumField.get(value)
                            if (enumValue != null) {
                                parts[key] = enumValue
                                break
                            }
                        }
                    } else {
                        parts[key] = value
                    }
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
fun Any.getDefaultKeyAndValue() = this::class.java
    .declaredFields
    .associate {
        it.isAccessible = true
        it.name to it.get(this)
    }