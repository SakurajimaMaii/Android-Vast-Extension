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

import com.ave.vastgui.tools.network.response.ResponseApi
import com.ave.vastgui.tools.network.response.ResponseLiveData
import com.ave.vastgui.tools.network.response.ResponseStateListener
import retrofit2.Call
import retrofit2.Response
import java.util.concurrent.Executor

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/10/9

class RequestImpl<T : ResponseApi>(
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
                    if (body.isSuccessful()) {
                        mListener.onSuccess(body)
                    } else {
                        mListener.onFailed(response.code(), response.message())
                    }
                }
            }
        })
    }

    override fun request(stateLiveData: ResponseLiveData<T>) {
        call.enqueue(object : retrofit2.Callback<T> {
            override fun onFailure(call: Call<T>, t: Throwable) {
                stateLiveData.postError(t)
            }

            override fun onResponse(call: Call<T>, response: Response<T>) {
                val body = response.body()
                if ((null == body) || (body.isEmpty())) {
                    stateLiveData.postEmpty()
                } else {
                    if (body.isSuccessful()) {
                        stateLiveData.postValueAndSuccess(body)
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
