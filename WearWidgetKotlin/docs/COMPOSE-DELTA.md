# Remote Compose vs. Standard Compose

This document outlines key differences ("deltas") between the Remote Compose API
(used for Wear Widgets) and standard Jetpack Compose. It categorizes these
differences to help developers distinguish between architectural necessities and
temporary API gaps.

## Architectural Differences

These differences stem from the fundamental architecture of Remote Compose,
where the UI definition (Composition) happens in one process, but the execution
and rendering happen in another (the System UI). In this model, your app logic
acts as a "recorder," executing once to produce a serialized **document** of the
UI and its dependency graph. This document is then transferred to the remote
rendering host, which handles all subsequent state updates and user interactions
locally without requiring the app process to be active. This separation allows
the system to manage resources more efficiently, reducing unnecessary process
wakeups and cross-process communication.

### Click Handling: Actions vs. Lambdas

**The Difference:** Standard Compose uses lambdas for event handling, allowing
arbitrary code execution. Remote Compose uses serializable `Action` objects.

**Why:** The widget code runs in your app's process, but the UI lives in the
System process. The System process cannot call back into your app's lambda to
execute arbitrary code (like logging or network calls) synchronously.

- **Standard Compose:**

  ```kotlin
  Modifier.clickable {
      Log.d("Tag", "Clicked") // Arbitrary code
      count.value++           // State mutation
  }
  ```

- **Remote Compose:**

  ```kotlin
  RemoteModifier.clickable(
      actions = listOf(ValueChange(count, count + 1.ri)) // Declarative instruction
  )
  ```

### Canvas Logic: Recorded vs. Executed

**The Difference:** In Standard Compose, `Canvas` `onDraw` is executed every
frame. In Remote Compose, drawing commands are _recorded_ once and replayed by
the renderer.

**Why:** Because the rendering host executes the drawing instructions
independently of the app process, logic inside `RemoteCanvas` runs only at
composition time. Any dynamic or state-dependent drawing behavior must be
encoded using the `drawConditionally` DSL so it can be evaluated by the
renderer.

- **Standard Compose:**

  ```kotlin
  Canvas {
      if (isActive) drawCircle(...) // Re-evaluates when isActive changes
  }
  ```

- **Remote Compose:**

  ```kotlin
  RemoteCanvas {
      // Must use DSL for remote logic
      drawConditionally(isActive) {
          drawCircle(...)
      }
  }
  ```

### Strings: Interpolation vs. Concatenation

**The Difference:** Standard Kotlin string templates (`"$value"`) cannot be used
for dynamic text that changes on the remote side. Remote Compose requires
constructing strings using the `.rs` extension and `toRemoteString()`.

**Why:** String templates are evaluated once in your app process during
composition. To update text based on a remote value without waking your app, the
concatenation logic must be sent to the System process.

**How it works:** The expression `"Prefix: ".rs + count.toRemoteString()` builds
a dependency graph in the document. `count` is assigned a state ID, and the
string concatenation becomes a "Merge" instruction dependent on that ID. When
the state changes on the host (e.g., via a click action), the renderer
re-evaluates the merge and repaints the text—all while your app process remains
asleep.

- **Standard Compose:**

  ```kotlin
  Text("Count: $count")
  ```

- **Remote Compose:**

  ```kotlin
  RemoteText("Count: ".rs + count.toRemoteString())
  ```

### Branching Logic: `if` vs. `.select()`

**The Difference:** Standard `if/else` statements cannot be used to
conditionally set properties based on `RemoteBoolean` state. You must use the
`.select()` operator (similar to a ternary operator).

**Why:** The fundamental architecture of Remote Compose separates the
**definition** of the UI (which happens once, in your app process) from its
**execution** (which happens dynamically on the System UI).

1. **Recording Phase:** Your Composable code runs only once to generate a static
   "document" or blueprint. Calls like `.select()` do not execute logic
   immediately; instead, they **record** a conditional instruction into the
   document's dependency graph.
2. **Execution Phase:** The System UI interprets this document. When state
   changes (like a `RemoteBoolean` flipping), the System UI re-evaluates the
   dependency graph and updates the screen without waking your app.

A standard Kotlin `if (remoteBoolean)` statement attempts to evaluate
immediately during the recording phase. Since the `RemoteBoolean`'s runtime
value isn't known yet (and lives on the watch), standard branching cannot work.

**Compiler Plugin Limitations:** While standard Compose uses a compiler plugin
to manage recomposition, it cannot easily repurpose standard Kotlin control flow
syntax (like `if`) for this use case. Transforming `if` statements into
serialized remote instructions would require fundamentally changing the language
semantics, which is beyond the capabilities of the current plugin architecture.

- **Standard Compose:**

  ```kotlin
  val color = if (isError) Color.Red else Color.Green
  ```

- **Remote Compose:**

  ```kotlin
  val color = isError.select(Color.Red.rc, Color.Green.rc)
  ```

### Animation: Explicit vs. Implicit

**The Difference:** Remote Compose uses
`RemoteModifier.animationSpec(enabled = true)` to implicitly animate property
changes, whereas Standard Compose typically uses explicit state-driven
animations (e.g., `animateFloatAsState`).

**Why:** The renderer handles property interpolation locally. The app process
simply declares the desire to animate changes and sends the new end-state; the
actual animation lifecycle is managed by the remote host.

- **Standard Compose:**

  ```kotlin
  val size by animateDpAsState(targetSize)
  Modifier.size(size)
  ```

- **Remote Compose:**

  ```kotlin
  RemoteModifier
      .size(targetSize)
      .animationSpec(enabled = true)
  ```

### State: Primitives vs. Remote Wrappers

**The Difference:** Widget properties often require `RemoteInt`, `RemoteColor`,
etc. (via `.ri`, `.rc` extensions) instead of raw primitives.

**Why:** These values are not just constants; they are handles to data that may
live or change on the remote host.

- **Standard Compose:**

  ```kotlin
  val color: Color = Color.Red
  ```

- **Remote Compose:**

  ```kotlin
  val color: RemoteColor = Color.Red.rc
  ```

### Recomposition: Local Scope vs. Remote Scope

**The Difference:** Both systems are reactive, but they observe state in
different scopes. Standard Compose tracks **local app state**; changes trigger
the app to re-execute its code ("recompose"). Remote Compose constructs a
dependency graph bound to **remote host state**; changes trigger the host to
update the UI, but the app's composition logic is _not_ re-executed.

**Why:** This allows the app to provide a document and then suspend. The host
manages the UI lifecycle and state updates locally, evaluating the dependency
graph to refresh the UI without involving the app process.

- **Standard Compose:** App State Change -> App Recomposes -> UI Update.
- **Remote Compose:** Remote State Change -> Host Re-evaluates -> UI Update (App
  Asleep).

### Side Effects & Coroutines

**The Difference:** `LaunchedEffect` and `SideEffect` are generally not
supported or useful in Remote Compose.

**Why:** Because the composition only runs once to generate a snapshot,
coroutines launched "in composition" would immediately be cancelled or have no
lifecycle to attach to on the remote host.

## Temporary Limitations (Bugs or Omissions)

These differences appear to be gaps in the current API surface that may be
resolved in future releases.

### `RemoteModifier.clip()` Signature

b/477860914

**The Difference:** `RemoteModifier.clip` for `CircleShape` requires an explicit
size argument, whereas standard Compose infers it.

**Why:** Likely a current limitation in how the renderer resolves shape bounds
for certain shapes during the recording phase.

- **Standard Compose:**

  ```kotlin
  Modifier.clip(CircleShape)
  ```

- **Remote Compose:**

  ```kotlin
  // Requires explicit size to calculate corner radius
  RemoteModifier.clip(CircleShape, DpSize(60.dp, 60.dp))
  ```

### `RemoteText`: No `AnnotatedString` Support

**The Difference:** `RemoteText` does not accept `AnnotatedString`, preventing
mixed styling (e.g., bold and italic in one text block).

**Why:** The `RemoteText` component currently maps to a simpler text primitive
in the protocol that applies styles uniformly.

- **Standard Compose:**

  ```kotlin
  Text(buildAnnotatedString { ... })
  ```

- **Remote Compose:**

  ```kotlin
  // Must compose multiple Text elements manually
  RemoteRow {
      RemoteText("Hello ")
      RemoteText("World", fontWeight = FontWeight.Bold)
  }
  ```

### `RemoteBox`: Alignment vs. Arrangement

b/471212869

**The Difference:** `RemoteBox` uses `horizontalAlignment` and
`verticalArrangement` instead of a single `contentAlignment`.

**Why:** Tracked as a known issue (b/471212869). This inconsistency (using
`Arrangement` for vertical positioning in a Box) aligns `RemoteBox`'s parameters
more closely with `RemoteColumn` than standard `Box`, but breaks muscle memory.

- **Standard Compose:**

  ```kotlin
  Box(contentAlignment = Alignment.Center)
  ```

- **Remote Compose:**

  ```kotlin
  RemoteBox(
      horizontalAlignment = RemoteAlignment.CenterHorizontally,
      verticalArrangement = RemoteArrangement.Center // Arrangement, not Alignment!
  )
  ```

### `RemoteModifier.padding`: No `RemoteDp` Support

b/470964182

**The Difference:** `RemoteModifier.padding` does not accept `RemoteDp` (e.g.,
`.rdp`), forcing immediate resolution of pixel values.

**Why:** Tracked as a known issue (b/470964182). Parallel modifiers like
`.border` already support `RemoteDp`.

- **Standard/Expected:**

  ```kotlin
  Modifier.padding(10.dp)
  ```

- **Remote Compose:**

  ```kotlin
  RemoteModifier.padding(10.rdp) // Compilation Error
  RemoteModifier.padding(10.dp)  // Works, but resolves immediately
  ```

### `RemoteArrangement` Context

b/471153933

**The Difference:** `RemoteArrangement.Center` is strictly typed as `Vertical`.

**Why:** Tracked as a known issue (b/471153933). In Standard Compose,
`Arrangement.Center` implements both Horizontal and Vertical interfaces. In
Remote Compose, the type hierarchy is stricter, preventing its use in
`RemoteRow` (which expects `Horizontal`).

- **Workaround:** Use specific constants like
  `RemoteArrangement.CenterHorizontally` for rows.
