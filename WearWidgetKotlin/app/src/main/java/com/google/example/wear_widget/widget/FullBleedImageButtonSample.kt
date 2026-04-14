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

import androidx.compose.remote.creation.compose.action.ValueChange
import androidx.compose.remote.creation.compose.layout.RemoteAlignment
import androidx.compose.remote.creation.compose.layout.RemoteBox
import androidx.compose.remote.creation.compose.layout.RemoteCanvas
import androidx.compose.remote.creation.compose.layout.RemoteComposable
import androidx.compose.remote.creation.compose.layout.RemoteImage
import androidx.compose.remote.creation.compose.layout.RemoteSize
import androidx.compose.remote.creation.compose.modifier.RemoteModifier
import androidx.compose.remote.creation.compose.modifier.background
import androidx.compose.remote.creation.compose.modifier.clickable
import androidx.compose.remote.creation.compose.modifier.clip
import androidx.compose.remote.creation.compose.modifier.fillMaxSize
import androidx.compose.remote.creation.compose.modifier.size
import androidx.compose.remote.creation.compose.shapes.RemoteCircleShape
import androidx.compose.remote.creation.compose.state.rb
import androidx.compose.remote.creation.compose.state.rc
import androidx.compose.remote.creation.compose.state.rdp
import androidx.compose.remote.creation.compose.state.rememberMutableRemoteInt
import androidx.compose.remote.creation.compose.state.ri
import androidx.compose.remote.creation.compose.state.rs
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import com.google.example.wear_widget.PreviewWearLarge
import com.google.example.wear_widget.R
import com.google.example.wear_widget.WidgetPreview

/**
 * Android logo in pink circle above white 'Widget Catalog' text. Below, a rounded gray-bordered
 * rectangle shows a blurry field of daisies. Centered on the field is a circular cutout of a
 * person with dreadlocks, wearing blue, against a light background.
 */
@RemoteComposable
@Composable
fun FullBleedImageButtonSample() {
    val backgroundBitmap = ImageBitmap.imageResource(id = R.drawable.photo_14).rb

    // Toggle state for click interaction
    val state = rememberMutableRemoteInt(0)
    val isToggled = state eq 1.ri

    // Overlay color: Semi-transparent Black when toggled, Transparent when not
    val overlayColor = isToggled.select(Color(0xAA000000).rc, Color.Transparent.rc)

    RemoteBox(modifier = RemoteModifier.fillMaxSize()) {
        // Background (Full Bleed Image) using RemoteCanvas to avoid RemoteImage conflict
        // (b/483291287)
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
                        .clip(RemoteCircleShape)
                        .clickable(ValueChange(state, state xor 1.ri)),
                contentAlignment = RemoteAlignment.Center,
            ) {
                RemoteImage(
                    bitmap = ImageBitmap.imageResource(id = R.drawable.photo_17),
                    contentDescription = "Avatar".rs,
                    contentScale = ContentScale.Crop,
                    modifier = RemoteModifier.fillMaxSize(),
                )

                // Overlay to indicate click state
                RemoteBox(modifier = RemoteModifier.fillMaxSize().background(overlayColor))
            }
        }
    }
}

@PreviewWearLarge
@Composable
fun FullBleedImageButtonSamplePreview() {
    WidgetPreview { FullBleedImageButtonSample() }
}
