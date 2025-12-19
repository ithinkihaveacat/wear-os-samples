# Usability Issue: Inconsistent Color Customization in `RemoteButton`

## Impact
The current implementation of `RemoteButtonColors` and its associated factory methods in `androidx.wear.compose.remote.material3` creates significant friction for developers attempting to customize button colors.

Unlike standard Jetpack Compose APIs (and even other components within the same library, like `RemoteIconButton`), `RemoteButtonColors` lacks the ability to easily "copy" and modify an existing configuration. Furthermore, the default factory method `RemoteButtonDefaults.buttonColors()` does not expose parameters for overriding specific color slots.

This forces developers to:
1.  **Abandon the default theme:** Instead of slightly tweaking the default theme (e.g., changing just the background color), they must manually instantiate `RemoteButtonColors` using its verbose 8-argument constructor.
2.  **Duplicate code:** To achieve simple customizations, developers must copy-paste boilerplate code, increasing maintenance burden and the risk of errors.

## Problem Details

### 1. `RemoteButtonColors` is not a `data class`
The class `RemoteButtonColors` is defined as a standard Kotlin `class` without a `copy()` method. This prevents the common Compose pattern of taking a default configuration and modifying only the properties of interest.

**Current Definition:**
```kotlin
public class RemoteButtonColors(
    public val containerColor: RemoteColor,
    public val contentColor: RemoteColor,
    // ... 6 other properties
)
```

**Missing Functionality:**
```kotlin
// This fails to compile:
val myColors = RemoteButtonDefaults.buttonColors().copy(containerColor = RemoteColor(Color.Red))
```

### 2. Rigid Factory Method
The `RemoteButtonDefaults.buttonColors()` function takes no arguments. In contrast, standard Compose defaults (and `RemoteIconButtonDefaults.iconButtonColors`) typically expose optional parameters for each property, allowing for concise overrides.

**Current Definition:**
```kotlin
@Composable
public fun buttonColors(): RemoteButtonColors = RemoteMaterialTheme.colorScheme.defaultButtonColors
```

**Comparison with `RemoteIconButtonDefaults` (Good):**
```kotlin
@Composable
public fun iconButtonColors(
    containerColor: RemoteColor = RemoteColor(Color.Transparent),
    // ... allows overriding specific colors
): RemoteIconButtonColors
```

## Concrete Examples

The following examples from `app/src/main/java/com/google/example/wear_widget/WidgetCatalog.kt` demonstrate the excessive verbosity required by the current API.

### Example 1: `ButtonSample4` (Custom Colors)

**Current Implementation (Verbose & Brittle):**
To change just the container and content colors, the developer must provide *all 8 arguments*, explicitly handling the disabled states that they might not even intend to customize.

```kotlin
RemoteButton(
    // ...
    colors = RemoteButtonColors(
        containerColor = RemoteColor(Color.Red),
        contentColor = RemoteColor(Color.Yellow),
        secondaryContentColor = RemoteColor(Color.Yellow),
        iconColor = RemoteColor(Color.Yellow),
        disabledContainerColor = RemoteColor(Color.Gray),
        disabledContentColor = RemoteColor(Color.LightGray),
        disabledSecondaryContentColor = RemoteColor(Color.LightGray),
        disabledIconColor = RemoteColor(Color.LightGray)
    )
)
```

**Improved Implementation (Clean & Idiomatic):**
If `RemoteButtonDefaults.buttonColors` accepted parameters (delegating to a `copy()` method or constructor with defaults), the code would be significantly cleaner:

```kotlin
RemoteButton(
    // ...
    colors = RemoteButtonDefaults.buttonColors(
        containerColor = RemoteColor(Color.Red),
        contentColor = RemoteColor(Color.Yellow),
        iconColor = RemoteColor(Color.Yellow)
        // Disabled colors automatically inferred from defaults or theme
    )
)
```

### Example 2: `ButtonSample9` (Dynamic Color Selection)

**Current Implementation:**
The developer again has to invoke the full constructor to swap between two color sets.

```kotlin
RemoteButton(
    // ...
    colors = RemoteButtonColors(
        containerColor = containerColor,
        contentColor = contentColor,
        secondaryContentColor = contentColor,
        iconColor = contentColor,
        // ... repetitive disabled colors ...
        disabledContainerColor = RemoteColor(Color.Gray),
        disabledContentColor = RemoteColor(Color.LightGray),
        disabledSecondaryContentColor = RemoteColor(Color.LightGray),
        disabledIconColor = RemoteColor(Color.LightGray)
    )
)
```

**Improved Implementation:**

```kotlin
RemoteButton(
    // ...
    colors = RemoteButtonDefaults.buttonColors(
        containerColor = containerColor,
        contentColor = contentColor
        // Secondary and Icon colors could default to contentColor or be explicitly set if needed
    )
)
```

### Example 3: `CardSample1` (Using Button as a Container)

**Current Implementation:**
The developer uses `RemoteButton` purely as a container, requiring a specific background but needing to define all other colors to satisfy the constructor.

```kotlin
colors = RemoteButtonColors(
    containerColor = RemoteColor(Color.DarkGray),
    contentColor = RemoteColor(Color.White),
    secondaryContentColor = RemoteColor(Color.LightGray),
    iconColor = RemoteColor(Color.White),
    // ... 4 more lines of disabled colors ...
)
```

**Improved Implementation:**

```kotlin
colors = RemoteButtonDefaults.buttonColors(
    containerColor = RemoteColor(Color.DarkGray),
    contentColor = RemoteColor(Color.White)
)
```

## Recommended Changes

### 1. Convert `RemoteButtonColors` to a `data class`
This aligns with other immutable data holders in the library (e.g., `RemoteSolidColor`, `RemoteLinearGradient`) and automatically provides the `copy()` method.

```kotlin
// In androidx.wear.compose.remote.material3.RemoteButton.kt
public data class RemoteButtonColors( ... )
```

### 2. Update `RemoteButtonDefaults` Factory Methods
Update `buttonColors` (and `buttonWithContainerPainterColors`) to accept parameters with default values derived from the theme. This matches the pattern already established in `RemoteIconButtonDefaults.iconButtonColors`.

```kotlin
// In androidx.wear.compose.remote.material3.RemoteButton.kt

object RemoteButtonDefaults {
    @Composable
    public fun buttonColors(
        containerColor: RemoteColor = RemoteMaterialTheme.colorScheme.primary,
        contentColor: RemoteColor = RemoteMaterialTheme.colorScheme.onPrimary,
        // ... other parameters with theme defaults
    ): RemoteButtonColors {
        // Implementation returning RemoteButtonColors(...)
    }
}
```

## Internal Consistency

Adopting these changes would bring `RemoteButton` in line with:
*   **Kotlin Conventions:** Using `data class` for simple data containers.
*   **AndroidX Compose Conventions:** Factory methods (`Defaults.colors()`) that allow granular overrides.
*   **Internal Consistency:** Matching the implementation of `RemoteIconButtonDefaults.iconButtonColors` and data classes like `RemoteSolidColor` found in the `remote-creation` library.
