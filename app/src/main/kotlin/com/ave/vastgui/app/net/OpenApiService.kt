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

package com.ave.vastgui.app.net

import com.ave.vastgui.app.adapter.entity.Images
import com.ave.vastgui.app.adapter.entity.Sentences
import com.ave.vastgui.app.adapter.entity.Videos
import com.ave.vastgui.tools.network.request.Request
import retrofit2.http.GET
import retrofit2.http.Query

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2024/1/2

interface OpenApiService {
    /**
     * 获取图片。
     *
     * @param page 页码。
     * @param size 总数。
     */
    @GET("/api/getImages")
    suspend fun getImages(@Query("page") page: Int, @Query("size") size: Int): Images

    /**
     * 获取好看的视频。
     *
     * @param page 页码。
     * @param size 总数。
     */
    @GET("/api/getHaoKanVideo")
    fun getHaoKanVideo(@Query("page") page: Int, @Query("size") size: Int): Request<Videos>

    /**
     * 获取一句名言。
     */
    @GET("/api/sentences")
    fun sentences(): Request<Sentences>
}