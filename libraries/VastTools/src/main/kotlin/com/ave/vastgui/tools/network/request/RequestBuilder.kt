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

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/9/24
// Description: 
// Documentation:
// Reference: https://juejin.cn/post/7027675216080240647#heading-6

abstract class RequestBuilder {

    private val okHttpClient: OkHttpClient by lazy {
        val builder = OkHttpClient.Builder()
            .callTimeout(setTimeOut(), TimeUnit.SECONDS)
            .connectTimeout(setTimeOut(), TimeUnit.SECONDS)
            .readTimeout(setTimeOut(), TimeUnit.SECONDS)
            .writeTimeout(setTimeOut(), TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
        registerLoggingInterceptor()?.also {
            builder.addInterceptor(it)
        }
        handleOkHttpClientBuilder(builder)
        builder.build()
    }

    private val retrofit = Retrofit.Builder()
        .baseUrl(setBaseUrl())
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .addCallAdapterFactory(RequestAdapterFactory())
        .client(okHttpClient)
        .build()

    fun <T> create(serviceClass: Class<T>): T = retrofit.create(serviceClass)

    /** Set timeout. Default return 8L. */
    open fun setTimeOut(): Long {
        return 8L
    }

    /** Set base url */
    abstract fun setBaseUrl(): String

    /** Customize the configuration of OKHttpClient. */
    open fun handleOkHttpClientBuilder(builder: OkHttpClient.Builder) {

    }

    /** Register a log interceptor. */
    open fun registerLoggingInterceptor(): Interceptor? {
        return null
    }

}