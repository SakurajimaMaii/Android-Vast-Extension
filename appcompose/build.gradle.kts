import com.gcode.plugin.version.*

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.gcode.plugin.version")
}

android {
    namespace = "com.gcode.vastutilscompose"
    compileSdk = Version.compile_sdk_version

    defaultConfig {
        applicationId = "com.gcode.vastutilscompose"
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
        debug {
            isDebuggable = true
            isJniDebuggable = true
            isShrinkResources = false
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.3.0"
    }

    sourceSets["main"].java.srcDir("src/main/kotlin")
}

dependencies {
    implementation(project(":libraries:VastTools"))

    implementation(Compose.compose_foundation)
    implementation(Compose.compose_foundation_layout)
    implementation(Compose.compose_activity)
    implementation(Compose.compose_ui)
    implementation(Compose.compose_ui_tooling_preview)
    implementation(Compose.compose_material3)
    debugImplementation(Compose.compose_ui_tooling)
    debugImplementation(Compose.compose_ui_test_manifest)
    androidTestImplementation(Compose.compose_ui_test_junit4)

    implementation(AndroidX.lifecycle_runtime_ktx)
}