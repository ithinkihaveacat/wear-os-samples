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
@file:SuppressLint("RestrictedApi")

package com.google.example.wear_widget

import android.annotation.SuppressLint
import android.graphics.Bitmap
import androidx.compose.remote.core.operations.BitmapData
import androidx.compose.remote.player.core.platform.BitmapLoader
import androidx.compose.remote.player.core.platform.RemoteBitmapDecoder
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config
import java.io.InputStream

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class BugReproductionTest {

    /**
     * Test Case 1: Programmatically reproduces the raw-color sign-extension corruption bug.
     *
     * In alpha12, the Java port in [RemoteBitmapDecoder] bitwise shifts signed bytes directly
     * without applying the 0xFF mask. When a channel value is 128 or higher (negative byte),
     * it sign-extends to 0xFFFFFFXX and bleeds 0xFF into all other color channels during the
     * bitwise OR, permanently corrupting dark colors to white/light-gray.
     */
    @Test
    fun testRaw8888ColorCorruptionBug() {
        // Construct a 1x1 RAW8888 image representing a pixel with:
        // Alpha = 255 (0xFF)
        // Red = 0 (0x00)
        // Green = 0 (0x00)
        // Blue = 128 (0x80) -> This is the critical sign byte (128 is negative in signed bytes)
        val rawData = byteArrayOf(
            255.toByte(), // Alpha
            0.toByte(),   // Red
            0.toByte(),   // Green
            128.toByte()  // Blue (negative byte value -128)
        )

        val mockLoader = object : BitmapLoader {
            override fun loadBitmap(path: String): InputStream {
                throw UnsupportedOperationException("Not needed for inline encoding")
            }
        }

        // Invoke the decoder directly (mimicking the player's execution)
        val decodedBitmap = RemoteBitmapDecoder.decodeBitmap(
            /* imageId = */ 1,
            /* encoding = */ BitmapData.ENCODING_INLINE,
            /* type = */ BitmapData.TYPE_RAW8888,
            /* width = */ 1,
            /* height = */ 1,
            /* data = */ rawData,
            /* bitmapLoader = */ mockLoader
        )

        // Ensure the decoded bitmap is not null
        assertNotNull("Decoded bitmap should not be null", decodedBitmap)

        // Retrieve the decoded pixel color
        val pixelColor = decodedBitmap!!.getPixel(0, 0)

        // Expected pixel color:
        // Alpha: 0xFF (255)
        // Red:   0x00 (0)
        // Green: 0x00 (0)
        // Blue:  0x80 (128)
        // Correct integer representation: 0xFF000080
        //
        // Due to the sign-extension bug in Java:
        // idata[i] = (data[p] << 24) | (data[p + 1] << 16) | (data[p + 2] << 8) | data[p + 3];
        // Blue byte (128 -> -128) sign-extends to 0xFFFFFF80.
        // Bitwise ORing with the rest promotes the entire integer to 0xFFFFFF80 (A=255, R=255, G=255, B=128).
        // This will bleed 255 into Red and Green, washing it out!
        
        val expectedColor = 0xFF000080.toInt()
        
        println("--- COLOR CORRUPTION BUG REPRODUCTION OUTPUT ---")
        println("Decoded Pixel Color (Hex): " + Integer.toHexString(pixelColor).uppercase())
        println("-------------------------------------------------")
        
        assertEquals(
            "Color was corrupted due to sign-extension bug!",
            expectedColor,
            pixelColor
        )
    }

    /**
     * Test Case 2: Programmatically reproduces the Layoutlib NullPointerException crash.
     *
     * In alpha10 (Kotlin), `image.getConfig() != Config.ALPHA_8` compiled to a null-safe
     * check. If Layoutlib returned null, it safely evaluated to `true`.
     *
     * In alpha12 (Java), `!image.getConfig().equals(Config.ALPHA_8)` is invoked directly
     * on the returned configuration. When Layoutlib returns null, it immediately throws a
     * [NullPointerException] and crashes Layoutlib rendering.
     */
    @Test
    fun testAlpha8NpeBug() {
        // Create a real 1x1 ALPHA_8 Bitmap
        val bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ALPHA_8)
        
        // Retrieve Robolectric's shadow instance of the bitmap
        val shadow = shadowOf(bitmap)
        
        // Use reflection on the plain Java Shadow class to set the private "config" field to null.
        // This bypasses Robolectric's bytecode shadowing and successfully nullifies the config!
        val configField = shadow.javaClass.getDeclaredField("config")
        configField.isAccessible = true
        configField.set(shadow, null)
        
        // Assert that getConfig() now returns null
        assertNull("Bitmap config should now be null (simulating Layoutlib)", bitmap.config)
        
        // 1. Show that the original Kotlin null-safe comparison is 100% safe
        // (evaluates to true safely, without throwing an exception)
        val kotlinSafeResult = bitmap.config != Bitmap.Config.ALPHA_8
        assertTrue("Kotlin safe comparison should succeed", kotlinSafeResult)
        
        // 2. Show that the manually ported Java line in alpha12 immediately throws an NPE!
        // We call BugTrigger (Java) which runs the exact buggy line:
        // !image.getConfig().equals(Bitmap.Config.ALPHA_8)
        //
        // Because the bug is present, this will throw a NullPointerException,
        // causing this test to FAIL in the console. When the bug is fixed,
        // this call will be safe, allowing the test to reach the sentinel assertion below.
        println("--- NULL CONFIG NPE BUG REPRODUCTION OUTPUT ---")
        println("Successfully simulated Layoutlib null config using Robolectric Shadows!")
        println("Executing buggy Java line (expecting NullPointerException)...")
        println("------------------------------------------------")
        
        BugTrigger.isNotAlpha8(bitmap)
        
        // Sentinel assertion to explicitly show the test reached the end successfully when fixed
        assertTrue("BugTrigger executed without throwing a NullPointerException", true)
    }
}
