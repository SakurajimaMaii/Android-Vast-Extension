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

package com.ave.vastgui.tools.manager.mediafilemgr

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import java.io.File

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/11/2

/** Using to provide information about media file. */
sealed interface MediaFileProperty {

    /**
     * Get application-specific storage folder.
     *
     * @param subDir The name of the subfolder you wish to create.
     * @since 0.5.0
     */
    fun getExternalFilesDir(subDir: String? = null): File?

    /**
     * Get shared storage folder.
     *
     * @since 0.5.0
     */
    fun getSharedFilesDir(): File

    fun getDefaultFileName(extension: String): String

    /**
     * Get file by [uri]. [getFileByUri] will query the DATA field
     * corresponding to [uri], and return the file if there is a corresponding
     * path, null otherwise.
     *
     * @return file, null otherwise.
     */
    fun getFileByUri(uri: Uri): File?

    @RequiresApi(Build.VERSION_CODES.R)
    fun getFileUriAboveApi30(file: File): Uri?

    @RequiresApi(Build.VERSION_CODES.R)
    fun getFileUriAboveApi30(saveOptions: MutableMap<String, String>.() -> Unit): Uri?

    /**
     * Get the uri by [file].
     *
     * Please register a provider in AndroidManifest.xml. For example:
     * ```xml
     * <provider
     *      android:name="androidx.core.content.FileProvider"
     *      android:authorities="${applicationId}"
     *      android:exported="false"
     *      android:grantUriPermissions="true">
     *      <meta-data
     *          android:name="android.support.FILE_PROVIDER_PATHS"
     *          android:resource="@xml/file_paths" />
     * </provider>
     * ```
     *
     * @param authority The authority of a [FileProvider] defined in a
     * <provider> element in your app's manifest.
     */
    @RequiresApi(Build.VERSION_CODES.N)
    fun getFileUriAboveApi24(file: File, authority: String): Uri

    /** Get the uri by [file]. */
    fun getFileUriOnApi23(file: File): Uri

}