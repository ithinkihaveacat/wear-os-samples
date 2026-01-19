# Wear Widget Component Catalog Plan

This document outlines the plan to implement a comprehensive catalog of Wear
Widget components, matching the existing ProtoLayout (Tiles) catalog where
applicable.

## Component Mapping

The following table maps existing ProtoLayout Material3 components (from
`WearTilesKotlin`) to their Wear Widget (Remote Compose) equivalents.

| ProtoLayout Component                | Wear Widget Component             | Status         | Notes                                                                   |
| :----------------------------------- | :-------------------------------- | :------------- | :---------------------------------------------------------------------- |
| `textButton`                         | `RemoteButton`                    | ✅ Implemented | Use `RemoteButton` with text content.                                   |
| `iconButton` | `RemoteButton` | ✅ Implemented | Use `RemoteButton` with `RemoteIcon` content. |
| `avatarButton`                       | `RemoteButton`                    | ⚠️ Issue     | Renders as black screen; issue tracked with TODO in source.            |
| `imageButton`                        | `RemoteButton`                    | ✅ Implemented | Use `RemoteBox` + `RemoteImage` as background.                          |
| `compactButton`                      | `RemoteButton`                    | ✅ Implemented | Use `RemoteButton` with `SmallIconSize`.                                |
| `button`                             | `RemoteButton`                    | ✅ Implemented | Standard `RemoteButton` usage.                                          |
| `iconEdgeButton` | N/A | ❌ Skipped | Edge buttons do not exist in Wear Widgets. |
| `textEdgeButton` | N/A | ❌ Skipped | Edge buttons do not exist in Wear Widgets. |
| `titleCard`                          | `RemoteCard`                      | ✅ Implemented | Use `RemoteCard` with title slots.                                      |
| `appCard`                            | `RemoteCard`                      | ✅ Implemented | Use `RemoteCard` with app-specific slots (time, avatar, etc.).          |
| `textDataCard`                       | `RemoteCard`                      | ⬜ Planned     | Use `RemoteCard` for data display.                                      |
| `iconDataCard`                       | `RemoteCard`                      | ⬜ Planned     | Use `RemoteCard` with icon slot.                                        |
| `graphicDataCard`                    | `RemoteCard`                      | ⬜ Planned     | Use `RemoteCard` with graphic slot.                                     |
| `circularProgressIndicator`          | `RemoteCard`                      | ✅ Implemented | Simulated using `RemoteCard` layout (icon + text) as `RemoteCircularProgressIndicator` is unavailable. |
| `segmentedCircularProgressIndicator` | `RemoteCard`                      | ✅ Implemented | Simulated using `RemoteCard` layout.                                    |
| `fullBleedImage`                     | `RemoteImage`                     | ✅ Implemented | Full-screen image with overlay text.                                    |
| `animatedBox`                        | `RemoteBox`                       | ✅ Implemented | Interactive box with property animations (color, size).                 |

## Additional Components

These are core Remote Compose components that should also be demonstrated, even
if they don't have a direct "Material Component" counterpart in the Tiles
catalog (which focuses on high-level M3 components).

- `RemoteText` (Basic text styling)
- `RemoteIcon` (Vector/Drawable rendering)
- `RemoteImage` (Bitmap rendering)
- `RemoteBox` (Layout primitives)
- `RemoteRow` (Layout primitives)
- `RemoteColumn` (Layout primitives)
- `RemoteCanvas` (Custom drawing)

## Implementation Plan

1. **Phase 1: Basic Inputs (Buttons)**
   - Implement `iconButton`, `avatarButton`, `imageButton`, `compactButton`.
   - Verify `EdgeButton` availability.

2. **Phase 2: Cards**
   - Implement `titleCard`, `appCard`.
   - Implement Data Cards (`textDataCard`, `iconDataCard`, `graphicDataCard`).

3. **Phase 3: Progress & Graphics**
   - Implement `circularProgressIndicator`.
   - Implement `segmentedCircularProgressIndicator`.
   - Add `RemoteCanvas` samples (already partially present in
     `WidgetCatalog.kt`).

4. **Phase 4: Documentation**
   - Generate screenshots for all new components.
   - Create a `README.md` in `screenshots/components/` similar to the Tiles
     project.
