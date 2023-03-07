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

package com.ave.vastgui.tools.network.response

import com.ave.vastgui.tools.network.handle.HttpHandle
import com.ave.vastgui.tools.network.request.Request
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

class ResponseBuilder(val mainScope: CoroutineScope) {

    fun <T : ResponseApi> requestWithListener(
        request: () -> Request<T>,
        listener: ResponseStateListener<T>.() -> Unit
    ) {
        val mListener = ResponseStateListener<T>().also(listener)
        mainScope.launch {
            flow {
                emit(HttpHandle.requestWithCall(request))
            }.onStart {
                mListener.onStart()
            }.onCompletion {
                mListener.onCompletion(it)
            }.collect { response ->
                parseData(response, mListener)
            }
        }
    }

    fun <T : ResponseApi> suspendWithListener(
        request: suspend () -> T,
        listener: ResponseStateListener<T>.() -> Unit
    ) {
        val mListener = ResponseStateListener<T>().also(listener)
        mainScope.launch {
            flow {
                emit(HttpHandle.requestWithSuspend(request))
            }.onStart {
                mListener.onStart()
            }.onCompletion {
                mListener.onCompletion(it)
            }.collect { response ->
                parseData(response, mListener)
            }
        }
    }

    private fun <T> parseData(
        response: ResponseWrapper<T>,
        listener: ResponseStateListener<T>
    ) {
        when (response) {
            is ResponseWrapper.SuccessResponseWrapper -> listener.onSuccess(response.data)
            is ResponseWrapper.EmptyResponseWrapper -> listener.onEmpty()
            is ResponseWrapper.FailedResponseWrapper -> listener.onFailed(
                response.errorCode,
                response.errorMsg
            )
            is ResponseWrapper.ErrorResponseWrapper -> listener.onError(response.throwable)
        }
    }

}