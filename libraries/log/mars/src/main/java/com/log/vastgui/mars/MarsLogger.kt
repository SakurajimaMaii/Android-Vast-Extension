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

package com.log.vastgui.mars

import com.ave.vastgui.core.extension.nothing_to_do
import com.log.vastgui.core.base.LogInfo
import com.log.vastgui.core.base.Logger
import com.log.vastgui.mars.base.MarsConfig

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2024/6/12 21:57
// Description: 
// Documentation:
// Reference:

/**
 * Mars
 *
 * @since 1.3.4
 */
fun Logger.Companion.mars(open: Boolean = true) = MarsLogger(open)

class MarsLogger internal constructor(open: Boolean = true) : Logger {
    override fun log(info: LogInfo) {
        // NOTE Because Mars integrates printing and storage,
        // NOTE there is no need to do anything here
        nothing_to_do()
    }

    init {
        MarsConfig.isConsoleLogOpen = open
    }
}