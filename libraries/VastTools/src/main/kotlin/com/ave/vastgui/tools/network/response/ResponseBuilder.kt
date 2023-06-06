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
import kotlinx.coroutines.CoroutineScope
import kotlin.coroutines.startCoroutine

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/9/26

class ResponseBuilder(private val mainScope: CoroutineScope) {

    fun <T : ResponseApi> suspendWithListener(
        request: suspend () -> T,
        listener: ResponseStateListener<T>.() -> Unit
    ) {
        val mListener = ResponseStateListener<T>().also(listener)
        suspend {
            HttpHandle.requestWithSuspend(request)
        }.startCoroutine(ResponseContinuation(mainScope.coroutineContext, mListener))
    }

    /**
     * Suspend with listener
     *
     * @param request The request api.
     * @param responseLiveData The responseLiveData is used to check status.
     * @since 0.3.0
     */
    fun <T : ResponseApi> suspendWithListener(
        request: suspend () -> T,
        responseLiveData: ResponseMutableLiveData<T>
    ) {
        suspend {
            HttpHandle.requestWithSuspend(request)
        }.startCoroutine(LiveDataContinuation(mainScope.coroutineContext, responseLiveData))
    }

}