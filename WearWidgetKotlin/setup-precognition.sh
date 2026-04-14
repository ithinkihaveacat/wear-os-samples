#!/usr/bin/env bash
set -euo pipefail

# Supported/Tested commit hash for yschimke/precognition:
# 9a43a691e79349f536b86d8a3bc5062f2571b4d3

PRECOGNITION_DIR="${PRECOGNITION_DIR:-../../precognition}"

if [ ! -d "$PRECOGNITION_DIR" ]; then
  echo "Error: precognition directory not found at $PRECOGNITION_DIR" >&2
  echo "Please check out yschimke/precognition to that directory or set PRECOGNITION_DIR." >&2
  exit 1
fi

echo "Switching to $PRECOGNITION_DIR and preparing distribution..."
(
  cd "$PRECOGNITION_DIR"
  git checkout main
  ./gradlew :preview-cli:installDist
)

echo "Success! precognition integration initialized."
