# Gradual widget rollout experiment

This branch adds a test harness for a runtime experimentation approach to
migrating from Tiles to Widgets. The approach is shipping a widget
service disabled, then turning it on at runtime with
`PackageManager.setComponentEnabledSetting()` for users enrolled in an
experiment.

The harness has two independent experiments. Each has its own legacy
ProtoLayout Tile, so you can test both at the same time.

| Experiment | Legacy Tile (always on)   | Rollout service (shipped `enabled="false"`)                          |
| ---------- | ------------------------- | -------------------------------------------------------------------- |
| Dual       | `DualLegacyTileService`   | `DualWidgetService`: `BIND_WIDGET_PROVIDER` only                     |
| Single     | `SingleLegacyTileService` | `SingleTempWidgetService`: both actions, `priority="10"`, SDK 37+ only |

In each experiment, the rollout widget's `group` is set to the fully qualified
class name of its legacy Tile. Each surface shows a different label, so you can
see which component the system bound to:

- `Dual: LEGACY TILE` / `Single: LEGACY TILE` (orange ProtoLayout text)
- `Dual: NEW WIDGET` / `Single: TEMP WIDGET` (Remote Compose widget)

Code: [`rollout/Rollout.kt`](app/src/main/java/com/google/example/wear_widget/rollout/Rollout.kt).

## Toggling

`RolloutReceiver` stands in for an experiment-enrollment callback. It calls
`setComponentEnabledSetting(..., DONT_KILL_APP)` from inside the app, which is
the same code path production would use. Every call also logs the current
component state under the `WidgetRollout` tag.

```bash
PKG=com.google.example.wear_widget
R="-n $PKG/.rollout.RolloutReceiver -a $PKG.ROLLOUT"

adb shell am broadcast $R                                            # dump state
adb shell am broadcast $R --es experiment dual   --es state enabled   # enroll
adb shell am broadcast $R --es experiment dual   --es state disabled  # rollback
adb shell am broadcast $R --es experiment dual   --es state default   # reset to manifest
adb shell am broadcast $R --es experiment single --es state enabled   # refused on SDK < 37
adb shell am broadcast $R --es experiment single --es state enabled --ez force true

adb logcat -s WidgetRollout
```

## Test matrix

| Device                          | Role                                   | Expected (per guide)                                         |
| ------------------------------- | -------------------------------------- | ------------------------------------------------------------ |
| Galaxy Watch (SM-L340, Wear 7)  | Wear OS 7, widget-capable              | Dual/Single: widget replaces the Tile, existing instance kept |
| Pixel Watch 3 / 4 (Wear 7)      | Wear OS 7, no widgets                  | Dual: legacy Tile only. Single: temp service in compat mode   |
| `wear-api-37` emulator          | Wear OS 7, no widgets                      | Same as Pixel                                              |
| `Wear_36_*` emulators           | Before Wear OS 7                       | Dual: legacy Tile only. Single: never enabled (guard)         |

## Scenarios to check on each device

1. Install the app and add both legacy Tiles to the carousel.
1. Enroll in the experiment, then check whether the Tile or widget is replaced
   in place, whether it moves, and whether a duplicate appears.
1. Check the Tile/widget picker for duplicate entries.
1. Roll back (`disabled`), then check that the legacy Tile comes back in the
   same slot with no ghost entry.
1. Reboot, then reinstall with `adb install -r`, and check that the enabled
   state persists. `setComponentEnabledSetting` state is expected to survive
   updates.
1. Single experiment only: enable with `force` on SDK < 37 to reproduce the
   "both tiles available" failure mode the guide warns about.
