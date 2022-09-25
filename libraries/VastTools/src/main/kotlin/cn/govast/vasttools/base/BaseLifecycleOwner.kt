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

package cn.govast.vasttools.base

import androidx.lifecycle.LifecycleOwner
import cn.govast.vasttools.network.BaseApiResponse
import cn.govast.vasttools.network.FlowBuilder
import cn.govast.vasttools.nothing_to_do

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/9/26
// Description: 
// Documentation:
// Reference:

interface BaseLifecycleOwner:LifecycleOwner {

    /**
     * Do something when data loading.
     *
     * @since 0.0.9
     */
    fun showLoading(){
        nothing_to_do()
    }

    /**
     * Do something when dismiss loading.
     *
     * @since 0.0.9
     */
    fun dismissLoading(){
        nothing_to_do()
    }

    /**
     * Construct a network request.
     *
     * @param lifecycleOwner the object that will initiate the request.
     * @param T type of the result object class.
     * @since 0.0.9
     */
    fun <T:BaseApiResponse> getFlowBuilder(lifecycleOwner: BaseLifecycleOwner) = FlowBuilder<T>(lifecycleOwner)

}