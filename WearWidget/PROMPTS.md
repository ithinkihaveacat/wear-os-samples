# Agent Task: Set up Wear OS Remote Demos Standalone App

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
resources:

1. **Configure Gradle Settings:** Create a `settings.gradle.kts` file naming the
   project (e.g., `rootProject.name = "wear-remote-demos"`). Under
   `dependencyResolutionManagement.repositories`, you **must** register the
   AndroidX Dev Snapshots repository to retrieve the latest compatible Remote
   Compose builds:
   ```kotlin
   repositories {
       google()
       mavenCentral()
       maven("https://androidx.dev/snapshots/latest/artifacts/repository")
   }
   ```
1. **Create Build Configuration:** Create a `build.gradle.kts` file at the root.
   Configure it as a Wear OS application (using `com.android.application` and
   `org.jetbrains.kotlin.plugin.compose` plugins) with `compileSdk = 37` and JVM
   toolchain version 17.
1. **Resolve Dependencies:** To prevent runtime binary compatibility (ABI)
   crashes, depend on the **`1.0.0-SNAPSHOT`** versions of all Remote Compose
   and Material 3 remote libraries. Use a `resolutionStrategy` to force these
   exact versions:
   ```kotlin
   dependencies {
       implementation("androidx.wear.compose.remote:remote-material3:1.0.0-SNAPSHOT")
       implementation("androidx.compose.remote:remote-creation-compose:1.0.0-SNAPSHOT")
       // ... other core remote compose dependencies ...
   }
   configurations.all {
       resolutionStrategy {
           force("androidx.wear.compose.remote:remote-material3:1.0.0-SNAPSHOT")
           force("androidx.compose.remote:remote-creation-compose:1.0.0-SNAPSHOT")
           // ... force all other compose.remote dependencies to 1.0.0-SNAPSHOT ...
       }
   }
   ```
1. **API Casing Precaution:** Do not rename the lowercase `valueChange(...)`
   factory calls in the samples to capitalized `ValueChange(...)`. The official
   snapshot artifacts expose the lowercase factory signature, and changing them
   will break compilation.
1. **Declare Dummy Sampled Annotation:** The monorepo code references a built-in
   AOSP documentation annotation `@Sampled`. To compile successfully without
   internal toolchains, declare a dummy annotation
   `src/main/java/androidx/annotation/Sampled.kt`:
   ```kotlin
   package androidx.annotation
   @Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
   @Retention(AnnotationRetention.BINARY)
   annotation class Sampled
   ```
1. **Create Manifest:** Create a standard `src/main/AndroidManifest.xml` file
   that defines the application and registers `MainActivity` as the launchable
   entry point.
1. **Add Missing Resources:** Copy or generate default launcher icon XMLs and
   check for referenced Android XML vector drawables (like
   `gs_map_wght500rond100_vd_theme_24.xml`) under `src/main/res/drawable/` so
   that all resource compilation links cleanly.

Verify that the resulting project compiles cleanly using standard Gradle CLI
tools:

```bash
./gradlew assembleDebug
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
