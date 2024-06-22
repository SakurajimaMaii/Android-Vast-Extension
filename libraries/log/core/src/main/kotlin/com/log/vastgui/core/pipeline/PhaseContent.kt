package com.log.vastgui.core.pipeline

internal class PhaseContent<TSubject : Any, Call: Any>(
    val phase: PipelinePhase,
    val relation: PipelinePhaseRelation,
    var interceptors: MutableList<PipelineInterceptor<TSubject, Call>>
) {

    val isEmpty: Boolean get() = interceptors.isEmpty()

    val size: Int get() = interceptors.size

    fun addInterceptor(interceptor: PipelineInterceptor<TSubject, Call>) {
        interceptors.add(interceptor)
    }

    fun addTo(destination: MutableList<PipelineInterceptor<TSubject, Call>>) {

        for (interceptor in interceptors) {
            destination.add(interceptor)
        }
    }

    fun addTo(destination: PhaseContent<TSubject, Call>) {
        if (isEmpty) return
        addTo(destination.interceptors)
    }

    override fun toString(): String {
        return "Phase `${phase.name}`, $size handlers"
    }

}