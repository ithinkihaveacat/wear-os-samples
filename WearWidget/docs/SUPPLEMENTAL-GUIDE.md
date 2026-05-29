# Getting Started with Wear Widgets

**Supplemental Guide for Partners**

**Version 1.3** May 26, 2026

This document serves as a supplemental guide for partners working with Wear
Widgets. It contains technical information, internal tools, and known issues
that are not covered in the public documentation.

For the canonical overview, tutorials, and standard API usage, please refer to
the official public resources:

- **Official Documentation**:
  [Wear Widgets on developer.android.com](https://developer.android.com/training/wearables/widgets)
- **Official Sample**:
  [Wear Widget Sample on GitHub](https://github.com/android/wear-os-samples/tree/main/WearWidget)

Please consider the public resources as the primary source of truth for standard
development. This guide focuses on the "delta" specific to early access and
internal tools.

## Prerequisites and Setup {#prerequisites-and-setup}

Before you begin, ensure your environment meets the requirements described in
the public documentation. This section details additional setup required for the
internal tools used in this guide.

### Runtime Requirements {#runtime-requirements}

Production devices can *run* widgets as long as they have
`com.google.android.wearable.protolayout.renderer` version 1.6.1 or higher
installed. However, partners using this guide have access to a special version
of this renderer that includes additional developer features.

To use the preview tools described in this guide (such as the standalone Widget
Tray Viewer), you must verify your installed version and, if necessary, manually
sideload the appropriate renderer binary from the [REDACTED].

To check what version you have installed, run the following command:

```shell
adb shell dumpsys package com.google.android.wearable.protolayout.renderer | \
  grep -m 1 versionName | \
  awk -F= '{print $2}'
```

If you do not have access to the shared Drive, please email your Google contact
and provide the email addresses of the users who require access.

To install the renderer:

1. **Download the appropriate binary**: Match the file to your system
   architecture (determined via `adb exec-out getprop ro.product.cpu.abi`) and
   build type. For physical Wear OS devices, the `armeabi-v7a` architecture is
   typically required. For emulators, use `arm64-v8a` for M-series Macs or the
   relevant `x86/x86_64` variant for other platforms. You should attempt to
   install the `releasekey` version first, and use the `testkey` variant only if
   that fails.

1. **Install via ADB**: Run the following command:

```shell
adb install -g -t -r <renderer_filename>.apk
```

1. **Restart the System UI**: Apply the update by forcing a restart of the
   service:

```shell
adb shell am force-stop com.google.android.wearable.sysui
```

### Gradle Configuration {#gradle-configuration}

For the latest library dependencies and SDK version requirements, please refer
to the official public sample at
[Wear Widget Sample on GitHub](https://github.com/android/wear-os-samples/tree/main/WearWidget).

Ensure you are using the latest available versions to avoid known issues present
in earlier alpha releases.

## Building Your First Widget

To get started with building a widget, please refer to the step-by-step
walkthrough in the official public documentation and the `HelloWidget`
implementation in the public sample repository.

- **Walkthrough**:
  [Build a Wear Widget](https://developer.android.com/training/wearables/widgets#build)
- **Sample Code**: See `HelloWidget.kt` and related files in the
  [Wear Widget Sample](https://github.com/android/wear-os-samples/tree/main/WearWidget).

These resources provide the canonical way to define the service, widget
document, content, and XML configuration.

#### Previews and Testing

While production `SMALL` and `LARGE` widgets use OS-level integration, partners
can preview these layouts using the standalone widget viewer. This tool provides
a vertically scrolling list for devices without OS-level carousel support, and
is a special feature exclusive to the shared internal renderer binary provided
in the [REDACTED].

##### Standalone Renderer (Internal Tool) {#standalone-renderer-internal-tool}

In production, `SMALL` and `LARGE` widgets will be visible via an OS-level
integration. A standalone renderer app is provided as a development tool to
allow you to preview these layouts on devices that do not yet have the OS-level
carousel support.

**The shared internal renderer binary contains a developer-preview variant with
the Standalone App (Widget Tray Viewer) enabled, which is not present in the
public Play Store release or standard emulator images.**

1. **Launch the Renderer:** Open the app drawer on your watch and tap **Widget
   Tray Viewer**. _(ADB shortcut:
   `adb shell monkey -p com.google.android.wearable.protolayout.renderer 1`)_
1. **Add a Widget:** Tap the **Add** button. You will see a list of available
   widgets. Tap your widget to add it to the tray.
1. **Refresh a Widget:** If you update your widget's UI, tap the **Refresh**
   button below the widget to force the renderer to fetch the latest layout.
1. **Clear Widgets:** Tap the **Clear** button (trash icon) to remove all
   widgets from the tray.

_Note: Adding and clearing widgets in this tool is conceptually similar to
adding tiles via ADB, but it operates specifically on this standalone preview
surface rather than the system Tile carousel._

For instructions on how to preview your widget as a full-screen Tile on older
Wear OS versions (compatibility mode), please refer to the `README.md` in the
public sample repository.

## Implementation Strategies

For a detailed technical breakdown of implementation strategies (Dual-Service vs
Single-Service) and how they affect different Wear OS versions, please refer to
the official public documentation on
[Migrate from tiles to widgets](https://developer.android.com/training/wearables/widgets/migration).

## Technical Guide

### Remote UI Programming Model

Wear widgets leverage Remote Compose, which features a declarative DSL that
aligns with Modern Android Development. To see these building blocks in
action—including visual samples and code for components like `RemoteBox`,
`RemoteButton`, and `RemoteCanvas`—please see the
[Component Gallery](https://github.com/ithinkihaveacat/wear-os-samples/blob/wear-widgets/WearWidget/docs/COMPONENTS.md).

### Event Handling: Actions vs. Lambdas

Because widgets run in a remote process, they cannot execute local code
(lambdas). Standard Compose syntax for event handling is replaced by
**Declarative Actions**. Instead of passing standard code lambdas to onClick
listeners, developers must provide serializable `Action` objects (such as
`ValueChange` or `PendingIntentAction`), as the logic must be packaged into the
UI 'document' before being sent to the remote process. This remote execution
model imposes several constraints:

1. **No Arbitrary Code Execution:** You cannot execute standard Kotlin code
   (e.g., `Log.d()`, `viewModel.update()`) inside the handler.
1. **Pre-calculated Logic:** Logic must be resolved at **composition time**.
   Instead of `onClick = { if (isActive) doThis() else doThat() }`, you must
   conditionally pass the correct action object:
   `onClick = if (isActive) ActionA else ActionB`.
1. **State vs. Computation:** Actions like `ValueChange` do not increment values
   dynamically; they send instructions to the remote host to update a state key
   to a new value (often a pre-calculated expression).
1. **Serialization of Side Effects:** Complex objects like `PendingIntent` are
   "captured" and serialized during composition, not at the moment of the click.

**Implementation Guide:**

1. **Use Declarative Actions:** Replace `{ ... }` with `Action` objects such as
   `ValueChange`.

1. **Handle Vararg Syntax:** When passing a single action to the named `onClick`
   parameter, wrap it in `arrayOf()`. Alternatively, pass it as the first
   positional argument to avoid the wrapper.

```kotlin
// 1. Wrapped in array (Named argument)
RemoteButton(
    modifier = RemoteModifier.padding(10.rdp),
    onClick = arrayOf(ValueChange(count, count + 1))
) { ... }

// 2. Positional argument (No array needed)
RemoteButton(
    ValueChange(count, count + 1),
    modifier = RemoteModifier.padding(10.rdp)
) { ... }
```

*Note: When passing an existing array of actions to the named parameter, pass it
directly without the spread operator (`*`).*

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
val myCustomScheme = RemoteColorScheme().copy(
    primary = Color(0xFF00008B).rc // Custom Blue
)

@RemoteComposable
@Composable
fun MyCustomWidget() {
    RemoteMaterialTheme(colorScheme = myCustomScheme) {
        // Content here uses the custom colors
    }
}
```

### Semantic Typography

Semantic typography styles are provided by `RemoteMaterialTheme.typography`. For
examples of how to apply standard Wear OS text styles (e.g., `titleLarge`,
`bodyMedium`), see the `SemanticStyleSample` or `TextSample1` in the Component
Gallery.

### Type Conversions

When working with Remote UI components, you frequently need to convert standard
Kotlin/Compose types (like `Color`, `Dp`, `String`, `Int`) into their `Remote`
equivalents. Consider using extension functions from
[the `androidx.compose.remote.creation.compose.state` package](https://android.googlesource.com/platform/frameworks/support/+/refs/heads/androidx-main/compose/remote/remote-creation-compose/src/main/java/androidx/compose/remote/creation/compose/state/)\
to reduce boilerplate:

- **Colors:** `Color.Red.rc` (instead of `RemoteColor(Color.Red)`)
- **Dimensions:** `10.rdp` or `10.dp.asRdp()` (instead of `RemoteDp(...)`)
- **Strings:** `"Hello".rs` (instead of `RemoteString("Hello")`)
- **Booleans:** `true.rb` (instead of `RemoteBoolean(true)`)
- **Integers:** `1.ri` (instead of `RemoteInt(1)`)
- **Floats:** `1f.rf` (instead of `RemoteFloat(1f)`)

### Triggering Updates from App Code

While client-side state changes can update the widget instantly, you often need
to push new data from your application (e.g., from a background worker or after
a network response). You can request a widget refresh using the `triggerUpdate`
method on your `GlanceWearWidget` implementation.

In newer EAP releases, you must use `GlanceWearWidgetManager` to retrieve active
widget instances and update them via their unique `WidgetInstanceId`:

```kotlin
// In an Activity, Worker, or other app component
val manager = GlanceWearWidgetManager(context)
val widget = MyWidget()
val activeWidgets = manager.fetchActiveWidgets(widget::class)
activeWidgets.forEach { handle ->
    widget.triggerUpdate(context, handle.instanceId)
}
```

Calling this will cause the system to re-bind to your `GlanceWearWidgetService`
and call `provideWidgetData` again to fetch the latest UI.

> [!NOTE] **Production Reference:** For a complete, type-safe implementation of
> state-driven updates (observing Preferences DataStore and triggering updates
> dynamically), see
> [WeatherActivity.kt](../app/src/main/java/com/google/example/wear_widget/WeatherActivity.kt)
> and
> [WeatherUpdateReceiver.kt](../app/src/main/java/com/google/example/wear_widget/WeatherUpdateReceiver.kt).

### Understanding Remote Dimensions (`RemoteDp`)

Remote Compose introduces `RemoteDp` to distinguish between **immediate** and
**deferred** layout resolution. Developers should specify `RemoteDp` where
possible to ensure dimensions are resolved correctly by the renderer at display
time, maintaining visual consistency.

- **`Dp` (Immediate):** Standard Compose `Dp` values are resolved to raw pixels
  *immediately* during composition, using the app's current `LocalDensity`. This
  "bakes" the specific pixel value into the document sent to the System UI.
- **`RemoteDp` (Deferred):** `RemoteDp` values (e.g., `10.rdp`) are serialized
  as **data instructions** (e.g., "apply 10dp spacing" or even more dynamic "if
  width > 200 apply 15dp else 10dp"). The final pixel value is calculated by the
  *System UI* (the renderer) at display time, ensuring it matches the exact
  density of the viewing surface.

**Why is `RemoteDp` needed?** It separates the *definition* of the UI from its
*execution*. This allows the System UI to cache, resize, or adapt the layout
(e.g., for different screen densities) without constantly waking up your
application to recalculate pixels.

## Manifest and XML Reference

To establish the contract between your app and the Wear OS surface, you must
configure your widget's supported sizes in XML and register the service in your
`AndroidManifest.xml`.

For detailed instructions and code snippets on defining capabilities and the
binding contract, please refer to the official public documentation:

- **XML Configuration**:
  [Create the Widget Configuration XML](https://developer.android.com/training/wearables/widgets#create-xml)
- **Manifest Registration**:
  [Register in AndroidManifest.xml](https://developer.android.com/training/wearables/widgets#register-manifest)

## Developer Workflow and Tools {#developer-workflow-and-tools}

### Previews {#previews}

To enable Compose Previews, use `RemotePreview` combined with
`@WearPreviewDevices`. Note that preview dependencies must be included in the
`implementation` configuration (not just `debugImplementation`) if your preview
code resides in the `main` source set.

```kotlin
@Preview(name = "Wear Large Round", device = "id:wearos_large_round", showSystemUi = true)
@Composable
fun HelloWidgetPreview() {
    RemotePreview(profile = RcPlatformProfiles.WEAR_WIDGETS) {
        HelloWidgetContent()
    }
}
```

## Troubleshooting {#troubleshooting}

### Build Failure: minSdk version conflict {#build-failure-minsdk-version-conflict}

**Symptom:** The build fails with a manifest merger error indicating that a
library's `minSdk` (e.g., 29) is higher than the application's `minSdk` (e.g.,
26).

**Resolution:**

1. **Option 1 (Recommended):** Increase the app's `minSdk` to 29 or higher in
   `build.gradle.kts`.
1. **Option 2:** If you can't increase your app's `minSdk`, use the
   `overrideLibrary` marker to suppress the error at build time, _and_
   conditionally enable widgets using resource qualifier checks.

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

To conditionally enable or disable the widget service, define a boolean resource
that defaults to `false` in the main `values` folder. You then override this
value to `true` in a version-specific folder (e.g., `values-v33`). By
referencing this boolean in the `android:enabled` attribute, the system
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

### Runtime Issue: Blank Screen {#runtime-issue-blank-screen}

**Symptom:** The widget adds successfully but displays as a completely black
screen or fails to appear.

#### Cause 1: Package name not allowlisted {#cause-1-package-name-not-allowlisted}

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

#### Cause 2: DataStore Conflict {#cause-2-datastore-conflict}

This is often caused by a DataStore conflict during rapid re-deployment; see
[Multiple DataStores Active Crash](#multiple-datastores-active-crash-datastore-conflict)
for more details.

## Feature Comparison: Tiles vs. Widgets {#feature-comparison-tiles-vs-widgets}

While Wear Widgets share some conceptual similarities with Tiles (both render on
a remote system surface), their capabilities and development models differ
significantly.

| Feature                   | Wear OS Tiles (ProtoLayout)                                                                                                                                                                                               | Wear Widgets (Remote Compose)                                                                                                                                                                                 |
| :------------------------ | :------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ | :------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| **Development Model**     | Imperative Builder Pattern (`LayoutElementBuilders`)                                                                                                                                                                      | Declarative, Compose-like DSL (`RemoteText`, `RemoteColumn`)                                                                                                                                                  |
| **State & Interactivity** | Server-side driven. Interactions (`LoadAction`) trigger a full service callback to refresh the UI.                                                                                                                        | Client-side driven. Declarative state (`rememberMutableRemoteInt`) and actions (`ValueChange`) allow instant UI updates without app round-trips.                                                              |
| **Dynamic Data**          | **Streaming Support.** Can bind directly to platform data (e.g., Heart Rate) via `DynamicBuilders` for real-time updates.                                                                                                 | **State Driven.** Updates are driven by state changes or app pushes. No direct platform sensor binding yet.                                                                                                   |
| **Update Scheduling**     | **Timeline Support.** Can pre-schedule future layouts (e.g., calendar events) to update automatically without waking the app.                                                                                             | **Real-time.** Updates are immediate and must be initiated by app code. No native mechanism to pre-cache future layouts.                                                                                      |
| **Curved Layouts**        | **Native Support.** dedicated `Arc` containers and components (`ArcText`) for circular screens.                                                                                                                           | **Container Not Required.** Widgets use standard linear layouts (`RemoteRow`) and do not aim to hug the screen curvature. However, component-level arcs (like progress indicators) are supported.             |
| **Transitions**           | **Granular Control.** Explicit APIs for `EnterTransition` and `ExitTransition`.                                                                                                                                           | **Animation Specs.** Uses generic `animationSpec` on modifiers. Granular transition control is less relevant in this model.                                                                                   |
| **Advanced Animations**   | **Lottie Supported.** Natively supports Lottie via `AndroidLottieResourceByResId`.                                                                                                                                        | **Support Planned.**                                                                                                                                                                                          |
| **Tween Animations**      | **Developer or Renderer Controlled.** Developers can provide an `AnimationSpec` but can also arrange for interpolation to be handled automatically by the renderer when dynamic values change.                            | **Developer Controlled.** Explicit `animationSpec` allows precise control over duration, delay, and easing curves (e.g. `tween`, `spring`).                                                                   |
| **Text Formatting**       | **Spannable Support.** Supports mixed styles (bold, italic) and inline images via `Spannable`.                                                                                                                            | **Uniform Style.** `RemoteText` accepts a single string. Styles apply to the whole text. No `AnnotatedString` support.                                                                                        |
| **Lifecycle**             | **Manual.** Developers must override `onTileAddEvent` for initialization (e.g., state setup, starting components) and `onTileRemoveEvent` for cleanup. Rendering requires handling separate layout and resource requests. | **Automated.** `GlanceWearWidgetService` manages session lifecycles internally. Optional `onAdded`/`onRemoved` hooks are available. Content and resources are resolved in a unified `provideWidgetData` pass. |
| **Resource Management**   | **Versioned.** Uses `onTileResourcesRequest` to serve and version resources (images) independently of the layout in the older versions of Tiles library.                                                                  | **Direct Binding.** Resources are handled transparently within the composition, similar to standard Compose (e.g., `R.drawable`).                                                                             |
| **Telemetry / Tracking**  | **Built-in Callback.** `onRecentInteractionEventsAsync` provides a stream of recent click events.                                                                                                                         | **Support Planned.**                                                                                                                                                                                          |

## Migrating from Legacy Tiles

For a comprehensive checklist and instructions on adding modern Widget support
to an existing `TileService`, please refer to the official public documentation
on
[Migrate from tiles to widgets](https://developer.android.com/training/wearables/widgets/migration).

## Known Issues and Limitations {#known-issues-and-limitations}

This section tracks technical hurdles and API limitations in the current
ALPHA/SNAPSHOT versions.

### [FIXED] Library ABI Incompatibility (Remote Material 3) {#library-abi-incompatibility}

b/507687866

_Fixed in `remote-material3:1.0.0-alpha03` and core libraries `1.0.0-alpha10`._

**Symptom:** The application crashes at runtime with a
`java.lang.NoClassDefFoundError` or `java.lang.NoSuchMethodError` when
attempting to use components from the `remote-material3` library (such as
`RemoteButton`).

**Context:** The release of `androidx.wear.compose.remote:remote-material3`
(`1.0.0-alpha02`) was compiled against core libraries version `alpha08`. Core
library versions `>= alpha09` introduced breaking ABI changes (including
relocated classes and changed method signatures for the `clickable` modifier)
that were incompatible with the pre-compiled Material library. This has been
resolved in newer releases.

### Unsupported Remote Compose Primitives Cause Runtime Crashes {#unsupported-remote-compose-primitives-cause-runtime-crashes}

b/502649242

**Symptom:** The application crashes at runtime with a
`java.lang.RuntimeException: Operation <X> is not supported for this version`
when attempting to render widgets containing certain advanced `RemoteComposable`
primitives. This includes:

- `RemoteCollapsibleColumn` (Operation 233)
- `RemoteModifier.scroll()` (Operation 226)
- `RemoteModifier.onTouchDown()` and `RemoteModifier.onTouchUp()` (Operations
  219 and 220)

**Workaround:** Do not use these layout primitives and modifiers when building
Wear OS Widgets. You must use alternative supported layouts (like `RemoteColumn`
or `RemoteBox`) and supported click modifiers (like
`RemoteModifier.clickable()`).

> [!NOTE] The exclusion of scrolling modifiers like `RemoteModifier.scroll()`
> and `RemoteModifier.verticalScroll()` is a permanent design decision for the
> `WEAR_WIDGETS` profile to ensure battery and performance characteristics.
> Internal scrolling within a widget will never be supported on Wear Widgets.

### Multiple APIs Trigger `RestrictedApi` Lint Errors {#multiple-apis-are-restricted}

b/502522668

**Symptom:** When using certain Remote Compose APIs, such as `RemotePreview`,
Android Studio triggers a lint error:
`[ApiName] can only be called from within the same library group`.

**Workaround:** Suppress the lint error using `@SuppressLint("RestrictedApi")`
on the specific function or `@file:SuppressLint("RestrictedApi")` at the top of
the file.

**Context:** The `RemotePreview` API (and historically many others) is annotated
with `@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)`. While this is intended
behavior during the alpha phase to limit external usage, it creates noise for
developers building widgets.

_Note: In earlier versions, many core APIs (e.g., `.rs`, `.rf`) were also marked
as restricted (b/474354218). This specific issue has been resolved for the core
API surface, but the workaround remains necessary for tooling APIs like
`RemotePreview`._

### [FIXED] `RemoteModifier.clip()` Requires Explicit Size for Relative Shapes {#remotemodifierclip-requires-explicit-size-for-relative-shapes}

b/477860914

_Fixed in `1.0.0-alpha08`. `RemoteModifier.clip` now expects `RemoteShape`
(e.g., `RemoteCircleShape`) and no longer accepts explicit dimensions._

**Symptom:** Shapes that rely on the component's layout size, such as
`RoundedCornerShape(percent = 50)` or `CircleShape`, may compile but fail to
clip correctly at runtime (often rendering as a square).

**Workaround:** Provide an explicit `DpSize` to the modifier. You should
typically ensure this matches the component's size to avoid clipping artifacts.

```kotlin
// Fails (renders as square)
RemoteModifier.clip(RoundedCornerShape(percent = 50))

// Works
RemoteModifier
    .size(60.rdp)
    .clip(CircleShape, DpSize(60.dp, 60.dp))
```

### [FIXED] `RemoteModifier.padding` Lacks `RemoteDp` Support {#remotemodifierpadding-lacks-remotedp-support}

b/470964182

_Fixed in `1.0.0-alpha07`. `RemoteModifier.padding` now supports `RemoteDp`
(e.g., `10.rdp`)._

The `RemoteModifier.padding` extension functions strictly require `RemoteDp` or
`RemoteFloat` values. Standard Compose `Dp` is no longer supported.

```kotlin
RemoteModifier.padding(10.rdp)
```

### [FIXED] `RemoteArrangement.Center` Can Only Be Used in Vertical Contexts {#remotearrangementcenter-can-only-be-used-in-vertical-contexts}

b/471153933

_Fixed in `1.0.0-alpha07`. `RemoteArrangement.Center` now implements
`HorizontalOrVertical` and can be used in both `RemoteRow` and `RemoteColumn`._

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

### [FIXED] `RemoteBox` Vertical Axis Requires `Arrangement` {#remotebox-differs-from-compose-box}

b/471212869

_Fixed in `1.0.0-alpha07`. `RemoteBox` now uses
`contentAlignment: RemoteAlignment` just like standard Compose `Box`._

**Symptom:** You cannot use `RemoteAlignment` constants for vertical positioning
when configuring a `RemoteBox`.

**Workaround:** Specify both parameters when using `RemoteBox`. For vertical
positioning, use `RemoteArrangement` constants (`Top`, `Center`, `Bottom`)
rather than `RemoteAlignment` constants.

**Context:** The `RemoteBox` API previously differed from standard Compose `Box`
by requiring separate `horizontalAlignment` and `verticalArrangement`
parameters. This was inconsistent with `RemoteRow`, which uses
`RemoteAlignment.Vertical` for vertical positioning.

> [!WARNING] This is a breaking change. Existing usages of `RemoteBox` with
> `horizontalAlignment` and `verticalArrangement` must be updated to use
> `contentAlignment`.

**Migration:** Replace:

```kotlin
RemoteBox(
    horizontalAlignment = RemoteAlignment.CenterHorizontally,
    verticalArrangement = RemoteArrangement.Center,
)
```

With:

```kotlin
RemoteBox(
    contentAlignment = RemoteAlignment.Center,
)
```

For other alignments, use `RemoteAlignment` constants (e.g.,
`RemoteAlignment.BottomEnd`).

### `RemoteModifier.graphicsLayer` Rendering Failures {#remotemodifiergraphicslayer-rendering-failures}

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

### [FIXED] Multiple DataStores Active Crash (DataStore Conflict) {#multiple-datastores-active-crash-datastore-conflict}

b/474292165

_Fixed. The `IllegalStateException: There are multiple DataStores active` crash
has been fixed in the library._

**Symptom:** The widget is blank, and the app process crashes with
`IllegalStateException: There are multiple DataStores active`. Various
conditions and states can cause this, including:

1. **Rapid Re-addition:** When using automated scripts to rapidly remove and
   re-add widgets (or switch widget types) via ADB.
1. **Configuration Changes:** When changing system display settings (such as
   "Display size" or "Font size").

In both cases, the widget may appear blank, show a loading state, or fail to
render.

**Workaround:** Force-stop the application process to release the file lock. For
automated testing, ensure a force-stop is included between the remove and add
commands:

```shell
# Remove the tile "$COMPONENT"
adb shell am broadcast \
  -a com.google.android.wearable.app.DEBUG_SURFACE \
  --es operation remove-tile \
  --ecn component "$COMPONENT"

# Force stop the application
adb shell am force-stop com.google.example.wear_widget

# Add the tile "$COMPONENT"
adb shell am broadcast \
  -a com.google.android.wearable.app.DEBUG_SURFACE \
  --es operation add-tile \
  --ecn component "$COMPONENT"
```

The system typically restarts the service automatically after configuration
changes, but a manual force-stop may be necessary if it remains blank.

### [FIXED] Android Studio Preview Limitations {#android-studio-preview-limitations}

b/431932822

_Fixed in Compose Remote `1.0.0-alpha08`. Multiple preview instances in the same
file now render correctly._

**Symptom:** When using `@Preview` or `@WearPreviewDevices` with
`RemotePreview`, Android Studio may not display all defined previews correctly,
especially when multiple previews are in the same file.

### `drawArc` `useCenter` Requires Static Boolean {#drawarc-usecenter-static-boolean}

b/477026287

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

### [FIXED] `RemoteMaterialTheme.typography` Does Not Expose Semantic Styles {#typography-does-not-expose-semantic-styles}

b/478828032

_Fixed. Semantic text styles (e.g., `titleLarge`) are now natively available
through `RemoteMaterialTheme.typography`._

**Symptom:** `RemoteMaterialTheme.colorScheme` exposes the system's dynamic
colors (e.g., `RemoteMaterialTheme.colorScheme.primary`), however the
`RemoteMaterialTheme.typography` object does not expose its styles publicly. The
underlying `typography` property has `internal` visibility, so accessing
semantic text styles directly (e.g.,
`RemoteMaterialTheme.typography.titleLarge`) causes a compilation error. This
means you cannot use "semantic" text styles to `MaterialRemoteText` components.

### `RemoteImage` Visibility and Duplication Issues {#remoteimage-visibility-and-duplication-issues}

b/483291287

**Symptom:** When multiple `RemoteImage` composables are used in the same
layout, the first image may fail to render (appearing invisible). In some
configurations, multiple images may incorrectly display the content of the
_last_ added image.

**Workaround:** Use `RemoteCanvas` combined with `drawScaledBitmap` instead of
the `RemoteImage` component.

```kotlin
// Define a helper composable
@RemoteComposable
@Composable
fun CanvasRemoteImage(
    bitmap: ImageBitmap,
    contentDescription: String,
    modifier: RemoteModifier = RemoteModifier,
    contentScale: ContentScale = ContentScale.Fit
) {
    val remoteBitmap = bitmap.rb
    RemoteCanvas(modifier = modifier) {
        drawScaledBitmap(
            image = remoteBitmap,
            dstSize = RemoteSize(remote.component.width, remote.component.height),
            scaleType = contentScale,
            contentDescription = contentDescription,
        )
    }
}

// Usage
CanvasRemoteImage(
    bitmap = ImageBitmap.imageResource(id = R.drawable.my_image),
    contentDescription = "My Image",
    modifier = RemoteModifier.size(35.rdp)
)
```

### [FIXED] Crash when using `drawScaledBitmap` with Resource Bitmaps {#crash-when-using-drawscaledbitmap-with-resource-bitmaps}

b/479893918

_Fixed. The crash when using `drawScaledBitmap` with resource-backed bitmaps has
been fixed. You can now use it without explicitly providing the `srcSize`
argument._

**Symptom:** The application crashes with
`java.lang.IllegalStateException: Bitmap width is not available in the remote document`
when calling `drawScaledBitmap` with a resource-backed `RemoteBitmap`.

**Workaround:** You must explicitly provide the `srcSize` argument. The default
value for `srcSize` attempts to read the bitmap's dimensions, which causes the
crash because `RemoteBitmap` instances created from resources do not hold
dimension data locally.

You can retrieve the dimensions using standard Android APIs (like
`BitmapFactory`) and pass them as a `RemoteSize`.

```kotlin
val resources = LocalContext.current.resources
val bitmap = BitmapFactory.decodeResource(resources, R.drawable.my_image)

drawScaledBitmap(
    image = ImageBitmap.imageResource(id = R.drawable.my_image).rb,
    // Explicitly provide srcSize to avoid the crash
    srcSize = RemoteSize(bitmap.width.toFloat().rf, bitmap.height.toFloat().rf),
    dstSize = ...
)
```

### RemoteImage failures when using large or unscaled bitmaps {#remoteimage-failures-when-using-large-or-unscaled-bitmaps}

b/488353353

**Symptom:** When using `RemoteImage` with large or unscaled bitmaps, the entire
widget or tile may fail to render, resulting in an **empty display**.

The system logs typically contain a generic `ExecutionException: Timed out` from
`ProtoTilesTileRendererImpl`. Deep analysis of the logs may reveal a low-level
`!!! FAILED BINDER TRANSACTION !!!` or a `TransactionTooLargeException`, but
these are often masked by the subsequent timeout.

**Workaround:** Manually scale bitmaps to the exact required display size before
converting them to `ImageBitmap` and passing them to `RemoteImage`.

For example, if displaying a 48dp avatar:

```kotlin
val originalBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.large_image)
val density = context.resources.displayMetrics.density
val sizePx = (48 * density).toInt()

// Scale the bitmap before conversion
val scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, sizePx, sizePx, true)
val imageBitmap = scaledBitmap.asImageBitmap()

RemoteImage(
    bitmap = imageBitmap,
    contentDescription = "Scaled Image".rs,
    modifier = RemoteModifier.size(48.rdp)
)
```

**Note:** Avoid passing unscaled `ImageBitmap` objects directly from resources,
as their decoded size (even after internal PNG compression) can easily exceed
the IPC payload limit.

### "Full Bleed" Backgrounds Limited to Solid Colors {#full-bleed-backgrounds-limited-to-solid-colors}

b/480859310

**Symptom:** When attempting to use complex background treatments—such as
images, gradients, or custom drawing—for the entire widget document, the
treatment appears inset with a visible border from the container's background
color. True "full bleed" rendering (where the background treatment extends to
the very edges of the rounded widget container) is currently only possible with
solid colors via `WearWidgetDocument(backgroundColor = ...)`.

**Workaround:** There is no known workaround to achieve a true full-bleed effect
with images or gradients at this time. To maintain visual consistency, ensure
your content or inner backgrounds complement the document's `backgroundColor`.

### [FIXED] Dynamic Theme Colors for Document Backgrounds Require Manual Binding {#dynamic-theme-colors-for-document-backgrounds-require-manual-binding}

_Fixed in `1.0.0-alpha02`. You can now set the background of a
`WearWidgetDocument` to a dynamic theme color using `RemoteColorScheme`
properties directly._

**Symptom:** You cannot set the `background` of a `WearWidgetDocument` to a
dynamic theme color using `RemoteColorScheme` properties (e.g.,
`colorScheme.surfaceContainerLow`). Attempting to do so results in a compiler
error:
`@Composable invocations can only happen from the context of a @Composable function`.

**Workaround:** Create a local `ColorScheme` to retrieve the static fallback
value, then manually construct a named `RemoteColor` using
`RemoteColor.createNamedRemoteColor(...)`. Pass this manually constructed
dynamic color to the document background. Ensure you initialize the
`RemoteMaterialTheme` using a `RemoteColorScheme` seeded with the same local
`ColorScheme` so your foreground components and document background stay in
sync.

```kotlin
override suspend fun provideWidgetData(
    context: Context,
    params: WearWidgetParams,
): WearWidgetData {
    // 1. Instantiate the standard local Material3 ColorScheme
    val localColorScheme = ColorScheme()

    // 2. Create the Remote version to pass into the Theme
    val remoteColorScheme = RemoteColorScheme(localColorScheme)

    // 3. Manually bind the dynamic system color using the canonical WearM3 name
    val dynamicBg = RemoteColor.createNamedRemoteColor(
        "WearM3.primaryContainer",
        localColorScheme.primaryContainer
    )

    return WearWidgetDocument(
        // 4. Set the dynamic background
        background = WearWidgetBrush.color(dynamicBg)
    ) {
        // 5. Apply the synced color scheme to foreground elements
        RemoteMaterialTheme(colorScheme = remoteColorScheme) {
            MyWidgetContent()
        }
    }
}
```

### `RemoteModifier.background(RemoteColor)` Ignores Clipping {#remotemodifierbackgroundremotecolor-ignores-clipping}

b/495827025

**Symptom:** When applying a dynamic `RemoteColor` (e.g., from
`RemoteMaterialTheme.colorScheme`) using the `background()` modifier, any
preceding `clip()` modifiers are ignored. The component will render as an
unclipped rectangle or square.

**Workaround:** To draw a shaped background with a dynamic theme color, use the
`drawWithContent` modifier and explicitly draw the shape (e.g., `drawCircle` or
`drawRoundRect`) with a `RemotePaint` object instead of using the `background`
modifier.

```kotlin
val iconBgColor = RemoteMaterialTheme.colorScheme.primary

// Fails (ignores CircleShape clip, renders as square)
RemoteBox(
    modifier =
        RemoteModifier.size(32.rdp)
            .clip(CircleShape, DpSize(32.dp, 32.dp))
            .background(iconBgColor),
) {
    // ...
}

// Works: Explicit Canvas drawing
RemoteBox(
    modifier =
        RemoteModifier.size(32.rdp).drawWithContent {
            val w = size.width
            val h = size.height
            drawCircle(
                paint =
                    RemotePaint().apply {
                        color = iconBgColor
                        style = PaintingStyle.Fill
                    },
                center = RemoteOffset(w / 2f.rf, h / 2f.rf),
                radius = w / 2f.rf
            )
            drawContent()
        }
)
```

## Feedback {#feedback}

As this project is currently in an **Early Access Program (EAP)** phase, your
participation and feedback are critical to shaping the future of Wear Widgets.
Please keep in mind that this is a technical preview of non-public code; as
such, you will likely encounter bugs and limitations as we continue to refine
the product. We deeply appreciate your collaboration and are committed to
providing help and support as you integrate Wear Widgets into your app.

We value your input in any form, though we are especially eager to hear your
perspective on the following categories to help us improve the developer
experience:

- **Bugs & Technical Hurdles:** Reports of crashes, rendering failures, or
  unexpected behavior on specific devices or emulators.
- **API & DSL Usability:** Feedback on the Remote Compose DSL, including any
  inconsistencies, "awkward" syntax, or missing features that would simplify
  your workflow.
- **Points of Confusion:** Sections of this guide or the underlying programming
  model (e.g., Actions vs. Lambdas) that require better documentation or clearer
  examples.
- **Developer Experience:** Challenges related to testing, such as limitations
  with Android Studio Previews or the manual sideloading process for the
  renderer.
- **Productionizing & Deployment:** Issues related to logging, telemetry, and
  the overall reliability of the widget lifecycle in a "real-world" app
  environment.

To provide feedback or request support, please reach out to your primary Google
contact or use the designated Buganizer components for your project.

## Updates {#updates}

### Wear Widgets EAP 1.3 - PENDING {#wear-widgets-eap-13}

#### Features {#features-13}

- **SDK 37 Baseline:** The build environment has been upgraded to **SDK 37**.
  Recent Jetpack alpha libraries now require `compileSdk 37`.
- **Dependencies Updated:** Updated `androidx.compose.remote` core libraries to
  `1.0.0-alpha09` (Note: Pinned back to `alpha08` in this sample for
  compatibility, see below), `androidx.glance.wear` to `1.0.0-alpha08`,
  `androidx.compose.runtime` to `1.10.6`, and `kotlin` to `2.3.21`.

#### Known Issues {#known-issues-13}

- **[FIXED] Library ABI Incompatibility (Remote Material 3):** (b/507687866)
  This issue where `remote-material3` was incompatible with core libraries
  `>= alpha09` has been resolved in `remote-material3:1.0.0-alpha03` and core
  libraries `1.0.0-alpha10`.
- **[ADDED] `wearwidget-provider` XML Parser Rejects Full-Screen Types:**
  (b/507693943) The XML parser rejects `FULLSCREEN` and `TILE_COMPAT` container
  types in the provider info file. **Workaround:** Use `LARGE` as a fallback.
- **[ADDED]**
  [Unsupported Remote Compose Primitives Cause Runtime Crashes](#unsupported-remote-compose-primitives-cause-runtime-crashes):
  Using advanced primitives like `RemoteCollapsibleColumn` or
  `RemoteModifier.scroll()` causes the application to crash at runtime due to
  unsupported operations in the `WEAR_WIDGETS` profile.
- **[FIXED]** `RemoteComposeCreationComposeFlags.isRemoteApplierEnabled`:
  Removed as the remote applier is now fully integrated.
- **[FIXED]**
  [RemoteModifier.clip() Requires Explicit Size for Relative Shapes](#remotemodifierclip-requires-explicit-size-for-relative-shapes):
  Resolved by `RemoteShape` support.
- **[FIXED]**
  [RemoteModifier.padding() Lacks RemoteDp Support](#remotemodifierpadding-lacks-remotedp-support):
  `RemoteModifier.padding()` now natively supports `RemoteDp` values.
- **[FIXED]**
  [RemoteArrangement.Center Can Only Be Used in Vertical Contexts](#remotearrangementcenter-can-only-be-used-in-vertical-contexts):
  `RemoteArrangement.Center` now implements `HorizontalOrVertical` and can be
  used in both rows and columns.
- **[FIXED]**
  [RemoteBox Vertical Axis Requires Arrangement](#remotebox-differs-from-compose-box):
  `RemoteBox` now uses the unified `contentAlignment` parameter.
- **[FIXED]**
  [Android Studio Preview Limitations](#android-studio-preview-limitations):
  Multiple preview instances in the same file now render correctly.
- **[FIXED]**
  [Dynamic Theme Colors for Document Backgrounds Require Manual Binding](#dynamic-theme-colors-for-document-backgrounds-require-manual-binding):
  You can now set the background of a `WearWidgetDocument` to a dynamic theme
  color using `RemoteColorScheme` properties directly.

#### Migration Instructions {#migration-instructions-13}

- **Upgrade to SDK 37:** Ensure your `compileSdk` and `targetSdk` are set to
  `37`.
- **Padding API Updates:** `RemoteModifier.padding` now strictly requires
  `RemoteDp`. Replace: `.padding(10.dp)` with `.padding(10.rdp).`
- **Padding Parameter Changes:** `RemoteModifier.padding` no longer accepts the
  `right` parameter. Use `end` instead for RTL support.
- **Clip API Updates:** `RemoteModifier.clip` now expects `RemoteShape` (e.g.,
  `RemoteCircleShape`) and no longer accepts explicit dimensions. Replace:
  `.clip(CircleShape, DpSize(60.dp, 60.dp))` with `.clip(RemoteCircleShape)`.
- **Theme Color Scheme Updates:** `RemoteColorScheme` is now final. Use the
  `copy()` method to override roles. Replace:
  `object : RemoteColorScheme() { ... }` with `RemoteColorScheme().copy(...)`.
- **RemoteBox Migration:** `RemoteBox` now uses `contentAlignment` instead of
  `horizontalAlignment` and `verticalArrangement`. This is a breaking change.
- **rememberRemoteIntValue Migration:** Deprecated in
  `androidx.compose.remote:remote-creation-compose:1.0.0-alpha07`. Replace with
  `rememberMutableRemoteInt(initialValue)`. Note that `rememberMutableRemoteInt`
  takes a literal value rather than a lambda.

### Wear Widgets EAP 1.2 — 24 Mar 2026 {#wear-widgets-eap-12-24-mar-2026}

#### Features {#features}

- **Semantic Text Styles are available.** See
  [Semantic Typography](#semantic-typography).
- **Standalone Renderer Updates.** A few user-facing features have been added to
  make it more usable. See
  [Add and Preview your Widget](#add-and-preview-your-widget).
- **Dependencies Updated:** The
  [samples](https://github.com/ithinkihaveacat/wear-os-samples/tree/wear-widgets/WearWidget)
  have been updated to target the latest available library versions (including
  SNAPSHOTs where necessary) to demonstrate the newest features and fixes. See
  [Migration Instructions](#migration-instructions) for details on updating your
  project.

#### Known Issues {#known-issues}

- **[ADDED]**
  [RemoteImage failures when using large or unscaled bitmaps](#remoteimage-failures-when-using-large-or-unscaled-bitmaps):
  Using `RemoteImage` with large or unscaled bitmaps can cause the widget to
  fail to render and display an empty screen. A workaround is to manually scale
  bitmaps to the exact required display size before use.
- **[ADDED]**
  ["Full Bleed" Backgrounds Limited to Solid Colors](#full-bleed-backgrounds-limited-to-solid-colors):
  Complex backgrounds like images or gradients appear inset with a visible
  border rather than true "full bleed". Currently, full bleed rendering is only
  possible with solid colors and there is no known workaround.
- **[ADDED]**
  [Dynamic Theme Colors for Document Backgrounds Require Manual Binding](#dynamic-theme-colors-for-document-backgrounds-require-manual-binding):
  Setting a document background to a dynamic theme color using
  `RemoteColorScheme` causes a compiler error. A workaround is to manually
  construct a named `RemoteColor` using a local `ColorScheme` fallback.
- **[ADDED]**
  [RemoteModifier.background(RemoteColor) Ignores Clipping](#remotemodifierbackgroundremotecolor-ignores-clipping):
  Applying a dynamic `RemoteColor` with the `background()` modifier ignores any
  preceding `clip()` modifiers. To create a shaped background, use
  `RemoteCanvas` to explicitly draw the shape instead.
- **[FIXED]** [Multiple APIs Are Restricted](#multiple-apis-are-restricted): The
  issue where many APIs (like `.rs`, `.rf`, `RemotePainter`) were marked as
  restricted and required suppressing lint errors with
  `@SuppressLint("RestrictedApi")` has been fixed.
- **[FIXED]**
  [RemoteMaterialTheme.typography Does Not Expose Semantic Styles](#typography-does-not-expose-semantic-styles):
  The issue where semantic text styles (e.g., `titleLarge`) were internal and
  required a local `MyWidgetTypography` helper object to access has been fixed.

#### Migration Instructions {#migration-instructions}

- **Update Library Versions:** Update your `libs.versions.toml` or
  `build.gradle` to use `androidx.compose.remote` libraries at
  **`1.0.0-alpha06`** and `androidx.glance.wear` libraries at
  **`1.0.0-alpha05`**. For SNAPSHOT dependencies, update the Maven repository
  URL to use a **Build ID** of **`14978996`**.
- **Package Name Changes:** Several classes have moved to new packages. Most
  notably, `WearWidgetParams` has moved to
  `androidx.glance.wear.core.WearWidgetParams`, and `RemoteImage` is now located
  at `androidx.compose.remote.creation.compose.layout.RemoteImage`.
- **Semantic Text Styles:** Semantic text styles (e.g., `titleLarge`,
  `bodyMedium`) are now natively available through
  `RemoteMaterialTheme.typography`. You can apply them directly to your text
  components and remove any temporary local workarounds (like
  `MyWidgetTypography`). See [Semantic Typography](#semantic-typography).
- **Drawing API Updates:** `RemotePaint` properties now utilize Compose-native
  graphics classes rather than standard Android `Paint` equivalents. When
  drawing, you must now use `PaintingStyle.Stroke` (instead of
  `Paint.Style.STROKE`) and `StrokeCap.Round` (instead of `Paint.Cap.ROUND`).
  Additionally, properties like `strokeWidth` must now be explicitly converted
  to remote floats using `.rf`.

### Wear Widgets EAP 1.1 — 9 Feb 2026 {#wear-widgets-eap-11-9-feb-2026}

#### Features {#features-1-1}

- **Standalone Renderer:** A standalone renderer has been added to the updated
  `com.google.android.wearable.protolayout.renderer` package. (Find it under
  "Widget Tray Viewer" in the launcher). This allows you to preview multiple
  widgets in SMALL and LARGE sizes in a vertically scrolling carousel.

- **Dependencies Updated:** The samples have been updated to target the latest
  available library versions (including SNAPSHOTs where necessary) to
  demonstrate the newest features and fixes. See
  [Migration Instructions](#migration-instructions) for details on updating your
  project.

- **Expanded Component Samples:** Added new samples including
  `FullBleedImageButtonSample`, `RotatedTextSample`, `AnchoredTextSample`, and
  `BitmapCanvasSample` to demonstrate advanced rendering capabilities.

- **Theming Support:** Introduced `CustomThemeSample` and updated existing
  samples to fully utilize `RemoteMaterialTheme`, allowing for both
  system-driven dynamic theming and custom brand overrides.

- **Improved Developer Tools:** The `widget-switch` script has been rewritten to
  be faster and more reliable. It now waits for a "State saved" log confirmation
  instead of force-stopping the app, preserving the process state.

- **Semantic Typography Helper:** Added `MyWidgetTypography` to provide access
  to standard Wear OS text styles (e.g., `titleLarge`, `bodyMedium`) while they
  remain internal in the library.

- **Documentation:** Added a new guide detailing the differences between Compose
  and Remote Compose: [Compose Differences](COMPOSE-DIFFERENCES.md).

#### Known Issues {#known-issues-1-1}

- [ADDED]
  [RemoteMaterialTheme.typography Does Not Expose Semantic Styles](#typography-does-not-expose-semantic-styles):
  Semantic text styles (e.g., `titleLarge`) are currently internal and cannot be
  accessed directly. A workaround using a local `MyWidgetTypography` object is
  provided.
- [ADDED]
  [RemoteImage Visibility and Duplication Issues](#remoteimage-visibility-and-duplication-issues):
  Multiple `RemoteImage` instances may fail to render correctly. A workaround
  using `RemoteCanvas` is provided.
- [FIXED]
  [Multiple DataStores Active Crash](#multiple-datastores-active-crash-datastore-conflict):
  The `IllegalStateException: There are multiple DataStores active` crash has
  been fixed in the library. The underlying conflict that occurred during rapid
  re-deployments or configuration changes is resolved.
- [FIXED]
  [`drawScaledBitmap` Crash](#crash-when-using-drawscaledbitmap-with-resource-bitmaps):
  The crash when using `drawScaledBitmap` with resource-backed bitmaps has been
  fixed. You can now use it without explicitly providing the `srcSize` argument
  (see `BitmapCanvasSample`).

#### Migration Instructions {#migration-instructions-1-1}

- **Update Library Versions:** Update your `libs.versions.toml` or
  `build.gradle` to use `androidx.compose.remote:remote-core:1.0.0-alpha03` and
  `androidx.glance.wear:wear:1.0.0-alpha02`. For SNAPSHOT dependencies, use a
  **Build ID** of 14765146.
- **Disable Remote Applier:** You **must** set
  `RemoteComposeCreationComposeFlags.isRemoteApplierEnabled = false` as early as
  possible in your application's lifecycle (e.g., in `Application.onCreate()`).
  - **Implementation Example:** The sample app achieves this by defining a
    custom `WearWidgetApplication` class and registering it in the
    `AndroidManifest.xml`.
  - See
    [WearWidgetApplication.kt](../app/src/main/java/com/google/example/wear_widget/WearWidgetApplication.kt)
    for the code.
  - This requirement is temporary and is expected to be removed in a future
    alpha release.
- **Mandatory Click Actions:** `RemoteButton` and `clickable` modifiers now
  require a valid `Action` to be passed to the `onClick` parameter. Empty arrays
  (`arrayOf()`) are no longer sufficient. See
  [WidgetCatalog.kt](../app/src/main/java/com/google/example/wear_widget/WidgetCatalog.kt)
  for examples.
