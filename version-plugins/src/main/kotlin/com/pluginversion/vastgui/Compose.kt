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
// Date: 2022/8/31 15:00
// Description: 
// Documentation:

object Compose {
    private const val version_compose = "1.3.3"
    private const val version_compose_activity = "1.4.0"
    private const val version_compose_animation = "1.2.1"
    private const val version_compose_bom = "2023.05.01" // https://developer.android.com/jetpack/compose/bom/bom-mapping
    private const val version_compose_constraintlayout = "1.0.1" // https://developer.android.com/jetpack/androidx/releases/constraintlayout
    private const val version_compose_foundation = "1.2.0-rc03"
    private const val version_compose_lifecycle_viewmodel = "2.5.1"
    private const val version_compose_material = "1.2.1"
    private const val version_compose_runtime = "1.2.1"
    private const val version_material3_version = "1.1.0-alpha06"
    private const val version_nav_version = "2.5.3" // https://developer.android.com/jetpack/compose/navigation

    const val compose_activity = "androidx.activity:activity-compose:$version_compose_activity"
    const val compose_animation = "androidx.compose.animation:animation:$version_compose_animation"
    const val compose_bom = "androidx.compose:compose-bom:$version_compose_bom"
    const val compose_compiler = "androidx.compose.compiler:compiler:1.1.0-alpha03"
    const val compose_constraintlayout = "androidx.constraintlayout:constraintlayout-compose:$version_compose_constraintlayout"
    const val compose_foundation = "androidx.compose.foundation:foundation:$version_compose_foundation"
    const val compose_foundation_layout = "androidx.compose.foundation:foundation-layout:$version_compose_foundation"
    const val compose_lifecycle_runtime = "androidx.lifecycle:lifecycle-runtime-compose:2.6.1" // https://mvnrepository.com/artifact/androidx.lifecycle/lifecycle-runtime-compose
    const val compose_lifecycle_viewmodel = "androidx.lifecycle:lifecycle-viewmodel-compose:$version_compose_lifecycle_viewmodel"
    const val compose_material = "androidx.compose.material:material:$version_compose_material"
    const val compose_material3 = "androidx.compose.material3:material3:$version_material3_version"
    const val compose_material_icons_core = "androidx.compose.material:material-icons-core:$version_compose_material"
    const val compose_material_icons_extends = "androidx.compose.material:material-icons-extended:$version_compose_material"
    const val compose_navigation = "androidx.navigation:navigation-compose:$version_nav_version"
    const val compose_runtime = "androidx.compose.runtime:runtime:1.2.0-beta03"
    const val compose_runtime_livedata = "androidx.compose.runtime:runtime-livedata:1.2.0-beta03"
    const val compose_runtime_rxjava2 = "androidx.compose.runtime:runtime-rxjava2:1.2.0-beta03"
    const val compose_ui = "androidx.compose.ui:ui:$version_compose"
    const val compose_ui_graphics = "androidx.compose.ui:ui-graphics:$version_compose"
    const val compose_ui_test_junit4 = "androidx.compose.ui:ui-test-junit4:$version_compose"
    const val compose_ui_test_manifest = "androidx.compose.ui:ui-test-manifest:$version_compose"
    const val compose_ui_tooling = "androidx.compose.ui:ui-tooling:$version_compose"
    const val compose_ui_tooling_preview = "androidx.compose.ui:ui-tooling-preview:$version_compose"
    const val compose_ui_viewbinding = "androidx.compose.ui:ui-viewbinding:1.2.1" // Use xml layout in Compose
}