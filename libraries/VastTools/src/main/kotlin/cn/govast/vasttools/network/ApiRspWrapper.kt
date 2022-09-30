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

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/9/25
// Description: 
// Documentation:
// Reference: https://juejin.cn/post/6993294489125126151

sealed class ApiRspWrapper<T : BaseApiRsp> {
    /**
     * Success response wrapper.
     *
     * @param T json bean class.
     * @param data api response item data.
     * @since 0.0.9
     */
    class ApiSuccessWrapper<T : BaseApiRsp>(val data: T) : ApiRspWrapper<T>()

    /**
     * Empty response wrapper.
     *
     * @since 0.0.9
     */
    class ApiEmptyWrapper<T : BaseApiRsp>() : ApiRspWrapper<T>()

    /**
     * Fail response wrapper.
     *
     * @param errorCode get by [BaseApiRsp.getErrorCode] of the api
     *     response item.
     * @param errorMsg get by [BaseApiRsp.getErrorMsg] of the api response
     *     item.
     * @since 0.0.9
     */
    class ApiFailedWrapper<T : BaseApiRsp>(val errorCode: Int?, val errorMsg: String?) :
        ApiRspWrapper<T>()

    /**
     * Error response wrapper.
     *
     * @param throwable get from [BaseRepository.handleHttpError]
     * @since 0.0.9
     */
    class ApiErrorWrapper<T : BaseApiRsp>(val throwable: Throwable?) : ApiRspWrapper<T>()
}