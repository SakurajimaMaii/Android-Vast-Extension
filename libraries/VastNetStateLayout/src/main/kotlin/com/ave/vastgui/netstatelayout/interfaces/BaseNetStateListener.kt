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

package com.ave.vastgui.netstatelayout.interfaces

import android.view.View
import com.ave.vastgui.netstatelayout.view.CONTENT_STATE_SHOW_EMPTY_DATA
import com.ave.vastgui.netstatelayout.view.CONTENT_STATE_SHOW_LOADING
import com.ave.vastgui.netstatelayout.view.CONTENT_STATE_SHOW_LOADING_ERROR
import com.ave.vastgui.netstatelayout.view.CONTENT_STATE_SHOW_NET_ERROR

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/9/18 21:22

interface BaseNetStateListener {

    /**
     * Net state listener for the [CONTENT_STATE_SHOW_EMPTY_DATA] state.
     */
    fun onEmptyData(view: View)

    /**
     * Net state listener for the [CONTENT_STATE_SHOW_LOADING_ERROR] state.
     */
    fun onLoadingError(view: View)

    /**
     * Net state listener for the [CONTENT_STATE_SHOW_LOADING] state.
     */
    fun onLoading(view: View)

    /**
     * Net state listener for the [CONTENT_STATE_SHOW_NET_ERROR] state.
     */
    fun onNetWorkError(view: View)

}