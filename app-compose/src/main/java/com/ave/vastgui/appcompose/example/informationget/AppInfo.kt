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

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import com.ave.vastgui.tools.utils.AppUtils

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/5/31
// Documentation: https://ave.entropy2020.cn/documents/VastTools/core-topics/information-get/app-utils/

@Composable
fun AppInfo() {
    val appInfo = mapOf(
        "getPackageName" to AppUtils.getPackageName(),
        "getAppName" to AppUtils.getAppName(),
        "getVersionName" to AppUtils.getVersionName(),
        "getVersionCode" to AppUtils.getVersionCode().toString(),
        "getAppDebug" to AppUtils.getAppDebug().toString()
    ).toList()

    Column {
        Text(
            text = "Function name: getAppBitmap",
            modifier = Modifier.padding(15.dp)
        )

        Image(
            bitmap = AppUtils.getAppBitmap().asImageBitmap(),
            contentDescription = null,
            modifier = Modifier.padding(15.dp)
        )

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(appInfo) { item ->
                ListItem(
                    headlineText = {
                        Text(text = "Function name: ${item.first}")
                    },
                    supportingText = {
                        Text(text = item.second)
                    }
                )
            }
        }
    }
}