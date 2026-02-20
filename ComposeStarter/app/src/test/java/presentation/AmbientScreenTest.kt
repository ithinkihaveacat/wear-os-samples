/*
 * Copyright 2024 The Android Open Source Project
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
package presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.wear.compose.foundation.AmbientMode
import androidx.wear.compose.foundation.AmbientModeManager
import androidx.wear.compose.foundation.LocalAmbientModeManager
import androidx.wear.compose.material3.AppScaffold
import androidx.wear.compose.material3.TimeSource
import androidx.wear.compose.material3.TimeText
import com.example.android.wearable.composestarter.presentation.AmbientScreen
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.ParameterizedRobolectricTestRunner

@RunWith(ParameterizedRobolectricTestRunner::class)
class AmbientScreenTest(
    override val device: WearDevice
) : WearScreenshotTest() {
    override val tolerance = 0.02f

    @Test
    fun ambientScreenInteractiveTest() =
        runTest(suffix = "_interactive") {
            val interactiveManager =
                object : AmbientModeManager {
                    override val currentAmbientMode: AmbientMode = AmbientMode.Interactive

                    override suspend fun withAmbientTick(block: () -> Unit) {}
                }
            CompositionLocalProvider(LocalAmbientModeManager provides interactiveManager) {
                AppScaffold(
                    timeText = {
                        TimeText(
                            timeSource =
                                object : TimeSource {
                                    @Composable
                                    override fun currentTime(): String = "10:10"
                                }
                        )
                    }
                ) {
                    AmbientScreen()
                }
            }
        }

    @Test
    fun ambientScreenAmbientTest() =
        runTest(suffix = "_ambient") {
            val ambientManager =
                object : AmbientModeManager {
                    override val currentAmbientMode: AmbientMode =
                        AmbientMode.Ambient(
                            isBurnInProtectionRequired = false,
                            isLowBitAmbientSupported = false
                        )

                    override suspend fun withAmbientTick(block: () -> Unit) {}
                }
            CompositionLocalProvider(LocalAmbientModeManager provides ambientManager) {
                AppScaffold(
                    timeText = {
                        TimeText(
                            timeSource =
                                object : TimeSource {
                                    @Composable
                                    override fun currentTime(): String = "10:10"
                                }
                        )
                    }
                ) {
                    AmbientScreen()
                }
            }
        }

    companion object {
        @JvmStatic
        @ParameterizedRobolectricTestRunner.Parameters
        fun devices() = WearDevice.entries
    }
}
