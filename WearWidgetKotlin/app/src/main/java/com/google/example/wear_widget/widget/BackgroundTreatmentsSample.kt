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
import androidx.compose.remote.creation.compose.layout.RemoteColumn
import androidx.compose.remote.creation.compose.layout.RemoteComposable
import androidx.compose.remote.creation.compose.layout.RemoteRow
import androidx.compose.remote.creation.compose.modifier.*
import androidx.compose.remote.creation.compose.painter.painterRemoteBitmap
import androidx.compose.remote.creation.compose.shaders.RemoteBrush
import androidx.compose.remote.creation.compose.shaders.horizontalGradient
import androidx.compose.remote.creation.compose.shaders.radialGradient
import androidx.compose.remote.creation.compose.shaders.sweepGradient
import androidx.compose.remote.creation.compose.shaders.verticalGradient
import androidx.compose.remote.creation.compose.state.RemotePaint
import androidx.compose.remote.creation.compose.state.rb
import androidx.compose.remote.creation.compose.state.rc
import androidx.compose.remote.creation.compose.state.rf
import androidx.compose.remote.creation.compose.state.rs
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.wear.compose.remote.material3.RemoteText as MaterialRemoteText
import com.google.example.wear_widget.R

/**
 * A Wear OS widget displaying a 2x3 grid of different background treatments: 1. Vertical (Red to
 * Yellow), 2. Radial (Blue center to Cyan edges), 3. Sweep (Green to Magenta), 4. Bitmap (Landscape
 * photo), 5. Custom (Orange circle drawn via Canvas), 6. Horizontal (White to Gray). Each cell
 * contains a centered white text label.
 */
@RemoteComposable
@Composable
fun BackgroundTreatmentsSample() {
    RemoteColumn(
        modifier = RemoteModifier.fillMaxSize(),
        horizontalAlignment = RemoteAlignment.CenterHorizontally,
        verticalArrangement = RemoteArrangement.Center
    ) {
        // Row 1: Vertical and Radial Gradients
        RemoteRow(
            modifier = RemoteModifier.weight(1f).fillMaxSize(),
            horizontalArrangement = RemoteArrangement.Center
        ) {
            // Vertical Gradient
            val vGradient =
                RemoteBrush.verticalGradient(colors = listOf(Color.Red.rc, Color.Yellow.rc))
            RemoteBox(
                modifier = RemoteModifier.weight(1f).fillMaxSize().background(brush = vGradient),
                contentAlignment = RemoteAlignment.Center,
            ) {
                MaterialRemoteText("Vertical".rs)
            }

            // Radial Gradient
            val rGradient =
                RemoteBrush.radialGradient(colors = listOf(Color.Blue.rc, Color.Cyan.rc))
            RemoteBox(
                modifier = RemoteModifier.weight(1f).fillMaxSize().background(brush = rGradient),
                contentAlignment = RemoteAlignment.Center,
            ) {
                MaterialRemoteText("Radial".rs)
            }
        }

        // Row 2: Sweep Gradient and Bitmap Background
        RemoteRow(
            modifier = RemoteModifier.weight(1f).fillMaxSize(),
            horizontalArrangement = RemoteArrangement.Center
        ) {
            // Sweep Gradient
            val sGradient =
                RemoteBrush.sweepGradient(
                    colors = listOf(Color.Green.rc, Color.Magenta.rc, Color.Green.rc)
                )
            RemoteBox(
                modifier = RemoteModifier.weight(1f).fillMaxSize().background(brush = sGradient),
                contentAlignment = RemoteAlignment.Center,
            ) {
                MaterialRemoteText("Sweep".rs)
            }

            // Bitmap Background (using background(RemotePainter))
            val bitmap = ImageBitmap.imageResource(id = R.drawable.photo_17).rb
            val painter = painterRemoteBitmap(bitmap)
            RemoteBox(
                modifier = RemoteModifier.weight(1f).fillMaxSize().background(painter),
                contentAlignment = RemoteAlignment.Center,
            ) {
                // Semi-transparent overlay to make text readable
                RemoteBox(
                    modifier =
                        RemoteModifier.fillMaxSize().background(Color.Black.copy(alpha = 0.5f))
                )
                MaterialRemoteText("Bitmap".rs)
            }
        }

        // Row 3: Custom Drawing and Horizontal Gradient
        RemoteRow(
            modifier = RemoteModifier.weight(1f).fillMaxSize(),
            horizontalArrangement = RemoteArrangement.Center
        ) {
            // Custom drawing
            RemoteBox(
                modifier =
                    RemoteModifier.weight(1f).fillMaxSize().drawWithContent {
                        // Custom background: orange circle
                        drawCircle(
                            paint = RemotePaint().apply { color = Color(0xFFFFA500).rc },
                            radius = (width.min(height) / 2.5f.rf)
                        )
                        drawContent()
                    },
                contentAlignment = RemoteAlignment.Center,
            ) {
                MaterialRemoteText("Custom".rs)
            }

            // Horizontal Gradient
            val hGradient =
                RemoteBrush.horizontalGradient(colors = listOf(Color.White.rc, Color.DarkGray.rc))
            RemoteBox(
                modifier = RemoteModifier.weight(1f).fillMaxSize().background(brush = hGradient),
                contentAlignment = RemoteAlignment.Center,
            ) {
                MaterialRemoteText("Horizontal".rs)
            }
        }
    }
}


@PreviewWearLarge
@Composable
fun BackgroundTreatmentsSamplePreview() {
    WidgetPreview {
        BackgroundTreatmentsSample()
    }
}
