@file:SuppressLint("RestrictedApi")

package com.google.example.wear_widget.widget

import android.annotation.SuppressLint

import androidx.compose.remote.creation.compose.layout.RemoteAlignment
import androidx.compose.remote.creation.compose.layout.RemoteArrangement
import androidx.compose.remote.creation.compose.layout.RemoteBox
import androidx.compose.remote.creation.compose.layout.RemoteColumn
import androidx.compose.remote.creation.compose.layout.RemoteComposable
import androidx.compose.remote.creation.compose.modifier.RemoteModifier
import androidx.compose.remote.creation.compose.modifier.fillMaxSize
import androidx.compose.remote.creation.compose.modifier.size
import androidx.compose.remote.creation.compose.state.RemoteColor
import androidx.compose.remote.creation.compose.state.rdp
import androidx.compose.remote.creation.compose.state.rs
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.wear.compose.remote.material3.RemoteMaterialTheme
import androidx.wear.compose.remote.material3.RemoteText as MaterialRemoteText

/**
 * A Wear OS widget display with an Android robot icon and "Wear Widget" text at the top. Below, a
 * rounded black rectangle contains three lines of text: "Title Style" in large cyan, "Default Body
 * Style" in white, and "Caption Style" in white italics.
 */
@RemoteComposable
@Composable
fun TypographyScaleSample() {
    // Define our own "semantic" styles
    val myTitleStyle = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.Cyan)

    val myCaptionStyle =
        TextStyle(fontSize = 12.sp, fontStyle = FontStyle.Italic, color = Color.LightGray)

    RemoteMaterialTheme {
        RemoteBox(
            modifier = RemoteModifier.fillMaxSize(),
            horizontalAlignment = RemoteAlignment.CenterHorizontally,
            verticalArrangement = RemoteArrangement.Center,
        ) {
            RemoteColumn(horizontalAlignment = RemoteAlignment.CenterHorizontally) {
                // 1. Title style applied manually
                MaterialRemoteText(
                    text = "Title Style".rs,
                    fontSize = myTitleStyle.fontSize,
                    fontWeight = myTitleStyle.fontWeight,
                    color = RemoteColor(myTitleStyle.color),
                )

                RemoteBox(RemoteModifier.size(12.rdp))

                // 2. Default style (bodyLarge) provided by RemoteMaterialTheme
                MaterialRemoteText("Default Body Style".rs)

                RemoteBox(RemoteModifier.size(12.rdp))

                // 3. Caption style applied via 'style' parameter
                MaterialRemoteText(text = "Caption Style".rs, style = myCaptionStyle)
            }
        }
    }
}
