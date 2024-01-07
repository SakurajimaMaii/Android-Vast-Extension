/*
 * Copyright 2024 VastGui guihy2019@gmail.com
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

import android.content.Context
import android.content.SharedPreferences
import com.ave.vastgui.core.extension.SingletonHolder
import com.ave.vastgui.tools.content.ContextHelper

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/3/14
// Documentation: https://ave.entropy2020.cn/documents/VastTools/core-topics/app-data-and-files/save-key-value-data/sp-normal/

/**
 * Creating an instance of SharedPreferences.
 *
 * ```kotlin
 * val sp = SpNormal.getInstance(name).getSharedPreferences()
 * ```
 *
 * @param name The name of the file to open; can not contain path separators.
 */
class SpNormal private constructor(name: String) {

    private val mSharedPreferences by lazy {
        ContextHelper.getAppContext().getSharedPreferences(name, Context.MODE_PRIVATE)
    }

    fun getSharedPreferences(): SharedPreferences {
        return mSharedPreferences
    }

    companion object : SingletonHolder<SpNormal, String>(::SpNormal)

}