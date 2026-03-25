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
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import java.io.File
import com.google.example.wear_widget.widget.*

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [35], qualifiers = "w227dp-h227dp-small-notlong-round-watch-xhdpi-keyshidden-nonav")
class RoboScreenshotToolTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun takeScreenshot() {
        val target = System.getProperty("robo.screenshot.target")
            ?: throw IllegalArgumentException("Target composable must be specified via system property 'robo.screenshot.target'")

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
                            .background(Color.DarkGray)
                            .padding(10.dp),
                        horizontalAlignment = RemoteAlignment.CenterHorizontally,
                        verticalArrangement = RemoteArrangement.Center
                    ) {
                        when (target) {
                            // Widget Catalog
                            "TaskSample" -> TaskSample()
                            "SemanticStyleWorkaroundSample" -> SemanticStyleWorkaroundSample()
                            "CanvasSample3" -> CanvasSample3()
                            "SystemThemeComparisonSample" -> SystemThemeComparisonSample()
                            "SystemThemeSample" -> SystemThemeSample()
                            "CustomThemeSample" -> CustomThemeSample()
                            "AustralianThemeSample" -> AustralianThemeSample()
                            "BoxSample1" -> BoxSample1()
                            "BoxSample2" -> BoxSample2()
                            "BoxSample3" -> BoxSample3()
                            "TextSample1" -> TextSample1()
                            "TextSample1WithMargin" -> TextSample1WithMargin()
                            "RowSample1" -> RowSample1()
                            "RowSample2" -> RowSample2()
                            "CollapsibleColumnSample1" -> CollapsibleColumnSample1()
                            "ButtonSample1" -> ButtonSample1()
                            "ButtonSample2" -> ButtonSample2()
                            "ButtonSample3" -> ButtonSample3()
                            "ButtonSample4" -> ButtonSample4()
                            "ButtonSample6" -> ButtonSample6()
                            "ButtonSample7" -> ButtonSample7()
                            "ButtonSample8" -> ButtonSample8()
                            "ButtonSample9" -> ButtonSample9()
                            "IconSample1" -> IconSample1()
                            "GridSample1" -> GridSample1()
                            "CardSample1" -> CardSample1()
                            "CounterSample1" -> CounterSample1()
                            "Material3ThemeSample" -> Material3ThemeSample()
                            "CanvasSample1" -> CanvasSample1()
                            "CanvasSample2" -> CanvasSample2()
                            "GradientBackgroundSample" -> GradientBackgroundSample()
                            "PendingIntentSample" -> PendingIntentSample()
                            "VerticalScrollSample" -> VerticalScrollSample()
                            "MixedStyleSample" -> MixedStyleSample()
                            "ConditionalRadiusSample" -> ConditionalRadiusSample()
                            "TypographyScaleSample" -> TypographyScaleSample()
                            "AnchoredTextSample" -> AnchoredTextSample()
                            "RotatedTextSample" -> RotatedTextSample()
                            "FullBleedImageButtonSample" -> FullBleedImageButtonSample()
                            "TwoRemoteImagesWorkaround" -> TwoRemoteImagesWorkaround()
                            "TwoRemoteImagesBug" -> TwoRemoteImagesBug()
                            "BitmapCanvasSample" -> BitmapCanvasSample()
                            "DebugClickSample" -> DebugClickSample()
                            "BackgroundTreatmentsSample" -> BackgroundTreatmentsSample()
                            
                            // Component Catalog
                            "ComponentCatalogTextButtonSample", "textButton" -> ComponentCatalogTextButtonSample()
                            "ComponentCatalogIconButtonSample", "iconButton" -> ComponentCatalogIconButtonSample()
                            "ComponentCatalogAvatarButtonSample", "avatarButton" -> ComponentCatalogAvatarButtonSample()
                            "ComponentCatalogImageButtonSample", "imageButton" -> ComponentCatalogImageButtonSample()
                            "ComponentCatalogCompactButtonSample", "compactButton" -> ComponentCatalogCompactButtonSample()
                            "ComponentCatalogTitleCardSample", "titleCard" -> ComponentCatalogTitleCardSample()
                            "ComponentCatalogAppCardSample", "appCard" -> ComponentCatalogAppCardSample()
                            "ComponentCatalogTextDataCardSample", "textDataCard" -> ComponentCatalogTextDataCardSample()
                            "ComponentCatalogIconDataCardSample", "iconDataCard" -> ComponentCatalogIconDataCardSample()
                            "ComponentCatalogGraphicDataCardSample", "graphicDataCard" -> ComponentCatalogGraphicDataCardSample()
                            "ComponentCatalogCircularProgressIndicatorSample", "circularProgressIndicator" -> ComponentCatalogCircularProgressIndicatorSample()
                            "ComponentCatalogSegmentedCircularProgressIndicatorSample", "segmentedCircularProgressIndicator" -> ComponentCatalogSegmentedCircularProgressIndicatorSample()
                            "ComponentCatalogFullBleedImageSample", "fullBleedImage" -> ComponentCatalogFullBleedImageSample()
                            "ComponentCatalogAnimatedBoxSample", "animatedBox" -> ComponentCatalogAnimatedBoxSample()

                            else -> throw IllegalArgumentException("Unknown composable name: \$target")
                        }
                    }
                }
            }
        }

        composeTestRule.waitForIdle()

        val rootNode = composeTestRule.onAllNodes(isRoot())[0]
        
        val dummyFile = File.createTempFile("dummy", ".png")
        rootNode.captureRoboImage(dummyFile.absolutePath)
        dummyFile.delete()

        rootNode.captureRoboImage("screenshots/${target}.png")
    }
}
