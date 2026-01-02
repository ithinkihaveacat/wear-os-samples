# Developer Notes

This repository provides sample code illustrating the use of new Wear Widget
libraries (specifically `androidx.compose.remote` and related packages). These
libraries are currently in active development.

This document captures key learnings and configuration details to assist future
development and avoid common pitfalls encountered during the initial setup of
this project.

## 1. Using `jetpack-*` Commands with SNAPSHOTs

For the Wear Widget libraries demonstrated in this sample (such as
`androidx.compose.remote:remote-creation` and
`androidx.wear.compose.remote:compose-material3`), you **must** use the
`SNAPSHOT` version when using `jetpack-*` commands. These libraries are new and
actively evolving, so the code in this repository relies on the latest snapshot
builds.

### The Mechanism

By default, `jetpack-inspect` attempts to resolve artifacts against the
stable/release repositories. The `SNAPSHOT` argument explicitly instructs the
tool to target the AndroidX snapshot repository instead.

### The Requirement

Because these specific libraries may not yet have a stable or beta release that
matches the features used here, omitting the `SNAPSHOT` argument will likely
result in the command failing or finding an older, incompatible version.

## 2. Dependencies and Requirements

### ProtoLayout Renderer

This project requires `com.google.android.wearable.protolayout.renderer` version
**1.5.7 or later** on the target device.

You can verify the version installed on your device using the following command:

```bash
adb shell dumpsys package com.google.android.wearable.protolayout.renderer | \
  grep -m 1 versionName | \
  awk -F= '{print $2}'
```

## 3. Preview Dependencies and Configuration

To enable Compose Previews for `HelloWidget`, specifically using `RemotePreview`
combined with `@WearPreviewDevices` for multi-device preview generation, the
following tooling dependencies are required:

- `androidx.compose.ui:ui-tooling-preview` (Core preview support)
- `androidx.compose.remote:remote-tooling-preview` (Remote preview support)
- `androidx.wear.compose:compose-ui-tooling` (Wear specific preview annotations
  like `@WearPreviewDevices`)

### Configuration Decision

In this project, these dependencies are configured using `implementation()`
rather than the more typical `debugImplementation()`.

```kotlin
// app/build.gradle.kts
implementation(libs.ui.tooling.preview)
implementation(libs.wear.compose.ui.tooling)
implementation(libs.remote.tooling.preview)
```

### Why?

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

### Implications

Using `implementation()` means these tooling libraries are included in the
release APK. This increases the APK size and includes code that is not needed
for the production app.

### Alternative Approach (Debug Source Set)

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
