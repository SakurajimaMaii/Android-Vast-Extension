import com.gcode.plugin.version.*

plugins {
    id("com.android.application")
    id("com.gcode.plugin.version")
}

android {

    namespace = "com.gcode.vastutilsjava"
    compileSdk = Version.compile_sdk_version

    defaultConfig {
        applicationId = "com.gcode.vastutilsjava"
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

    buildFeatures {
        viewBinding = true
        dataBinding = true
    }

}

dependencies {
    implementation(project(":libraries:VastTools"))
    implementation(project(":libraries:VastAdapter"))
    implementation(Google.material)
    implementation(AndroidX.core_splashscreen)
}