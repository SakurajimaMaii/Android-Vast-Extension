package com.log.vastgui.core.pipeline

internal sealed class PipelinePhaseRelation {

    class After(val relativeTo: PipelinePhase) : PipelinePhaseRelation()

    class Before(val relativeTo: PipelinePhase) : PipelinePhaseRelation()

    data object Last : PipelinePhaseRelation()
}
