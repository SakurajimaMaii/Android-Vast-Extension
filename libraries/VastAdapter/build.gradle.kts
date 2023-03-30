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
import com.pluginversion.vastgui.Jetbrains
import com.pluginversion.vastgui.Version

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("com.pluginversion.vastgui")
    id("convention.publication")
}

android {
    namespace = "com.ave.vastgui.adapter"
    compileSdk = Version.compile_sdk_version

    defaultConfig {
        minSdk = Version.min_sdk_version

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        freeCompilerArgs = listOf("-Xjvm-default=all-compatibility")
    }

    buildFeatures {
        dataBinding = true
    }

    sourceSets["main"].java.srcDir("src/main/kotlin")

    publishing {
        singleVariant("release") {
            withSourcesJar()
        }
    }
}

dependencies {
    api(AVE.core)
    implementation(AndroidX.annotation)
    implementation(AndroidX.arch_core_runtime)
    implementation(AndroidX.core_ktx)
    implementation(AndroidX.recyclerview)
    implementation(AndroidX.paging3)
    implementation(Jetbrains.kotlin_reflect)
}

extra["PUBLISH_ARTIFACT_ID"] = "VastAdapter"
extra["PUBLISH_DESCRIPTION"] = "Help you quickly build an Adapter suitable for RecyclerView."
extra["PUBLISH_URL"] = "https://github.com/SakurajimaMaii/Android-Vast-Extension/tree/develop/libraries/VastAdapter"

publishing {
    publications {
        register<MavenPublication>("release") {
            groupId = "io.github.sakurajimamaii"
            artifactId = "VastAdapter"
            version = "0.1.0"

            afterEvaluate {
                from(components["release"])
            }
        }
    }
}