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
import com.google.example.wear_widget.PreviewWearLarge
import com.google.example.wear_widget.WidgetPreview


import androidx.compose.remote.creation.compose.layout.RemoteAlignment
import androidx.compose.remote.creation.compose.layout.RemoteBox
import androidx.compose.remote.creation.compose.layout.RemoteComposable
import androidx.compose.remote.creation.compose.layout.RemoteOffset
import androidx.compose.remote.creation.compose.modifier.RemoteModifier
import androidx.compose.remote.creation.compose.modifier.background
import androidx.compose.remote.creation.compose.modifier.fillMaxSize
import androidx.compose.remote.creation.compose.shaders.RemoteLinearGradient
import androidx.compose.remote.creation.compose.state.rc
import androidx.compose.remote.creation.compose.state.rs
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.wear.compose.remote.material3.RemoteText as MaterialRemoteText

/**
 * Android icon and "Wear Widget" text at the top. Below, a dark-grey-bordered rectangle with
 * rounded corners contains a red (top-left) to blue (bottom-right) gradient background. White text
 * "Gradient Background" is centered within the rectangle.
 */
@RemoteComposable
@Composable
fun GradientBackgroundSample() {
    val gradient =
        RemoteLinearGradient(
            colors = listOf(Color.Red.rc, Color.Blue.rc),
            start = RemoteOffset.Zero,
            end = null,
        )
    RemoteBox(
        modifier = RemoteModifier.fillMaxSize().background(brush = gradient),
        contentAlignment = RemoteAlignment.Center,
    ) {
        MaterialRemoteText("Gradient Background".rs)
    }
}


@PreviewWearLarge
@Composable
fun GradientBackgroundSamplePreview() {
    WidgetPreview {
        GradientBackgroundSample()
    }
}
