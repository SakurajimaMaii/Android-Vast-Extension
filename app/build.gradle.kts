import com.gcode.plugin.version.*

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("org.jetbrains.kotlin.android")
    id("com.gcode.plugin.version")
}

android {

    signingConfigs {

        getByName("debug") {
            storeFile = File("D:\\AndroidKey\\VastUtils.jks")
            storePassword = project.property("myStorePassword") as String?
            keyPassword = project.property("myKeyPassword") as String?
            keyAlias = project.property("myKeyAlias") as String?
        }

        create("release") {
            storeFile = File("D:\\AndroidKey\\VastUtils.jks")
            storePassword = project.property("myStorePassword") as String?
            keyPassword = project.property("myKeyPassword") as String?
            keyAlias = project.property("myKeyAlias") as String?
        }
    }

    compileSdk = Version.compile_sdk_version

    defaultConfig {
        applicationId = "com.gcode.vastutils"
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
        viewBinding = true
        dataBinding = true
    }

    namespace = "com.gcode.vastutils"

    sourceSets["main"].java.srcDir("src/main/kotlin")

}

dependencies {
    implementation(project(":libraries:VastNetStateLayout"))
    implementation(project(":libraries:VastTools"))
    implementation(project(":libraries:VastAdapter"))
    implementation(AndroidX.constraintlayout)
    implementation(AndroidX.core_splashscreen)
    implementation(Google.material)
    implementation(Libraries.progressmanager)
}