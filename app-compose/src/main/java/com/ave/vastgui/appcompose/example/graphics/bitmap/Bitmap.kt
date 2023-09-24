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

package com.ave.vastgui.appcompose.example.graphics.bitmap

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.annotation.ColorInt
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ave.vastgui.appcompose.R
import com.ave.vastgui.tools.graphics.BmpUtils
import com.ave.vastgui.tools.graphics.MergeScale
import com.ave.vastgui.tools.utils.DensityUtils.DP

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/8/31
// Documentation: https://ave.entropy2020.cn/documents/VastTools/core-topics/graphics/bitmap/bitmap/

@Composable
fun MergeBitmap() {
    val mergeBitmap = BmpUtils.mergeBitmapTB(
        getColorBitmap(Color(0xFF6ab04c).toArgb(), 200, 200),
        getColorBitmap(Color(0xFFeb4d4b).toArgb(), 300, 300),
        MergeScale.SMALL_ENLARGE
    ) ?: throw RuntimeException("Can't get the bitmap.")
    Image(bitmap = mergeBitmap.asImageBitmap(), contentDescription = "测试图片")
}

@Composable
fun Base64Bitmap() {
    val base64 = "......"
    val base64Bitmap = BmpUtils.getBitmapFromBase64(base64)
    Image(bitmap = base64Bitmap.asImageBitmap(), contentDescription = "测试图片")
}

@Composable
fun GetBitmap() {
    val context = LocalContext.current
    val bitmap = BmpUtils.getBitmapFromDrawable(R.drawable.ic_github, context)
    Image(bitmap = bitmap.asImageBitmap(), contentDescription = "测试图片")
}

@Composable
fun ScaleBitmap() {
    val size = 50F.DP.toInt()
    val requiredSize = 100F.DP.toInt()
    val bitmap = BmpUtils.scaleBitmap(
        getColorBitmap(Color(0xFF6ab04c).toArgb(), size, size),
        requiredSize, requiredSize
    )
    Row(verticalAlignment = Alignment.CenterVertically) {
        Image(
            bitmap = getColorBitmap(Color(0xFF6ab04c).toArgb(), size, size).asImageBitmap(),
            contentDescription = "测试图片"
        )
        Divider(modifier = Modifier.width(20.dp), color = Color.Transparent)
        Image(bitmap = bitmap.asImageBitmap(), contentDescription = "测试图片")
    }
}

@Preview(showBackground = true)
@Composable
fun SizeBitmap() {
    val context = LocalContext.current
    val (width, height) = BmpUtils.getBitmapWidthHeight { options ->
        BitmapFactory.decodeResource(context.resources, R.drawable.github, options)
    }
    Text(text = "图像的尺寸是 $width $height")
}

/** Get color bitmap */
internal fun getColorBitmap(@ColorInt colorInt: Int, width: Int, height: Int) =
    Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888).also {
        it.eraseColor(colorInt)
    }