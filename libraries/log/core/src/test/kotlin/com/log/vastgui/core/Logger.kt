/*
 * Copyright 2024 VastGui
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

package com.log.vastgui.core

import com.log.vastgui.core.base.LogLevel
import com.log.vastgui.core.json.GsonConverter
import com.log.vastgui.core.plugin.LogJson
import com.log.vastgui.core.plugin.LogPretty
import com.log.vastgui.core.plugin.LogPrinter
import com.log.vastgui.core.plugin.LogSwitch

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/7/5
// Documentation: https://ave.entropy2020.cn/documents/VastTools/log/description/

private val gson = GsonConverter.getInstance(true)

val mLogFactory: LogFactory = getLogFactory {
    install(LogSwitch) {
        open = true
    }
    install(LogPrinter) {
        levelSet = setOf(LogLevel.DEBUG)
        logger = SimpleLogger()
    }
    install(LogJson) {
        converter = gson
    }
    install(LogPretty) {
        converter = gson
    }
}

//    install(LogPrinter) {
//        logger = object : Logger {
//            override val logFormat: LogFormat
//                get() = OnlyMsgFormat
//
//            override fun log(logInfo: LogInfo) {
//                println(logFormat.format(logInfo))
//            }
//        }
//    }
//    install(LogJson){
//        converter = gson
//    }
//    install(LogPretty){
//        converter = gson
//    }