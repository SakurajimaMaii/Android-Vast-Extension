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

package cn.govast.vasttools.lifecycle.base

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/10/15
// Description: 
// Documentation:
// Reference:

class StateObserver : MutableLiveData<State>(), StateChange {

    override fun changeCompletion() {
        postValue(State.Completion)
    }

    override fun changeEmpty() {
        postValue(State.Empty)
    }

    override fun changeError(t: Throwable?) {
        postValue(State.Error(t))
    }

    override fun changeFailed(errorCode: Int?, errorMsg: String?) {
        postValue(State.Failed(errorCode, errorMsg))
    }

    override fun changeStart() {
        postValue(State.Start)
    }

    override fun changeState(s: State) {
        postValue(s)
    }

    override fun changeSuccess() {
        postValue(State.Success)
    }

    override fun clearState() {
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
                        mListener.onFailed(state.errorCode, state.errorMsg)
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