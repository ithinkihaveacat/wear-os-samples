
package com.google.example.wear_widget.widget


import androidx.compose.remote.creation.compose.action.ValueChange
import androidx.compose.remote.creation.compose.layout.RemoteAlignment
import androidx.compose.remote.creation.compose.layout.RemoteArrangement
import androidx.compose.remote.creation.compose.layout.RemoteBox
import androidx.compose.remote.creation.compose.layout.RemoteComposable
import androidx.compose.remote.creation.compose.modifier.RemoteModifier
import androidx.compose.remote.creation.compose.modifier.fillMaxSize
import androidx.compose.remote.creation.compose.state.rc
import androidx.compose.remote.creation.compose.state.rememberRemoteIntValue
import androidx.compose.remote.creation.compose.state.ri
import androidx.compose.remote.creation.compose.state.rs
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.wear.compose.remote.material3.RemoteButton
import androidx.wear.compose.remote.material3.RemoteButtonColors
import androidx.wear.compose.remote.material3.RemoteMaterialTheme
import androidx.wear.compose.remote.material3.RemoteText as MaterialRemoteText
import androidx.wear.compose.remote.material3.buttonSizeModifier

/**
 * Black background. Top: Android icon above "Wear Widget" text. Below, a large gray rounded
 * rectangle contains a smaller, centered red rounded rectangle button with yellow text "Custom
 * Colors".
 */
@RemoteComposable
@Composable
fun ButtonSample4() {
val dummy = rememberRemoteIntValue { 0 }
    RemoteBox(
        modifier = RemoteModifier.fillMaxSize(),
        contentAlignment = RemoteAlignment.Center,
    ) {
        RemoteButton(
            onClick = ValueChange(dummy, 0.ri),
            modifier = RemoteModifier.buttonSizeModifier(),
            colors =
                RemoteButtonColors(
                    containerColor = Color.Red.rc,
                    contentColor = Color.Yellow.rc,
                    secondaryContentColor = Color.Yellow.rc,
                    iconColor = Color.Yellow.rc,
                    disabledContainerColor = Color.Gray.rc,
                    disabledContentColor = Color.LightGray.rc,
                    disabledSecondaryContentColor = Color.LightGray.rc,
                    disabledIconColor = Color.LightGray.rc,
                ),
        ) {
            MaterialRemoteText("Custom Colors".rs)
        }
    }
}
