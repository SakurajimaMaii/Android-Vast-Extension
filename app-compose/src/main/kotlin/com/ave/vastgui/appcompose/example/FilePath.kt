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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ave.vastgui.appcompose.ui.theme.AndroidVastExtensionTheme
import com.ave.vastgui.tools.manager.filemgr.FileMgr
import com.ave.vastgui.tools.manager.filemgr.FileMgr.appExternalCacheDir
import com.ave.vastgui.tools.manager.filemgr.FileMgr.appExternalFilesDir
import com.ave.vastgui.tools.manager.filemgr.FileMgr.appInternalCacheDir
import com.ave.vastgui.tools.manager.filemgr.FileMgr.appInternalFilesDir
import com.ave.vastgui.tools.manager.mediafilemgr.ImageMgr
import com.ave.vastgui.tools.manager.mediafilemgr.MusicMgr

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/6/5
// Documentation: https://ave.entropy2020.cn/documents/VastTools/core-topics/app-data-and-files/file-manager/file-mgr/

private val devicePath = listOf(
    appInternalFilesDir().path,
    appInternalCacheDir().path,
    appExternalFilesDir(null).path,
    appExternalCacheDir().path,
    ImageMgr.getExternalFilesDir().path,
    ImageMgr.getSharedFilesDir().path,
    MusicMgr.getExternalFilesDir().path,
    MusicMgr.getSharedFilesDir().path
)

@Composable
fun FilePath(modifier: Modifier = Modifier) {
    val path1 = FileMgr.getPath {
        "a" f "b" f "c"
    }
    Column(modifier) {
        Text(text = path1)
        Text(text = appInternalFilesDir().path)
    }
}

@Composable
fun DevicePath(modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier) {
        items(devicePath) {
            ListItem(headlineContent = { Text(text = it) })
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FilePathPreview() {
    AndroidVastExtensionTheme {
        FilePath(modifier = Modifier.wrapContentSize())
    }
}