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

package cn.govast.vastutils.viewModel

import android.annotation.SuppressLint
import cn.govast.vasttools.livedata.NetStateLiveData
import cn.govast.vasttools.viewModel.VastViewModel
import cn.govast.vastutils.network.NetworkRepository
import cn.govast.vastutils.network.service.QRCodeKey
import cn.govast.vastutils.network.service.SongResult

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/8/28 12:34
// Description: 
// Documentation:

class BasicViewModel(
    private val networkRepository: NetworkRepository
) : VastViewModel() {

    @SuppressLint("NewApi")
    suspend fun getQRCode(): QRCodeKey {
        return networkRepository.getQRCode()
    }

    val songResult = NetStateLiveData<SongResult>()

    @SuppressLint("NewApi")
    fun searchSong(song: String) {
        networkRepository.searchSong(song).request(songResult)
    }

}