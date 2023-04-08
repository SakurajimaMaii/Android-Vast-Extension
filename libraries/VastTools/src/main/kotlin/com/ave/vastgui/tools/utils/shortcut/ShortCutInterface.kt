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

package com.ave.vastgui.tools.utils.shortcut

import android.app.PendingIntent
import android.content.Intent
import android.os.Build

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/9/19 20:02

interface ShortCutInterface {
    /**
     * Set an [Intent] for shortcut when it be clicked. You will use it if the
     * Android version is before [Build.VERSION_CODES.O].
     */
    fun onPinShortCutClick(): Intent?

    /**
     * Configure the intent by [PendingIntent.getBroadcast] so that your app's
     * broadcast receiver gets the callback successfully. You will use it if
     * the Android version is after [Build.VERSION_CODES.O].
     */
    fun onPinShortCutSuccess(): PendingIntent?
}