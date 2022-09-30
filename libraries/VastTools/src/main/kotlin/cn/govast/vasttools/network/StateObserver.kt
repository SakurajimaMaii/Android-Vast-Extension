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

import androidx.lifecycle.Observer
import cn.govast.vasttools.base.BaseApiRsp

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/9/28
// Description: 
// Documentation:
// Reference:

abstract class StateObserver<T: BaseApiRsp>:Observer<ApiRspWrapper<T>> {

    override fun onChanged(apiRspWrapper: ApiRspWrapper<T>?) {
        onStart()
        apiRspWrapper?.also {
            when(it){
                is ApiRspWrapper.ApiSuccessWrapper -> onSuccess(it.data)
                is ApiRspWrapper.ApiEmptyWrapper -> onEmpty()
                is ApiRspWrapper.ApiFailedWrapper -> onFailed(it.errorCode, it.errorMsg)
                is ApiRspWrapper.ApiErrorWrapper -> onError(it.throwable)
            }
        }
        onComplete()
    }

    abstract fun onStart()

    abstract fun onSuccess(data: T)

    abstract fun onEmpty()

    abstract fun onError(e: Throwable?)

    abstract fun onComplete()

    abstract fun onFailed(errorCode: Int?, errorMsg: String?)

}