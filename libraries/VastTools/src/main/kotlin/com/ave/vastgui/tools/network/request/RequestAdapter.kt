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

package com.ave.vastgui.tools.network.request

import com.ave.vastgui.tools.network.response.ResponseApi
import retrofit2.Call
import retrofit2.CallAdapter
import java.lang.reflect.Type
import java.util.concurrent.Executor

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/10/9
// Description: 
// Documentation:
// Reference:

class RequestAdapter<T : ResponseApi>(
    private val responseType: Type,
    private val executor: Executor?
) :
    CallAdapter<T, Request<T>> {

    override fun adapt(call: Call<T>): Request<T> {
        return RequestImpl(call, executor)
    }

    override fun responseType(): Type {
        return responseType
    }

}