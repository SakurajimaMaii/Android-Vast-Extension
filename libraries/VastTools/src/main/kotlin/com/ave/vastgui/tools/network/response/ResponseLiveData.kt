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
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/9/28
// Documentation: https://ave.entropy2020.cn/documents/VastTools/core-topics/connectivity/performing-network-operations/response-livedata/

abstract class ResponseLiveData<T> : LiveData<T> {

    constructor() : super()

    /**
     * @since 0.3.0
     */
    constructor(value: T) : super(value)

    sealed class State {
        object Clear : State()
        object Empty : State()
        class Error(val t: Throwable?) : State()
        class Failed(val code: Int?, val message: String?) : State()
        object Success : State()
    }

    inner class StateListener {
        var onClear: () -> Unit = {}
        var onEmpty: () -> Unit = {}
        var onFailed: (errorCode: Int?, errorMsg: String?) -> Unit = { _, _ -> }
        var onError: (t: Throwable?) -> Unit = { }
        var onSuccess: () -> Unit = {}
    }

    inner class StateObserver : MutableLiveData<State>() {

        fun changeEmpty() {
            postValue(State.Empty)
        }

        fun changeError(t: Throwable?) {
            postValue(State.Error(t))
        }

        fun changeFailed(code: Int?, message: String?) {
            postValue(State.Failed(code, message))
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
         * @param listener The observer listener.
         */
        fun observeState(owner: LifecycleOwner, listener: StateListener.() -> Unit) {
            val mObserver =
                Observer<State> { state ->
                    val mListener = StateListener().also(listener)
                    when (state) {

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

                        is State.Clear -> {
                            mListener.onClear()
                        }
                    }
                }
            super.observe(owner, mObserver)
        }

    }

    private val state = StateObserver()

    protected fun getState() = state

    /**
     * Observe state.
     *
     * @param owner The LifecycleOwner which controls the observer
     * @param listener The listener that will receive the events.
     * @since 0.3.0
     */
    fun observeState(owner: LifecycleOwner, listener: StateListener.() -> Unit) {
        state.observeState(owner, listener)
    }

}