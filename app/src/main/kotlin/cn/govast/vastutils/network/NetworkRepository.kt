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

package cn.govast.vastutils.network

import android.annotation.SuppressLint
import cn.govast.vastutils.network.service.QRCodeKey
import cn.govast.vastutils.network.service.SongResult
import cn.govast.vastutils.network.service.UserService
import java.time.Instant

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/9/25
// Description: 
// Documentation:
// Reference:

class NetworkRepository() {

    private val networkRetrofitBuilder = NetworkRetrofitBuilder()

    private val service by lazy {
        networkRetrofitBuilder.create(UserService::class.java)
    }

    @SuppressLint("NewApi")
    suspend fun getQRCode(): QRCodeKey {
        return service.generateQRCode("${Instant.now().epochSecond}")
    }

    @SuppressLint("NewApi")
    suspend fun searchSong(song:String): SongResult {
        return service.searchSong(song,"${Instant.now().epochSecond}")
    }

}