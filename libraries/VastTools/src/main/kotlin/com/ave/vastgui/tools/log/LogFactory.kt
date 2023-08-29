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

package com.ave.vastgui.tools.log

import com.ave.vastgui.core.extension.cast
import com.ave.vastgui.tools.log.base.LogPlugin

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/7/5
// Documentation: https://ave.entropy2020.cn/documents/VastTools/log/Description/

/**
 * Get log factory
 *
 * @since 0.5.2
 */
fun getLogFactory(factory: LogFactory.() -> Unit) = LogFactory().also(factory)

/**
 * Log factory. You can only get a [LogFactory] by [getLogFactory]. By
 * default, you should only create one factory in your app.
 *
 * @since 0.5.2
 */
class LogFactory internal constructor() {

    private val plugins: MutableMap<String, (LogUtil) -> Unit> = mutableMapOf()
    private val pluginConfigurations: MutableMap<String, Any.() -> Unit> = mutableMapOf()

    /**
     * Install plugin for your log.
     *
     * @since 0.5.2
     */
    fun <TConfig, TPlugin> install(
        plugin: LogPlugin<TConfig, TPlugin>,
        configure: TConfig.() -> Unit = {}
    ) {
        val previousConfigBlock = pluginConfigurations[plugin.key]
        pluginConfigurations[plugin.key] = {
            previousConfigBlock?.invoke(this)
            cast<TConfig>(this).configure()
        }

        if (plugins.containsKey(plugin.key)) return

        plugins[plugin.key] = { scope ->
            val pluginData = plugin.configuration(configure)
            plugin.install(pluginData, scope)
        }
    }

    /**
     * Get log with [clazz].
     *
     * @since 0.5.2
     */
    fun getLog(clazz: Class<*>) = LogUtil().also {
        it.tag = clazz.simpleName
        install(it)
    }

    /**
     * Get log with [tag].
     *
     * @since 0.5.2
     */
    fun getLog(tag: String) = LogUtil().also {
        it.tag = tag
        install(it)
    }

    /**
     * Install the plugin to [logUtil].
     *
     * @since 0.5.2
     */
    private fun install(logUtil: LogUtil) {
        plugins.values.forEach { logUtil.apply(it) }
    }

}