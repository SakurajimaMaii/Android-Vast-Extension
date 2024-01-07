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

package com.ave.vastgui.tools.network.response

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/2/20
// Documentation: https://ave.entropy2020.cn/documents/VastTools/core-topics/connectivity/performing-network-operations/response-api/

@JvmDefaultWithCompatibility
interface ResponseApi {

    /**
     * @return true when the response is successful, false otherwise.
     */
    fun isSuccessful(): Boolean = true

    /**
     * @return true when the response is successful but data is empty, false otherwise.
     */
    fun isEmpty(): Boolean = false

    /**
     * @return status code, by default is null.
     */
    fun code(): Int? = null

    /**
     * @return message, by default is null.
     */
    fun message(): String? = null

}