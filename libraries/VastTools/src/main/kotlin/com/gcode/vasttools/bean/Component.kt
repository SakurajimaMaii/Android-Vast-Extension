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

package com.gcode.vasttools.bean

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/9/19 19:47
// Description: 
// Documentation:

data class Component2<A : Any, B : Any>(
    val param1: A,
    val param2: B
)

data class Component3<A : Any, B : Any, C : Any>(
    val param1: A,
    val param2: B,
    val param3: C
)

data class Component4<A : Any, B : Any, C : Any, D : Any>(
    val param1: A,
    val param2: B,
    val param3: C,
    val param4: D
)

data class Component5<A : Any, B : Any, C : Any, D : Any, E : Any>(
    val param1: A,
    val param2: B,
    val param3: C,
    val param4: D,
    val param5: E
)