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

package com.pluginversion.vastgui

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2022/8/31 15:40
// Description: 
// Documentation:

object Google {
    private const val hilt_version = "2.46.1" // https://dagger.dev/hilt/gradle-setup

    const val accompanist_coil = "com.google.accompanist:accompanist-coil:0.15.0" // 加载网络图片
    const val accompanist_insets = "com.google.accompanist:accompanist-insets:0.26.2-beta"
    const val accompanist_insets_ui = "com.google.accompanist:accompanist-insets-ui:0.26.2-beta"
    const val accompanist_permissions = "com.google.accompanist:accompanist-permissions:0.31.3-beta" // https://google.github.io/accompanist/permissions/
    const val accompanist_systemuicontroller = "com.google.accompanist:accompanist-systemuicontroller:0.26.2-beta"
    const val gson = "com.google.code.gson:gson:2.10.1"
    const val hilt_android = "com.google.dagger:hilt-android:$hilt_version"
    const val hilt_compiler = "com.google.dagger:hilt-compiler:$hilt_version"
    const val material = "com.google.android.material:material:1.11.0-alpha02"
    const val symbol_processing_api = "com.google.devtools.ksp:symbol-processing-api:1.8.10-1.0.9"
}