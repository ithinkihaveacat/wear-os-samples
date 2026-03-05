@file:SuppressLint("RestrictedApi")

package com.google.example.wear_widget.widget

import android.annotation.SuppressLint
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
import androidx.compose.remote.creation.compose.state.rc
import androidx.compose.remote.creation.compose.state.rb
import androidx.compose.remote.creation.compose.state.rs
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.wear.compose.remote.material3.RemoteMaterialTheme
import androidx.wear.compose.remote.material3.RemoteText as MaterialRemoteText
import com.google.example.wear_widget.R

@RemoteComposable
@Composable
fun BackgroundTreatmentsSample() {
    RemoteMaterialTheme {
        RemoteColumn(
            modifier = RemoteModifier.fillMaxSize(),
            horizontalAlignment = RemoteAlignment.CenterHorizontally,
            verticalArrangement = RemoteArrangement.Center
        ) {
            // Row 1: Vertical and Radial Gradients
            RemoteRow(
                modifier = RemoteModifier.weight(1f).fillMaxSize(),
                horizontalArrangement = RemoteArrangement.CenterHorizontally
            ) {
                // Vertical Gradient
                val vGradient = RemoteBrush.verticalGradient(
                    colors = listOf(Color.Red.rc, Color.Yellow.rc)
                )
                RemoteBox(
                    modifier = RemoteModifier.weight(1f).fillMaxSize().background(brush = vGradient),
                    horizontalAlignment = RemoteAlignment.CenterHorizontally,
                    verticalArrangement = RemoteArrangement.Center
                ) {
                    MaterialRemoteText("Vertical".rs)
                }

                // Radial Gradient
                val rGradient = RemoteBrush.radialGradient(
                    colors = listOf(Color.Blue.rc, Color.Cyan.rc)
                )
                RemoteBox(
                    modifier = RemoteModifier.weight(1f).fillMaxSize().background(brush = rGradient),
                    horizontalAlignment = RemoteAlignment.CenterHorizontally,
                    verticalArrangement = RemoteArrangement.Center
                ) {
                    MaterialRemoteText("Radial".rs)
                }
            }

            // Row 2: Sweep Gradient and Bitmap Background
            RemoteRow(
                modifier = RemoteModifier.weight(1f).fillMaxSize(),
                horizontalArrangement = RemoteArrangement.CenterHorizontally
            ) {
                // Sweep Gradient
                val sGradient = RemoteBrush.sweepGradient(
                    colors = listOf(Color.Green.rc, Color.Magenta.rc, Color.Green.rc)
                )
                RemoteBox(
                    modifier = RemoteModifier.weight(1f).fillMaxSize().background(brush = sGradient),
                    horizontalAlignment = RemoteAlignment.CenterHorizontally,
                    verticalArrangement = RemoteArrangement.Center
                ) {
                    MaterialRemoteText("Sweep".rs)
                }

                // Bitmap Background (using background(RemotePainter))
                val bitmap = ImageBitmap.imageResource(id = R.drawable.photo_17).rb
                val painter = painterRemoteBitmap(bitmap)
                RemoteBox(
                    modifier = RemoteModifier.weight(1f).fillMaxSize().background(painter),
                    horizontalAlignment = RemoteAlignment.CenterHorizontally,
                    verticalArrangement = RemoteArrangement.Center
                ) {
                    // Semi-transparent overlay to make text readable
                    RemoteBox(
                        modifier = RemoteModifier.fillMaxSize().background(Color.Black.copy(alpha = 0.5f))
                    )
                    MaterialRemoteText("Bitmap".rs)
                }
            }

            // Row 3: Custom Drawing and Horizontal Gradient
            RemoteRow(
                modifier = RemoteModifier.weight(1f).fillMaxSize(),
                horizontalArrangement = RemoteArrangement.CenterHorizontally
            ) {
                // Custom drawing
                RemoteBox(
                    modifier = RemoteModifier.weight(1f).fillMaxSize().drawWithContent {
                        // Custom background: orange circle
                        drawCircle(
                            paint = RemotePaint().apply { remoteColor = Color(0xFFFFA500).rc },
                            radius = (remoteWidth.min(remoteHeight) / 2.5f)
                        )
                        drawContent()
                    },
                    horizontalAlignment = RemoteAlignment.CenterHorizontally,
                    verticalArrangement = RemoteArrangement.Center
                ) {
                    MaterialRemoteText("Custom".rs)
                }

                // Horizontal Gradient
                val hGradient = RemoteBrush.horizontalGradient(
                    colors = listOf(Color.White.rc, Color.DarkGray.rc)
                )
                RemoteBox(
                    modifier = RemoteModifier.weight(1f).fillMaxSize().background(brush = hGradient),
                    horizontalAlignment = RemoteAlignment.CenterHorizontally,
                    verticalArrangement = RemoteArrangement.Center
                ) {
                    MaterialRemoteText("Horizontal".rs)
                }
            }
        }
    }
}
