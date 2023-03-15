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

import android.content.Context
import android.content.SharedPreferences
import com.ave.vastgui.core.extension.SingletonHolder
import com.ave.vastgui.tools.helper.ContextHelper


// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/3/14
// Description: 
// Documentation:
// Reference:

/**
 * Opens an instance of SharedPreferences.
 *
 * ```kotlin
 * // Declare your own SharedPreferences.
 * class NormalSp(name: String) {
 *
 *     // Get the SharedPreferences instance by SpNormal.getInstance()
 *     private val sp by lazy{
 *         SpNormal.getInstance(name)
 *     }
 *
 *     // Declare variables.
 *     var count by sp.float()
 *
 * }
 *
 * // Use it.
 * private val mSp by lazy { NormalSp(defaultLogTag()) }
 *
 * // Set values.
 * mSp.count = 1f
 * // Get values.
 * val count = mSp.count
 * ```
 *
 * @param name The name of the file to open; can not contain path
 *     separators.
 */
class SpNormal private constructor(name: String) : SpDelegates() {

    private val mSharedPreferences by lazy {
        ContextHelper.getAppContext().getSharedPreferences(name, Context.MODE_PRIVATE)
    }

    override fun getSharedPreferences(): SharedPreferences {
        return mSharedPreferences
    }

    companion object : SingletonHolder<SpNormal, String>(::SpNormal)

}