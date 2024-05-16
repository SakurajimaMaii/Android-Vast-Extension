import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("java-library")
    id("convention.publication")
    alias(libs.plugins.kotlinJvm)
}

group = "io.github.sakurajimamaii"
version = "1.3.1"

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
    implementation(libs.junit)
    testImplementation(libs.gson)
    testImplementation(libs.fastjson2.kotlin)
    testImplementation(libs.jackson.databind)
    testImplementation(libs.fastjson2)
}

extra["PUBLISH_ARTIFACT_ID"] = "log-core"
extra["PUBLISH_DESCRIPTION"] = "Desktop for log"
extra["PUBLISH_URL"] =
    "https://github.com/SakurajimaMaii/Android-Vast-Extension/tree/develop/libraries/log/desktop"

publishing {
    publications {
        register<MavenPublication>("release") {
            groupId = "io.github.sakurajimamaii"
            artifactId = "log-desktop"
            version = "1.3.1"

            afterEvaluate {
                from(components["java"])
            }
        }
    }
}