# Helper Scripts

This directory contains utility scripts for maintaining the Widget Samples and
documentation.

## Scripts

### `update-screenshots.sh`

Updates the screenshot artifacts stored in `screenshots/` by automating the
widget switching, capturing, and comparison process.

**Purpose:**

- Ensures screenshots match the current code.
- Detects visual regressions or changes using AI comparison.
- Automates the tedious process of `switch -> capture -> rename`.

**Usage:**

```bash
# Update ALL screenshots (iterates through existing files in screenshots/)
./scripts/update-screenshots.sh

# Update specific layouts only
./scripts/update-screenshots.sh BoxSample1 ButtonSample2
```

**Dependencies:**

- `widget-switch` (in project root)
- `adb` (via `.gemini/skills/adb`)
- `screenshot-compare` (via `.gemini/skills/ai-analysis` - requires
  `GEMINI_API_KEY`)
- Connected Android device/emulator with the app installed and tile added.

**Safety:**

- **Destructive:** Overwrites existing image files in `screenshots/`.
- **Safe to run:** Can be run anytime provided a device is connected. It will
  report changes but assumes the new capture is the "correct" state to save.

---

### `generate-samples.py`

Generates the `screenshots/SAMPLES.md` catalog file by parsing the Kotlin source
code.

**Purpose:**

- Creates a unified Markdown catalog combining Screenshot + KDoc Description +
  Code Snippet.
- Ensures documentation (`SAMPLES.md`) is always in sync with
  `WidgetCatalog.kt`.

**Usage:**

```bash
python3 scripts/generate-samples.py
```

**Logic:**

- Reads `app/src/main/java/com/google/example/wear_widget/WidgetCatalog.kt`.
- Extracts functions annotated with `@RemoteComposable`.
- Parses the KDoc (for description) and the function body (for code snippet).
- Outputs formatted Markdown to `screenshots/SAMPLES.md`.

**Dependencies:**

- Python 3.

**Safety:**

- **Destructive:** Overwrites `screenshots/SAMPLES.md`.
- **Safe to run:** Anytime. Does not require a device.

## Recommended Workflow

When making changes to widget samples:

1. **Modify Code:** Update `WidgetCatalog.kt`.
2. **Verify & Capture:** Run `./scripts/update-screenshots.sh [ChangedSample]`
   to verify rendering and update the image artifact.
3. **Check Diffs:** Review the script output ("CHANGE DETECTED") and visual
   diffs to ensure correctness.
4. **Update Docs:** Run `python3 scripts/generate-samples.py` to update
   `SAMPLES.md` with the new code/description.
5. **Commit:** Commit changes to code, images, and documentation together.
