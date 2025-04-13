/*
 * Copyright 2021-2025 VastGui
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

package com.ave.vastgui.tools.utils.download.core

import java.io.File

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2025/3/17
// Documentation: 
// Reference:

/**
 * The event of the download.
 *
 * @since 1.5.2
 */
sealed class DownloadResult {
    /**
     * @property file The file has been downloaded successfully.
     * @since 1.5.2
     */
    class Success(val file: File) : DownloadResult()

    /** @since 1.5.2 */
    class Download(val currentLength: Float, val length: Float) : DownloadResult() {
        /** @since 1.5.2 */
        constructor() : this(Float.NaN, Float.NaN)

        /**
         * [rate] value will be [Float.NaN] if the `content-length` field is not
         * included in the download request response.
         *
         * @since 1.5.2
         */
        val rate: Float
            get() = currentLength / length
    }

    /**
     * @property exception Exception that causes download failure.
     * @since 1.5.2
     */
    class Failure(val exception: Throwable) : DownloadResult()
}