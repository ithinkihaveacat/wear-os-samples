@file:SuppressLint("RestrictedApi")

package com.google.example.wear_widget.widget

import android.annotation.SuppressLint

import androidx.compose.remote.creation.compose.layout.RemoteAlignment
import androidx.compose.remote.creation.compose.layout.RemoteArrangement
import androidx.compose.remote.creation.compose.layout.RemoteBox
import androidx.compose.remote.creation.compose.layout.RemoteCanvas
import androidx.compose.remote.creation.compose.layout.RemoteComposable
import androidx.compose.remote.creation.compose.layout.translate
import androidx.compose.remote.creation.compose.modifier.RemoteModifier
import androidx.compose.remote.creation.compose.modifier.fillMaxSize
import androidx.compose.remote.creation.compose.state.RemotePaint
import androidx.compose.remote.creation.compose.state.rc
import androidx.compose.remote.creation.compose.state.rf
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.wear.compose.remote.material3.RemoteMaterialTheme

/**
 * A dark screen shows a white circle with a grey Android robot icon at the top center. Below it,
 * white text reads "Wear Widget." Centered below the text is a large, dark grey rounded rectangle
 * containing a bright yellow equilateral triangle.
 */
@RemoteComposable
@Composable
fun CanvasSample2() {
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

                // Draw a triangle path
                val path =
                    Path().apply {
                        moveTo(0f, -50f)
                        lineTo(50f, 50f)
                        lineTo(-50f, 50f)
                        close()
                    }

                translate(centerX, centerY) {
                    drawPath(
                        path = path,
                        paint = RemotePaint().apply { remoteColor = Color.Yellow.rc },
                    )
                }
            }
        }
    }
}
