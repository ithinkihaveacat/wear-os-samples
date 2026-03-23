@file:SuppressLint("RestrictedApi")

package com.google.example.wear_widget.widget

import android.annotation.SuppressLint

import androidx.compose.remote.creation.compose.layout.RemoteAlignment
import androidx.compose.remote.creation.compose.layout.RemoteArrangement
import androidx.compose.remote.creation.compose.layout.RemoteBox
import androidx.compose.remote.creation.compose.layout.RemoteComposable
import androidx.compose.remote.creation.compose.layout.RemoteText
import androidx.compose.remote.creation.compose.modifier.RemoteModifier
import androidx.compose.remote.creation.compose.modifier.background
import androidx.compose.remote.creation.compose.modifier.fillMaxSize
import androidx.compose.remote.creation.compose.modifier.padding
import androidx.compose.remote.creation.compose.state.rc
import androidx.compose.remote.creation.compose.state.rsp
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.wear.compose.remote.material3.RemoteMaterialTheme

/**
 * Android logo above "Wear Widget" title. Below, a large blue rectangle with a gray border. Inside
 * the blue box, at the bottom right, is yellow text: "Box Sample 3 (Bottom End)".
 */
@RemoteComposable
@Composable
fun BoxSample3() {
    // Box with different alignment (BottomEnd)
    RemoteMaterialTheme {
        RemoteBox(
            modifier = RemoteModifier.fillMaxSize().background(Color.Blue),
            horizontalAlignment = RemoteAlignment.End,
            verticalArrangement = RemoteArrangement.Bottom,
        ) {
            RemoteText(
                modifier = RemoteModifier.padding(10.dp),
                text = "Box Sample 3\n(Bottom End)",
                color = Color.Yellow.rc,
                fontSize = 10.rsp,
                textAlign = TextAlign.End,
            )
        }
    }
}
