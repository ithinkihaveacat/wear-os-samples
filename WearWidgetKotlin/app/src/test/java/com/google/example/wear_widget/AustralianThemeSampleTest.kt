package com.google.example.wear_widget

import androidx.compose.remote.creation.compose.layout.RemoteAlignment
import androidx.compose.remote.creation.compose.layout.RemoteArrangement
import androidx.compose.remote.creation.compose.layout.RemoteBox
import androidx.compose.remote.creation.compose.modifier.*
import androidx.compose.remote.creation.compose.state.rdp
import androidx.compose.remote.creation.profile.RcPlatformProfiles.WEAR_WIDGETS
import androidx.compose.remote.tooling.preview.RemotePreview
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.unit.dp
import com.github.takahirom.roborazzi.captureRoboImage
import com.google.example.wear_widget.widget.AustralianThemeSample
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import java.io.File

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [34], qualifiers = "w227dp-h227dp-small-notlong-round-watch-xhdpi-keyshidden-nonav")
class AustralianThemeSampleTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun takeScreenshot() {
        composeTestRule.setContent {
            RemotePreview(WEAR_WIDGETS) {
                RemoteBox(
                    modifier = RemoteModifier
                        .fillMaxSize()
                        .background(Color.Black),
                    horizontalAlignment = RemoteAlignment.CenterHorizontally,
                    verticalArrangement = RemoteArrangement.Center
                ) {
                    RemoteBox(
                        modifier = RemoteModifier
                            .size(170.rdp)
                            .background(Color.DarkGray),
                        horizontalAlignment = RemoteAlignment.CenterHorizontally,
                        verticalArrangement = RemoteArrangement.Center
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
