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

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/9/28
// Description: 
// Documentation:
// Reference: https://juejin.cn/post/6844903855675670535

class ResponseLiveData<T> : MutableLiveData<T>() {

    sealed class State {
        object Clear : State()
        object Completion : State()
        object Empty : State()
        class Error(val t: Throwable?) : State()
        class Failed(val code: Int?, val message: String?) : State()
        object Start : State()
        object Success : State()
    }

    inner class StateListener {
        var onClear: () -> Unit = {}
        var onEmpty: () -> Unit = {}
        var onFailed: (errorCode: Int?, errorMsg: String?) -> Unit = { _, _ -> }
        var onError: (t: Throwable?) -> Unit = { }
        var onStart: () -> Unit = {}
        var onCompletion: () -> Unit = {}
        var onSuccess: () -> Unit = {}
    }

    inner class StateObserver : MutableLiveData<State>() {

        fun changeCompletion() {
            postValue(State.Completion)
        }

        fun changeEmpty() {
            postValue(State.Empty)
        }

        fun changeError(t: Throwable?) {
            postValue(State.Error(t))
        }

        fun changeFailed(code: Int?, message: String?) {
            postValue(State.Failed(code, message))
        }

        fun changeStart() {
            postValue(State.Start)
        }

        fun changeState(s: State) {
            postValue(s)
        }

        fun changeSuccess() {
            postValue(State.Success)
        }

        fun clearState() {
            postValue(State.Clear)
        }

        /**
         * Add a state observer for [StateObserver].
         *
         * @param owner The LifecycleOwner which controls the observer.
         * @param listener The observer listener. By default, you need to set the
         *     **Start**, **Completion** state by yourself.
         */
        fun observeState(owner: LifecycleOwner, listener: StateListener.() -> Unit) {
            val mObserver =
                Observer<State> { state ->
                    val mListener = StateListener().also(listener)
                    when (state) {
                        is State.Start -> {
                            mListener.onStart()
                        }

                        is State.Success -> {
                            mListener.onSuccess()
                        }

                        is State.Error -> {
                            mListener.onError(state.t)
                        }

                        is State.Failed -> {
                            mListener.onFailed(state.code, state.message)
                        }

                        is State.Empty -> {
                            mListener.onEmpty()
                        }

                        is State.Completion -> {
                            mListener.onCompletion()
                        }

                        is State.Clear -> {
                            mListener.onClear()
                        }
                    }
                }
            super.observe(owner, mObserver)
        }

    }

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
        state.changeSuccess()
    }

}