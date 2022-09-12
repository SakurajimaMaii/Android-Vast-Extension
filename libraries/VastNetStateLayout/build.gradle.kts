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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
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