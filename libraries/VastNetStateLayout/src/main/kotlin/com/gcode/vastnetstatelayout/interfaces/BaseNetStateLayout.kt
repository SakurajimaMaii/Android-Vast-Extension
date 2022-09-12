/*
 * Copyright 2022 VastGui
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gcode.vastnetstatelayout.interfaces

import com.gcode.vastnetstatelayout.view.VastNetStateMgr
import com.gcode.vastnetstatelayout.view.VastNetStateLayout

// Author: Vast Gui 
// Email: guihy2019@gmail.com
// Date: 2022/4/12 7:53
// Description:
// Documentation:

/**
 * Interface of [VastNetStateLayout].
 *
 * @since 0.0.4
 */
internal interface BaseNetStateLayout {

    fun setVastNetStateMgr(mgr: VastNetStateMgr)

    fun showLoading()
    fun showNetError()
    fun showLoadingError()
    fun showEmptyData()
    fun showSuccess()

}