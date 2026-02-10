package com.google.example.wear_widget.widget

import androidx.compose.remote.creation.compose.layout.RemoteAlignment
import androidx.compose.remote.creation.compose.layout.RemoteArrangement
import androidx.compose.remote.creation.compose.layout.RemoteBox
import androidx.compose.remote.creation.compose.layout.RemoteComposable
import androidx.compose.remote.creation.compose.layout.RemoteRow
import androidx.compose.remote.creation.compose.layout.RemoteText
import androidx.compose.remote.creation.compose.modifier.RemoteModifier
import androidx.compose.remote.creation.compose.modifier.background
import androidx.compose.remote.creation.compose.modifier.fillMaxSize
import androidx.compose.remote.creation.compose.modifier.padding
import androidx.compose.remote.creation.compose.state.rc
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.wear.compose.remote.material3.RemoteMaterialTheme

/**
 * A screen features an Android robot head logo and "Wear Widget" text at the top. Below, a dark
 * gray bordered rectangle serves as a widget frame. Inside, three horizontal buttons are displayed:
 * a red button with "Red" text, a green button with "Green" text, and a blue button with "Blue"
 * text. All button text is white.
 */
@RemoteComposable
@Composable
fun RowSample1() {
    RemoteMaterialTheme {
        RemoteBox(
            modifier = RemoteModifier.fillMaxSize(),
            horizontalAlignment = RemoteAlignment.CenterHorizontally,
            verticalArrangement = RemoteArrangement.Center,
        ) {
            RemoteRow(
                modifier = RemoteModifier.fillMaxSize(),
                horizontalArrangement = RemoteArrangement.CenterHorizontally,
                verticalAlignment = RemoteAlignment.CenterVertically,
            ) {
                RemoteBox(modifier = RemoteModifier.padding(5.dp).background(Color.Red)) {
                    RemoteText(
                        "Red",
                        color = Color.White.rc,
                        modifier = RemoteModifier.padding(5.dp),
                    )
                }
                RemoteBox(modifier = RemoteModifier.padding(5.dp).background(Color.Green)) {
                    RemoteText(
                        "Green",
                        color = Color.Black.rc,
                        modifier = RemoteModifier.padding(5.dp),
                    )
                }
                RemoteBox(modifier = RemoteModifier.padding(5.dp).background(Color.Blue)) {
                    RemoteText(
                        "Blue",
                        color = Color.White.rc,
                        modifier = RemoteModifier.padding(5.dp),
                    )
                }
            }
        }
    }
}
