@file:SuppressLint("RestrictedApi")

package com.google.example.wear_widget.widget

import androidx.compose.remote.creation.compose.state.rc
import androidx.compose.remote.creation.compose.state.rs
import android.annotation.SuppressLint

import androidx.compose.remote.creation.compose.layout.RemoteBox
import androidx.compose.remote.creation.compose.layout.RemoteCanvas
import androidx.compose.remote.creation.compose.layout.RemoteComposable
import androidx.compose.remote.creation.compose.layout.RemoteSize
import androidx.compose.remote.creation.compose.modifier.RemoteModifier
import androidx.compose.remote.creation.compose.modifier.fillMaxSize
import androidx.compose.remote.creation.compose.state.rb
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.wear.compose.remote.material3.RemoteMaterialTheme
import com.google.example.wear_widget.R

/**
 * A central Android robot icon appears above the white text "Wear Widget". Below this, a rounded
 * rectangular image displays a soft-focus field of daisies and dry grass under warm light. The
 * entire composition is set against a black background.
 */
@RemoteComposable
@Composable
fun BitmapCanvasSample() {
    val backgroundBitmap = ImageBitmap.imageResource(id = R.drawable.photo_14).rb

    RemoteMaterialTheme {
        RemoteBox(modifier = RemoteModifier.fillMaxSize()) {
            RemoteCanvas(modifier = RemoteModifier.fillMaxSize()) {
                drawScaledBitmap(
                    image = backgroundBitmap,
                    dstSize = RemoteSize(remote.component.width, remote.component.height),
                    scaleType = ContentScale.Crop,
                    contentDescription = "Background",
                )
            }
        }
    }
}
