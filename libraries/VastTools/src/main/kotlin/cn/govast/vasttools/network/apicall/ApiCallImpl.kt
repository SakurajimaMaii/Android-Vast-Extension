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

package cn.govast.vasttools.network.apicall

import cn.govast.vasttools.lifecycle.StateLiveData
import cn.govast.vasttools.network.ApiRspStateListener
import cn.govast.vasttools.network.base.BaseApiRsp
import retrofit2.Call
import retrofit2.Response
import java.util.concurrent.Executor

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/10/9
// Description: 
// Documentation:
// Reference:

internal class ApiCallImpl<T : BaseApiRsp>(
    private val call: Call<T>,
    private val callbackExecutor: Executor?
) : ApiCall<T> {

    override fun cancel() {
        call.cancel()
    }

    override fun request(listener: ApiRspStateListener<T>.() -> Unit) {
        val mListener = ApiRspStateListener<T>().also(listener)
        call.enqueue(object : retrofit2.Callback<T> {
            override fun onFailure(call: Call<T>, t: Throwable) {
                mListener.onError(t)
            }

            override fun onResponse(call: Call<T>, response: Response<T>) {
                val body = response.body()
                if (null == body) {
                    mListener.onError(Throwable("Response is null."))
                } else {
                    if (body.isSuccess())
                        mListener.onSuccess(body)
                    else
                        mListener.onFailed(body.getErrorCode(), body.getErrorMsg())
                }
            }
        })
    }

    override fun request(stateLiveData: StateLiveData<T>) {
        call.enqueue(object : retrofit2.Callback<T> {
            override fun onFailure(call: Call<T>, t: Throwable) {
                stateLiveData.postError(t)
            }

            override fun onResponse(call: Call<T>, response: Response<T>) {
                val body = response.body()
                if (null == body) {
                    stateLiveData.postError(Throwable("Response is null."))
                } else {
                    if (body.isSuccess())
                        stateLiveData.postValueAndSuccess(body)
                    else
                        stateLiveData.postFailed(body.getErrorCode(), body.getErrorMsg())
                }
            }
        })
    }

    override fun clone(): ApiCall<T> {
        return ApiCallImpl(call.clone(), callbackExecutor)
    }
}
