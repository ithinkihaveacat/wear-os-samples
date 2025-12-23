# Bug: RemoteBox API Inconsistency with Standard Compose Box

**Date:** Tuesday, 23 December 2025 **Affected Component:**
`androidx.compose.remote.creation.compose.layout.RemoteBox` **Severity:** Medium
(API Usability/Consistency)

## Overview

The `RemoteBox` composable in the Remote Compose Material3 library diverges from
the standard Compose `Box` API. This inconsistency creates friction for
developers migrating existing Compose knowledge and code to Remote Compose, as
it breaks established mental models regarding alignment and arrangement.

## Expected Behavior (Standard Compose)

In standard Jetpack Compose (`androidx.compose.foundation.layout.Box`), the
`Box` composable uses a single `contentAlignment` parameter of type `Alignment`
to control the positioning of its children across both horizontal and vertical
axes.

```kotlin
// Standard Compose Signature
@Composable
inline fun Box(
    modifier: Modifier = Modifier,
    contentAlignment: Alignment = Alignment.TopStart, // Single parameter, Alignment type
    propagateMinConstraints: Boolean = false,
    content: @Composable BoxScope.() -> Unit
)
```

## Actual Behavior (Remote Compose)

The `RemoteBox` composable splits the alignment control into two separate
parameters: `horizontalAlignment` and `verticalArrangement`. In particular, it
uses `RemoteArrangement.Vertical` for the vertical axis instead of
`RemoteAlignment.Vertical`.

```kotlin
// Remote Compose Signature (Snapshot 1.0.0-SNAPSHOT)
@RemoteComposable
@Composable
public fun RemoteBox(
    modifier: RemoteModifier = RemoteModifier,
    horizontalAlignment: RemoteAlignment.Horizontal = RemoteAlignment.Start,
    verticalArrangement: RemoteArrangement.Vertical = RemoteArrangement.Top, // Uses Arrangement, not Alignment
    content: @Composable () -> Unit
)
```

## Details & Analysis

### 1. Decomposed Layout Configuration

Standard `Box` uses one `contentAlignment` (e.g., `Alignment.Center`).
`RemoteBox` requires defining `horizontalAlignment` and `verticalArrangement`
separately.

### 2. Type Mismatch

`RemoteBox` uses `RemoteArrangement` for the vertical axis.

- **Alignment** defines how a child is positioned within a parent (e.g., Top, Center, Bottom).
- **Arrangement** defines how children are distributed relative to each other along a flow axis (e.g., SpaceBetween).

Conceptually, a `Box` stacks children on top of each other (z-axis); it does not "arrange" them in a sequence like a Row or Column. Therefore, an `Arrangement` parameter—which implies spacing logic—is ill-suited for a layout that lacks a flow direction.

| Container       | Horizontal Parameter           | Vertical Parameter                                        |
| :-------------- | :----------------------------- | :-------------------------------------------------------- |
| `RemoteRow`     | `RemoteArrangement.Horizontal` | `RemoteAlignment.Vertical` (Correct)                      |
| `RemoteColumn`  | `RemoteAlignment.Horizontal`   | `RemoteArrangement.Vertical` (Correct)                    |
| **`RemoteBox`** | `RemoteAlignment.Horizontal`   | **`RemoteArrangement.Vertical` (Inconsistent)**           |

### 3. Underlying Type Conversion Logic

The internal implementation explicitly acknowledges this semantic mismatch by manually converting the `RemoteArrangement` type (intended for linear flow) back into the standard `Alignment` type (intended for positioning) required by the underlying system:

```kotlin
// androidx/compose/remote/creation/compose/layout/RemoteBox.kt

private fun RemoteArrangement.Vertical.toComposeUiAlignment(): androidx.compose.ui.Alignment.Vertical {
    return when (this) {
        RemoteArrangement.Top -> androidx.compose.ui.Alignment.Top
        RemoteArrangement.Center -> androidx.compose.ui.Alignment.CenterVertically
        RemoteArrangement.Bottom -> androidx.compose.ui.Alignment.Bottom
        else -> {
            System.err.println("Unsupported RemoteArrangement $this")
            androidx.compose.ui.Alignment.CenterVertically
        }
    }
}
```

## Impact

1. **Developer Confusion:** Developers attempting to use
   `RemoteAlignment.CenterVertically` (as they would in a `Row` or standard
   `Box` equivalent) will encounter type errors.
2. **Boilerplate:** Centering an item requires more verbose syntax:
   - **Standard:** `contentAlignment = Alignment.Center`
   - **Remote:**
     `horizontalAlignment = RemoteAlignment.CenterHorizontally, verticalArrangement = RemoteArrangement.Center`
3. **Inconsistency:** It breaks the symmetry with `RemoteRow` and
   `RemoteColumn`.

## Recommendation

Update `RemoteBox` to accept `RemoteAlignment.Vertical` for its vertical
positioning parameter, aligning it with standard Compose `Box` semantics and the
other Remote Compose containers. Ideally, provide a single `contentAlignment`
parameter of type `RemoteAlignment` that covers both axes, or at least use
`RemoteAlignment` for both individual axes.
