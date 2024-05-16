/*
 * Copyright 2024 VastGui guihy2019@gmail.com
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

package com.ave.vastgui.tools.os

import android.os.Build
import android.os.Bundle
import android.os.Parcelable

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/3/22
// Documentation: https://ave.entropy2020.cn/documents/VastTools/app-entry-points/activities/bundle/

/**
 * Returns the value associated with the given key.
 *
 * @param key A string.
 * @param callback As the return value if the return the value associated
 *     with the given key is null.
 * @see [Bundle.getParcelable]
 */
inline fun <reified T : Parcelable> Bundle.getParcelable(key: String, callback: T) =
    getParcelable(this, key) ?: callback

/** @see [Bundle.getParcelable]. */
inline fun <reified T : Parcelable> getParcelable(bundle: Bundle, key: String) =
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
        @Suppress("DEPRECATION")
        bundle.getParcelable(key)
    } else {
        bundle.getParcelable(key, T::class.java)
    }