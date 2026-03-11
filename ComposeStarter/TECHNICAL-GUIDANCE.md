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

The way screen transitions are animated differs fundamentally between the two platforms due to the need for native gesture handling on Wear OS.

### Mobile: `NavDisplay` Transitions
On mobile, transitions are typically handled globally by the `NavDisplay` composable itself, or overridden via metadata on a per-`NavEntry` basis.
*   `NavDisplay` accepts `transitionSpec`, `popTransitionSpec`, and `predictivePopTransitionSpec` parameters (which take standard Compose `ContentTransform`s like `slideInHorizontally`).
*   These are fed into the `AnimatedContent` underlying the `NavDisplay`.

### Wear OS: Complete Override (`val key: Any = Unit`)
Wear OS completely bypasses the `NavDisplay`'s built-in `AnimatedContent` crossfades.
*   **The `Unit` Key Hack:** Both `SwipeToDismissScene` and `PredictiveBackScene` force their internal `override val key: Any = Unit`. This tricks the `NavDisplay` into thinking the *scene itself* hasn't changed, even when the underlying `NavEntry` content has.
*   **Custom Animatable:** Because `NavDisplay` animations are effectively disabled, `SwipeToDismissScene` and `PredictiveBackScene` use their own `Animatable` and `LaunchedEffect` blocks to manually drive the scale and fade transitions. This is necessary because the transition progress is tied directly to the user's physical finger swipe (via the `SwipeToDismissBox` state or the `NavigationBackHandler` progress), rather than a simple fire-and-forget `tween()`.

---

## 3. Unused Mobile Features on Wear OS

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