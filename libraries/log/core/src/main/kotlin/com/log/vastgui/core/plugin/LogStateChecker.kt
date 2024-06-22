package com.log.vastgui.core.plugin

import com.log.vastgui.core.LogPipeline
import com.log.vastgui.core.LogUtil
import com.log.vastgui.core.base.LogPlugin

class LogStateChecker(
    val config: Config
) {

    class Config {
    }

    companion object : LogPlugin<Config, LogStateChecker> {

        override val key: String = "LogStateChecker"

        override fun configuration(config: Config.() -> Unit): LogStateChecker {
            return LogStateChecker(Config().also(config))
        }

        override fun install(plugin: LogStateChecker, scope: LogUtil) {
            scope.logPipeline.intercept(LogPipeline.State) {
                if (!context.logEnabled) {
                    finish()
                    return@intercept
                }
                // TODO check loglevel
                proceed()
            }
        }
    }
}

