# PROBLEM2 - Generalizing Remote Compose Previews

## Background

We have successfully implemented authentic previews for Wear Widgets in the `compose-ai-tools` pipeline. This involved:
1.  Creating a `@WearWidgetPreview` meta-annotation.
2.  Updating `DiscoverPreviewsTask` to extract widget-specific parameters (`frame`, `title`, `icon`).
3.  Updating the renderer (`RenderEngine` and `RobolectricRenderTest`) to apply a `WearWidgetFrame` that simulates the watch face and header.

This solution works well for Wear Widgets and is currently available in the `wear-widget-previews` branch of `compose-ai-tools` (not yet merged).

## Component Locations

Here are the full paths to the various components involved:

-   **`WearWidget` Sample App**: `/usr/local/google/home/stillers/workspace/wear-os-samples/WearWidget`
-   **`compose-ai-tools` Repository**: `/usr/local/google/home/stillers/workspace/compose-ai-tools` (Branch: `wear-widget-previews`)
    -   **Annotations**: `../../compose-ai-tools/preview-annotations/src/main/kotlin/ee/schimke/composeai/preview/WearWidgetPreview.kt` (relative to `WearWidget` root)
    -   **Discovery**: `../../compose-ai-tools/gradle-plugin/src/main/kotlin/ee/schimke/composeai/plugin/DiscoverPreviewsTask.kt`
    -   **Daemon Renderer**: `../../compose-ai-tools/daemon/android/src/main/kotlin/ee/schimke/composeai/daemon/RenderEngine.kt`
    -   **Static Renderer**: `../../compose-ai-tools/renderer-android/src/main/kotlin/ee/schimke/composeai/renderer/RobolectricRenderTest.kt`

## The Problem: Over-Specialization

While the current solution works well for Wear Widgets, we need to ensure that we haven't modified `compose-ai-tools` too much in a Wear-specific direction. The goal is to ensure that the annotations and processes we've added are somewhat general and can be adapted to other form factors that may use Remote Compose in the future.

It is acceptable to have Wear Widget-specific tokens and "frames", and even for them to be the only such special-purpose frames right now. However, it should be obvious and straightforward to extend this system to other form factors without requiring major architectural changes.

## Current State of the Branch

The `wear-widget-previews` branch in `compose-ai-tools` contains working code that:
-   Discovers `@WearWidgetPreview` annotations.
-   Passes `wearWidgetFrame`, `wearWidgetTitle`, and `wearWidgetIcon` through the JSON-RPC payload.
-   Renders the `WearWidgetFrame` in `RenderEngine` (daemon) and `RobolectricRenderTest` (static renderer).

This code has **not yet been merged** to the main branch. We have not validated or checked whether this approach can be easily adapted to other form factors.

## Suggested Next Steps and Approaches

To ensure extensibility without going overboard with abstractions, we should consider the following:

### 1. Generalize Override Parameters

Currently, we added specific fields to `PreviewOverrides` and `PreviewParams`:
-   `wearWidgetFrame: String?`
-   `wearWidgetTitle: String?`
-   `wearWidgetIcon: String?`

**Approach**: Consider replacing or supplementing these with a more general map structure for extension-specific parameters, for example:
```kotlin
val extensionParams: Map<String, String> = emptyMap()
```
This would allow other form factors to pass arbitrary key-value pairs without modifying the core protocol messages.

### 2. Pluggable Frame Decorators

In `RenderEngine` and `RobolectricRenderTest`, the `WearWidgetFrame` is hardcoded as a conditional wrapper around the composable invocation.

**Approach**: Investigate if we can introduce a concept of "Frame Decorators" that can be registered or looked up based on the parameters. For example, a registry of frames:
```kotlin
interface FrameDecorator {
    @Composable
    fun Decorate(params: Map<String, String>, content: @Composable () -> Unit)
}
```
This would make it clear how to add new frames for new form factors without cluttering the main render method with specific branches.

### 3. Validation with Another Form Factor

To truly validate the generality, we should attempt to define a preview for a hypothetical non-Wear Remote Compose use case (e.g., a simple dashboard widget or a different embedded display) and see what friction we encounter when trying to add a custom frame for it.
