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

package com.ave.vastgui.adapter.widget


// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/10/10
// Description: 
// Documentation:
// Reference:

interface AdapterClickRegister {

    /**
     * Register a click listener for adapter.
     *
     * @param l a click listener.
     */
    fun registerClickEvent(l: AdapterClickListener?) {
        return
    }

    /** @return a click listener for adapter. */
    fun getClickEvent(): AdapterClickListener?{
        return null
    }

    /**
     * Register a long click listener for adapter.
     *
     * @param l a long click listener.
     */
    fun registerLongClickEvent(l: AdapterLongClickListener?) {
        return
    }

    /** @return a long click listener for adapter. */
    fun getLongClickEvent(): (AdapterLongClickListener)?{
        return null
    }

}