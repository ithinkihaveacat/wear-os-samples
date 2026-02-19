@file:SuppressLint("RestrictedApi")

package com.google.example.wear_widget.widget

import android.annotation.SuppressLint

import androidx.compose.remote.creation.compose.layout.RemoteAlignment
import androidx.compose.remote.creation.compose.layout.RemoteArrangement
import androidx.compose.remote.creation.compose.layout.RemoteBox
import androidx.compose.remote.creation.compose.layout.RemoteColumn
import androidx.compose.remote.creation.compose.layout.RemoteComposable
import androidx.compose.remote.creation.compose.layout.RemoteText
import androidx.compose.remote.creation.compose.modifier.RemoteModifier
import androidx.compose.remote.creation.compose.modifier.background
import androidx.compose.remote.creation.compose.modifier.fillMaxSize
import androidx.compose.remote.creation.compose.state.rc
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.wear.compose.remote.material3.RemoteMaterialTheme

/**
 * Android logo above "Wear Widget" title. A dark-grey bordered, rounded green widget displays:
 * white bold "TextSample1", white ellipsized "This is a long text that should wrap to multiple
 * lin...", and green "Version 1.0".
 */
@RemoteComposable
@Composable
fun TextSample1() {
    RemoteMaterialTheme {
        RemoteBox(
            modifier = RemoteModifier.fillMaxSize().background(Color(0xFF006400)),
            horizontalAlignment = RemoteAlignment.CenterHorizontally,
            verticalArrangement = RemoteArrangement.Center,
        ) {
            RemoteColumn(
                horizontalAlignment = RemoteAlignment.CenterHorizontally,
                verticalArrangement = RemoteArrangement.Center,
            ) {
                RemoteText(
                    text = "TextSample1",
                    color = Color.White.rc,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                )
                RemoteText(
                    text =
                        "This is a long text that should wrap to multiple lines to demonstrate the multi-line capability.",
                    color = Color.LightGray.rc,
                    fontSize = 14.sp,
                    maxLines = 2,
                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                )
                RemoteText(
                    text = "Version 1.0",
                    color = Color.Cyan.rc,
                    fontSize = 10.sp,
                    fontStyle = FontStyle.Italic,
                )
            }
        }
    }
}
