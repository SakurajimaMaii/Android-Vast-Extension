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

package com.ave.vastgui.tools.content

import android.content.Intent
import android.os.Build
import com.ave.vastgui.core.extension.cast
import java.io.Serializable

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/3/22
// Documentation: https://ave.entropy2020.cn/documents/VastTools/core-topics/intent/Intent/

/**
 * Retrieve extended data from the intent.
 *
 * @param key The name of the desired item.
 * @param callback As the return value if the retrieve extended data from
 *     the intent is null.
 * @see [Intent.getSerializableExtra]
 */
inline fun <reified T : Serializable> Intent.getSerializableExtra(key: String, callback: T): T {
    return getSerializableExtra(this, key) ?: callback
}

/** @see [Intent.getSerializableExtra]. */
inline fun <reified T : Serializable> getSerializableExtra(intent: Intent, key: String): T? {
    val data = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
        @Suppress("DEPRECATION")
        intent.getSerializableExtra(key)
    } else {
        intent.getSerializableExtra(key, T::class.java)
    }
    return if (null != data) {
        cast(data)
    } else null
}