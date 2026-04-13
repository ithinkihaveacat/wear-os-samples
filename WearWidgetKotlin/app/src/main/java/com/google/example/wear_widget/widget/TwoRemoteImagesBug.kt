
package com.google.example.wear_widget.widget

import androidx.compose.remote.creation.compose.state.rc

import androidx.compose.remote.creation.compose.layout.RemoteAlignment
import androidx.compose.remote.creation.compose.layout.RemoteArrangement
import androidx.compose.remote.creation.compose.layout.RemoteBox
import androidx.compose.remote.creation.compose.layout.RemoteComposable
import androidx.compose.remote.creation.compose.layout.RemoteRow
import androidx.compose.remote.creation.compose.modifier.RemoteModifier
import androidx.compose.remote.creation.compose.modifier.background
import androidx.compose.remote.creation.compose.modifier.fillMaxSize
import androidx.compose.remote.creation.compose.modifier.size
import androidx.compose.remote.creation.compose.state.rdp
import androidx.compose.remote.creation.compose.state.rs
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.remote.creation.compose.layout.RemoteImage
import androidx.wear.compose.remote.material3.RemoteMaterialTheme
import com.google.example.wear_widget.R

/**
 * The original buggy sample using RemoteImage.
 * Displays two different images side-by-side.
 * Expected: Left = Landscape, Right = Person.
 * Actual (Bug): Both may show Person, or one is invisible.
 */
@RemoteComposable
@Composable
fun TwoRemoteImagesBug() {
    RemoteBox(
        modifier = RemoteModifier.fillMaxSize().background(Color.Black),
        horizontalAlignment = RemoteAlignment.CenterHorizontally,
        verticalArrangement = RemoteArrangement.Center,
    ) {
        RemoteRow(
            verticalAlignment = RemoteAlignment.CenterVertically,
            horizontalArrangement = RemoteArrangement.Center,
        ) {
            // Image 1: Landscape
            RemoteImage(
                bitmap = ImageBitmap.imageResource(id = R.drawable.photo_14),
                contentDescription = "Landscape".rs,
                modifier = RemoteModifier.size(35.rdp),
            )

            RemoteBox(RemoteModifier.size(10.rdp))

            // Image 2: Person
            RemoteImage(
                bitmap = ImageBitmap.imageResource(id = R.drawable.photo_17),
                contentDescription = "Person".rs,
                modifier = RemoteModifier.size(35.rdp),
            )
        }
    }
}
