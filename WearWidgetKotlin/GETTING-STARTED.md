<!-- markdownlint-disable MD013 -->

# Getting Started with Wear Widgets

**What are Wear Widgets?** Starting with Wear 7, full-screen Tiles will evolve
into partial-height Widgets. Widgets are a new glanceable surface for Wear OS,
designed to complement apps and watch faces. Initially, these widgets will be
visible on a surface similar to the tile carousel, providing users with
effortless access to information and key actions. Partial-height, vertically
scrolling Widgets provide the flexibility to deploy content in various sizes and
deliver more focused value to the user.

Architecturally, Wear widgets represent an evolution of
[RemoteViews](https://developer.android.com/reference/android/widget/RemoteViews).
In this context, "remote" means the UI is rendered in a separate process—or even
on a different device—than where the application code runs. UI is defined by a
"document" that is sent to a system-managed surface (the "player") to be
displayed.

Wear widgets align with Modern Android Development by adopting a declarative DSL
similar to Compose. While the developer mental model remains consistent, the
remote architecture introduces nuances because the UI is displayed by a separate
system player. The specific nuances of this remote model, particularly regarding
how logic and interactions are handled, are detailed in the
[Event Handling](#event-handling-actions-vs-lambdas) section below.

In Wear OS 7, only some devices will support partial height vertical scrolling.
See the table below for understanding the system behaviour on different devices.

| OS Version                                                           | App Provides... | Resulting System Behavior                                                                                                                      |
| :------------------------------------------------------------------- | :-------------- | :--------------------------------------------------------------------------------------------------------------------------------------------- |
| Wear 7 — system has Partial Height Support (e.g. GW8 and GW9)        | Tile \+ Widget  | **Multi-Info Tile (Vertical)l**: Displays the Widget at partial height.                                                                        |
|                                                                      | Widget Only     | **Multi-Info Tile (Vertical)**: Displays the Widget at partial height.                                                                         |
|                                                                      | Tile Only       | **Less Accessible Horizontal Carousel:** Displays the full-screen ProtoLayout Tile in a horizontal carousel adjacent to the Vertical Carousel. |
| Wear 7 — device does _not_ have Partial Height Support (Pixel Watch) | Tile \+ Widget  | **Horizontal Carousel (Tile)**: Displays the full-screen ProtoLayout Tile for better UX.                                                       |
|                                                                      | Widget Only     | **Horizontal Carousel (Widget)**: **Large** Widget is embedded into a full-screen Tile-like layout.                                            |
|                                                                      | Tile Only       | **Horizontal Carousel (Tile)**: Displays the full-screen ProtoLayout Tile.                                                                     |
| Wear 4, 5, 6                                                         | Tile \+ Widget  | **Horizontal Carousel (Tile)**: Displays the full-screen ProtoLayout Tile.                                                                     |
|                                                                      | Widget Only     | **Horizontal Carousel (Widget)**: **Large** Widget is adapted to a full-screen layout for backward compatibility.                              |
|                                                                      | Tile Only       | **Horizontal Carousel (Tile)**: Displays the full-screen ProtoLayout Tile.                                                                     |
| Wear 3                                                               | Tile \+ Widget  | **Horizontal Carousel (Tile)**: Displays the full-screen ProtoLayout Tile.                                                                     |
|                                                                      | Widget Only     | **None**: Widgets are **not compatible** with Wear 3 devices.                                                                                  |
|                                                                      | Tile Only       | **Horizontal Carousel (Tile)**: Displays the full-screen ProtoLayout Tile.                                                                     |

## Prerequisites and Setup

Before you begin, ensure your environment meets the following requirements.

### Runtime Requirements

This project requires `com.google.android.wearable.protolayout.renderer` version
**1.5.7.54.855545428 or later** on the target device.

To meet this requirement, you must manually sideload the appropriate renderer
binary from [REDACTED].

(If you do not have access to the shared Drive, please email your Google contact
and provide the email addresses of the users who will be accessing the folder.)

To install the renderer:

1. **Download the appropriate binary**: Match the file to your system
   architecture (determined via `adb exec-out getprop ro.product.cpu.abi`) and
   build type. For physical Wear OS devices, the `armeabi-v7a` architecture is
   typically required. For emulators, use `arm64-v8a` for M-series Macs or the
   relevant `x86` variant for other platforms. You should attempt to install the
   `releasekey` version first, and use the `testkey` variant only if that fails.

2. **Install via ADB**: Run the following command:

```shell
adb install -g -t -r <renderer_filename>.apk
```

1. **Restart the System UI**: Apply the update by forcing a restart of the
   service:

```shell
adb shell am force-stop com.google.android.wearable.sysui
```

1. **Verify the version**: Once the service has restarted, confirm you have a
   compatible version installed on your device using the following command:

```shell
adb shell dumpsys package com.google.android.wearable.protolayout.renderer | \
  grep -m 1 versionName | \
  awk -F= '{print $2}'
```

### Gradle Configuration

While these libraries are in active development, many have recently transitioned
to **ALPHA** releases available on Google Maven. Some components still require
the **AndroidX Snapshot repository**.

#### 1. Configure Repositories

Ensure `google()` is in your repository list. Add the specific snapshot
repository to your `settings.gradle.kts` (or `build.gradle`) file:

```kotlin
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven {
            // This build id needed to align with non-SNAPSHOT coordinates
            url = uri("https://androidx.dev/snapshots/builds/14682660/artifacts/repository")
        }
    }
}
```

#### 2. Add Dependencies

Include the following dependencies in your app's `build.gradle.kts` file:

```kotlin
dependencies {
    // Core Wear Widget / Remote Compose libraries (ALPHA)
    implementation("androidx.compose.remote:remote-creation-compose:1.0.0-alpha02")
    implementation("androidx.compose.remote:remote-core:1.0.0-alpha02")
    implementation("androidx.glance.wear:wear:1.0.0-alpha01")
    implementation("androidx.glance.wear:wear-core:1.0.0-alpha01")

    // Libraries still on SNAPSHOT
    implementation("androidx.wear.compose.remote:remote-material3:1.0.0-SNAPSHOT")

    // Tooling for previews (optional, but recommended)
    implementation("androidx.compose.remote:remote-tooling-preview:1.0.0-alpha02")
    implementation("androidx.wear.compose:compose-ui-tooling:1.5.6")
    implementation("androidx.wear.tiles:tiles-tooling-preview:1.5.0")
    debugImplementation("androidx.wear.tiles:tiles-renderer:1.5.0")
}
```

## Quick Start: Building a Hello World Widget

A Wear Widget consists of a service extending `GlanceWearWidgetService` and a
widget class extending `GlanceWearWidget`. The UI is defined using
`@RemoteComposable` functions.

**Note:** You must add `@file:SuppressLint("RestrictedApi")` to the top of your
Kotlin file to use the Alpha APIs.

### 1. Define the Service

The service is the entry point that the system binds to.

```kotlin
import android.annotation.SuppressLint
import androidx.glance.wear.GlanceWearWidget
import androidx.glance.wear.GlanceWearWidgetService

@SuppressLint("RestrictedApi")
class HelloWidgetService : GlanceWearWidgetService() {
    override val widget: GlanceWearWidget = HelloWidget()
}
```

### 2. Define the Widget

The widget class provides the data and layout for the widget.

```kotlin
import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.ui.graphics.Color
import androidx.glance.wear.GlanceWearWidget
import androidx.glance.wear.WearWidgetData
import androidx.glance.wear.WearWidgetDocument
import androidx.glance.wear.WearWidgetParams

@SuppressLint("RestrictedApi")
class HelloWidget : GlanceWearWidget() {
    override suspend fun provideWidgetData(
        context: Context,
        params: WearWidgetParams,
    ): WearWidgetData {
        return WearWidgetDocument(backgroundColor = Color.Blue) {
            HelloWidgetContent()
        }
    }
}
```

### 3. Define the Content

The content is built using Remote Compose components.

```kotlin
import android.annotation.SuppressLint
import androidx.compose.remote.creation.compose.layout.RemoteBox
import androidx.compose.remote.creation.compose.layout.RemoteComposable
import androidx.compose.remote.creation.compose.layout.RemoteAlignment
import androidx.compose.remote.creation.compose.layout.RemoteArrangement
import androidx.compose.remote.creation.compose.layout.RemoteText
import androidx.compose.remote.creation.compose.modifier.RemoteModifier
import androidx.compose.remote.creation.compose.modifier.fillMaxSize
import androidx.compose.remote.creation.compose.state.RemoteColor
import androidx.compose.remote.creation.compose.state.rc
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@SuppressLint("RestrictedApi")
@RemoteComposable @Composable
fun HelloWidgetContent() {
    RemoteBox(
        modifier = RemoteModifier.fillMaxSize(),
        horizontalAlignment = RemoteAlignment.CenterHorizontally,
        verticalArrangement = RemoteArrangement.Center,
    ) {
        RemoteText(
            text = "Hello World",
            color = Color.White.rc
        )
    }
}
```

### 4. Create the Widget Configuration XML

Create a new file `res/xml/hello_widget_info.xml` to define the widget's
properties and supported sizes. (This filename is just an example; you will
reference it in the `AndroidManifest.xml` in the next step.)

When defining your widget's metadata, follow these guidelines:

- **`label`**: A short title (1–3 words).
- **`description`**: A single-line sentence describing the widget.

```xml
<?xml version="1.0" encoding="utf-8"?>
<wearwidget-provider
    description="@string/hello_widget_description"
    icon="@mipmap/ic_launcher"
    label="@string/hello_widget_label"
    preferredType="SMALL">

    <container
        type="SMALL"
        previewImage="@drawable/tile_preview" />
    <container
        type="LARGE"
        previewImage="@drawable/tile_preview" />
</wearwidget-provider>
```

### 5. Register in AndroidManifest.xml

Register the service in your `AndroidManifest.xml` with the required intent
filters and metadata.

**Note:** You will need to provide a `@drawable/tile_preview` image resource.
For a quick start, you can create a simple vector drawable in
`res/drawable/tile_preview.xml` or temporarily reference an existing icon (e.g.,
`@mipmap/ic_launcher` if accessible as a drawable).

```xml
<service
    android:name=".HelloWidgetService"
    android:exported="true"
    android:icon="@mipmap/ic_launcher"
    android:label="@string/hello_widget_label"
    android:permission="com.google.android.wearable.permission.BIND_TILE_PROVIDER">

    <intent-filter>
        <action android:name="androidx.glance.wear.action.BIND_WIDGET_PROVIDER" />
        <action android:name="androidx.wear.tiles.action.BIND_TILE_PROVIDER" />
    </intent-filter>

    <meta-data
        android:name="androidx.glance.wear.widget.provider"
        android:resource="@xml/hello_widget_info" />

    <meta-data
        android:name="androidx.wear.tiles.PREVIEW"
        android:resource="@drawable/tile_preview" />
</service>
```

### 6. Build and Deploy

#### Build and Install

Build the project and install the debug APK onto your connected device or
emulator:

```bash
./gradlew :app:installDebug
```

#### Add and Display the Tile

Once the app is installed, you can use ADB to programmatically add and show the
tile.

**1. Add the tile:**

Replace `com.google.example.wear_widget` with your app's package name (e.g.,
`com.example.myapp`) and ensure the service path (`.HelloWidgetService`) is
correct.

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

**Outcome:** Following these steps will add a new Hello World widget to your
device. You can verify it by swiping through the tile carousel.

![Hello World Widget](screenshots/hello_world_widget.png)

## Technical Guide

This section covers the architecture of building a Remote UI and how that UI
integrates with the Wear OS system.

### Remote UI Programming Model

#### Event Handling: Actions vs. Lambdas

Because widgets run in a remote process, they cannot execute local code
(lambdas). Standard Compose syntax for event handling is replaced by
**Declarative Actions**. Instead of passing standard code lambdas to onClick
listeners, developers must provide serializable `Action` objects (such as
`ValueChange` or `PendingIntentAction`), as the logic must be packaged into the
UI 'document' before being sent to the remote process. This remote execution
model imposes several constraints:

1. **No Arbitrary Code Execution:** You cannot execute standard Kotlin code
   (e.g., `Log.d()`, `viewModel.update()`) inside the handler.
2. **Pre-calculated Logic:** Logic must be resolved at **composition time**.
   Instead of `onClick = { if (isActive) doThis() else doThat() }`, and assuming
   isActive is known, you must conditionally pass the correct action object:
   `onClick = if (isActive) ActionA else ActionB`.
3. **State vs. Computation:** Actions like `ValueChange` do not increment values
   dynamically; they send instructions to the remote host to update a state key
   to a new value (often a pre-calculated expression).
4. **Serialization of Side Effects:** Complex objects like `PendingIntent` are
   "captured" and serialized during composition, not at the moment of the click.

**Implementation Guide:**

1. **Use Declarative Actions:** Replace `{ ... }` with `Action` objects such as
   `ValueChange`.
2. **Handle Vararg Syntax:** When passing a single action to the named `onClick`
   parameter, wrap it in `arrayOf()`. Alternatively, pass it as the first
   positional argument to avoid the wrapper.

   ```kotlin
   // 1. Wrapped in array (Named argument)
   RemoteButton(
       modifier = RemoteModifier.padding(10.dp),
       onClick = arrayOf(ValueChange(count, count + 1))
   ) { ... }

   // 2. Positional argument (No array needed)
   RemoteButton(
       ValueChange(count, count + 1),
       modifier = RemoteModifier.padding(10.dp)
   ) { ... }
   ```

   _Note: When passing an existing array of actions to the named parameter, pass
   it directly without the spread operator (`*`)._

#### Theming

The visual presentation of Wear Widgets is governed by the `RemoteMaterialTheme`
composable. This system allows widgets to adapt to the user's system theme
(Dynamic Theming) or enforce a specific brand identity (Custom Theming).

#### Dynamic Theming (System Theme)

By default (when no `colorScheme` is provided), `RemoteMaterialTheme` uses the
system's dynamic color scheme. This is the **recommended approach** to ensure
widgets feel like a native part of the user's experience.

#### Custom Theming (Fixed / Brand Colors)

If a specific brand identity is required, you can provide a custom
`RemoteColorScheme`.

```kotlin
val myCustomScheme = object : RemoteColorScheme() {
    override val primary: RemoteColor
        @RemoteComposable @Composable get() = Color(0xFF00008B).rc // Custom Blue
    // ... override other roles as needed
}

@RemoteComposable
@Composable
fun MyCustomWidget() {
    RemoteMaterialTheme(colorScheme = myCustomScheme) {
        // Content here uses the custom colors
    }
}
```

#### Type Conversions

When working with Remote UI components, you frequently need to convert standard
Kotlin/Compose types (like `Color`, `Dp`, `String`, `Int`) into their `Remote`
equivalents. Consider using extension functions from
[the `androidx.compose.remote.creation.compose.state` package](https://android.googlesource.com/platform/frameworks/support/+/refs/heads/androidx-main/compose/remote/remote-creation-compose/src/main/java/androidx/compose/remote/creation/compose/state/)

to reduce boilerplate:

- **Colors:** `Color.Red.rc` (instead of `RemoteColor(Color.Red)`)
- **Dimensions:** `10.rdp` or `10.dp.asRdp()` (instead of `RemoteDp(...)`)
- **Strings:** `"Hello".rs` (instead of `RemoteString("Hello")`)
- **Booleans:** `true.rb` (instead of `RemoteBoolean(true)`)
- **Integers:** `1.ri` (instead of `RemoteInt(1)`)
- **Floats:** `1f.rf` (instead of `RemoteFloat(1f)`)

#### Understanding Remote Dimensions (`RemoteDp`)

Remote Compose introduces `RemoteDp` to distinguish between **immediate** and
**deferred** layout resolution. Developers should specify `RemoteDp` where
possible to ensure dimensions are resolved correctly by the renderer at display
time, maintaining visual consistency.

- **`Dp` (Immediate):** Standard Compose `Dp` values are resolved to raw pixels
  _immediately_ during composition, using the app's current `LocalDensity`. This
  "bakes" the specific pixel value into the document sent to the System UI.
- **`RemoteDp` (Deferred):** `RemoteDp` values (e.g., `10.rdp`) are serialized
  as **data instructions** (e.g., "apply 10dp spacing"). The final pixel value
  is calculated by the _System UI_ (the renderer) at display time, ensuring it
  matches the exact density of the viewing surface.

**Why is `RemoteDp` needed?** It separates the _definition_ of the UI from its
_execution_. This allows the System UI to cache, resize, or adapt the layout
(e.g., for different screen densities) without constantly waking up your
application to recalculate pixels.

**Best Practice:** Use `.rdp` (e.g., `10.rdp`) for structural layout modifiers
like `.size()`, `.width()`, and `.border()` whenever possible.

_Note: As mentioned in "Known Issues", some modifiers like `.padding()`
currently only accept `Dp`. These will resolve to absolute pixel units at
composition time._

#### Component Gallery

For a visual overview of the available components and layout samples (including
`RemoteBox`, `RemoteButton`, `RemoteCanvas`, and more) along with their
corresponding code, please refer to the
**[Widget Component Gallery](screenshots/SAMPLES.md)**.

#### Triggering Updates from App Code

While client-side state changes can update the widget instantly, you often need
to push new data from your application (e.g., from a background worker or after
a network response). You can request a widget refresh using the `triggerUpdate`
method on your `GlanceWearWidget` implementation.

```kotlin
// In an Activity, Worker, or other app component
val componentName = ComponentName(context, HelloWidgetService::class.java)
HelloWidget().triggerUpdate(context, componentName)
```

Calling this will cause the system to re-bind to your `GlanceWearWidgetService`
and call `provideWidgetData` again to fetch the latest UI.

### System Integration & Capabilities

Integrating a Wear Widget into the system requires establishing a contract
between your app and the Wear OS surface. This contract defines what your widget
_can_ do and how the system _connects_ to it.

#### Defining Capabilities (XML)

The `wearwidget-provider` XML file serves as the source of truth for your
widget's supported configurations. It explicitly tells the system which
container types your implementation can handle.

**File:** `res/xml/hello_widget_info.xml`

```xml
<wearwidget-provider
    label="@string/widget_label"
    description="@string/widget_desc"
    icon="@drawable/ic_widget"
    preferredType="SMALL">

    <!-- Defines supported sizes. -->
    <container type="SMALL" />
    <container type="LARGE" />

</wearwidget-provider>
```

- **`container`:** Declares a supported size. `SMALL` (~72dp height) and `LARGE`
  (~96dp height) are the standard Widget sizes.
- **`preferredType`:** Specifies the default size used if the system doesn't
  request a specific one (e.g., when falling back from a legacy surface).

#### The Binding Contract (Manifest)

The system binds to your widget through a Service. To support the transition
from legacy Tiles to modern Widgets, this service supports two different
protocols.

**File:** `AndroidManifest.xml`

```xml
<service
    android:name=".HelloWidgetService"
    android:permission="com.google.android.wearable.permission.BIND_TILE_PROVIDER"
    ...>

    <intent-filter>
        <!-- Protocol 1: The Modern Widget Interface -->
        <action android:name="androidx.glance.wear.action.BIND_WIDGET_PROVIDER" />
        <!-- Protocol 2: The Legacy Tile Interface (Backward Compatibility) -->
        <action android:name="androidx.wear.tiles.action.BIND_TILE_PROVIDER" />
    </intent-filter>

    <meta-data
        android:name="androidx.glance.wear.widget.provider"
        android:resource="@xml/hello_widget_info" />
    ...
</service>
```

- **Permissions:** The `BIND_TILE_PROVIDER` permission is required for security;
  only authorized system services holding this permission can bind to your
  provider.
- **Protocol Dispatch:** The `GlanceWearWidgetService` base class automatically
  handles the handshake by inspecting the incoming action to return the correct
  interface:
  - `BIND_WIDGET_PROVIDER`: Returns the modern `IWearWidgetProvider` binder.
    This protocol supports the `SMALL` and `LARGE` container sizes.
  - `BIND_TILE_PROVIDER`: Returns the legacy `TileProvider` binder. This ensures
    your widget remains visible on the standard Tile Carousel (rendering as
    `FULLSCREEN`).

### Current and Future Renderers

The Wear Widget ecosystem is currently in an Early Access phase, and the
available renderer is quite limited. An updated renderer will soon be available
with more capabilities, and in the near future both emulators and physical
devices from different OEMs will be able to replicate the production experience
(including vertically scrolling views of heterogenous widgets).

While you define capabilities for both `SMALL` and `LARGE`, the actual runtime
behavior depends on the device's capabilities and the stage of the rollout.

#### Developer Support Roadmap

| Platform                                       | Now                                                                      | Feb 2026                                                                                                                                        |
| :--------------------------------------------- | :----------------------------------------------------------------------- | :---------------------------------------------------------------------------------------------------------------------------------------------- |
| **Emulators, Pixel Watch 2+, Galaxy Watch 5+** | **Tile Mode Only:** Renders as `FULLSCREEN` Tile (Icon \+ Label header). | **Widget Support:** A new standalone renderer app (coming soon) will be made available, which will enable testing of `SMALL` and `LARGE` modes. |

## Developer Workflow and Tools

### Previews

To enable Compose Previews, use `RemotePreview` combined with
`@WearPreviewDevices`. Note that preview dependencies must be included in the
`implementation` configuration (not just `debugImplementation`) if your preview
code resides in the `main` source set.

```kotlin
@WearPreviewDevices
@Composable
fun HelloWidgetPreview() {
    val content: @Composable @RemoteComposable () -> Unit = { HelloWidgetContent() }
    RemotePreview(RcPlatformProfiles.WEAR_WIDGETS, content)
}
```

**Note:** Android Studio currently has known issues when rendering multiple
previews in a single file. While you may use these previews where functional, we
recommend verifying your widget's appearance and behavior on an emulator or
physical device for the most reliable experience and highest fidelity.

## Feedback

## Troubleshooting

### Build Failure: minSdk version conflict

**Symptom:** The build fails with a manifest merger error indicating that a
library's `minSdk` (e.g., 29) is higher than the application's `minSdk` (e.g.,
26).

**Resolution:**

1. **Option 1 (Recommended):** Increase the app's `minSdk` to 29 or higher in
   `build.gradle.kts`.
2. **Option 2:** If you can't increase your app's `minSdk`, use the
   `overrideLibrary` marker to suppress the error at build time, _and_
   conditionally enable widgets using resource qualifier checks at runtime.

To use `overrideLibrary`, add the `tools` namespace and the attribute to your
`AndroidManifest.xml`. You may need to list multiple libraries if the conflict
propagates.

```xml
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools">

    <uses-sdk tools:overrideLibrary="androidx.wear.compose.remote.material3, androidx.glance.wear.core, androidx.glance.wear"/>
    ...
</manifest>
```

For more information, see
[Override uses-sdk for imported libraries](https://developer.android.com/build/manage-manifests#override_uses-sdk_for_imported_libraries).

To conditionally enable or disable the widget service at runtime, define a
boolean resource that defaults to `false` in the main `values` folder. You then
override this value to `true` in a version-specific folder (e.g., `values-v33`).
By referencing this boolean in the `android:enabled` attribute, the system
automatically disables the service on older devices, preventing them from
loading incompatible classes.

**Disable by default (`res/values/bools.xml`)**

```xml
<resources>
    <bool name="is_widgets_enabled">false</bool>
</resources>
```

**Enable for specific API level (`res/values-v33/bools.xml`)** _Replace `v33`
with your target API level._

```xml
<resources>
    <bool name="is_widgets_enabled">true</bool>
</resources>
```

**Apply to Manifest (`AndroidManifest.xml`)**

```xml
<service
    android:name=".WidgetCatalogService"
    android:enabled="@bool/is_widgets_enabled"
    ...>
</service>
```

For more details on how this attribute controls the instantiation of components,
see the documentation for
[android:enabled](https://developer.android.com/guide/topics/manifest/application-element#enabled).

### Runtime Issue: Blank Screen

**Symptom:** The widget adds successfully but displays as a completely black
screen or fails to appear.

Blank Widget

#### Cause 1: Package name not allowlisted

The renderer enforces a package name allowlist. If your package is not on the
list, the UI will not render.

**Diagnosis:** Check the device logs for the error "Provider is not allowlisted
for Remote Compose".

**Resolution:** Provide to Google the package name you are using if it is
different from the one used from the main app (which has been allowlisted for
you).

**Log Extract:**

```text

01-08 06:21:02.164 10032 28409 28409 E ProtoTilesTileRendererImpl: Error getting tile response com.example.android.wearable.composestarter/.HelloWidgetService

01-08 06:21:02.164 10032 28409 28409 E ProtoTilesTileRendererImpl: java.util.concurrent.ExecutionException: awk: Provider is not allowlisted for Remote Compose. com.example.android.wearable.composestarter/.HelloWidgetService

01-08 06:21:02.164 10032 28409 28409 E ProtoTilesTileRendererImpl: Caused by: awk: Provider is not allowlisted for Remote Compose. com.example.android.wearable.composestarter/.HelloWidgetService

```

#### Cause 2: DataStore Conflict

Certain operations—such as rapidly removing and re-adding a widget (e.g., during
automated testing) or changing system display settings—can cause a crash in the
application process due to overlapping `DataStore` instances. This prevents the
tile from rendering correctly.

**Diagnosis:** Search the logs for a fatal `IllegalStateException` with the
message:
`There are multiple DataStores active for the same file: .../androidx_glance_wear_widget_cache.pb`.

**Resolution:** Force-stop the application process to ensure the previous
service has fully released the file lock. If this occurs during automated
testing, add a force-stop command between the remove and add steps.

**Log Extract:**

```text

01-08 01:20:35.864  8807  8807 E AndroidRuntime: FATAL EXCEPTION: main

01-08 01:20:35.864  8807  8807 E AndroidRuntime: Process: com.google.example.wear_widget, PID: 8807

01-08 01:20:35.864  8807  8807 E AndroidRuntime: java.lang.IllegalStateException: There are multiple DataStores active for the same file: /data/user/0/com.google.example.wear_widget/files/datastore/androidx_glance_wear_widget_cache.pb. You should either maintain your DataStore as a singleton or confirm that there is no two DataStore's active on the same file (by confirming that the scope is cancelled).

```

## Feature Comparison: Tiles vs. Widgets

While Wear Widgets share some conceptual similarities with Tiles (both render on
a remote system surface), their capabilities and development models differ
significantly.

| Feature                   | Wear OS Tiles (ProtoLayout)                                                                                                                                           | Wear Widgets (Remote Compose)                                                                                                                                                              |
| :------------------------ | :-------------------------------------------------------------------------------------------------------------------------------------------------------------------- | :----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **Development Model**     | Imperative Builder Pattern (`LayoutElementBuilders`)                                                                                                                  | Declarative, Compose-like DSL (`RemoteText`, `RemoteColumn`)                                                                                                                               |
| **State & Interactivity** | Server-side driven. Interactions (`LoadAction`) trigger a full service callback to refresh the UI.                                                                    | Client-side driven. Declarative state (`rememberRemoteIntValue`) and actions (`ValueChange`) allow instant UI updates without app round-trips.                                             |
| **Dynamic Data**          | **Streaming Support.** Can bind directly to platform data (e.g., Heart Rate) via `DynamicBuilders` for real-time updates.                                             | **State Driven.** Updates are driven by state changes or app pushes. No direct platform sensor binding yet.                                                                                |
| **Update Scheduling**     | **Timeline Support.** Can pre-schedule future layouts (e.g., calendar events) to update automatically without waking the app.                                         | **Real-time.** Updates are immediate and must be initiated by app code. No native mechanism to pre-cache future layouts.                                                                   |
| **Curved Layouts**        | **Native Support.** dedicated `Arc` containers and components (`ArcText`) for circular screens.                                                                       | **Not Required.** Widgets use standard linear layouts (`RemoteRow`) and do not aim to hug the screen curvature.                                                                            |
| **Transitions**           | **Granular Control.** Explicit APIs for `EnterTransition` and `ExitTransition`.                                                                                       | **Animation Specs.** Uses generic `animationSpec` on modifiers. Granular transition control is less relevant in this model.                                                                |
| **Tween Animations**      | **Renderer Controlled.** Interpolation is handled automatically by the renderer when dynamic values change.                                                           | **Developer Controlled.** Explicit `animationSpec` allows precise control over duration, delay, and easing curves (e.g. `tween`, `spring`).                                                |
| **Advanced Animations**   | **Lottie Supported.** Natively supports Lottie via `AndroidLottieResourceByResId`.                                                                                    | **Support Planned.**                                                                                                                                                                       |
| **Text Formatting**       | **Spannable Support.** Supports mixed styles (bold, italic) and inline images via `Spannable`.                                                                        | **Uniform Style.** `RemoteText` accepts a single string. Styles apply to the whole text. No `AnnotatedString` support.                                                                     |
| **Lifecycle**             | **Manual.** Developers must override `onTileAddEvent` for initialization (e.g., state setup, starting components) and `onTileRemoveEvent` for cleanup. Rendering requires handling separate layout and resource requests. | **Automated.** `GlanceWearWidgetService` manages session lifecycles internally. Optional `onAdded`/`onRemoved` hooks are available. Content and resources are resolved in a unified `provideWidgetData` pass.              |
| **Resource Management**   | **Versioned.** Uses `onTileResourcesRequest` to serve and version resources (images) independently of the layout.                                                     | **Direct Binding.** Resources are handled transparently within the composition, similar to standard Compose (e.g., `R.drawable`).                                                          |
| **Telemetry / Tracking**  | **Built-in Callback.** `onRecentInteractionEventsAsync` provides a stream of recent click events.                                                                     | **Support Planned.**                                                                                                                                                                       |

## Adding Widget Support to Existing Tiles

Adding modern Widget support to an existing `TileService` is a seamless process
for your users. By using the `group` attribute, you can link your new
implementation to the old one. This enables the system to recognize that they
form a single logical component, allowing you to provide a high-fidelity Widget
on supported surfaces, while preserving the user's carousel configuration and
maintaining your existing `TileService` for use in situations where this
provides the best user experience.

### Side-by-Side Upgrade (Recommended)

This approach is the most robust because it preserves your existing high-quality
Tile implementation for legacy devices while adding modern Widget support for
newer ones.

1. **Create New Code:** Implement your new `NewWidgetService` (extending
   `GlanceWearWidgetService`) and its XML configuration.
2. **Link Identity:** In your new widget's XML configuration (e.g.,
   `res/xml/new_widget_info.xml`), set the `group` attribute to the **exact
   fully qualified class name** of your existing `TileService`.

   ```xml
   <wearwidget-provider
       xmlns:android="http://schemas.android.com/apk/res/android"
       group="com.example.OldTileService"
       ... >
   ```

3. **Update Manifest:** Ensure **both** services remain enabled
   (`android:enabled="true"`).

When a widget is added in this way, the system will use the high-fidelity Widget
on supported Wear 7+ surfaces and fall back to your native `ProtoLayout` Tile on
older platforms, ensuring an optimal experience across all devices.

### In-place Upgrade (Strongly Discouraged)

While you can technically convert your existing `TileService` to a
`GlanceWearWidgetService` in place, this is **strongly discouraged**.

1. **Rewrite Code:** Update your `OldTileService.kt` to extend
   `GlanceWearWidgetService`.
2. **Update Manifest:** Add the required `<intent-filter>` for
   `androidx.glance.wear.action.BIND_WIDGET_PROVIDER`.

**Why this is discouraged:** An in-place upgrade effectively retires your custom
`ProtoLayout` Tile. On older devices, the system would be forced to use the
"Widget-as-Tile" compatibility mode, which is generally less polished than a
purpose-built Tile. Maintaining separate implementations ensures the highest
quality experience across Wear devices.

### Advanced: Runtime Switching

For advanced use cases like A/B testing or developer debug menus, you can use
`PackageManager.setComponentEnabledSetting()` to toggle between the two services
at runtime. However, we **recommend using the Manifest approach** for the actual
release to ensure stability and avoid complex state management bugs.

## Known Issues and Limitations

This section tracks technical hurdles and API limitations in the current
ALPHA/SNAPSHOT versions.

### Multiple APIs Are Restricted

[b/474354218](http://b/474354218)

Many APIs (e.g., `.rs`, `.rf`, `RemotePainter`) are currently marked as
`@RestrictTo(LIBRARY_GROUP)`.

**Workaround:** Suppress the lint error by adding
`@file:SuppressLint("RestrictedApi")` at the top of each file (or
`@SuppressLint("RestrictedApi")` immediately above a function). This enables
access to the full API surface.

### `RemoteModifier.padding` Lacks `RemoteDp` Support

b/470964182

The `RemoteModifier.padding` extension functions do not currently accept
`RemoteDp` values. They only support `RemoteFloat` (which requires raw pixel
values or manual conversion) or standard Compose `Dp` (which requires a
`@Composable` context to convert). Unlike other layout modifiers in the library
(such as `RemoteModifier.size` and `RemoteModifier.border`), `padding` does not
support `RemoteDp`.

```kotlin
// Fails compilation
RemoteModifier.padding(10.dp.asRdp())

// Works: Parallel modifiers like 'border' support RemoteDp
RemoteModifier.border(width = 10.dp.asRdp(), color = Color.Red)
```

**Workaround:** Use standard Compose `Dp` (e.g., `11.dp`) which utilizes a
built-in `@Composable` conversion. Alternatively, you can manually wrap the
value in a `RemotePaddingValues` object, though this still performs immediate
resolution internally:

```kotlin
// Option 1: Standard Compose Dp
RemoteModifier.padding(10.dp)

// Option 2: Explicit RemotePaddingValues wrapper
RemoteModifier.padding(RemotePaddingValues(all = 10.rdp))
```

You can also manually define extension functions that accept `RemoteDp` and
delegate to `padding(all.toPx())`.

### `RemoteArrangement.Center` Can Only Be Used in Vertical Contexts

b/471153933

**Symptom:** A type mismatch error occurs when using `RemoteArrangement.Center`
in a `RemoteRow`.

**Workaround:** Use `RemoteArrangement.CenterHorizontally` for horizontal
centering.

```kotlin
// For horizontal centering (RemoteRow):
RemoteRow(horizontalArrangement = RemoteArrangement.CenterHorizontally) { ... }

// For vertical centering (RemoteColumn):
RemoteColumn(verticalArrangement = RemoteArrangement.Center) { ... }
```

**Context:** In standard Compose, `Arrangement.Center` implements
`HorizontalOrVertical`, allowing it to be used in both `Row` and `Column`. In
Remote Compose, `RemoteArrangement.Center` is typed as
`RemoteArrangement.Vertical` only. This limitation excludes `SpaceBetween`,
`SpaceEvenly`, and `SpaceAround`, which implement the `HorizontalOrVertical`
interface.

### `RemoteBox` Differs from Compose `Box`

b/471212869

The `RemoteBox` API differs from standard Compose `Box` in two ways:

1. **Parameter count:** `Box` uses a single `contentAlignment` parameter;
   `RemoteBox` uses separate `horizontalAlignment` and `verticalArrangement`
   parameters.
2. **Vertical axis type:** `RemoteBox` uses `RemoteArrangement.Vertical` for
   vertical positioning. This is inconsistent with `RemoteRow`, which uses
   `RemoteAlignment.Vertical` for vertical positioning.

**Standard Compose (`Box`):**

```kotlin
Box(
    modifier = Modifier.fillMaxSize(),
    contentAlignment = Alignment.Center  // Single parameter for both axes
) { ... }
```

**Remote Compose (`RemoteBox`):**

```kotlin
RemoteBox(
    modifier = RemoteModifier.fillMaxSize(),
    horizontalAlignment = RemoteAlignment.CenterHorizontally,
    verticalArrangement = RemoteArrangement.Center,  // Note: Arrangement, not Alignment
) { ... }
```

**Comparison with `RemoteRow` and `RemoteColumn`:**

| Container      | Horizontal Parameter           | Vertical Parameter           |
| :------------- | :----------------------------- | :--------------------------- |
| `RemoteRow`    | `RemoteArrangement.Horizontal` | `RemoteAlignment.Vertical`   |
| `RemoteColumn` | `RemoteAlignment.Horizontal`   | `RemoteArrangement.Vertical` |
| `RemoteBox`    | `RemoteAlignment.Horizontal`   | `RemoteArrangement.Vertical` |

**Workaround:** Specify both parameters when using `RemoteBox`. For vertical
positioning, use `RemoteArrangement` constants (`Top`, `Center`, `Bottom`)
rather than `RemoteAlignment` constants (`Top`, `CenterVertically`, `Bottom`).

### `RemoteModifier.graphicsLayer` Rendering Failures

[b/473745800](http://b/473745800)

**Symptom:** The tile fails to load and does not appear (or shows a black
screen) when using `RemoteModifier.graphicsLayer`. Logs indicate a "Failed to
render and attach the tile" error.

**Affected Properties:**

- `renderEffect` (used for Blur)
- `alpha` (used for Opacity)

**Workarounds:**

- **For Opacity:** Apply alpha directly to the color instead of using the
  modifier (e.g., `Color.Red.copy(alpha = 0.5f).rc`).
- **For Blur:** There is no known workaround at this time. Avoid using blur
  effects until supported by the library/renderer.

**Context:** The current snapshot of the library and/or the
`ProtoLayoutRenderer` (version 1.5.7.dev) does not support these graphics layer
operations.

### Multiple DataStores Active Crash (DataStore Conflict)

b/474292165

**Symptom:** The app process crashes with
`IllegalStateException: There are multiple DataStores active`. This can occur
under two primary conditions:

1. **Rapid Re-addition:** When using automated scripts to rapidly remove and
   re-add widgets (or switch widget types) via ADB.
2. **Configuration Changes:** When changing system display settings (such as
   "Display size" or "Font size") while a widget is active.

In both cases, the widget may appear blank, show a loading state, or fail to
render.

**Workaround:** Force-stop the application process to release the file lock. For
automated testing, ensure a force-stop is included between the remove and add
commands:

```shell
adb-tile-remove "$COMPONENT"
adb shell am force-stop com.google.example.wear_widget
adb-tile-add --type LARGE "$COMPONENT"
```

The system typically restarts the service automatically after configuration
changes, but a manual force-stop may be necessary if it remains blank.

### Android Studio Preview Limitations

b/431932822

**Symptom:** When using `@Preview` or `@WearPreviewDevices` with
`RemotePreview`, Android Studio may not display all defined previews correctly,
especially when multiple previews are in the same file. You might encounter
issues such as only the first preview rendering, or previews not updating as
expected.

**Workaround:** Deploying the widget to an emulator or physical device provides
the most reliable method to confirm the appearance and behavior.

### `drawArc` `useCenter` Parameter Requires Static Boolean

**Symptom:** You encounter a compilation error when trying to pass a
`RemoteBoolean` to the `useCenter` parameter of `drawArc` in `RemoteCanvas` or
`RemoteDrawScope`.

**Workaround:** Use `drawConditionally` to toggle between two separate `drawArc`
calls with static `Boolean` values:

```kotlin
drawConditionally(isToggled) {
    drawArc(..., useCenter = true)
}
drawConditionally(isToggled.not()) {
    drawArc(..., useCenter = false)
}
```

## Updates

_This section will be updated with updates, e.g. new lib version availability
and fixes_
