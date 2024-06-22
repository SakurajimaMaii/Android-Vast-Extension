package com.log.vastgui.core

import com.log.vastgui.core.base.LogInfoBuilder
import com.log.vastgui.core.pipeline.Pipeline
import com.log.vastgui.core.pipeline.PipelinePhase

class LogPipeline : Pipeline<LogInfoBuilder, LogUtil>(Before, State, Transform, Render, Output) {

    companion object {

        val Before: PipelinePhase = PipelinePhase("Before")

        val State: PipelinePhase = PipelinePhase("State")

        val Transform: PipelinePhase = PipelinePhase("Transform")

        val Render: PipelinePhase = PipelinePhase("Render")

        val Output: PipelinePhase = PipelinePhase("Output")
    }
}

