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

package cn.govast.vasttools.lifecycle.base


// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/10/15
// Description: 
// Documentation:
// Reference:

sealed interface StateChange{
    fun changeCompletion()
    fun changeEmpty()
    fun changeError(t: Throwable?)
    fun changeFailed(errorCode: Int?, errorMsg: String?)
    fun changeStart()
    fun changeState(s: State)
    fun changeSuccess()
    fun clearState()
}