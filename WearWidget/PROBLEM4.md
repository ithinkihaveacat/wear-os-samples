# PROBLEM4 - Frame Rendering Regression and Environment Issues for Wear Widget Previews

## Background

In `PROBLEM3.md`, we aimed to achieve Android Studio compatibility for Wear Widget previews by removing custom annotations (like `@WearWidgetPreview`) and using standard Compose `@Preview` annotations with conventions (e.g., `group = "WearWidget"`).

We successfully modified the discovery task (`DiscoverPreviewsTask.kt`) to identify these previews and infer `extensionParams` (like `frame` and `title`) from standard `@Preview` properties, populating `previews.json` correctly.

## The Problem: Rendering Failures and Environment Issues

While discovery now works with standard annotations, we introduced a regression in the rendering pipeline when trying to verify the changes in the `WearWidget` sample app workspace. The `renderPreviews` task failed to generate screenshots, leaving the output directory empty.

Multiple environment and dependency issues were encountered:
1.  **Composite Build Issues**: When using `includeBuild` to test local plugin changes, the plugin failed to locate the test harness classes (`Project with path ':renderer-android' not found`).
2.  **Dependency Conflicts**: The test runner failed with `AbstractMethodError` regarding `kotlinx.serialization` methods, due to version mismatches between `compose-ai-tools` (using `1.11.0`) and `WearWidget` (resolving `1.7.3`).
3.  **Missing Transitive AAR Classes**: After resolving the serialization issue by forcing versions, all tests failed with `NoClassDefFoundError` for classes in `data-a11y-core` (e.g., `RoundClipKt`). This indicates that the custom `renderPreviews` test task does not correctly handle or extract classes from transitive AAR dependencies on the host unit test classpath.

## Previous Working State

Previously, things worked fine without any of these complex environmental workarounds. The working state used the custom `@WearWidgetPreview` annotation. The discovery task searched workspace files for this specific annotation to identify Wear Widget previews and applied the custom frames accordingly during rendering.

## Current State

-   **Discovery**: Works as intended with standard `@Preview(group = "WearWidget")`. `previews.json` is generated with correct `extensionParams`.
-   **Rendering**: Blocked in the `WearWidget` workspace due to classpath issues with transitive AAR dependencies in the `renderPreviews` task.
-   **Verification**: Frame rendering was verified in the `compose-ai-tools` repository by adding a test preview, but full verification in the user's `WearWidget` workspace is blocked.

## Suggested Next Steps

Given the difficulties in making the new convention-based discovery work with the existing rendering infrastructure in external projects, the next person to pick up this task should consider:

1.  **Revert to Custom Annotations**: Revert to the previous working state that used `@WearWidgetPreview` to search workspace files. This approach "just worked" and avoided the complex classpath issues encountered when trying to overload standard `@Preview` annotations.
2.  **Fix AAR Classpath in Plugin**: If sticking with the new standard `@Preview` approach, the `compose-preview-plugin` needs to be updated to properly handle and extract classes from transitive AAR dependencies for its custom host-side test tasks.
