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

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/9/25
// Description: 
// Documentation:
// Reference: https://juejin.cn/post/6993294489125126151

open class ApiResponse<T : BaseApiResponse>(
    open val data: T? = null,
    open val errorCode: Int? = null,
    open val errorMsg: String? = null,
    open val error: Throwable? = null,
)

class ApiSuccessResponse<T : BaseApiResponse>(private val response: T) : ApiResponse<T>(data = response)
class ApiEmptyResponse<T : BaseApiResponse> : ApiResponse<T>()
class ApiFailedResponse<T : BaseApiResponse>(
    override val errorCode: Int?,
    override val errorMsg: String?
) : ApiResponse<T>(errorCode = errorCode, errorMsg = errorMsg)
class ApiErrorResponse<T : BaseApiResponse>(val throwable: Throwable?) :
    ApiResponse<T>(error = throwable)