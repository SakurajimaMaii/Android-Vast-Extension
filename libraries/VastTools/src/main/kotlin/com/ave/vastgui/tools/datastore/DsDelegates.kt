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
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/5/29
// Description: 
// Documentation:
// Reference:

/**
 * Save an int value into [DataStore].
 *
 * @since 0.5.1
 */
suspend fun DataStore<Preferences>.saveInt(key: String, value: Int) {
    this.edit { mutablePreferences ->
        mutablePreferences[intPreferencesKey(key)] = value
    }
}

/**
 * Save a double value into [DataStore].
 *
 * @since 0.5.1
 */
suspend fun DataStore<Preferences>.saveDouble(key: String, value: Double) {
    this.edit { mutablePreferences ->
        mutablePreferences[doublePreferencesKey(key)] = value
    }
}

/**
 * Save a string value into [DataStore].
 *
 * @since 0.5.1
 */
suspend fun DataStore<Preferences>.saveString(key: String, value: String) {
    this.edit { mutablePreferences ->
        mutablePreferences[stringPreferencesKey(key)] = value
    }
}

/**
 * Save a boolean value into [DataStore].
 *
 * @since 0.5.1
 */
suspend fun DataStore<Preferences>.saveBoolean(key: String, value: Boolean) {
    this.edit { mutablePreferences ->
        mutablePreferences[booleanPreferencesKey(key)] = value
    }
}

/**
 * Save a boolean value into [DataStore].
 *
 * @since 0.5.1
 */
suspend fun DataStore<Preferences>.saveFloat(key: String, value: Float) {
    this.edit { mutablePreferences ->
        mutablePreferences[floatPreferencesKey(key)] = value
    }
}

/**
 * Save a long value into [DataStore].
 *
 * @since 0.5.1
 */
suspend fun DataStore<Preferences>.saveLong(key: String, value: Long) {
    this.edit { mutablePreferences ->
        mutablePreferences[longPreferencesKey(key)] = value
    }
}

/**
 * Save a string set value into [DataStore].
 *
 * @since 0.5.1
 */
suspend fun DataStore<Preferences>.saveStringSet(key: String, value: Set<String>) {
    this.edit { mutablePreferences ->
        mutablePreferences[stringSetPreferencesKey(key)] = value
    }
}

/**
 * Read an int value from [DataStore].
 *
 * @since 0.5.1
 */
fun DataStore<Preferences>.readIntFlow(key: String, callback: Int? = null): Flow<Int> =
    this.data.catch {
        checkCollectorAction(it, this)
    }.map {
        it[intPreferencesKey(key)] ?: callback
        ?: throw NullPointerException("Can get the value by the key of $key and the callback is null.")
    }

/**
 * Read a double value from [DataStore].
 *
 * @since 0.5.1
 */
fun DataStore<Preferences>.readDoubleFlow(key: String, callback: Double? = null): Flow<Double> =
    this.data.catch {
        checkCollectorAction(it, this)
    }.map {
        it[doublePreferencesKey(key)] ?: callback
        ?: throw NullPointerException("Can get the value by the key of $key and the callback is null.")
    }

/**
 * Read a string value from [DataStore].
 *
 * @since 0.5.1
 */
fun DataStore<Preferences>.readStringFlow(key: String, callback: String? = null): Flow<String> =
    this.data.catch {
        checkCollectorAction(it, this)
    }.map {
        it[stringPreferencesKey(key)] ?: callback
        ?: throw NullPointerException("Can get the value by the key of $key and the callback is null.")
    }

/**
 * Read a boolean value from [DataStore].
 *
 * @since 0.5.1
 */
fun DataStore<Preferences>.readBooleanFlow(key: String, callback: Boolean? = null): Flow<Boolean> =
    this.data.catch {
        checkCollectorAction(it, this)
    }.map {
        it[booleanPreferencesKey(key)] ?: callback
        ?: throw NullPointerException("Can get the value by the key of $key and the callback is null.")
    }

/**
 * Read a float value from [DataStore].
 *
 * @since 0.5.1
 */
fun DataStore<Preferences>.readFloatFlow(key: String, callback: Float? = null): Flow<Float> =
    this.data.catch {
        checkCollectorAction(it, this)
    }.map {
        it[floatPreferencesKey(key)] ?: callback
        ?: throw NullPointerException("Can get the value by the key of $key and the callback is null.")
    }

/**
 * Read a long value from [DataStore].
 *
 * @since 0.5.1
 */
fun DataStore<Preferences>.readLongFlow(key: String, callback: Long? = null): Flow<Long> =
    this.data.catch {
        checkCollectorAction(it, this)
    }.map {
        it[longPreferencesKey(key)] ?: callback
        ?: throw NullPointerException("Can get the value by the key of $key and the callback is null.")
    }

/**
 * Read a long value from [DataStore].
 *
 * @since 0.5.1
 */
fun DataStore<Preferences>.readStringSet(key: String, callback: Set<String>? = null): Flow<Set<String>> =
    this.data.catch {
        checkCollectorAction(it, this)
    }.map {
        it[stringSetPreferencesKey(key)] ?: callback
        ?: throw NullPointerException("Can get the value by the key of $key and the callback is null.")
    }

/**
 * Check collector action
 *
 * @since 0.5.1
 */
private suspend fun checkCollectorAction(
    exception: Throwable,
    collector: FlowCollector<Preferences>
) {
    if (exception is IOException) {
        exception.printStackTrace()
        collector.emit(emptyPreferences())
    } else {
        throw exception
    }
}