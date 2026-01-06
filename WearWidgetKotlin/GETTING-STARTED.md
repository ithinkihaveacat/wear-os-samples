# Getting Started with Wear Widgets

This document contains general information about building Wear Widgets, including dependencies, configuration, theming, and known limitations.

## Prerequisites

### ProtoLayout Renderer

This project requires `com.google.android.wearable.protolayout.renderer` version
**1.5.7.43.851332805.dev or later** on the target device.

You can verify the version installed on your device using the following command:

```bash
adb shell dumpsys package com.google.android.wearable.protolayout.renderer | \
  grep -m 1 versionName | \
  awk -F= 
{print $2}
```

## Widget Dependencies

To use the Wear Widget libraries in your own project, you must configure your
build to pull from the AndroidX Snapshot repository. These libraries are in
active development and are not yet available on Maven Central or Google's
primary Maven repository.

### 1. Configure Repositories

Add the specific snapshot repository to your `settings.gradle.kts` (or
`build.gradle`) file:

```kotlin
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven {
            url = uri("https://androidx.dev/snapshots/builds/14666938/artifacts/repository")
        }
    }
}
```

### 2. Add Dependencies

Include the following dependencies in your app's `build.gradle.kts` file:

```kotlin
dependencies {
    // Core Wear Widget / Remote Compose libraries
    implementation("androidx.compose.remote:remote-creation-compose:1.0.0-SNAPSHOT")
    implementation("androidx.wear.compose.remote:remote-material3:1.0.0-SNAPSHOT")
    implementation("androidx.compose.remote:remote-core:1.0.0-SNAPSHOT")
    
    // Support for Wear Tiles
    implementation("androidx.wear.tiles:tiles:1.5.0")

    // Tooling for previews (optional, but recommended)
    implementation("androidx.compose.remote:remote-tooling-preview:1.0.0-SNAPSHOT")
    implementation("androidx.wear.compose:compose-ui-tooling:1.5.6")
    implementation("androidx.wear.tiles:tiles-tooling-preview:1.5.0")
    debugImplementation("androidx.wear.tiles:tiles-renderer:1.5.0")
}
```

## Widget Theming

The visual presentation of Wear Widgets is governed by the `RemoteMaterialTheme`
composable. This system allows widgets to adapt to the user's system theme
(Dynamic Theming) or enforce a specific brand identity (Custom Theming).

### Initialization

All UI components for a given widget layout should be wrapped in a
`RemoteMaterialTheme` composable. This functions as the entry point for the
theming system.

```kotlin
@RemoteComposable
@Composable
fun MyWidgetContent() {
    RemoteMaterialTheme {
        // Content here inherits the theme
        RemoteButton(...) { ... }
    }
}
```

### Dynamic Theming (System Theme)

By default (when no `colorScheme` is provided), `RemoteMaterialTheme` uses the
system's dynamic color scheme. The default `RemoteColorScheme` is pre-configured
with tokens that the system renderer resolves to the user's active theme (e.g.,
based on their watch face colors).

**This is the recommended approach** to ensure widgets feel like a native part
of the user's experience.

### Custom Theming (Fixed / Brand Colors)

If a specific brand identity is required, you can provide a custom
`RemoteColorScheme`. Create an object that inherits from `RemoteColorScheme` and
overrides specific color roles.

```kotlin
val myCustomScheme = object : RemoteColorScheme() {
    override val primary: RemoteColor
        @RemoteComposable @Composable get() = Color(0xFF00008B).rc // Custom Blue

    override val onPrimary: RemoteColor
        @RemoteComposable @Composable get() = Color.White.rc

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

### Customizing Component Colors

You can also override colors for a specific component instance by passing a
color object (e.g., `RemoteButtonColors`).

```kotlin
RemoteButton(
    colors = RemoteButtonColors(
        containerColor = Color.Red.rc,
        contentColor = Color.White.rc,
        // ... all other required color parameters must be set
    )
) { ... }
```

_Note: Helper functions like `filledTonalButtonColors()` may not be available in
the snapshot version, requiring manual construction of color objects._

## Widget Configuration

### Widget vs. Tile Configuration

The `WearWidgetProviderInfo` XML configuration allows specifying supported sizes
(`SMALL`, `LARGE`) and other metadata. However, the system's behavior depends
heavily on how the service is bound.

#### Intent Filters and Binding Precedence

The behavior of the widget depends on which intent filters are declared in
`AndroidManifest.xml` and which tool is used to add the tile.

1. **Both Intents (`BIND_WIDGET_PROVIDER` + `BIND_TILE_PROVIDER`):**
   - **Behavior:** Tools like `adb-tile-add` default to the **Tile Protocol**.
   - **Result:** `containerType=0` (FULLSCREEN). The system displays the
     standard header (Icon + Label). XML sizing (`SMALL`/`LARGE`) is ignored.
   - **Override:** Use `adb-tile-add --type SMALL` or `adb-tile-add --type LARGE` to force **Widget Mode**.

2. **Tile Only (`BIND_TILE_PROVIDER`):**
   - **Behavior:** Forces the **Legacy Tile Protocol**.
   - **Result:** `containerType=0` (FULLSCREEN). Standard header (Icon + Label).

3. **Widget Only (`BIND_WIDGET_PROVIDER`):**
   - **Behavior:** Forces the **Widget Protocol**.
   - **Result:** `containerType=1` (LARGE) or `2` (SMALL), respecting the XML
     `preferredType`. The system displays a minimal header (Icon Only).

#### Observable Differences

When forcing the Widget protocol (State 3 above), you can observe distinct
differences:

- **Logcat:** The `WearWidgetParams` passed to `provideWidgetData` will report
different `containerType` and dimensions.
  - **LARGE:** `containerType=1` (e.g., height ~96dp)
  - **SMALL:** `containerType=2` (e.g., height ~72dp)
- **Visual:** The `SMALL` variant typically renders with a shorter height, affecting the layout of centered content compared to the `LARGE` variant.

#### Header Configuration

The visibility of the widget title (e.g., "Wear Widget") in the system header is
determined by the **Binding Protocol** (Tile vs. Widget), not the XML
configuration.

- **Show Title (Tile Mode):** When the service is bound as a Tile (default for
  `adb-tile-add` if `BIND_TILE_PROVIDER` is present), the system displays the
  **icon and the text label**.
- **Hide Title (Widget Mode):** When the service is bound as a Widget (forced by
  removing `BIND_TILE_PROVIDER`), the system displays the **icon only**.

To achieve a "clean" look with no text title during development, you must force
the Widget protocol by commenting out the `BIND_TILE_PROVIDER` intent filter in
`AndroidManifest.xml`.

### XML Configuration Reference

This section describes the format and implications of the `wearwidget-provider` XML configuration used by `androidx.glance.wear`.

#### XML Format Specification

The configuration file is typically located at `res/xml/widget_info.xml` and referenced in the `AndroidManifest.xml` via meta-data:

```xml
<meta-data
    android:name="androidx.glance.wear.widget.provider"
    android:resource="@xml/widget_info" />
```

##### `<wearwidget-provider>` (Root Tag)

| Attribute | Description | Default | 
| :--- | :--- | :--- |
| `label` | Human-readable name of the widget. | Service label from Manifest |
| `description` | Description of the widget's purpose. | Service description |
| `icon` | Drawable resource for the widget icon. | Service icon |
| `group` | A unique string identifying a group of related widget providers. | Fully qualified class name |
| `preferredType` | The default container type to use if none is specified. | `SMALL` (2) |
| `configIntentAction` | Intent action to launch a configuration Activity. | `null` |

##### `<container>` (Child Tag)

Defines a supported size for the widget. At least one `<container>` is required.

| Attribute | Description | Valid Values | 
| :--- | :--- | :--- |
| `type` | The size type of the container. | `SMALL` (2), `LARGE` (1) |
| `previewImage` | Static drawable shown in the picker before the widget loads. | Drawable resource |
| `label` | (Optional) Size-specific label override. | Root label |
| `description` | (Optional) Size-specific description override. | Root description |

#### Key Findings & Implications

1. **Surface Differentiation**
   *   **Tiles (Legacy):** When binding via `androidx.wear.tiles.action.BIND_TILE_PROVIDER`, the system requests `containerType=0` (FULLSCREEN).
   *   **Widgets (New):** When binding via `androidx.glance.wear.action.BIND_WIDGET_PROVIDER`, the system respects the XML configuration for `SMALL` (2) and `LARGE` (1) types.

   **Binding Precedence Matrix:**
   | Intent Filters Declared | `adb-tile-add` Result | Container Type | Header Style | 
   | :--- | :--- | :--- | :--- |
   | **Both** | Tile Mode (Default) | `0` (FULLSCREEN) | Icon + Label |
   | **Both** | Widget Mode (with `--type`) | `1` (LARGE) or `2` (SMALL) | Icon Only |
   | **Tile Only** | Tile Mode | `0` (FULLSCREEN) | Icon + Label |
   | **Widget Only** | Widget Mode | `1` (LARGE) or `2` (SMALL) | Icon Only |

   To test Widget-specific sizing and headers during development, use `adb-tile-add --type SMALL` or `adb-tile-add --type LARGE`.

2. **Grouping Behavior**
 The `group` attribute allows the system to treat multiple services as different versions of the same widget. This is intended for seamless migration or providing different implementations for the same content without duplicating entries in the user's carousel. It defaults to the fully qualified class name of the service.

3. **Container Types**
   *   **SMALL (2):** Typically represents a smaller slot (e.g., height ~72dp).
   *   **LARGE (1):** Represents a larger area (e.g., height ~96dp).
   *   **FULLSCREEN (0):** Reserved for legacy Tile compatibility. It **cannot** be declared in the `<container>` tag in XML (the parser will throw an exception).

4. **Previews**
   The `previewImage` is highly recommended. The system uses this to provide immediate visual feedback in the widget/tile picker while the actual widget data is being fetched or rendered for the first time.

5. **Runtime Parameters**
   The `WearWidgetParams` passed to `provideWidgetData` includes more than just dimensions. It also carries:
   *   `horizontalPaddingDp` / `verticalPaddingDp`
   *   `cornerRadiusDp`
   *   `instanceId` (namespace and unique ID)

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

### Agent Tooling Requirements

Agents working on this codebase are required to utilize the following
specialized shell scripts to ensure efficient development and reliable
verification. These tools are designed to streamline device interaction and
source code analysis.

#### Device Interaction and Verification

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

#### Targeting Specific Devices

All commands that interact with devices (including specialized scripts and
`./gradlew` tasks) respect the `ANDROID_SERIAL` environment variable. When
targeting a specific device among multiple connected ones, the recommended
pattern is to prefix the command with the environment variable:

```bash
env ANDROID_SERIAL=<SERIAL_ID> adb-tile-add ...
env ANDROID_SERIAL=<SERIAL_ID> ./gradlew :app:installDebug
```

#### Source Code Analysis

- **`jetpack-inspect <ARTIFACT> SNAPSHOT`**: Downloads and extracts the source
  code for AndroidX libraries. Since this project uses snapshot versions of the
  Wear Widget libraries, agents **must** use this tool with the `SNAPSHOT`
  argument to understand the underlying implementation and available APIs.

## Known Issues and Limitations

This section tracks technical hurdles, API limitations, and known issues
encountered while implementing the Wear Widget samples using the Remote Compose
Material3 library. It focuses on documenting practical workarounds and necessary
adaptations where the API diverges from expectations established by standard
Compose, Wear OS Tiles, or general Android development paradigms.

### Multiple APIs Are Restricted

A significant portion of the library's API surface is currently marked as
restricted (`@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)`) in the SNAPSHOT
version. This includes:

- **Convenience Extensions:** `.rs`, `.rf`, `.rb`, and `.asRdp()` for concise
  type conversion.
- **Core Classes:** Essential components like `RemotePainter` and various
  internal state wrappers.

This is an omission; these APIs will be made public in a future release.

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

**Context:**

In standard Compose, `Arrangement.Center` implements `HorizontalOrVertical`,
allowing it to be used in both `Row` and `Column`:

```kotlin
// Standard Compose: Arrangement.Center works for both axes
Row(horizontalArrangement = Arrangement.Center) { ... }
Column(verticalArrangement = Arrangement.Center) { ... }
```

In Remote Compose, `RemoteArrangement.Center` is typed as
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

`RemoteRow` and `RemoteColumn` use `RemoteAlignment` for their non-flow axes, consistent with standard Compose. `RemoteBox` uses `RemoteArrangement` for its vertical axis:

<!-- markdownlint-disable MD013 -->

| Container      | Horizontal Parameter           | Vertical Parameter           | 
| :------------- | :----------------------------- | :--------------------------- | 
| `RemoteRow`    | `RemoteArrangement.Horizontal` | `RemoteAlignment.Vertical`   | 
| `RemoteColumn` | `RemoteAlignment.Horizontal`   | `RemoteArrangement.Vertical` | 
| `RemoteBox`    | `RemoteAlignment.Horizontal`   | `RemoteArrangement.Vertical` | 

<!-- markdownlint-enable MD013 -->

**Workaround:** Specify both parameters when using `RemoteBox`. For vertical
positioning, use `RemoteArrangement` constants (`Top`, `Center`, `Bottom`)
rather than `RemoteAlignment` constants (`Top`, `CenterVertically`, `Bottom`).

### Event Handling Uses Actions, Not Lambdas

**Symptom:** Standard Compose syntax for event handling (lambdas) is not
supported. Additionally, the `vararg` signature of `onClick` requires an array
wrapper when assigning single actions using named arguments.

**Workarounds:**

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

   ```kotlin
   val myActions = arrayOf(ValueChange(...), LaunchActivity(...))
   RemoteButton(onClick = myActions) { ... }
   ```

**Context:** Because widgets run in a remote process, they cannot execute local
code (lambdas). Interactions are defined by serializable `Action` objects, which
imposes several constraints:

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
5. **Limited API:** You are restricted to the side effects supported by the
   `Action` API.

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
