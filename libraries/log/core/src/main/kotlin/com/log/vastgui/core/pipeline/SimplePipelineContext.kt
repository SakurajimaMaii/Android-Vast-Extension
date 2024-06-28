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

package com.log.vastgui.core.pipeline

// Author: ywnkm
// Email: https://github.com/ywnkm
// Date: 2024/6/22

/**
 * [SimplePipelineContext].
 *
 * @since 1.3.4
 */
internal class SimplePipelineContext<TSubject: Any, TContext: Any> (
    context: TContext,
    override var subject: TSubject,
    private val interceptors: List<PipelineInterceptor<TSubject, TContext>>,
) : PipelineContext<TSubject, TContext>(context) {

    /**
     * @since 1.3.4
     */
    private var index = 0

    /**
     * @since 1.3.4
     */
    override fun finish() {
        index = -1
    }

    /**
     * @since 1.3.4
     */
    override fun proceedWith(subject: TSubject): TSubject {
        this.subject = subject
        return proceed()
    }

    /**
     * @since 1.3.4
     */
    override fun proceed(): TSubject {
        val index = index
        if (index < 0) return subject
        if (index >= interceptors.size) {
            finish()
            return subject
        }
        return proceedLoop()
    }

    /**
     * @since 1.3.4
     */
    override fun execute(initial: TSubject): TSubject {
        index = 0
        subject = initial
        return proceed()
    }

    /**
     * @since 1.3.4
     */
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