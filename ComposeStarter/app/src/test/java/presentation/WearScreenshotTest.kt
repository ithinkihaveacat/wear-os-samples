package presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.unit.Density
import com.github.takahirom.roborazzi.ExperimentalRoborazziApi
import com.github.takahirom.roborazzi.RoborazziOptions
import com.github.takahirom.roborazzi.captureScreenRoboImage
import org.junit.Rule
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [33])
abstract class WearScreenshotTest {

    @get:Rule
    val composeRule: ComposeContentTestRule = createComposeRule()

    abstract val device: WearDevice
    open val tolerance: Float = 0.02f

    fun runTest(content: @Composable () -> Unit) {
        val qualifier = "w${device.dp}dp-h${device.dp}dp-small-notlong-round-watch-${device.dpi}dpi-keyshidden-nonav"
        RuntimeEnvironment.setQualifiers(qualifier)

        composeRule.setContent {
            CompositionLocalProvider(
                LocalDensity provides Density(
                    density = LocalDensity.current.density,
                    fontScale = device.fontScale
                )
            ) {
                content()
            }
        }
        captureScreenshot("")
    }

    @OptIn(ExperimentalRoborazziApi::class)
    fun captureScreenshot(suffix: String) {
        val fileName = "${this::class.simpleName}_${device.id}$suffix.png"
        captureScreenRoboImage(
            filePath = "src/test/screenshots/$fileName",
            roborazziOptions = RoborazziOptions(
                recordOptions = RoborazziOptions.RecordOptions(applyDeviceCrop = true),
                compareOptions = RoborazziOptions.CompareOptions(changeThreshold = tolerance)
            )
        )
    }
}
