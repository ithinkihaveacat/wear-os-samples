package com.google.example.wear_widget.widget

import androidx.compose.remote.creation.compose.action.ValueChange
import androidx.compose.remote.creation.compose.layout.RemoteAlignment
import androidx.compose.remote.creation.compose.layout.RemoteArrangement
import androidx.compose.remote.creation.compose.layout.RemoteBox
import androidx.compose.remote.creation.compose.layout.RemoteComposable
import androidx.compose.remote.creation.compose.layout.RemoteRow
import androidx.compose.remote.creation.compose.modifier.RemoteModifier
import androidx.compose.remote.creation.compose.modifier.fillMaxSize
import androidx.compose.remote.creation.compose.modifier.padding
import androidx.compose.remote.creation.compose.modifier.size
import androidx.compose.remote.creation.compose.state.rdp
import androidx.compose.remote.creation.compose.state.rememberRemoteIntValue
import androidx.compose.remote.creation.compose.state.ri
import androidx.compose.remote.creation.compose.state.rs
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.wear.compose.remote.material3.RemoteButton
import androidx.wear.compose.remote.material3.RemoteMaterialTheme
import androidx.wear.compose.remote.material3.RemoteText as MaterialRemoteText

/**
 * A dark UI displays an Android logo, text "Wear Widget," and a rounded dark gray card. Inside the
 * card are two horizontal, light gray, pill-shaped buttons: "Yes" on the left and "No" on the
 * right.
 */
@RemoteComposable
@Composable
fun ButtonSample7() {
    val dummy = rememberRemoteIntValue { 0 }
    RemoteMaterialTheme {
        RemoteBox(
            modifier = RemoteModifier.fillMaxSize(),
            horizontalAlignment = RemoteAlignment.CenterHorizontally,
            verticalArrangement = RemoteArrangement.Center,
        ) {
            RemoteRow(
                modifier = RemoteModifier.padding(horizontal = 11.dp),
                horizontalArrangement = RemoteArrangement.CenterHorizontally,
                verticalAlignment = RemoteAlignment.CenterVertically,
            ) {
                RemoteButton(
                    onClick = ValueChange(dummy, 0.ri),
                    modifier = RemoteModifier.weight(1f),
                ) {
                    MaterialRemoteText("Yes".rs)
                }
                RemoteBox(RemoteModifier.size(4.rdp)) // Spacing
                RemoteButton(
                    onClick = ValueChange(dummy, 0.ri),
                    modifier = RemoteModifier.weight(1f),
                ) {
                    MaterialRemoteText("No".rs)
                }
            }
        }
    }
}
