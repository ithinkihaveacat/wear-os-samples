@file:SuppressLint("RestrictedApi")

package com.google.example.wear_widget.widget

import android.annotation.SuppressLint

import androidx.compose.remote.creation.compose.layout.RemoteAlignment
import androidx.compose.remote.creation.compose.layout.RemoteArrangement
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
import androidx.wear.compose.remote.material3.RemoteMaterialTheme

/**
 * A screen with a white Android logo on a light gray circle, followed by "Wear Widget" text. Below,
 * a white rounded rectangle shows "Top Left" in red, "Center" in black, and "Bottom Right" in blue,
 * demonstrating UI placement. Black background.
 */
@RemoteComposable
@Composable
fun AnchoredTextSample() {
    RemoteMaterialTheme {
        RemoteBox(
            modifier = RemoteModifier.fillMaxSize().background(Color.White),
            horizontalAlignment = RemoteAlignment.CenterHorizontally,
            verticalArrangement = RemoteArrangement.Center,
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
}
