package com.log.vastgui.core.pipeline

internal class SimplePipelineContext<TSubject: Any, TContext: Any> (
    context: TContext,
    private val interceptors: List<PipelineInterceptor<TSubject, TContext>>,
    subject: TSubject,
) : PipelineContext<TSubject, TContext>(context) {

    override var subject: TSubject = subject

    private var index = 0

    override fun finish() {
        index = -1
    }

    override fun proceedWith(subject: TSubject): TSubject {
        this.subject = subject
        return proceed()
    }

    override fun proceed(): TSubject {
        val index = index
        if (index < 0) return subject

        if (index >= interceptors.size) {
            finish()
            return subject
        }

        return proceedLoop()
    }

    override fun execute(initial: TSubject): TSubject {
        index = 0
        subject = initial
        return proceed()
    }

    private fun proceedLoop(): TSubject {
        do {
            val index = index
            if (index == -1) {
                break
            }
            val interceptors = interceptors
            if (index >= interceptors.size) {
                finish()
                break
            }

            val interceptor = interceptors[index]
            this.index = index + 1
            interceptor(this, subject)
        } while (true)

        return subject
    }
}