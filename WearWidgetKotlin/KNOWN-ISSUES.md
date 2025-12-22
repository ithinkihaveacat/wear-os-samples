# Known Issues and Limitations

This document tracks technical hurdles, API limitations, and known issues
encountered while implementing the Wear Widget samples using the Remote Compose
Material3 library.

## Essential APIs Are Restricted

A significant portion of the library's API surface is currently marked as
restricted (`@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)`) in the SNAPSHOT
version. This includes:

-   **Convenience Extensions:** `.rs`, `.rf`, `.rb`, and `.asRdp()` for concise
    type conversion.
-   **Core Classes:** Essential components like `RemotePainter` and various
    internal state wrappers.

This is an omission; these APIs will be made public in a future release.

**Workarounds:**

1.  **(Recommended)** Suppress the lint error by adding
    `@file:SuppressLint("RestrictedApi")` at the top of the file. This enables
    access to the full API surface required for implementation.
2.  For simple type conversions, use public constructors (e.g.,
    `RemoteString("...")`) instead of extensions where possible.

## `RemoteButtonColors` Customization Is Inflexible

b/470339092

`RemoteButtonColors` is not a data class and therefore lacks a `copy()` method.
Additionally, the standard `RemoteButtonDefaults.buttonColors()` factory does
not accept parameters. This makes it impossible to "copy" and modify an existing
configuration or override specific color slots from the default theme.

To achieve simple customizations (e.g., changing just the background color),
implementation requires:

- Manually instantiating `RemoteButtonColors` using its verbose 8-argument
  constructor.
- Duplicating boilerplate code to explicitly handle disabled states and other
  properties that are not being customized.

**Workaround:** Manually instantiate `RemoteButtonColors` providing all 8
arguments.

## `RemoteModifier.padding` Lacks `RemoteDp` Support

b/470964182

The `RemoteModifier.padding` extension functions do not currently accept
`RemoteDp` values. They only support `RemoteFloat` (which requires raw pixel
values or manual conversion) or standard Compose `Dp` (which requires a
`@Composable` context to convert). Unlike other layout modifiers in the library
(such as `RemoteModifier.size` and `RemoteModifier.border`), `padding` does not
support `RemoteDp`.

```kotlin
// Fails compilation
RemoteModifier.padding(10.dp.asRdp())

// Works: Parallel modifiers like 'border' support RemoteDp
RemoteModifier.border(width = 10.dp.asRdp(), color = Color.Red)
```

**Workaround:** Use standard Compose `Dp` (e.g., `11.dp`) which utilizes a
built-in `@Composable` conversion, or manually define extension functions that
accept `RemoteDp` and delegate to `padding(all.toPx())`.

## `RemoteArrangement.Center` Is Ambiguous

`RemoteArrangement.Center` is exclusively a `Vertical` type. Using it in a
`RemoteRow` (which expects a `Horizontal` type) results in a type mismatch.

**Workaround:** Use `RemoteArrangement.CenterHorizontally` for `RemoteRow`.

## Single Element Varargs Fail in Named Parameters

Assigning a single `Action` to the named parameter `onClick` fails with a Kotlin
error: "Assigning single elements to varargs in named form is prohibited."

**Workaround:** Wrap the action in an array:
`onClick = arrayOf(ValueChange(...))`.

## Tile Rendering Is Stale After Install

The Wear OS emulator often displays stale content even after a successful
`adb install`.

**Workaround:** Use `adb-tile-add` to force the system to refresh the tile's
metadata and service binding, followed by `adb-tile-show` and a short delay
before verifying the output.
