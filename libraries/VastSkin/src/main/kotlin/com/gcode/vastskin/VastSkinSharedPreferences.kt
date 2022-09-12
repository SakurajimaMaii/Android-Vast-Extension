/*
 * Copyright 2022 VastGui
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gcode.vastskin

import android.app.Application
import android.content.Context
import android.content.SharedPreferences

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/3/27 19:47
// Description:
// Documentation:

/**
 * [VastSkinSharedPreferences] is used to
 * store the skin path in [SharedPreferences]
 *
 * @since 0.0.1
 */
object VastSkinSharedPreferences {

    /**
     * The [SharedPreferences] of the skin.
     *
     * @since 0.0.1
     */
    private lateinit var skinSharedPreferences:SharedPreferences

    internal fun initSkinSharedPreferences(application: Application){
        skinSharedPreferences = application.getSharedPreferences(THEME_FILE, Context.MODE_PRIVATE)
    }

    /**
     * Remove file path from [skinSharedPreferences].
     *
     * @since 0.0.1
     */
    internal fun reset() {
        skinSharedPreferences.edit().apply {
            remove(THEME_PATH)
            apply()
        }
    }

    /**
     * Get skin file path from [skinSharedPreferences].
     *
     * @since 0.0.1
     */
    internal var skin: String
        get() = skinSharedPreferences.getString(THEME_PATH, null) ?: ""
        set(skinPath) {
            skinSharedPreferences.edit().apply {
                putString(THEME_PATH, skinPath)
                apply()
            }
        }

}