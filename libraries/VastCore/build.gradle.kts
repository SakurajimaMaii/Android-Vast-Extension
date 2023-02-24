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

import com.pluginversion.vastgui.Version

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("com.pluginversion.vastgui")
}

android {
    namespace = "com.ave.vastgui.core"
    compileSdk = Version.compile_sdk_version

    defaultConfig {
        minSdk = Version.min_sdk_version
        targetSdk = Version.target_sdk_version

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = Version.java_version
        targetCompatibility = Version.java_version
    }

    kotlinOptions {
        jvmTarget = Version.java_version.toString()
    }

    sourceSets {
        getByName("main").java.srcDirs("src/main/kotlin")
    }
}

extra["PUBLISH_GROUP_ID"] = "io.github.sakurajimamaii"
extra["PUBLISH_ARTIFACT_ID"] = "VastCore"
extra["PUBLISH_VERSION"] = "0.0.1"
extra["PUBLISH_DESCRIPTION"] = "Core for Android-Vast-Extension"
extra["PUBLISH_URL"] = "https://github.com/SakurajimaMaii/Android-Vast-Extension/tree/develop/libraries/VastCore"

apply(from = "${rootProject.projectDir}/publish-mavencentral.gradle")