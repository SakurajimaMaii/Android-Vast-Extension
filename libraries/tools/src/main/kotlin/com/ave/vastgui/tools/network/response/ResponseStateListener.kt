/*
 * Copyright 2021-2024 VastGui
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

import com.ave.vastgui.tools.network.request.Request
import retrofit2.Response

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/10/15

class ResponseStateListener<T> {

    /** A http response empty listener. */
    var onEmpty: () -> Unit = {}

    /**
     * A http response failed listener.
     *
     * Using with [Request]: When the [Response] returns false, errorCode
     * and errorMsg will return the code and message carried by the response
     * itself. When the [Response] returns true but [ResponseApi.isSuccessful]
     * return false, errorCode and errorMsg will return the code and
     * message carried by [ResponseApi.code] and [ResponseApi.message].
     *
     * Using with [ResponseBuilder]: errorCode and errorMsg will
     * return the code and message carried by [ResponseApi.code] and
     * [ResponseApi.message].
     *
     * @see Request
     * @see ResponseApi
     * @see ResponseBuilder
     */
    var onFailed: (errorCode: Int?, errorMsg: String?) -> Unit = { _, _ -> }

    /** A http response error listener. */
    var onError: (t: Throwable?) -> Unit = { }

    /** A http response success listener. */
    var onSuccess: (data: T) -> Unit = {}

}