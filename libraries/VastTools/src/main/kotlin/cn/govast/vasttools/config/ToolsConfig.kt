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

package cn.govast.vasttools.config

import android.app.Application
import android.os.Looper
import androidx.security.crypto.MasterKey
import cn.govast.vasttools.extension.NotNUllVar
import cn.govast.vasttools.helper.ContextHelper
import cn.govast.vasttools.utils.LogUtils


// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/8/28 10:42
// Description: 
// Documentation:

/** VastUtils config. */
object ToolsConfig {

    /** Is initialized */
    private var isInitialized by NotNUllVar<Boolean>(true)

    /**
     * Initialize the tools.
     *
     * @param application the application of your app.
     */
    @JvmStatic
    fun init(application: Application) {
        ContextHelper.init(application)
        LogUtils.init()
        isInitialized = true
    }

    @JvmStatic
    fun isMainThread() = Looper.getMainLooper() == Looper.myLooper()

    /**
     * Get master key.
     * The key scheme is [MasterKey.KeyScheme.AES256_GCM]
     */
    @JvmStatic
    fun getMasterKey() = if (isInitialized) MasterKey.Builder(ContextHelper.getAppContext())
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build() else throw RuntimeException("Please init ToolsConfig first.")

}