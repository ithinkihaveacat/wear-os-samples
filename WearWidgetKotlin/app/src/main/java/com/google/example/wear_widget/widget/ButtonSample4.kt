@file:SuppressLint("RestrictedApi")

package com.google.example.wear_widget.widget

import android.annotation.SuppressLint

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
import androidx.compose.remote.creation.compose.state.rsp
import androidx.compose.remote.creation.compose.state.rs
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.wear.compose.remote.material3.RemoteButton
import androidx.wear.compose.remote.material3.RemoteButtonColors
import androidx.wear.compose.remote.material3.RemoteMaterialTheme
import androidx.wear.compose.remote.material3.RemoteText as MaterialRemoteText
import androidx.wear.compose.remote.material3.buttonSizeModifier

/**
 * A dark screen with a top-centered light grey circular icon featuring a dark grey Android logo.
 * Below, white text "Wear Widget". A dark gray rounded card houses a centered pink button labeled
 * "Custom Colors" in dark gray.
 */
@RemoteComposable
@Composable
fun ButtonSample4() {
    val dummy = rememberRemoteIntValue { 0 }
    RemoteMaterialTheme {
        RemoteBox(
            modifier = RemoteModifier.fillMaxSize(),
            horizontalAlignment = RemoteAlignment.CenterHorizontally,
            verticalArrangement = RemoteArrangement.Center,
        ) {
            RemoteButton(
                onClick = ValueChange(dummy, 0.ri),
                modifier = RemoteModifier.buttonSizeModifier(),
                colors =
                    RemoteButtonColors(
                        containerColor = Color.Magenta.rc,
                        contentColor = Color.Black.rc,
                        secondaryContentColor = Color.Black.rc,
                        iconColor = Color.Black.rc,
                        disabledContainerColor = Color.Gray.rc,
                        disabledContentColor = Color.LightGray.rc,
                        disabledSecondaryContentColor = Color.LightGray.rc,
                        disabledIconColor = Color.LightGray.rc,
                    ),
            ) {
                MaterialRemoteText("Custom Colors".rs, fontSize = 12.rsp)
            }
        }
    }
}
