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
@file:android.annotation.SuppressLint("RestrictedApi")

package com.google.example.wear_widget.widget

import androidx.compose.remote.creation.compose.action.ValueChange
import androidx.compose.remote.creation.compose.layout.RemoteAlignment
import androidx.compose.remote.creation.compose.layout.RemoteBox
import androidx.compose.remote.creation.compose.layout.RemoteColumn
import androidx.compose.remote.creation.compose.layout.RemoteComposable
import androidx.compose.remote.creation.compose.modifier.RemoteModifier
import androidx.compose.remote.creation.compose.modifier.fillMaxSize
import androidx.compose.remote.creation.compose.modifier.size
import androidx.compose.remote.creation.compose.state.rc
import androidx.compose.remote.creation.compose.state.rdp
import androidx.compose.remote.creation.compose.state.rememberMutableRemoteInt
import androidx.compose.remote.creation.compose.state.ri
import androidx.compose.remote.creation.compose.state.rs
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.wear.compose.remote.material3.RemoteButton
import androidx.wear.compose.remote.material3.RemoteButtonColors
import androidx.wear.compose.remote.material3.RemoteMaterialTheme
import androidx.wear.compose.remote.material3.RemoteText as MaterialRemoteText
import com.google.example.wear_widget.PreviewWearLarge
import com.google.example.wear_widget.WidgetPreview

/**
 * Android Widget Catalog screen on a dark background. A pink Android icon and "Widget Catalog"
 * title are at the top. A dark gray rounded rectangle below contains "System Theme" in white text,
 * followed by two light pink rounded buttons with dark red text: "Primary Button" and "Secondary
 * Button".
 */
@RemoteComposable
@Composable
fun SystemThemeComparisonSample() {
    val dummy = rememberMutableRemoteInt(0)
    RemoteBox(
        modifier = RemoteModifier.fillMaxSize(),
        contentAlignment = RemoteAlignment.Center,
    ) {
        RemoteColumn(horizontalAlignment = RemoteAlignment.CenterHorizontally) {
            MaterialRemoteText("System Theme".rs)
            RemoteBox(RemoteModifier.size(10.rdp))
            RemoteButton(onClick = ValueChange(dummy, 0.ri)) {
                MaterialRemoteText("Primary Button".rs)
            }
            RemoteBox(RemoteModifier.size(10.rdp))
            RemoteButton(
                onClick = ValueChange(dummy, 0.ri),
                colors =
                    RemoteButtonColors(
                        containerColor = RemoteMaterialTheme.colorScheme.secondary,
                        contentColor = RemoteMaterialTheme.colorScheme.onSecondary,
                        secondaryContentColor = RemoteMaterialTheme.colorScheme.onSecondary,
                        iconColor = RemoteMaterialTheme.colorScheme.onSecondary,
                        disabledContainerColor = Color.Gray.rc,
                        disabledContentColor = Color.LightGray.rc,
                        disabledSecondaryContentColor = Color.LightGray.rc,
                        disabledIconColor = Color.LightGray.rc,
                    ),
            ) {
                MaterialRemoteText("Secondary Button".rs)
            }
        }
    }
}

@PreviewWearLarge
@Composable
fun SystemThemeComparisonSamplePreview() {
    WidgetPreview { SystemThemeComparisonSample() }
}
