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
import androidx.compose.remote.creation.compose.modifier.size
import androidx.compose.remote.creation.compose.state.rememberRemoteIntValue
import androidx.compose.remote.creation.compose.state.ri
import androidx.compose.remote.creation.compose.state.rs
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.wear.compose.remote.material3.RemoteButton
import androidx.wear.compose.remote.material3.RemoteButtonDefaults
import androidx.wear.compose.remote.material3.RemoteIcon
import androidx.wear.compose.remote.material3.RemoteMaterialTheme
import androidx.wear.compose.remote.material3.RemoteText as MaterialRemoteText
import androidx.wear.compose.remote.material3.buttonSizeModifier
import com.google.example.wear_widget.R

/**
 * UI screenshot on a black background. Top center features a light grey circular Android robot
 * icon. Below it, white text "Wear Widget". A dark grey rounded rectangular container holds a light
 * purple rounded button with a dark grey Android chip-like icon and the text "Button with Icon".
 */
@RemoteComposable
@Composable
fun ButtonSample2() {
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
                icon = {
                    RemoteIcon(
                        imageVector =
                            ImageVector.vectorResource(id = R.drawable.ic_launcher_foreground),
                        contentDescription = "Icon".rs,
                        modifier = RemoteModifier.size(RemoteButtonDefaults.IconSize),
                    )
                },
                label = { MaterialRemoteText("Button with Icon".rs) },
            )
        }
    }
}
