@file:SuppressLint("RestrictedApi")

package com.google.example.wear_widget.widget

import android.annotation.SuppressLint

import androidx.compose.remote.creation.compose.action.ValueChange
import androidx.compose.remote.creation.compose.layout.RemoteAlignment
import androidx.compose.remote.creation.compose.layout.RemoteArrangement
import androidx.compose.remote.creation.compose.layout.RemoteBox
import androidx.compose.remote.creation.compose.layout.RemoteColumn
import androidx.compose.remote.creation.compose.layout.RemoteComposable
import androidx.compose.remote.creation.compose.modifier.RemoteModifier
import androidx.compose.remote.creation.compose.modifier.fillMaxSize
import androidx.compose.remote.creation.compose.modifier.size
import androidx.compose.remote.creation.compose.state.RemoteColor
import androidx.compose.remote.creation.compose.state.rc
import androidx.compose.remote.creation.compose.state.rdp
import androidx.compose.remote.creation.compose.state.rememberRemoteIntValue
import androidx.compose.remote.creation.compose.state.ri
import androidx.compose.remote.creation.compose.state.rs
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.wear.compose.remote.material3.RemoteButton
import androidx.wear.compose.remote.material3.RemoteButtonColors
import androidx.wear.compose.remote.material3.RemoteColorScheme
import androidx.wear.compose.remote.material3.RemoteMaterialTheme
import androidx.wear.compose.remote.material3.RemoteText as MaterialRemoteText

/**
 * Android icon and "Wear Widget" text above a dark gray rounded card. The card displays "Aussie
 * Theme" heading, a blue rounded button labeled "Primary (Blue)", and a red rounded button labeled
 * "Secondary (Red)".
 */
@RemoteComposable
@Composable
fun AustralianThemeSample() {
    val dummy = rememberRemoteIntValue { 0 }
    val australianColorScheme =
        object : RemoteColorScheme() {
            // Australian Flag Blue
            override val primary: RemoteColor
                @RemoteComposable @Composable get() = Color(0xFF00008B).rc

            override val onPrimary: RemoteColor
                @RemoteComposable @Composable get() = Color.White.rc

            // Australian Flag Red
            override val secondary: RemoteColor
                @RemoteComposable @Composable get() = Color(0xFFFF0000).rc

            override val onSecondary: RemoteColor
                @RemoteComposable @Composable get() = Color.White.rc

            // White stars
            override val tertiary: RemoteColor
                @RemoteComposable @Composable get() = Color.White.rc

            override val onTertiary: RemoteColor
                @RemoteComposable @Composable get() = Color.Black.rc
        }

    RemoteMaterialTheme(colorScheme = australianColorScheme) {
        RemoteBox(
            modifier = RemoteModifier.fillMaxSize(),
            horizontalAlignment = RemoteAlignment.CenterHorizontally,
            verticalArrangement = RemoteArrangement.Center,
        ) {
            RemoteColumn(horizontalAlignment = RemoteAlignment.CenterHorizontally) {
                MaterialRemoteText("Aussie Theme".rs)
                RemoteBox(RemoteModifier.size(10.rdp))
                RemoteButton(onClick = ValueChange(dummy, 0.ri)) {
                    MaterialRemoteText("Primary (Blue)".rs)
                }
                RemoteBox(RemoteModifier.size(10.rdp))
                RemoteButton(
                    onClick = ValueChange(dummy, 0.ri),
                    colors =
                        RemoteButtonColors(
                            containerColor = australianColorScheme.secondary,
                            contentColor = australianColorScheme.onSecondary,
                            secondaryContentColor = australianColorScheme.onSecondary,
                            iconColor = australianColorScheme.onSecondary,
                            disabledContainerColor = Color.Gray.rc,
                            disabledContentColor = Color.LightGray.rc,
                            disabledSecondaryContentColor = Color.LightGray.rc,
                            disabledIconColor = Color.LightGray.rc,
                        ),
                ) {
                    MaterialRemoteText("Secondary (Red)".rs)
                }
            }
        }
    }
}
