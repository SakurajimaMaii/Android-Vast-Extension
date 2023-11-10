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

package com.ave.vastgui.appcompose.example.text

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.ave.vastgui.tools.text.Kmh
import com.ave.vastgui.tools.text.withUnit

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/7/15
// Documentation: https://ave.entropy2020.cn/documents/VastTools/core-topics/app-resources/text/unit-string/

@Composable
fun UnitString(){
    Text(text = "12".withUnit(Kmh()))
}