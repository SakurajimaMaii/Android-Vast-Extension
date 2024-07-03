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

import com.ave.vastgui.tools.network.response.ResponseApi
import com.ave.vastgui.tools.network.response.ResponseMutableLiveData
import com.ave.vastgui.tools.network.response.ResponseStateListener
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Response
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.util.concurrent.Executor

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/10/9

/**
 * [Request] adapter factory.
 *
 * When you use [Request] as the return value of the network
 * api.You should add [RequestAdapterFactory] to the
 * [Retrofit] by [Retrofit.Builder.addCallAdapterFactory].
 *
 * ```kotlin
 * // The is a example.
 * val retrofit = Retrofit.Builder()
 *         .... // Other configuration.
 *         .addCallAdapterFactory(RequestAdapterFactory())
 *         .build()
 * ```
 */
class RequestAdapterFactory : CallAdapter.Factory() {
    override fun get(
        returnType: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        if (getRawType(returnType) != Request::class.java) return null
        check(returnType is ParameterizedType) {
            "Request must have generic type (e.g., Request<ResponseBody>)"
        }
        val responseType = getParameterUpperBound(0, returnType)
        val executor = retrofit.callbackExecutor()
        return RequestAdapter<ResponseApi>(responseType, executor)
    }
}

internal class RequestAdapter<T : ResponseApi>(
    private val responseType: Type,
    private val executor: Executor?
) : CallAdapter<T, Request<T>> {

    override fun adapt(call: Call<T>): Request<T> {
        return RequestImpl(call, executor)
    }

    override fun responseType(): Type {
        return responseType
    }

}

internal class RequestImpl<T : ResponseApi>(
    private val call: Call<T>,
    private val callbackExecutor: Executor?
) : Request<T> {

    override fun cancel() {
        call.cancel()
    }

    override fun request(
        listener: ResponseStateListener<T>.() -> Unit
    ) {
        val mListener = ResponseStateListener<T>().also(listener)
        call.enqueue(object : retrofit2.Callback<T> {
            override fun onFailure(call: Call<T>, t: Throwable) {
                mListener.onError(t)
            }

            override fun onResponse(call: Call<T>, response: Response<T>) {
                val body = response.body()
                if ((body == null) || (body.isEmpty())) {
                    mListener.onEmpty
                } else {
                    if (response.isSuccessful) {
                        if (body.isSuccessful()) {
                            mListener.onSuccess(body)
                        } else {
                            mListener.onFailed(body.code(), body.message())
                        }
                    } else {
                        mListener.onFailed(response.code(), response.message())
                    }
                }
            }
        })
    }

    override fun request(stateLiveData: ResponseMutableLiveData<T>) {
        call.enqueue(object : retrofit2.Callback<T> {
            override fun onFailure(call: Call<T>, t: Throwable) {
                stateLiveData.postError(t)
            }

            override fun onResponse(call: Call<T>, response: Response<T>) {
                val body = response.body()
                if ((null == body) || (body.isEmpty())) {
                    stateLiveData.postEmpty()
                } else {
                    if (response.isSuccessful) {
                        if (body.isSuccessful()) {
                            stateLiveData.postValueAndSuccess(body)
                        } else {
                            stateLiveData.postFailed(body.code(), body.message())
                        }
                    } else {
                        stateLiveData.postFailed(response.code(), response.message())
                    }
                }
            }
        })
    }

    override fun clone(): Request<T> {
        return RequestImpl(call.clone(), callbackExecutor)
    }
}