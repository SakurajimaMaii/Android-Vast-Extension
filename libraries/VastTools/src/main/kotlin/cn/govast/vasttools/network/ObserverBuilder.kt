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

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.observers.DisposableObserver
import io.reactivex.rxjava3.schedulers.Schedulers


// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/9/26
// Description: 
// Documentation:
// Reference:

open class ObserverBuilder<T : BaseApiResponse> :
    BaseNetState<T>, DisposableObserver<T>() {

    companion object{
        @JvmStatic
        fun <T : BaseApiResponse> applySchedulers(): ObservableTransformer<T, T> {
            return ObservableTransformer<T, T> {
                it.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
            }
        }
    }

    private var start: () -> Unit = {}
    private var success: (data: T?) -> Unit = {}
    private var empty: () -> Unit = {}
    private var failed: (errorCode: Int?, errorMsg: String?) -> Unit = { _, _ -> }
    private var error: (e: Throwable?) -> Unit = { }
    private var complete: () -> Unit = {}

    override fun onStart() {
        start.invoke()
    }

    override fun onComplete() {
        complete.invoke()
    }

    override fun onError(e: Throwable) {
        error.invoke(e)
    }

    override fun onNext(t: T) {
        if (t.isSuccess()) {
            success.invoke(t)
        } else {
            failed.invoke(t.getErrorCode(), t.getErrorMsg())
        }
    }

    override fun onStartState(onStart: () -> Unit) = apply {
        start = onStart
    }

    override fun onEmptyState(onEmpty: () -> Unit) = apply {
        empty = onEmpty
    }

    override fun onFailedState(onFailed: (errorCode: Int?, errorMsg: String?) -> Unit) = apply {
        failed = onFailed
    }

    override fun onErrorState(onError: (e: Throwable?) -> Unit) = apply {
        error = onError
    }

    override fun onCompleteState(onComplete: () -> Unit) = apply {
        complete = onComplete
    }

    override fun onSuccessState(onSuccess: (data: T?) -> Unit) = apply {
        success = onSuccess
    }

}