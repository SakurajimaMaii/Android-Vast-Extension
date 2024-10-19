/*
 * Copyright 2021-2024 VastGui
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

package com.log.vastgui.android.lifecycle

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2024/10/18
// Description: 
// Documentation: 
// Reference:

/** @since 1.3.10 */
annotation class LogLifecycle(
    val name: String = "",
    val obverseEvent: Array<LogLifecycleEvent> = [
        LogLifecycleEvent.ON_ATTACH,
        LogLifecycleEvent.ON_CREATE,
        LogLifecycleEvent.ON_CREATE_VIEW,
        LogLifecycleEvent.ON_ACTIVITY_CREATED,
        LogLifecycleEvent.ON_START,
        LogLifecycleEvent.ON_RESUME,
        LogLifecycleEvent.ON_PAUSE,
        LogLifecycleEvent.ON_STOP,
        LogLifecycleEvent.ON_DESTROY_VIEW,
        LogLifecycleEvent.ON_DESTROY,
        LogLifecycleEvent.ON_DETACH,
        LogLifecycleEvent.ON_PRE_ATTACHED,
        LogLifecycleEvent.ON_PRE_CREATED,
        LogLifecycleEvent.ON_SAVE_INSTANCE_STATE
    ]
)