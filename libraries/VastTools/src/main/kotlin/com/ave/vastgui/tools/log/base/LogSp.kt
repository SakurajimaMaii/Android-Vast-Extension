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

package com.ave.vastgui.tools.log.base

import com.ave.vastgui.tools.log.LogUtil
import com.ave.vastgui.tools.sharedpreferences.SpEncrypted
import com.ave.vastgui.tools.sharedpreferences.string

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/9/9
// Documentation: https://ave.entropy2020.cn/documents/VastTools/log/description/

/**
 * Log SharedPreferences.
 *
 * @since 0.5.3
 */
class LogSp internal constructor() {

    companion object {
        internal const val DEFAULT_FILE_NAME = ""
    }

    private val mSp by lazy {
        SpEncrypted.getInstance(LogUtil::class.java.name).getSharedPreferences()
    }

    internal var mCurrentFileName by mSp.string(DEFAULT_FILE_NAME)

}