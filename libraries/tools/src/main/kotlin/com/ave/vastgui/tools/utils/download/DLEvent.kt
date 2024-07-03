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

package com.ave.vastgui.tools.utils.download

import java.io.File

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/7/23
// Documentation: https://ave.entropy2020.cn/documents/VastTools/core-topics/connectivity/download/download/

/**
 * The event of the download.
 *
 * @since 0.5.2
 */
sealed class DLEvent {
    class SUCCESS(val data: File) : DLEvent()
    class DOWNLOADING(val currentLength: Long, val length: Long) : DLEvent() {
        val rate: Float
            get() = currentLength.toFloat() / length.toFloat()
    }

    class FAILED(val exception: Throwable) : DLEvent()
    object PAUSE : DLEvent()
    object RESUME : DLEvent()
    object CANCEL : DLEvent()
    object INIT : DLEvent()
}