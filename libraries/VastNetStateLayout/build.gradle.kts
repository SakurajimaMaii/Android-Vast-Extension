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
import com.pluginversion.vastgui.Libraries
import com.pluginversion.vastgui.Version

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("com.pluginversion.vastgui")
    id("convention.publication")
}

group = "io.github.sakurajimamaii"
version = "0.0.3"

android {
    compileSdk = Version.compile_sdk_version
    namespace = "com.ave.vastgui.netstatelayout"

    defaultConfig {
        minSdk = Version.min_sdk_version

        testInstrumentationRunner ="androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = Version.java_version
        targetCompatibility = Version.java_version
    }

    kotlinOptions {
        jvmTarget = Version.java_version.toString()
    }

    sourceSets["main"].java.srcDir("src/main/kotlin")
}

dependencies {
    implementation(AndroidX.activity_ktx)
    implementation(AndroidX.annotation)
    implementation(AndroidX.appcompat)
    implementation(AndroidX.arch_core_runtime)
    implementation(AndroidX.constraintlayout)
    implementation(AndroidX.core_ktx)
    implementation(AndroidX.core_splashscreen)
    implementation(AndroidX.fragment_ktx)
    androidTestImplementation(AndroidX.espresso_core)
    androidTestImplementation(AndroidX.junit)
    implementation(Google.material)
    testImplementation(Libraries.junit)
}

extra["PUBLISH_ARTIFACT_ID"] = "VastNetStateLayout"
extra["PUBLISH_DESCRIPTION"] = "A layout to set customized network state ui."
extra["PUBLISH_URL"] = "https://github.com/SakurajimaMaii/VastUtils/tree/master/libraries/VastNetStateLayout"
