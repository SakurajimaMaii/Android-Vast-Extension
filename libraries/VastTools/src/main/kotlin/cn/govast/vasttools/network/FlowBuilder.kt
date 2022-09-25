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

import androidx.lifecycle.lifecycleScope
import cn.govast.vasttools.base.BaseLifecycleOwner
import cn.govast.vasttools.extension.NotNUllSingleVar
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/9/26
// Description: 
// Documentation:
// Reference:

class FlowBuilder<T : BaseApiResponse>(private val lifecycleOwner: BaseLifecycleOwner) {

    private var request: suspend () -> ApiResponse<T> by NotNUllSingleVar()
    private var success: (data: T?) -> Unit = {}
    private var dataEmpty: () -> Unit = {}
    private var failed: (errorCode: Int?, errorMsg: String?) -> Unit = { _, _ -> }
    private var error: (e: Throwable?) -> Unit = { }
    private var complete: () -> Unit = {}

    fun initRequest(request: suspend () -> ApiResponse<T>) = apply {
        this.request = request
    }

    fun onSuccess(onSuccess: (data: T?) -> Unit) = apply {
        this.success = onSuccess
    }

    fun onDataEmpty(onDataEmpty: () -> Unit) = apply {
        this.dataEmpty = onDataEmpty
    }

    fun onFailed(onFailed: (errorCode: Int?, errorMsg: String?) -> Unit) = apply {
        this.failed = onFailed
    }

    fun onError(onError: (e: Throwable?) -> Unit) = apply {
        this.error = onError
    }

    fun onComplete(onComplete: () -> Unit) = apply {
        this.complete = onComplete
    }

    fun launchFlow() {
        lifecycleOwner.lifecycleScope.launch {
            flow {
                emit(request())
            }.onStart {
                lifecycleOwner.showLoading()
            }.onCompletion {
                lifecycleOwner.dismissLoading()
                complete.invoke()
            }.collect { response ->
                when (response) {
                    is ApiSuccessResponse -> success.invoke(response.data)
                    is ApiEmptyResponse -> dataEmpty.invoke()
                    is ApiFailedResponse -> failed.invoke(response.errorCode, response.errorMsg)
                    is ApiErrorResponse -> error.invoke(response.error)
                }
            }
        }
    }

}