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

import java.io.File

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/3/14
// Description: 
// Documentation:
// Reference:

sealed interface FileProperty {

    /** Get the extension name of the file. */
    fun getExtension(file: File): String

    /**
     * Get the mimetype of the [file], [fallback] otherwise. For more
     * information, please refer to
     * [How to determine MIME type of file in android?](https://stackoverflow.com/questions/8589645/how-to-determine-mime-type-of-file-in-android)
     */
    fun getMimeType(file: File, fallback: String = "image/*"): String

}