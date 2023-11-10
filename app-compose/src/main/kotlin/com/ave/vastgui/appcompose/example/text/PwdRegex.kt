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

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.ave.vastgui.tools.text.RwdStrength3
import com.ave.vastgui.tools.text.isPwd

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/7/15
// Documentation: https://ave.entropy2020.cn/documents/VastTools/core-topics/text/pwd-regex/

@Composable
fun PwdRegex(){
    val password = "852admin."
    Column {
        Text(text = "密码 $password 是否符合要求？")
        Text(text = "${password.isPwd(RwdStrength3, shouldNotAppear = arrayOf("123","admin"))}")
    }
}