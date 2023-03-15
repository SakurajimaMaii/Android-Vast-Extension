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
// Description: 
// Documentation:
// Reference:

sealed class SpDelegates {

    fun int(defaultValue: Int = 0) = object : ReadWriteProperty<Any, Int> {
        override fun getValue(thisRef: Any, property: KProperty<*>): Int {
            return getSharedPreferences().getInt(property.name, defaultValue)
        }

        override fun setValue(thisRef: Any, property: KProperty<*>, value: Int) {
            getSharedPreferences().edit().putInt(property.name, value).apply()
        }
    }

    fun string(defaultValue: String? = null) = object : ReadWriteProperty<Any, String?> {
        override fun getValue(thisRef: Any, property: KProperty<*>): String? {
            return getSharedPreferences().getString(property.name, defaultValue)
        }

        override fun setValue(thisRef: Any, property: KProperty<*>, value: String?) {
            getSharedPreferences().edit().putString(property.name, value).apply()
        }
    }

    fun long(defaultValue: Long = 0L) = object : ReadWriteProperty<Any, Long> {

        override fun getValue(thisRef: Any, property: KProperty<*>): Long {
            return getSharedPreferences().getLong(property.name, defaultValue)
        }

        override fun setValue(thisRef: Any, property: KProperty<*>, value: Long) {
            getSharedPreferences().edit().putLong(property.name, value).apply()
        }
    }

    fun boolean(defaultValue: Boolean = false) = object : ReadWriteProperty<Any, Boolean> {
        override fun getValue(thisRef: Any, property: KProperty<*>): Boolean {
            return getSharedPreferences().getBoolean(property.name, defaultValue)
        }

        override fun setValue(thisRef: Any, property: KProperty<*>, value: Boolean) {
            getSharedPreferences().edit().putBoolean(property.name, value).apply()
        }
    }

    fun float(defaultValue: Float = 0.0f) = object : ReadWriteProperty<Any, Float> {
        override fun getValue(thisRef: Any, property: KProperty<*>): Float {
            return getSharedPreferences().getFloat(property.name, defaultValue)
        }

        override fun setValue(thisRef: Any, property: KProperty<*>, value: Float) {
            getSharedPreferences().edit().putFloat(property.name, value).apply()
        }
    }

    fun stringSet(defaultValue: Set<String>? = null) = object :
        ReadWriteProperty<Any, Set<String>?> {
        override fun getValue(thisRef: Any, property: KProperty<*>): Set<String>? {
            return getSharedPreferences().getStringSet(property.name, defaultValue)
        }

        override fun setValue(thisRef: Any, property: KProperty<*>, value: Set<String>?) {
            getSharedPreferences().edit().putStringSet(property.name, value).apply()
        }
    }

    fun clearAll() {
        getSharedPreferences().edit().clear().apply()
    }

    protected abstract fun getSharedPreferences(): SharedPreferences

}
