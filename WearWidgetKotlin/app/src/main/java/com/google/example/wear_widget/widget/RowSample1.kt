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
import com.google.example.wear_widget.PreviewWearLarge
import com.google.example.wear_widget.WidgetPreview


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
import androidx.compose.remote.creation.compose.state.rdp
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/**
 * A screen features an Android robot head logo and "Wear Widget" text at the top. Below, a dark
 * gray bordered rectangle serves as a widget frame. Inside, three horizontal buttons are displayed:
 * a red button with "Red" text, a green button with "Green" text, and a blue button with "Blue"
 * text. All button text is white.
 */
@RemoteComposable
@Composable
fun RowSample1() {
    RemoteBox(
        modifier = RemoteModifier.fillMaxSize(),
        contentAlignment = RemoteAlignment.Center,
    ) {
        RemoteRow(
            modifier = RemoteModifier.fillMaxSize(),
            horizontalArrangement = RemoteArrangement.Center,
            verticalAlignment = RemoteAlignment.CenterVertically,
        ) {
            RemoteBox(modifier = RemoteModifier.padding(5.rdp).background(Color.Red)) {
                RemoteText(
                    "Red",
                    color = Color.White.rc,
                    modifier = RemoteModifier.padding(5.rdp),
                )
            }
            RemoteBox(modifier = RemoteModifier.padding(5.rdp).background(Color.Green)) {
                RemoteText(
                    "Green",
                    color = Color.Black.rc,
                    modifier = RemoteModifier.padding(5.rdp),
                )
            }
            RemoteBox(modifier = RemoteModifier.padding(5.rdp).background(Color.Blue)) {
                RemoteText(
                    "Blue",
                    color = Color.White.rc,
                    modifier = RemoteModifier.padding(5.rdp),
                )
            }
        }
    }
}


@PreviewWearLarge
@Composable
fun RowSample1Preview() {
    WidgetPreview {
        RowSample1()
    }
}
