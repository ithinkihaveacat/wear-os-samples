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

import androidx.compose.remote.creation.compose.layout.RemoteAlignment
import androidx.compose.remote.creation.compose.layout.RemoteBox
import androidx.compose.remote.creation.compose.modifier.*
import androidx.compose.remote.creation.compose.state.rdp
import androidx.compose.remote.creation.profile.RcPlatformProfiles.WEAR_WIDGETS
import androidx.compose.remote.tooling.preview.RemotePreview
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.github.takahirom.roborazzi.captureRoboImage
import com.google.example.wear_widget.widget.AustralianThemeSample
import java.io.File
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [35], qualifiers = "w227dp-h227dp-small-notlong-round-watch-xhdpi-keyshidden-nonav")
class AustralianThemeSampleTest {

    @get:Rule val composeTestRule = createComposeRule()

    @Test
    fun takeScreenshot() {
        composeTestRule.setContent {
            RemotePreview(WEAR_WIDGETS) {
                RemoteBox(
                    modifier = RemoteModifier.fillMaxSize().background(Color.Black),
                    contentAlignment = RemoteAlignment.Center,
                ) {
                    RemoteBox(
                        modifier = RemoteModifier.size(170.rdp).background(Color.DarkGray),
                        contentAlignment = RemoteAlignment.Center,
                    ) {
                        AustralianThemeSample()
                    }
                }
            }
        }

        composeTestRule.waitForIdle()

        val rootNode = composeTestRule.onAllNodes(isRoot())[0]

        val dummyFile = File.createTempFile("dummy", ".png")
        rootNode.captureRoboImage(dummyFile.absolutePath)
        dummyFile.delete()

        rootNode.captureRoboImage("screenshots/AustralianThemeSample.png")
    }
}
