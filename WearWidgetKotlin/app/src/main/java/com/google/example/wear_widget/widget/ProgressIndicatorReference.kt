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
import androidx.compose.remote.creation.compose.layout.RemoteComposable
import androidx.compose.remote.creation.compose.modifier.RemoteModifier
import androidx.compose.remote.creation.compose.modifier.clickable
import androidx.compose.remote.creation.compose.modifier.fillMaxSize
import androidx.compose.remote.creation.compose.modifier.size
import androidx.compose.remote.creation.compose.state.animateRemoteFloat
import androidx.compose.remote.creation.compose.state.rdp
import androidx.compose.remote.creation.compose.state.rememberMutableRemoteFloat
import androidx.compose.remote.creation.compose.state.rf
import androidx.compose.runtime.Composable
import androidx.wear.compose.remote.material3.RemoteCircularProgressIndicator
import com.google.example.wear_widget.PreviewWearLarge
import com.google.example.wear_widget.WidgetPreview

/** Displays a fixed determinate CircularProgressIndicator loaded with 75% complete values. */
@RemoteComposable
@Composable
fun CircularProgressIndicatorSample1() {
    RemoteBox(
        modifier = RemoteModifier.fillMaxSize(),
        contentAlignment = RemoteAlignment.Center,
    ) {
        RemoteCircularProgressIndicator(progress = 0.75f.rf)
    }
}

/** Displays an Indeterminate progress indicator sequence loop continuously. */
@RemoteComposable
@Composable
fun CircularProgressIndicatorSample2() {
    RemoteBox(
        modifier = RemoteModifier.fillMaxSize(),
        contentAlignment = RemoteAlignment.Center,
    ) {
        RemoteCircularProgressIndicator(progress = 0.5f.rf)
    }
}

/** Displays an animated progress cycle reacting dynamically upon click events. */
@RemoteComposable
@Composable
fun CircularProgressIndicatorSample3() {
    val progress = rememberMutableRemoteFloat { 0.25f.rf }
    val animatedProgress = animateRemoteFloat(0.25f) { progress }
    val toggleAction = ValueChange(progress, (progress + 0.25f.rf).createReference())

    RemoteBox(
        modifier = RemoteModifier.fillMaxSize(),
        contentAlignment = RemoteAlignment.Center,
    ) {
        RemoteCircularProgressIndicator(
            progress = animatedProgress,
            modifier = RemoteModifier.size(150.rdp).clickable(toggleAction)
        )
    }
}

@PreviewWearLarge
@Composable
fun CircularProgressIndicatorSample1Preview() {
    WidgetPreview { CircularProgressIndicatorSample1() }
}

@PreviewWearLarge
@Composable
fun CircularProgressIndicatorSample2Preview() {
    WidgetPreview { CircularProgressIndicatorSample2() }
}
