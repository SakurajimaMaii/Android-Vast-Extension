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

@file:JvmName("VastSwipeRvConstant")

package com.gcode.vastswiperecyclerview.annotation

import android.util.TypedValue.*
import androidx.annotation.IntDef
import androidx.annotation.StringDef

// Author: SakurajimaMai
// Email: guihy2019@gmail.com
// Date: 2022/6/14
// Description:
// Documentation:

/**
 * Swipe in the x-axis direction
 */
const val TOUCH_STATE_X = 1

/**
 * Swipe in the y-axis direction
 */
const val TOUCH_STATE_Y = 2

/**
 * Default swipe direction.
 */
const val TOUCH_STATE_NONE = 0

/**
 * Just show title
 */
const val ONLY_TITLE = "ONLY_TITLE"

/**
 * Just show icon
 */
const val ONLY_ICON = "ONLY_ICON"

/**
 * show title and icon
 */
const val ICON_TITLE = "ICON_TITLE"

const val STATE_INIT = 0X01

/**
 * Swipe menu close state.
 */
const val STATE_CLOSE = 0X02

/**
 * Swipe menu right open state.
 *
 * It also means you swipe to the left.
 */
const val STATE_RIGHT_OPEN = 0X03

/**
 * Swipe menu left open state.
 *
 * It also means you swipe to the right.
 */
const val STATE_LEFT_OPEN = 0X04

/**
 * Not init
 */
const val NOT_INIT = -1

/**
 * Only right have menu.
 */
const val ONLY_RIGHT = 0

/**
 * Only left have menu.
 */
const val ONLY_LEFT = 1

/**
 * Left and right have menu.
 */
const val LEFT_RIGHT = 2

/**
 * Using when you want to set swipe menu content style.
 */
@Retention(AnnotationRetention.SOURCE)
@StringDef(ONLY_TITLE, ONLY_ICON, ICON_TITLE)
annotation class SwipeMenuContentStyle

/**
 * Use when set the swipe orientation
 */
@Retention(AnnotationRetention.SOURCE)
@IntDef(STATE_CLOSE, STATE_LEFT_OPEN, STATE_RIGHT_OPEN)
annotation class SwipeMenuOrientation

/**
 * Using when you want to set swipe menu style.
 */
@Retention(AnnotationRetention.SOURCE)
@IntDef(NOT_INIT, ONLY_LEFT, ONLY_RIGHT, LEFT_RIGHT)
annotation class MenuStyle

/**
 * Dimension
 */
@Retention(AnnotationRetention.SOURCE)
@IntDef(COMPLEX_UNIT_PX,COMPLEX_UNIT_DIP,COMPLEX_UNIT_SP,COMPLEX_UNIT_PT,COMPLEX_UNIT_IN,COMPLEX_UNIT_MM)
annotation class Dimension