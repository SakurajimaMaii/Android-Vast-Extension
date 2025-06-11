/*
 * Copyright 2021-2025 VastGui
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

package com.ave.vastgui.tools.view.breadcrumb

import android.graphics.drawable.Drawable

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2025/6/11
// Documentation:

/**
 * Bread crumb.
 *
 * @since 1.5.2
 */
data class BreadCrumb(val icon: Drawable?, val path: String?) {

    /** @since 1.5.2 */
    constructor(path: String) : this(null, path)

    /** @since 1.5.2 */
    constructor(icon: Drawable) : this(icon, null)

    init {
        check(icon != null || path?.isNotBlank() == true) {
            "The icon and path cannot be empty at the same time."
        }
        icon?.setBounds(0, 0, icon.intrinsicWidth, icon.intrinsicHeight)
    }

}