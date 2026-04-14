#!/usr/bin/env bash
set -euo pipefail

# Supported/Tested fork commit hash for ithinkihaveacat/precognition:
# 639c01689e6f882ef70115ce76a313f0a886a988

PRECOGNITION_DIR="${PRECOGNITION_DIR:-../../precognition}"

if [ ! -d "$PRECOGNITION_DIR" ]; then
  echo "Error: precognition directory not found at $PRECOGNITION_DIR" >&2
  echo "Please check out ithinkihaveacat/precognition to that directory or set PRECOGNITION_DIR." >&2
  exit 1
fi

echo "Switching to $PRECOGNITION_DIR and preparing distribution..."
(
  cd "$PRECOGNITION_DIR"
  git checkout feature/precognition-screenshot-fixes
  ./gradlew :preview-cli:installDist
)

echo "Success! precognition integration initialized."
