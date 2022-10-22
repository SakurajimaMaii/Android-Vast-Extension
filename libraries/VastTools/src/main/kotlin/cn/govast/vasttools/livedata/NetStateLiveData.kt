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

package cn.govast.vasttools.livedata

import androidx.lifecycle.MutableLiveData
import cn.govast.vasttools.livedata.base.State
import cn.govast.vasttools.livedata.base.StateObserver

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/9/28
// Description: 
// Documentation:
// Reference: https://juejin.cn/post/6844903855675670535

open class NetStateLiveData<T> : MutableLiveData<T>() {

    private val state = StateObserver()

    init {
        clearState()
    }

    fun getState() = state

    fun clearState() {
        state.clearState()
    }

    fun changeState(s: State) {
        state.changeState(s)
    }

    fun postCompletion() {
        state.changeCompletion()
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

    fun postStart() {
        state.changeStart()
    }

    fun postSuccess() {
        state.changeSuccess()
    }

    fun postValueAndSuccess(value: T) {
        super.postValue(value)
        postSuccess()
    }

}