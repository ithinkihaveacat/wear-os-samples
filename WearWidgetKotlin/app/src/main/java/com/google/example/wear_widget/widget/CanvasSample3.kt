@file:SuppressLint("RestrictedApi")

package com.google.example.wear_widget.widget

import android.annotation.SuppressLint

import androidx.compose.remote.creation.compose.layout.RemoteAlignment
import androidx.compose.remote.creation.compose.layout.RemoteArrangement
import androidx.compose.remote.creation.compose.layout.RemoteBox
import androidx.compose.remote.creation.compose.layout.RemoteCanvas
import androidx.compose.remote.creation.compose.layout.RemoteComposable
import androidx.compose.remote.creation.compose.layout.RemoteOffset
import androidx.compose.remote.creation.compose.layout.RemoteSize
import androidx.compose.remote.creation.compose.layout.rotate
import androidx.compose.remote.creation.compose.layout.translate
import androidx.compose.remote.creation.compose.modifier.RemoteModifier
import androidx.compose.remote.creation.compose.modifier.fillMaxSize
import androidx.compose.remote.creation.compose.state.RemotePaint
import androidx.compose.remote.creation.compose.state.rc
import androidx.compose.remote.creation.compose.state.rf
import androidx.compose.remote.creation.compose.state.rs
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.wear.compose.remote.material3.RemoteMaterialTheme

/**
 * Screen shows an Android icon and "Wear Widget" text at the top on a black background. Below, a
 * dark gray rounded rectangular frame encloses a black area. Centered within, a bright green
 * diamond shape has white, horizontal text "Rotated" overlaid.
 */
@RemoteComposable
@Composable
fun CanvasSample3() {
    RemoteMaterialTheme {
        RemoteBox(
            modifier = RemoteModifier.fillMaxSize(),
            horizontalAlignment = RemoteAlignment.CenterHorizontally,
            verticalArrangement = RemoteArrangement.Center,
        ) {
            RemoteCanvas(modifier = RemoteModifier.fillMaxSize()) {
                val width = remote.component.width
                val height = remote.component.height
                val centerX = width / 2f.rf
                val centerY = height / 2f.rf

                translate(centerX, centerY) {
                    rotate(45f.rf) {
                        translate(-centerX, -centerY) {
                            drawRect(
                                paint = RemotePaint().apply { color = Color.Green.rc },
                                topLeft = RemoteOffset(centerX - 40f.rf, centerY - 40f.rf),
                                size = RemoteSize(80f.rf, 80f.rf),
                            )
                        }
                    }
                }

                // Anchored Text
                drawAnchoredText(
                    text = "Rotated".rs,
                    anchorX = centerX,
                    anchorY = centerY,
                    paint =
                        RemotePaint().apply {
                            color = Color.White.rc
                            textSize = 40f.rf
                        },
                )
            }
        }
    }
}
