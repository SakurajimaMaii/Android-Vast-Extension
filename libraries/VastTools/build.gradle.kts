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

// Groovy --> KTS
// https://cloud.tencent.com/developer/article/1839887
// https://mp.weixin.qq.com/s/mVqShijGTExtQ_nLslchpQ

import com.pluginversion.vastgui.AVE
import com.pluginversion.vastgui.AndroidX
import com.pluginversion.vastgui.Google
import com.pluginversion.vastgui.Jetbrains
import com.pluginversion.vastgui.Libraries
import com.pluginversion.vastgui.Squareup
import com.pluginversion.vastgui.Version
import org.jetbrains.dokka.DokkaConfiguration.Visibility
import org.jetbrains.dokka.gradle.DokkaTaskPartial

plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
    id("org.jetbrains.kotlin.android")
    id("com.pluginversion.vastgui")
    id("org.sonarqube") version "3.4.0.2513"
    id("convention.publication")
    id("org.jetbrains.dokka") version "1.8.10"
}

android {
    namespace = "com.ave.vastgui.tools"
    compileSdk = Version.compile_sdk_version

    defaultConfig {
        minSdk = Version.min_sdk_version

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"),"proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = Version.java_version
        targetCompatibility = Version.java_version
    }

    kotlinOptions {
        jvmTarget = Version.java_version.toString()
        // https://stackoverflow.com/questions/53964192/jvmdefault-and-how-add-compiler-option
        freeCompilerArgs = listOf("-Xjvm-default=all")
    }

    viewBinding.isEnabled = true

    // https://www.jianshu.com/p/2a539b497460
    // https://www.lordcodes.com/tips/kotlin-source-directory-gradle-kotlin-dsl
    sourceSets {
        getByName("main").java.srcDirs("src/main/kotlin")
    }

    publishing {
        singleVariant("release") {
            withSourcesJar()
        }
    }
}

//project(":libraries:VastTools"){
//    sonarqube {
//        properties {
//            property("sonar.sourceEncoding", "UTF-8")
//            property("sonar.projectKey", "VastTools")
//            property("sonar.projectName", "VastTools")
//            property("sonar.sources", "src")
//            property("sonar.projectVersion", project.version)
//        }
//    }
//}

dependencies {
    api(AndroidX.security_crypto)
    api(Libraries.zxing)
    api(AVE.core)
    implementation(AndroidX.activity)
    implementation(AndroidX.activity_ktx)
    implementation(AndroidX.annotation)
    implementation(AndroidX.appcompat)
    implementation(AndroidX.appcompat_resources)
    implementation(AndroidX.arch_core_runtime)
    implementation(AndroidX.constraintlayout)
    implementation(AndroidX.core_ktx)
    implementation(AndroidX.datastore)
    implementation(AndroidX.exifinterface)
    implementation(AndroidX.fragment_ktx)
    implementation(AndroidX.lifecycle_livedata_core_ktx)
    implementation(AndroidX.lifecycle_livedata_ktx)
    implementation(AndroidX.lifecycle_runtime_ktx)
    implementation(AndroidX.lifecycle_viewmodel_ktx)
    implementation(AndroidX.recyclerview)
    implementation(AndroidX.test_monitor)
    implementation(Google.gson)
    implementation(Google.material)
    implementation(Jetbrains.kotlin_reflect)
    implementation(Jetbrains.kotlin_stdlib)
    implementation(Jetbrains.kotlinx_coroutines_android)
    implementation(Jetbrains.kotlinx_coroutines_core)
    implementation(Squareup.okhttp3)
    implementation(Squareup.retrofit2)
    implementation(Squareup.retrofit2_adapter_rxjava3)
    implementation(Squareup.retrofit2_converter_gson)
}

tasks.withType<DokkaTaskPartial>().configureEach {
    dokkaSourceSets.configureEach {
        jdkVersion.set(17)
        languageVersion.set("1.8.10")
        suppressInheritedMembers.set(true)
        documentedVisibilities.set(
            setOf(Visibility.PUBLIC, Visibility.PROTECTED)
        )
    }
}

extra["PUBLISH_ARTIFACT_ID"] = "VastTools"
extra["PUBLISH_DESCRIPTION"] = "Easy Quick Android Tools for you to faster project development."
extra["PUBLISH_URL"] = "https://github.com/SakurajimaMaii/VastUtils/tree/release/libraries/VastTools"

publishing {
    publications {
        register<MavenPublication>("release") {
            groupId = "io.github.sakurajimamaii"
            artifactId = "VastTools"
            version = "0.5.1"

            afterEvaluate {
                from(components["release"])
            }
        }
    }
}