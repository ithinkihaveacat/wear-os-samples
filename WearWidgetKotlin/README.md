# Wear Widget Sample

This repository provides sample code illustrating the use of new Wear Widget
libraries (specifically `androidx.compose.remote` and related packages).

## Documentation

- [Getting Started](docs/GETTING-STARTED.md): Detailed guide on building Wear
  Widgets.
- [Components](docs/COMPONENTS.md): Catalog of available components.

## Available Skills

This repository includes specialized skills to assist with development. These
are located in the `.gemini/skills` directory and are designed to work with
agents from various providers:

- **`jetpack`**: Manage AndroidX libraries.
- **`emumanager`**: Manage Android emulators.
- **`ai-analysis`**: AI-powered analysis tools.
- **`adb`**: Wear OS specific ADB commands.

## Quick Start

### 1. Prerequisites: Install the Renderer

**Critical:** To display Wear Widgets correctly, your Wear OS device or emulator
must have a compatible version of the `protolayout` renderer installed.

1. **Check Installed Version:**

   This project requires `com.google.android.wearable.protolayout.renderer`
   version **1.6.1.4.862675839.exp** or later.

   ```bash
   adb shell dumpsys package com.google.android.wearable.protolayout.renderer | \
     grep -m 1 versionName | \
     awk -F= '{print $2}'
   ```

   _If your version matches or is higher, you can skip the rest of this section
   and proceed to **2. Build and Install the App**._

2. **Check Device Architecture:**

   ```bash
   adb shell getprop ro.product.cpu.abi
   ```

   _(e.g., `arm64-v8a` for most physical devices/Mac emulators, `x86_64` for
   Intel/AMD emulators)_

3. **Check Build Type:**

   ```bash
   adb shell getprop ro.build.type
   ```

   _(Returns `releasekey` for user builds or `testkey` for userdebug builds)_

4. **Install the Renderer:** Locate the correct APK in the `renderer/` directory
   (or provided path) matching your ABI and build type.

   ```bash
   # Example
   adb install -g -t -r renderer/renderer_..._arm64-v8a_releasekey_...apk
   ```

5. **Restart System UI:**

   ```bash
   adb shell am force-stop com.google.android.wearable.sysui
   ```

### 2. Build and Install the App

```bash
./gradlew :app:installDebug
```

### 3. Add and Display the Tile

You can add the tile manually via the watch interface (long press on watch face
-> add tile) or use ADB for automation.

**Using ADB:**

````bash
# Add the HelloWidget
```bash
adb shell am broadcast \
  -a com.google.android.wearable.app.DEBUG_SURFACE \
  --es operation add-tile \
  --ecn component com.google.example.wear_widget/.HelloWidgetService

# Show the tile (assuming index 0)
```bash
adb shell am broadcast \
  -a com.google.android.wearable.app.DEBUG_SYSUI \
  --es operation show-tile \
  --ei index 0
````

## Exploring the Catalogs

This sample includes two catalogs: `WidgetCatalog` (full widgets) and
`ComponentCatalog` (individual components).

### Component Catalog (Recommended)

Use the included `component-switch` script to quickly browse different
components without code changes.

```bash
# Switch layouts
./component-switch textButton
./component-switch appCard
```

### Widget Catalog

Use the included `widget-switch` script to quickly browse different full widget
samples.

```bash
# Switch layouts
./widget-switch BoxSample1
./widget-switch SystemThemeSample
```

## Development Tools

### Compose AI Tools Rendering Integration

**Why it's useful:** Accelerates localized testing cycles efficiently using
Compose AI Tools Roborazzi infrastructure.

**Prerequisites:**

- The `compose-ai-tools` repository must be checked out locally. By default, the
  script expects it at `../../compose-ai-tools`. You can override this by
  setting the `COMPOSE_AI_DIR` environment variable.
- Ensure you have checked out the correct commit in `compose-ai-tools`
  (specifically `b3cfc77a6edc7f6367bb1ed3501c7f9ff55f0c47` or a branch
  containing it like `feature/option-2-support`).
- You must publish both the library and the plugin to `mavenLocal`. To do this
  in one go, run the following command from the `compose-ai-tools` root
  directory:

  ```bash
  PLUGIN_VERSION=0.3.3-SNAPSHOT ./gradlew publishToMavenLocal && PLUGIN_VERSION=0.3.3-SNAPSHOT ./gradlew -p gradle-plugin publishToMavenLocal
  ```

  > [!NOTE]
  > The `PLUGIN_VERSION` environment variable ensures that the published artifacts use the version expected by this project (configured in `app/build.gradle.kts`). If you bump the version there, update it here as well.

**Workflow:**

1. Define standard `@Composable` wrapper functions for your `@RemoteComposable`
   widgets:

   ```kotlin
   import androidx.compose.ui.tooling.preview.Preview
   import androidx.compose.remote.creation.profile.RcPlatformProfiles.WEAR_WIDGETS
   import androidx.compose.remote.tooling.preview.RemotePreview

   @Preview(device = "id:wearos_large_round")
   @Composable
   fun YourSamplePreview() {
       RemotePreview(WEAR_WIDGETS) {
           YourSample()
       }
   }
   ```

2. Generate graphics: `./widget-screenshot YourSamplePreview`

3. Generate all previews: You can render all previews at once using the Gradle
   tasks provided by the plugin:

   ```bash
   ./gradlew renderAllPreviews
   ```

   or

   ```bash
   ./gradlew renderPreviews
   ```

   > [!NOTE] `renderAllPreviews` is the user-facing entry point that also
   > handles archiving screenshots if history is enabled. `renderPreviews` is
   > the core task that runs the tests. If history is disabled, they do the same
   > thing.

**How it works & Troubleshooting:**

- **Under the hood**: The rendering process uses a custom Gradle task
  (`renderPreviews`) that runs Robolectric unit tests. It uses Roborazzi to
  capture the screenshots of the composed previews.
- **Problem Diagnostics**: If a preview fails or acts unexpectedly, you can run
  it with verbose output to see detailed engine traces:

  ```bash
  WIDGET_VERBOSE=true ./widget-screenshot YourSamplePreview
  ```

**Workaround for Classpath Issues**: If `renderPreviews` fails with `ClassNotFoundException` (e.g., for `android.app.Application`), it is because it runs as a plain Gradle `Test` task. You can use the standard Android unit test task instead, which handles the classpath correctly:
```bash
./gradlew testDebugUnitTest -Dcomposeai.render.manifest=build/compose-previews/previews.json -Dcomposeai.render.outputDir=build/compose-previews/renders
```
This will run the rendering tests and output PNGs to `app/build/compose-previews/renders`.

**Reversion / Cleanup:** Clear local outputs using:

```bash
rm -rf app/build/compose-previews/
```

## Troubleshooting

**Issue: Blank Widget** If the widget appears blank, especially after switching
rapidly:

1. Force-stop the app:

   ```bash
   adb shell am force-stop com.google.example.wear_widget
   ```

2. Re-add the tile.
