# Migrating from Navigation 2 to Navigation 3 on Wear OS

Navigation 3 represents a fundamental shift in how Jetpack Compose handles navigation state, offering significant architectural advantages over Navigation 2 (the `NavController` / `NavHost` era). 

This guide details the specific steps, gotchas, and architectural changes required to migrate a Wear Compose app from Navigation 2 to Navigation 3, drawing directly from the lessons learned while migrating the official `ComposeStarter` repository.

## Key Advantages of Navigation 3

*   **Direct Back Stack Control**: The `NavBackStack` is fundamentally just a mutable list of `NavKey` objects. You control it exactly like you would any Kotlin `MutableList` (`add`, `removeLast`, `clear`).
*   **Compose-First Design**: The architecture treats navigation state purely as a Compose State object, removing the heavy, opaque `NavController` routing logic.
*   **Type-Safe by Default**: String-based routes are eliminated entirely. Navigation utilizes serializable data objects and data classes.
*   **Decoupled Presentations (Scene Strategies)**: The UI transition layer (`NavDisplay` and `SwipeDismissableSceneStrategy`) is entirely separated from the state tracking (`NavBackStack`), enabling simpler integration of native Wear OS gestures.

---

## Migration Steps

### 1. Update Dependencies

You must remove the old `androidx.wear.compose:compose-navigation` dependency and introduce the new split Navigation 3 dependencies, along with Kotlin serialization support.

**Remove:**
```kotlin
implementation("androidx.wear.compose:compose-navigation:...")
```

**Add:**
```kotlin
implementation("androidx.navigation3:navigation3-runtime:...") // State logic
implementation("androidx.navigation3:navigation3-ui:...")      // Display logic
implementation("androidx.wear.compose:compose-navigation3:...") // Wear gestures
implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:...")

// Requires the kotlinx-serialization plugin in your build.gradle.kts
```

---

### 2. Update Destinations to Implement `NavKey`

In Navigation 2, you might have used strings or generic objects for routing. In Navigation 3, you **must** implement the `NavKey` marker interface and annotate every screen object with `@Serializable`.

*Why is this required?* To guarantee that the back stack can be saved and restored across process death, the underlying `navigation3-runtime` relies on `kotlinx-serialization` to bundle the state.

**Before (Navigation 2 - Generic Type-Safe Routes):**
```kotlin
sealed class Screen {
    data object Landing : Screen()
    data object List : Screen()
}
```

**After (Navigation 3 - NavKey + Serializable):**
```kotlin
import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface Screen : NavKey {
    @Serializable
    data object Landing : Screen

    @Serializable
    data object List : Screen
}
```

---

### 3. Replace the Routing Logic (`NavController` to `NavBackStack`)

Replace your `NavController` with a `NavBackStack` initialized via `rememberNavBackStack`. You also need to instantiate the `SwipeDismissableSceneStrategy` specifically for Wear OS.

**Before (Navigation 2):**
```kotlin
val navController = rememberSwipeDismissableNavController()
```

**After (Navigation 3):**
```kotlin
val backStack = rememberNavBackStack(Screen.Landing)
val strategy = rememberSwipeDismissableSceneStrategy<NavKey>()
```

---

### 4. Replace `NavHost` with `NavDisplay` and the `entryProvider` DSL

The `NavHost` container and its internal `composable("route") { ... }` builder DSL are replaced by `NavDisplay` and the `entryProvider { entry<Key> { ... } }` DSL.

**Before (Navigation 2):**
```kotlin
SwipeDismissableNavHost(navController = navController, startDestination = "menu") {
    composable("menu") {
        GreetingScreen(
            onShowList = { navController.navigate("list") }
        )
    }
    composable("list") {
        ListScreen()
    }
}
```

**After (Navigation 3):**
```kotlin
NavDisplay(
    backStack = backStack,
    sceneStrategy = strategy,
    entryProvider = entryProvider {
        entry<Screen.Landing> {
            GreetingScreen(
                onShowList = { backStack.add(Screen.List) }
            )
        }
        entry<Screen.List> { 
            ListScreen() 
        }
    }
)
```

---

## Critical Lessons from the Field (Gotchas)

During the migration of the `ComposeStarter` repository, several subtle issues were encountered that you should avoid:

### 1. The Split Architecture and the `mutableStateListOf` Trap
In Navigation 2, a single artifact provided both the routing logic and the UI container. In Navigation 3, these are split into:
*   `navigation3-ui`: Contains the generic `NavDisplay` composable. It accepts *any* `List<T>` and just knows how to animate transitions.
*   `navigation3-runtime`: Contains the "batteries-included" state management (`rememberNavBackStack`, `NavKey`, and the `entryProvider` DSL).

Because `NavDisplay` is so generic, it is extremely tempting to bypass the `navigation3-runtime` entirely and just build your back stack using Compose's standard `remember { mutableStateListOf(...) }`.

**While this will appear to work and animate correctly, a standard `mutableStateListOf` will not survive Android process death.** If the app goes to the background and the system reclaims memory, the user's navigation history will be lost.

For production apps, you should almost always include both artifacts, implement `NavKey`, and use `rememberNavBackStack()` (which safely wraps `rememberSaveable` internally).

### 2. Generics Mismatches
When configuring Navigation 3, the `NavDisplay` composable is highly generic: `fun <T : Any> NavDisplay(...)`. It expects the back stack, the scene strategy, and the entry provider to all share the exact same generic type `T`.

When defining your routes using a sealed class or interface, you will typically type it like this: `sealed interface Screen : NavKey`.

The `rememberNavBackStack(Screen.Home)` function automatically infers its generic type as the base `NavKey` interface, **not** your specific `Screen` interface. 

If you explicitly type your strategy to your specific sealed class:
```kotlin
// INFERRENCE MISMATCH
val backStack = rememberNavBackStack(Screen.Home) // Inferred as NavBackStack<NavKey>
val strategy = rememberSwipeDismissableSceneStrategy<Screen>() // Explicitly Screen
```
When you pass these to `NavDisplay(backStack = backStack, sceneStrategy = strategy)`, the Kotlin compiler will fail because the `T` in `SceneStrategy<T>` (`Screen`) does not match the `T` in `NavBackStack<T>` (`NavKey`).

**The Fix:** Always align the generic type of your strategy with the generic type of your back stack (which is almost always `NavKey` when using the runtime artifact).

```kotlin
// CORRECT
val strategy = rememberSwipeDismissableSceneStrategy<NavKey>()
```

### 3. ViewModel Scoping Behavior Changes
In Navigation 2, calling `viewModel()` or `hiltViewModel()` inside a `composable()` automatically scoped the ViewModel to the `NavBackStackEntry`. It was cleared when the user navigated backward.

In Navigation 3, `entryProvider` does not do this automatically. If you call `viewModel()` inside an `entry`, it is scoped to the **Activity** by default. This causes memory leaks on Wear OS, as the ViewModel state persists even after the screen is swiped away.

To restore Navigation 2's automatic scoping behavior, you must:
1. Include the `androidx.lifecycle:lifecycle-viewmodel-navigation3` dependency.
2. Supply the `rememberViewModelStoreNavEntryDecorator()` to your `NavDisplay`.
3. **CRITICAL:** If you supply *any* decorators to `NavDisplay`, you must explicitly include the default `rememberSaveableStateHolderNavEntryDecorator()` or `rememberSaveable` state will break across your app.

```kotlin
NavDisplay(
    // ...
    entryDecorators = listOf(
        rememberSaveableStateHolderNavEntryDecorator(),
        rememberViewModelStoreNavEntryDecorator()
    ),
    // ...
)
```

---

## Future Considerations (Placeholders)

*   **Handling Result Callbacks:** How to effectively return results from a pushed screen (e.g., a detail screen selecting an item and popping back).
*   **Multi-stack Scenarios:** Complex setups involving multiple distinct back stacks (less common on Wear OS, but possible for multi-tab apps).
*   **Deep Linking:** Navigation 3 deep linking mechanisms are still evolving and not covered in this base migration guide.