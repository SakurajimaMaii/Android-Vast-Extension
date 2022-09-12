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

package com.gcode.vastadapter.interfaces

import android.view.View

// @Author: Vast Gui
// @Email: guihy2019@gmail.com
// @Date: 2022/3/30 7:30

// Fix https://github.com/SakurajimaMaii/VastUtils/issues/35
/**
 * @since 0.0.4
 */
interface VAdpLongClickEventListener {
    fun vAdpLongClickEvent(view: View, pos: Int):Boolean
}