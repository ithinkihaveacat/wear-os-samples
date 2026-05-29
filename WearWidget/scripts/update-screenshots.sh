#!/bin/bash
# scripts/update-screenshots.sh
# Updates screenshots in screenshots/ directory by running widget-switch and adb-screenshot.
# Compares with existing to detect changes.

set -u

# Ensure we are in project root
if [ ! -f "widget-switch" ]; then
    echo "Error: Run from project root."
    exit 1
fi

require() {
  if ! hash "$1" 2>/dev/null; then
    echo "Error: '$1' not found in PATH."
    echo "Please ensure the relevant agent skill (adb or ai-tools) is installed and its scripts are in your PATH."
    exit 127
  fi
}

require adb-screenshot
require screenshot-compare

mkdir -p screenshots/temp

# Extract valid layouts from widget-switch source
VALID_LAYOUTS_STR=$(grep "VALID_LAYOUTS=" widget-switch | cut -d'"' -f2)

LAYOUTS_TO_PROCESS=""

if [ $# -gt 0 ]; then
    # Use provided arguments
    LAYOUTS_TO_PROCESS="$@"
else
    # Process all existing screenshots that map to valid layouts
    for FILE in screenshots/*.png; do
        LAYOUT=$(basename "$FILE" .png)
        if [[ " $VALID_LAYOUTS_STR " =~ " $LAYOUT " ]]; then
            LAYOUTS_TO_PROCESS="$LAYOUTS_TO_PROCESS $LAYOUT"
        fi
    done
fi

echo "Starting screenshot update..."

CHANGED_FILES=""

for LAYOUT in $LAYOUTS_TO_PROCESS; do
    # Check validity again just in case
    if [[ ! " $VALID_LAYOUTS_STR " =~ " $LAYOUT " ]]; then
        echo "Skipping $LAYOUT (invalid layout)"
        continue
    fi

    FILE="screenshots/$LAYOUT.png"
    echo "========================================"
    echo "Processing $LAYOUT..."
    
    if ! ./widget-switch "$LAYOUT"; then
        echo "Failed to switch to $LAYOUT. Skipping."
        continue
    fi
    
    TEMP_FILE="screenshots/temp/$LAYOUT.png"
    adb-screenshot "$TEMP_FILE"
    
    if [ -f "$FILE" ]; then
        # Compare
        set +e
        screenshot-compare "$FILE" "$TEMP_FILE" "Check for visual differences"
        RET=$?
        set -e
        
        if [ $RET -eq 0 ]; then
            echo ">>> CHANGE DETECTED in $LAYOUT <<<"
            CHANGED_FILES="$CHANGED_FILES $LAYOUT"
        elif [ $RET -eq 2 ]; then
            echo "No change in $LAYOUT."
        else
            echo "Comparison failed/error for $LAYOUT (Exit $RET)."
        fi
    else
        echo "New screenshot captured (no previous file)."
        CHANGED_FILES="$CHANGED_FILES $LAYOUT"
    fi
    
    # Update/Create
    mv "$TEMP_FILE" "$FILE"
done

echo "========================================"
if [ -n "$CHANGED_FILES" ]; then
    echo "Summary: The following screenshots have changed:"
    for f in $CHANGED_FILES; do echo "  - $f"; done
else
    echo "Summary: No screenshots changed."
fi
rm -rf screenshots/temp
