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

import kotlinx.coroutines.flow.Flow
import retrofit2.Response

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/10/15
// Description: 
// Documentation:
// Reference:

class ResponseStateListener<T> {
    /**
     * A http response empty listener. It is called when [Response.body] is
     * null.
     */
    var onEmpty: () -> Unit = {}

    /**
     * A http response failed listener. It is called when
     * [ResponseApi.isSuccessful] return false.
     */
    var onFailed: (errorCode: Int?, errorMsg: String?) -> Unit = { _, _ -> }

    /**
     * A http response error listener. It is called in
     * [retrofit2.Callback.onFailure].
     */
    var onError: (t: Throwable?) -> Unit = { }

    /**
     * A http response success listener. It is called in
     * [Response.isSuccessful] return true.
     */
    var onSuccess: (data: T) -> Unit = {}

    /** A listener that is called when [Flow] starts to be collected. */
    var onStart: () -> Unit = {}

    /** A listener that is called when [Flow] is completed. */
    var onCompletion: (t: Throwable?) -> Unit = {}
}