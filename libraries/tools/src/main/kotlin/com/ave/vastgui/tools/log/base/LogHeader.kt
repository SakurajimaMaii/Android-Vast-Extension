/*
 * Copyright 2024 VastGui guihy2019@gmail.com
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

package com.ave.vastgui.tools.log.base

import com.log.vastgui.core.base.LogInfo

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2024/5/13 20:53
// Description: 
// Documentation:
// Reference:

/**
 * Log header configuration.
 *
 * @property thread `true` if you want to show [LogInfo.mThreadName] in
 *     header, `false` otherwise.
 * @property tag `true` if you want to show [LogInfo.mTag] in header,
 *     `false` otherwise.
 * @property level `true` if you want to show [LogInfo.mLevel] in header,
 *     `false` otherwise.
 * @property time `true` if you want to show [LogInfo.mTime] in header,
 *     `false` otherwise.
 * @since 1.3.1
 */
data class LogHeader(val thread: Boolean, val tag: Boolean, val level: Boolean, val time: Boolean)