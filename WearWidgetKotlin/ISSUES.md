# Implementation Issues and Workarounds

This document tracks technical hurdles and API "weirdness" encountered while implementing the Wear Widget samples using the Remote Compose Material3 library.

## 1. Library Package Migration

**Problem:** The `1.0.0-SNAPSHOT` version of the library moved `painterRemoteColor` from `androidx.compose.remote.creation.compose.capture.painter` to `androidx.compose.remote.creation.compose.painter`.
**Impact:** This broke existing code in the project (`HelloWidget.kt`).
**Workaround:** Updated all imports to the new package path.

## 2. Restricted `Boolean.rb` Extension

**Problem:** The convenience extension property `Boolean.rb` (e.g., `false.rb`) failed to resolve, likely due to `@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)` constraints in the latest SNAPSHOT.
**Workaround:** Used the public `RemoteBoolean(boolean)` constructor instead.

## 3. Inflexible Color Customization

**Problem:** `RemoteButtonColors` is not a data class, and the `copy()` method found in the source code was unresolved at compile time. Additionally, the standard `RemoteButtonDefaults.buttonColors()` factory does not accept parameters.
**Workaround:** Manually instantiated `RemoteButtonColors` using its 8-argument constructor.

## 4. `RemoteModifier.padding` Type Mismatch

**Problem:** The `padding` modifier does not accept `RemoteDp` (returned by `.asRdp()`), even though `RemoteModifier.size` and other layout modifiers do. It only accepts `Dp` or `RemoteFloat`.
**Workaround:** Used standard Compose `Dp` (e.g., `11.dp`) which utilizes a built-in `@Composable` conversion.

## 5. `RemoteArrangement` Ambiguity

**Problem:** `RemoteArrangement.Center` is exclusively a `Vertical` type. Using it in a `RemoteRow` (which expects a `Horizontal` type) causes a type mismatch.
**Workaround:** Used `RemoteArrangement.CenterHorizontally` for `RemoteRow`.

## 6. Named Vararg Limitation

**Problem:** Assigning a single `Action` to the named parameter `onClick` failed with: "Assigning single elements to varargs in named form is prohibited."
**Workaround:** Wrapped the action in an array: `onClick = arrayOf(ValueChange(...))`.

## 7. Stale Tile Rendering

**Problem:** The Wear OS emulator often displays stale content even after a successful `adb install`.
**Workaround:** Used `adb-tile-add` to force the system to refresh the tile's metadata and service binding, followed by `adb-tile-show` and a 5-second sleep before capturing screenshots.
