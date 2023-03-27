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

import com.pluginversion.vastgui.AVE
import com.pluginversion.vastgui.AndroidX
import com.pluginversion.vastgui.Google
import com.pluginversion.vastgui.Jetbrains
import com.pluginversion.vastgui.Squareup
import com.pluginversion.vastgui.Version

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("org.jetbrains.kotlin.android")
    id("com.pluginversion.vastgui")
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
        applicationId = "com.ave.vastgui.app"
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
        sourceCompatibility = Version.java_version
        targetCompatibility = Version.java_version
    }

    kotlinOptions {
        jvmTarget = Version.java_version.toString()
    }

    buildFeatures {
        dataBinding = true
    }

    namespace = "com.ave.vastgui.app"

    sourceSets["main"].java.srcDirs("src/main/kotlin","src/main/java","src/main/compose")

}

project(":app") {
    sonarqube {
        this.isSkipProject = true
    }
}

dependencies {
    implementation(AndroidX.activity_ktx)
    implementation(AndroidX.constraintlayout)
    implementation(AndroidX.core_splashscreen)
    implementation(AndroidX.security_crypto)
    implementation(AndroidX.fragment_ktx)
    implementation(AndroidX.lifecycle_runtime_ktx)
    implementation(AndroidX.lifecycle_viewmodel_ktx)
    implementation(AndroidX.lifecycle_livedata_ktx)
    implementation(Google.material)
    implementation(Jetbrains.kotlin_reflect)
    implementation(Jetbrains.kotlinx_coroutines_android)
    implementation(Jetbrains.kotlinx_coroutines_core)
    implementation(Jetbrains.kotlinx_coroutines_rx3)
    implementation(Squareup.okhttp3)
    implementation(Squareup.retrofit2)
    implementation(Squareup.retrofit2_adapter_rxjava3)
    implementation(Squareup.retrofit2_converter_gson)
    implementation(project(":libraries:VastAdapter"))
    implementation(AVE.netStateLayout)
    implementation(project(":libraries:VastTools"))
    implementation(AVE.core)
}