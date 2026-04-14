plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    // alias(libs.plugins.dependency.analysis)
}

android {
    namespace = "com.google.example.wear_widget"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.google.example.wear_widget"
        minSdk = 33
        targetSdk = 37
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
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
            all { test ->
                test.systemProperty("roborazzi.test.record", "true")
                if (System.getProperty("robo.screenshot.target") != null) {
                    test.systemProperty("robo.screenshot.target", System.getProperty("robo.screenshot.target"))
                }
            }
        }
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

    implementation(libs.remote.creation.core)
    implementation(libs.remote.creation)

    // Tooling dependencies for previewing tiles in Android Studio.
    implementation(libs.wear.tiles.tooling)
    debugImplementation(libs.wear.tiles.renderer)
    // ui-tooling is already added above via libs.ui.tooling (debugImplementation) and libs.androidx.ui.tooling
    // debugImplementation("androidx.compose.ui:ui-tooling:1.9.4") 
    // wear-tooling-preview is already added above via libs.wear.tooling.preview
    // debugImplementation("androidx.wear:wear-tooling-preview:1.0.0")
    // Tiles tooling repeated in original file, removing duplicate.
    
    // The tile preview code is in the same file as the tiles themselves, so we need to make the
    // androidx.wear.tiles:tiles-tooling-preview dependency available to release builds, not
    // just debug builds.
    implementation(libs.wear.tiles.tooling.preview)
    implementation(libs.activity.compose)
    implementation(libs.wear.compose.material)
    implementation(libs.wear.compose.material3)
    implementation(libs.wear.compose.foundation)
    implementation(libs.datastore.preferences)

    testImplementation(enforcedPlatform(libs.compose.bom))
    debugImplementation("androidx.compose.ui:ui-graphics-android:1.7.8")
    debugImplementation("androidx.compose.ui:ui-android:1.7.8")
    testImplementation(libs.junit)
    testImplementation(libs.ext.junit)
    testImplementation(libs.robolectric) {
        exclude(group = "org.conscrypt", module = "conscrypt-openjdk-uber")
    }
    testImplementation(libs.roborazzi)
    testImplementation(libs.roborazzi.compose)
    testImplementation(libs.roborazzi.rule)
    testImplementation(libs.compose.ui.test.junit4)
    debugImplementation(libs.compose.ui.test.manifest)
}

tasks.register("collectPreviewInfo") {
    doLast {
        val buildDir = layout.buildDirectory.get().asFile
        val classDirsList = mutableListOf<String>()
        if (buildDir.exists()) {
            buildDir.walkTopDown()
                .maxDepth(12)
                .filter { it.isDirectory && it.name == "classes" }
                .forEach { classesDir ->
                    val path = classesDir.path.lowercase().replace('\\', '/')
                    val isDebugVariant = path.contains("/debug/") || path.contains("/debugunittest/") || path.contains("/main/")
                    val isAndroidTest = path.contains("/androidtest/")
                    val isRelease = path.contains("/release/")
                    val isKotlinOrJava = path.contains("kotlin") || path.contains("javac")
                    if (isDebugVariant && !isAndroidTest && !isRelease && isKotlinOrJava) {
                        classDirsList.add(classesDir.path)
                    }
                }
            buildDir.walkTopDown()
                .maxDepth(12)
                .filter { it.isDirectory && it.name == "out" && it.parentFile.name.contains("Config") }
                .forEach { configDir ->
                    val testConfig = File(configDir, "com/android/tools/test_config.properties")
                    if (testConfig.exists()) {
                        classDirsList.add(configDir.path)
                    }
                }
        }
        
        val attr = Attribute.of("artifactType", String::class.java)
        val configuration = configurations.findByName("debugUnitTestRuntimeClasspath")
            ?: configurations.findByName("debugRuntimeClasspath")
            ?: configurations.findByName("runtimeClasspath")
        
        val deps = configuration?.incoming?.artifactView { attributes.attribute(attr, "jar") }?.files?.map { it.path } ?: emptyList()
        val moduleRoot = projectDir.path
        val mergedManifest = File(buildDir, "intermediates/merged_manifests/debug/processDebugManifest/AndroidManifest.xml")
            .takeIf { it.exists() }?.path ?: ""
        val resourceApk = File(buildDir, "intermediates/linked_resources_binary_format/debug/processDebugResources/linked-resources-binary-format-debug.ap_")
            .takeIf { it.exists() }?.path ?: ""
        val rClassJar = File(buildDir, "intermediates/compile_and_runtime_r_class_jar/debug/processDebugResources/R.jar")
            .takeIf { it.exists() }?.path ?: ""

        val outputFile = file("build/preview/project-info.txt")
        outputFile.parentFile.mkdirs()
        outputFile.writeText(
            classDirsList.joinToString(File.pathSeparator) + "\n" +
            deps.joinToString(File.pathSeparator) + "\n" +
            moduleRoot + "\n" +
            mergedManifest + "\n" +
            resourceApk + "\n" +
            rClassJar
        )
    }
}