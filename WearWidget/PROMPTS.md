# Agent Task: Set up Wear OS Remote Demos Standalone App (Updated)

You are an agent tasked with extracting the Wear OS integration demos from the
AndroidX monorepo and setting them up as a standalone, buildable Android
application.

Follow these steps to complete the task:

______________________________________________________________________

## Step 1: Download the Sample Code

Execute the following commands in a standard shell to download and extract the
raw Kotlin source files from the AndroidX monorepo:

```bash
mkdir -p wear-remote-demos && cd wear-remote-demos
curl -sSL "https://android.googlesource.com/platform/frameworks/support/+archive/refs/heads/androidx-main/wear/compose/remote/integration-tests/demos.tar.gz" | tar -xz
```

______________________________________________________________________

## Step 2: Transform into a Standalone App

Using the downloaded directory containing the raw Kotlin sources, transform it
into a fully compilable and runnable Wear OS Android application project by
generating the standard Gradle build system, Android Manifest, and necessary
resources.

### 1. Configure Gradle Settings (`settings.gradle.kts`)

Create a `settings.gradle.kts` file at the root. You **must** register the
AndroidX Dev Snapshots repository to retrieve the latest compatible Remote
Compose builds:

```kotlin
pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://androidx.dev/snapshots/latest/artifacts/repository")
    }
}
rootProject.name = "wear-remote-demos"
```

### 2. Modernized Build Configuration (`build.gradle.kts`)

Create a `build.gradle.kts` file at the root. Configure it as a Wear OS
application. Note that with **AGP 9.0+**, Kotlin support is built-in. Do **NOT**
apply the `org.jetbrains.kotlin.android` plugin explicitly. Apply the Compose
compiler plugin (matching the bundled Kotlin version) and the Compose Preview
plugin:

```kotlin
plugins {
    id("com.android.application") version "9.1.0"
    id("org.jetbrains.kotlin.plugin.compose") version "2.2.10" // Matches AGP 9.1.0 bundled Kotlin
    id("ee.schimke.composeai.preview") version "0.13.4"
}

android {
    namespace = "androidx.wear.compose.remote.integration.demos"
    compileSdk = 37 // Required by latest Compose snapshots

    defaultConfig {
        applicationId = "androidx.wear.compose.remote.integration.demos"
        minSdk = 29
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlin {
        jvmToolchain(17)
    }

    buildFeatures {
        compose = true
    }
}
```

### 3. Resolve Dependencies

To prevent runtime binary compatibility (ABI) crashes and compile against the
latest AOSP changes, depend on the **`1.0.0-SNAPSHOT`** versions of all Remote
Compose and Material 3 remote libraries. Use a `resolutionStrategy` to force
these exact versions:

```kotlin
dependencies {
    // Remote Compose dependencies (forced to 1.0.0-SNAPSHOT)
    implementation("androidx.compose.remote:remote-core:1.0.0-SNAPSHOT")
    implementation("androidx.compose.remote:remote-creation:1.0.0-SNAPSHOT")
    implementation("androidx.compose.remote:remote-creation-compose:1.0.0-SNAPSHOT")
    implementation("androidx.compose.remote:remote-creation-core:1.0.0-SNAPSHOT")
    implementation("androidx.compose.remote:remote-player-compose:1.0.0-SNAPSHOT")
    implementation("androidx.compose.remote:remote-player-view:1.0.0-SNAPSHOT")
    implementation("androidx.compose.remote:remote-player-core:1.0.0-SNAPSHOT")
    implementation("androidx.compose.remote:remote-tooling-preview:1.0.0-SNAPSHOT")
    
    implementation("androidx.wear.compose.remote:remote-material3:1.0.0-SNAPSHOT")
    implementation("androidx.wear.compose.remote:remote-material3-samples:1.0.0-SNAPSHOT")

    // Core AndroidX dependencies
    implementation("androidx.datastore:datastore-preferences:1.2.1")
    implementation("androidx.activity:activity-compose:1.11.0")
    
    // Compose dependencies
    implementation("androidx.compose.runtime:runtime:1.11.0")
    implementation("androidx.compose.ui:ui:1.11.0")
    implementation("androidx.compose.ui:ui-graphics:1.11.0")
    implementation("androidx.compose.ui:ui-tooling:1.11.0")
    implementation("androidx.compose.ui:ui-tooling-preview:1.11.0")
    
    // Wear Compose dependencies
    implementation("androidx.wear.compose:compose-foundation:1.5.5")
    implementation("androidx.wear.compose:compose-material3:1.5.5")
    implementation("androidx.wear.compose:compose-navigation:1.5.6")
    implementation("androidx.wear.compose:compose-ui-tooling:1.5.5")
    implementation("androidx.wear:wear-tooling-preview:1.0.0")
}

configurations.all {
    resolutionStrategy {
        force("androidx.compose.remote:remote-core:1.0.0-SNAPSHOT")
        force("androidx.compose.remote:remote-creation:1.0.0-SNAPSHOT")
        force("androidx.compose.remote:remote-creation-compose:1.0.0-SNAPSHOT")
        force("androidx.compose.remote:remote-creation-core:1.0.0-SNAPSHOT")
        force("androidx.compose.remote:remote-player-compose:1.0.0-SNAPSHOT")
        force("androidx.compose.remote:remote-player-view:1.0.0-SNAPSHOT")
        force("androidx.compose.remote:remote-player-core:1.0.0-SNAPSHOT")
        force("androidx.compose.remote:remote-tooling-preview:1.0.0-SNAPSHOT")
        
        force("androidx.wear.compose.remote:remote-material3:1.0.0-SNAPSHOT")
        force("androidx.wear.compose.remote:remote-material3-samples:1.0.0-SNAPSHOT")
    }
}
```

### 4. Integrate Missing `testutils` Locally (Critical Workaround)

The required library `androidx.compose.remote:remote-player-compose-testutils`
is **not published** to Maven (even as a snapshot). You must extract the
required typeface resolvers from AOSP and integrate them locally:

1. Download the `remote-player-compose-testutils` source from AOSP:
   ```bash
   curl -sSL "https://android.googlesource.com/platform/frameworks/support/+archive/refs/heads/androidx-main/compose/remote/remote-player-compose-testutils.tar.gz" | tar -xz -C temp-testutils
   ```
1. Copy the following files to your project under
   `src/main/java/androidx/compose/remote/player/compose/test/utils/`:
   - `DownloadableTypefaceResolver.kt`
   - `FallbackCreateTypefaceResolver.kt`
   - `RemappingTypefaceResolver.kt`
   - `SimpleFontInstance.kt`
1. Copy the resource file `default_gms_fonts_certs.xml` to
   `src/main/res/values/`.
1. **Important:** Add the following import to `DownloadableTypefaceResolver.kt`
   so it can resolve the copied resources:
   ```kotlin
   import androidx.wear.compose.remote.integration.demos.R
   ```
1. Clean up the temporary download: `rm -rf temp-testutils`.

### 5. API Casing Precaution

Do not rename the lowercase `valueChange(...)` factory calls in the samples to
capitalized `ValueChange(...)`. The official snapshot artifacts expose the
lowercase factory signature, and changing them will break compilation.

### 6. Declare Dummy Sampled Annotation

The monorepo code references a built-in AOSP documentation annotation
`@Sampled`. To compile successfully, declare a dummy annotation
`src/main/java/androidx/annotation/Sampled.kt`:

```kotlin
package androidx.annotation
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.BINARY)
annotation class Sampled
```

### 7. Create Manifest

Ensure `src/main/AndroidManifest.xml` (included in the download) registers
`MainActivity` as the launchable entry point.

### 8. Verify Build

Verify that the project compiles cleanly using Gradle:

```bash
JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64 ./gradlew assembleDebug
```

______________________________________________________________________

## Step 3: Capture Component Screenshots

Once the application builds cleanly, generate a visual gallery of all 11
component demo screens. The target outcome is a new directory containing a
descriptive screenshot or PNG image for each widget demo.

You may achieve this outcome using any preferred solution, for example:

- Running the application on a target Wear OS emulator or device and
  systematically capturing screenshots of each navigated screen.
- Rendering Compose `@Preview` annotations or component layouts directly to PNG
  files using local UI testing, Compose Previews, or screenshot testing utility
  frameworks.
