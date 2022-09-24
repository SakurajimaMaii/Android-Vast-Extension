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

import cn.govast.plugin.version.AndroidX
import cn.govast.plugin.version.Compose
import cn.govast.plugin.version.Version

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("cn.govast.plugin.version")
}

android {
    namespace = "cn.govast.vastutilscompose"
    compileSdk = Version.compile_sdk_version

    defaultConfig {
        applicationId = "com.gcode.vastutilscompose"
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
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.3.0"
    }

    sourceSets["main"].java.srcDir("src/main/kotlin")
}

dependencies {
    implementation(project(":libraries:VastTools"))

    implementation(Compose.compose_foundation)
    implementation(Compose.compose_foundation_layout)
    implementation(Compose.compose_activity)
    implementation(Compose.compose_ui)
    implementation(Compose.compose_ui_tooling_preview)
    implementation(Compose.compose_material3)
    debugImplementation(Compose.compose_ui_tooling)
    debugImplementation(Compose.compose_ui_test_manifest)
    androidTestImplementation(Compose.compose_ui_test_junit4)

    implementation(AndroidX.lifecycle_runtime_ktx)
}