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

package com.ave.vastgui.app.network.service

import com.ave.vastgui.app.network.QRCodeKey
import com.ave.vastgui.tools.network.response.ResponseBuilder
import retrofit2.http.POST
import retrofit2.http.Query

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/6/7
// Description: 
// Documentation:
// Reference:

/**
 * The service when you use [ResponseBuilder] to request data.
 */
interface SuspendService {

    @POST("/login/qr/key")
    suspend fun generateQRCode(@Query("timestamp") timestamp: String): QRCodeKey

}