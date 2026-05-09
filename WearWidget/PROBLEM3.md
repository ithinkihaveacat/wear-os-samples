# PROBLEM3 - Android Studio Compatibility for Remote Compose Previews

## Background

We have been working on adding support for authentic previews for Wear Widgets in the `compose-ai-tools` pipeline. In a previous iteration (documented in `PROBLEM2.md`), we generalized the system to use an `extensionParams` map and pluggable `FrameDecorator`s to avoid over-specialization.

However, we have realized a fundamental flaw in that design: it relies on custom annotations (like `@WearWidgetPreview` or a general `@RemoteComposePreview`) defined in our own libraries. This means the source code under development must depend on our library to compile, and more importantly, standard Android Studio previews will not work out of the box unless AS is also configured to understand these custom annotations (which it doesn't by default for the custom frame rendering).

We need a solution where the source code can compile just fine **without** our custom annotations or Gradle plugin, and where standard Android Studio previews just work, while still allowing `compose-ai-tools` to discover and render them with the appropriate custom frames.

## Component Locations

-   **`WearWidget` Sample App**: `/usr/local/google/home/stillers/workspace/wear-os-samples/WearWidget`
-   **`compose-ai-tools` Repository**: `/usr/local/google/home/stillers/workspace/compose-ai-tools` (Branch: `wear-widget-previews`)
    -   **Annotations**: `../../compose-ai-tools/preview-annotations/src/main/kotlin/ee/schimke/composeai/preview/`
    -   **Discovery**: `../../compose-ai-tools/gradle-plugin/src/main/kotlin/ee/schimke/composeai/plugin/DiscoverPreviewsTask.kt`
    -   **Daemon Renderer**: `../../compose-ai-tools/daemon/android/src/main/kotlin/ee/schimke/composeai/daemon/RenderEngine.kt`
    -   **Static Renderer**: `../../compose-ai-tools/renderer-android/src/main/kotlin/ee/schimke/composeai/renderer/RobolectricRenderTest.kt`

## The Problem: Android Studio Compatibility

The core requirement is that the user's source code should remain standard and not be forced to depend on `compose-ai-tools` specific annotations. We want standard `@Preview` annotations to be the source of truth.

This introduces the challenge of how to pass form-factor-specific parameters (like `frame`, `title`, `icon` for Wear Widgets) through the standard `@Preview` annotation, which has a fixed set of properties (`name`, `group`, `device`, etc.) and does not support arbitrary "extras".

We need to investigate if we can encode these parameters into existing string properties of `@Preview`, such as `name` or `group`, or if there are other standard ways to attach metadata that both Android Studio and our plugin can understand.

For example, could we use a naming convention in the `name` parameter?
```kotlin
@Preview(name = "Hello Widget [formFactor=wearWidget, frame=small]")
```
Android Studio would display this full string as the preview name, which might be acceptable, and our `DiscoverPreviewsTask` could parse it to extract `extensionParams`.

We need to evaluate the feasibility and ergonomics of such approaches.

## Current State of the Branch

The changes made so far to generalize the protocol (using `extensionParams` map) and the renderer (using `FrameDecorator`s) are currently residing in the `wear-widget-previews` branch of `compose-ai-tools`.

These changes are **as yet uncommitted** (per user instruction for this problem statement) and have not been merged to the main branch. Therefore, we do not need to worry about backward compatibility with this branch's current state. We can and should change the API to be as good as it can be to solve this new problem.

## Suggested Next Steps and Approaches

1.  **Investigate `@Preview` Properties**: Carefully check all available properties on the standard `@Preview` annotation to see if any can be used or overloaded to pass custom parameters.
2.  **Explore Parameter Encoding**: If no direct support for extras exists, explore encoding key-value pairs in the `name` or `group` fields of `@Preview`.
3.  **Update Discovery**: Modify `DiscoverPreviewsTask.kt` to extract these encoded parameters from standard `@Preview` annotations instead of looking for custom annotations.
4.  **Maintain Extensibility**: Ensure that whatever encoding scheme we choose still populates the `extensionParams` map, preserving the pluggable `FrameDecorator` system we built in the renderer.
