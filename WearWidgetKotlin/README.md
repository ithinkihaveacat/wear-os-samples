# Developer Notes

This document captures key learnings and configuration details to assist future development and avoid common pitfalls encountered during the initial setup of this project.

## 1. Using `jetpack-*` Commands with SNAPSHOTs

When working with AndroidX libraries that are in active development or using snapshot versions (e.g., `1.0.0-SNAPSHOT`), the `jetpack-inspect` and related commands require explicit instruction to target the snapshot repository.

### The Issue
By default, `jetpack-inspect` attempts to resolve artifacts against the stable/release repositories. If you simply run:
```bash
jetpack-inspect androidx.compose.remote:remote-creation-compose
```
It may fail or find an older version if the library is only available as a snapshot or if you specifically need the latest snapshot changes.

### The Solution
You must append the `SNAPSHOT` argument to the command. This tells the tool to look in the AndroidX snapshot repository.

**Correct Usage:**
```bash
jetpack-inspect androidx.compose.remote:remote-creation-compose SNAPSHOT
```

### Documentation Suggestion
The documentation or help output for `jetpack-inspect` (and related `jetpack-*` tools) should prominently mention the `SNAPSHOT` argument. A usage example specifically for snapshots would be very valuable, as it is a common requirement when working with cutting-edge Jetpack libraries. For example:
> "To inspect a library version from the snapshot repository, append 'SNAPSHOT' to the artifact name."

## 2. Preview Dependencies and Configuration

To enable Compose Previews for `HelloWidget`, specifically using `RemotePreview` combined with `@WearPreviewDevices` for multi-device preview generation, the following tooling dependencies are required:

- `androidx.compose.ui:ui-tooling-preview` (Core preview support)
- `androidx.compose.remote:remote-tooling-preview` (Remote preview support)
- `androidx.wear.compose:compose-ui-tooling` (Wear specific preview annotations like `@WearPreviewDevices`)

### Configuration Decision
In this project, these dependencies are configured using `implementation()` rather than the more typical `debugImplementation()`.

```kotlin
// app/build.gradle.kts
implementation(libs.ui.tooling.preview)
implementation(libs.wear.compose.ui.tooling)
implementation(libs.remote.tooling.preview)
```

### Why?
The preview composable, `HelloWidgetPreview`, is defined in the same file as the production code (`HelloWidget.kt`), which resides in the `main` source set.
- The `main` source set is compiled for **all** build variants (debug and release).
- If `debugImplementation` were used, the preview dependencies would only be available in the `debug` variant.
- Consequently, the `release` build would fail to compile because `HelloWidget.kt` (in `main`) would try to import preview classes (like `RemotePreview` and `@WearPreviewDevices`) that are missing in the release classpath.

### Implications
Using `implementation()` means these tooling libraries are included in the release APK. This increases the APK size and includes code that is not needed for the production app.

### Alternative Approach (Debug Source Set)
A cleaner alternative is to move the preview code to a dedicated `debug` source set:
1.  Create `app/src/debug/java/com/google/example/wear_widget/HelloWidgetPreview.kt`.
2.  Move `HelloWidgetPreview` to this file.
3.  Use `debugImplementation()` for the dependencies.

**Downsides:**
- The preview code is separated from the component being previewed, which can be less convenient for rapid iteration.
- You might need to adjust visibility modifiers (e.g., making internal composables `public` or keeping them in the same package) to allow the debug code to access the composables defined in `main`.

For this simple sample, keeping the preview co-located and using `implementation()` was chosen for simplicity, but for a production app, the `debug` source set approach is generally preferred to keep the release APK optimized.
