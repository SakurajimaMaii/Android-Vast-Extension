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

package com.ave.vastgui.tools.service

import android.content.Context
import androidx.lifecycle.LifecycleService
import com.ave.vastgui.core.extension.NotNUllVar
import com.ave.vastgui.tools.coroutines.createMainScope
import com.ave.vastgui.tools.network.response.getResponseBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/9/27
// Documentation: https://ave.entropy2020.cn/documents/VastTools/core-topics/service/Service/

abstract class VastService : LifecycleService() {

    protected var mMainScope:CoroutineScope by NotNUllVar()

    protected fun getContext(): Context {
        return this
    }

    protected fun getDefaultTag(): String = this::class.java.simpleName

    protected fun getRequestBuilder() = mMainScope.getResponseBuilder()

    override fun onCreate() {
        mMainScope = createMainScope(getDefaultTag())
        super.onCreate()
    }

    override fun onDestroy() {
        mMainScope.cancel()
        super.onDestroy()
    }

}