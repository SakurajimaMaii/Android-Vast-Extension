/*
 * Copyright 2022 VastGui guihy2019@gmail.com
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

import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/6/2

/**
 * [Request2] adapter factory.
 *
 * When you use [Request2] as the return value of the network
 * api.You should add [Request2AdapterFactory] to the
 * [Retrofit] by [Retrofit.Builder.addCallAdapterFactory].
 *
 * ```kotlin
 * // The is a example.
 * val retrofit = Retrofit.Builder()
 *         .... // Other configuration.
 *         .addCallAdapterFactory(Request2AdapterFactory())
 *         .build()
 * ```
 *
 * @since 0.5.1
 */
class Request2AdapterFactory : CallAdapter.Factory() {
    override fun get(
        returnType: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        if (getRawType(returnType) != Call::class.java) return null
        if (returnType !is ParameterizedType) return null
        val responseType = getParameterUpperBound(0, returnType)
        val responseRawType = getRawType(responseType)
        if (!Request2::class.java.isAssignableFrom(responseRawType)) {
            return null
        }
        if (responseType !is ParameterizedType) return null
        val apiResponse =
            getRawType(responseType).getConstructor().newInstance() as Request2<*>
        val successType = getParameterUpperBound(0, responseType)
        return Request2Adapter(successType, apiResponse)
    }
}

/**
 * Request2adapter
 *
 * @since 0.5.1
 */
internal class Request2Adapter<T>(
    private val responseType: Type,
    private val responseInstance: Request2<T>
) : CallAdapter<T, Call<Request2<T>>> {

    override fun adapt(call: Call<T>): Call<Request2<T>> {
        return Request2Impl(call, responseInstance)
    }

    override fun responseType(): Type {
        return responseType
    }

}

/**
 * Request2impl
 *
 * @since 0.5.1
 */
internal class Request2Impl<T>(
    private val call: Call<T>,
    private val responseInstance: Request2<T>
) : Call<Request2<T>> {

    override fun cancel() {
        call.cancel()
    }

    override fun isCanceled() = call.isCanceled

    override fun request(): Request = call.request()

    override fun timeout(): Timeout = call.timeout()

    override fun clone(): Call<Request2<T>> {
        return Request2Impl(call, responseInstance)
    }

    override fun execute(): Response<Request2<T>> {
        throw UnsupportedOperationException("ResponseCall not support execute")
    }

    override fun isExecuted(): Boolean = call.isExecuted

    override fun enqueue(callback: Callback<Request2<T>>) {
        return call.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                val body = response.body()
                val data = if (body == null) {
                    responseInstance.empty()
                } else {
                    if (response.isSuccessful) {
                        responseInstance.success(body)
                    } else {
                        responseInstance.failure(response.code(), response.message())
                    }
                }
                callback.onResponse(this@Request2Impl, Response.success(data))
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                val failed = responseInstance.exception(t)
                callback.onResponse(this@Request2Impl, Response.success(failed))
            }
        })
    }
}