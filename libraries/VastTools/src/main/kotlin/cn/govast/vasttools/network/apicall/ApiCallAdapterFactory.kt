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

package cn.govast.vasttools.network.apicall

import cn.govast.vasttools.network.base.BaseApiRsp
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/10/9
// Description: 
// Documentation:
// Reference:

class ApiCallAdapterFactory : CallAdapter.Factory() {
    override fun get(returnType: Type, annotations: Array<Annotation>, retrofit: Retrofit): CallAdapter<*, *>? {
        if (getRawType(returnType) != ApiCall::class.java) return null
        check(returnType is ParameterizedType) {
            "MyCall must have generic type (e.g., MyCall<ResponseBody>)"
        }
        val responseType = getParameterUpperBound(0, returnType)
        val executor = retrofit.callbackExecutor()
        return ApiCallAdapter<BaseApiRsp>(responseType, executor)
    }
}