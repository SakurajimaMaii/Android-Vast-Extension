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
import cn.govast.plugin.version.Google
import cn.govast.plugin.version.Libraries
import cn.govast.plugin.version.Version

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("cn.govast.plugin.version")
}

android {
    compileSdk = Version.compile_sdk_version

    defaultConfig {
        minSdk = Version.min_sdk_version
        targetSdk = Version.target_sdk_version

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

extra["PUBLISH_GROUP_ID"] = "io.github.sakurajimamaii"
extra["PUBLISH_ARTIFACT_ID"] = "VastNetStateLayout"
extra["PUBLISH_VERSION"] = "0.0.3"
extra["PUBLISH_DESCRIPTION"] = "A layout to set customized network state ui."
extra["PUBLISH_URL"] = "https://github.com/SakurajimaMaii/VastUtils/tree/master/libraries/VastNetStateLayout"

apply(from = "${rootProject.projectDir}/publish-mavencentral.gradle")