/*
 * Copyright 2021-2024 VastGui
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ave.vastgui.tools.network.request

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/6/2
// Documentation: https://ave.entropy2020.cn/documents/VastTools/core-topics/connectivity/performing-network-operations/request2/

/**
 * Request2
 *
 * @since 0.5.1
 */
open class Request2<T> internal constructor() {
    class Success<T> internal constructor(val data: T) : Request2<T>()
    class Failure<T> internal constructor(val code: Int, val message: String) : Request2<T>()
    class Empty<T> internal constructor() : Request2<T>()
    class Exception<T> internal constructor(val exception: Throwable) : Request2<T>()

    fun success(data: T): Success<T> = Success(data)
    fun failure(code: Int, message: String): Failure<T> = Failure(code, message)
    fun empty(): Empty<T> = Empty()
    fun exception(exception: Throwable): Exception<T> = Exception(exception)
}