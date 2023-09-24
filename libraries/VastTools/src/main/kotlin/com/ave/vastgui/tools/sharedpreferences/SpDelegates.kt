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

package com.ave.vastgui.tools.sharedpreferences

import android.content.SharedPreferences
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/3/14
// Documentation: https://ave.entropy2020.cn/documents/VastTools/core-topics/app-data-and-files/save-key-value-data/shared-preferences/

/**
 * Save or set a string value into [SharedPreferences].
 *
 * @param defaultValue The defaultValue you get when
 *     [SharedPreferences.getString] return null.
 * @since 0.5.1
 */
fun SharedPreferences.string(defaultValue: String? = null) =
    object : ReadWriteProperty<Any, String> {
        override fun getValue(thisRef: Any, property: KProperty<*>): String {
            return this@string.getString(property.name, defaultValue) ?: defaultValue
            ?: throw NullPointerException("SharedPreferences.getString() return null and defaultValue is null.")
        }

        override fun setValue(thisRef: Any, property: KProperty<*>, value: String) {
            this@string.edit().putString(property.name, value).apply()
        }
    }

/**
 * Save or set a string set value into [SharedPreferences].
 *
 * @param defaultValue The defaultValue you get when
 *     [SharedPreferences.stringSet] return null.
 * @since 0.5.1
 */
fun SharedPreferences.stringSet(defaultValue: Set<String>? = null) =
    object : ReadWriteProperty<Any, Set<String>> {
        override fun getValue(thisRef: Any, property: KProperty<*>): Set<String> {
            return this@stringSet.getStringSet(property.name, defaultValue) ?: defaultValue
            ?: throw NullPointerException("SharedPreferences.stringSet() return null and defaultValue is null.")
        }

        override fun setValue(thisRef: Any, property: KProperty<*>, value: Set<String>) {
            this@stringSet.edit().putStringSet(property.name, value).apply()
        }
    }

/**
 * Save or set an int value into [SharedPreferences].
 *
 * @param defaultValue The defaultValue to return if this preference does
 *     not exist
 * @since 0.5.1
 */
fun SharedPreferences.int(defaultValue: Int = 0) = object : ReadWriteProperty<Any, Int> {
    override fun getValue(thisRef: Any, property: KProperty<*>): Int {
        return this@int.getInt(property.name, defaultValue)
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Int) {
        this@int.edit().putInt(property.name, value).apply()
    }
}

/**
 * Save or set a long value into [SharedPreferences].
 *
 * @param defaultValue The defaultValue to return if this preference does
 *     not exist
 * @since 0.5.1
 */
fun SharedPreferences.long(defaultValue: Long = 0L) = object : ReadWriteProperty<Any, Long> {
    override fun getValue(thisRef: Any, property: KProperty<*>): Long {
        return this@long.getLong(property.name, defaultValue)
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Long) {
        this@long.edit().putLong(property.name, value).apply()
    }
}

/**
 * Save or set a float value into [SharedPreferences].
 *
 * @param defaultValue The defaultValue to return if this preference does
 *     not exist
 * @since 0.5.1
 */
fun SharedPreferences.float(defaultValue: Float = 0.0f) = object : ReadWriteProperty<Any, Float> {
    override fun getValue(thisRef: Any, property: KProperty<*>): Float {
        return this@float.getFloat(property.name, defaultValue)
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Float) {
        this@float.edit().putFloat(property.name, value).apply()
    }
}

/**
 * Save or set a float value into [SharedPreferences].
 *
 * @param defaultValue The defaultValue to return if this preference does
 *     not exist.
 * @since 0.5.1
 */
fun SharedPreferences.boolean(defaultValue: Boolean = false) =
    object : ReadWriteProperty<Any, Boolean> {
        override fun getValue(thisRef: Any, property: KProperty<*>): Boolean {
            return this@boolean.getBoolean(property.name, defaultValue)
        }

        override fun setValue(thisRef: Any, property: KProperty<*>, value: Boolean) {
            this@boolean.edit().putBoolean(property.name, value).apply()
        }
    }

/**
 * Save or set a double value into [SharedPreferences].
 *
 * @param defaultValue The defaultValue to return if this preference does
 *     not exist.
 * @since 0.5.1
 */
fun SharedPreferences.double(defaultValue: Double = 0.0) =
    object : ReadWriteProperty<Any, Double> {
        override fun getValue(thisRef: Any, property: KProperty<*>): Double {
            return Double.Companion.fromBits(
                this@double.getLong(
                    property.name, defaultValue.toRawBits()
                )
            )
        }

        override fun setValue(thisRef: Any, property: KProperty<*>, value: Double) {
            this@double.edit().putLong(property.name, value.toRawBits()).apply()
        }
    }

/**
 * Clear all in [SharedPreferences].
 *
 * @since 0.5.1
 */
fun SharedPreferences.clearAll() {
    this.edit().clear().apply()
}