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

package com.ave.vastgui.tools.io

import java.io.File

/** @since 1.5.2 */
fun File.asImageFile() = ImageFile(this)

/** @since 1.5.2 */
fun getImageFile() = ImageFile.Companion

/** @since 1.5.2 */
class ImageFile internal constructor(override val file: File) : MediaFile {

    /** @since 1.5.2 */
    override fun getDefaultFileName(): String = getDefaultFileName(file.extension)

    companion object : ImageFileProperty

}