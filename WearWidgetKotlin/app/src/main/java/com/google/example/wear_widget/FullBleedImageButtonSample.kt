@file:SuppressLint("RestrictedApi")

package com.google.example.wear_widget

import android.annotation.SuppressLint
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.remote.creation.compose.layout.RemoteAlignment
import androidx.compose.remote.creation.compose.layout.RemoteArrangement
import androidx.compose.remote.creation.compose.layout.RemoteBox
import androidx.compose.remote.creation.compose.layout.RemoteCanvas
import androidx.compose.remote.creation.compose.layout.RemoteComposable
import androidx.compose.remote.creation.compose.layout.RemoteOffset
import androidx.compose.remote.creation.compose.layout.RemoteSize
import androidx.wear.compose.remote.material3.RemoteImage
import androidx.compose.remote.creation.compose.modifier.RemoteModifier
import androidx.compose.remote.creation.compose.modifier.clip
import androidx.compose.remote.creation.compose.modifier.clickable
import androidx.compose.remote.creation.compose.modifier.fillMaxSize
import androidx.compose.remote.creation.compose.modifier.size
import androidx.compose.remote.creation.compose.state.RemoteBitmap
import androidx.compose.remote.creation.compose.state.rdp
import androidx.compose.remote.creation.compose.action.ValueChange
import androidx.compose.remote.creation.compose.state.rememberRemoteIntValue
import androidx.compose.remote.creation.compose.state.ri
import androidx.compose.remote.creation.compose.state.rs
import androidx.compose.remote.creation.compose.state.rb
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource

@RemoteComposable
@Composable
fun FullBleedImageButtonSample() {
    val backgroundBitmap = ImageBitmap.imageResource(id = R.drawable.photo_14).rb

    RemoteBox(
        modifier = RemoteModifier.fillMaxSize()
    ) {
        // Background (Full Bleed Image) using RemoteCanvas to avoid RemoteImage conflict
        RemoteCanvas(modifier = RemoteModifier.fillMaxSize()) {
             drawScaledBitmap(
                 image = backgroundBitmap,
                 dstSize = RemoteSize(remote.component.width, remote.component.height),
                 scaleType = ContentScale.Crop,
                 contentDescription = "Background"
             )
        }

        // Overlay with Floating Image Button
        RemoteBox(
            modifier = RemoteModifier.fillMaxSize(),
            horizontalAlignment = RemoteAlignment.CenterHorizontally,
            verticalArrangement = RemoteArrangement.Center
        ) {
            // The floating Image Button (using imageButton pattern but made clickable)
            RemoteBox(
                modifier = RemoteModifier
                    .size(60.rdp)
                    .clip(RoundedCornerShape(percent = 50)) // Circle
                    .clickable(ValueChange(rememberRemoteIntValue { 0 }, 0.ri)), // Add click action if needed, currently empty
                horizontalAlignment = RemoteAlignment.CenterHorizontally,
                verticalArrangement = RemoteArrangement.Center
            ) {
                 RemoteImage(
                    bitmap = ImageBitmap.imageResource(id = R.drawable.ali),
                    contentDescription = "Avatar".rs,
                    contentScale = ContentScale.Crop,
                    modifier = RemoteModifier.fillMaxSize()
                )
            }
        }
    }
}
