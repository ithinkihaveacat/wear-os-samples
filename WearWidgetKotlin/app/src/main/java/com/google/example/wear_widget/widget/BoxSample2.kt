@file:SuppressLint("RestrictedApi")

package com.google.example.wear_widget.widget

import android.annotation.SuppressLint

import androidx.compose.remote.creation.compose.layout.RemoteAlignment
import androidx.compose.remote.creation.compose.layout.RemoteArrangement
import androidx.compose.remote.creation.compose.layout.RemoteBox
import androidx.compose.remote.creation.compose.layout.RemoteComposable
import androidx.compose.remote.creation.compose.modifier.RemoteModifier
import androidx.compose.remote.creation.compose.modifier.border
import androidx.compose.remote.creation.compose.modifier.fillMaxSize
import androidx.compose.remote.creation.compose.modifier.padding
import androidx.compose.remote.creation.compose.state.rc
import androidx.compose.remote.creation.compose.state.rdp
import androidx.compose.remote.creation.compose.state.rs
import androidx.compose.remote.creation.compose.text.RemoteTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.wear.compose.remote.material3.ProvideRemoteTextStyle
import androidx.wear.compose.remote.material3.RemoteMaterialTheme
import androidx.wear.compose.remote.material3.RemoteText as MaterialRemoteText
import com.google.example.wear_widget.MyWidgetTypography

/**
 * Android logo and "Wear Widget" text. A dark grey rounded rectangle widget displays white text
 * "Box Sample 2 (Border & Padding)", centered and surrounded by a distinct red rectangular border.
 * The text is visibly padded from the red border, which itself is padded from the grey widget's
 * edges.
 */
@RemoteComposable
@Composable
fun BoxSample2() {
// Box with padding and border
    ProvideRemoteTextStyle(value = RemoteTextStyle.fromTextStyle(MyWidgetTypography.bodyMedium)) {
        RemoteBox(
            modifier =
                RemoteModifier.fillMaxSize()
                    .padding(20.dp)
                    .border(width = 2.rdp, color = Color.Red.rc),
            horizontalAlignment = RemoteAlignment.CenterHorizontally,
            verticalArrangement = RemoteArrangement.Center,
        ) {
            MaterialRemoteText(
                text = "Box Sample 2\n(Border & Padding)".rs,
                color = Color.White.rc,
                textAlign = TextAlign.Center,
            )
        }
    }
}
