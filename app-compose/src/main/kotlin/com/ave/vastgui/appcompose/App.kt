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

package com.ave.vastgui.appcompose

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.ave.vastgui.tools.config.ToolsConfig

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/5/11
// Documentation: https://ave.entropy2020.cn/documents/VastTools/init/tools-config/

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        ToolsConfig.init(this)
    }

}