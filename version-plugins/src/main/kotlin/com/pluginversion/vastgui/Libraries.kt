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

object Libraries{
    private const val donut_version = "2.2.0"
    private const val refresh_version = "2.0.5"
    private const val rxjava3 = "3.1.5"
    private const val jackson_version = "2.14.2"
    private const val compose_setting_version = "0.27.0"
    const val proto_version = "3.23.2"

    const val aachartcore_kotlin = "com.github.AAChartModel:AAChartCore-Kotlin:7.2.0" // 图表 https://github.com/AAChartModel/AAChartCore-Kotlin/issues/146
    const val addresspicker = "com.github.gzu-liyujiang.AndroidPicker:AddressPicker:4.1.11" //时间选择器 https://github.com/gzu-liyujiang/AndroidPicker
    const val amapSearch = "com.amap.api:search:latest.integration"
    const val amap_location = "com.amap.api:location:latest.integration" // https://lbs.amap.com/api/android-location-sdk/guide/create-project/android-studio-create-project#t1
    const val amap_navi_3dmap = "com.amap.api:navi-3dmap:9.6.0_3dmap9.6.0"
    const val animatedbottombar = "nl.joery.animatedbottombar:library:1.1.0" //底部导航栏 https://github.com/Droppers/AnimatedBottomBar
    const val circleimageview = "de.hdodenhof:circleimageview:3.1.0" // https://github.com/hdodenhof/CircleImageView/issues
    const val colorpicker = "com.github.QuadFlask:colorpicker:0.0.15" //颜色选择器
    const val commons_codec = "commons-codec:commons-codec:1.16.0" // https://mvnrepository.com/artifact/commons-codec/commons-codec
    const val commons_lang3 = "org.apache.commons:commons-lang3:3.12.0" // https://mvnrepository.com/artifact/org.apache.commons/commons-lang3
    const val compose_coil = "io.coil-kt:coil-compose:2.4.0" // https://github.com/coil-kt/coil/
    const val compose_settings_storage_preferences = "com.github.alorma:compose-settings-storage-preferences:$compose_setting_version"
    const val compose_settings_ui_m3 = "com.github.alorma:compose-settings-ui-m3:$compose_setting_version" // https://github.com/alorma/Compose-Settings
    const val cos_android = "com.qcloud.cos:cos-android:5.9.4" //腾讯云对象存储
    const val donut = "app.futured.donut:donut:$donut_version"
    const val donut_compose = "app.futured.donut:donut-compose:$donut_version"
    const val event_bus = "org.greenrobot:eventbus:3.3.1" // https://github.com/greenrobot/EventBus
    const val fastjson2 = "com.alibaba.fastjson2:fastjson2:2.0.39.android4"
    const val flow_event_bus = "com.github.biubiuqiu0:flow-event-bus:1.0.1" // https://github.com/biubiuqiu0/flow-event-bus
    const val glide = "com.github.bumptech.glide:glide:4.14.2" // https://github.com/bumptech/glide
    const val glide_compiler = "com.github.bumptech.glide:compiler:4.14.2" // https://github.com/bumptech/glide
    const val glide_transformations = "jp.wasabeef:glide-transformations:4.3.0" // https://github.com/wasabeef/glide-transformations
    const val gradienttext = "io.github.weilianyang:gradienttext:1.0.2" // 渐变色字体
    const val jackson_annotations = "com.fasterxml.jackson.core:jackson-annotations:$jackson_version"
    const val jackson_core = "com.fasterxml.jackson.core:jackson-core:$jackson_version"
    const val jackson_databind = "com.fasterxml.jackson.core:jackson-databind:$jackson_version"
    const val junit = "junit:junit:4.13.2"
    const val ktorm_core = "org.ktorm:ktorm-core:3.6.0" // https://central.sonatype.com/artifact/org.ktorm/ktorm-core
    const val logback_classic = "ch.qos.logback:logback-classic:1.4.8" // https://central.sonatype.com/namespace/ch.qos.logback
    const val lombok = "org.projectlombok:lombok:1.18.26" // https://mvnrepository.com/artifact/org.projectlombok/lombok
    const val mmkv = "com.tencent:mmkv:1.2.14"
    const val permissionx = "com.guolindev.permissionx:permissionx:1.6.4" //自定义权限
    const val picktime = "com.github.codbking:PickTime:v1.0.1"
    const val progressmanager = "com.github.JessYanCoding:ProgressManager:v1.5.0"
    const val protobuf_kotlin = "com.google.protobuf:protobuf-kotlin:$proto_version"
    const val protobuf_kotlin_lite = "com.google.protobuf:protobuf-kotlin-lite:$proto_version"
    const val recyclerview_animators = "jp.wasabeef:recyclerview-animators:4.0.2" // https://github.com/wasabeef/recyclerview-animators
    const val refresh_footer_ball = "io.github.scwang90:refresh-footer-ball:$refresh_version"        //球脉冲加载
    const val refresh_footer_classics = "io.github.scwang90:refresh-footer-classics:$refresh_version"    //经典加载
    const val refresh_header_classics = "io.github.scwang90:refresh-header-classics:$refresh_version"    //经典刷新头
    const val refresh_header_falsify = "io.github.scwang90:refresh-header-falsify:$refresh_version"    //虚拟刷新头
    const val refresh_header_material = "io.github.scwang90:refresh-header-material:$refresh_version"    //谷歌刷新头
    const val refresh_header_radar = "io.github.scwang90:refresh-header-radar:$refresh_version" //雷达刷新头
    const val refresh_header_two_level = "io.github.scwang90:refresh-header-two-level:$refresh_version"   //二级刷新头
    const val refresh_layout_kernel = "io.github.scwang90:refresh-layout-kernel:$refresh_version" //核心必须依赖
    const val roundedimageview = "com.github.mmmelik:RoundedImageView:v1.0.1"
    const val rxjava3_rxandroid = "io.reactivex.rxjava3:rxandroid:3.0.0"
    const val rxjava3_rxjava = "io.reactivex.rxjava3:rxjava:$rxjava3" // https://github.com/ReactiveX/RxJava
    const val security_crypto_datastore_preferences = "io.github.osipxd:security-crypto-datastore-preferences:1.0.0-alpha04" // https://github.com/osipxd/encrypted-datastore
    const val shimmer = "io.github.shenzhen2017:shimmer:1.0.0" // https://github.com/RicardoJiang/ComposeShimmer
    const val tbs="com.tencent.tbs:tbssdk:44216"
    const val transformationlayout = "com.github.skydoves:transformationlayout:1.0.8"
    const val zxing = "com.google.zxing:core:3.5.0" // https://github.com/zxing/zxing
}