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

import com.pluginversion.vastgui.AndroidX
import com.pluginversion.vastgui.Compose
import com.pluginversion.vastgui.Jetbrains
import com.pluginversion.vastgui.Libraries
import com.pluginversion.vastgui.Squareup
import com.pluginversion.vastgui.Version

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.pluginversion.vastgui")
    id("com.google.devtools.ksp")
    kotlin("plugin.serialization")
}

android {
    namespace = "com.ave.vastgui.appcompose"
    compileSdk = Version.compile_sdk_version

    defaultConfig {
        applicationId = "com.ave.vastgui.appcompose"
        minSdk = Version.min_sdk_version
        targetSdk = Version.target_sdk_version
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            kotlin {
                sourceSets.main {
                    kotlin.srcDir("build/generated/ksp/release/kotlin")
                }
            }
        }
        debug {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            kotlin {
                sourceSets.main {
                    kotlin.srcDir("build/generated/ksp/debug/kotlin")
                }
            }
        }
    }
    compileOptions {
        sourceCompatibility = Version.java_version
        targetCompatibility = Version.java_version
    }
    kotlinOptions {
        jvmTarget = Version.java_version.toString()
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.2"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(project(":libraries:VastTools"))
    implementation(AndroidX.core_ktx)
    implementation(AndroidX.datastore)
    implementation(AndroidX.fragment)
    implementation(AndroidX.lifecycle_runtime_ktx)
    implementation(Compose.compose_activity)
    implementation(Compose.compose_ui)
    implementation(Compose.compose_ui_graphics)
    implementation(Compose.compose_ui_tooling_preview)
    implementation(Compose.compose_material3)
    implementation(Compose.compose_lifecycle_viewmodel)
    implementation(Compose.compose_runtime_livedata)
    implementation(Jetbrains.kotlinx_coroutines_core)
    implementation(Jetbrains.kotlinx_serialization_json)
    implementation(Squareup.okhttp3)
    implementation(Squareup.retrofit2)
    implementation(Squareup.retrofit2_adapter_rxjava3)
    implementation(Squareup.retrofit2_converter_gson)
    implementation(Libraries.compose_coil)
    implementation(project(":libraries:VastJIntent"))
    implementation(project(":libraries:VastJIntent-Annotation"))
    ksp(project(":libraries:VastJIntent-Processor"))
    implementation("tech.thdev:extensions-compose-keyboard-state:1.5.0")
    debugImplementation(Compose.compose_ui_tooling)
    debugImplementation(Compose.compose_ui_test_manifest)
}