# Bug: RemoteBox API Inconsistency with Standard Compose Box

**Date:** Tuesday, 23 December 2025 **Affected Component:**
`androidx.compose.remote.creation.compose.layout.RemoteBox` **Severity:** Medium
(API Usability/Consistency)

## Overview

The `RemoteBox` composable in the Remote Compose Material3 library significantly
diverges from the standard Compose `Box` API. This inconsistency creates
friction for developers migrating existing Compose knowledge and code to Remote
Compose, as it breaks established mental models regarding alignment and
arrangement.

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
parameters: `horizontalAlignment` and `verticalArrangement`. Crucially, it uses
`RemoteArrangement.Vertical` for the vertical axis instead of
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

### 1. Parameter Split

Standard `Box` uses one `contentAlignment` (e.g., `Alignment.Center`).
`RemoteBox` requires defining `horizontalAlignment` and `verticalArrangement`
separately.

### 2. Type Mismatch (The Core Issue)

`RemoteBox` uses `RemoteArrangement` for the vertical axis.

- **Alignment** defines how a smaller child is positioned within a larger parent
  (e.g., Top, Center, Bottom).
- **Arrangement** typically defines how _multiple_ children are distributed
  relative to each other along a main axis (e.g., SpaceBetween, SpaceAround).

Using `Arrangement` for a `Box`'s vertical positioning is semantically incorrect
and inconsistent with `RemoteRow` and `RemoteColumn`, which correctly use
`RemoteAlignment` for their cross-axis positioning.

| Container       | Main Axis                      | Cross Axis / Positioning                                  |
| :-------------- | :----------------------------- | :-------------------------------------------------------- |
| `RemoteRow`     | `RemoteArrangement.Horizontal` | `RemoteAlignment.Vertical` (Correct)                      |
| `RemoteColumn`  | `RemoteArrangement.Vertical`   | `RemoteAlignment.Horizontal` (Correct)                    |
| **`RemoteBox`** | `RemoteAlignment.Horizontal`   | **`RemoteArrangement.Vertical` (Incorrect/Inconsistent)** |

### 3. Source Code Verification

Inspection of `RemoteBox.kt` confirms the internal conversion logic maps
`RemoteArrangement` to standard Compose `Alignment`:

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
