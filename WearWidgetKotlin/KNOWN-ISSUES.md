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

Event handlers in Remote Compose (like `onClick`) do not accept lambda functions
(`{ ... }`) because the UI is rendered in a remote process that cannot execute
local code. Instead, interactions are defined using declarative `Action` objects
(e.g., `ValueChange`, `LaunchActivity`).

When assigning a single action to the `onClick` named parameter, you must wrap
the action in an array.

**The Issue:** `RemoteButton` defines `onClick` as a `vararg` parameter to
support multiple actions. In Kotlin, you cannot assign a single element to a
`vararg` parameter using named syntax without wrapping it.

**Workaround:** Wrap single actions in an array using `arrayOf()`.

```kotlin
// Standard Compose (Not Supported)
// Button(onClick = { count++ }) { ... }

// Remote Compose (Correct)
RemoteButton(
    // Must use named arguments for clarity with other params like 'modifier'
    modifier = RemoteModifier.padding(10.dp),
    onClick = arrayOf(ValueChange(count, count + 1)) // Wrapped in array
) { ... }
```

**Passing an existing array:**

If you have an array of actions, pass it directly. Do not use the spread
operator (`*`) with named arguments, as it produces a "redundant spread
operator" warning.

```kotlin
val myActions = arrayOf(
    ValueChange(count, count + 1),
    LaunchActivity(...)
)

RemoteButton(
    modifier = RemoteModifier.padding(10.dp),
    onClick = myActions // Pass directly (no * needed)
) { ... }
```

**Alternative:** You can pass the action as a **positional argument** (the first
parameter) to avoid the array wrapper entirely:

```kotlin
RemoteButton(
    ValueChange(count, count + 1), // No array needed!
    modifier = RemoteModifier.padding(10.dp)
) { ... }
```

**Implications of Declarative Actions:**

Because `onClick` accepts a data object rather than a lambda, the execution
model is fundamentally different from standard Compose. Actions are serialized
instructions sent to the remote host:

1. **No Arbitrary Code Execution:** You cannot execute standard Kotlin code
   inside the handler. Calls like `Log.d()`, `viewModel.update()`, or
   `Toast.makeText()` are impossible because the widget runs in a remote process
   that does not share your app's memory or code.
2. **Pre-calculated Logic:** All logic must be resolved at **composition time**.
   You cannot write `onClick = { if (isActive) doThis() else doThat() }`.
   Instead, you must conditionally pass the correct action object when the
   widget is built: `onClick = if (isActive) ActionA else ActionB`.
3. **State vs. Computation:** Actions like `ValueChange` do not "increment" a
   value dynamically in your code; they send an instruction to the remote host
   to update a specific state variable to a new value (which is often a
   pre-calculated expression).
4. **Serialization of Side Effects:** Complex objects like `PendingIntent` are
   "captured" during composition. The library stores them in a side-table and
   sends a simple reference index to the remote host. This means you cannot
   generate Intents dynamically _at the moment of the click_; they must be fully
   formed when the widget is composed.
5. **Limited API:** You are restricted to the specific side effects supported by
   the `Action` API (primarily `ValueChange` for internal state and
   `LaunchActivity`/`PendingIntent` for external interactions).
