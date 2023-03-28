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

package com.ave.vastgui.tools.coroutines

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob


// Author: Vast Gui

// Email: guihy2019@gmail.com
// Date: 2023/3/6
// Description: 
// Documentation:
// Reference:

/**
 * Create Main Scope
 *
 * @param name User-defined coroutine name.
 */
fun createMainScope(name: String): CoroutineScope {
    return CoroutineScope(
        CoroutineName(name) + SupervisorJob() + Dispatchers.Main.immediate
    )
}