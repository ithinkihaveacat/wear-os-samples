
package com.google.example.wear_widget.widget


import androidx.compose.remote.creation.compose.action.ValueChange
import androidx.compose.remote.creation.compose.layout.RemoteAlignment
import androidx.compose.remote.creation.compose.layout.RemoteArrangement
import androidx.compose.remote.creation.compose.layout.RemoteBox
import androidx.compose.remote.creation.compose.layout.RemoteColumn
import androidx.compose.remote.creation.compose.layout.RemoteComposable
import androidx.compose.remote.creation.compose.modifier.RemoteModifier
import androidx.compose.remote.creation.compose.modifier.fillMaxSize
import androidx.compose.remote.creation.compose.modifier.size
import androidx.compose.remote.creation.compose.state.rdp
import androidx.compose.remote.creation.compose.state.rememberRemoteIntValue
import androidx.compose.remote.creation.compose.state.ri
import androidx.compose.remote.creation.compose.state.rs
import androidx.compose.runtime.Composable
import androidx.wear.compose.remote.material3.RemoteButton
import androidx.wear.compose.remote.material3.RemoteMaterialTheme
import androidx.wear.compose.remote.material3.RemoteText as MaterialRemoteText

/**
 * A dark screen displays a light gray Android icon above "Wear Widget". Below, a dark gray rounded
 * rectangle contains "System Theme" text, and a light purple button labeled "Primary Button".
 */
@RemoteComposable
@Composable
fun SystemThemeSample() {
val dummy = rememberRemoteIntValue { 0 }
    RemoteBox(
        modifier = RemoteModifier.fillMaxSize(),
        contentAlignment = RemoteAlignment.Center,
    ) {
        RemoteColumn(horizontalAlignment = RemoteAlignment.CenterHorizontally) {
            MaterialRemoteText("System Theme".rs)
            RemoteBox(RemoteModifier.size(10.rdp))
            RemoteButton(onClick = ValueChange(dummy, 0.ri)) {
                MaterialRemoteText("Primary Button".rs)
            }
        }
    }
}
