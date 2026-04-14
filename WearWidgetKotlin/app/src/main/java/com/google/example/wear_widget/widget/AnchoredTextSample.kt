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
import androidx.compose.remote.creation.compose.layout.RemoteBox
import androidx.compose.remote.creation.compose.layout.RemoteCanvas
import androidx.compose.remote.creation.compose.layout.RemoteComposable
import androidx.compose.remote.creation.compose.modifier.RemoteModifier
import androidx.compose.remote.creation.compose.modifier.background
import androidx.compose.remote.creation.compose.modifier.fillMaxSize
import androidx.compose.remote.creation.compose.state.RemotePaint
import androidx.compose.remote.creation.compose.state.rc
import androidx.compose.remote.creation.compose.state.rf
import androidx.compose.remote.creation.compose.state.rs
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/**
 * A screen with a white Android logo on a light gray circle, followed by "Wear Widget" text. Below,
 * a white rounded rectangle shows "Top Left" in red, "Center" in black, and "Bottom Right" in blue,
 * demonstrating UI placement. Black background.
 */
@RemoteComposable
@Composable
fun AnchoredTextSample() {
    RemoteBox(
        modifier = RemoteModifier.fillMaxSize().background(Color.White),
        contentAlignment = RemoteAlignment.Center,
    ) {
        RemoteCanvas(modifier = RemoteModifier.fillMaxSize()) {
            val width = remote.component.width
            val height = remote.component.height
            val centerX = width / 2f.rf
            val centerY = height / 2f.rf

            // 1. Center Text (pan 0 = center)
            drawAnchoredText(
                text = "Center".rs,
                anchorX = centerX,
                anchorY = centerY,
                flags = 0,
                paint =
                    RemotePaint().apply {
                        color = Color.Black.rc
                        textSize = 30f.rf
                    },
            )

            // 2. Top-Left Text
            // Anchor at absolute top-left (0,0)
            // panx = -1 (Left Align/Shift Right)
            // pany = 1 (Top Align/Shift Down - assumed based on v4 failure with -1)
            drawAnchoredText(
                text = "Top Left".rs,
                anchorX = 0f.rf,
                anchorY = 0f.rf,
                flags = 0,
                paint =
                    RemotePaint().apply {
                        color = Color.Red.rc
                        textSize = 20f.rf
                    },
            )

            // 3. Bottom-Right Text
            // Anchor at absolute bottom-right (w,h)
            // panx = 1 (Right Align/Shift Left)
            // pany = -1 (Bottom Align/Shift Up - assumed based on v4 failure with 1)
            drawAnchoredText(
                text = "Bottom Right".rs,
                anchorX = width,
                anchorY = height,
                flags = 0,
                paint =
                    RemotePaint().apply {
                        color = Color.Blue.rc
                        textSize = 20f.rf
                    },
            )
        }
    }
}


@PreviewWearLarge
@Composable
fun AnchoredTextSamplePreview() {
    WidgetPreview {
        AnchoredTextSample()
    }
}
