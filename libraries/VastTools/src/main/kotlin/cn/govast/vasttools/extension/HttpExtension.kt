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

package cn.govast.vasttools.extension

import cn.govast.vasttools.network.ApiRspWrapper
import cn.govast.vasttools.network.apicall.ApiCall
import cn.govast.vasttools.network.base.BaseApiRsp
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/9/28
// Description: 
// Documentation:
// Reference:

suspend fun <T : BaseApiRsp> executeHttp(block: () -> ApiCall<T>): ApiRspWrapper<T> {
    return suspendCoroutine {
        block().request {
            onSuccess = { data ->
                it.resume(ApiRspWrapper.ApiSuccessWrapper(data))
            }
            onEmpty = {
                it.resume(ApiRspWrapper.ApiEmptyWrapper())
            }
            onFailed = { code, msg ->
                it.resume(ApiRspWrapper.ApiFailedWrapper(code, msg))
            }
            onError = { throwable ->
                it.resume(ApiRspWrapper.ApiErrorWrapper(throwable))
            }
        }
    }
}

suspend fun <T : BaseApiRsp> executeHttp(block: suspend () -> T): ApiRspWrapper<T> {
    runCatching {
        block.invoke()
    }.onSuccess { data: T ->
        return handleHttpOk(data)
    }.onFailure { e ->
        return handleHttpError(e)
    }
    return ApiRspWrapper.ApiEmptyWrapper()
}

/**
 * Non-background return errors, caught exceptions.
 *
 * @since 0.0.9
 */
private fun <T : BaseApiRsp> handleHttpError(e: Throwable): ApiRspWrapper<T> {
    return ApiRspWrapper.ApiErrorWrapper(e)
}

/**
 * Get data and also judge is success.
 *
 * @since 0.0.9
 */
private fun <T : BaseApiRsp> handleHttpOk(data: T): ApiRspWrapper<T> {
    return if (data.isSuccess()) {
        getHttpSuccessResponse(data)
    } else {
        ApiRspWrapper.ApiFailedWrapper(data.getErrorCode(), data.getErrorMsg())
    }
}

/**
 * Deal with success and empty data.
 *
 * @since 0.0.9
 */
private fun <T : BaseApiRsp> getHttpSuccessResponse(data: T): ApiRspWrapper<T> {
    return if (data.isEmpty()) {
        ApiRspWrapper.ApiEmptyWrapper()
    } else {
        ApiRspWrapper.ApiSuccessWrapper(data)
    }
}