# Generating WearWidget Previews

This document contains exhaustive details on how the WearWidget previews were
successfully generated. Use this as a known-good reference to debug issues on
other systems where preview generation fails.

## Repository Setup

### 1. `compose-ai-tools`

The renderer and Gradle plugin that extract and generate the screenshots.

- **Path**: `/Users/stillers/workspace/compose-ai-tools`
- **Branch**: `wear-widget-previews`
- **Remote**: `git@github.com:ithinkihaveacat/compose-ai-tools.git` (origin),
  `https://github.com/yschimke/compose-ai-tools.git` (upstream)

**Installation**: Before previews can be generated in the consumer project, the
plugin must be published to your local Maven repository (`~/.m2/repository`).
Run the following from the root of the `compose-ai-tools` project:

```bash
./gradlew :gradle-plugin:publishToMavenLocal
```

This publishes the `ee.schimke.composeai.preview` plugin (version
`0.10.4-SNAPSHOT`).

### 2. `WearWidget`

The consumer Android project containing the Wear OS Widget composables.

- **Path**: `/Users/stillers/workspace/wear-os-samples/WearWidget`
- **Branch**: `compose-ai-tools-tmp`
- **Remote**: `git@github.com:ithinkihaveacat/wear-os-samples.git` (origin),
  `https://github.com/android/wear-os-samples.git` (upstream)

**Configuration**:

1. `settings.gradle.kts` MUST include `mavenLocal()` inside both
   `pluginManagement.repositories` and
   `dependencyResolutionManagement.repositories` so it can find the locally
   published plugin.
1. `app/build.gradle.kts` MUST apply the plugin using:
   ```kotlin
   plugins {
       id("ee.schimke.composeai.preview") version "0.10.4-SNAPSHOT"
   }
   ```
1. `app/build.gradle.kts` MUST include the annotations dependency:
   ```kotlin
   implementation("ee.schimke.composeai:preview-annotations:0.10.4-SNAPSHOT")
   ```

**Execution**: Run the following from the root of the `WearWidget` project to
generate the previews:

```bash
./gradlew :app:renderAllPreviews
```

*Note: If previews have already been generated and cached, you may need to
append `--rerun-tasks` to force regeneration.*

**Outputs**: Rendered `.png` files will be output to:
`app/build/compose-previews/renders/`

______________________________________________________________________

## Environment Information

The following environment variables and tool versions reflect the exact setup of
the working machine.

### System Information

- **OS**: Mac OS X 26.4.1 aarch64 (Darwin)

### Java & Gradle

- **Gradle**: 9.4.1
- **Kotlin**: 2.3.0
- **Groovy**: 4.0.29
- **Launcher JVM**: 21.0.10 (JetBrains s.r.o. 21.0.10+-117844308-b1163.108)
- **Daemon JVM**:
  `/Applications/Android Studio Preview.app/Contents/jbr/Contents/Home`

### Critical Environment Variables

- `JAVA_HOME`:
  `/Applications/Android Studio Preview.app/Contents/jbr/Contents/Home`
- `ANDROID_HOME`: `/Users/stillers/.local/share/android-sdk`
- `ANDROID_JAR`:
  `/Users/stillers/.local/share/android-sdk/platforms/android-36/android.jar`
- `ANDROID_SERIAL`: `emulator-5554`

### Path

The `PATH` variable includes paths to standard binaries, brew installations, and
critically, the Android SDK tools:

- `/Users/stillers/.local/share/android-sdk/build-tools/36.1.0`
- `/Users/stillers/.local/share/android-sdk/emulator`
- `/Users/stillers/.local/share/android-sdk/cmdline-tools/latest/bin`
- `/Users/stillers/.local/share/android-sdk/platform-tools`

### Equivalent to `compose-preview doctor`

If checking manually, ensure your JVM matches (Java 17/21 is recommended
depending on the specific tooling, but here 21.0.10 was used successfully) and
that Robolectric SDKs are available locally or can be resolved. The Robolectric
SDK version used internally by the renderer is SDK `35`, while the app's
`compileSdk` is `37`.
