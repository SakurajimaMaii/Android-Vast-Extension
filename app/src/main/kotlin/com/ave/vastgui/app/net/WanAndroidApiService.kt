/*
 * Copyright 2021-2024 VastGui
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ave.vastgui.app.net

import com.ave.vastgui.app.net.model.Articles
import com.ave.vastgui.tools.bean.UserBean
import com.ave.vastgui.tools.network.request.Request2
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST


// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2024/6/4
// Description: WanAndroid 开放 Api
// Reference: https://www.wanandroid.com/blog/show/2

interface WanAndroidApiService {

    @GET("/article/list/1/json")
    suspend fun getArticles(): Articles

    @POST("/user/login")
    suspend fun login(@Body userBean: UserBean): Request2<RequestBody>

}