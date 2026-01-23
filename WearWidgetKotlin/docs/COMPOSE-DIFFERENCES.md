# Remote Compose vs. Standard Compose

This document outlines key differences ("deltas") between the Remote Compose API
(used for Wear Widgets) and standard Jetpack Compose. It categorizes these
differences to help developers distinguish between architectural necessities and
temporary API gaps.

## The Remote Architecture

The fundamental difference lies in the architecture: Remote Compose separates
the UI definition (Composition) from its execution (Rendering).

In this model, your app logic acts as a "recorder," executing once to produce a
serialized **document** of the UI and its dependency graph. This document is
then transferred to the remote rendering host (the System UI), which handles all
subsequent state updates and user interactions locally without requiring the app
process to be active. This separation allows the system to manage resources more
efficiently, reducing unnecessary process wakeups and cross-process
communication.

### Analogy to DisplayList

You can think of the "Recording Phase" as similar to building a declarative
**DisplayList** (or `RenderNode`). Your app constructs a tree of drawing
commands and state dependencies, which are then handed off to a separate
renderer (the System UI process) for execution, much like how the main UI thread
hands off work to the `RenderThread`.

## Compose Parallels and Similarities

Despite the architectural differences necessitated by its remote nature, Remote
Compose is designed to feel immediately familiar to Jetpack Compose developers.
The core mental model and syntax are intentionally aligned.

- **Declarative UI Model:** Just like standard Compose, you describe _what_ the
  UI should look like for a given state, rather than imperatively mutating
  views. The framework handles the complexity of updating the display when state
  changes.
- **Composable Tree Structure:** You build UIs by nesting composable functions.
  The structure of your code mirrors the structure of the UI, with parent
  composables (like `RemoteColumn`) containing children (like `RemoteText`).
- **Modifier System:** Styling and layout are handled through a chainable
  `Modifier` object (here, `RemoteModifier`). Concepts like padding, background,
  and size work identically to their standard counterparts.
- **Unidirectional Data Flow:** State flows down, and events flow up. Parent
  composables pass data to children, and children report user interactions back
  up to parents (via Actions), maintaining a clear separation of concerns.
- **Component Parallels:** Most standard components have a direct remote
  equivalent with similar parameters. `Column` becomes `RemoteColumn`, `Row`
  becomes `RemoteRow`, `Box` becomes `RemoteBox`, and `Text` becomes
  `RemoteText`.
- **Material Design Theming:** Theming works the same way, with a hierarchy of
  color and typography provided by `RemoteMaterialTheme`. This allows for
  consistent branding and design system implementation across your app and
  widgets.
- **State Management:** You use `remember` (specifically
  `rememberRemoteIntValue` etc.) to hold state across recompositions, mirroring
  the `remember { mutableStateOf(...) }` pattern in standard Compose.

## Feature Differences

While the mental model is similar, the "remote" nature of the execution imposes
specific constraints on available features:

- **Components:** The component library is a subset of Wear Compose. Complex,
  interactive components (like `ScalingLazyColumn` or `SwipeToDismiss`) are
  replaced by simpler remote equivalents or are not yet available.
- **Theming:** `RemoteMaterialTheme` mirrors the structure of `MaterialTheme`
  but relies on `RemoteColor` references. Dynamic color extraction from the
  user's wallpaper or system theme is handled implicitly by the renderer rather
  than explicitly in your code.
- **Text & Typography:** `RemoteText` supports standard styling (color, size,
  weight) but lacks support for `AnnotatedString` (rich text within a single
  element) and custom paragraph styling. Font support is currently limited to
  system fonts.
- **Animation:** Detailed, frame-by-frame control via `Animatable` or
  `updateTransition` is not supported. Animations are declarative: you specify
  an `animationSpec` on a modifier (e.g., `RemoteModifier.size(...)`), and the
  system handles the interpolation.
- **Accessibility:** Semantics are supported via `RemoteModifier.semantics`, but
  properties are serialized to the remote host. You cannot attach arbitrary
  accessibility actions or delegates that execute code in your app process.
- **Touch & Input:** Advanced gesture detection (`PointerInput`, `Draggable`) is
  not supported. Interaction is limited to click events (`clickable`) that
  trigger declarative Actions.

## Architectural Differences

The following differences stem directly from the recorder/renderer architecture
described above.

### Click Handling: Actions vs. Lambdas

Standard Compose uses lambdas for event handling, allowing arbitrary code
execution. Because the widget UI lives in the System process, it cannot call
back into your app's lambdas synchronously. Remote Compose instead uses
serializable **Action** objects (declarative instructions for the host).

- **Standard Compose:** `Modifier.clickable { count.value++ }`
- **Remote Compose:**
  `RemoteModifier.clickable(actions = listOf(ValueChange(count, count + 1.ri)))`

### Canvas Logic: Recorded vs. Executed

In Standard Compose, `Canvas` `onDraw` is executed every frame. Because the
remote host executes drawing instructions independently of the app process,
`RemoteCanvas` commands are **recorded** once and replayed. Dynamic behavior
must be encoded using the `drawConditionally` DSL so it can be evaluated locally
by the renderer.

- **Standard Compose:** `Canvas { if (isActive) drawCircle(...) }`
- **Remote Compose:**
  `RemoteCanvas { drawConditionally(isActive) { drawCircle(...) } }`

### Strings: Interpolation vs. Concatenation

Standard Kotlin string templates (`"$value"`) are evaluated once in your app
process. To update text based on a remote value without waking your app, the
concatenation logic must be sent to the System process. Remote Compose requires
constructing strings using the `.rs` and `toRemoteString()` extensions to build
a dependency graph in the document.

- **Standard Compose:** `Text("Count: $count")`
- **Remote Compose:** `RemoteText("Count: ".rs + count.toRemoteString())`

### Branching Logic: `if` vs. `.select()`

Standard `if/else` statements cannot be used to conditionally set properties
based on `RemoteBoolean` state. You must use the `.select()` operator (similar
to a ternary operator). Because standard Kotlin `if` is evaluated immediately
during the recording phase—before the runtime value is known on the watch—the
conditional logic must instead be encoded into the document's dependency graph.

**Compiler Plugin Limitations:** While standard Compose uses a compiler plugin
to manage recomposition, it cannot easily repurpose standard Kotlin control flow
syntax (like `if`) for this use case. Transforming `if` statements into
serialized remote instructions would require fundamentally changing the language
semantics.

- **Standard Compose:** `val color = if (isError) Color.Red else Color.Green`
- **Remote Compose:** `val color = isError.select(Color.Red.rc, Color.Green.rc)`

### Animation: Explicit vs. Implicit

Remote Compose uses `RemoteModifier.animationSpec(enabled = true)` to implicitly
animate property changes, whereas Standard Compose typically uses explicit
state-driven animations (e.g., `animateFloatAsState`). The renderer handles
property interpolation locally; the app process simply declares the desire to
animate changes and sends the new end-state.

- **Standard Compose:**
  `val size by animateDpAsState(targetSize); Modifier.size(size)`
- **Remote Compose:**
  `RemoteModifier.size(targetSize).animationSpec(enabled = true)`

### State: Primitives vs. Remote Wrappers

Standard Compose `State<T>` objects hold actual values in the app process.
Remote Compose properties instead require **references** (or "futures") like
`RemoteInt` or `RemoteColor` (via `.ri`, `.rc` extensions). These are not
containers for data; they are pointers to state that lives and changes on the
remote host. This is why you cannot read their value directly in a standard
Kotlin `if` statement during the recording phase.

- **Standard Compose:** `val color: Color = Color.Red`
- **Remote Compose:** `val color: RemoteColor = Color.Red.rc`

### Recomposition: Local Scope vs. Remote Scope

Both systems are reactive, but observe state in different scopes. Standard
Compose tracks **local app state**; changes trigger the app to re-execute its
code ("recompose"). Remote Compose constructs a dependency graph bound to
**remote host state**; changes trigger the host to refresh the UI locally
without involving the app process. This allows the app to provide a document and
then suspend.

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
