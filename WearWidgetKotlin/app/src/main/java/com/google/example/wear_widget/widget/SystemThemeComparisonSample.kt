
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
import androidx.compose.remote.creation.compose.state.rc
import androidx.compose.remote.creation.compose.state.rdp
import androidx.compose.remote.creation.compose.state.rememberRemoteIntValue
import androidx.compose.remote.creation.compose.state.ri
import androidx.compose.remote.creation.compose.state.rs
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.wear.compose.remote.material3.RemoteButton
import androidx.wear.compose.remote.material3.RemoteButtonColors
import androidx.wear.compose.remote.material3.RemoteMaterialTheme
import androidx.wear.compose.remote.material3.RemoteText as MaterialRemoteText

/**
 * A UI showing an Android Wear Widget. It features a dark gray rounded rectangle container with the
 * title "System Theme" in white. Inside are two horizontal rounded buttons: "Primary Button" with
 * dark text on light blue-gray, and "Secondary Button" with dark text on light gray. An Android
 * logo is at the top.
 */
@RemoteComposable
@Composable
fun SystemThemeComparisonSample() {
val dummy = rememberRemoteIntValue { 0 }
    RemoteBox(
        modifier = RemoteModifier.fillMaxSize(),
        horizontalAlignment = RemoteAlignment.CenterHorizontally,
        verticalArrangement = RemoteArrangement.Center,
    ) {
        RemoteColumn(horizontalAlignment = RemoteAlignment.CenterHorizontally) {
            MaterialRemoteText("System Theme".rs)
            RemoteBox(RemoteModifier.size(10.rdp))
            RemoteButton(onClick = ValueChange(dummy, 0.ri)) {
                MaterialRemoteText("Primary Button".rs)
            }
            RemoteBox(RemoteModifier.size(10.rdp))
            RemoteButton(
                onClick = ValueChange(dummy, 0.ri),
                colors =
                    RemoteButtonColors(
                        containerColor = RemoteMaterialTheme.colorScheme.secondary,
                        contentColor = RemoteMaterialTheme.colorScheme.onSecondary,
                        secondaryContentColor = RemoteMaterialTheme.colorScheme.onSecondary,
                        iconColor = RemoteMaterialTheme.colorScheme.onSecondary,
                        disabledContainerColor = Color.Gray.rc,
                        disabledContentColor = Color.LightGray.rc,
                        disabledSecondaryContentColor = Color.LightGray.rc,
                        disabledIconColor = Color.LightGray.rc,
                    ),
            ) {
                MaterialRemoteText("Secondary Button".rs)
            }
        }
    }
}
