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

package com.log.vastgui.core.plugin

import com.log.vastgui.core.LogPipeline
import com.log.vastgui.core.LogUtil
import com.log.vastgui.core.base.LogPlugin
import com.log.vastgui.core.json.Converter
import com.log.vastgui.core.pipeline.PipelinePhase
import kotlin.properties.Delegates

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/9/8
// Documentation: https://ave.entropy2020.cn/documents/log/log-core/description/

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
        var converter: Converter by Delegates.notNull()
    }

    companion object : LogPlugin<Configuration, LogJson> {
        override val key: String
            get() = LogJson::class.java.simpleName

        override fun install(plugin: LogJson, scope: LogUtil) {
            // scope.mLogConverter = plugin.mConfiguration.converter
            scope.logPipeline.intercept(LogPipeline.Transform) {
                val content = subject.content()
                if (content is String) { // already string, skip
                    proceed()
                    return@intercept
                }
                // TODO check if content need to convert, could add predicate in configuration
                val converter = plugin.mConfiguration.converter
                val json = converter.toJson(content)
                subject.setStringContent(json)
                proceedWith(subject)
            }
        }

        override fun configuration(config: Configuration.() -> Unit): LogJson {
            val configuration = Configuration().also(config)
            return LogJson(configuration)
        }
    }

}