package com.log.vastgui.core.base

import com.log.vastgui.core.LogUtil
import com.log.vastgui.core.internel.LazyMessageWrapper

class LogInfoBuilder(
    var level: LogLevel,
    var tag: String,
    content: Any,
    var tr: Throwable? = null
) {

    // String or lazy String (() -> String)
    private var rawContentOrLazy: Any = content

    internal val isFuncContent: Boolean
        get() = rawContentOrLazy is LazyMessageWrapper


    fun content(): Any {
        if (isFuncContent) {
            rawContentOrLazy = (rawContentOrLazy as LazyMessageWrapper).lazyMsg()
        }
        return rawContentOrLazy
    }

    // for convertor
    fun setStringContent(content: String) {
        this.rawContentOrLazy = content
    }

    fun build(): LogInfo {
        val thread = Thread.currentThread()
        val index = getStackOffset<LogUtil>(thread.stackTrace)
        return LogInfo(
            thread.name,
            thread.stackTrace[index + 1],
            level,
            tag,
            System.currentTimeMillis(),
            content() as String,
            TEXT_TYPE,
            tr
        )
    }
}