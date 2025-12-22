# Known Issues and Limitations

This document tracks technical hurdles, API limitations, and known issues
encountered while implementing the Wear Widget samples using the Remote Compose
Material3 library. It focuses on documenting practical workarounds and necessary
adaptations where the API diverges from expectations established by standard
Compose, Wear OS Tiles, or general Android development paradigms.

## Essential APIs Are Restricted

A significant portion of the library's API surface is currently marked as
restricted (`@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)`) in the SNAPSHOT
version. This includes:

- **Convenience Extensions:** `.rs`, `.rf`, `.rb`, and `.asRdp()` for concise
  type conversion.
- **Core Classes:** Essential components like `RemotePainter` and various
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

## `RemoteArrangement.Center` Can Only Be Used in Vertical Contexts

If you're seeing a type mismatch when using `RemoteArrangement.Center` in a
`RemoteRow`, use `RemoteArrangement.CenterHorizontally` instead. Unlike standard
Compose where `Arrangement.Center` works for both axes, Remote Compose requires
axis-specific constants for centering.

**Solution:**

```kotlin
// For horizontal centering (RemoteRow):
RemoteRow(horizontalArrangement = RemoteArrangement.CenterHorizontally) { ... }

// For vertical centering (RemoteColumn):
RemoteColumn(verticalArrangement = RemoteArrangement.Center) { ... }
```

**Why this differs from standard Compose:**

In standard Compose, `Arrangement.Center` implements `HorizontalOrVertical`,
allowing it to be used in both `Row` and `Column`:

```kotlin
// Standard Compose: Arrangement.Center works for both axes
Row(horizontalArrangement = Arrangement.Center) { ... }
Column(verticalArrangement = Arrangement.Center) { ... }
```

In Remote Compose, `RemoteArrangement.Center` is typed as
`RemoteArrangement.Vertical` only. This limitation does not apply to
`SpaceBetween`, `SpaceEvenly`, and `SpaceAround`; they can be used in both
horizontal and vertical contexts because they implement the
`HorizontalOrVertical` interface.

## `RemoteBox` Differs from Standard `Box`

The `RemoteBox` API differs from standard Compose `Box` in two ways:

1. **Parameter count:** `Box` uses a single `contentAlignment` parameter;
   `RemoteBox` uses separate `horizontalAlignment` and `verticalArrangement`
   parameters.

2. **Vertical axis type:** `RemoteBox` uses `RemoteArrangement.Vertical` for
   vertical positioning, whereas `RemoteRow` and `RemoteColumn` use
   `RemoteAlignment` for their cross-axis parameters.

**Standard Compose (`Box`):**

```kotlin
Box(
    modifier = Modifier.fillMaxSize(),
    contentAlignment = Alignment.Center  // Single parameter for both axes
) { ... }
```

**Remote Compose (`RemoteBox`):**

```kotlin
RemoteBox(
    modifier = RemoteModifier.fillMaxSize(),
    horizontalAlignment = RemoteAlignment.CenterHorizontally,
    verticalArrangement = RemoteArrangement.Center,  // Note: Arrangement, not Alignment
) { ... }
```

**Comparison with `RemoteRow` and `RemoteColumn`:**

`RemoteRow` and `RemoteColumn` use `RemoteAlignment` for their cross-axis
parameters, consistent with standard Compose. `RemoteBox` uses
`RemoteArrangement` for its vertical axis:

<!-- markdownlint-disable MD013 -->

| Container      | Main Axis                      | Cross Axis                   |
| :------------- | :----------------------------- | :--------------------------- |
| `RemoteRow`    | `RemoteArrangement.Horizontal` | `RemoteAlignment.Vertical`   |
| `RemoteColumn` | `RemoteArrangement.Vertical`   | `RemoteAlignment.Horizontal` |
| `RemoteBox`    | `RemoteAlignment.Horizontal`   | `RemoteArrangement.Vertical` |

<!-- markdownlint-enable MD013 -->

**Workaround:** Specify both parameters when using `RemoteBox`. For vertical
positioning, use `RemoteArrangement` constants (`Top`, `Center`, `Bottom`)
rather than `RemoteAlignment` constants (`Top`, `CenterVertically`, `Bottom`).

## Single Element Varargs Fail in Named Parameters

Assigning a single `Action` to the named parameter `onClick` fails with a Kotlin
error: "Assigning single elements to varargs in named form is prohibited."

**Workaround:** Wrap the action in an array:
`onClick = arrayOf(ValueChange(...))`.
