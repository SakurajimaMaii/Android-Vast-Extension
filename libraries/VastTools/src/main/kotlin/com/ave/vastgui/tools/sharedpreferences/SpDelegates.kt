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
import androidx.core.content.edit
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

// Author: Vast Gui | SOCK-MAGIC
// Email: guihy2019@gmail.com
// Date: 2023/3/14
// Documentation: https://ave.entropy2020.cn/documents/VastTools/core-topics/app-data-and-files/save-key-value-data/shared-preferences/

/**
 * [ISharedPreferencesOwner].
 *
 * @since 0.5.6
 */
interface ISharedPreferencesOwner {
    val name: String
    val kv: SharedPreferences
    fun clearAllKV() = kv.clearAll()
}

/**
 * [SharedPreferencesProperty].
 *
 * @since 0.5.6
 */
interface SharedPreferencesProperty<V> : ReadWriteProperty<ISharedPreferencesOwner, V>

/**
 * Save or set a string value into [SharedPreferences].
 *
 * @param defaultValue The defaultValue you get when
 *     [SharedPreferences.getString] return null.
 * @since 0.5.6
 */
fun ISharedPreferencesOwner.string(defaultValue: String = "") =
    object : SharedPreferencesProperty<String> {
        override fun getValue(thisRef: ISharedPreferencesOwner, property: KProperty<*>): String {
            return kv.getString(property.name, defaultValue) ?: defaultValue
        }

        override fun setValue(
            thisRef: ISharedPreferencesOwner,
            property: KProperty<*>,
            value: String
        ) {
            kv.edit { putString(property.name, value) }
        }
    }

/**
 * Save or set a string value into [SharedPreferences].
 *
 * @since 0.5.6
 */
fun ISharedPreferencesOwner.string() =
    object : SharedPreferencesProperty<String?> {
        override fun getValue(thisRef: ISharedPreferencesOwner, property: KProperty<*>): String? {
            return kv.getString(property.name, null)
        }

        override fun setValue(
            thisRef: ISharedPreferencesOwner,
            property: KProperty<*>,
            value: String?
        ) {
            kv.edit { putString(property.name, value) }
        }
    }

/**
 * Save or set a string set value into [SharedPreferences].
 *
 * @param defaultValue The defaultValue you get when
 *     [SharedPreferences.stringSet] return null.
 * @since 0.5.6
 */
fun ISharedPreferencesOwner.stringSet(defaultValue: Set<*> = setOf<String>()) =
    object : SharedPreferencesProperty<Set<*>> {
        override fun getValue(thisRef: ISharedPreferencesOwner, property: KProperty<*>): Set<*> {
            return kv.getStringSet(property.name, mutableSetOf()) ?: defaultValue
        }

        override fun setValue(
            thisRef: ISharedPreferencesOwner,
            property: KProperty<*>,
            value: Set<*>
        ) {
            kv.edit { putStringSet(property.name, value.map { it.toString() }.toMutableSet()) }
        }
    }

/**
 * Save or set a string set value into [SharedPreferences].
 *
 * @since 0.5.6
 */
fun ISharedPreferencesOwner.stringSet() =
    object : SharedPreferencesProperty<Set<*>?> {
        override fun getValue(thisRef: ISharedPreferencesOwner, property: KProperty<*>): Set<*>? {
            return kv.getStringSet(property.name, null)
        }

        override fun setValue(
            thisRef: ISharedPreferencesOwner,
            property: KProperty<*>,
            value: Set<*>?
        ) {
            kv.edit { putStringSet(property.name, value?.map { it.toString() }?.toMutableSet()) }
        }
    }

/**
 * Save or set an int value into [SharedPreferences].
 *
 * @param defaultValue The defaultValue to return if this preference does
 *     not exist.
 * @since 0.5.6
 */
fun ISharedPreferencesOwner.int(defaultValue: Int = 0) =
    object : SharedPreferencesProperty<Int> {
        override fun getValue(thisRef: ISharedPreferencesOwner, property: KProperty<*>): Int {
            return kv.getInt(property.name, defaultValue)
        }

        override fun setValue(thisRef: ISharedPreferencesOwner, property: KProperty<*>, value: Int) {
            kv.edit { putInt(property.name, value) }
        }
    }

/**
 * Save or set a long value into [SharedPreferences].
 *
 * @param defaultValue The defaultValue to return if this preference does
 *     not exist
 * @since 0.5.6
 */
fun ISharedPreferencesOwner.long(defaultValue: Long = 0L) =
    object : SharedPreferencesProperty<Long> {
        override fun getValue(thisRef: ISharedPreferencesOwner, property: KProperty<*>): Long {
            return kv.getLong(property.name, defaultValue)
        }

        override fun setValue(thisRef: ISharedPreferencesOwner, property: KProperty<*>, value: Long) {
            kv.edit { putLong(property.name, value) }
        }
    }

/**
 * Save or set a float value into [SharedPreferences].
 *
 * @param defaultValue The defaultValue to return if this preference does
 *     not exist.
 * @since 0.5.6
 */
fun ISharedPreferencesOwner.float(defaultValue: Float = 0.0f) =
    object : SharedPreferencesProperty<Float> {
        override fun getValue(thisRef: ISharedPreferencesOwner, property: KProperty<*>): Float {
            return kv.getFloat(property.name, defaultValue)
        }

        override fun setValue(thisRef: ISharedPreferencesOwner, property: KProperty<*>, value: Float) {
            kv.edit { putFloat(property.name, value) }
        }
    }

/**
 * Save or set a float value into [SharedPreferences].
 *
 * @param defaultValue The defaultValue to return if this preference does
 *     not exist.
 * @since 0.5.6
 */
fun ISharedPreferencesOwner.boolean(defaultValue: Boolean = false) =
    object : SharedPreferencesProperty<Boolean> {
        override fun getValue(thisRef: ISharedPreferencesOwner, property: KProperty<*>): Boolean {
            return kv.getBoolean(property.name, defaultValue)
        }

        override fun setValue(
            thisRef: ISharedPreferencesOwner,
            property: KProperty<*>,
            value: Boolean
        ) {
            kv.edit { putBoolean(property.name, value) }
        }
    }

/**
 * Save or set a double value into [SharedPreferences].
 *
 * @param defaultValue The defaultValue to return if this preference does
 *     not exist.
 * @since 0.5.6
 */
fun ISharedPreferencesOwner.double(defaultValue: Double = 0.0) =
    object : SharedPreferencesProperty<Double> {
        override fun getValue(thisRef: ISharedPreferencesOwner, property: KProperty<*>): Double {
            return Double.Companion.fromBits(
                kv.getLong(property.name, defaultValue.toRawBits())
            )
        }

        override fun setValue(
            thisRef: ISharedPreferencesOwner,
            property: KProperty<*>,
            value: Double
        ) {
            kv.edit { putLong(property.name, value.toRawBits()) }
        }
    }

/**
 * Convert [SharedPreferencesProperty] to [MutableLiveData].
 *
 * ```kotlin
 * object ThemeSp: ISharedPreferencesOwner {
 *      .... // Code
 *      val theme by boolean(false).asLiveData()
 * }
 *
 * // In Activity
 * val darkTheme by ThemeSp.theme.observeAsState()
 * ```
 *
 * @since 0.5.6
 */
fun <V> SharedPreferencesProperty<V>.asLiveData() = object :
    ReadOnlyProperty<ISharedPreferencesOwner, MutableLiveData<V>> {
    private var cache: MutableLiveData<V>? = null

    override fun getValue(
        thisRef: ISharedPreferencesOwner,
        property: KProperty<*>
    ): MutableLiveData<V> =
        cache ?: object : MutableLiveData<V>() {
            override fun getValue() = this@asLiveData.getValue(thisRef, property)

            override fun setValue(value: V) {
                if (super.getValue() == value) return
                this@asLiveData.setValue(thisRef, property, value)
                super.setValue(value)
            }

            override fun onActive() = super.setValue(value)
        }.also { cache = it }
}

/**
 * Convert [SharedPreferencesProperty] to [SharedPreferencesStateFlow].
 *
 * ```kotlin
 * object ThemeSp: ISharedPreferencesOwner {
 *      .... // Code
 *      val theme by boolean(false).asStateFlow()
 * }
 *
 * // In Activity
 * val theme by boolean(false).asStateFlow()
 * ```
 *
 * @since 0.5.6
 */
fun <V> SharedPreferencesProperty<V>.asStateFlow() =
    object : ReadOnlyProperty<ISharedPreferencesOwner, SharedPreferencesStateFlow<V>> {
        private lateinit var cache: SharedPreferencesStateFlow<V>
        override fun getValue(
            thisRef: ISharedPreferencesOwner,
            property: KProperty<*>
        ): SharedPreferencesStateFlow<V> =
            if (::cache.isInitialized)
                cache
            else SharedPreferencesStateFlow(
                MutableStateFlow(this@asStateFlow.getValue(thisRef, property))
            ) {
                this@asStateFlow.setValue(thisRef, property, it)
            }.also { cache = it }
    }

/**
 * [SharedPreferencesStateFlow].
 *
 * @property put Callback when the data in SharedPreferences needs to be updated.
 * @since 0.5.6
 */
class SharedPreferencesStateFlow<V>(
    private val flow: MutableStateFlow<V>,
    private val put: (V) -> Unit
) : MutableStateFlow<V> by flow {

    /**
     * Updates the [MutableStateFlow.value] atomically using the specified
     * [function] of its value, and returns its prior value.
     *
     * [function] may be evaluated multiple times, if [value] is being
     * concurrently updated.
     *
     * [update] will also call [put].
     *
     * @since 0.5.6
     */
    fun getAndUpdate(function: (V) -> V): V {
        while (true) {
            val prevValue = value
            val nextValue = function(prevValue)
            if (compareAndSet(prevValue, nextValue)) {
                put(nextValue)
                return prevValue
            }
        }
    }

    /**
     * Updates the [MutableStateFlow.value] atomically using the
     * specified [function] of its value, and returns the new value.
     *
     * [function] may be evaluated multiple times, if [value] is being
     * concurrently updated.
     *
     * [update] will also call [put].
     *
     * @since 0.5.6
     */
    fun updateAndGet(function: (V) -> V): V {
        while (true) {
            val prevValue = value
            val nextValue = function(prevValue)
            if (compareAndSet(prevValue, nextValue)) {
                put(nextValue)
                return nextValue
            }
        }
    }

    /**
     * Updates the [SharedPreferencesStateFlow.value] atomically
     * using the specified [function] of its value.
     *
     * [function] may be evaluated multiple times, if [value] is
     * being concurrently updated.
     *
     * [update] will also call [put].
     *
     * @since 0.5.6
     */
    fun update(function: (V) -> V) {
        while (true) {
            val prevValue = value
            val nextValue = function(prevValue)
            if (compareAndSet(prevValue, nextValue)) {
                put(nextValue)
                return
            }
        }
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