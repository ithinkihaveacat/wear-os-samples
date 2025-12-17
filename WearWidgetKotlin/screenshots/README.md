# Remote Widget Sample & Screenshot Requirements

## 1. Motivation and Scope

The primary goal of this initiative is to establish a clear, visual mapping
between `RemoteComposable` source code and its rendered output on Wear OS. This
serves as a reference catalog for developers to:

- Visualize available UI components.
- Understand the effect of specific modifiers and style options.
- Quickly identify the code required to achieve a specific visual result.

The scope is limited to UI elements that can be embedded within a
`GlanceWearWidget`'s `WearWidgetDocument`. Specifically, this concerns functions
annotated with `@RemoteComposable`.

## 2. Code Organization

### Location

All sample code must be added to the following file:
`app/src/main/java/com/google/example/wear_widget/WidgetCatalog.kt`

### Structure

- **Container:** The entry point for the widget is the `WidgetCatalog` class.
- **Integration:** To view a specific sample, update the `provideWidgetData`
  method to call the desired sample function.
  ```kotlin
  override suspend fun provideWidgetData(
      context: Context,
      params: WearWidgetParams,
  ): WearWidgetData =
      WearWidgetDocument(backgroundPainter = painterRemoteColor(Color.Black)) {
           // Swap this function call to view different samples
          BoxSample1()
      }
  ```
- **Sample Functions:** New samples should be added as top-level functions
  within the same file.

## 3. Naming Conventions

- **Function Names:**
  - Must be descriptive and numbered.
  - Use PascalCase.
  - Use underscores to "namespace" related groups if necessary (e.g.,
    `Text_Style_1`, `Text_Wrapping_2`).
  - **Immutability:** Existing function names should generally not be changed to
    preserve the link with existing screenshots.
- **Annotations:** All sample functions must be annotated with:
  ```kotlin
  @RemoteComposable
  @Composable
  ```

## 4. Screenshot Capture Process

To generate a screenshot for a sample:

1.  **Configure:** Update `provideWidgetData` in `WidgetCatalog.kt` to call the
    target sample function.
2.  **Deploy:** Build and install the application to the target device/emulator.
3.  **Activate:** Add and display the tile (e.g., using `adb-tile-add` and
    `adb-tile-show`).
4.  **Capture:** Take a screenshot of the device.
5.  **Verify:**
    - Ensure the screenshot filename matches the function name exactly (e.g.,
      `BoxSample1` -> `screenshots/BoxSample1.png`).
    - **Visual Check:** Verify the screenshot actually displays the widget
      content and not a loading state, black screen, or watch face.
    - **Retry:** If the capture is invalid (e.g., device was asleep), wake the
      device and retry the deploy/show/capture cycle 1-2 times.

## 5. Artifacts

- **Directory:** All screenshots must be stored in the `screenshots/` directory.
- **Filename:** `[FunctionName].png` (Case-sensitive match to the code).

## 6. Future Compatibility

This structure is designed to support the future generation of an automated
Markdown catalog. Adherence to the naming conventions (matching code function to
image filename) is critical for this automation.

## 7. Standardization Guidelines

- **Background:** Use `Color.Black` as the default background in
  `provideWidgetData` unless the sample specifically demonstrates background
  transparency or coloring.
- **Device Configuration:** Ideally, use a consistent Wear OS emulator
  configuration (e.g., standard round layout, consistent density) for all
  captures to ensure images are comparable.
