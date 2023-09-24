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

package com.ave.vastgui.appcompose.example.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import com.ave.vastgui.appcompose.ui.theme.AndroidVastExtensionTheme
import com.ave.vastgui.tools.view.vp2indicator.Vp2IndicatorView

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/6/14
// Documentation: https://ave.entropy2020.cn/documents/VastTools/core-topics/ui/viewpager2/vp2-indicator-view/vp2-indicator-view/

@Composable
fun Vp2Indicator() {
    Column(modifier = Modifier.fillMaxSize()) {
        AndroidView(factory = { ctx ->
            Vp2IndicatorView(ctx).apply {
                setIndicatorItemCount(5)
                setIndicatorCircleRadius(20f)
            }
        })
    }
}

@Preview(showBackground = true)
@Composable
fun Vp2IndicatorViewPreview() {
    AndroidVastExtensionTheme {
        Vp2Indicator()
    }
}