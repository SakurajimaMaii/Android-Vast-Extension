/*
 * Copyright 2024 VastGui guihy2019@gmail.com
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
import com.ave.vastgui.tools.network.response.ResponseMutableLiveData
import com.ave.vastgui.tools.network.response.ResponseStateListener

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/10/9
// Documentation: https://ave.entropy2020.cn/documents/VastTools/core-topics/connectivity/performing-network-operations/request/

interface Request<T : ResponseApi> {

    fun cancel()

    fun request(listener: ResponseStateListener<T>.() -> Unit)

    fun request(stateLiveData: ResponseMutableLiveData<T>)

    fun clone(): Request<T>

}