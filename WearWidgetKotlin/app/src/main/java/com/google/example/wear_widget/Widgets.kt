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
@file:Suppress("COMPOSE_APPLIER_CALL_MISMATCH") // b/446706254
@file:android.annotation.SuppressLint("RestrictedApi")

package com.google.example.wear_widget

import androidx.compose.remote.core.operations.TextFromFloat
import androidx.compose.remote.creation.ExperimentalRemoteCreationApi
import androidx.compose.remote.creation.compose.action.ValueChange
import androidx.compose.remote.creation.compose.layout.RemoteAlignment
import androidx.compose.remote.creation.compose.layout.RemoteBox
import androidx.compose.remote.creation.compose.layout.RemoteComposable
import androidx.compose.remote.creation.compose.modifier.RemoteModifier
import androidx.compose.remote.creation.compose.modifier.fillMaxSize
import androidx.compose.remote.creation.compose.modifier.size
import androidx.compose.remote.creation.compose.state.RemoteColor
import androidx.compose.remote.creation.compose.state.rb
import androidx.compose.remote.creation.compose.state.rdp
import androidx.compose.remote.creation.compose.state.rememberMutableRemoteInt
import androidx.compose.remote.creation.compose.state.ri
import androidx.compose.remote.creation.compose.state.rs
import androidx.compose.remote.creation.profile.Profile
import androidx.compose.remote.creation.profile.RcPlatformProfiles.ANDROIDX
import androidx.compose.remote.creation.profile.RcPlatformProfiles.WEAR_WIDGETS
import androidx.compose.remote.tooling.preview.RemotePreview
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.wear.compose.remote.material3.RemoteButton
import androidx.wear.compose.remote.material3.RemoteIcon
import androidx.wear.compose.remote.material3.RemoteText
import androidx.wear.compose.remote.material3.buttonSizeModifier
import androidx.wear.compose.ui.tooling.preview.WearPreviewDevices

@Composable
@RemoteComposable
fun WidgetButtonSample(modifier: RemoteModifier = RemoteModifier) {
    val tapCount = rememberMutableRemoteInt(0)
    val countSuffix = " (".rs + tapCount.toRemoteString(10, TextFromFloat.PAD_PRE_NONE) + " taps)"

    RemoteButton(ValueChange(tapCount, tapCount + 1), modifier = modifier) {
        RemoteText("Tap me!".rs + countSuffix)
    }
}

@WearPreviewDevices
@Composable
fun WidgetButtonSamplePreview() = RemotePreview { Container { WidgetButtonSample() } }

@Composable
@RemoteComposable
fun WidgetIconSample(modifier: RemoteModifier = RemoteModifier) {
    RemoteIcon(
        modifier = modifier.size(24.rdp),
        imageVector = ImageVector.vectorResource(R.drawable.android_24px),
        contentDescription = "Launcher Icon".rs,
        tint = RemoteColor(Color.White),
    )
}

@WearPreviewDevices
@Composable
fun WidgetIconSamplePreview() = RemotePreview { Container { WidgetIconSample() } }

@Composable
@RemoteComposable
fun WidgetTextSample(modifier: RemoteModifier = RemoteModifier) {
    RemoteText("Just some text".rs, modifier = modifier, color = RemoteColor(Color.Cyan))
}

@WearPreviewDevices
@Composable
fun WidgetTextSamplePreview() = RemotePreview { Container { WidgetTextSample() } }

@Composable
@RemoteComposable
private fun Container(
    modifier: RemoteModifier = RemoteModifier.fillMaxSize(),
    content: @Composable @RemoteComposable () -> Unit,
) {
    RemoteBox(modifier, contentAlignment = RemoteAlignment.Center, content = content)
}

class ProfilePreviewParameterProvider : PreviewParameterProvider<Profile> {
    @OptIn(ExperimentalRemoteCreationApi::class)
    override val values = sequenceOf(ANDROIDX, WEAR_WIDGETS)
}

@Composable
@RemoteComposable
fun RemoteButtonEnabled() {
    val dummy = rememberMutableRemoteInt(0)
    RemoteButton(
        onClick = ValueChange(dummy, 0.ri),
        modifier = RemoteModifier.buttonSizeModifier(),
        enabled = true.rb,
        content = { RemoteText("button_enabled".rs) },
    )
}

@WearPreviewDevices
@Composable
private fun RemoteButtonEnabledPreview(
    @PreviewParameter(ProfilePreviewParameterProvider::class) profile: Profile
) {
    val content: @Composable @RemoteComposable () -> Unit = { Container { RemoteButtonEnabled() } }
    RemotePreview(profile = profile, content = content)
}
