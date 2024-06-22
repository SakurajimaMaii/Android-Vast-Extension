package com.log.vastgui.core.plugin

import com.log.vastgui.core.LogPipeline
import com.log.vastgui.core.LogUtil
import com.log.vastgui.core.base.LogInfoBuilder
import com.log.vastgui.core.base.LogLevel
import com.log.vastgui.core.base.LogPlugin
import com.log.vastgui.core.pipeline.PipelinePhase

class LogTypeValidator {

    companion object : LogPlugin<Unit, LogTypeValidator> {

        private val instance = LogTypeValidator()

        override val key: String = "LogTypeValidator"

        override fun configuration(config: Unit.() -> Unit): LogTypeValidator {
            return instance
        }

        override fun install(plugin: LogTypeValidator, scope: LogUtil) {
            val afterTransform = PipelinePhase("AfterTransform")
            scope.logPipeline.insertPhaseAfter(LogPipeline.Transform, afterTransform)
            scope.logPipeline.intercept(afterTransform) {
                if (subject.content() !is String) {
                    val builder = LogInfoBuilder(
                        LogLevel.ERROR,
                        "LogTypeValidator",
                        "Can not convert ${subject.content().javaClass}, please install a specific converter plugin."
                    )
                    proceedWith(builder)
                } else {
                    proceed()
                }
            }
        }
    }
}