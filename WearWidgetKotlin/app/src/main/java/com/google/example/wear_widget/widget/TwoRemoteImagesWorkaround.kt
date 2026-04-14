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

import androidx.compose.remote.creation.compose.layout.RemoteAlignment
import androidx.compose.remote.creation.compose.layout.RemoteArrangement
import androidx.compose.remote.creation.compose.layout.RemoteBox
import androidx.compose.remote.creation.compose.layout.RemoteCanvas
import androidx.compose.remote.creation.compose.layout.RemoteComposable
import androidx.compose.remote.creation.compose.layout.RemoteRow
import androidx.compose.remote.creation.compose.layout.RemoteSize
import androidx.compose.remote.creation.compose.modifier.RemoteModifier
import androidx.compose.remote.creation.compose.modifier.background
import androidx.compose.remote.creation.compose.modifier.fillMaxSize
import androidx.compose.remote.creation.compose.modifier.size
import androidx.compose.remote.creation.compose.state.rb
import androidx.compose.remote.creation.compose.state.rdp
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import com.google.example.wear_widget.PreviewWearLarge
import com.google.example.wear_widget.R
import com.google.example.wear_widget.WidgetPreview

/**
 * A minimal reproduction sample for RemoteImage bitmap collision. Displays two different images
 * side-by-side. Expected: Left = Landscape, Right = Person. Actual (Bug): Both may show Person, or
 * one is invisible.
 */
/** Workaround for RemoteImage bug using RemoteCanvas. */
@RemoteComposable
@Composable
fun CanvasRemoteImage(
    bitmap: ImageBitmap,
    contentDescription: String,
    modifier: RemoteModifier = RemoteModifier,
    contentScale: ContentScale = ContentScale.Fit
) {
    val remoteBitmap = bitmap.rb
    RemoteCanvas(modifier = modifier) {
        drawScaledBitmap(
            image = remoteBitmap,
            dstSize = RemoteSize(remote.component.width, remote.component.height),
            scaleType = contentScale,
            contentDescription = contentDescription,
        )
    }
}

/**
 * Workaround for RemoteImage bug using RemoteCanvas. Displays two different images side-by-side
 * using CanvasRemoteImage. Expected: Left = Landscape, Right = Person.
 */
@RemoteComposable
@Composable
fun TwoRemoteImagesWorkaround() {
    RemoteBox(
        modifier = RemoteModifier.fillMaxSize().background(Color.Black),
        contentAlignment = RemoteAlignment.Center,
    ) {
        RemoteRow(
            verticalAlignment = RemoteAlignment.CenterVertically,
            horizontalArrangement = RemoteArrangement.Center,
        ) {
            // Image 1: Landscape
            CanvasRemoteImage(
                bitmap = ImageBitmap.imageResource(id = R.drawable.photo_14),
                contentDescription = "Landscape",
                modifier = RemoteModifier.size(35.rdp),
            )

            RemoteBox(RemoteModifier.size(10.rdp))

            // Image 2: Person
            CanvasRemoteImage(
                bitmap = ImageBitmap.imageResource(id = R.drawable.photo_17),
                contentDescription = "Person",
                modifier = RemoteModifier.size(35.rdp),
            )
        }
    }
}

@PreviewWearLarge
@Composable
fun TwoRemoteImagesWorkaroundPreview() {
    WidgetPreview { TwoRemoteImagesWorkaround() }
}
