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

package cn.govast.vasttools.lifecycle

import androidx.lifecycle.MutableLiveData

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/9/28
// Description: 
// Documentation:
// Reference: https://juejin.cn/post/6844903855675670535

open class StateLiveData<T> : MutableLiveData<T>() {

    sealed class State {
        object Idle : State()
        object Loading : State()
        object Success : State()
        object Error : State()
        object Empty : State()
        object Start : State()
        object Failed : State()
        object Completion : State()
    }

    val state = MutableLiveData<State>()

    init {
        clearState()
    }

    fun postValueAndSuccess(value: T) {
        super.postValue(value)
        postSuccess()
    }

    fun clearState() {
        state.postValue(State.Idle)
    }

    open fun postLoading() {
        state.postValue(State.Loading)
    }

    fun postSuccess() {
        state.postValue(State.Success)
    }

    open fun postError() {
        state.postValue(State.Error)
    }

    fun changeState(s: State) {
        state.postValue(s)
    }

}