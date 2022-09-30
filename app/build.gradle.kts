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

import cn.govast.plugin.version.*

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("org.jetbrains.kotlin.android")
    id("cn.govast.plugin.version")
}

android {

    signingConfigs {

        getByName("debug") {
            storeFile = File("D:\\AndroidKey\\VastUtils.jks")
            storePassword = project.property("myStorePassword") as String?
            keyPassword = project.property("myKeyPassword") as String?
            keyAlias = project.property("myKeyAlias") as String?
        }

        create("release") {
            storeFile = File("D:\\AndroidKey\\VastUtils.jks")
            storePassword = project.property("myStorePassword") as String?
            keyPassword = project.property("myKeyPassword") as String?
            keyAlias = project.property("myKeyAlias") as String?
        }
    }

    compileSdk = Version.compile_sdk_version

    defaultConfig {
        applicationId = "cn.govast.vastutils"
        minSdk = Version.min_sdk_version
        targetSdk = Version.target_sdk_version
        versionCode = Version.version_code
        versionName = Version.version_name

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {

        release {
            isDebuggable = false
            isJniDebuggable = false
            isShrinkResources = true
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"),"proguard-rules.pro")
        }

        debug {
            isDebuggable = true
            isJniDebuggable = true
            isShrinkResources = false
            isMinifyEnabled = false
        }

    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        viewBinding = true
        dataBinding = true
    }

    namespace = "cn.govast.vastutils"

    sourceSets["main"].java.srcDir("src/main/kotlin")

}

dependencies {
    implementation(project(":libraries:VastNetStateLayout"))
    implementation(project(":libraries:VastTools"))
    implementation(project(":libraries:VastAdapter"))
    implementation(AndroidX.constraintlayout)
    implementation(AndroidX.core_splashscreen)
    implementation(Google.material)
    implementation(Squareup.okhttp3)
    implementation(Squareup.retrofit2)
    implementation(Squareup.retrofit2_adapter_rxjava3)
    implementation(Squareup.retrofit2_converter_gson)
    implementation(Jetbrains.kotlin_reflect)
    implementation(Jetbrains.kotlinx_coroutines_core)
    implementation(Jetbrains.kotlinx_coroutines_android)
    implementation(Jetbrains.kotlinx_coroutines_rx3)
}