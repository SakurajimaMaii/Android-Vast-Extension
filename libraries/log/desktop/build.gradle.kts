/*
 * Copyright 2021-2024 VastGui
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.jetbrains.dokka.gradle.DokkaTaskPartial
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.net.URL

plugins {
    id("java-library")
    id("convention.publication")
    alias(libs.plugins.kotlinJvm)
}

group = "io.github.sakurajimamaii"
version = libs.versions.log.local.get()

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
    withSourcesJar()
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
}

sourceSets["main"].java.srcDir("src/main/kotlin")

kotlin.sourceSets.all {
    languageSettings.optIn("com.log.vastgui.core.annotation.LogApi")
}

dependencies {
    implementation(projects.libraries.log.core)
    testImplementation(libs.junit)
    testImplementation(libs.gson)
    testImplementation(libs.fastjson2.kotlin)
    testImplementation(libs.jackson.databind)
    testImplementation(libs.fastjson2)
}

extra["PUBLISH_ARTIFACT_ID"] = "log-desktop"
extra["PUBLISH_DESCRIPTION"] = "Desktop for log"
extra["PUBLISH_URL"] = "https://github.com/SakurajimaMaii/Android-Vast-Extension/tree/develop/libraries/log/desktop"

publishing {
    publications {
        register<MavenPublication>("release") {
            groupId = "io.github.sakurajimamaii"
            artifactId = "log-desktop"
            version = libs.versions.log.local.get()

            afterEvaluate {
                from(components["java"])
            }
        }
    }
}

tasks.withType<DokkaTaskPartial> {
    moduleName.set("log-desktop")
    dokkaSourceSets.configureEach {
        sourceLink {
            localDirectory.set(projectDir.resolve("src"))
            remoteUrl.set(URL("https://github.com/SakurajimaMaii/Android-Vast-Extension/blob/develop/libraries/log/desktop/src"))
            remoteLineSuffix.set("#L")
        }
    }
}