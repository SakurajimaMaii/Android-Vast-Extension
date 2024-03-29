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

package com.ave.vastgui.app.activity.log

import com.ave.vastgui.tools.log.getLogFactory
import com.ave.vastgui.tools.log.json.GsonConverter
import com.ave.vastgui.tools.log.plugin.LogJson
import com.ave.vastgui.tools.log.plugin.LogPrinter
import com.ave.vastgui.tools.log.plugin.LogSwitch

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/7/5
// Documentation: https://ave.entropy2020.cn/documents/VastTools/log/description/

val mLogFactory = getLogFactory {
    install(LogSwitch) {
        open = true
    }
    install(LogPrinter) {
        maxSingleLogLength = 100
        maxPrintTimes = 5
    }
//    install(LogStorage) {
//        fileMaxSize = 1024L * 1000
//        storageFormat = { time, level, content ->
//            "$level || $time || $content "
//        }
//    }
    install(LogJson){
        converter = GsonConverter(true)
    }
}