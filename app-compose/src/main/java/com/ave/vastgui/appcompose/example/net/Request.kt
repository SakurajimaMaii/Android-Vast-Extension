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

import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ave.vastgui.appcompose.ui.theme.AndroidVastExtensionTheme
import com.ave.vastgui.tools.network.request.Request
import com.ave.vastgui.tools.network.request.create
import com.ave.vastgui.tools.network.response.ResponseApi
import retrofit2.http.GET
import retrofit2.http.Query

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/7/3
// Description: 
// Documentation:
// Reference:

data class RequestResponse(
    val code: Int,
    val message: String,
    val result: Result
) : ResponseApi {
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

    override fun isSuccessful() = code == 200

    override fun code() = code

    override fun message() = message
}

interface RequestService {
    /**
     * [获取好看视频](https://api.apiopen.top/swagger/index.html#/%E5%BC%80%E6%94%BE%E6%8E%A5%E5%8F%A3/get_api_getHaoKanVideo)
     */
    @GET("/api/getHaoKanVideo")
    fun getHaoKanVideo(
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Request<RequestResponse>
}

@Composable
fun Request() {
    var text by remember {
        mutableStateOf("点击数据请求数据")
    }

    Button(onClick = {
        NetRequestBuilder().create(RequestService::class.java)
            .getHaoKanVideo(page = 0, size = 2).request {
                onSuccess = {
                    text = it.result.list[0].title
                }

                onError = {
                    text = "遇到异常 ${it?.message}"
                }

                onFailed = { code, message ->
                    text = "请求失败 $code $message"
                }
            }
    }) {
        Text(text = text)
    }
}

@Composable
fun RequestWithVM(netVM: NetVM = viewModel()) {
    val text by netVM.requestResponse.observeAsState()

    Button(
        onClick = {
            netVM.getRequestResponse_1(page = 0, size = 2)
        },
        modifier = Modifier.wrapContentHeight()
    ) {
        Text(text = text?.result?.list?.get(0)?.title ?: "点击数据请求数据")
    }
}

@Preview(showBackground = true)
@Composable
fun RequestPreview() {
    AndroidVastExtensionTheme {
        Request()
    }
}