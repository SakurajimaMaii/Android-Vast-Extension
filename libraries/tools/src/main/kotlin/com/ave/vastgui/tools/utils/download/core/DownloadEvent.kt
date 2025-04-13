/*
 * Copyright 2021-2025 VastGui
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

package com.ave.vastgui.tools.utils.download.core

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2025/3/17
// Documentation: 
// Reference:

sealed class DownloadEvent(val priority: Int) : Comparable<DownloadEvent> {
    /** @since 1.5.2 */
    data object Init : DownloadEvent(0)

    /** @since 1.5.2 */
    data object Start : DownloadEvent(1)

    /** @since 1.5.2 */
    data object Pause : DownloadEvent(2)

    /** @since 1.5.2 */
    data object Resume : DownloadEvent(2)

    /** @since 1.5.2 */
    data object Termination : DownloadEvent(3)

    override fun compareTo(other: DownloadEvent): Int {
        return this.priority.compareTo(other.priority)
    }
}