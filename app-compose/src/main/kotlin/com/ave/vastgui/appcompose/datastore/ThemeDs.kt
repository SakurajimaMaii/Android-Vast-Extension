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

package com.ave.vastgui.appcompose.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.ave.vastgui.tools.content.ContextHelper
import com.ave.vastgui.tools.datastore.IDataStoreOwner

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/11/9

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

object ThemeDs : IDataStoreOwner {
    override val dataStore: DataStore<Preferences> =
        ContextHelper.getAppContext().dataStore

    val isDark by boolean(false)
}