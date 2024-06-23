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

package com.log.vastgui.core.plugin

import com.log.vastgui.core.LogPipeline
import com.log.vastgui.core.LogCat
import com.log.vastgui.core.base.LogPlugin

// Author: ywnkm
// Email: https://github.com/ywnkm
// Date: 2024/6/22

/**
 * [LogStateChecker] is used to detect whether printing logs is currently allowed.
 *
 * @since 1.3.4
 */
class LogStateChecker internal constructor(){

    companion object : LogPlugin<Unit, LogStateChecker> {

        override val key: String = LogStateChecker::class.java.simpleName

        override fun configuration(config: Unit.() -> Unit): LogStateChecker {
            return LogStateChecker()
        }

        override fun install(plugin: LogStateChecker, scope: LogCat) {
            scope.logPipeline.intercept(LogPipeline.State) {
                if (!context.logEnabled) {
                    finish()
                    return@intercept
                }
                proceed()
            }
        }
    }
}