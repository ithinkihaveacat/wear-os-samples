plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
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
    implementation(libs.ui.tooling.preview)
    implementation(libs.compose.material)
    implementation(libs.compose.foundation)
    implementation(libs.wear.tooling.preview)
    implementation(libs.activity.compose)
    implementation(libs.core.splashscreen)
    implementation(libs.tiles)
    implementation(libs.tiles.material)
    implementation(libs.tiles.tooling.preview)
    implementation(libs.horologist.compose.tools)
    implementation(libs.horologist.tiles)
    implementation(libs.watchface.complications.data.source.ktx)
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.ui.test.junit4)
    debugImplementation(libs.ui.tooling)
    debugImplementation(libs.ui.test.manifest)
    debugImplementation(libs.tiles.tooling)
    // Horologist provides helpful wrappers for Tiles development
    implementation("com.google.android.horologist:horologist-tiles:0.7.12-alpha")

    // Coil for asynchronous image loading
    implementation("io.coil-kt.coil3:coil:3.3.0")
    implementation("io.coil-kt.coil3:coil-network-okhttp:3.3.0")

    // Java 8+ API desugaring
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.1.5")

    // Core Tile dependencies for creating the service and layouts
    implementation("androidx.wear.tiles:tiles:1.5.0")
    implementation("androidx.wear.protolayout:protolayout-material:1.3.0")
    implementation("androidx.wear.protolayout:protolayout-material3:1.3.0")

    // Remote Compose Dependencies
    implementation("androidx.compose.remote:remote-core:1.0.0-SNAPSHOT")
    implementation("androidx.compose.remote:remote-creation:1.0.0-SNAPSHOT")
    implementation("androidx.compose.remote:remote-creation-compose:1.0.0-SNAPSHOT")

    implementation("androidx.wear.compose.remote:remote-material3:1.0.0-SNAPSHOT")
    implementation("androidx.glance.wear:wear-core:1.0.0-SNAPSHOT")
    implementation("androidx.glance.wear:wear:1.0.0-SNAPSHOT")

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
