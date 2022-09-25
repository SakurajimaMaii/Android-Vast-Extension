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

import com.gcode.vasttools.BuildConfig

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/9/25
// Description: 
// Documentation:
// Reference: https://github.com/SakurajimaMaii/FastJetpack/blob/dev/network/src/main/java/com/aisier/network/base/BaseRepository.kt

open class BaseRepository {

    protected suspend fun <T:BaseApiResponse> executeHttp(block: suspend () -> T): ApiResponse<T> {
        runCatching {
            block.invoke()
        }.onSuccess { data: T ->
            return handleHttpOk(data)
        }.onFailure { e ->
            return handleHttpError(e)
        }
        return ApiEmptyResponse()
    }

    /**
     * Non-background return errors, caught exceptions.
     *
     * @since 0.0.9
     */
    private fun <T:BaseApiResponse> handleHttpError(e: Throwable): ApiErrorResponse<T> {
        if (BuildConfig.DEBUG) e.printStackTrace()
        return ApiErrorResponse(e)
    }

    /**
     * Return 200, but also judge is success.
     *
     * @since 0.0.9
     */
    private fun <T:BaseApiResponse> handleHttpOk(data: T): ApiResponse<T> {
        return if (data.isSuccess()) {
            getHttpSuccessResponse(data)
        } else {
            ApiFailedResponse(data.getErrorCode(), data.getErrorMsg())
        }
    }

    /**
     * Deal with success and empty data.
     *
     * @since 0.0.9
     */
    private fun <T:BaseApiResponse> getHttpSuccessResponse(data: T): ApiResponse<T> {
        return if (data.isEmpty()) {
            ApiEmptyResponse()
        } else {
            ApiSuccessResponse(data)
        }
    }

}