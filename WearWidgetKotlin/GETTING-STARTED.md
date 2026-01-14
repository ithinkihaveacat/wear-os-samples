# Getting Started with Wear Widgets

**What are Wear Widgets?** Wear widgets are a new glanceable surface for Wear
OS, designed to complement existing apps, tiles, and watch faces. Initially,
these widgets will be visible on a surface similar to the tile carousel,
providing users with effortless access to information and key actions. The
primary motivation for this evolution is to move from full-screen Tiles to
partial-height, vertically scrolling Widgets, which provides developers with the
flexibility to deploy content in various sizes and deliver more focused value to
the user.

Architecturally, Wear widgets represent an evolution of
[RemoteViews](https://developer.android.com/reference/android/widget/RemoteViews).
In this context, "remote" means the UI is rendered in a separate process—or even
on a different device—than where the application code runs. UI is defined by a
"document" that is sent to a system-managed surface (the "player") to be
displayed.

Wear widgets align with Modern Android Development by adopting a declarative DSL
similar to Compose. While the developer mental model remains consistent, the
remote architecture introduces nuances because the UI is displayed by a separate
system player. These architectural departures—particularly regarding how logic
and interactions are handled—are detailed in the
[Event Handling](#event-handling:-actions-vs.-lambdas) section below.

## Prerequisites and Setup

Before you begin, ensure your environment meets the following requirements.

### Runtime Requirements

This project requires `com.google.android.wearable.protolayout.renderer` version
**1.5.7.43.851332805.dev or later** on the target device.

You can verify the version installed on your device using the following command:

```bash
adb shell dumpsys package com.google.android.wearable.protolayout.renderer | \
  grep -m 1 versionName | \
  awk -F= '{print $2}'
```

### Gradle Configuration

While these libraries are in active development, many have recently transitioned
to **ALPHA** releases available on Google Maven. Some components still require
the **AndroidX Snapshot repository**.

**1\. Configure Repositories**

Ensure `google()` is in your repository list. Add the specific snapshot
repository to your `settings.gradle.kts` (or `build.gradle`) file:

```kotlin
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven {
            // Required for libraries still on SNAPSHOT (e.g., remote-material3)
            url = uri("https://androidx.dev/snapshots/builds/14666938/artifacts/repository")
        }
    }
}
```

**2. Add Dependencies**

Include the following dependencies in your app's `build.gradle.kts` file:

```kotlin
dependencies {
    // Core Wear Widget / Remote Compose libraries (ALPHA)
    implementation("androidx.compose.remote:remote-creation-compose:1.0.0-alpha01")
    implementation("androidx.compose.remote:remote-core:1.0.0-alpha01")

    // Libraries still on SNAPSHOT
    implementation("androidx.wear.compose.remote:remote-material3:1.0.0-SNAPSHOT")
    implementation("androidx.glance.wear:wear:1.0.0-SNAPSHOT")
    implementation("androidx.glance.wear:wear-core:1.0.0-SNAPSHOT")

    // Tooling for previews (optional, but recommended)
    implementation("androidx.compose.remote:remote-tooling-preview:1.0.0-alpha01")
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
properties and supported sizes.

```xml
<?xml version="1.0" encoding="utf-8"?>
<wearwidget-provider
    description="@string/hello_widget_description"
    icon="@mipmap/ic_launcher"
    label="@string/hello_widget_label"
    preferredType="SMALL">

    <container type="SMALL" />
    <container type="LARGE" />

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

#### Event Handling: Actions vs. Lambdas {#event-handling:-actions-vs.-lambdas}

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
   Instead of `onClick = { if (isActive) doThis() else doThat() }`, you must
   conditionally pass the correct action object:
   `onClick = if (isActive) ActionA else ActionB`.
3. **State vs. Computation:** Actions like `ValueChange` do not increment values
   dynamically; they send instructions to the remote host to update a state key
   to a new value (often a pre-calculated expression).
4. **Serialization of Side Effects:** Complex objects like `PendingIntent` are
   "captured" and serialized during composition, not at the moment of the click.

**Implementation Guide:**

1. **Use Declarative Actions:** Replace `{ ... }` with `Action` objects (e.g.,
   `ValueChange`, `LaunchActivity`).
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

**Dynamic Theming (System Theme)**

By default (when no `colorScheme` is provided), `RemoteMaterialTheme` uses the
system's dynamic color scheme. This is the **recommended approach** to ensure
widgets feel like a native part of the user's experience.

**Custom Theming (Fixed / Brand Colors)**

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
equivalents. Use the provided extension functions to reduce boilerplate:

- **Colors:** `Color.Red.rc` (instead of `RemoteColor(Color.Red)`)
- **Dimensions:** `10.rdp` or `10.dp.asRdp()` (instead of `RemoteDp(...)`)
- **Strings:** `"Hello".rs` (instead of `RemoteString("Hello")`)
- **Booleans:** `true.rb` (instead of `RemoteBoolean(true)`)
- **Integers:** `1.ri` (instead of `RemoteInt(1)`)
- **Floats:** `1f.rf` (instead of `RemoteFloat(1f)`)

#### Component Gallery

For a visual overview of the available components and layout samples (including
`RemoteBox`, `RemoteButton`, `RemoteCanvas`, and more) along with their
corresponding code, please refer to the
**[Widget Component Gallery](screenshots/SAMPLES.md)**.

#### Triggering Updates from App Code

While client-side state changes can update the widget instantly, you often need
to push new data from your application (e.g., from a background worker or after
a network response). You can request a widget refresh using the
`triggerUpdate` method on your `GlanceWearWidget` implementation.

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

#### Ecosystem Roadmap

| Platform              | Now                                                                                              | Feb 2026                                                                                                                                | \~Sep 2026 |
| :-------------------- | :----------------------------------------------------------------------------------------------- | :-------------------------------------------------------------------------------------------------------------------------------------- | :--------- |
| **Emulator**          | **Tile Mode Only:** Renders as `FULLSCREEN` Tile (Icon \+ Label header). Ignores `--type` flags. | **Widget Support:** A new standalone renderer app (coming soon) will enable testing of `SMALL` and `LARGE` modes directly on emulators. | **?**      |
| **Pixel Watch 3 & 4** | **Tile Mode Only:** Renders as `FULLSCREEN` Tile (Icon \+ Label header). Ignores `--type` flags. | **Widget Support:** A new standalone renderer app (coming soon) will enable testing of `SMALL` and `LARGE` modes directly on emulators. | **?**      |
| **Galaxy Watch ?**    | **Tile Mode Only:** Renders as `FULLSCREEN` Tile (Icon \+ Label header). Ignores `--type` flags. | **?**                                                                                                                                   | **?**      |

#### Developer Recommendation

- **For Integration Testing:** Use standard Emulators. Verify basic connectivity
  and logic, but expect the UI to look like a full-screen Tile
  (`containerType=0`).
- **For Layout Verification:** Use a Physical Device with the `adb-tile-add`
  command to verify your layouts in the correct constraints:
  - `adb-tile-add --type SMALL <COMPONENT>`
  - `adb-tile-add --type LARGE <COMPONENT>`

This transitional behavior ensures your widget works today on existing surfaces
(as a Tile) while lighting up with the new, compact Widget experience on
supported devices and future tools.

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

### Developer Tools

You may find some of the tools in the following directory useful for development
(especially agent-assisted development):  
To ease development of widgets, we created some scripts hosted on
[github](https://github.com/ithinkihaveacat/dotfiles/tree/master/bin%20) which
you may find useful during development:

[https://github.com/ithinkihaveacat/dotfiles/tree/master/bin](https://github.com/ithinkihaveacat/dotfiles/tree/master/bin)

- **`adb-tile-add <COMPONENT>`**: Registers and displays a new tile service.
- **`adb-tile-show <INDEX>`**: Scrolls to a specific tile index.
- **`adb-screenshot <FILENAME>`**: Captures a screenshot. (Note: Wait a few
  seconds after showing a tile to ensure rendering is complete before
  capturing.)
- **`screenshot-describe <FILENAME>`**: Generates a text description of a
  screenshot (for agents).
- **`screenshot-compare <IMAGE1> <IMAGE2> [PROMPT]`**: Compares two screenshots
  using the Gemini API to identify visual differences like layout shifts, color
  changes, padding, or text updates.
- **`jetpack-inspect <ARTIFACT> [ALPHA|SNAPSHOT]`**: Downloads source code for
  AndroidX libraries. Use `ALPHA` for migrated libraries (e.g., `remote-core`)
  and `SNAPSHOT` for those still in development.

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

### Runtime Issue: Blank Screen (Package Name Restriction)

**Symptom:** The widget adds successfully but displays as a completely black
screen.

![Blank Widget](screenshots/widget_blank.png)

**Cause:** The renderer enforces a package name allowlist. If your package is
not on the list, the UI will not render.

**Diagnosis:** Check the device logs for the error "Provider is not allowlisted
for Remote Compose". If this error is found, provide to Google the package name
you are using if it is different from the one used from the main app (which has
been allowlisted for you).

**Log Extract:**

```text
01-08 06:21:02.164 10032 28409 28409 E ProtoTilesTileRendererImpl: Error getting tile response com.example.android.wearable.composestarter/.HelloWidgetService
01-08 06:21:02.164 10032 28409 28409 E ProtoTilesTileRendererImpl: java.util.concurrent.ExecutionException: awk: Provider is not allowlisted for Remote Compose. com.example.android.wearable.composestarter/.HelloWidgetService
01-08 06:21:02.164 10032 28409 28409 E ProtoTilesTileRendererImpl: Caused by: awk: Provider is not allowlisted for Remote Compose. com.example.android.wearable.composestarter/.HelloWidgetService
```

## Feature Comparison: Tiles vs. Widgets

While Wear Widgets share some conceptual similarities with Tiles (both render on
a remote system surface), their capabilities and development models differ
significantly.

| Feature                   | Wear OS Tiles (ProtoLayout)                                                                                               | Wear Widgets (Remote Compose)                                                                                                                  |
| :------------------------ | :------------------------------------------------------------------------------------------------------------------------ | :--------------------------------------------------------------------------------------------------------------------------------------------- |
| **Development Model**     | Imperative Builder Pattern (`LayoutElementBuilders`)                                                                      | Declarative, Compose-like DSL (`RemoteText`, `RemoteColumn`)                                                                                   |
| **Vertical Scrolling**    | **Not Supported.** Fixed height (screen height).                                                                          | **Support Planned.** Content can exceed screen height. (See Known Issues).                                                                           |
| **State & Interactivity** | Server-side driven. Interactions (`LoadAction`) trigger a full service callback to refresh the UI.                        | Client-side driven. Declarative state (`rememberRemoteIntValue`) and actions (`ValueChange`) allow instant UI updates without app round-trips. |
| **Dynamic Data**          | **Streaming Support.** Can bind directly to platform data (e.g., Heart Rate) via `DynamicBuilders` for real-time updates. | **State Driven.** Updates are driven by state changes or app pushes. No direct platform sensor binding yet.                                    |
| **Update Scheduling**     | **Timeline Support.** Can pre-schedule future layouts (e.g., calendar events) to update automatically without waking the app.| **Real-time.** Updates are immediate and must be initiated by app code. No native mechanism to pre-cache future layouts.                      |
| **Curved Layouts**        | **Native Support.** dedicated `Arc` containers and components (`ArcText`) for circular screens.                           | **Not Required.** Widgets use standard linear layouts (`RemoteRow`) and do not aim to hug the screen curvature.                                |
| **Transitions**           | **Granular Control.** Explicit APIs for `EnterTransition` and `ExitTransition`.                                           | **Animation Specs.** Uses generic `animationSpec` on modifiers. Granular transition control is less relevant in this model.                    |
| **Advanced Animations**   | **Lottie Supported.** Natively supports Lottie via `AndroidLottieResourceByResId`.                                        | **Not Supported.** No native Lottie component.                                                                                                 |
| **Text Formatting**       | **Spannable Support.** Supports mixed styles (bold, italic) and inline images via `Spannable`.                            | **Uniform Style.** `RemoteText` accepts a single string. Styles apply to the whole text. No `AnnotatedString` support yet.                     |
| **Lifecycle**             | **Manual & Event-Driven.** Requires explicitly overriding callbacks like `onTileAddEvent` and `onTileRemoveEvent` in `TileService` to track presence in the carousel. | **Automated.** `GlanceWearWidgetService` manages sessions internally, removing the need to manually handle `onTileAdd`/`remove`. Developer focus is solely on providing the UI definition. |
| **Resource Management**   | **Versioning Protocol.** Uses `onTileResourcesRequest` to serve and version resources (images) independently of the layout.| **Direct Binding.** Resources are handled transparently within the composition, similar to standard Compose (e.g., `R.drawable`).              |
| **Telemetry / Tracking**  | **Built-in Callback.** `onRecentInteractionEventsAsync` provides a stream of recent click events.                         | **Manual.** No dedicated callback. To track interactions, use `PendingIntent` actions (e.g., targeting a `BroadcastReceiver`) to manually log events.  |

## Known Issues and Limitations

This section tracks technical hurdles and API limitations in the current
SNAPSHOT version.

### Vertical Scrolling is Currently Disabled

**Symptom:** Content that exceeds the screen height is clipped, and the user
cannot scroll to view it.

**Context:** While the `RemoteModifier.verticalScroll(state)` API exists in the
library, the underlying renderer in the current Alpha/Snapshot build does not
yet fully support scrollable containers.

**Affected APIs:**

- `RemoteModifier.verticalScroll`
- `rememberRemoteScrollState`

**Status:** Full scrolling support is expected in an upcoming renderer update.

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
built-in `@Composable` conversion, or manually define extension functions that
accept `RemoteDp` and delegate to `padding(all.toPx())`.

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

### `RemoteBox` Differs from Standard `Box`

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

### Crash using `RemoteBrush.linearGradient` with `Offset.Infinite`

b/473617206

**Symptom:** Passing `RemoteOffset(Float.POSITIVE_INFINITY, ...)` to
`RemoteBrush.linearGradient` causes the System UI process to crash with an
`IllegalArgumentException`.

**Workaround:** Instantiate `RemoteLinearGradient` directly and pass `null` for
the `end` parameter. This triggers the fallback logic that correctly uses the
component's bounds.

```kotlin
// CRASHES:
RemoteBrush.linearGradient(..., end = RemoteOffset(Float.POSITIVE_INFINITY, ...))

// WORKS:
RemoteLinearGradient(..., end = null)
```

**Context:** The underlying `RemoteLinearGradient` implementation does not
handle infinite values for the `end` parameter, passing them directly to the
native Android `LinearGradient`, which rejects them. Passing `null` activates
the correct logic to use the component's size.

### Rapid Tile Re-addition Causes Crash (DataStore Conflict)

b/474292165

**Symptom:** The app process crashes with
`IllegalStateException: There are multiple DataStores active`. This may occur
during development or QA when using automated scripts to rapidly remove and
re-add widgets (or switch widget types) via ADB.

**Workaround:** Force-stop the app process between remove and add commands to
ensure the DataStore lock is released.

```bash
adb-tile-remove "$COMPONENT"
adb shell am force-stop com.google.example.wear_widget
adb-tile-add --type LARGE "$COMPONENT"
```

**Context:** The `WearWidgetCache` maintains a `DataStore` instance that is
currently tied to the `GlanceWearWidgetService` lifecycle. Rapid service
restarts can cause a new service to start before the previous one has fully
released the DataStore file lock.

### Android Studio Preview Limitations

b/431932822

**Symptom:** When using `@Preview` or `@WearPreviewDevices` with
`RemotePreview`, Android Studio may not display all defined previews correctly,
especially when multiple previews are in the same file. You might encounter
issues such as only the first preview rendering, or previews not updating as
expected.

**Workaround:** Deploying the widget to an emulator or physical device provides
the most reliable method to confirm the appearance and behavior.

**Context:** These are known limitations related to the Android Studio preview
tooling for Compose-based UIs, including Remote Compose.

## Feedback

## Updates

_This section will be updated with updates, e.g. new lib version availability
and fixes_
