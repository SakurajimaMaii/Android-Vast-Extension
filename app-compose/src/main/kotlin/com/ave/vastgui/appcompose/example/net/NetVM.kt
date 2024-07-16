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

package com.ave.vastgui.appcompose.example.net

import androidx.lifecycle.ViewModel
import com.ave.vastgui.tools.network.request.create
import com.ave.vastgui.tools.network.response.ResponseLiveData
import com.ave.vastgui.tools.network.response.ResponseMutableLiveData

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/2/22
// Documentation: https://ave.entropy2020.cn/documents/tools/core-topics/connectivity/performing-network-operations/response-livedata/

class NetVM : ViewModel() {

    private val _requestResponse = ResponseMutableLiveData<RequestResponse>()
    val requestResponse: ResponseLiveData<RequestResponse>
        get() = _requestResponse

    /**
     * 自动检测数据请求的状态 目前支持 Error Failed Successful Empty 四种状态
     *
     * @param timestamp 时间戳
     */
    fun autoRequest(page: Int, size: Int) {
        NetRequestBuilder().create(RequestService::class.java)
            .getHaoKanVideo(page = page, size = size)
            .request(_requestResponse)
    }

    /**
     * 手动检测数据请求的状态
     *
     * @param timestamp 时间戳
     */
    fun manualRequest(page: Int, size: Int) {
        NetRequestBuilder().create(RequestService::class.java)
            .getHaoKanVideo(page = page, size = size)
            .request {
                onSuccess = {
                    _requestResponse.postValueAndSuccess(it)
                }
                onFailed = { errorCode, errorMsg ->
                    _requestResponse.postFailed(errorCode, errorMsg)
                }
                onError = {
                    _requestResponse.postError(it)
                }
            }
    }

}