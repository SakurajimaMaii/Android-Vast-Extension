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
import com.pluginversion.vastgui.Google
import com.pluginversion.vastgui.Version

plugins {
    id("com.android.application")
    id("kotlin-android")
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
    }

    namespace = "com.ave.vastgui.app"
    compileSdk = Version.compile_sdk_version

    defaultConfig {
        applicationId = "com.gcode.vastutilsjava"
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
    }

    compileOptions {
        sourceCompatibility = Version.java_version
        targetCompatibility = Version.java_version
    }

    kotlinOptions {
        jvmTarget = Version.java_version.toString()
    }
}

dependencies {
    implementation(AndroidX.appcompat)
    implementation(Google.material)
}