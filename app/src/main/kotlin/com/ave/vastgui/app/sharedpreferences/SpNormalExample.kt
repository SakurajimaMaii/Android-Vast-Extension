/*
 * Copyright 2021-2024 VastGui
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ave.vastgui.app.sharedpreferences

import com.ave.vastgui.tools.sharedpreferences.ISharedPreferencesOwner
import com.ave.vastgui.tools.sharedpreferences.SpNormal
import com.ave.vastgui.tools.sharedpreferences.float

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/3/14
// Documentation: https://ave.entropy2020.cn/documents/VastTools/core-topics/app-data-and-files/save-key-value-data/sp-normal/

class SpNormalExample(override val name: String) : ISharedPreferencesOwner {

    override val kv by lazy {
        SpNormal(name).getSharedPreferences()
    }

    // declare variables
    var count by float()

}