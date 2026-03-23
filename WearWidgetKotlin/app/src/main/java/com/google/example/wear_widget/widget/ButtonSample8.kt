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
import androidx.compose.remote.creation.compose.state.rsp
import androidx.compose.remote.creation.compose.state.rs
import androidx.compose.runtime.Composable
import androidx.wear.compose.remote.material3.RemoteButton
import androidx.wear.compose.remote.material3.RemoteButtonGroup
import androidx.wear.compose.remote.material3.RemoteButtonGroupDefaults
import androidx.wear.compose.remote.material3.RemoteMaterialTheme
import androidx.wear.compose.remote.material3.RemoteText as MaterialRemoteText

/**
 * Android logo above "Wear Widget" title. Below, a dark gray rounded dialog box contains two light
 * purple, rounded buttons side-by-side. The left button says "Yes Confirm", and the right says "No
 * Cancel".
 */
@RemoteComposable
@Composable
fun ButtonSample8() {
    val dummy = rememberRemoteIntValue { 0 }
    RemoteMaterialTheme {
        RemoteBox(
            modifier = RemoteModifier.fillMaxSize(),
            horizontalAlignment = RemoteAlignment.CenterHorizontally,
            verticalArrangement = RemoteArrangement.Center,
        ) {
            RemoteButtonGroup {
                RemoteButton(
                    onClick = ValueChange(dummy, 0.ri),
                    modifier = RemoteModifier.weight(1f),
                    label = { MaterialRemoteText("Yes".rs, fontSize = 12.rsp) },
                    secondaryLabel = { MaterialRemoteText("Confirm".rs, fontSize = 10.rsp) },
                )
                RemoteBox(RemoteModifier.size(RemoteButtonGroupDefaults.Spacing))
                RemoteButton(
                    onClick = ValueChange(dummy, 0.ri),
                    modifier = RemoteModifier.weight(1f),
                    label = { MaterialRemoteText("No".rs, fontSize = 12.rsp) },
                    secondaryLabel = { MaterialRemoteText("Cancel".rs, fontSize = 10.rsp) },
                )
            }
        }
    }
}
