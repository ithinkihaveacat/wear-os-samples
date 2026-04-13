
package com.google.example.wear_widget.widget

import androidx.compose.remote.creation.compose.state.rs
import androidx.compose.remote.creation.compose.state.rf
import androidx.compose.remote.creation.compose.state.rsp

import androidx.compose.remote.creation.compose.layout.RemoteAlignment
import androidx.compose.remote.creation.compose.layout.RemoteArrangement
import androidx.compose.remote.creation.compose.layout.RemoteBox
import androidx.compose.remote.creation.compose.layout.RemoteColumn
import androidx.compose.remote.creation.compose.layout.RemoteComposable
import androidx.compose.remote.creation.compose.layout.RemoteText
import androidx.compose.remote.creation.compose.modifier.RemoteModifier
import androidx.compose.remote.creation.compose.modifier.background
import androidx.compose.remote.creation.compose.modifier.fillMaxSize
import androidx.compose.remote.creation.compose.modifier.padding
import androidx.compose.remote.creation.compose.state.rc
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.remote.material3.RemoteMaterialTheme

/**
 * UI screenshot with a black background. At the top, an Android icon above "Wear Widget" text.
 * Below, a dark green rounded rectangular widget with a gray border. Inside, three lines of white
 * text: "TextSample", "1", and "This is a long text".
 */
@RemoteComposable
@Composable
fun TextSample1WithMargin() {
    RemoteBox(
        modifier = RemoteModifier.fillMaxSize().background(Color(0xFF006400)).padding(30.dp),
        contentAlignment = RemoteAlignment.Center,
    ) {
        RemoteColumn(
            horizontalAlignment = RemoteAlignment.CenterHorizontally,
            verticalArrangement = RemoteArrangement.Center,
        ) {
            RemoteText(
                text = "TextSample1".rs,
                color = Color.White.rc,
                fontSize = 20.rsp,
                fontWeight = FontWeight.Bold,
            )
            RemoteText(
                text =
                    "This is a long text that should wrap to multiple lines to demonstrate the multi-line capability.".rs,
                color = Color.LightGray.rc,
                fontSize = 14.rsp,
                maxLines = 2,
                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            )
            RemoteText(
                text = "Version 1.0".rs,
                color = Color.Cyan.rc,
                fontSize = 10.rsp,
                fontStyle = FontStyle.Italic,
            )
        }
    }
}
