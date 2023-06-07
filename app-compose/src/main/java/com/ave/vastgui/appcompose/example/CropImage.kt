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

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ave.vastgui.tools.activity.result.contract.CropPhotoContract
import com.ave.vastgui.tools.activity.result.contract.PickPhotoContract
import com.ave.vastgui.tools.utils.cropimage.CropIntent

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/6/5
// Description: 
// Documentation:
// Reference:

@Composable
fun CropImage(modifier: Modifier = Modifier) {
    var image by remember {
        mutableStateOf<Uri?>(null)
    }
    val cropImage = rememberLauncherForActivityResult(CropPhotoContract()) { uri: Uri? ->
        image = uri
    }
    val pickImage = rememberLauncherForActivityResult(PickPhotoContract()) {
        it?.let {
            val cropIntent = CropIntent()
                .setData(it)
                .setCorp(true)
                .setAspect(1, 1)
                .setOutput(200, 200)
                .setReturnData(false)
                .setNoFaceDetection(true)
            cropImage.launch(cropIntent)
        }
    }
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier) {
        Button(onClick = { pickImage.launch(null) }) {
            Text(text = "从相册选择一张照片并调用系统裁剪")
        }
        image?.let { it ->
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current).data(it).build(),
                contentDescription = null,
                modifier = Modifier.size(200.dp,200.dp)
            )
        }
    }
}