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

package com.ave.vastgui.tools.view.badgeview

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/9/12
// Documentation: https://sakurajimamaii.github.io/AVE-DOC/documents/tools/core-topics/ui/badge/description/

/**
 * Badge state.
 *
 * @since 0.5.3
 */
sealed class BadgeState {

    /** @since 1.5.2 */
    internal object UnspecifiedState : BadgeState()

    /**
     * Dot state.
     *
     * @property Show Show the badge dot.
     * @property Hide Hide the badge dot.
     * @since 1.5.2
     */
    sealed class DotState : BadgeState() {
        data object Show : DotState()
        data object Hide : DotState()
    }

    /**
     * Bubble State.
     *
     * @property Default The default state of badge.
     * @property Connect The fixed point and the current moving point are also
     * connected using a Bezier curve.
     * @property Apart The current moving point and the fixed point are
     * separated and are not connected by a Bezier curve.
     * @property Hide The moving point will be hided and start explosion
     * animation.
     * @since 1.5.2
     */
    sealed class BubbleState : BadgeState() {
        data object Default : BubbleState()
        data object Connect : BubbleState()
        data object Apart : BubbleState()
        data object Hide : BubbleState()
    }
}