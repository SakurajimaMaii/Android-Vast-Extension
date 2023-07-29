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

import com.pluginversion.vastgui.Jetbrains
import com.pluginversion.vastgui.Version
import org.jetbrains.dokka.DokkaConfiguration.Visibility
import org.jetbrains.dokka.gradle.DokkaTaskPartial
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("java-library")
    id("org.jetbrains.kotlin.jvm")
    id("com.pluginversion.vastgui")
    id("convention.publication")
    id("org.jetbrains.dokka")
}

subprojects {
    apply(plugin = "org.jetbrains.dokka")
}

group = "io.github.sakurajimamaii"
version = "0.0.4"

java {
    sourceCompatibility = Version.java_version
    targetCompatibility = Version.java_version
}

tasks.withType<KotlinCompile>{
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
}

sourceSets["main"].java.srcDir("src/main/kotlin")

dependencies {
    implementation(Jetbrains.kotlin_reflect)
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

extra["PUBLISH_ARTIFACT_ID"] = "VastCore"
extra["PUBLISH_DESCRIPTION"] = "Core for Android-Vast-Extension"
extra["PUBLISH_URL"] = "https://github.com/SakurajimaMaii/Android-Vast-Extension/tree/develop/libraries/VastCore"

publishing {
    publications {
        register<MavenPublication>("release") {
            groupId = "io.github.sakurajimamaii"
            artifactId = "VastCore"
            version = "0.0.4"
        }
    }
}