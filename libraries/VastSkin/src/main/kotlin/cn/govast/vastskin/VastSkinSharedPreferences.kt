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

package cn.govast.vastskin

import android.app.Application
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/3/27 19:47
// Description:
// Documentation:

/**
 * [VastSkinSharedPreferences] is used to
 * store the skin path in [SharedPreferences]
 */
object VastSkinSharedPreferences {

    /**
     * The [SharedPreferences] of the skin.
     */
    private lateinit var skinSharedPreferences:SharedPreferences

    internal fun initSkinSharedPreferences(application: Application){
        // Make sure using an unencrypted database is safe here.
        val masterKey = MasterKey.Builder(application)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
        skinSharedPreferences = EncryptedSharedPreferences.create(
            application,
            THEME_FILE,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    /**
     * Remove file path from [skinSharedPreferences].
     */
    internal fun reset() {
        skinSharedPreferences.edit().apply {
            remove(THEME_PATH)
            apply()
        }
    }

    /**
     * Get skin file path from [skinSharedPreferences].
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