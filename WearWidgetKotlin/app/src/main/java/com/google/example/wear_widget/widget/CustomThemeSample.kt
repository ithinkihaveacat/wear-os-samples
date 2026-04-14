/*
 * Copyright 2026 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.example.wear_widget.widget

import androidx.compose.remote.creation.compose.action.ValueChange
import androidx.compose.remote.creation.compose.layout.RemoteAlignment
import androidx.compose.remote.creation.compose.layout.RemoteBox
import androidx.compose.remote.creation.compose.layout.RemoteColumn
import androidx.compose.remote.creation.compose.layout.RemoteComposable
import androidx.compose.remote.creation.compose.modifier.RemoteModifier
import androidx.compose.remote.creation.compose.modifier.fillMaxSize
import androidx.compose.remote.creation.compose.modifier.size
import androidx.compose.remote.creation.compose.state.rdp
import androidx.compose.remote.creation.compose.state.rememberMutableRemoteInt
import androidx.compose.remote.creation.compose.state.ri
import androidx.compose.remote.creation.compose.state.rs
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.wear.compose.material3.ColorScheme
import androidx.wear.compose.remote.material3.RemoteButton
import androidx.wear.compose.remote.material3.RemoteColorScheme
import androidx.wear.compose.remote.material3.RemoteMaterialTheme
import androidx.wear.compose.remote.material3.RemoteText as MaterialRemoteText

/**
 * A sample demonstrating how to override the system theme with a custom color scheme using
 * RemoteColorScheme. The theme sets the primary color to White.
 */
@RemoteComposable
@Composable
fun CustomThemeSample() {
    val dummy = rememberMutableRemoteInt(0)
    // Define a custom color scheme where primary is White.
    // This acts as the fallback when dynamic theming is disabled.
    val customColorScheme =
        RemoteColorScheme(
            colorScheme =
                ColorScheme(
                    primary = Color.White,
                    onPrimary = Color.Black,
                    primaryContainer = Color.White,
                    onPrimaryContainer = Color.Black
                )
        )

    RemoteMaterialTheme(colorScheme = customColorScheme) {
        RemoteBox(
            modifier = RemoteModifier.fillMaxSize(),
            contentAlignment = RemoteAlignment.Center,
        ) {
            RemoteColumn(horizontalAlignment = RemoteAlignment.CenterHorizontally) {
                MaterialRemoteText("Custom Theme".rs)
                RemoteBox(RemoteModifier.size(10.rdp))

                // Use the theme's colors.
                // If dynamic theming is ON, this will use system colors.
                // If dynamic theming is OFF, this will use our customColorScheme (White/Black).
                RemoteButton(onClick = ValueChange(dummy, 0.ri)) {
                    MaterialRemoteText("Primary Button".rs)
                }
            }
        }
    }
}
