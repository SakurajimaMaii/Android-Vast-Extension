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
import com.ave.vastgui.app.adapter.entity.Images
import com.ave.vastgui.app.net.OpenApi
import com.ave.vastgui.app.net.OpenApiService
import com.ave.vastgui.tools.network.request.create

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2024/1/6 16:46

class ImageRepository : PagingSource<Int, Images.Image>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Images.Image> {
        return try {
            val nextPage = params.key ?: 1

            val response: List<Images.Image> = OpenApi()
                .create(OpenApiService::class.java)
                .getImages(nextPage, 10)
                .result?.list ?: emptyList()

            LoadResult.Page(
                data = response,
                prevKey = if (nextPage == 1) null else nextPage - 1,
                nextKey = nextPage + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Images.Image>): Int? {
        return null
    }
}