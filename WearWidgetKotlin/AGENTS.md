# Agent Instructions

This document contains specific instructions for agents operating within this
codebase.

## Wear OS Device & Emulator Setup (Renderer Installation)

**Important:** These instructions only apply if the target device is a Wear OS
device.

**Important:** In the instructions below, `RENDERER_DIR` refers to the directory
containing the necessary renderer APKs. The exact location of this directory
will be provided to you separately (e.g., via environment variable, context, or
specific instructions).

**CRITICAL:** Whether you are using a newly created emulator, a reset emulator,
or a physical Wear OS device, the system image may **NOT** have the required
version of the `com.google.android.wearable.protolayout.renderer` package.

You **MUST** verify and, if necessary, sideload the updated renderer manually.
Failure to do so will result in blank widgets or rendering errors.

### 1. Verify Device Type and Identify Architecture/Build Type

Before installation, verify that the device is a Wear OS device and identify its
architecture and build type.

- **Device Type (Must contain "wear"):**

  ```bash
  adb shell getprop ro.build.characteristics
  ```

- **Architecture (ABI):**

  ```bash
  adb shell getprop ro.product.cpu.abi
  ```

  _(Commonly `arm64-v8a` for physical hardware and M-series Macs, or `x86_64`
  for Intel/AMD emulators)_

- **Build Type:**

  ```bash
  adb shell getprop ro.build.type
  ```

  _(Used to select between `releasekey` for **user** builds and `testkey` for
  **userdebug** builds)_

### 2. Locate and Install the Renderer

The renderer APKs are stored in `RENDERER_DIR`. Select the APK that matches both
the ABI and the Build Type identified in Step 1.

Sideload the APK using `adb install`. Use the `-g` (grant permissions), `-t`
(allow test packages), and `-r` (replace existing) flags.

```bash
# Example: arm64-v8a (User Build -> releasekey)
adb install -g -t -r $RENDERER_DIR/renderer_..._arm64-v8a_releasekey_...apk
```

### 3. Restart System UI

After installation, you **MUST** restart the Wear OS System UI for the changes
to take effect.

```bash
adb shell am force-stop com.google.android.wearable.sysui
```

### 4. Verify Installation

Verify that the installed version matches the requirement (Version
**1.5.7.54.855545428** or later).

```bash
adb shell dumpsys package com.google.android.wearable.protolayout.renderer \
  | grep versionName
```

---

## Summary Checklist for Device Setup

1. [ ] Verify Wear OS: `adb shell getprop ro.build.characteristics` (must
       contain `wear`)
2. [ ] Check ABI & Build Type: `adb shell getprop ro.product.cpu.abi` and
       `ro.build.type`
3. [ ] Install Renderer: `adb install -g -t -r $RENDERER_DIR/<matching_apk>`
4. [ ] Restart SysUI:
       `adb shell am force-stop com.google.android.wearable.sysui`
5. [ ] Verify Version:
       `adb shell dumpsys package com.google.android.wearable.protolayout.renderer`
       `| grep versionName`
