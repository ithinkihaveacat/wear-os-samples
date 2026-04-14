
package com.google.example.wear_widget.widget


import androidx.compose.remote.creation.compose.action.ValueChange
import androidx.compose.remote.creation.compose.layout.RemoteAlignment
import androidx.compose.remote.creation.compose.layout.RemoteArrangement
import androidx.compose.remote.creation.compose.layout.RemoteBox
import androidx.compose.remote.creation.compose.layout.RemoteColumn
import androidx.compose.remote.creation.compose.layout.RemoteComposable
import androidx.compose.remote.creation.compose.layout.RemoteRow
import androidx.compose.remote.creation.compose.modifier.RemoteModifier
import androidx.compose.remote.creation.compose.modifier.fillMaxSize
import androidx.compose.remote.creation.compose.modifier.padding
import androidx.compose.remote.creation.compose.modifier.size
import androidx.compose.remote.creation.compose.state.rb
import androidx.compose.remote.creation.compose.state.rc
import androidx.compose.remote.creation.compose.state.rdp
import androidx.compose.remote.creation.compose.state.rememberMutableRemoteInt
import androidx.compose.remote.creation.compose.state.ri
import androidx.compose.remote.creation.compose.state.rs
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.wear.compose.remote.material3.RemoteButton
import androidx.wear.compose.remote.material3.RemoteButtonColors
import androidx.wear.compose.remote.material3.RemoteIcon
import androidx.wear.compose.remote.material3.RemoteMaterialTheme
import androidx.wear.compose.remote.material3.RemoteText as MaterialRemoteText
import com.google.example.wear_widget.R

/**
 * A dark screen shows a light gray circular icon with an Android head, then white text "Wear
 * Widget". Below, a dark gray rounded rectangle card features a tilted cyan Android icon on the
 * left and stacked white text "Card Title", "Subtitle", "goes here" on the right. Small black
 * triangles highlight each of the card's four corners.
 */
@RemoteComposable
@Composable
fun CardSample1() {
val dummy = rememberMutableRemoteInt(0)
    RemoteBox(
        modifier = RemoteModifier.fillMaxSize(),
        contentAlignment = RemoteAlignment.Center,
    ) {
        RemoteButton(
            onClick = ValueChange(dummy, 0.ri),
            modifier = RemoteModifier.fillMaxSize().padding(10.rdp),
            enabled = false.rb, // act as container
            colors =
                RemoteButtonColors(
                    containerColor = Color.DarkGray.rc,
                    contentColor = Color.White.rc,
                    secondaryContentColor = Color.LightGray.rc,
                    iconColor = Color.White.rc,
                    disabledContainerColor = Color.DarkGray.rc,
                    disabledContentColor = Color.White.rc,
                    disabledSecondaryContentColor = Color.LightGray.rc,
                    disabledIconColor = Color.White.rc,
                ),
        ) {
            RemoteRow(verticalAlignment = RemoteAlignment.CenterVertically) {
                RemoteIcon(
                    imageVector =
                        ImageVector.vectorResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = "Card Icon".rs,
                    modifier = RemoteModifier.size(40.rdp),
                    tint = Color.Cyan.rc,
                )
                RemoteBox(RemoteModifier.size(10.rdp))
                RemoteColumn {
                    MaterialRemoteText("Card Title".rs)
                    MaterialRemoteText("Subtitle goes here".rs)
                }
            }
        }
    }
}
