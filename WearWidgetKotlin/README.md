# Wear Widget Sample

This repository provides sample code illustrating the use of new Wear Widget
libraries (specifically `androidx.compose.remote` and related packages).

For general information on how to build Wear Widgets, including prerequisites, dependencies, and configuration, please refer to [GETTING-STARTED.md](GETTING-STARTED.md).

## Getting Started with this Sample

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

## Sample Visuals

You can find screenshots of various samples in the `screenshots/` directory. These can help you visualize what the samples look like without deploying them.

```