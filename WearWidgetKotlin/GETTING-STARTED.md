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
similar to Compose. However, while the developer mental model is consistent with
Compose, the remote architecture imposes nuances that become apparent during
implementation. These departures from standard behavior typically stem from the
UI being executed by a separate system player. For instance, instead of passing
standard code lambdas to `onClick` listeners, developers must provide
serializable `Action` objects such as `ValueChange` or `SendIntent`, as the
logic must be packaged into the UI 'document' before being sent to the remote
process.

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
to **ALPHA** releases available on Google Maven. Some newer or more experimental
components may still require the **AndroidX Snapshot repository**.

**1. Configure Repositories**

Ensure `google()` is in your repository list. If you need components that
haven't reached alpha yet, add the specific snapshot repository to your
`settings.gradle.kts` (or `build.gradle`) file:

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

    // Support for Wear Tiles
    implementation("androidx.wear.tiles:tiles:1.5.0")

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

**Note:** You must add `@file:SuppressLint("RestrictedApi")` to the top of your Kotlin file to use the Alpha APIs.

### 1. Define the Service

The service is the entry point that the system binds to.

```kotlin
class HelloWidgetService : GlanceWearWidgetService() {
    override val widget: GlanceWearWidget = HelloWidget()
}
```

### 2. Define the Widget

The widget class provides the data and layout for the widget.

```kotlin
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
@RemoteComposable
@Composable
fun HelloWidgetContent() {
    RemoteBox(
        modifier = RemoteModifier.fillMaxSize(),
        horizontalAlignment = RemoteAlignment.CenterHorizontally,
        verticalArrangement = RemoteArrangement.Center,
    ) {
        RemoteText(
            text = "Hello World",
            color = RemoteColor(Color.White)
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
    preferredType="@integer/glance_wear_container_type_small">

    <container type="@integer/glance_wear_container_type_small" />
    <container type="@integer/glance_wear_container_type_large" />

</wearwidget-provider>
```

### 5. Register in AndroidManifest.xml

Register the service in your `AndroidManifest.xml` with the required intent
filters and metadata. Note that you will need to provide a `@drawable/tile_preview` image resource.

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

**Supporting Tiles and Widgets**

To support both standard Wear OS Tiles and new Wear Widgets within the same service, you must declare intent actions for both protocols. Note that both use the `com.google.android.wearable.permission.BIND_TILE_PROVIDER` permission. This is because, at the operating system level, both are managed by the Tile Manager, which requires this specific permission to bind to the service. The system distinguishes between the legacy Tile protocol and the new Widget protocol during discovery using the specific intent actions: `BIND_TILE_PROVIDER` and `BIND_WIDGET_PROVIDER` respectively.
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

**Outcome:** Following these steps will add a new Hello World widget to your device. You can verify it by swiping through the tile carousel.

![Hello World Widget](screenshots/hello_world_widget.png)

## Core Concepts and Implementation

### Event Handling: Actions vs. Lambdas

Because widgets run in a remote process, they cannot execute local code
(lambdas). Standard Compose syntax for event handling is replaced by
**Declarative Actions**. Interactions are defined by serializable `Action`
objects, which imposes several constraints:

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

### Theming

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

### Type Conversions

When working with Remote UI components, you frequently need to convert standard
Kotlin/Compose types (like `Color`, `Dp`, `String`, `Int`) into their `Remote`
equivalents. Use the provided extension functions to reduce boilerplate:

- **Colors:** `Color.Red.rc` (instead of `RemoteColor(Color.Red)`)
- **Dimensions:** `10.rdp` or `10.dp.asRdp()` (instead of `RemoteDp(...)`)
- **Strings:** `"Hello".rs` (instead of `RemoteString("Hello")`)
- **Booleans:** `true.rb` (instead of `RemoteBoolean(true)`)
- **Integers:** `1.ri` (instead of `RemoteInt(1)`)
- **Floats:** `1f.rf` (instead of `RemoteFloat(1f)`)

### Component Gallery

For a visual overview of the available components and layout samples (including
`RemoteBox`, `RemoteButton`, `RemoteCanvas`, and more) along with their
corresponding code, please refer to the
**[Widget Component Gallery](screenshots/SAMPLES.md)**.

## Configuration and Integration

### XML Configuration and Manifest

The `wearwidget-provider` XML configuration (referenced in `AndroidManifest.xml`
via `androidx.glance.wear.widget.provider`) defines the widget's supported sizes
and metadata.

**XML Format Specification (`res/xml/widget_info.xml`)**

```xml
<wearwidget-provider
    android:label="@string/widget_label"
    android:description="@string/widget_desc"
    android:icon="@drawable/ic_widget"
    android:preferredType="small">

    <!-- Defines supported sizes. "small" is ~72dp, "large" is ~96dp -->
    <container android:type="small" />
    <container android:type="large" />

</wearwidget-provider>
```

### Binding Protocols (Widgets vs. Tiles)

The system's behavior depends heavily on how the service is bound (Tile Protocol
vs. Widget Protocol). This is determined by the Intent Filters in your Manifest
and how the widget is added (e.g., via ADB).

| Intent Filters Declared | `adb-tile-add` Result       | Container Type             | Header Style |
| :---------------------- | :-------------------------- | :------------------------- | :----------- |
| **Both**                | Tile Mode (Default)         | `0` (FULLSCREEN)           | Icon + Label |
| **Both**                | Widget Mode (with `--type`) | `1` (LARGE) or `2` (SMALL) | Icon Only    |
| **Tile Only**           | Tile Mode                   | `0` (FULLSCREEN)           | Icon + Label |
| **Widget Only**         | Widget Mode                 | `1` (LARGE) or `2` (SMALL) | Icon Only    |

- **Tile Protocol (Legacy):** Requests `containerType=0` (FULLSCREEN). Displays
  a standard header (Icon + Label).
- **Widget Protocol (New):** Respects XML configuration for `SMALL`/`LARGE`.
  Displays a minimal header (Icon Only).

**Recommendation:** To achieve a "clean" widget look during development, force
the Widget protocol by commenting out the `BIND_TILE_PROVIDER` intent filter or
using `adb-tile-add --type SMALL`.

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

This codebase requires specialized shell scripts for efficient development.

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

## Known Issues and Limitations

This section tracks technical hurdles and API limitations in the current
SNAPSHOT version.

### Multiple APIs Are Restricted

Many APIs (e.g., `.rs`, `.rf`, `RemotePainter`) are currently marked as
`@RestrictTo(LIBRARY_GROUP)`.

**Workarounds:**

1. **(Recommended)** Suppress the lint error by adding
   `@file:SuppressLint("RestrictedApi")` at the top of the file. This enables
   access to the full API surface required for implementation.
2. For simple type conversions, use public constructors (e.g.,
   `RemoteString("...")`) instead of extensions where possible.

### `RemoteButtonColors` Requires Explicit Definition

b/470339092

**Symptom:** Changing a single color property (e.g., background) requires
defining the colors for all states (enabled, disabled, content, etc.).

**Workaround:** Use the 8-argument `RemoteButtonColors` constructor to
explicitly define your color theme.

**Context:** `RemoteButtonColors` is not a data class and therefore lacks a
`copy()` method. Additionally, the standard
`RemoteButtonDefaults.buttonColors()` factory does not accept parameters, making
it impossible to "copy" and modify an existing configuration. This limitation
does not apply to other similar constructions, such as `RemoteIconButton`,
`RemoteTextButtonColors`, and `RemoteSolidColor`.

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
`ProtoLayoutRenderer` (version 1.5.7.dev) appears to have incomplete support for
these graphics layer operations.

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
