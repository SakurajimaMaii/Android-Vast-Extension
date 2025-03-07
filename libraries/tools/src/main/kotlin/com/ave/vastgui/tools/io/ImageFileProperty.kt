/*
 * Copyright 2021-2024 VastGui
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

package com.ave.vastgui.tools.io

import android.os.Build
import android.os.Environment
import java.io.File

/** @since 1.5.2 */
sealed interface ImageFileProperty : MediaFileProperty {

    /**
     * Get the traditional location for pictures and videos when mounting the
     * device as a camera. It will be created if the directory not yet exist.
     *
     * @since 1.5.2
     */
    fun sharedDCIM(): File = getSharedFilesDir(Environment.DIRECTORY_DCIM)

    /**
     * Standard directory in which to place pictures that are available to the
     * user. It will be created if the directory not yet exist.
     *
     * @since 1.5.2
     */
    fun sharedPictures(): File = getSharedFilesDir(Environment.DIRECTORY_PICTURES)

    /**
     * Standard directory in which to place screenshots that have been taken by
     * the user. It will be created if the directory not yet exist.
     *
     * @since 1.5.2
     */
    fun sharedScreenshots(): File {
        val type = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
            Environment.DIRECTORY_SCREENSHOTS else "Screenshots"
        return getSharedFilesDir(type)
    }

    /**
     * Get the traditional location for pictures and videos when mounting the
     * device as a camera. It will be created if the directory not yet exist.
     * These files are internal to the applications, and not typically visible
     * to the user as media.
     *
     * @since 1.5.2
     */
    fun externalDCIM(): File = getExternalFilesDir(Environment.DIRECTORY_DCIM)

    /**
     * Standard directory in which to place pictures that are available to the
     * user. It will be created if the directory not yet exist. These files are
     * internal to the applications, and not typically visible to the user as
     * media.
     *
     * @since 1.5.2
     */
    fun externalPictures(): File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)

    /**
     * Standard directory in which to place screenshots that have been taken by
     * the user. It will be created if the directory not yet exist. These files
     * are internal to the applications, and not typically visible to the user
     * as media.
     *
     * @since 1.5.2
     */
    fun externalScreenshots(): File {
        val type = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
            Environment.DIRECTORY_SCREENSHOTS else "Screenshots"
        return getExternalFilesDir(type)
    }

}