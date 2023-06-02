package com.ave.vastgui.tools.network.request

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/6/2
// Documentation: https://ave.entropy2020.cn/documents/VastTools/core-topics/connectivity/performing-network-operations/Request2/

/**
 * Request2
 *
 * @since 0.5.1
 */
open class Request2<T> internal constructor() {
    class Success<T> internal constructor(val data: T) : Request2<T>()
    class Failure<T> internal constructor(val code: Int, val message: String) : Request2<T>()
    class Empty<T> internal constructor(): Request2<T>()
    class Exception<T> internal constructor(val exception: Throwable): Request2<T>()

    fun success(data: T): Success<T> = Success(data)
    fun failure(code: Int, message: String): Failure<T> = Failure(code, message)
    fun empty(): Empty<T> = Empty()
    fun exception(exception: Throwable): Exception<T> = Exception(exception)
}