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

package com.ave.vastgui.appcompose.example.graphics

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.tooling.preview.Preview
import com.ave.vastgui.tools.graphics.QRCode.createQRCodeBitmap

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/5/31
// Documentation: https://ave.entropy2020.cn/documents/tools/core-topics/graphics/qrcode/qrcode/

@Composable
fun QRCode() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        val qrCode = createQRCodeBitmap("Hello", 800, 800)
        Image(bitmap = qrCode.asImageBitmap(), contentDescription = null)
    }
}

@Preview(showBackground = true)
@Composable
fun QRCodePreview() {
    QRCode()
}