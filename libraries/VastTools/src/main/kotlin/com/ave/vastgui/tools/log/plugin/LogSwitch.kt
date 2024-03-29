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

package com.ave.vastgui.tools.log.plugin

import com.ave.vastgui.tools.log.LogUtil
import com.ave.vastgui.tools.log.base.LogPlugin
import com.ave.vastgui.tools.log.plugin.LogSwitch.Configuration

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/7/5
// Documentation: https://ave.entropy2020.cn/documents/VastTools/log/description/

/**
 * LogSwitch.
 *
 * @property open Refer to [Configuration.open].
 * @since 0.5.2
 */
class LogSwitch internal constructor(
    val open: Boolean
) {

    /**
     * Configuration of [LogSwitch].
     *
     * @property open [LogUtil] will print log when the [open] is true and the
     *     app is in debug mode.
     * @since 0.5.4
     */
    class Configuration {
        var open: Boolean = false
    }

    companion object : LogPlugin<Configuration, LogSwitch> {

        override val key: String
            get() = LogSwitch::class.java.simpleName

        override fun configuration(config: Configuration.() -> Unit): LogSwitch {
            val configuration = Configuration().also(config)
            return LogSwitch(configuration.open)
        }

        override fun install(plugin: LogSwitch, scope: LogUtil) {
            scope.logEnabled = plugin.open
        }

    }

}