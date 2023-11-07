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
import com.ave.vastgui.tools.annotation.ExperimentalSp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/3/14
// Documentation: https://ave.entropy2020.cn/documents/VastTools/core-topics/app-data-and-files/save-key-value-data/shared-preferences/

/**
 * Sp Key.
 *
 * @since 0.5.6
 */
@JvmInline
@ExperimentalSp
value class SpKey(val name: String)

/**
 * [SpObservableProperty] provides [StateFlow] to obtain state to update the UI.
 *
 * @since 0.5.6
 */
@ExperimentalSp
abstract class SpObservableProperty<T> : ReadWriteProperty<Any, T> {
    protected abstract val flow: MutableStateFlow<T>

    fun flow(): StateFlow<T> = flow.asStateFlow()
}

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
 *     not exist.
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
 *     not exist.
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
 * Save or set a string value into [SharedPreferences].
 *
 * @param defaultValue The defaultValue you get when
 *     [SharedPreferences.getString] return null.
 * @param key Contains the name of the preference to retrieve.
 * @since 0.5.6
 */
@ExperimentalSp
fun SharedPreferences.string(defaultValue: String = "", key: SpKey) =
    object : SpObservableProperty<String>() {
        override val flow: MutableStateFlow<String> =
            MutableStateFlow(getString(key.name, defaultValue) ?: defaultValue)

        override fun getValue(thisRef: Any, property: KProperty<*>): String {
            return this@string.getString(property.name, defaultValue) ?: defaultValue
        }

        override fun setValue(thisRef: Any, property: KProperty<*>, value: String) {
            this@string.edit().putString(property.name, value).apply()
            flow.update { value }
        }
    }

/**
 * Save or set a string set value into [SharedPreferences].
 *
 * @param defaultValue The defaultValue you get when
 *     [SharedPreferences.stringSet] return null.
 * @param key Contains the name of the preference to retrieve.
 * @since 0.5.6
 */
@ExperimentalSp
fun SharedPreferences.stringSet(defaultValue: Set<String> = setOf(), key: SpKey) =
    object : SpObservableProperty<Set<String>>() {
        override val flow: MutableStateFlow<Set<String>> =
            MutableStateFlow(getStringSet(key.name, defaultValue) ?: defaultValue)

        override fun getValue(thisRef: Any, property: KProperty<*>): Set<String> {
            return this@stringSet.getStringSet(key.name, defaultValue) ?: defaultValue
        }

        override fun setValue(thisRef: Any, property: KProperty<*>, value: Set<String>) {
            this@stringSet.edit().putStringSet(key.name, value).apply()
            flow.update { value }
        }
    }

/**
 * Save or set an int value into [SharedPreferences].
 *
 * @param defaultValue The defaultValue to return if this preference does
 *     not exist.
 * @param key Contains the name of the preference to retrieve.
 * @since 0.5.6
 */
@ExperimentalSp
fun SharedPreferences.int(defaultValue: Int = 0, key: SpKey) =
    object : SpObservableProperty<Int>() {
        override val flow: MutableStateFlow<Int> =
            MutableStateFlow(getInt(key.name, defaultValue))

        override fun getValue(thisRef: Any, property: KProperty<*>): Int {
            return this@int.getInt(key.name, defaultValue)
        }

        override fun setValue(thisRef: Any, property: KProperty<*>, value: Int) {
            this@int.edit().putInt(key.name, value).apply()
            flow.update { value }
        }
    }

/**
 * Save or set a long value into [SharedPreferences].
 *
 * @param defaultValue The defaultValue to return if this preference does
 *     not exist.
 * @param key Contains the name of the preference to retrieve.
 * @since 0.5.6
 */
@ExperimentalSp
fun SharedPreferences.long(defaultValue: Long = 0L, key: SpKey) =
    object : SpObservableProperty<Long>() {
        override val flow: MutableStateFlow<Long> =
            MutableStateFlow(getLong(key.name, defaultValue))

        override fun getValue(thisRef: Any, property: KProperty<*>): Long {
            return this@long.getLong(key.name, defaultValue)
        }

        override fun setValue(thisRef: Any, property: KProperty<*>, value: Long) {
            this@long.edit().putLong(key.name, value).apply()
        }
    }

/**
 * Save or set a float value into [SharedPreferences].
 *
 * @param defaultValue The defaultValue to return if this preference does
 *     not exist.
 * @param key Contains the name of the preference to retrieve.
 * @since 0.5.6
 */
@ExperimentalSp
fun SharedPreferences.float(defaultValue: Float = 0.0f, key: SpKey) =
    object : SpObservableProperty<Float>() {
        override val flow: MutableStateFlow<Float> =
            MutableStateFlow(getFloat(key.name, defaultValue))

        override fun getValue(thisRef: Any, property: KProperty<*>): Float {
            return this@float.getFloat(key.name, defaultValue)
        }

        override fun setValue(thisRef: Any, property: KProperty<*>, value: Float) {
            this@float.edit().putFloat(key.name, value).apply()
        }
    }

/**
 * Save or set a float value into [SharedPreferences].
 *
 * @param defaultValue The defaultValue to return if this preference does
 *     not exist.
 * @param key Contains the name of the preference to retrieve.
 * @since 0.5.6
 */
@ExperimentalSp
fun SharedPreferences.boolean(defaultValue: Boolean = false, key: SpKey) =
    object : SpObservableProperty<Boolean>() {
        override val flow: MutableStateFlow<Boolean> =
            MutableStateFlow(getBoolean(key.name, defaultValue))

        override fun getValue(thisRef: Any, property: KProperty<*>): Boolean {
            return this@boolean.getBoolean(key.name, defaultValue)
        }

        override fun setValue(thisRef: Any, property: KProperty<*>, value: Boolean) {
            this@boolean.edit().putBoolean(key.name, value).apply()
            flow.update { value }
        }
    }

/**
 * Save or set a double value into [SharedPreferences].
 *
 * @param defaultValue The defaultValue to return if this preference does
 *     not exist.
 * @param key Contains the name of the preference to retrieve.
 * @since 0.5.6
 */
@ExperimentalSp
fun SharedPreferences.double(defaultValue: Double = 0.0, key: SpKey) =
    object : SpObservableProperty<Double>() {
        override val flow: MutableStateFlow<Double> =
            MutableStateFlow(
                Double.Companion.fromBits(
                    getLong(
                        key.name, defaultValue.toRawBits()
                    )
                )
            )

        override fun getValue(thisRef: Any, property: KProperty<*>): Double {
            return Double.Companion.fromBits(
                this@double.getLong(
                    key.name, defaultValue.toRawBits()
                )
            )
        }

        override fun setValue(thisRef: Any, property: KProperty<*>, value: Double) {
            this@double.edit().putLong(key.name, value.toRawBits()).apply()
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