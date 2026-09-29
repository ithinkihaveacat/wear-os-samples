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
package com.google.example.wear_widget

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Usable sizes below are the preview param sizes from glance-wear-tooling-preview minus twice the
 * host padding.
 */
class DynamicLayoutTest {

    @Test
    fun roundSmall_showsButtonOnly() {
        val layout = dynamicLayout(usableWidthDp = 156f, usableHeightDp = 38f)
        assertEquals(DynamicTier.ButtonOnly, layout.tier)
        assertEquals(38f, layout.buttonHeightDp, 0.01f)
        assertTrue(layout.buttonLabelSp >= 14f)
    }

    @Test
    fun roundLarge_keepsAllLinesButDropsIconOnNarrowWidth() {
        val layout = dynamicLayout(usableWidthDp = 92f, usableHeightDp = 88f)
        assertEquals(DynamicTier.Full, layout.tier)
        assertFalse(layout.showIcon)
    }

    @Test
    fun fullScreen_usesMaximumSizes() {
        val layout = dynamicLayout(usableWidthDp = 180f, usableHeightDp = 180f)
        assertEquals(DynamicTier.Full, layout.tier)
        assertTrue(layout.showIcon)
        assertEquals(52f, layout.buttonHeightDp, 0.01f)
        assertEquals(18f, layout.summarySp, 0.01f)
    }

    @Test
    fun largeFontScale_dropsHeaderInsteadOfClipping() {
        assertEquals(DynamicTier.Full, dynamicLayout(92f, 80f, fontScale = 1f).tier)
        assertEquals(DynamicTier.Summary, dynamicLayout(92f, 80f, fontScale = 1.5f).tier)
    }

    @Test
    fun textAndButtonNeverGoBelowMinimum() {
        for (height in 40..200 step 2) {
            val layout = dynamicLayout(usableWidthDp = 150f, usableHeightDp = height.toFloat())
            assertTrue(layout.headerSp >= 12f)
            assertTrue(layout.summarySp >= 14f)
            assertTrue(layout.buttonLabelSp >= 14f)
            assertTrue(layout.buttonHeightDp >= 32f)
        }
    }

    @Test
    fun hostShorterThanButton_shrinksOnlyTheButton() {
        val layout = dynamicLayout(usableWidthDp = 150f, usableHeightDp = 28f)
        assertEquals(DynamicTier.ButtonOnly, layout.tier)
        assertEquals(28f, layout.buttonHeightDp, 0.01f)
        assertEquals(14f, layout.buttonLabelSp, 0.01f)
    }
}
