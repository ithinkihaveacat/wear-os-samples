# TODO

## Investigate rendering discrepancies between Compose Preview and Live Emulator (2026-07-21)

**Problem:** Side-by-side comparison between offline `@Preview` composable
renders (`compose-preview`) and live Wear OS 7 (`wear-api-37-canary-standalone`)
emulator screen captures reveals visual rendering discrepancies for several
canvas and complex layout samples. As documented in the Zipline Build report
([Build URL](https://serve-dot-zipline.googleplex.com/hosting/6e7e4265-d35e-4476-912a-c9b82bc7e966)):

- `CanvasReferenceSample2Preview` and `CanvasReferenceSample3Preview` exhibit
  distinct layout and drawing discrepancies when rendered in desktop/Robolectric
  preview host versus the native Wear OS ProtoLayout renderer.
- `CanvasReferenceSample5Preview` fails to render in the `compose-preview`
  environment, displaying a red error fallback state, whereas it renders
  correctly on the live emulator device.

**Goal:** Identify and resolve the underlying causes of rendering discrepancies
between `compose-preview` and live Wear OS device execution, ensuring canvas
operations and Remote Compose layout primitives render consistently across both
preview and device environments.

**Criteria:** The root causes for `CanvasReferenceSample2Preview`,
`CanvasReferenceSample3Preview`, and `CanvasReferenceSample5Preview`
discrepancies are documented, `CanvasReferenceSample5Preview` renders without
error in `compose-preview`, and canvas rendering output matches between preview
and device hosts.

**Sketch:** Investigate differences between `androidx.compose.remote` canvas
drawing command serialization in the Robolectric preview pipeline versus
`ProtoTilesTileRendererImpl` execution on Wear OS 7 (API 37).
`CanvasReferenceSample5Preview` likely throws an unsupported modifier or
unhandled drawing context exception during preview generation.
