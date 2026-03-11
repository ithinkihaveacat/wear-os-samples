# Getting Started with Navigation 3 on Wear OS

Navigation 3 is the latest navigation library designed from the ground up for Jetpack Compose. This guide provides a focused, step-by-step approach to implementing Navigation 3 specifically for Wear OS applications. 

*(Note: This guide is derived from the official mobile Navigation 3 documentation but is heavily tailored for Wear OS paradigms, specifically utilizing the `SwipeDismissableSceneStrategy`.)*

## Core Concepts

*   **`NavKey`**: A type-safe, serializable identifier for a destination (screen) in your app.
*   **`NavBackStack`**: A mutable list of `NavKey` instances representing the navigation history. You push and pop items directly from this list.
*   **`rememberNavBackStack`**: A composable that creates and persists the back stack across configuration changes and process death.
*   **`NavDisplay`**: The core UI component that observes the back stack and renders the active screen.
*   **`EntryProvider`**: A mapping DSL that links a `NavKey` to its actual `@Composable` UI.
*   **`SwipeDismissableSceneStrategy`**: The Wear-specific strategy that wraps your screens in a swipe-to-dismiss gesture and handles native back animations.

---

## Step 1: Add Dependencies

Add the required Navigation 3, Wear Compose, and Serialization dependencies to your project.

**In `gradle/libs.versions.toml`:**
```toml
[versions]
nav3Core = "1.0.0"
wearCompose = "1.6.0-alpha10" # Requires 1.6.0+ for Navigation 3 support
kotlinxSerialization = "1.8.0"

[libraries]
androidx-navigation3-runtime = { module = "androidx.navigation3:navigation3-runtime", version.ref = "nav3Core" }
androidx-navigation3-ui = { module = "androidx.navigation3:navigation3-ui", version.ref = "nav3Core" }
wear-compose-navigation3 = { module = "androidx.wear.compose:compose-navigation3", version.ref = "wearCompose" }
kotlinx-serialization-json = { module = "org.jetbrains.kotlinx:kotlinx-serialization-json", version.ref = "kotlinxSerialization" }

[plugins]
kotlinx-serialization = { id = "org.jetbrains.kotlin.plugin.serialization", version.ref = "kotlin-version" }
```

**In `app/build.gradle.kts`:**
```kotlin
plugins {
    alias(libs.plugins.kotlinx.serialization)
}

dependencies {
    implementation(libs.androidx.navigation3.runtime)
    implementation(libs.androidx.navigation3.ui)
    implementation(libs.wear.compose.navigation3)
    implementation(libs.kotlinx.serialization.json)
}
```

---

## Step 2: Define Destinations (`NavKey`s)

Screens are defined as strongly typed, serializable objects or data classes that implement the `NavKey` interface.

```kotlin
import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface Screen : NavKey {
    @Serializable
    data object Home : Screen

    @Serializable
    data class Details(val itemId: String) : Screen
}
```

---

## Step 3: Setup `NavDisplay` and the Back Stack

At the root of your application, initialize the back stack and the Wear OS scene strategy, then plug them into `NavDisplay`.

```kotlin
import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import androidx.wear.compose.navigation3.rememberSwipeDismissableSceneStrategy

@Composable
fun WearApp() {
    // 1. Create the persistent back stack starting at the Home screen
    val backStack = rememberNavBackStack(Screen.Home)
    
    // 2. Initialize the Wear OS swipe-to-dismiss strategy
    val strategy = rememberSwipeDismissableSceneStrategy<NavKey>()

    // 3. Render the NavDisplay
    NavDisplay(
        backStack = backStack,
        sceneStrategy = strategy,
        entryProvider = entryProvider {
            // 4. Map keys to Composables
            entry<Screen.Home> {
                HomeScreen(
                    onNavigateToDetails = { id -> backStack.add(Screen.Details(id)) }
                )
            }
            entry<Screen.Details> { key ->
                DetailsScreen(
                    itemId = key.itemId,
                    onBack = { backStack.removeLast() }
                )
            }
        }
    )
}
```

---

## Step 4: Perform Navigation Actions

Because the back stack is just a customized `MutableList`, navigation is incredibly straightforward. You perform operations directly on the `backStack` instance:

*   **Navigate Forward**: `backStack.add(Screen.Details("123"))`
*   **Navigate Back**: `backStack.removeLast()` or `backStack.removeLastOrNull()`
*   **Clear and Reset**: `backStack.clear(); backStack.add(Screen.Home)` (or use list operations to replace the stack).

---

## Step 5: (Optional) Scope ViewModels to Destinations

By default, ViewModels are scoped to the `Activity`. Navigation 3 provides a specific artifact (`androidx.lifecycle:lifecycle-viewmodel-navigation3`) to safely scope a ViewModel to a `NavEntry` on the back stack. When the destination is popped off the back stack, the ViewModel is cleared.

1.  Add the dependency:
    ```kotlin
    implementation("androidx.lifecycle:lifecycle-viewmodel-navigation3:...")
    ```
2.  Add the ViewModel store decorator to your `NavDisplay`'s `entryDecorators`. You must also explicitly include the `SaveableStateHolderNavEntryDecorator` when providing custom decorators to retain Compose `rememberSaveable` state:

    ```kotlin
    import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
    import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator

    NavDisplay(
        backStack = backStack,
        sceneStrategy = strategy,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        entryProvider = entryProvider {
            entry<Screen.Home> { 
                // Any viewModel() requested here will be scoped to this NavEntry
                val viewModel: HomeViewModel = viewModel()
                HomeScreen(...) 
            }
        }
    )
    ```