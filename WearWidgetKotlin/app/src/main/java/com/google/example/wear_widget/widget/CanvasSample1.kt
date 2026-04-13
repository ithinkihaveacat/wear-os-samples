
package com.google.example.wear_widget.widget


import androidx.compose.remote.creation.compose.layout.RemoteAlignment
import androidx.compose.remote.creation.compose.layout.RemoteArrangement
import androidx.compose.remote.creation.compose.layout.RemoteBox
import androidx.compose.remote.creation.compose.layout.RemoteCanvas
import androidx.compose.remote.creation.compose.layout.RemoteComposable
import androidx.compose.remote.creation.compose.layout.RemoteOffset
import androidx.compose.remote.creation.compose.layout.RemoteSize
import androidx.compose.remote.creation.compose.modifier.RemoteModifier
import androidx.compose.remote.creation.compose.modifier.fillMaxSize
import androidx.compose.remote.creation.compose.state.RemotePaint
import androidx.compose.remote.creation.compose.state.rc
import androidx.compose.remote.creation.compose.state.rf
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.wear.compose.remote.material3.RemoteMaterialTheme

/**
 * Android icon and text "Wear Widget" above a dark gray framed rectangle. Inside the frame, a red
 * circle is centered at the top and a blue rectangle is centered at the bottom.
 */
@RemoteComposable
@Composable
fun CanvasSample1() {
    RemoteBox(
        modifier = RemoteModifier.fillMaxSize(),
        contentAlignment = RemoteAlignment.Center,
    ) {
        RemoteCanvas(modifier = RemoteModifier.fillMaxSize()) {
            val width = remote.component.width
            val height = remote.component.height
            val centerX = width / 2f.rf
            val centerY = height / 2f.rf

            // Draw a circle
            drawCircle(
                paint = RemotePaint().apply { color = Color.Red.rc },
                radius = 50f.rf,
                center = RemoteOffset(centerX, centerY),
            )

            // Draw a rect
            drawRect(
                paint = RemotePaint().apply { color = Color.Blue.rc },
                topLeft = RemoteOffset(centerX - 100f.rf, centerY + 60f.rf),
                size = RemoteSize(200f.rf, 50f.rf),
            )
        }
    }
}
