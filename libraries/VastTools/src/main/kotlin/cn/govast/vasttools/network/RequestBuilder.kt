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

package cn.govast.vasttools.network

import cn.govast.vasttools.base.BaseApiRsp
import cn.govast.vasttools.extension.executeHttp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/9/26
// Description: 
// Documentation:
// Reference:

class RequestBuilder(private val mainScope: CoroutineScope) {

    fun <T : BaseApiRsp> requestWithListener(
        request: suspend () -> T,
        listener: ApiRspListener<T>.() -> Unit
    ) = apply {
        val mListener = ApiRspListener<T>().also(listener)
        mainScope.launch {
            flow {
                emit(executeHttp { request() })
            }.onStart {
                mListener.onStart
            }.onCompletion {
                mListener.onCompletion(it)
            }.collect { response ->
                when (response) {
                    is ApiRspWrapper.ApiSuccessWrapper -> mListener.onSuccess(response.data)
                    is ApiRspWrapper.ApiEmptyWrapper -> mListener.onEmpty()
                    is ApiRspWrapper.ApiFailedWrapper -> mListener.onFailed(
                        response.errorCode,
                        response.errorMsg
                    )
                    is ApiRspWrapper.ApiErrorWrapper -> mListener.onError(response.throwable)
                }
            }
        }
    }

}