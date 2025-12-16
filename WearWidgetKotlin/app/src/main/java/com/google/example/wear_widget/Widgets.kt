@file:SuppressLint("RestrictedApi")
@file:Suppress("COMPOSE_APPLIER_CALL_MISMATCH") // b/446706254

package com.google.example.wear_widget

import android.annotation.SuppressLint
import androidx.compose.remote.core.operations.TextFromFloat
import androidx.compose.remote.creation.ExperimentalRemoteCreationApi
import androidx.compose.remote.creation.compose.action.ValueChange
import androidx.compose.remote.creation.compose.layout.RemoteAlignment
import androidx.compose.remote.creation.compose.layout.RemoteArrangement
import androidx.compose.remote.creation.compose.layout.RemoteBox
import androidx.compose.remote.creation.compose.layout.RemoteComposable
import androidx.compose.remote.creation.compose.modifier.RemoteModifier
import androidx.compose.remote.creation.compose.modifier.fillMaxSize
import androidx.compose.remote.creation.compose.modifier.size
import androidx.compose.remote.creation.compose.state.RemoteColor
import androidx.compose.remote.creation.compose.state.rb
import androidx.compose.remote.creation.compose.state.rdp
import androidx.compose.remote.creation.compose.state.rememberRemoteIntValue
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
    val tapCount = rememberRemoteIntValue { 0 }
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
        imageVector = ImageVector.vectorResource(R.drawable.ic_launcher_foreground),
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
    RemoteBox(
        modifier,
        horizontalAlignment = RemoteAlignment.CenterHorizontally,
        verticalArrangement = RemoteArrangement.Center,
        content = content,
    )
}

class ProfilePreviewParameterProvider : PreviewParameterProvider<Profile> {
    @OptIn(ExperimentalRemoteCreationApi::class)
    override val values = sequenceOf(ANDROIDX, WEAR_WIDGETS)
}

@Composable
@RemoteComposable
fun RemoteButtonEnabled() {
    RemoteButton(
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
