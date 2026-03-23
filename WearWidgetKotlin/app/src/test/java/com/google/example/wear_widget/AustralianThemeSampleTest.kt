package com.google.example.wear_widget

import androidx.compose.remote.tooling.preview.RemotePreview
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
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
@Config(sdk = [33], qualifiers = "w227dp-h227dp-small-notlong-round-watch-xhdpi-keyshidden-nonav")
class AustralianThemeSampleTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun takeScreenshot() {
        composeTestRule.setContent {
            RemotePreview {
                AustralianThemeSample()
            }
        }

        composeTestRule.waitForIdle()

        // The Wear Widget renderer (protolayout) operates asynchronously and often needs a 
        // forced draw cycle in Robolectric to flush the rendered layout to the surface.
        // A dummy capture forces this full layout/draw pass.
        val rootNode = composeTestRule.onAllNodes(isRoot())[0]
        
        val dummyFile = java.io.File.createTempFile("dummy", ".png")
        rootNode.captureRoboImage(dummyFile.absolutePath)
        dummyFile.delete()

        // Capture the actual settled frame to the single desired output file
        rootNode.captureRoboImage("screenshots/AustralianThemeSample.png")
    }
}
