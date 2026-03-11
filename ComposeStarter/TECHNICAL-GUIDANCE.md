# Navigation 3: Wear OS vs. Mobile Technical Reference

Navigation 3 introduces a unified architecture across Jetpack Compose platforms. However, due to the unique form factor and interaction paradigms of Wear OS, the implementation details diverge significantly from the mobile (phone/tablet) versions.

This document serves as a technical reference detailing the core architectural differences, conceptual divergences, and features designed for mobile that you are unlikely to utilize on Wear OS.

---

## 1. Scene Strategies: `SwipeDismissableSceneStrategy` vs. Adaptive Layouts

The most significant divergence between Mobile and Wear OS in Navigation 3 revolves around the `SceneStrategy`. A `SceneStrategy` is the decision-maker that dictates *how* the `NavBackStack` (the list of `NavEntry` instances) is translated into a visual `Scene` on the screen.

### Mobile: Adaptive Multi-Pane Layouts
On mobile, `SceneStrategy` is the primary mechanism for implementing adaptive layouts (e.g., list-detail views on tablets).
*   **The Default:** `SinglePaneSceneStrategy`. It always displays the topmost `NavEntry`.
*   **Adaptive Layouts:** Mobile relies heavily on the `androidx.compose.material3.adaptive:adaptive-navigation3` artifact. This provides advanced strategies like `ListDetailSceneStrategy`. It uses metadata attached to `NavEntry`s (e.g., `listPane()` or `detailPane()`) to dynamically construct split-pane views if the device window is wide enough.
*   **Chaining Strategies:** Mobile developers often chain strategies (`CustomTwoPaneStrategy() then SinglePaneSceneStrategy()`) so the UI degrades gracefully on smaller screens.

### Wear OS: Swipe-to-Dismiss and Predictive Back
Wear OS does not utilize multi-pane adaptive layouts. Instead, the entire `SceneStrategy` concept is co-opted to integrate the ubiquitous "swipe-to-dismiss" gesture and OS-level predictive back animations.
*   **The Wear Standard:** `SwipeDismissableSceneStrategy` (from `androidx.wear.compose:compose-navigation3`).
*   **API Level Adaptation (The Hidden Complexity):** The `SwipeDismissableSceneStrategy` intelligently switches its internal `Scene` implementation based on the Android API level:
    *   **API <= 35 (`SwipeToDismissScene`):** Wraps the content in a classic Wear Compose `SwipeToDismissBox`. The box detects the edge swipe and triggers the `onBack` action, popping the stack.
    *   **API >= 36 (`PredictiveBackScene`):** Integrates with Android's modern Predictive Back system. It uses `NavigationBackHandler` to listen for system events and orchestrates the "peek-through" scaling animations natively.

---

## 2. Animation and Transition Management

The way screen transitions are animated differs fundamentally based on the UX paradigms of the platforms. 

On mobile, navigation transitions are typically "fire-and-forget"—a user taps a button, and a pre-defined animation (like a crossfade or slide) plays to completion. 

On Wear OS, the primary method of backward navigation is a physical, interactive edge swipe. **The visual transition must track precisely with the user's finger movement**, allowing them to "peek" at the previous screen and smoothly snap back or cancel the gesture. This interactive, gesture-driven requirement forces a much more complex underlying implementation than simple Compose transitions. The `SwipeDismissableSceneStrategy` handles this abstraction, but it results in two completely different animation architectures under the hood depending on the OS version.

### Mobile: `NavDisplay` Transitions
On mobile, transitions are typically handled globally by the `NavDisplay` composable itself, or overridden via metadata on a per-`NavEntry` basis.
*   `NavDisplay` accepts `transitionSpec`, `popTransitionSpec`, and `predictivePopTransitionSpec` parameters.
*   These are fed into the `AnimatedContent` container underlying the `NavDisplay`.

### Wear OS API <= 35: The `Unit` Key Override
On older API levels, Wear OS completely bypasses the `NavDisplay`'s built-in `AnimatedContent` crossfades to allow the physical gesture to drive the animation.
*   **The `Unit` Key Hack:** The internal `SwipeToDismissScene` forces `override val key: Any = Unit`. This tricks the `NavDisplay` into thinking the *scene itself* hasn't changed, even when the underlying `NavEntry` content has. This prevents `NavDisplay` from triggering its "fire-and-forget" crossfades.
*   **Manual Animation:** Because `NavDisplay` animations are disabled, the `SwipeToDismissScene` uses its own `Animatable` and `LaunchedEffect` blocks to manually drive a scale/fade entrance animation, while relying on the `SwipeToDismissBox` to handle the interactive exit animation directly tied to the user's finger gesture.

### Wear OS API >= 36: Embracing `NavDisplay` (Predictive Back)
Starting in API 36, Android introduced the system-level Predictive Back gesture, which standardizes interactive back animations across form factors. Wear OS aligns much closer to the mobile architecture here, fully utilizing `NavDisplay`'s capabilities.
*   **Using Content Keys:** Unlike the older implementation, `PredictiveBackScene` correctly sets `override val key: Any = currentEntry.contentKey`. This allows `NavDisplay`'s `AnimatedContent` to trigger transitions.
*   **Metadata Injection:** To achieve the distinct Wear OS "peek-through" scaling animations, `PredictiveBackScene` automatically injects Wear-specific `ContentTransform`s (like `scaleIn` and `slideOutHorizontally`) directly into the scene's `metadata` map.
*   **Gesture Delegation:** It delegates the interactive, physical gesture tracking entirely to `NavDisplay`'s internal `NavigationBackHandler`, resulting in a cleaner implementation that natively supports the finger-tracking UX required on the wrist.

---

## 3. Wear-Specific Configuration Options

When initializing the `SwipeDismissableSceneStrategy` on Wear OS, there is an important configuration parameter you can provide that dictates back gesture behavior.

### Disabling User Swipes
If you have a screen that requires horizontal swiping (like a map or a carousel), you may need to disable the system's swipe-to-dismiss gesture to prevent conflicts.

You can do this globally by configuring the strategy:

```kotlin
val strategy = rememberSwipeDismissableSceneStrategy<NavKey>(
    isUserSwipeEnabled = false // Disables user-initiated swipes
)
```
*   **API <= 35:** This disables the swipe gesture detection within the underlying `SwipeToDismissBox`.
*   **API >= 36:** This disables the `NavigationBackHandler`, preventing the OS from intercepting the edge swipe.

*(Note: Currently, Navigation 3 does not offer a built-in way to toggle this dynamically on a per-screen basis via metadata. You must manage this state at the strategy level.)*

---

## 4. Unused Mobile Features on Wear OS

Because of the constraints of the wrist-based form factor, several prominent Navigation 3 features highlighted in the official documentation are generally inapplicable to Wear OS development.

### Material 3 Adaptive Scenes
*   **Artifact:** `androidx.compose.material3.adaptive:adaptive-navigation3`
*   **Feature:** `rememberListDetailSceneStrategy()`, `listPane()`, `detailPane()`.
*   **Why it's unused:** Wear OS screens are too small to support side-by-side list and detail views. Navigation is strictly sequential (single-pane).

### DialogSceneStrategy
*   **Artifact:** `androidx.navigation3:navigation3-ui`
*   **Feature:** `DialogSceneStrategy()`. Displays an entry marked with `dialog()` metadata as a floating window over the previous entry.
*   **Why it's unused (usually):** Wear OS dialogs are typically full-screen `Dialog` composables (from `androidx.wear.compose.material3`) that obscure the entire screen beneath them. The concept of a smaller window floating over a visible background screen is a mobile/tablet paradigm. While technically possible, it violates Wear OS design guidelines.

### Bottom Sheets
*   **Feature:** The Bottom Sheet recipe provided in the `nav3-recipes` repository.
*   **Why it's unused:** Wear OS does not utilize bottom sheets. The interaction model relies on vertical scrolling (`TransformingLazyColumn`) and horizontal swipe-to-dismiss, not dragging modal sheets up from the bottom edge.

---

## 4. Shared Concepts: State Preservation Mechanics

Despite the differences in presentation, the underlying mechanism for preserving the back stack across process death is identical on both platforms.

*   **`rememberNavBackStack()`:** This function wraps `rememberSaveable`. It takes a `SavedStateConfiguration` containing a `SerializersModule` to serialize the polymorphic `NavKey` hierarchy into the Android `Bundle`.
*   **`NavEntryDecorator` & `SaveableState`:** Both platforms use the `SaveableStateHolderNavEntryDecorator` by default. This decorator wraps every `NavEntry`'s content with a `SaveableStateProvider`, ensuring that `rememberSaveable` calls *inside* the individual screen composables function correctly even when the screen is pushed deep into the back stack and popped back out.
*   **ViewModel Scoping (`lifecycle-viewmodel-navigation3`):** Neither platform scopes `ViewModel`s to back stack entries automatically. Both mobile and Wear OS require developers to explicitly provide the `rememberViewModelStoreNavEntryDecorator()` to the `NavDisplay`. This ensures that when a `NavEntry` is popped via `SwipeToDismissBox` (Wear) or `PredictiveBack` (Mobile), the associated `ViewModelStore` is cleared, preventing memory leaks. If provided, developers must ensure the `SaveableStateHolderNavEntryDecorator` is also passed, as overriding `entryDecorators` clears the defaults.

---

## Future Revisions (Placeholders)

*   **Nested Navigation Graphs:** Investigating how modularized navigation (`api` vs `impl` modules) translates to Wear OS when the app has multiple complex flows (e.g., a fitness tracking flow vs. a settings flow).