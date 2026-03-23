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

Use the included `component-switch` script to quickly browse different components
without code changes.

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

### Headless Screenshot Tool (`jvm-screenshot`)

This repository includes `./jvm-screenshot`, a script designed for rapid,
headless visual validation of UI components.

**Why it's useful (for humans and AI agents):**
It provides an instantaneous feedback loop (~10s) when modifying code or
comparing a design to a reference image (like Figma). It bypasses the slow,
error-prone process of building an APK, pushing it via ADB, and manipulating an
emulator. Instead, it natively renders the `RemoteComposable` on the JVM using
Robolectric and Roborazzi.

**Usage:**

```bash
# Take a screenshot of a specific component
./jvm-screenshot AustralianThemeSample
./jvm-screenshot textButton
```

**Requirements & Context:**
- The target component **must** be registered within the `when` block of the
  pre-compiled test dispatcher:
  `app/src/test/java/com/google/example/wear_widget/RoboScreenshotToolTest.kt`
- If you request an unregistered component, the script will intentionally fail
  with an actionable error message pointing you to the dispatcher file.
- The output is always saved to `app/screenshots/<COMPOSABLE_NAME>.png`.
- **Note:** While this avoids a full APK compilation, the underlying test runner
  (`RoboScreenshotToolTest`) will be compiled on the first run, introducing
  slight initial latency. Subsequent runs for registered components will be much
  faster.

## Troubleshooting

**Issue: Blank Widget** If the widget appears blank, especially after switching
rapidly:

1. Force-stop the app:

   ```bash
   adb shell am force-stop com.google.example.wear_widget
   ```

2. Re-add the tile.
