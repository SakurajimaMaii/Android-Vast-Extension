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

import com.ave.vastgui.core.extension.NotNUllVar
import com.ave.vastgui.tools.log.LogUtil
import com.ave.vastgui.tools.log.base.LogPlugin
import com.ave.vastgui.tools.log.json.Converter

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/9/8
// Documentation: https://ave.entropy2020.cn/documents/VastTools/log/description/

/**
 * LogJson.
 *
 * @property mConfiguration The configuration of [LogJson].
 * @since 0.5.3
 */
class LogJson private constructor(private val mConfiguration: Configuration) {

    /**
     * Configuration of [LogJson].
     *
     * @property converter The json converter.
     * @since 0.5.3
     */
    class Configuration internal constructor() {
        var converter by NotNUllVar<Converter>()
    }

    companion object : LogPlugin<Configuration, LogJson> {
        override val key: String
            get() = LogJson::class.java.simpleName

        override fun install(plugin: LogJson, scope: LogUtil) {
            scope.mLogConverter = plugin.mConfiguration.converter
        }

        override fun configuration(config: Configuration.() -> Unit): LogJson {
            val configuration = Configuration().also(config)
            return LogJson(configuration)
        }
    }

}