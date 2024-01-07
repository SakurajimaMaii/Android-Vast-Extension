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

package com.ave.vastgui.tools.utils.permission

import android.Manifest
import android.os.Build
import androidx.annotation.RequiresApi

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/11/18

object Permission {
    /**
     * None Permission
     *
     * @since 0.5.6
     */
    private const val _NONE_PERMISSION = ""

    /**
     * Allows an app to access location in the background. If you're requesting
     * this permission, you must also request either [ACCESS_COARSE_LOCATION]
     * or [ACCESS_FINE_LOCATION].
     *
     * @since 0.5.6
     */
    @RequiresApi(Build.VERSION_CODES.Q)
    private val _ACCESS_BACKGROUND_LOCATION =
        Manifest.permission.ACCESS_BACKGROUND_LOCATION

    /**
     * Allows an application to read from external storage. If you're
     * requesting this permission above [Build.VERSION_CODES.TIRAMISU]
     * (including 33), it will be replaced with [_NONE_PERMISSION].
     *
     * @see READ_MEDIA_IMAGES
     * @see READ_MEDIA_VIDEO
     * @see READ_MEDIA_AUDIO
     * @since 0.5.6
     */
    val READ_EXTERNAL_STORAGE =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) _NONE_PERMISSION
        else Manifest.permission.READ_EXTERNAL_STORAGE

    /**
     * Allows an application to write to external storage. If you're requesting
     * this permission above [Build.VERSION_CODES.Q] (including 30), it will be
     * replaced with [_NONE_PERMISSION].
     *
     * @since 0.5.6
     */
    val WRITE_EXTERNAL_STORAGE =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) _NONE_PERMISSION
        else Manifest.permission.WRITE_EXTERNAL_STORAGE

    /**
     * Allows an app to access approximate location. Alternatively, you might
     * want [ACCESS_FINE_LOCATION] .
     *
     * @since 0.5.6
     */
    const val ACCESS_COARSE_LOCATION: String = Manifest.permission.ACCESS_COARSE_LOCATION

    /**
     * Allows an app to access precise location. Alternatively, you might want
     * [ACCESS_COARSE_LOCATION] .
     *
     * @since 0.5.6
     */
    const val ACCESS_FINE_LOCATION: String = Manifest.permission.ACCESS_FINE_LOCATION

    /**
     * Allows an app to access location in the background. If you're requesting
     * this permission above [Build.VERSION_CODES.Q] (including 29), it will
     * contain the [ACCESS_COARSE_LOCATION] , If you're requesting this
     * permission under [Build.VERSION_CODES.Q], it will be replaced with
     * [ACCESS_COARSE_LOCATION] .
     *
     * @since 0.5.6
     */
    val ACCESS_BACKGROUND_LOCATION: Array<String> =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
            arrayOf(_ACCESS_BACKGROUND_LOCATION, ACCESS_COARSE_LOCATION)
        else
            arrayOf(ACCESS_COARSE_LOCATION)

    /**
     * Allows an application to read image files from external storage. If
     * you're requesting this permission under [Build.VERSION_CODES.TIRAMISU],
     * it will be replaced with [READ_EXTERNAL_STORAGE] .
     *
     * @see READ_EXTERNAL_STORAGE
     * @since 0.5.6
     */
    val READ_MEDIA_IMAGES: String =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            Manifest.permission.READ_MEDIA_IMAGES
        else
            Manifest.permission.READ_EXTERNAL_STORAGE

    /**
     * Allows an application to read video files from external storage. If
     * you're requesting this permission under [Build.VERSION_CODES.TIRAMISU],
     * it will be replaced with [READ_EXTERNAL_STORAGE] .
     *
     * @see READ_EXTERNAL_STORAGE
     * @since 0.5.6
     */
    val READ_MEDIA_VIDEO: String =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            Manifest.permission.READ_MEDIA_VIDEO
        else
            Manifest.permission.READ_EXTERNAL_STORAGE

    /**
     * Allows an application to read audio files from external storage. If
     * you're requesting this permission under [Build.VERSION_CODES.TIRAMISU],
     * it will be replaced with [READ_EXTERNAL_STORAGE] .
     *
     * @see READ_EXTERNAL_STORAGE
     * @since 0.5.6
     */
    val READ_MEDIA_AUDIO: String =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            Manifest.permission.READ_MEDIA_AUDIO
        else
            Manifest.permission.READ_EXTERNAL_STORAGE

}