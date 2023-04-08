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

package com.ave.vastgui.tools.utils.permission

import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultCaller
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/4/6

/**
 * PermissionLauncher
 *
 * @since 0.2.0
 */
class PermissionLauncher<I, O>(
    activityResultCaller: ActivityResultCaller,
    activityResultContract: ActivityResultContract<I, O>
) {

    private var activityResultCallback: ActivityResultCallback<O>? = null


    private val launcher: ActivityResultLauncher<I> =
        activityResultCaller.registerForActivityResult(activityResultContract) {
            activityResultCallback?.onActivityResult(it)
        }


    fun launch(input: I, activityResultCallback: ActivityResultCallback<O>?) {
        this.activityResultCallback = activityResultCallback
        launcher.launch(input)
    }

    fun unregister() {
        launcher.unregister()
    }

}