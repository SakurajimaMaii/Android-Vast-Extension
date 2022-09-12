import com.gcode.plugin.version.*

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("com.gcode.plugin.version")
}

android {
    compileSdk = Version.compile_sdk_version

    defaultConfig {
        minSdk = Version.min_sdk_version
        targetSdk = Version.target_sdk_version

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"),"proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    implementation(project(":libraries:VastTools"))
    implementation(AndroidX.recyclerview)
    implementation(Google.material)
}

extra["PUBLISH_GROUP_ID"] = "io.github.sakurajimamaii"
extra["PUBLISH_ARTIFACT_ID"] = "VastSwipeListView"
extra["PUBLISH_VERSION"] = "0.0.2"
extra["PUBLISH_DESCRIPTION"] = "A ListView that supports swipe list items."
extra["PUBLISH_URL"] = "https://github.com/SakurajimaMaii/VastUtils/tree/master/libraries/VastSwipeListView"

apply(from = "${rootProject.projectDir}/publish-mavencentral.gradle")