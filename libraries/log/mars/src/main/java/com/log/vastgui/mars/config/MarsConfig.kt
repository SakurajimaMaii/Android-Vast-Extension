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

package com.log.vastgui.mars.config


// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2024/6/1 0:16
// Description: 
// Documentation:
// Reference:

object MarsConfig {

    init {
        System.loadLibrary("c++_shared")
        System.loadLibrary("marsxlog")
    }

    /**
     * Whether to save a log file every day
     *
     * @since 1.3.4
     */
    private var oneFileEveryday = true

}