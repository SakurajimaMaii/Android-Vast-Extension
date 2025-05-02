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

package com.ave.vastgui.tools.vibrator

import android.os.VibrationEffect.Composition
import androidx.annotation.IntDef

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2025/5/1
// Documentation:

/** @since 1.5.2 */
@IntDef(
    Composition.PRIMITIVE_CLICK,
    Composition.PRIMITIVE_SPIN,
    Composition.PRIMITIVE_THUD,
    Composition.PRIMITIVE_TICK,
    Composition.PRIMITIVE_LOW_TICK,
    Composition.PRIMITIVE_QUICK_FALL,
    Composition.PRIMITIVE_QUICK_RISE,
    Composition.PRIMITIVE_SLOW_RISE
)
@Retention(AnnotationRetention.SOURCE)
annotation class PrimitiveType