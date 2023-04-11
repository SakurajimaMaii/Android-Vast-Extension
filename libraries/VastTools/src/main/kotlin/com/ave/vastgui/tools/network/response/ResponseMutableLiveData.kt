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

import androidx.annotation.MainThread

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/4/11
// Documentation: https://ave.entropy2020.cn/documents/VastTools/core-topics/connectivity/performing-network-operations/ResponseLiveData/

/**
 * Response mutable live data
 *
 * @since 0.3.0
 */
class ResponseMutableLiveData<T> : ResponseLiveData<T> {

    constructor() : super()

    constructor(value: T) : super(value)

    init {
        clearState()
    }

    fun clearState() {
        state.clearState()
    }

    fun changeState(s: State) {
        state.changeState(s)
    }

    fun postEmpty() {
        state.changeEmpty()
    }

    @JvmOverloads
    fun postError(t: Throwable? = null) {
        state.changeError(t)
    }

    @JvmOverloads
    fun postFailed(errorCode: Int? = null, errorMsg: String? = null) {
        state.changeFailed(errorCode, errorMsg)
    }

    fun postSuccess() {
        state.changeSuccess()
    }

    fun postValueAndSuccess(value: T) {
        super.postValue(value)
        state.changeSuccess()
    }

    @MainThread
    fun setValueAndSuccess(value: T){
        super.setValue(value)
        state.changeSuccess()
    }

    /**
     * @since 0.3.0
     */
    override fun postValue(value: T) {
        throw RuntimeException("Please call postValueAndSuccess.")
    }

    /**
     * @since 0.3.0
     */
    override fun setValue(value: T) {
        throw RuntimeException("Please call setValueAndSuccess.")
    }

}