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

package com.ave.vastgui.tools.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/5/29
// Documentation: https://ave.entropy2020.cn/documents/VastTools/architecture-components/data-layer-libraries/datastore/

/**
 * [IPreferenceProperty].
 *
 * @since 0.5.6
 */
sealed interface IPreferenceProperty<V> {
    /**
     * Return key for an preference with [key].
     *
     * @since 0.5.6
     */
    fun key(key: String): Preferences.Key<V>

    /**
     * Default value of preference property.
     *
     * @since 0.5.6
     */
    fun default(): V?
}

/**
 * [IDataStorePreference].
 *
 * @since 0.5.6
 */
sealed interface IDataStorePreference<V> {
    /**
     * [DataStore].
     *
     * @since 0.5.6
     */
    fun dataStore(): DataStore<Preferences>

    /**
     * Return key for an preference.
     *
     * @since 0.5.6
     */
    fun key(): Preferences.Key<V>

    /**
     * Default value of preference property.
     *
     * @since 0.5.6
     */
    fun default(): V?
}

/**
 * [IDataStoreOwner]. It holds [DataStore] and provides some expansion methods.
 *
 * ```kotlin
 * // Here is a example
 * object ThemeDs: IDataStoreOwner {
 *     override val dataStore: DataStore<Preferences> =
 *         ContextHelper.getAppContext().dataStore
 *
 *     val theme by boolean(false)
 * }
 * ```
 *
 * @since 0.5.6
 */
interface IDataStoreOwner {
    /**
     * @since 0.5.6
     */
    val dataStore: DataStore<Preferences>

    /**
     * @since 0.5.6
     */
    fun int(defaultValue: Int?) = object : PreferenceProperty<Int>() {
        override fun key(key: String) = intPreferencesKey(key)
        override fun default() = defaultValue
    }

    /**
     * @since 0.5.6
     */
    fun double(defaultValue: Double?) = object : PreferenceProperty<Double>() {
        override fun key(key: String) = doublePreferencesKey(key)
        override fun default() = defaultValue
    }

    /**
     * @since 0.5.6
     */
    fun string(defaultValue: String?) = object : PreferenceProperty<String>() {
        override fun key(key: String) = stringPreferencesKey(key)
        override fun default() = defaultValue
    }

    /**
     * @since 0.5.6
     */
    fun boolean(defaultValue: Boolean?) = object : PreferenceProperty<Boolean>() {
        override fun key(key: String) = booleanPreferencesKey(key)
        override fun default() = defaultValue
    }

    /**
     * @since 0.5.6
     */
    fun float(defaultValue: Float?) = object : PreferenceProperty<Float>() {
        override fun key(key: String) = floatPreferencesKey(key)
        override fun default() = defaultValue
    }

    /**
     * @since 0.5.6
     */
    fun long(defaultValue: Long?) = object : PreferenceProperty<Long>() {
        override fun key(key: String) = longPreferencesKey(key)
        override fun default() = defaultValue
    }

    /**
     * @since 0.5.6
     */
    fun stringSet(defaultValue: Set<*>?) = object : PreferenceProperty<Set<String>>() {
        override fun key(key: String) = stringSetPreferencesKey(key)
        override fun default() = defaultValue?.map { it.toString() }?.toSet()
    }
}

/**
 * [PreferenceProperty].
 *
 * @since 0.5.6
 */
abstract class PreferenceProperty<V> internal constructor():
    ReadOnlyProperty<IDataStoreOwner, DataStorePreference<V>>, IPreferenceProperty<V> {
    private var cache: DataStorePreference<V>? = null

    override fun getValue(thisRef: IDataStoreOwner, property: KProperty<*>): DataStorePreference<V> =
        cache ?: object : DataStorePreference<V>() {
            override fun dataStore() = thisRef.dataStore
            override fun key() = this@PreferenceProperty.key(property.name)
            override fun default() = this@PreferenceProperty.default()
        }.also { cache = it }
}

/**
 * [DataStorePreference].
 *
 * @since 0.5.6
 */
abstract class DataStorePreference<V> internal constructor(): IDataStorePreference<V> {
    /**
     * Save value into [dataStore], or remove the preferences from
     * [dataStore] if the [value] is null.
     *
     * @since 0.5.6
     */
    suspend fun set(value: V?): Preferences =
        dataStore().edit { preferences ->
            if (value == null) {
                preferences.remove(key())
            } else {
                preferences[key()] = value
            }
        }

    /**
     * Returns the first element emitted by the [asFlow] and then
     * cancels flow's collection
     *
     * @since 0.5.6
     */
    suspend fun get(): V? = asFlow().first()

    /**
     * @see [DataStore.data]
     * @since 0.5.6
     */
    fun asNotNullFlow(): Flow<V> =
        dataStore().data.map {
            it[key()] ?:
            default() ?:
            throw RuntimeException("The value of ${key().name} is null.")
        }

    /**
     * @see [DataStore.data]
     * @since 0.5.6
     */
    fun asFlow(): Flow<V?> =
        dataStore().data.map { it[key()] ?: default() }
}