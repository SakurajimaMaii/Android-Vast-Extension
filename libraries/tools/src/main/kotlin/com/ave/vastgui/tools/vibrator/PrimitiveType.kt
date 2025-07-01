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

import android.os.Build
import android.os.VibrationEffect.Composition

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2025/5/1
// Documentation: https://sakurajimamaii.github.io/AVE-DOC/documents/tools/core-topics/hardware/vibrator/

/** @since 1.5.2 */
enum class PrimitiveType(val value: Int) {
    PRIMITIVE_UNKNOW(-1),
    PRIMITIVE_CLICK(if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) Composition.PRIMITIVE_CLICK else PRIMITIVE_UNKNOW.value),
    PRIMITIVE_THUD(if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) Composition.PRIMITIVE_THUD else PRIMITIVE_UNKNOW.value),
    PRIMITIVE_SPIN(if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) Composition.PRIMITIVE_SPIN else PRIMITIVE_UNKNOW.value),
    PRIMITIVE_QUICK_RISE(if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) Composition.PRIMITIVE_QUICK_RISE else PRIMITIVE_UNKNOW.value),
    PRIMITIVE_SLOW_RISE(if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) Composition.PRIMITIVE_SLOW_RISE else PRIMITIVE_UNKNOW.value),
    PRIMITIVE_QUICK_FALL(if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) Composition.PRIMITIVE_QUICK_FALL else PRIMITIVE_UNKNOW.value),
    PRIMITIVE_TICK(if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) Composition.PRIMITIVE_TICK else PRIMITIVE_UNKNOW.value),
    PRIMITIVE_LOW_TICK(if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) Composition.PRIMITIVE_LOW_TICK else PRIMITIVE_UNKNOW.value)
}