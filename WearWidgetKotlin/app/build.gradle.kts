plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.dependency.analysis)
}

android {
    namespace = "com.google.example.wear_widget"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.google.example.wear_widget"
        minSdk = 33
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    useLibrary("wear-sdk")
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(libs.play.services.wearable)
    implementation(platform(libs.compose.bom))
    implementation(libs.ui)
    implementation(libs.ui.graphics)
    
    // Transitive dependencies declared directly
    implementation("androidx.compose.runtime:runtime:1.9.4")

    debugImplementation(libs.ui.tooling)

    // Java 8+ API desugaring
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.1.5")

    // Remote Compose Dependencies
    implementation("androidx.compose.remote:remote-creation-compose:1.0.0-SNAPSHOT")

    implementation("androidx.glance.wear:wear-core:1.0.0-SNAPSHOT")
    implementation("androidx.glance.wear:wear:1.0.0-SNAPSHOT")

    // Tooling dependencies for previewing tiles in Android Studio.
    debugImplementation("androidx.compose.ui:ui-tooling:1.9.4")
}
