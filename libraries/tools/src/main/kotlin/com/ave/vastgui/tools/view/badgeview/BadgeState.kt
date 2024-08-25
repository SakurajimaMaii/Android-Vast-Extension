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

package com.ave.vastgui.tools.view.badgeview

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/9/12
// Documentation: https://ave.entropy2020.cn/documents/tools/core-topics/ui/badge/description/

/**
 * Badge state.
 *
 * @since 0.5.3
 */
sealed class BadgeState {
    internal object UNSPECIFIED : BadgeState()

    /**
     * Dot state.
     *
     * @property SHOW Show the badge dot.
     * @property HIDE Hide the badge dot.
     * @since 0.5.3
     */
    sealed class DOT : BadgeState() {
        object SHOW : DOT()
        object HIDE : DOT()
    }

    /**
     * Bubble State.
     *
     * @property DEFAULT The default state of badge.
     * @property CONNECT The fixed point and the current moving point are also
     *     connected using a Bezier curve.
     * @property APART The current moving point and the fixed point are
     *     separated and are not connected by a Bezier curve.
     * @property HIDE The moving point will be hided and start explosion
     *     animation.
     * @since 0.5.3
     */
    sealed class BUBBLE : BadgeState() {
        object DEFAULT : BUBBLE()
        object CONNECT : BUBBLE()
        object APART : BUBBLE()
        object HIDE : BUBBLE()
    }

    override fun toString(): String = this::class.java.toString()
}