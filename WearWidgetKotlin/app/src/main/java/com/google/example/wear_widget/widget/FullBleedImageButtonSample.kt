
package com.google.example.wear_widget.widget


import androidx.compose.foundation.shape.CircleShape
import androidx.compose.remote.creation.compose.action.ValueChange
import androidx.compose.remote.creation.compose.layout.RemoteAlignment
import androidx.compose.remote.creation.compose.layout.RemoteArrangement
import androidx.compose.remote.creation.compose.layout.RemoteBox
import androidx.compose.remote.creation.compose.layout.RemoteCanvas
import androidx.compose.remote.creation.compose.layout.RemoteComposable
import androidx.compose.remote.creation.compose.layout.RemoteSize
import androidx.compose.remote.creation.compose.modifier.RemoteModifier
import androidx.compose.remote.creation.compose.modifier.background
import androidx.compose.remote.creation.compose.modifier.clickable
import androidx.compose.remote.creation.compose.modifier.clip
import androidx.compose.remote.creation.compose.modifier.fillMaxSize
import androidx.compose.remote.creation.compose.modifier.size
import androidx.compose.remote.creation.compose.state.rb
import androidx.compose.remote.creation.compose.state.rc
import androidx.compose.remote.creation.compose.state.rdp
import androidx.compose.remote.creation.compose.state.rememberRemoteIntValue
import androidx.compose.remote.creation.compose.state.ri
import androidx.compose.remote.creation.compose.state.rs
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.remote.creation.compose.layout.RemoteImage
import androidx.wear.compose.remote.material3.RemoteMaterialTheme
import com.google.example.wear_widget.R

/**
 * An Android Wear logo and "Wear Widget" text are centered above a rounded rectangular widget. The
 * widget displays a background of a field with white daisies, overlaid by a circular profile
 * picture of a woman with curly brown hair and sunglasses, set against a yellow circle.
 */
@RemoteComposable
@Composable
fun FullBleedImageButtonSample() {
val backgroundBitmap = ImageBitmap.imageResource(id = R.drawable.photo_14).rb

// Toggle state for click interaction
val state = rememberRemoteIntValue { 0 }
val isToggled = state eq 1.ri

// Overlay color: Semi-transparent Black when toggled, Transparent when not
val overlayColor = isToggled.select(Color(0xAA000000).rc, Color.Transparent.rc)

    RemoteBox(modifier = RemoteModifier.fillMaxSize()) {
        // Background (Full Bleed Image) using RemoteCanvas to avoid RemoteImage conflict (b/483291287)
        RemoteCanvas(modifier = RemoteModifier.fillMaxSize()) {
            drawScaledBitmap(
                image = backgroundBitmap,
                dstSize = RemoteSize(remote.component.width, remote.component.height),
                scaleType = ContentScale.Crop,
                contentDescription = "Background",
            )
        }

        // Overlay with Floating Image Button
        RemoteBox(
            modifier = RemoteModifier.fillMaxSize(),
            contentAlignment = RemoteAlignment.Center,
        ) {
            // The floating Image Button (using imageButton pattern but made clickable)
            RemoteBox(
                modifier =
                    RemoteModifier.size(60.rdp)
                        .clip(CircleShape, DpSize(60.dp, 60.dp)) // Explicit size required (b/477860914)
                        .clickable(
                            ValueChange(state, state xor 1.ri)
                        ),
                contentAlignment = RemoteAlignment.Center,
            ) {
                RemoteImage(
                    bitmap = ImageBitmap.imageResource(id = R.drawable.photo_17),
                    contentDescription = "Avatar".rs,
                    contentScale = ContentScale.Crop,
                    modifier = RemoteModifier.fillMaxSize(),
                )

                // Overlay to indicate click state
                RemoteBox(
                    modifier = RemoteModifier.fillMaxSize().background(overlayColor)
                )
            }
        }
    }
}
