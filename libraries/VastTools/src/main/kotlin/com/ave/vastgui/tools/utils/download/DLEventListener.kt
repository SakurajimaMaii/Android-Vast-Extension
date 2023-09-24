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

package com.ave.vastgui.tools.utils.download

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/7/23
// Documentation: https://ave.entropy2020.cn/documents/VastTools/core-topics/connectivity/download/download/

/**
 * The listener of the [DLEvent] for the download.
 *
 * @since 0.5.2
 */
class DLEventListener internal constructor() {
    var onSuccess: ((DLEvent.SUCCESS) -> Unit) = {}
    var onDownloading: ((DLEvent.DOWNLOADING) -> Unit) = {}
    var onFailure: ((DLEvent.FAILED) -> Unit) = {}
    var onPause: (() -> Unit) = {}
    var onCancel: (() -> Unit) = {}
}