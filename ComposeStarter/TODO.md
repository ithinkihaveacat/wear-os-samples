# TODO: Complete Horologist Removal

The project has been partially migrated to Wear Compose 1.6 Material 3 native components, but
Horologist remains as a dependency, primarily for screenshot testing.

## Steps for Full Removal

### 1. Remove Dependencies

- [x] In `gradle/libs.versions.toml`:
  - Remove `horologist` version.
  - Remove `horologist-roboscreenshots` library.
  - Remove `horologist-compose-layout` library.
- [x] In `app/build.gradle.kts`:
  - Remove `implementation(libs.horologist.compose.layout)`.
  - Remove `testImplementation(libs.horologist.roboscreenshots)`.

### 2. Refactor Tests

The following test files currently depend on Horologist and need to be refactored to use standard
Wear Compose 1.6 Material 3 patterns:

- [x] `app/src/test/java/presentation/GreetingScreenTest.kt`
- [x] `app/src/test/java/presentation/ListScreenTest.kt`
- [x] `app/src/test/java/presentation/SampleDialogTest.kt`

#### Specific Refactoring Details

- **Replace `AppScaffold`**: Tests currently use
  `com.google.android.horologist.compose.layout.AppScaffold`. These should be updated to use the
  Material 3 `androidx.wear.compose.material3.AppScaffold`. (Done)
- **Replace `ResponsiveTimeText`**: Use the native `androidx.wear.compose.material3.TimeText`
  instead of Horologist's version. (Done)
- **Screenshot Base Class**: These tests inherit from `WearScreenshotTest`. Removing Horologist
  entirely would require migrating to a standard `ComposeContentTestRule` combined with Roborazzi's
  `captureRoboImage()` directly, or another screenshot testing harness that doesn't depend on
  Horologist. (Done)

### 3. Native Replacements (Completed in MainActivity)

- [x] Use `androidx.wear.compose.material3.AppScaffold` and `ScreenScaffold` instead of Horologist
      `AppScaffold` and `ResponsiveColumnPadding`.
- [x] Use native `TransformingLazyColumn` features (Snapping, RSB support) instead of Horologist
      rotary modifiers.
