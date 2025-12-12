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
    implementation(libs.runtime)

    debugImplementation(libs.ui.tooling)

    // Java 8+ API desugaring
    coreLibraryDesugaring(libs.desugar.jdk.libs)

    // Remote Compose Dependencies
    implementation(libs.remote.creation.compose)
    implementation(libs.remote.material3)
    implementation(libs.remote.core)

    implementation(libs.wear.core)
    implementation(libs.wear)

    // Tooling dependencies for previewing tiles in Android Studio.
    debugImplementation(libs.androidx.ui.tooling)
    implementation(libs.ui.tooling.preview)
    implementation(libs.wear.compose.ui.tooling)
    implementation(libs.wear.tooling.preview)
    implementation(libs.remote.tooling.preview)


    // Tooling dependencies for previewing tiles in Android Studio.
    implementation("androidx.wear.tiles:tiles-tooling:1.5.0")
    debugImplementation("androidx.wear.tiles:tiles-renderer:1.5.0")
    debugImplementation("androidx.compose.ui:ui-tooling:1.9.4")
    debugImplementation("androidx.wear:wear-tooling-preview:1.0.0")
    debugImplementation("androidx.wear.tiles:tiles-tooling:1.5.0")
    // The tile preview code is in the same file as the tiles themselves, so we need to make the
    // androidx.wear.tiles:tiles-tooling-preview dependency available to release builds, not
    // just debug builds.
    implementation("androidx.wear.tiles:tiles-tooling-preview:1.5.0")
}
