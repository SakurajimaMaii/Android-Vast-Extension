package com.log.vastgui.core.pipeline

abstract class PipelineContext<TSubject : Any, TContext : Any>(
    val context: TContext
) {

    abstract var subject: TSubject

    abstract fun finish()

    abstract fun proceedWith(subject: TSubject): TSubject

    abstract fun proceed(): TSubject

    internal abstract fun execute(initial: TSubject): TSubject
}