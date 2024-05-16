/*
 * Copyright 2024 VastGui guihy2019@gmail.com
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

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.ave.vastgui.app.adapter.entity.Sentences
import com.ave.vastgui.app.net.OpenApi
import com.ave.vastgui.app.net.OpenApiService
import com.ave.vastgui.app.repository.VideoRepository
import com.ave.vastgui.tools.network.request.create
import com.ave.vastgui.tools.network.response.ResponseLiveData
import com.ave.vastgui.tools.network.response.ResponseMutableLiveData

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2024/1/2 20:57

class SharedVM : ViewModel() {

    /** 获取到的视频数据流。 */
    val videoFlow =
        Pager(PagingConfig(pageSize = 20)) { VideoRepository() }.flow.cachedIn(viewModelScope)

    private val _sentence = ResponseMutableLiveData<Sentences>()
    val sentence: ResponseLiveData<Sentences>
        get() = _sentence

    /** 手动设置状态。 */
    fun getSentenceWithHandle() {
        OpenApi().create(OpenApiService::class.java)
            .sentences()
            .request {
                onSuccess = { _sentence.postValueAndSuccess(it) }
                onError = { _sentence.postError(it) }
                onFailed = { code, message -> _sentence.postFailed(code, message) }
            }
    }

    /** 自动设置状态。 */
    fun getSentenceAutoHandle() {
        OpenApi().create(OpenApiService::class.java)
            .sentences()
            .request(_sentence)
    }

}