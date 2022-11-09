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

package cn.govast.vasttools.network.base

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/9/26
// Description: 
// Documentation:
// Reference:

@JvmDefaultWithCompatibility
interface BaseApiRsp {
    /**
     * @return True if the request was successful, false otherwise.
     */
    fun isSuccess(): Boolean = true

    /**
     * @return True if the request was successful but the data is empty, false
     *     otherwise.
     */
    fun isEmpty(): Boolean = false

    /**
     * @return Error code. null by default.
     */
    fun getErrorCode(): Int? = null

    /**
     * @return Error message. null by default.
     */
    fun getErrorMsg(): String? = null
}