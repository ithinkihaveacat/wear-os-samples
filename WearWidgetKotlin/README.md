# Developer Notes

This repository provides sample code illustrating the use of new Wear Widget
libraries (specifically `androidx.compose.remote` and related packages). These
libraries are currently in active development.

This document captures key learnings and configuration details to assist future
development and avoid common pitfalls encountered during the initial setup of
this project.

## Prerequisites

### ProtoLayout Renderer

This project requires `com.google.android.wearable.protolayout.renderer` version
**1.5.7.43.851332805.dev or later** on the target device.

You can verify the version installed on your device using the following command:

```bash
adb shell dumpsys package com.google.android.wearable.protolayout.renderer | \
  grep -m 1 versionName | \
  awk -F= '{print $2}'
```

## Getting Started

### Build and Install

Build the project and install the debug APK for the HelloWidget (only) onto your
connected device or emulator:

```bash
./gradlew :app:installDebug
```

### Add and Display the Tile

Once the app is installed, you need to add the tile to the carousel to see it.
There are three ways to do this:

#### Option A: Manual (User Interface)

Follow the standard Wear OS instructions to add a tile from the watch face:
<https://support.google.com/wearos/answer/9314375>

#### Option B: Android Studio

Use the "run" configuration features in Android Studio as described in the Wear
OS Tiles codelab: <https://developer.android.com/codelabs/wear-tiles#3>

#### Option C: ADB Commands

You can use ADB to programmatically add and show the tile. This is useful for
automation.

**1. Add the tile:**

```bash
adb shell am broadcast \
  -a com.google.android.wearable.app.DEBUG_SURFACE \
  --es operation add-tile \
  --ecn component com.google.example.wear_widget/.HelloWidgetService
```

**2. Show the tile:**

(Assuming this is the first tile added, it will be at index 0.)

```bash
adb shell am broadcast \
  -a com.google.android.wearable.app.DEBUG_SYSUI \
  --es operation show-tile \
  --ei index 0
```

## Exploring the Widget Catalog

The `WidgetCatalog.kt` file contains various widget samples (e.g.,
`ButtonSample9`, `CardSample1`). To display a specific sample on your device,
follow these steps:

### 1. Select a Sample

Open `app/src/main/java/com/google/example/wear_widget/WidgetCatalog.kt` and
locate the `provideWidgetData` method in the `WidgetCatalog` class. Update the
lambda to call the composable function of the sample you wish to view.

```kotlin
override suspend fun provideWidgetData(
    context: Context,
    params: WearWidgetParams,
): WearWidgetData =
    // Change ButtonSample9() to the desired sample function
    WearWidgetDocument(backgroundPainter = painterRemoteColor(Color.Black)) { ButtonSample9() }
```

### 2. Build and Install

Rebuild and install the application:

```bash
./gradlew :app:installDebug
```

### 3. Deploy and Verify

Use the `adb-tile-add` script to register the catalog service and automatically
show it on the device.

```bash
adb-tile-add com.google.example.wear_widget/.WidgetCatalogService
```

Finally, verify the tile is displayed correctly by taking a screenshot. The
screenshot can be reviewed manually, but if the reader is an agent, they should
use the `screenshot-describe` command. It is good practice to take a baseline
screenshot before making changes to compare against.

```bash
adb-screenshot sample_verification.png
screenshot-describe sample_verification.png
```

## Widget vs. Tile Configuration

The `WearWidgetProviderInfo` XML configuration allows specifying supported sizes (`SMALL`, `LARGE`) and other metadata. However, the system's behavior depends heavily on how the service is bound.

### Intent Filters and Binding Precedence

A service can declare support for both the new Widget protocol and the legacy Tile protocol:

```xml
<intent-filter>
    <action android:name="androidx.glance.wear.action.BIND_WIDGET_PROVIDER" />
    <action android:name="androidx.wear.tiles.action.BIND_TILE_PROVIDER" />
</intent-filter>
```

- **Both Present:** System tools (like `adb-tile-add`) and some surfaces often default to the Tile protocol. This results in a `containerType` of `FULLSCREEN` (0), effectively ignoring the `wearwidget-provider` XML sizing configuration.
- **Widget Only:** Removing `BIND_TILE_PROVIDER` forces the system to use the Widget protocol. In this mode, `adb-tile-add` respects the `preferredType` defined in the XML (e.g., `SMALL` or `LARGE`).

### Observable Differences

When forcing the Widget protocol (by removing the Tile intent filter), you can observe distinct differences between the `SMALL` and `LARGE` types:

- **Logcat:** The `WearWidgetParams` passed to `provideWidgetData` will report different `containerType` and dimensions.
  - **LARGE:** `containerType=1` (e.g., height ~96dp)
  - **SMALL:** `containerType=2` (e.g., height ~72dp)
- **Visual:** The `SMALL` variant typically renders with a shorter height, affecting the layout of centered content compared to the `LARGE` variant.

### Header Configuration

The visibility of the widget title (e.g., "Wear Widget") in the system header is determined by the **Binding Protocol** (Tile vs. Widget), not the XML configuration.

*   **Show Title (Tile Mode):** When the service is bound as a Tile (default for `adb-tile-add` if `BIND_TILE_PROVIDER` is present), the system displays the **icon and the text label**.
*   **Hide Title (Widget Mode):** When the service is bound as a Widget (forced by removing `BIND_TILE_PROVIDER`), the system displays the **icon only**.

To achieve a "clean" look with no text title during development, you must force the Widget protocol by commenting out the `BIND_TILE_PROVIDER` intent filter in `AndroidManifest.xml`.

## Development Guide

### Using `jetpack-*` Commands with SNAPSHOTs

For the Wear Widget libraries demonstrated in this sample (such as
`androidx.compose.remote:remote-creation` and
`androidx.wear.compose.remote:compose-material3`), you **must** use the
`SNAPSHOT` version when using `jetpack-*` commands. These libraries are new and
actively evolving, so the code in this repository relies on the latest snapshot
builds.

#### The Mechanism

By default, `jetpack-inspect` attempts to resolve artifacts against the
stable/release repositories. The `SNAPSHOT` argument explicitly instructs the
tool to target the AndroidX snapshot repository instead.

#### The Requirement

Because these specific libraries may not yet have a stable or beta release that
matches the features used here, omitting the `SNAPSHOT` argument will likely
result in the command failing or finding an older, incompatible version.

### Type Conversions and Extension Functions

When working with Remote UI components, you frequently need to convert standard
Kotlin/Compose types (like `Color`, `Dp`, `String`, `Int`) into their `Remote`
equivalents (like `RemoteColor`, `RemoteDp`, `RemoteString`, `RemoteInt`).

The library provides convenient extension functions and properties for these
conversions. **Use these extensions instead of manual constructors** to reduce
boilerplate and improve readability:

- **Colors:** `Color.Red.rc` (instead of `RemoteColor(Color.Red)`)
- **Dimensions:** `10.rdp` or `10.dp.asRdp()` (instead of `RemoteDp(...)`)
- **Strings:** `"Hello".rs` (instead of `RemoteString("Hello")`)
- **Booleans:** `true.rb` (instead of `RemoteBoolean(true)`)
- **Integers:** `1.ri` (instead of `RemoteInt(1)`)
- **Floats:** `1f.rf` (instead of `RemoteFloat(1f)`)

### Preview Dependencies and Configuration

To enable Compose Previews for `HelloWidget`, specifically using `RemotePreview`
combined with `@WearPreviewDevices` for multi-device preview generation, the
following tooling dependencies are required:

- `androidx.compose.ui:ui-tooling-preview` (Core preview support)
- `androidx.compose.remote:remote-tooling-preview` (Remote preview support)
- `androidx.wear.compose:compose-ui-tooling` (Wear specific preview annotations
  like `@WearPreviewDevices`)

#### Configuration Decision

In this project, these dependencies are configured using `implementation()`
rather than the more typical `debugImplementation()`.

```kotlin
// app/build.gradle.kts
implementation(libs.ui.tooling.preview)
implementation(libs.wear.compose.ui.tooling)
implementation(libs.remote.tooling.preview)
```

#### Why?

The preview composable, `HelloWidgetPreview`, is defined in the same file as the
production code (`HelloWidget.kt`), which resides in the `main` source set.

- The `main` source set is compiled for **all** build variants (debug and
  release).
- If `debugImplementation` were used, the preview dependencies would only be
  available in the `debug` variant.
- Consequently, the `release` build would fail to compile because
  `HelloWidget.kt` (in `main`) would try to import preview classes (like
  `RemotePreview` and `@WearPreviewDevices`) that are missing in the release
  classpath.

#### Implications

Using `implementation()` means these tooling libraries are included in the
release APK. This increases the APK size and includes code that is not needed
for the production app.

#### Alternative Approach (Debug Source Set)

A cleaner alternative is to move the preview code to a dedicated `debug` source
set:

1. Create
   `app/src/debug/java/com/google/example/wear_widget/HelloWidgetPreview.kt`.
2. Move `HelloWidgetPreview` to this file.
3. Use `debugImplementation()` for the dependencies.

**Downsides:**

- The preview code is separated from the component being previewed, which can be
  less convenient for rapid iteration.
- You might need to adjust visibility modifiers (e.g., making internal
  composables `public` or keeping them in the same package) to allow the debug
  code to access the composables defined in `main`.

For this simple sample, keeping the preview co-located and using
`implementation()` was chosen for simplicity, but for a production app, the
`debug` source set approach is generally preferred to keep the release APK
optimized.

## Agent Tooling Requirements

Agents working on this codebase are required to utilize the following
specialized shell scripts to ensure efficient development and reliable
verification. These tools are designed to streamline device interaction and
source code analysis.

### Device Interaction and Verification

- **`adb-tile-add <COMPONENT>`**: Registers a new tile service component on the
  device and automatically displays it. This is the primary method for deploying
  new widgets for testing.
- **`adb-tile-show <INDEX>`**: Forces the carousel to scroll to the tile at the
  specified index.
- **`adb-screenshot <FILENAME>`**: Captures a screenshot of the connected device
  and saves it to the local filesystem.
- **`screenshot-describe <FILENAME>`**: Generates a text description of a
  screenshot. **Agents must use this tool to verify the visual output of their
  changes.**
- **`screenshot-compare <FILE1> <FILE2>`**: Compares two screenshots using the
  Gemini API. Identifies visual differences like layout shifts, color changes,
  padding, or text updates.

### Targeting Specific Devices

All commands that interact with devices (including specialized scripts and
`./gradlew` tasks) respect the `ANDROID_SERIAL` environment variable. When
targeting a specific device among multiple connected ones, the recommended
pattern is to prefix the command with the environment variable:

```bash
env ANDROID_SERIAL=<SERIAL_ID> adb-tile-add ...
env ANDROID_SERIAL=<SERIAL_ID> ./gradlew :app:installDebug
```

### Source Code Analysis

- **`jetpack-inspect <ARTIFACT> SNAPSHOT`**: Downloads and extracts the source
  code for AndroidX libraries. Since this project uses snapshot versions of the
  Wear Widget libraries, agents **must** use this tool with the `SNAPSHOT`
  argument to understand the underlying implementation and available APIs.
