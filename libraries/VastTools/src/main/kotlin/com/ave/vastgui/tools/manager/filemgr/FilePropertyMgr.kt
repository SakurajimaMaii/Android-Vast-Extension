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

package com.ave.vastgui.tools.manager.filemgr

import android.webkit.MimeTypeMap
import java.io.File
import java.util.Locale


// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/3/14
// Description: 
// Documentation:
// Reference:

internal class FilePropertyMgr : FileProperty {

    private val mimeTypeMap = MimeTypeMap.getSingleton()

    override fun getExtension(file: File): String {
        return MimeTypeMap.getFileExtensionFromUrl(file.toString())
    }

    override fun getMimeType(file: File, fallback: String): String {
        return MimeTypeMap.getFileExtensionFromUrl(file.toString())
            ?.run { mimeTypeMap.getMimeTypeFromExtension(lowercase(Locale.ROOT)) }
            ?: fallback
    }

}