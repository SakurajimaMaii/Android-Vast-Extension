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

import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/3/27

@Deprecated(
    level = DeprecationLevel.WARNING,
    message = "Please use Request or Request2 to replace ResponseBuilder.",
    replaceWith = ReplaceWith("")
)
class ResponseContinuation<T : ResponseApi> internal constructor(
    override val context: CoroutineContext,
    private val listener: ResponseStateListener<T>
) : Continuation<ResponseWrapper<T>> {

    override fun resumeWith(result: Result<ResponseWrapper<T>>) {
        result.getOrNull()?.apply {
            when (this) {
                is ResponseWrapper.SuccessResponseWrapper -> listener.onSuccess(this.data)
                is ResponseWrapper.EmptyResponseWrapper -> listener.onEmpty()
                is ResponseWrapper.FailedResponseWrapper -> listener.onFailed(
                    this.errorCode,
                    this.errorMsg
                )

                is ResponseWrapper.ErrorResponseWrapper -> listener.onError(this.throwable)
            }
        }
    }

}

/**
 * Live data continuation.
 *
 * @since 0.3.0
 */
@Deprecated(
    level = DeprecationLevel.WARNING,
    message = "Please use Request or Request2 to replace ResponseBuilder.",
    replaceWith = ReplaceWith("")
)
class LiveDataContinuation<T : ResponseApi> internal constructor(
    override val context: CoroutineContext,
    private val responseLiveData: ResponseMutableLiveData<T>
) : Continuation<ResponseWrapper<T>> {

    override fun resumeWith(result: Result<ResponseWrapper<T>>) {
        result.getOrNull()?.apply {
            when (this) {
                is ResponseWrapper.SuccessResponseWrapper -> responseLiveData.postValueAndSuccess(
                    this.data
                )

                is ResponseWrapper.EmptyResponseWrapper -> responseLiveData.postEmpty()
                is ResponseWrapper.FailedResponseWrapper -> responseLiveData.postFailed(
                    this.errorCode,
                    this.errorMsg
                )

                is ResponseWrapper.ErrorResponseWrapper -> responseLiveData.postError(this.throwable)
            }
        }
    }

}