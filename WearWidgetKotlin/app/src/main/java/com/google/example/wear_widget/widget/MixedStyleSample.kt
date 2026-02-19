@file:SuppressLint("RestrictedApi")

package com.google.example.wear_widget.widget

import android.annotation.SuppressLint

import androidx.compose.remote.creation.compose.layout.RemoteAlignment
import androidx.compose.remote.creation.compose.layout.RemoteArrangement
import androidx.compose.remote.creation.compose.layout.RemoteBox
import androidx.compose.remote.creation.compose.layout.RemoteComposable
import androidx.compose.remote.creation.compose.layout.RemoteRow
import androidx.compose.remote.creation.compose.layout.RemoteText
import androidx.compose.remote.creation.compose.modifier.RemoteModifier
import androidx.compose.remote.creation.compose.modifier.background
import androidx.compose.remote.creation.compose.modifier.fillMaxSize
import androidx.compose.remote.creation.compose.state.rc
import androidx.compose.remote.creation.compose.state.rs
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.wear.compose.remote.material3.RemoteMaterialTheme

/**
 * A screen features an Android icon and "Wear Widget" text at the top. Below, a white rectangular
 * card with rounded corners displays "Mixed Styles". The word "Mixed" is red and bold, while
 * "Styles" is blue and italic.
 */
@RemoteComposable
@Composable
fun MixedStyleSample() {
    RemoteMaterialTheme {
        RemoteBox(
            modifier = RemoteModifier.fillMaxSize().background(Color.White),
            horizontalAlignment = RemoteAlignment.CenterHorizontally,
            verticalArrangement = RemoteArrangement.Center,
        ) {
            RemoteRow(
                verticalAlignment = RemoteAlignment.CenterVertically,
                horizontalArrangement = RemoteArrangement.CenterHorizontally,
            ) {
                // First part: Bold Red Text
                RemoteText(
                    text = "Mixed ".rs,
                    color = Color.Red.rc,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                )
                // Second part: Italic Blue Text
                RemoteText(
                    text = "Styles".rs,
                    color = Color.Blue.rc,
                    fontSize = 20.sp,
                    fontStyle = FontStyle.Italic,
                )
            }
        }
    }
}
