/*
 * Copyright 2024 VastGui
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

package com.ave.vastgui.tools.log.base

import android.content.ComponentCallbacks2
import com.ave.vastgui.tools.content.ContextHelper
import com.log.vastgui.core.base.LogInfo
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlin.coroutines.CoroutineContext

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2024/5/18 22:47
// Documentation: https://ave.entropy2020.cn/documents/VastTools/log

/**
 * Provide a [CoroutineScope] for log use.
 *
 * @since 1.3.1
 */
open class LogScope internal constructor(){

    /** @since 1.3.1 */
    private val mHandler = CoroutineExceptionHandler { context, exception ->
        handleCoroutineExceptionHandler(context, exception)
    }

    /** @since 1.3.1 */
    protected val mLogScope: CoroutineScope =
        CoroutineScope(SupervisorJob() + Dispatchers.IO + CoroutineName("LogScope") + mHandler)

    /**
     * A channel of [LogInfo].
     *
     * @since 1.3.1
     */
    protected val mLogChannel: Channel<LogInfo> = Channel()

    /** @since 1.3.1 */
    protected open fun handleCoroutineExceptionHandler(
        context: CoroutineContext,
        exception: Throwable
    ) {
        TODO("Please return a CoroutineExceptionHandler.")
    }

    init {
        ContextHelper.getApp().registerComponentCallbacks(object : ComponentCallbacks2 {
            override fun onConfigurationChanged(newConfig: android.content.res.Configuration) {

            }

            override fun onLowMemory() {
                mLogScope.cancel("Cancel mLogScope onLowMemory.")
            }

            override fun onTrimMemory(level: Int) {

            }
        })
    }

}