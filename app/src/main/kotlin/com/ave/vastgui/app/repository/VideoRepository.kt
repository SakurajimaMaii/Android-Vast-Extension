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

package com.ave.vastgui.app.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.ave.vastgui.adapter.base.ItemWrapper
import com.ave.vastgui.app.R
import com.ave.vastgui.app.adapter.entity.Videos
import com.ave.vastgui.app.net.OpenApi
import com.ave.vastgui.app.net.OpenApiService
import com.ave.vastgui.tools.network.request.Request
import com.ave.vastgui.tools.network.request.create
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2024/1/6 16:44

/**
 * 将 [Request] 回调改为协程。
 */
private suspend fun Request<Videos>.await(): List<Videos.Data> = suspendCoroutine { coroutine ->
    request {
        onSuccess = { coroutine.resume(it.result.list) }
        onError = { coroutine.resumeWithException(it ?: RuntimeException("请求失败")) }
        onFailed = { code, message ->
            coroutine.resumeWithException(RuntimeException("请求失败 $code $message"))
        }
        onEmpty = { coroutine.resume(emptyList()) }
    }
}

class VideoRepository : PagingSource<Int, ItemWrapper<Videos.Data>>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ItemWrapper<Videos.Data>> {
        return try {
            val nextPage = params.key ?: 1

            val response: List<ItemWrapper<Videos.Data>> = OpenApi()
                .create(OpenApiService::class.java)
                .getHaoKanVideo(nextPage, 10)
                .await()
                .map { ItemWrapper(it, layoutId = R.layout.item_video) }

            LoadResult.Page(
                data = response,
                prevKey = if (nextPage == 1) null else nextPage - 1,
                nextKey = nextPage + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ItemWrapper<Videos.Data>>): Int? {
        return null
    }
}