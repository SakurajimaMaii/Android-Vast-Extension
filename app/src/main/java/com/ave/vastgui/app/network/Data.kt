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

package com.ave.vastgui.app.network

import com.ave.vastgui.tools.network.response.ResponseApi

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/9/24
// Description: 
// Documentation:

data class QRCodeKey(
    val code: Int,
    val data: Data?
) : ResponseApi {

    data class Data(
        val code: Int,
        val unikey: String
    )

    override fun isSuccessful(): Boolean {
        return code == 200
    }

    override fun isEmpty(): Boolean {
        return data == null
    }

    override fun code(): Int? {
        return data?.code
    }

    override fun message(): String? {
        return data?.unikey
    }

}

data class QRCodeKey2(
    val code: Int,
    val data: Data?
) {

    data class Data(
        val code: Int,
        val unikey: String
    )

}