/*
 * Copyright 2026 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.example.wear_widget;

import android.graphics.Bitmap;

public class BugTrigger {
    /**
     * This method perfectly replicates the buggy logic found in
     * RemoteBitmapDecoder.java:66:
     * https://github.com/androidx/androidx/blob/eb3e9d199a4c5f4ef235ddac9ff66784d667c7be/compose/remote/remote-player-core/src/main/java/androidx/compose/remote/player/core/platform/RemoteBitmapDecoder.java#L66
     * 
     * Because this is compiled as Java, the compiler has no concept of
     * Kotlin's compile-time null safety. It will compile this line directly,
     * which immediately throws a NullPointerException at runtime if
     * image.getConfig() is null.
     */
    public static boolean isNotAlpha8(Bitmap image) {
        return !image.getConfig().equals(Bitmap.Config.ALPHA_8);
    }
}
