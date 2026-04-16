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
- Ensure you have checked out the correct branch in `compose-ai-tools` (e.g.,
  `feature/option-2-support` which contains the necessary changes for external
  consumption).
- You must publish the library to mavenLocal by running
  `./gradlew publishToMavenLocal` in the `compose-ai-tools` directory before
  running previews here.

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

**Architecture Overview & Troubleshooting Details:**

- **Backend Platform**: Leverages Gradle tasks paired with internal Roborazzi
  unit rules.
- **Problem Diagnostics**: Run verbose configurations by passing
  `WIDGET_VERBOSE=true ./widget-screenshot ...` to observe detailed engine
  traces.

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
