# Plan: Wear Compose 1.6 Features Demo

## 1. Objectives
The goal of this project is to update the `ComposeStarter` sample to demonstrate three major features introduced in **Wear Compose 1.6.0-alpha10**:
1.  **Wear Navigation 3**: Implementing the new, type-safe navigation system specifically designed for Wear OS.
2.  **TransformingLazyColumn (TLC) Enhancements**: Showcasing the successor to `ScalingLazyColumn` with its new capabilities (Reverse Layout, Snapping, Responsive Padding).
3.  **Ambient Mode**: Integrating "Always-on" display support directly into Composables.

The final result will be a modified sample app that is easy to understand, allowing developers to copy-paste patterns into their own applications.

---

## 2. Dependencies
We need to update the project dependencies to access these alpha features.

*   **Wear Compose:** Update all `androidx.wear.compose:*` artifacts to `1.6.0-alpha10`.
*   **Navigation 3:** Add the new specific artifacts:
    *   `androidx.wear.compose:compose-navigation3:1.6.0-alpha10` (The Wear wrapper)
    *   `androidx.navigation3:navigation3-ui:1.0.0-alpha01` (Provides `NavDisplay`)

---

## 3. Feature Details & Implementation Strategy

### A. Navigation 3 (Wear OS Flavor)

**Overview:**
Navigation 3 offers a flexible, type-safe way to handle screens, moving away from the string-based routes and graph-centric model of Jetpack Navigation. 

**Important:** This completely replaces the existing `SwipeDismissableNavHost`. In Navigation 3, the "host" concept is replaced by `NavDisplay`, and the swipe-to-dismiss behavior is provided by a `SceneStrategy`.

**Key Components:**
*   **`NavDisplay`**: The main container for hosting navigation (from `androidx.navigation3.ui`).
*   **`rememberSwipeDismissableSceneStrategy`**: The Wear-specific strategy that wraps your screens in a swipe-to-dismiss box (from `androidx.wear.compose.navigation3`).
*   **Type-Safe Keys**: Defining screens as distinct objects/data classes.

**Sample Implementation:**
We will replace the existing `SwipeDismissableNavHost` in `MainActivity` with a `NavDisplay`.

*   **Define Routes:** Create a generic sealed class `Screen` (e.g., `Screen.Landing`, `Screen.List`, `Screen.Details`).
*   **Setup:**
    ```kotlin
    // 1. Define the backstack (source of truth)
    val backStack = rememberMutableStateList(Screen.Landing)
    
    // 2. Define the strategy (handles swipe-to-dismiss)
    val strategy = rememberSwipeDismissableSceneStrategy()

    // 3. Render the Display
    NavDisplay(
        backstack = backStack,
        sceneStrategy = strategy
    ) { screen ->
        // 4. Content mapping
        when (screen) {
            is Screen.Landing -> LandingScreen(
                onNavigate = { backStack.add(Screen.List) }
            )
            is Screen.List -> ListScreen(...)
        }
    }
    ```

### B. TransformingLazyColumn (TLC) Enhancements

**Overview:**
`TransformingLazyColumn` is the modern replacement for `ScalingLazyColumn`. It offers better performance and new layout capabilities. We will create a new screen (e.g., `EnhancedListScreen`) to demonstrate three specific enhancements:

1.  **Reverse Layout:**
    *   *Use Case:* Chat apps or logging screens where new items appear at the bottom.
    *   *Implementation:* Set `reverseLayout = true` in the `TransformingLazyColumn` constructor.

2.  **Responsive Padding:**
    *   *Use Case:* Automatically avoiding clipping on rounded screens without manually calculating screen shapes.
    *   *Implementation:* Use `Modifier.minimumVerticalContentPadding()`. This replaces the older `responsiveVerticalPadding`.

3.  **Snapping:**
    *   *Use Case:* Date pickers or lists where items must land in the center.
    *   *Implementation:*
        ```kotlin
        TransformingLazyColumn(
            flingBehavior = TransformingLazyColumnDefaults.snapFlingBehavior(),
            // ...
        )
        ```
        *Note:* We should also attach `RotaryScrollableDefaults.snapBehavior` if we want rotary input (crown) to snap as well.

### C. Ambient Mode

**Overview:**
Wear OS apps can now handle "Always-on" states more natively within Compose. Previously, this required complex Activity-level handling. The new `LocalAmbientModeManager` allows individual Composables to react when the watch enters low-power mode (e.g., hiding backgrounds, thinning fonts, stopping animations).

**Key Components:**
*   **`LocalAmbientModeManager`**: A CompositionLocal providing access to the ambient state.
*   **`AmbientModeAware`**: (Concept) Using the state to modify UI.

**Sample Implementation:**
We will modify the `LandingScreen` or `EnhancedListScreen` to react to ambient mode.
*   **Code Snippet:**
    ```kotlin
    val ambientSupport = LocalAmbientModeManager.current
    val isAmbient = ambientSupport.isAmbient

    if (!isAmbient) {
        // Render colorful background or heavy animations
    } else {
        // Render simple, black-and-white, thin-stroke UI
    }
    ```

---

## 4. Execution Steps

1.  **Project Setup:**
    *   Modify `libs.versions.toml` (or `build.gradle.kts`) to add the new alpha dependencies.
    *   Sync project.

2.  **Create `Navigation3Sample.kt`:**
    *   Implement the `NavDisplay` harness.
    *   Define simple "Route" objects.

3.  **Create `TlcEnhancementScreen.kt`:**
    *   Build a list that uses `TransformingLazyColumn`.
    *   Add toggles (or separate sections) to demonstrate:
        *   Reversed layout (items populating from bottom).
        *   Snapping behavior.

4.  **Integrate Ambient Mode:**
    *   Wrap the root of the application (or a specific screen) to demonstrate hiding/showing elements based on `LocalAmbientModeManager`.

5.  **Verify:**
    *   Run on emulator (API 30+ recommended).
    *   Test swipe-to-dismiss in Nav3.
    *   Test scrolling physics in TLC.
    *   Trigger Ambient Mode (via Emulator controls) and verify UI adaptation.
