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

import androidx.compose.remote.creation.compose.layout.RemoteAlignment
import androidx.compose.remote.creation.compose.layout.RemoteArrangement
import androidx.compose.remote.creation.compose.layout.RemoteBox
import androidx.compose.remote.creation.compose.layout.RemoteColumn
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
import com.google.example.wear_widget.PreviewWearLarge
import com.google.example.wear_widget.WidgetPreview

/**
 * A screen titled 'Widget Catalog' below a pink Android icon. A dark gray rounded rectangle displays
 * a 2x2 grid of smaller colored rectangles: red (1), blue (2), green (3), and yellow (4), numbered
 * top-left to bottom-right.
 */
@RemoteComposable
@Composable
fun GridSample1() {
    RemoteBox(
        modifier = RemoteModifier.fillMaxSize(),
        contentAlignment = RemoteAlignment.Center,
    ) {
        RemoteColumn(
            modifier = RemoteModifier.fillMaxSize().padding(20.rdp),
            verticalArrangement = RemoteArrangement.SpaceEvenly,
        ) {
            RemoteRow(modifier = RemoteModifier.weight(1f)) {
                RemoteBox(
                    modifier = RemoteModifier.weight(1f).fillMaxSize().background(Color.Red),
                    contentAlignment = RemoteAlignment.Center,
                ) {
                    RemoteText("1", color = Color.White.rc)
                }
                RemoteBox(
                    modifier = RemoteModifier.weight(1f).fillMaxSize().background(Color.Blue),
                    contentAlignment = RemoteAlignment.Center,
                ) {
                    RemoteText("2", color = Color.White.rc)
                }
            }
            RemoteRow(modifier = RemoteModifier.weight(1f)) {
                RemoteBox(
                    modifier = RemoteModifier.weight(1f).fillMaxSize().background(Color.Green),
                    contentAlignment = RemoteAlignment.Center,
                ) {
                    RemoteText("3", color = Color.Black.rc)
                }
                RemoteBox(
                    modifier = RemoteModifier.weight(1f).fillMaxSize().background(Color.Yellow),
                    contentAlignment = RemoteAlignment.Center,
                ) {
                    RemoteText("4", color = Color.Black.rc)
                }
            }
        }
    }
}

@PreviewWearLarge
@Composable
fun GridSample1Preview() {
    WidgetPreview { GridSample1() }
}
