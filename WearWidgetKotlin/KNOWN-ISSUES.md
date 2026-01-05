# Known Issues and Limitations

This document tracks technical hurdles, API limitations, and known issues
encountered while implementing the Wear Widget samples using the Remote Compose
Material3 library. It focuses on documenting practical workarounds and necessary
adaptations where the API diverges from expectations established by standard
Compose, Wear OS Tiles, or general Android development paradigms.

## Multiple APIs Are Restricted

A significant portion of the library's API surface is currently marked as
restricted (`@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)`) in the SNAPSHOT
version. This includes:

- **Convenience Extensions:** `.rs`, `.rf`, `.rb`, and `.asRdp()` for concise
  type conversion.
- **Core Classes:** Essential components like `RemotePainter` and various
  internal state wrappers.

This is an omission; these APIs will be made public in a future release.

**Workarounds:**

1. **(Recommended)** Suppress the lint error by adding
   `@file:SuppressLint("RestrictedApi")` at the top of the file. This enables
   access to the full API surface required for implementation.
2. For simple type conversions, use public constructors (e.g.,
   `RemoteString("...")`) instead of extensions where possible.

## `RemoteButtonColors` Requires Explicit Definition

b/470339092

**Symptom:** Changing a single color property (e.g., background) requires
defining the colors for all states (enabled, disabled, content, etc.).

**Workaround:** Use the 8-argument `RemoteButtonColors` constructor to
explicitly define your color theme.

**Context:** `RemoteButtonColors` is not a data class and therefore lacks a
`copy()` method. Additionally, the standard
`RemoteButtonDefaults.buttonColors()` factory does not accept parameters, making
it impossible to "copy" and modify an existing configuration. This limitation
does not apply to other similar constructions, such as `RemoteIconButton`,
`RemoteTextButtonColors`, and `RemoteSolidColor`.

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

b/471153933

**Symptom:** A type mismatch error occurs when using `RemoteArrangement.Center`
in a `RemoteRow`.

**Workaround:** Use `RemoteArrangement.CenterHorizontally` for horizontal
centering.

```kotlin
// For horizontal centering (RemoteRow):
RemoteRow(horizontalArrangement = RemoteArrangement.CenterHorizontally) { ... }

// For vertical centering (RemoteColumn):
RemoteColumn(verticalArrangement = RemoteArrangement.Center) { ... }
```

**Context:**

In standard Compose, `Arrangement.Center` implements `HorizontalOrVertical`,
allowing it to be used in both `Row` and `Column`:

```kotlin
// Standard Compose: Arrangement.Center works for both axes
Row(horizontalArrangement = Arrangement.Center) { ... }
Column(verticalArrangement = Arrangement.Center) { ... }
```

In Remote Compose, `RemoteArrangement.Center` is typed as
`RemoteArrangement.Vertical` only. This limitation excludes `SpaceBetween`,
`SpaceEvenly`, and `SpaceAround`, which implement the `HorizontalOrVertical`
interface.

## `RemoteBox` Differs from Standard `Box`

b/471212869

The `RemoteBox` API differs from standard Compose `Box` in two ways:

1. **Parameter count:** `Box` uses a single `contentAlignment` parameter;
   `RemoteBox` uses separate `horizontalAlignment` and `verticalArrangement`
   parameters.

2. **Vertical axis type:** `RemoteBox` uses `RemoteArrangement.Vertical` for
   vertical positioning. This is inconsistent with `RemoteRow`, which uses
   `RemoteAlignment.Vertical` for vertical positioning.

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

`RemoteRow` and `RemoteColumn` use `RemoteAlignment` for their non-flow axes,
consistent with standard Compose. `RemoteBox` uses `RemoteArrangement` for its
vertical axis:

<!-- markdownlint-disable MD013 -->

| Container      | Horizontal Parameter           | Vertical Parameter           |
| :------------- | :----------------------------- | :--------------------------- |
| `RemoteRow`    | `RemoteArrangement.Horizontal` | `RemoteAlignment.Vertical`   |
| `RemoteColumn` | `RemoteAlignment.Horizontal`   | `RemoteArrangement.Vertical` |
| `RemoteBox`    | `RemoteAlignment.Horizontal`   | `RemoteArrangement.Vertical` |

<!-- markdownlint-enable MD013 -->

**Workaround:** Specify both parameters when using `RemoteBox`. For vertical
positioning, use `RemoteArrangement` constants (`Top`, `Center`, `Bottom`)
rather than `RemoteAlignment` constants (`Top`, `CenterVertically`, `Bottom`).

## Event Handling Uses Actions, Not Lambdas

**Symptom:** Standard Compose syntax for event handling (lambdas) is not
supported. Additionally, the `vararg` signature of `onClick` requires an array
wrapper when assigning single actions using named arguments.

**Workarounds:**

1. **Use Declarative Actions:** Replace `{ ... }` with `Action` objects (e.g.,
   `ValueChange`, `LaunchActivity`).
2. **Handle Vararg Syntax:** When passing a single action to the named `onClick`
   parameter, wrap it in `arrayOf()`. Alternatively, pass it as the first
   positional argument to avoid the wrapper.

   ```kotlin
   // 1. Wrapped in array (Named argument)
   RemoteButton(
       modifier = RemoteModifier.padding(10.dp),
       onClick = arrayOf(ValueChange(count, count + 1))
   ) { ... }

   // 2. Positional argument (No array needed)
   RemoteButton(
       ValueChange(count, count + 1),
       modifier = RemoteModifier.padding(10.dp)
   ) { ... }
   ```

   _Note: When passing an existing array of actions to the named parameter, pass
   it directly without the spread operator (`_`).\*

   ```kotlin
   val myActions = arrayOf(ValueChange(...), LaunchActivity(...))
   RemoteButton(onClick = myActions) { ... }
   ```

**Context:** Because widgets run in a remote process, they cannot execute local
code (lambdas). Interactions are defined by serializable `Action` objects, which
imposes several constraints:

1. **No Arbitrary Code Execution:** You cannot execute standard Kotlin code
   (e.g., `Log.d()`, `viewModel.update()`) inside the handler.
2. **Pre-calculated Logic:** Logic must be resolved at **composition time**.
   Instead of `onClick = { if (isActive) doThis() else doThat() }`, you must
   conditionally pass the correct action object:
   `onClick = if (isActive) ActionA else ActionB`.
3. **State vs. Computation:** Actions like `ValueChange` do not increment values
   dynamically; they send instructions to the remote host to update a state key
   to a new value (often a pre-calculated expression).
4. **Serialization of Side Effects:** Complex objects like `PendingIntent` are
   "captured" and serialized during composition, not at the moment of the click.
5. **Limited API:** You are restricted to the side effects supported by the
   `Action` API.
