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

import com.log.vastgui.core.base.LogInfo
import com.log.vastgui.core.base.LogLevel
import com.log.vastgui.core.base.LogStore
import com.log.vastgui.mars.config.MarsConfig
import com.tencent.mars.xlog.Log

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2024/6/1 0:12
// Description: 
// Documentation:
// Reference:

fun LogStore.Companion.mars(config: MarsConfig) = MarsStore(config)

class MarsStore internal constructor(val config: MarsConfig) : LogStore {
    override fun store(info: LogInfo) = when (info.mLevel) {
        LogLevel.VERBOSE -> Log.v(info.mTag, info.mContent)
        LogLevel.DEBUG -> Log.d(info.mTag, info.mContent)
        LogLevel.INFO -> Log.i(info.mTag, info.mContent)
        LogLevel.WARN -> Log.w(info.mTag, info.mContent)
        LogLevel.ERROR -> Log.e(info.mTag, info.mContent)
        LogLevel.ASSERT -> Log.f(info.mTag, info.mContent)
    }
}