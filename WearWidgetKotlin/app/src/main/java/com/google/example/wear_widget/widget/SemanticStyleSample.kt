

package com.google.example.wear_widget.widget


import androidx.compose.remote.creation.compose.layout.RemoteAlignment
import androidx.compose.remote.creation.compose.layout.RemoteArrangement
import androidx.compose.remote.creation.compose.layout.RemoteBox
import androidx.compose.remote.creation.compose.layout.RemoteColumn
import androidx.compose.remote.creation.compose.layout.RemoteComposable
import androidx.compose.remote.creation.compose.modifier.RemoteModifier
import androidx.compose.remote.creation.compose.modifier.fillMaxSize
import androidx.compose.remote.creation.compose.modifier.fillMaxWidth
import androidx.compose.remote.creation.compose.modifier.size
import androidx.compose.remote.creation.compose.state.rdp
import androidx.compose.remote.creation.compose.state.rs
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.remote.creation.compose.text.RemoteTextStyle
import androidx.wear.compose.remote.material3.RemoteMaterialTheme
import androidx.wear.compose.remote.material3.RemoteText as MaterialRemoteText

/**
 * Android robot icon above "Wear Widget" text. A dark gray rounded rectangular widget displays
 * "Semantic Styles Demo" and a large digital time "12:34" in white text on a black background.
 */
@RemoteComposable
@Composable
fun SemanticStyleSample() {
    RemoteBox(
        modifier = RemoteModifier.fillMaxSize(),
        horizontalAlignment = RemoteAlignment.CenterHorizontally,
        verticalArrangement = RemoteArrangement.Center,
    ) {
        RemoteColumn(
            modifier = RemoteModifier.fillMaxWidth(),
            horizontalAlignment = RemoteAlignment.CenterHorizontally,
            verticalArrangement = RemoteArrangement.Center,
        ) {
            MaterialRemoteText(
                text = "Semantic Styles Demo".rs,
                style = RemoteMaterialTheme.typography.titleLarge,
            )

            RemoteBox(RemoteModifier.size(16.rdp))

            MaterialRemoteText(text = "12:34".rs, style = RemoteMaterialTheme.typography.numeralLarge)

            RemoteBox(RemoteModifier.size(12.rdp))

            MaterialRemoteText(
                text = "Session complete".rs,
                style = RemoteMaterialTheme.typography.titleMedium,
            )
        }
    }
}
