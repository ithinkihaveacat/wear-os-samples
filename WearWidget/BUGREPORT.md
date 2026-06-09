# REGRESSION: Remote Compose alpha12 Port Introduces Layoutlib NPE and Raw-Color Corruption

This document provides a factual bug report documenting two regressions introduced during the manual Kotlin-to-Java refactoring of the `remote-compose` library in version `1.0.0-alpha12` (core player) and `1.0.0-alpha05` (material3). 

Both features functioned correctly in the Kotlin-based `1.0.0-alpha10` release but were broken during the Java migration. A complete, programmatic reproduction test suite utilizing Robolectric shadows has been committed to the project's test source set to demonstrate both failures.

---

## 1. Regression A: Layoutlib NullPointerException Crash

### Description
During the manual translation of `ComposeRemoteContext.kt` into the Java class `RemoteBitmapDecoder.java`, a null-safe Kotlin inequality check was translated into an unsafe Java method invocation. 

*   **In Kotlin (`alpha10`):** `image.getConfig() != Config.ALPHA_8` compiled to a null-safe compiler-generated comparison (`!Intrinsics.areEqual(image.getConfig(), ALPHA_8)`). If `image.getConfig()` returned `null`, it safely evaluated to `false` (negated to `true`) without throwing an exception.
*   **In Java (`alpha12`):** `!image.getConfig().equals(Config.ALPHA_8)` is invoked directly on the returned configuration reference. 
*   **The Trigger:** Under Android Studio's layout preview engine (Layoutlib), image decoding is delegated to the host JVM's Java 2D graphics framework (`BufferedImage`). Because Java 2D color models do not cleanly map to Android's `Bitmap.Config` enums, Layoutlib's `Bitmap` delegate frequently returns `null` for `Bitmap.getConfig()`. 
*   **Symptom:** Invoking `.equals()` on this null reference immediately throws a `NullPointerException` at runtime. The player's playback loop catches this crash and silently skips rendering the affected image asset.

### Triggering Source Code Link
The regression is located at line 66 of `RemoteBitmapDecoder.java` in the internal Googleplex AndroidX source repository:
🔗 [RemoteBitmapDecoder.java:66](https://source.corp.google.com/h/googleplex-android/platform/frameworks/support/+/androidx-platform-dev:compose/remote/remote-player-core/src/main/java/androidx/compose/remote/player/core/platform/RemoteBitmapDecoder.java;l=66)

```java
// Unsafe line in RemoteBitmapDecoder.java
if (!image.getConfig().equals(Bitmap.Config.ALPHA_8)) { // <-- Crashes here if getConfig() is null
```

### Impact
*   **Developer Experience:** Previews utilizing `ALPHA_8` assets fail to render in the Android Studio Layout Editor. Previews appear blank or missing key UI elements, affecting layout design velocity. This affects any Remote Compose document utilizing `ALPHA_8` bitmap assets—including all `RemoteIcon` components (which automatically serialize vector drawables into `ALPHA_8` alpha masks at serialization time) and many `RemoteAppCard` assets.
*   **Scope:** Affects all developers using Android Studio Layout Editor Previews compiled against `1.0.0-alpha12` or higher.

---

## 2. Regression B: Raw-Color Sign-Extension Corruption (`TYPE_RAW8888`)

### Description
The Java port in `RemoteBitmapDecoder.java` handles raw ARGB byte-to-integer decoding by bitwise shifting signed Java bytes directly without applying the `& 0xFF` masking operation. 

*   **Symptom:** When a color channel value is `128` or higher (which represents a negative byte in Java's signed 8-bit byte model), the bitwise shift operator sign-extends the negative byte, padding the upper bits with `1`s (e.g., `128` -> `0x80` -> `0xFFFFFF80`). 
*   **The Trigger:** During the subsequent bitwise OR operations to assemble the 32-bit pixel integer, this sign-extended `0xFF` bleeds into all other color channels, corrupting the pixel color to washed-out white/gray.

### Triggering Source Code Link
The regression is located at line 124 of `RemoteBitmapDecoder.java` in the internal Googleplex AndroidX source repository:
🔗 [RemoteBitmapDecoder.java:124](https://source.corp.google.com/h/googleplex-android/platform/frameworks/support/+/androidx-platform-dev:compose/remote/remote-player-core/src/main/java/androidx/compose/remote/player/core/platform/RemoteBitmapDecoder.java;l=124)

```java
// Unsafe bitwise shifts in RemoteBitmapDecoder.java
idata[i] = (data[p] << 24) | (data[p + 1] << 16) | (data[p + 2] << 8) | data[p + 3]; // <-- Sign-extends negative bytes
```

### Impact
*   **User Experience:** On-device rendering of `TYPE_RAW8888` assets with color channel magnitudes >= 128 results in incorrect pixel mapping, appearing as light-gray or white. This affects the visual rendering of the application (such as rendering dark blue `0xFF000080` as washed-out gray).

---

## 3. Environment
*   **Engine/Framework:** Android Studio Layoutlib (Canary/Quail 2026.1.2) & Robolectric JVM Test Runner.
*   **API Level:** API 34 (Wear OS 4).
*   **Libraries:** 
    *   `androidx.compose.remote:remote-player-core:1.0.0-alpha12` (broken)
    *   `androidx.wear.compose.remote:remote-material3:1.0.0-alpha05` (broken)
    *   *Works in:* `remote-core:1.0.0-alpha10` (Kotlin-based).

---

## 4. Reproduction Steps

A complete, programmatic reproduction test suite utilizing Robolectric shadows has been added to the project's test source set. Persistent GitHub links to the source files on our pushed branch are provided below:
*   [**`BugReproductionTest.kt`**](https://github.com/ithinkihaveacat/wear-os-samples/blob/de41a03986a0aa9e4f65644134db861099aa109c/WearWidget/app/src/test/java/com/google/example/wear_widget/BugReproductionTest.kt) (Kotlin Test Suite)
*   [**`BugTrigger.java`**](https://github.com/ithinkihaveacat/wear-os-samples/blob/de41a03986a0aa9e4f65644134db861099aa109c/WearWidget/app/src/test/java/com/google/example/wear_widget/BugTrigger.java) (Java Interop Shim)

### Execution Command
Execute the unified reproduction suite from the terminal:
```bash
./gradlew :app:testDebugUnitTest --tests "com.google.example.wear_widget.BugReproductionTest"
```

### Expected Behavior
If the library were functioning correctly:
*   `testAlpha8NpeBug` completes successfully, executing `BugTrigger.isNotAlpha8` without throwing a `NullPointerException`.
*   `testRaw8888ColorCorruptionBug` completes successfully, decoding the raw bytes `[255, 0, 0, 128]` to the uncorrupted dark blue pixel `0xFF000080`.
*   **Result:** `2 tests completed, 0 failed`.

### Actual Behavior
Due to the regressions in `alpha12`:
*   `testAlpha8NpeBug` crashes immediately on the Java shim, throwing a native `java.lang.NullPointerException` in the console.
*   `testRaw8888ColorCorruptionBug` fails with an `AssertionError`, printing the corrupted pixel color `FFFFFF80` instead of `FF000080`.
*   **Result:** `2 tests completed, 2 failed`.

---

## 5. Error Log

When triggering Regression A, the following exception stack trace is caught by the JVM during the test run:

```text
java.lang.NullPointerException: Cannot invoke "android.graphics.Bitmap$Config.equals(Object)" because the return value of "android.graphics.Bitmap.getConfig()" is null
    at androidx.compose.remote.player.core.platform.RemoteBitmapDecoder.decodeBitmap(RemoteBitmapDecoder.java:66)
    at com.google.example.wear_widget.BugTrigger.isNotAlpha8(BugTrigger.java:29)
    at com.google.example.wear_widget.BugReproductionTest$testAlpha8NpeBug$exception$1.invoke(BugReproductionTest.kt:155)
    at com.google.example.wear_widget.BugReproductionTest$testAlpha8NpeBug$exception$1.invoke(BugReproductionTest.kt:154)
    at org.junit.Assert.assertThrows(Assert.java:1001)
    at org.junit.Assert.assertThrows(Assert.java:981)
    at com.google.example.wear_widget.BugReproductionTest.testAlpha8NpeBug(BugReproductionTest.kt:154)
```

---

## 6. Technical Analysis & Recommended Fixes

### Fix A: Resolving the Layoutlib NPE
The crash occurs because the Java `.equals()` method is invoked directly on a potentially null reference returned by `image.getConfig()`. 

**Recommended Solution:**
Align the Java code with Kotlin's null-safe contract by making the comparison null-safe. Replace line 66 of `RemoteBitmapDecoder.java` with a null check or an identity comparison:

```java
// Safe Java replacement in RemoteBitmapDecoder.java
Bitmap.Config config = image.getConfig();
if (config == null || config != Bitmap.Config.ALPHA_8) {
    // Proceed with conversion...
}
```
*Because `Bitmap.Config` is an Enum, a null-safe identity comparison (`!=` or `==`) is fully sufficient and completely avoids the `NullPointerException`.*

### Fix B: Resolving the Raw-Color Sign-Extension
The corruption occurs because Java's `byte` type is signed, and bitwise shifting a negative byte promotes it to a signed 32-bit integer, padding all upper bits with `1`s.

**Recommended Solution:**
Apply a bitwise AND mask (`& 0xFF`) to each byte channel before shifting. This zero-extends the byte into a clean, unsigned integer representation, preventing sign-extension bleed. Replace line 124 of `RemoteBitmapDecoder.java` with:

```java
// Safe bitwise shifts in RemoteBitmapDecoder.java
idata[i] = ((data[p] & 0xFF) << 24) | 
           ((data[p + 1] & 0xFF) << 16) | 
           ((data[p + 2] & 0xFF) << 8) | 
           (data[p + 3] & 0xFF);
```
