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

package com.ave.vastgui.app.viewmodel

import com.ave.vastgui.app.network.MyRequestBuilder
import com.ave.vastgui.app.network.QRCodeKey
import com.ave.vastgui.app.network.service.RequestService
import com.ave.vastgui.app.network.service.SuspendService
import com.ave.vastgui.tools.lifecycle.viewModel.VastViewModel
import com.ave.vastgui.tools.network.response.ResponseLiveData
import com.ave.vastgui.tools.network.response.ResponseMutableLiveData


// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/2/22
// Description: 
// Documentation:
// Reference:

class NetVM : VastViewModel() {

    private val _qRCodeKey = ResponseMutableLiveData<QRCodeKey>()
    val qRCodeKey: ResponseLiveData<QRCodeKey>
        get() = _qRCodeKey

    /**
     * 自动检测数据请求的状态 目前支持 Error Failed Successful Empty 四种状态
     *
     * @param timestamp 时间戳
     */
    fun getQRCode_1(timestamp: String) {
        MyRequestBuilder().create(RequestService::class.java).generateQRCode(timestamp)
            .request(_qRCodeKey)
    }

    /**
     * 手动检测数据请求的状态
     *
     * @param timestamp 时间戳
     */
    fun getQRCode_2(timestamp: String) {
        MyRequestBuilder().create(RequestService::class.java).generateQRCode(timestamp)
            .request {
                onSuccess = {
                    _qRCodeKey.postValueAndSuccess(it)
                }
                onFailed = { errorCode, errorMsg ->
                    _qRCodeKey.postFailed(errorCode, errorMsg)
                }
                onError = {
                    _qRCodeKey.postError(it)
                }
            }
    }

    /**
     * 自动检测数据请求的状态 目前支持 Error Failed Successful Empty 四种状态
     *
     * @param timestamp 时间戳
     */
    fun getQRCode_3(timestamp: String) {
        getRequestBuilder()
            .suspendWithListener({
                MyRequestBuilder().create(SuspendService::class.java).generateQRCode(timestamp)
            }, _qRCodeKey)
    }

}