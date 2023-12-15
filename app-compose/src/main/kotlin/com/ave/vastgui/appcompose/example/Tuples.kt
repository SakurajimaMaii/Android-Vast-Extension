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

package com.ave.vastgui.appcompose.example

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.ave.vastgui.core.utils.Quadruple
import com.ave.vastgui.core.utils.Quintuple

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/7/24
// Documentation: https://ave.entropy2020.cn/documents/VastCore/utils/tuples/

enum class Gender {
    MAN,
    WOMAN
}

@Composable
private fun tuples() {
    val student1 =
        Quadruple("张", "三", 18, Gender.MAN)
    val student2 =
        Quintuple("张", "三", 18, Gender.WOMAN, "大学")
    Column {
        Text(text = "$student1")
        Text(text = "$student2")
    }
}