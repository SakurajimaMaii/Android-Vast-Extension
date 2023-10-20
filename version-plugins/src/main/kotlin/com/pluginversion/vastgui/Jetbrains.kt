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
// Date: 2022/9/3 12:18
// Description: 
// Documentation:

object Jetbrains {

    private const val reflect_version =
        "1.8.10" // https://mvnrepository.com/artifact/org.jetbrains.kotlin/kotlin-reflect
    private const val coroutines_android_version =
        "1.7.3" // https://mvnrepository.com/artifact/org.jetbrains.kotlinx/kotlinx-coroutines-android
    private const val coroutines_version =
        "1.7.3" // https://mvnrepository.com/artifact/org.jetbrains.kotlinx/kotlinx-coroutines-core

    const val kotlin_stdlib = "org.jetbrains.kotlin:kotlin-stdlib:$coroutines_version"
    const val kotlin_reflect = "org.jetbrains.kotlin:kotlin-reflect:$reflect_version"
    const val kotlinx_coroutines_android =
        "org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutines_android_version"
    const val kotlinx_coroutines_core =
        "org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutines_version"
    const val kotlinx_coroutines_reactor =
        "org.jetbrains.kotlinx:kotlinx-coroutines-reactor:$coroutines_version"
    const val kotlinx_coroutines_reactive =
        "org.jetbrains.kotlinx:kotlinx-coroutines-reactive:$coroutines_version"
    const val kotlinx_coroutines_jvm =
        "org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:$coroutines_version"
    const val kotlinx_coroutines_rx3 =
        "org.jetbrains.kotlinx:kotlinx-coroutines-rx3:$coroutines_version"

    // https://mvnrepository.com/artifact/org.jetbrains.kotlinx/kotlinx-serialization-json-jvm
    const val kotlinx_serialization_json =
        "org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1"

    // https://mvnrepository.com/artifact/org.jetbrains.kotlinx/kotlinx-serialization-runtime
    const val kotlinx_serialization_runtime =
        "org.jetbrains.kotlinx:kotlinx-serialization-runtime:1.0-M1-1.4.0-rc"

    // https://github.com/Kotlin/kotlinx-datetime
    const val kotlinx_datetime = "org.jetbrains.kotlinx:kotlinx-datetime:0.4.1"
}