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

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import com.ave.vastgui.appcompose.ui.theme.AndroidVastExtensionTheme
import com.ave.vastgui.tools.network.request.Request2
import com.ave.vastgui.tools.network.request.getApi
import kotlinx.coroutines.launch
import retrofit2.http.GET
import retrofit2.http.Query

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/6/2
// Documentation: https://ave.entropy2020.cn/documents/VastTools/core-topics/connectivity/performing-network-operations/request2/

private data class Request2Response(
    val code: Int,
    val message: String,
    val result: Result
) {
    data class Result(
        val list: List<Data>,
        val total: Int
    ) {
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
}

private interface Request2Service {
    /**
     * [获取好看视频](https://api.apiopen.top/swagger/index.html#/%E5%BC%80%E6%94%BE%E6%8E%A5%E5%8F%A3/get_api_getHaoKanVideo)
     */
    @GET("/api/getHaoKanVideo")
    suspend fun getHaoKanVideo(
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Request2<Request2Response>
}

@Composable
fun Request2() {
    val scope = rememberCoroutineScope()
    var text by remember {
        mutableStateOf("点击数据请求数据")
    }

    Button(onClick = {
        scope.launch {
            NetRequestBuilder()
                .getApi(Request2Service::class.java) {
                    getHaoKanVideo(page = 0, size = 2)
                }.collect {
                    text = when (it) {
                        is Request2.Success -> it.data.result.list.get(0).title
                        is Request2.Empty -> "这是一条空数据"
                        is Request2.Exception -> "遇到异常 ${it.exception}"
                        is Request2.Failure -> "${it.code} ${it.message}"
                        else -> ""
                    }
                }
        }
    }) {
        Text(text = text)
    }
}

@Preview(showBackground = true)
@Composable
fun Request2Preview() {
    AndroidVastExtensionTheme {
        Request2()
    }
}