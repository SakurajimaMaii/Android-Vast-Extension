/*
 * Copyright 2022 VastGui
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

package com.gcode.vastskin

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/3/30 18:41

internal const val THEME_FILE = "com.gcode.vastskin"
internal const val THEME_PATH = "com.gcode.vastskin.path"

internal const val CHANGEABLY_BACKGROUND = "background"
internal const val CHANGEABLY_BACKGROUND_TINT= "backgroundTint"
internal const val CHANGEABLY_DRAWABLE_BOTTOM = "drawableBottom"
internal const val CHANGEABLY_DRAWABLE_LEFT = "drawableLeft"
internal const val CHANGEABLY_DRAWABLE_RIGHT = "drawableRight"
internal const val CHANGEABLY_DRAWABLE_TOP = "drawableTop"
internal const val CHANGEABLY_SRC = "src"
internal const val CHANGEABLY_TEXT = "text"
internal const val CHANGEABLY_TEXT_COLOR = "textColor"

/**
 * Vast skin currently supports changeably resource types.
 */
internal val ChangeablyAttrs: MutableList<String> = ArrayList<String>().apply {
    add(CHANGEABLY_BACKGROUND)
    add(CHANGEABLY_BACKGROUND_TINT)
    add(CHANGEABLY_DRAWABLE_BOTTOM)
    add(CHANGEABLY_DRAWABLE_LEFT)
    add(CHANGEABLY_DRAWABLE_RIGHT)
    add(CHANGEABLY_DRAWABLE_TOP)
    add(CHANGEABLY_SRC)
    add(CHANGEABLY_TEXT)
    add(CHANGEABLY_TEXT_COLOR)
}