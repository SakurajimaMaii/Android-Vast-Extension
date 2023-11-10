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

package com.ave.vastgui.appcompose.example.informationget

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ave.vastgui.appcompose.ui.theme.AndroidVastExtensionTheme
import com.ave.vastgui.tools.utils.SystemUtils

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/5/31
// Documentation: https://ave.entropy2020.cn/documents/VastTools/core-topics/information-get/system-utils/

@Composable
fun SystemInfo(){
    Column(
        modifier = Modifier.fillMaxSize().padding(10.dp)
    ) {
        Text(text = "系统语言 ${SystemUtils.systemLanguage}")
        Text(text = "系统安卓版本 ${SystemUtils.systemAndroidVersion}")
        Text(text = "用户可见设备名 ${SystemUtils.systemModel}")
        Text(text = "设备品牌 ${SystemUtils.deviceBrand}")
    }
}

@Preview(showBackground = true)
@Composable
fun SystemInfoPreview(){
    AndroidVastExtensionTheme {
        SystemInfo()
    }
}
