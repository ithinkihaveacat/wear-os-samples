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
import com.google.example.wear_widget.PreviewWearLarge
import com.google.example.wear_widget.R
import com.google.example.wear_widget.WidgetPreview

/**
 * Android logo in a pink circle, above "Widget Catalog" text. Below is a dark gray-framed,
 * rounded-corner image of white daisies in a golden sunlit field. The overall screen background is
 * black.
 */
@RemoteComposable
@Composable
fun BitmapCanvasSample() {
    val backgroundBitmap = ImageBitmap.imageResource(id = R.drawable.photo_14).rb

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

@PreviewWearLarge
@Composable
fun BitmapCanvasSamplePreview() {
    WidgetPreview { BitmapCanvasSample() }
}
