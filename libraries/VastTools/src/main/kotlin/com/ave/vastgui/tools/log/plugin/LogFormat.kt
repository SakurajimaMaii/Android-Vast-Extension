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

package com.ave.vastgui.tools.log.plugin

import com.ave.vastgui.tools.log.LogUtil
import com.ave.vastgui.tools.log.base.LogPlugin
import com.ave.vastgui.tools.log.json.Converter

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/7/5
// Description: 
// Documentation:
// Reference:

class LogFormat internal constructor(val config: LogFormat.Config) {

    /**
     * [LogFormat] configuration.
     *
     * @since 0.5.2
     */
    class Config {
        /** @see LogUtil.singleLogCharLength */
        var singleLogCharLength = LogUtil.defaultCharLength

        /** @see LogUtil.maxPrintTimes */
        var maxPrintTimes = LogUtil.defaultCharLength

        var converter: Converter? = null
    }

    companion object : LogPlugin<Config, LogFormat> {
        override val key: String
            get() = LogFormat::class.java.simpleName

        override fun configuration(config: Config.() -> Unit): LogFormat {
            val configuration = Config().apply(config)
            return LogFormat(configuration)
        }

        override fun install(plugin: LogFormat, scope: LogUtil) {
            scope.singleLogCharLength = plugin.config.singleLogCharLength
            scope.maxPrintTimes = plugin.config.maxPrintTimes
            scope.converter = plugin.config.converter
        }
    }

}