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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ave.vastgui.tools.utils.ScreenSizeUtils

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/6/2
// Documentation: https://ave.entropy2020.cn/documents/VastTools/core-topics/information-get/screen-size-utils/

@Composable
fun ScreenInfo(modifier: Modifier = Modifier){
    val height = ScreenSizeUtils.getMobileScreenHeight()
    val width = ScreenSizeUtils.getMobileScreenWidth()
    val orientation = ScreenSizeUtils.getScreenOrientation()
    Column(modifier) {
        Text(text = "屏幕高度 $height")
        Text(text = "屏幕宽度 $width")
        Text(text = "屏幕方向 $orientation")
    }
}