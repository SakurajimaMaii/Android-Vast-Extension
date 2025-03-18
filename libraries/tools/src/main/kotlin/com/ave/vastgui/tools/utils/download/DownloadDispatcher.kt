/*
 * Copyright 2021-2025 VastGui
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

package com.ave.vastgui.tools.utils.download

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2025/3/19
// Documentation: 
// Reference:

class DownloadDispatcher internal constructor() : DownloadEventOwner {

    private val _beans: MutableList<DownloadBean> = ArrayList()
    val beans: List<DownloadBean>
        get() = _beans

    fun addBean(bean: DownloadBean) {
        _beans.add(bean)
    }

    override fun start() {
        _beans.forEach { bean -> bean.onStart() }
    }

    override fun pause() {
        _beans.forEach { bean -> bean.onPause() }
    }

    override fun resume() {
        _beans.forEach { bean -> bean.onResume() }
    }

    override fun cancel() {
        _beans.forEach { bean -> bean.onCancel() }
    }

}