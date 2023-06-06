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

package com.ave.vastgui.tools.network.handle

import com.ave.vastgui.tools.network.response.ResponseApi
import com.ave.vastgui.tools.network.response.ResponseWrapper

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/2/25

internal object HttpHandle {

    suspend fun <T : ResponseApi> requestWithSuspend(
        block: suspend () -> T
    ): ResponseWrapper<T> {
        runCatching {
            block()
        }.onSuccess { data: T ->
            return handleHttpOk(data)
        }.onFailure { e ->
            return handleHttpError(e)
        }
        return ResponseWrapper.EmptyResponseWrapper()
    }

    private fun <T : ResponseApi> handleHttpError(e: Throwable): ResponseWrapper<T> {
        return ResponseWrapper.ErrorResponseWrapper(e)
    }

    private fun <T : ResponseApi> handleHttpOk(data: T): ResponseWrapper<T> {
        return if (data.isSuccessful()) {
            getHttpSuccessResponse(data)
        } else {
            ResponseWrapper.FailedResponseWrapper(
                data.code(),
                data.message()
            )
        }
    }

    private fun <T : ResponseApi> getHttpSuccessResponse(
        data: T,
    ): ResponseWrapper<T> {
        return if (data.isEmpty()) {
            ResponseWrapper.EmptyResponseWrapper()
        } else {
            ResponseWrapper.SuccessResponseWrapper(data)
        }
    }
}