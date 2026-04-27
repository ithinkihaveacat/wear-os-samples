# Wear Widget Sample

This repository provides sample code illustrating the use of new Wear Widget
libraries (specifically `androidx.compose.remote` and related packages).

## Documentation

- [Getting Started](docs/GETTING-STARTED.md): Detailed guide on building Wear
  Widgets.

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
   version **1.6.1.87.903743252.exp** or later.

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

Accelerates localized testing cycles efficiently using the `compose-preview` CLI.

**Standard Tooling (`compose-preview` CLI):** The recommended way to generate
previews is to use the `compose-preview` CLI tool.

1. Install the CLI:
   ```bash
   curl -fsSL https://raw.githubusercontent.com/yschimke/compose-ai-tools/main/scripts/install.sh | bash
   ```
2. Run `compose-preview render --filter YourPreviewName --output your_file.png`
   to render a specific preview.
3. Run `compose-preview show` to render and inspect all previews.

Note: You may need to `unset ANDROID_JAR` on some Linux environments to avoid
classpath conflicts.

**Offline Environment Setup (Robolectric SDKs):** If you are running in an
offline environment, Robolectric may fail to download the required Android SDK
jar (e.g., for API 35). To identify which file is missing:

1. Run `compose-preview show` with `--verbose`.
2. Check the error output for a message like:
   `java.lang.IllegalArgumentException: Path is not a file: .../android-all-instrumented-15-robolectric-13954326-i7.jar`.
3. The path indicates the EXACT filename needed.

To resolve it:

1. Manually download the requested `android-all` (or `android-all-instrumented`)
   JAR file from Maven Central (Group: `org.robolectric`, Artifact:
   `android-all`).
2. Place it in the project root or a directory of your choice.
3. Set the system property `robolectric.dependency.dir` (e.g. in `gradle.properties` or via CLI `-Drobolectric.dependency.dir=...`) to point to that directory.

If you have internet access, simply running `compose-preview show` once
will automatically download and cache these dependencies, preventing silent
failures later when offline.

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
