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
@file:android.annotation.SuppressLint("RestrictedApi")

package com.google.example.wear_widget

import androidx.compose.remote.core.CoreDocument
import androidx.compose.remote.core.operations.TextData
import androidx.compose.remote.core.operations.TextFromFloat
import androidx.compose.remote.core.operations.layout.Component
import androidx.compose.remote.creation.compose.capture.RemoteCreationDisplayInfo
import androidx.compose.remote.creation.profile.RcPlatformProfiles
import androidx.compose.remote.player.core.platform.AndroidRemoteContext
import androidx.compose.remote.testing.RemoteContentTestRule
import com.google.example.wear_widget.widget.CounterSample1
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [33], qualifiers = "w227dp-h227dp-small-notlong-round-watch-xhdpi-keyshidden-nonav")
class CounterSample1Test {

    @get:Rule val remoteRule = RemoteContentTestRule()

    @Test
    fun testCounterSample1_incrementAndDecrement() {
        val displayInfo = RemoteCreationDisplayInfo(454, 454, 320, 1f)
        var capturedDoc: CoreDocument? = null
        remoteRule.setContent(
            remoteCreationDisplayInfo = displayInfo,
            profile = RcPlatformProfiles.WEAR_WIDGETS,
            onCreate = { doc -> capturedDoc = doc },
        ) {
            CounterSample1()
        }
        val doc = checkNotNull(capturedDoc) { "Failed to capture CoreDocument" }
        val context = AndroidRemoteContext()
        doc.initializeContext(context)
        doc.applyDataOperations(context)

        val allComponents = mutableListOf<Component>()
        fun collect(comp: Component) {
            allComponents.add(comp)
            for (op in comp.list) {
                if (op is Component) {
                    collect(op)
                }
            }
        }
        collect(checkNotNull(doc.rootLayoutComponent))

        fun findButton(symbol: String): Component? {
            return allComponents.find { comp ->
                comp.list.any { op -> op is TextData && op.mText == symbol }
            }
        }

        val plusButton = checkNotNull(findButton("+")) { "Could not find plus button" }
        val minusButton = checkNotNull(findButton("-")) { "Could not find minus button" }

        org.junit.Assert.assertTrue(
            "Plus button must declare ValueIntegerExpressionChangeActionOperation",
            plusButton.deepToString("").contains("ValueIntegerExpressionChangeActionOperation"),
        )
        org.junit.Assert.assertTrue(
            "Minus button must declare ValueIntegerExpressionChangeActionOperation",
            minusButton.deepToString("").contains("ValueIntegerExpressionChangeActionOperation"),
        )

        // Find TextFromFloat operation displaying the counter
        var textFromFloatOp: TextFromFloat? = null
        for (comp in allComponents) {
            for (op in comp.list) {
                if (op is TextFromFloat) {
                    textFromFloatOp = op
                    break
                }
            }
        }
        val textOp = checkNotNull(textFromFloatOp) { "Could not find TextFromFloat operation" }
        val textId = textOp.mTextId

        // Verify initial counter display
        assertEquals("Initial counter value should be 0", "0", context.getText(textId))

        // Click plus: 0 -> 1
        plusButton.onClick(context, doc, -1f, -1f)
        doc.applyDataOperations(context)
        assertEquals("Counter after plus click should be 1", "1", context.getText(textId))

        // Click plus again: 1 -> 2
        plusButton.onClick(context, doc, -1f, -1f)
        doc.applyDataOperations(context)
        assertEquals("Counter after second plus click should be 2", "2", context.getText(textId))

        // Click minus: 2 -> 1
        minusButton.onClick(context, doc, -1f, -1f)
        doc.applyDataOperations(context)
        assertEquals("Counter after minus click should be 1", "1", context.getText(textId))

        // Click minus again: 1 -> 0
        minusButton.onClick(context, doc, -1f, -1f)
        doc.applyDataOperations(context)
        assertEquals("Counter after second minus click should be 0", "0", context.getText(textId))

        // Click minus to negative: 0 -> -1
        minusButton.onClick(context, doc, -1f, -1f)
        doc.applyDataOperations(context)
        assertEquals("Counter after third minus click should be -1", "-1", context.getText(textId))
    }
}
