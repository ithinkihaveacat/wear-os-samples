# Remote Compose vs. Standard Compose

This document outlines key differences between the
[Remote Compose API](https://developer.android.com/jetpack/androidx/releases/compose-remote)
(used for Wear Widgets) and
[standard Jetpack Compose](https://developer.android.com/develop/ui/compose). It
categorizes these differences to help developers distinguish between
architectural necessities and temporary API gaps.

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

**Analogy to DisplayList:** You can think of the "Recording Phase" as similar to
building a declarative DisplayList (or
[`RenderNode`](https://developer.android.com/reference/android/graphics/RenderNode)).
Your app constructs a tree of drawing commands and state dependencies, which are
then handed off to a separate renderer (the System UI process) for execution,
much like how the main UI thread hands off work to the `RenderThread`.

## Compose Parallels and Similarities

Despite the architectural differences necessitated by its remote nature, Remote
Compose is designed to feel immediately familiar to Jetpack Compose developers.
The core mental model and syntax are intentionally aligned.

- **[Declarative UI Model](https://developer.android.com/develop/ui/compose/mental-model):**
  Just like standard Compose, you describe _what_ the UI should look like for a
  given state, rather than imperatively mutating views. The framework handles
  the complexity of updating the display when state changes.
- **Composable Tree Structure:** You build UIs by nesting composable functions.
  The structure of your code mirrors the structure of the UI, with parent
  composables (like `RemoteColumn`) containing children (like `RemoteText`).
- **[Modifier System](https://developer.android.com/develop/ui/compose/modifiers):**
  Styling and layout are handled through a chainable `Modifier` object (here,
  `RemoteModifier`). Concepts like padding, background, and size work
  identically to their standard counterparts.
- **[Unidirectional Data Flow](https://developer.android.com/develop/ui/compose/architecture#udf):**
  State flows down, and events flow up. Parent composables pass data to
  children, and children report user interactions back up to parents (via
  Actions), maintaining a clear separation of concerns.
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
  weight) but lacks support for
  [`AnnotatedString`](https://developer.android.com/reference/kotlin/androidx/compose/ui/text/AnnotatedString)
  (rich text within a single element) and custom paragraph styling. Font support
  is limited to generic font families (serif, sans-serif, monospace, cursive)
  and bitmap fonts.
- **Animation:** Detailed, frame-by-frame control via
  [`Animatable`](https://developer.android.com/reference/kotlin/androidx/compose/animation/core/Animatable)
  or `updateTransition` is not supported. Animations are declarative: you
  specify an `animationSpec` on a modifier (e.g., `RemoteModifier.size(...)`),
  and the system handles the interpolation.
- **Accessibility:**
  [Semantics](https://developer.android.com/develop/ui/compose/accessibility/semantics)
  are supported via `RemoteModifier.semantics`, but properties are serialized to
  the remote host. You cannot attach arbitrary accessibility actions or
  delegates that execute code in your app process.
- **Touch & Input:** High-level gesture detection (`PointerInput`, `Draggable`)
  is not natively supported. While the underlying protocol supports low-level
  touch tracking, the Compose API primarily exposes simple interaction via click
  events (`clickable`) that trigger declarative Actions.

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

### Drawing Styles: Stateless vs. Stateful

**The Difference:** Standard Compose drawing commands (`DrawScope`) are
stateless; you pass style parameters (color, alpha, stroke) directly to the
function. Remote Compose uses a stateful `RemotePaint` object (mirroring
`android.graphics.Paint`) that must be configured and passed to draw commands.

**Why:** This maps more directly to the underlying display list protocol used by
the renderer.

- **Standard Compose:** `drawCircle(color = Color.Red, radius = 10f)`
- **Remote Compose:**
  `drawCircle(paint = RemotePaint().apply { remoteColor = Color.Red.rc }, radius = 10f.rf)`

### Strings: Interpolation vs. Concatenation

Standard Kotlin string templates (`"$value"`) are evaluated once in your app
process. To update text based on a remote value without waking your app, the
concatenation logic must be sent to the System process. Remote Compose requires
constructing strings using the `.rs` extension and explicit format conversions
to build a dependency graph in the document.

- **Standard Compose:** `Text("Count: $count")`
- **Remote Compose:**
  `RemoteText("Count: ".rs + count.toRemoteString(before = 3))`

Note: `toRemoteString()` requires format parameters like `before` (digit count)
and optionally `after` (decimal places) and `flags` (padding options).

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

### Control Flow: `for` vs. `loop()`

**The Difference:** Standard Kotlin `for` loops are evaluated immediately during
the recording phase. To iterate based on a dynamic value that only exists on the
remote host (e.g., a `RemoteInt` state), you must use the `loop()` DSL.

**Why:** Similar to branching logic, the iteration structure must be recorded
into the document graph to be executed by the renderer at runtime.

- **Standard Compose:** `for (i in 0 until count) { ... }`
- **Remote Compose:** `loop(0f.rf, count, 1f.rf) { i -> ... }`

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
remote host. Depending on the source, a reference might point to a **constant**,
a mutable **document variable**, a system-provided **host variable**, or an
**expression** derived from other values. This is why you cannot read their
value directly in a standard Kotlin `if` statement during the recording phase.

- **Standard Compose:** `val color: Color = Color.Red`
- **Remote Compose:** `val color: RemoteColor = Color.Red.rc`

### Resources: Polymorphism vs. Explicit Components

**The Difference:** Standard Compose uses a polymorphic `Painter` abstraction,
allowing the `Image` component to display bitmaps, vectors, or drawables
interchangeably. Remote Compose requires specific components for each resource
type: `RemoteImage` for bitmaps and `RemoteIcon` for vectors.

**Why:** The wire protocol distinguishes between heavy binary assets (bitmaps)
and lightweight serialized drawing commands (vectors). There is currently no
unified "RemotePainter" abstraction to hide this difference.

- **Standard Compose:** `Image(painter = painterResource(id), ...)` (Works for
  PNGs and SVGs)
- **Remote Compose:**
  - For Bitmaps: `RemoteImage(bitmap = ImageBitmap.imageResource(id), ...)`
  - For Vectors: `RemoteIcon(imageVector = ImageVector.vectorResource(id), ...)`

### Remote System Values

**The Difference:** You cannot use CompositionLocals like `LocalDensity.current`
or `LocalConfiguration.current` to access device configuration for remote logic.
Instead, you must use `RemoteContext` constants to create references that
resolve on the remote system.

**Why:** The code runs on the app process (recording), but the UI renders on the
system process (display). The recording process may not know the display's
configuration, current time, or sensor data at composition time.

**Examples:** Density, Window Width, Time of Day, Ambient Light Level.

- **Standard Compose:** `val px = with(LocalDensity.current) { 10.dp.toPx() }`
- **Remote Compose:**
  `val px = RemoteFloat(RemoteContext.FLOAT_DENSITY) * 10f.rf`

### Object Measurement & Layout

**The Difference:** Immediate measurement of UI elements is generally not
possible during the recording phase. You cannot access the runtime size of a
component, the width of a text string, or the intrinsic dimensions of a remote
resource using standard Compose tools.

**Why:** The app process records the _intent_ to draw, but the _actual_
measurement and layout happen later on the remote device (renderer).

- **Text:** `TextMeasurer` is not available. To align text without knowing its
  width, use `drawAnchoredText` with specific anchor points (e.g., center)
  rather than calculating offsets manually.
- **Containers:** You cannot use `Modifier.onSizeChanged`. Instead, inside
  `RemoteCanvas` or `drawWithContent`, access dimensions via
  `remote.component.width` and `height`, which are `RemoteFloat` futures, not
  immediate values.

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

## Recomposition Mechanics and Session Lifecycle

Unlike standard Jetpack Compose, which recomposes dynamically on the main thread
of an active application process, Remote Compose and Jetpack Glance operate on a
**decoupled, session-driven lifecycle**.

### Background Session Model

In standard Compose, the composition runtime is long-lived and continuously
listens for state changes. In Glance, the composition runs within a short-lived
**Session** (orchestrated in public Jetpack libraries via
`androidx.glance.session.Session` and background `WorkManager` workers like
`SessionWorker`):

- **Waking up:** When state changes (e.g., a database or `DataStore` update) or
  a system event occurs, a session is spun up.
- **Executing:** Your `@Composable` tree compiles, runs once to evaluate the new
  UI state, and serializes the result.
- **Unbinding:** Once the update is pushed and the session becomes idle, the
  connection is closed (typically after a short timeout) to preserve battery.

### Document Streaming and IPC

For Remote Compose (such as Wear OS widgets and Tiles), recomposition does not
surgically update individual UI nodes over the wire. Instead:

- Every time recomposition runs, it generates a **complete new UI document**.
- This serialized document is streamed via an IPC channel (like AIDL) from the
  app process to the remote host (System UI).
- The remote host receives the document and executes the updates locally.

### Handling Connection Loss and Severing

Because the renderer and provider run in separate processes, the connection can
be severed (e.g., if the provider app is updated, crashes, or is killed by the
system to reclaim memory):

- **Stale Content Fallback:** The renderer does not immediately blank the UI. It
  keeps displaying the last successfully received document ("stale UI") to
  ensure a seamless user experience.
- **Exponential Retry Logic:** The system renderer schedules reconnection
  retries in the background using an exponential back-off strategy (e.g.,
  starting at 5 seconds and scaling up to several minutes) until the connection
  is successfully re-established.
- **Provider Resource Disposal:** On the provider side, when the renderer
  unbinds or the connection is lost, the coroutine scope collecting the UI state
  flow is cancelled. This automatically disposes of active resources (like view
  models or observers), preventing the app from wasting CPU cycles on a UI that
  is no longer visible.

### Public Code References

To understand how session management and document streaming are orchestrated,
you can examine the following public source directories and files in the
official [Jetpack AndroidX GitHub Mirror](https://github.com/androidx/androidx):

- **[AppWidgetSession.kt](https://github.com/androidx/androidx/blob/androidx-main/glance/glance-appwidget/src/main/java/androidx/glance/appwidget/AppWidgetSession.kt):**
  Manages the lifecycle of the Compose session for a single app widget instance,
  handling update requests and resizing events.
- **[SessionWorker.kt](https://github.com/androidx/androidx/blob/androidx-main/glance/glance/src/main/java/androidx/glance/session/SessionWorker.kt):**
  The background `CoroutineWorker` that runs the Glance session, executing the
  composition and enforcing timeouts.
- **[remote-player-view](https://github.com/androidx/androidx/tree/androidx-main/compose/remote/remote-player-view):**
  The client-side player module that decodes the binary drawing instructions and
  plays them back natively on the Android canvas.
- **[remote-creation](https://github.com/androidx/androidx/tree/androidx-main/compose/remote/remote-creation):**
  The pure-JVM creation module used by provider hosts or servers to construct
  and serialize Remote Compose documents.

## Temporary Limitations (Bugs or Omissions)

These differences are gaps in the current API surface that may be resolved in
future releases.

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
