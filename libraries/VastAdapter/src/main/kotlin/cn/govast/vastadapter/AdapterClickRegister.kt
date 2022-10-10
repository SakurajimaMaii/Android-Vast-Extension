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

package cn.govast.vastadapter


// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/10/10
// Description: 
// Documentation:
// Reference:

@JvmDefaultWithCompatibility
interface AdapterClickRegister {

    /**
     * Register a click listener for adapter.
     *
     * @param click a click listener.
     */
    fun registerClickEvent(l: AdapterClickListener?) {
        nothing_to_do()
    }

    /** @return a click listener for adapter. */
    fun getClickEvent(): AdapterClickListener?{
        TODO("getClickEvent is not implement.")
    }

    /**
     * Register a long click listener for adapter.
     *
     * @param click a long click listener.
     */
    fun registerLongClickEvent(l: AdapterLongClickListener?) {
        nothing_to_do()
    }

    /** @return a long click listener for adapter. */
    fun getLongClickEvent(): (AdapterLongClickListener)?{
        TODO("getLongClickEvent is not implement.")
    }

}