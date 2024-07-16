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

package com.ave.vastgui.appcompose.example.net

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Chip
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.ave.vastgui.appcompose.R
import com.ave.vastgui.core.extension.nothing_to_do
import com.ave.vastgui.tools.network.request.Request2
import com.ave.vastgui.tools.network.request.create
import retrofit2.http.GET
import retrofit2.http.Query

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/6/2
// Documentation: https://ave.entropy2020.cn/documents/tools/core-topics/connectivity/performing-network-operations/request2/

/**
 * 通过 Api 请求到的好看视频测试数据。
 *
 * @property code 返回代码。
 * @property message 返回信息。
 * @property result 包含请求到的好看视频列表。
 */
data class Videos(val code: Int, val message: String, val result: Result) {
    data class Result(val list: List<Data>, val total: Int)

    data class Data(
        val coverUrl: String,
        val duration: String,
        val id: Int,
        val playUrl: String,
        val title: String,
        val userName: String,
        val userPic: String
    )
}

interface OpenApiService {
    /**
     * [获取好看视频](https://api.apiopen.top/swagger/index.html#/%E5%BC%80%E6%94%BE%E6%8E%A5%E5%8F%A3/get_api_getHaoKanVideo)
     */
    @GET("/api/getHaoKanVideo")
    suspend fun getHaoKanVideo(@Query("page") page: Int, @Query("size") size: Int): Request2<Videos>
}

class DataSource : PagingSource<Int, Videos.Data>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Videos.Data> {
        return try {
            val nextPage = params.key ?: 1

            val response: Request2<Videos> = NetRequestBuilder()
                .create(OpenApiService::class.java)
                .getHaoKanVideo(nextPage, 10)

            LoadResult.Page(
                data = if (response is Request2.Success) {
                    response.data.result.list
                } else emptyList(),
                prevKey = if (nextPage == 1) null else nextPage - 1,
                nextKey = nextPage + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Videos.Data>): Int? {
        return null
    }
}

class NetViewModel : ViewModel() {
    val pagingData = Pager(PagingConfig(pageSize = 20)) { DataSource() }.flow
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Request2(viewModel: NetViewModel = viewModel()) {
    val videos: LazyPagingItems<Videos.Data> =
        viewModel.pagingData.collectAsLazyPagingItems()
    val pullRefreshState =
        rememberPullRefreshState(videos.loadState.refresh is LoadState.Loading, { videos.refresh() })
    Box(
        modifier = Modifier
            .fillMaxSize()
            .pullRefresh(pullRefreshState)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(videos.itemCount) {
                val video = videos[it] ?: return@items
                VideoCard(video)
            }
        }

        PullRefreshIndicator(
            videos.loadState.refresh is LoadState.Loading,
            pullRefreshState,
            Modifier.align(Alignment.TopCenter)
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun VideoCard(data: Videos.Data) {
    Box(modifier = Modifier.padding(10.dp)) {
        AsyncImage(
            model = data.coverUrl,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .clip(RoundedCornerShape(20.dp)),
            contentScale = ContentScale.FillBounds
        )
        Chip(
            modifier = Modifier
                .wrapContentSize()
                .align(Alignment.BottomEnd)
                .padding(bottom = 5.dp, end = 10.dp),
            onClick = { nothing_to_do() },
            leadingIcon = {
                Icon(painterResource(id = R.drawable.ic_schedule_24px), contentDescription = null)
            },
            shape = MaterialTheme.shapes.small.copy(CornerSize(percent = 25))
        ) {
            Text(data.duration)
        }
    }
}