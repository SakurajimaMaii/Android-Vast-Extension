/*
 * Copyright 2021-2024 VastGui
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.log.vastgui.mars.base

import com.tencent.mars.xlog.Xlog

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2024/6/12 22:35
// Description: 
// Documentation:
// Reference:

/**
 * Mars write mode
 *
 * @since 1.3.4.
 */
enum class MarsWriteMode(val value: Int) {
    /**
     * Async
     *
     * @since 1.3.4
     */
    Async(Xlog.AppednerModeAsync),

    /**
     * Sync. Synchronous writing, recommended when the application
     * is in debug mode, real-time writing, no encryption.
     *
     * @since 1.3.4
     */
    Sync(Xlog.AppednerModeSync)
}