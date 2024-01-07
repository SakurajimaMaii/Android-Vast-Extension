import com.android.build.gradle.internal.api.BaseVariantOutputImpl

plugins {
    id("com.android.application")
    id("kotlin-android")
}

android {
    namespace = "com.ave.vastgui.appskin"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.ave.vastgui.appskin"
        minSdk = 23
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }

    sourceSets["main"].java.srcDirs("src/main/kotlin")

    // https://stackoverflow.com/questions/50792428/how-to-access-variant-outputfilename-in-kotlin
    applicationVariants.all {
        outputs.map { it as BaseVariantOutputImpl }.forEach { output ->
            val outputFileName = "app-skin.skin"
            output.outputFileName = outputFileName
        }
    }
}

dependencies {
    implementation(libs.material)
}