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

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/9/25

open class ResponseWrapper<T> {
    /**
     * Success response.
     *
     * @param T json bean class.
     * @param data api response item data.
     */
    class SuccessResponseWrapper<T>(val data: T) : ResponseWrapper<T>()

    /** Empty response. */
    class EmptyResponseWrapper<T> : ResponseWrapper<T>()

    /**
     * Fail response.
     *
     * @param errorCode get by [BaseApiRspWrapper.getErrorCode] of the api
     *     response item.
     * @param errorMsg get by [BaseApiRspWrapper.getErrorMsg] of the api
     *     response item.
     */
    class FailedResponseWrapper<T>(val errorCode: Int?, val errorMsg: String?) :
        ResponseWrapper<T>()

    /** Error response. */
    class ErrorResponseWrapper<T>(val throwable: Throwable?) : ResponseWrapper<T>()
}