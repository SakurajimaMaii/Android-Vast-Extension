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
// Date: 2022/8/29 17:04
// Description:
// Documentation:

object AndroidX{
    private const val activity_version = "1.7.0-beta02" // https://developer.android.com/jetpack/androidx/releases/activity
    private const val annotation_version = "1.5.0"
    private const val appcompat_version = "1.6.1" // https://developer.android.com/jetpack/androidx/releases/appcompat
    private const val arch_core_version = "2.1.0"
    private const val camerax_version = "1.2.0-alpha04"
    private const val core_version = "1.10.0"
    private const val datastore_version = "1.0.0" // https://developer.android.com/topic/libraries/architecture/datastore
    private const val emoji2_version = "1.4.0-beta04" // https://developer.android.com/jetpack/androidx/releases/emoji2
    private const val exifinterface_version = "1.3.6" // https://developer.android.com/jetpack/androidx/releases/exifinterface
    private const val ext_junit_version = "1.1.3"
    private const val fragment_version = "1.5.2"
    private const val lifecycle_version = "2.5.0-rc01"
    private const val nav_version = "2.5.2"
    private const val paging_version = "3.1.1"
    private const val preference_version = "1.2.0"
    private const val recyclerview_selection_version = "1.1.0"
    private const val recyclerview_version = "1.2.1" // https://developer.android.com/jetpack/androidx/releases/recyclerview
    private const val room_version = "2.5.0" // https://developer.android.com/jetpack/androidx/releases/room
    private const val start_up_version = "1.1.1"
    private const val test_monitor_version = "1.5.0"

    const val activity = "androidx.activity:activity:$activity_version"
    const val activity_ktx = "androidx.activity:activity-ktx:$activity_version"
    const val annotation = "androidx.annotation:annotation:$annotation_version"
    const val appcompat = "androidx.appcompat:appcompat:$appcompat_version"
    const val appcompat_resources = "androidx.appcompat:appcompat-resources:$appcompat_version"
    const val arch_core_runtime = "androidx.arch.core:core-runtime:$arch_core_version"
    const val camerax_camera2 = "androidx.camera:camera-camera2:$camerax_version"
    const val camerax_core = "androidx.camera:camera-core:$camerax_version"
    const val camerax_extensions = "androidx.camera:camera-extensions:$camerax_version"
    const val camerax_lifecycle = "androidx.camera:camera-lifecycle:$camerax_version"
    const val camerax_video = "androidx.camera:camera-video:$camerax_version"
    const val camerax_view = "androidx.camera:camera-view:$camerax_version"
    const val cardview = "androidx.cardview:cardview:1.0.0"
    const val constraintlayout = "androidx.constraintlayout:constraintlayout:2.1.4"
    const val core_ktx = "androidx.core:core-ktx:$core_version"
    const val core_splashscreen = "androidx.core:core-splashscreen:1.0.0"
    const val datastore = "androidx.datastore:datastore-preferences:$datastore_version"
    const val datastore_proto = "androidx.datastore:datastore:1.0.0"
    const val emoji2 = "androidx.emoji2:emoji2:$emoji2_version"
    const val emoji2_views = "androidx.emoji2:emoji2-views:$emoji2_version"
    const val emoji2_views_helper = "androidx.emoji2:emoji2-views-helper:$emoji2_version"
    const val espresso_core = "androidx.test.espresso:espresso-core:3.4.0"
    const val exifinterface = "androidx.exifinterface:exifinterface:$exifinterface_version"
    const val fragment = "androidx.fragment:fragment:$fragment_version"
    const val fragment_ktx = "androidx.fragment:fragment-ktx:$fragment_version"
    const val fragment_testing = "androidx.fragment:fragment-testing:$fragment_version"
    const val junit = "androidx.test.ext:junit:$ext_junit_version"
    const val hilt_lifecycle_viewmodel = "androidx.hilt:hilt-lifecycle-viewmodel:1.0.0-alpha03" // https://androidx.tech/artifacts/hilt/hilt-lifecycle-viewmodel/1.0.0-alpha03
    const val hilt_compiler = "androidx.hilt:hilt-compiler:1.0.0" // https://androidx.tech/artifacts/hilt/hilt-compiler/1.0.0
    const val hilt_navigation_compose = "androidx.hilt:hilt-navigation-compose:1.0.0"
    const val lifecycle_livedata = "androidx.lifecycle:lifecycle-livedata:$lifecycle_version"
    const val lifecycle_livedata_core = "androidx.lifecycle:lifecycle-livedata-core:$lifecycle_version"
    const val lifecycle_livedata_core_ktx = "androidx.lifecycle:lifecycle-livedata-core-ktx:$lifecycle_version"
    const val lifecycle_livedata_ktx = "androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version"
    const val lifecycle_runtime = "androidx.lifecycle:lifecycle-runtime:$lifecycle_version"
    const val lifecycle_runtime_ktx = "androidx.lifecycle:lifecycle-runtime-ktx:$lifecycle_version"
    const val lifecycle_viewmodel = "androidx.lifecycle:lifecycle-viewmodel:$lifecycle_version"
    const val lifecycle_viewmodel_ktx = "androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version"
    const val nav_dynamic_features_fragment = "androidx.navigation:navigation-dynamic-features-fragment:$nav_version"
    const val nav_fragment_ktx = "androidx.navigation:navigation-fragment-ktx:$nav_version"
    const val nav_ui_ktx = "androidx.navigation:navigation-ui-ktx:$nav_version"
    const val paging3 = "androidx.paging:paging-runtime:$paging_version"
    const val palette = "androidx.palette:palette:1.0.0"
    const val preference = "androidx.preference:preference:$preference_version"
    const val preference_ktx = "androidx.preference:preference-ktx:$preference_version"
    const val recyclerview = "androidx.recyclerview:recyclerview:$recyclerview_version"
    const val recyclerview_selection = "androidx.recyclerview:recyclerview-selection:$recyclerview_selection_version"
    const val room_compiler = "androidx.room:room-compiler:$room_version"
    const val room_ktx = "androidx.room:room-ktx:$room_version"
    const val room_runtime = "androidx.room:room-runtime:$room_version"
    const val security_crypto = "androidx.security:security-crypto:1.1.0-alpha04"
    const val start_up_runtime = "androidx.startup:startup-runtime:$start_up_version"
    const val test_monitor = "androidx.test:monitor:$test_monitor_version"
    const val viewpager2 = "androidx.viewpager2:viewpager2:1.0.0"
}